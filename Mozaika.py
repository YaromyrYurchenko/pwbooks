import os
import tqdm
from PIL import Image
import numpy as np
from tqdm import tqdm
import math
from concurrent.futures import ThreadPoolExecutor


TILE_SIZE = 5
TILE_DIR = "images_for_mozaika"
TARGET_IMAGE = "target.jpg"
OUTPUT_IMAGE = "mosaic_output.jpg"


def average_rgb(image):
    np_img = np.array(image)
    w, h, d = np_img.shape
    return tuple(np.mean(np_img.reshape(w * h, d), axis=0))


def load_tiles(tile_dir, tile_size):
    tile_data = []
    print("Loading and resizing tile images...")
    for filename in tqdm(os.listdir(tile_dir)):
        if filename.lower().endswith((".png", ".jpg", ".jpeg")):
            path = os.path.join(tile_dir, filename)
            try:
                tile = Image.open(path).convert("RGB")
                tile = tile.resize((tile_size, tile_size))
                avg = average_rgb(tile)
                tile_data.append((tile.copy(), avg))
            except Exception as e:
                print(f"Failed to load {filename}: {e}")
    return tile_data


def closest_tile(avg_rgb, tile_data):
    best_dist = float("inf")
    best_tile = None
    for tile, tile_avg in tile_data:
        dist = sum((a - b) ** 2 for a, b in zip(avg_rgb, tile_avg))
        if dist < best_dist:
            best_dist = dist
            best_tile = tile
    return best_tile


def process_block(x, y, target, tile_data, tile_size):
    block = target.crop((x, y, x + tile_size, y + tile_size))
    avg = average_rgb(block)
    tile = closest_tile(avg, tile_data)
    return x, y, tile


def create_mosaic(target_path, tile_dir, tile_size, output_path):
    target_img = Image.open(target_path).convert("RGB")
    width, height = target_img.size
    target_img = target_img.crop((0, 0, width - width % tile_size, height - height % tile_size))

    tile_data = load_tiles(tile_dir, tile_size)
    if not tile_data:
        print("No tile images found.")
        return

    out_img = Image.new("RGB", target_img.size)
    tasks = []
    print("Processing image blocks...")

    with ThreadPoolExecutor() as executor:
        for y in range(0, height, tile_size):
            for x in range(0, width, tile_size):
                tasks.append(executor.submit(process_block, x, y, target_img, tile_data, tile_size))

        for future in tqdm(tasks):
            x, y, tile = future.result()
            out_img.paste(tile, (x, y))

    out_img.save(output_path)
    print(f"Mosaic saved to {output_path}") 


if __name__ == "__main__":
    create_mosaic(TARGET_IMAGE, TILE_DIR, TILE_SIZE, OUTPUT_IMAGE)
