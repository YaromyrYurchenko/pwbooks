from abc import ABC, abstractmethod  # Импорт для создания абстрактных классов и методов

class Logger:  # Шаблон Singleton — только один объект логгера в программе
    _instance = None  # Приватная переменная для хранения экземпляра

    def __new__(cls):  # Метод вызывается при создании объекта
        if cls._instance is None:  # Если еще нет экземпляра
            cls._instance = super().__new__(cls)  # Создаем новый экземпляр
        return cls._instance  # Возвращаем существующий экземпляр

    def log(self, message):  # Метод логирования
        print(f"[LOG]: {message}")  # Печатает сообщение с префиксом [LOG]

class Shape(ABC):  # Абстрактный базовый класс для фигур
    @abstractmethod
    def draw(self):  # Абстрактный метод, должен быть реализован в подклассах
        pass

class Circle(Shape):  # Класс круга
    def draw(self):  # Реализация метода draw
        print("Rysuję koło")  # Вывод сообщения
        Logger().log("Circle drawn")  # Логирование через Singleton

class Square(Shape):  # Класс квадрата
    def draw(self):  # Реализация метода draw
        print("Rysuję kwadrat")  # Вывод сообщения
        Logger().log("Square drawn")  # Логирование через Singleton

class ShapeFactory:  # Фабрика — реализация шаблона Factory Method
    @staticmethod
    def create_shape(shape_type: str) -> Shape:  # Метод создает нужную фигуру
        if shape_type.lower() == "circle":  # Если введено circle
            return Circle()  # Вернуть объект круга
        elif shape_type.lower() == "square":  # Если square
            return Square()  # Вернуть объект квадрата
        else:
            raise ValueError(f"Nieznany kształt: {shape_type}")  # Ошибка, если неизвестная фигура

def main():  # Главная функция программы
    choice = input("Podaj kształt (circle/square): ")  # Ввод фигуры от пользователя
    try:
        shape = ShapeFactory.create_shape(choice)  # Создание фигуры через фабрику
        shape.draw()  # Вызов метода draw (рисование и лог)
    except ValueError as e:
        print(e)  # Обработка ошибки, если форма не распознана

if __name__ == "__main__":  # Запуск main, если это основной файл
    main()
#Logger — класс, реализующий шаблон Singleton. Гарантирует, что в программе существует только один экземпляр логгера. Метод log() используется для вывода сообщений.
#Shape — абстрактный класс (интерфейс) для фигур. Содержит абстрактный метод draw(), который обязаны реализовать подклассы.
#Circle и Square — классы, представляющие конкретные фигуры. Реализуют метод draw(), который выводит сообщение с названием фигуры и вызывает логирование через Logger.
#ShapeFactory — класс, реализующий шаблон Фабричный метод (Factory Method). На основе введённого текста ("circle" или "square") создаёт соответствующий объект фигуры.
#main() — главная функция программы. Запрашивает у пользователя тип фигуры, создаёт объект через ShapeFactory и вызывает метод draw(), который рисует фигуру и записывает лог.
