import os
from PIL import Image
import numpy as np
from concurrent.futures import ThreadPoolExecutor

GRID_SIZE = 5
SOURCE_FOLDER = "images_for_mozaika"
BASE_IMAGE = "target.jpg"
RESULT_IMAGE = "mosaic_result.jpg"

def get_average_color(img):
    array = np.array(img)
    shape = array.shape
    return tuple(np.mean(array.reshape(-1, shape[2]), axis=0))

def prepare_tiles(folder, size):
    collection = []
    print("Reading tiles...")
    for file in os.listdir(folder):
        if file.lower().endswith(('.jpg', '.png', '.jpeg')):
            try:
                image = Image.open(os.path.join(folder, file)).convert("RGB")
                resized = image.resize((size, size))
                avg_color = get_average_color(resized)
                collection.append((resized.copy(), avg_color))
            except Exception as error:
                print(f"Skipped {file}: {error}")
    return collection

def select_best_tile(block_avg, tiles):
    closest = float('inf')
    selected = None
    for tile_img, tile_avg in tiles:
        distance = sum((a - b) ** 2 for a, b in zip(block_avg, tile_avg))
        if distance < closest:
            closest = distance
            selected = tile_img
    return selected

def analyze_block(px, py, img, tiles, block_size):
    crop_area = img.crop((px, py, px + block_size, py + block_size))
    block_avg = get_average_color(crop_area)
    best_match = select_best_tile(block_avg, tiles)
    return px, py, best_match

def build_mosaic(base_path, tile_folder, block_size, output_file):
    base = Image.open(base_path).convert("RGB")
    w, h = base.size
    base = base.crop((0, 0, w - w % block_size, h - h % block_size))

    tiles = prepare_tiles(tile_folder, block_size)
    if not tiles:
        print("No valid tile images found.")
        return

    result = Image.new("RGB", base.size)
    positions = []

    print("Composing mosaic...")

    with ThreadPoolExecutor() as workers:
        for top in range(0, h, block_size):
            for left in range(0, w, block_size):
                positions.append(workers.submit(analyze_block, left, top, base, tiles, block_size))

        for job in positions:
            x, y, img = job.result()
            result.paste(img, (x, y))

    result.save(output_file)
    print(f"Done! Mosaic saved to: {output_file}")

if __name__ == "__main__":
    build_mosaic(BASE_IMAGE, SOURCE_FOLDER, GRID_SIZE, RESULT_IMAGE)
