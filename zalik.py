from abc import ABC, abstractmethod

class Logger:
    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

    def log(self, message):
        print(f"[LOG]: {message}")

class Shape(ABC):
    @abstractmethod
    def draw(self):
        pass

class Circle(Shape):
    def draw(self):
        print("Rysuję koło")
        Logger().log("Circle drawn")

class Square(Shape):
    def draw(self):
        print("Rysuję kwadrat")
        Logger().log("Square drawn")

class ShapeFactory:
    @staticmethod
    def create_shape(shape_type: str) -> Shape:
        if shape_type.lower() == "circle":
            return Circle()
        elif shape_type.lower() == "square":
            return Square()
        else:
            raise ValueError(f"Nieznany kształt: {shape_type}")

def main():
    choice = input("Podaj kształt (circle/square): ")
    try:
        shape = ShapeFactory.create_shape(choice)
        shape.draw()
    except ValueError as e:
        print(e)

if __name__ == "__main__":
    main()

#Logger – klasa implementująca wzorzec Singleton. Gwarantuje, że w programie istnieje tylko jedna instancja logera. Metoda log() służy do wypisywania komunikatów.
#Shape – klasa abstrakcyjna (interfejs) dla figur. Zawiera abstrakcyjną metodę draw(), którą muszą zaimplementować klasy potomne.
#Circle i Square – klasy reprezentujące konkretne figury. Implementują metodę draw(), która wypisuje nazwę figury i wywołuje logowanie przez Logger.
#ShapeFactory – klasa implementująca wzorzec Metoda Wytwórcza (Factory Method). Na podstawie tekstu ("circle" lub "square") tworzy odpowiedni obiekt figury.
#main() – funkcja główna programu. Pobiera od użytkownika typ figury, tworzy obiekt przez ShapeFactory i wywołuje draw(), co rysuje figurę i zapisuje log.
