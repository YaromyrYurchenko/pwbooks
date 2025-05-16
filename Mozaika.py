import os  # Для работы с файловой системой (получение списка файлов и путей)
from PIL import Image  # Работа с изображениями (открытие, изменение размера, копирование)
import numpy as np  # Работа с массивами и вычислениями (расчет среднего цвета)
from concurrent.futures import ThreadPoolExecutor  # Многопоточность для ускорения обработки

# Размер блока (плитки), на который делится исходное изображение
GRID_SIZE = 5

# Папка с изображениями, которые будут использоваться как плитки
SOURCE_FOLDER = "images_for_mozaika"

# Исходное изображение, которое мы превращаем в мозаику
BASE_IMAGE = "target.jpg"

# Имя выходного файла с готовой мозаикой
RESULT_IMAGE = "mosaic_result.jpg"

# Функция вычисляет средний цвет изображения
def average_color(img):
    return tuple(np.array(img).reshape(-1, 3).mean(axis=0))  # Преобразуем изображение в массив, выравниваем и считаем среднее по каналам

# Загрузка всех плиток из папки и подготовка их среднего цвета
def load_tiles(folder, size):
    tiles = []  # Список для хранения кортежей (плитка, её средний цвет)
    for name in os.listdir(folder):  # Проходимся по всем файлам в папке
        if name.lower().endswith(('.jpg', '.jpeg', '.png')):  # Проверяем, что файл — это изображение
            try:
                img = Image.open(os.path.join(folder, name)).convert("RGB").resize((size, size))  # Открываем, приводим к RGB и уменьшаем до размера блока
                tiles.append((img.copy(), average_color(img)))  # Сохраняем копию изображения и его средний цвет
            except Exception:
                pass  # Игнорируем ошибки (например, битые изображения)
    return tiles  # Возвращаем список подготовленных плиток

# Выбор плитки, средний цвет которой ближе всего к среднему цвету блока
def best_match(avg, tiles):
    return min(tiles, key=lambda t: sum((a - b) ** 2 for a, b in zip(avg, t[1])))[0]  # Находим плитку с минимальной "дистанцией" по цвету

# Обработка одного блока исходного изображения: находит и возвращает подходящую плитку
def process_block(x, y, base, size, tiles):
    block = base.crop((x, y, x + size, y + size))  # Вырезаем блок из исходного изображения
    avg = average_color(block)  # Считаем его средний цвет
    return x, y, best_match(avg, tiles)  # Возвращаем координаты и подходящую плитку

# Основная функция для создания мозаики
def create_mosaic(base_path, tile_folder, block_size, output_path):
    base = Image.open(base_path).convert("RGB")  # Загружаем исходное изображение и переводим в RGB
    w, h = base.size  # Получаем размеры изображения
    base = base.crop((0, 0, w - w % block_size, h - h % block_size))  # Обрезаем изображение, чтобы оно делилось на блоки без остатка

    tiles = load_tiles(tile_folder, block_size)  # Загружаем и подготавливаем плитки
    if not tiles:  # Если плитки не загружены (например, папка пустая или файлы некорректные)
        print("No tiles found.")  # Сообщаем об этом
        return  # Выходим из функции

    result = Image.new("RGB", base.size)  # Создаем новое пустое изображение под мозаику

    # Параллельно обрабатываем все блоки изображения
    with ThreadPoolExecutor() as executor:
        futures = [  # Создаем список задач на обработку блоков
            executor.submit(process_block, x, y, base, block_size, tiles)  # Подаём координаты и изображение в поток
            for y in range(0, h, block_size)  # По вертикали
            for x in range(0, w, block_size)  # По горизонтали
        ]
        for f in futures:  # Как только блок обработан
            x, y, tile = f.result()  # Получаем координаты и выбранную плитку
            result.paste(tile, (x, y))  # Вставляем плитку в итоговое изображение

    result.save(output_path)  # Сохраняем итоговое изображение в файл
    print(f"Mosaic saved to {output_path}")  # Уведомляем, что всё готово

# Запуск функции при запуске скрипта напрямую
if __name__ == "__main__":
    create_mosaic(BASE_IMAGE, SOURCE_FOLDER, GRID_SIZE, RESULT_IMAGE)  # Стартуем процесс создания мозаики
