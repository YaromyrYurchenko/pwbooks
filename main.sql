
INSERT INTO Authors (Surename, Name, Biography) VALUES 
('Smith', 'John', 'John Smith is a prolific writer known for his mystery novels.'),
('Johnson', 'Emily', 'Emily Johnson is an award-winning author specializing in historical fiction.'),
('Garcia', 'David', 'David Garcia is a renowned poet and essayist.');

INSERT INTO Publishing_houses (Name) VALUES 
('Penguin Random House'),
('HarperCollins'),
('Simon & Schuster');

INSERT INTO Categories (Name) VALUES 
('Mystery'),
('Historical Fiction'),
('Poetry');

INSERT INTO Books (Author_ID, Description, Title, Publishing_year, Publishing_house_ID, Category_ID, Review_ID, Numbers_of_books) VALUES
(1, 'A gripping mystery set in the heart of London.', 'The Lost Key', 2018, 1, 1, 1, 10),
(2, 'A tale of love and betrayal during the American Revolution.', 'The Secret Garden', 2015, 2, 2, 2, 8),
(3, 'A collection of evocative poems exploring the human condition.', 'Echoes of Eternity', 2020, 3, 3, 3, 12);

INSERT INTO Providers (Company_name, Adres, Phone_number) VALUES 
('Book Distributors Inc.', '123 Main Street, Anytown, USA', '123-456-7890'),
('Global Books', '456 Elm Street, Othertown, USA', '987-654-3210');

INSERT INTO Locations (City, Street, Number, Post_code) VALUES 
('Anytown', 'Broadway', '1A', '12345'),
('Othertown', 'Oak Street', '22B', '54321');

INSERT INTO Libraries (Name, Location_ID, Open_hour_ID, Employee_ID) VALUES 
('Central Library', 1, 1, 1),
('Community Library', 2, 2, 2);

INSERT INTO Elementary_books (Book_ID, Library_ID, Status, Wear) VALUES 
(1, 1, 'Available', 5),
(2, 2, 'Available', 3),
(3, 1, 'Available', 8);

INSERT INTO Users (Name, Email, Phone_number, Status, Indeks) VALUES 
('Alice Johnson', 'alice@example.com', '555-1234', 'Active', 12345),
('Bob Smith', 'bob@example.com', '555-5678', 'Active', 67890);

INSERT INTO Orders (Elementary_book_ID, User_ID, Date, Pickup_time) VALUES 
(1, 1, '2024-03-28', 14),
(2, 2, '2024-03-29', 15);



INSERT INTO Rents (User_ID, Elementary_book_ID, Rent_date, Return_date, Rental_time, Order_ID, Employee_ID) VALUES 
(1, 1, '2024-03-28', '2024-04-04', 7, 1, 1),
(2, 2, '2024-03-29', '2024-04-05', 7, 2, 2);
