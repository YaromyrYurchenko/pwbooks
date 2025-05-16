import os
from PIL import Image
import numpy as np
from concurrent.futures import ThreadPoolExecutor

GRID_SIZE = 5
SOURCE_FOLDER = "images_for_mozaika"
BASE_IMAGE = "target.jpg"
RESULT_IMAGE = "mosaic_result.jpg"

def average_color(img):
    return tuple(np.array(img).reshape(-1, 3).mean(axis=0))

def load_tiles(folder, size):
    tiles = []
    for name in os.listdir(folder):
        if name.lower().endswith(('.jpg', '.jpeg', '.png')):
            try:
                img = Image.open(os.path.join(folder, name)).convert("RGB").resize((size, size))
                tiles.append((img.copy(), average_color(img)))
            except Exception:
                pass
    return tiles

def best_match(avg, tiles):
    return min(tiles, key=lambda t: sum((a - b) ** 2 for a, b in zip(avg, t[1])))[0]

def process_block(x, y, base, size, tiles):
    block = base.crop((x, y, x + size, y + size))
    avg = average_color(block)
    return x, y, best_match(avg, tiles)

def create_mosaic(base_path, tile_folder, block_size, output_path):
    base = Image.open(base_path).convert("RGB")
    w, h = base.size
    base = base.crop((0, 0, w - w % block_size, h - h % block_size))

    tiles = load_tiles(tile_folder, block_size)
    if not tiles:
        print("No tiles found.")
        return

    result = Image.new("RGB", base.size)

    with ThreadPoolExecutor() as executor:
        futures = [executor.submit(process_block, x, y, base, block_size, tiles)
                   for y in range(0, h, block_size)
                   for x in range(0, w, block_size)]
        for f in futures:
            x, y, tile = f.result()
            result.paste(tile, (x, y))

    result.save(output_path)
    print(f"Mosaic saved to {output_path}")

if __name__ == "__main__":
    create_mosaic(BASE_IMAGE, SOURCE_FOLDER, GRID_SIZE, RESULT_IMAGE)
