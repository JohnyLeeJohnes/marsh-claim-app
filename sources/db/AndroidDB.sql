CREATE DATABASE AndroidApp;
USE AndroidApp; 

-- Policy of the user--
CREATE TABLE Policy(
	ID int primary key auto_increment,
    Policy_Number int not null
);

-- Main User --
CREATE TABLE User(
	ID int primary key auto_increment,
	username varchar(50),
	pass varchar(250),
    email varchar(250),
	firstname varchar(250),
    lastname varchar(250)
);

-- User joined with Policy --
CREATE TABLE UserPolicy(
	ID int primary key auto_increment,
    User_ID int not null,
    Policy_ID int not null,
	Datum_Od date not null,
    Datum_Do date,
	FOREIGN KEY (User_ID) REFERENCES User(ID),
    FOREIGN KEY (Policy_ID) REFERENCES Policy(ID)
);

-- Available products --
CREATE TABLE Product(
	ID int primary key auto_increment,
    Product_Name varchar(20) not null,
    Product_Code varchar(4) not null
);

-- Assign product to policy --
CREATE TABLE PolicyProduct(
	ID int primary key auto_increment,
    Policy_ID int not null,
    Product_ID int not null,
    FOREIGN KEY (Policy_ID) REFERENCES Policy(ID),
    FOREIGN KEY (Product_ID) REFERENCES Product(ID)
);

-- Car Claim--
CREATE TABLE Car_Claim(
	ID int primary key auto_increment,
	Policy_ID int not null,
    Datum datetime not null,
    Place text,
    Damage_Car tinyint(1) not null,
    Damage_Car_Other tinyint(1) not null,
    Damage_Other tinyint(1) not null,
    SPZ varchar(20) not null,
    Car_Type varchar(50) not null,
    Contact_Firstname varchar(50) not null,
    Contact_Lastname varchar(50) not null,
    Contact_Email varchar(50) not null,
	Contact_Phone varchar(25),
    FOREIGN KEY (Policy_ID) REFERENCES Policy(ID)
);

-- Car Claim join to user--
CREATE TABLE Car_Accident(
	ID int primary key auto_increment,
    Car_Claim_ID int not null,
    User_ID int not null,
    FOREIGN KEY (Car_Claim_ID) REFERENCES Car_Claim(ID),
    FOREIGN KEY (User_ID) REFERENCES User(ID)
);

-- Car Claim joined with photo --
CREATE TABLE Car_Accident_Photos(
	ID int primary key auto_increment,
    Car_Claim_ID int not null,
    Media MEDIUMBLOB not null,
    FOREIGN KEY (Car_Claim_ID) REFERENCES Car_Claim(ID)
);

-- Drone insurance --
CREATE TABLE Drone_Insurance(
	ID int primary key auto_increment,
    Policy_ID int not null,
    Datum_Od date not null,
    Datum_Do date not null,
    FOREIGN KEY (Policy_ID) REFERENCES Policy(ID)
);

-- Drone insurance joined user--
CREATE TABLE Drone_Insurance_Create(
	ID int primary key auto_increment,
    User_ID int not null,
    Drone_Insurance_ID int not null,
    FOREIGN KEY (User_ID) REFERENCES User(ID),
    FOREIGN KEY (Drone_Insurance_ID) REFERENCES Drone_Insurance(ID)
);

-- Drop all tables
DROP TABLE Car_Accident_Photos;
DROP TABLE Car_Accident;
DROP TABLE Car_Claim;
DROP TABLE PolicyProduct;
DROP TABLE Product;
DROP TABLE User;
DROP TABLE Policy;
DROP TABLE Drone_Insurance;
DROP TABLE Drone_Insurance_Create;

-- SELECTS & INSERTS
SELECT CC.ID, CC.Datum, CC.Place, CC.SPZ, CC.Car_Type,CC.Contact_Firstname, CC.Contact_Lastname, CC.Contact_Email, CC.Contact_Phone, PO.Policy_Number, CC.Damage_Car, CC.Damage_Car_Other,CC.Damage_Other
FROM Car_Accident as CA 
INNER JOIN Car_Claim as CC ON CA.Car_Claim_ID = CC.ID
INNER JOIN User AS US ON CA.User_ID = US.ID
INNER JOIN Policy AS PO ON CC.Policy_ID = PO.ID
WHERE US.username = "Johny";

SELECT DI.ID,DI.Datum_Od,DI.Datum_Do,PO.Policy_Number
FROM Drone_Insurance_Create as DIS
INNER JOIN Drone_Insurance as DI ON DIS.Drone_Insurance_ID = DI.ID
INNER JOIN User AS US ON DIS.User_ID = US.ID
INNER JOIN Policy as PO on DI.Policy_ID = PO.ID
WHERE US.username = "Johny";

SELECT Media
FROM Car_Accident_Photos as CAP
WHERE CAP.Car_Claim_ID = 1;

SELECT PO.ID FROM Policy as PO
INNER JOIN PolicyProduct as PP ON PP.Policy_ID = PO.ID
WHERE PO.Policy_Number = 123456 AND PP.Product_ID = (SELECT ID FROM Product as PRO WHERE PRO.Product_Code = "DRON");

SELECT US.pass, US.email, US.firstname, US.lastname, PO.Policy_Number
FROM User as US INNER JOIN Policy as PO ON US.Policy_ID = PO.ID
WHERE username = "Johny";

-- Init test data
INSERT INTO `AndroidApp`.`Product` (`Product_Name`, `Product_Code`) VALUES ('Povinné ručení', 'POV');
INSERT INTO `AndroidApp`.`Product` (`Product_Name`, `Product_Code`) VALUES ('Drony', 'DRON');
INSERT INTO `AndroidApp`.`Policy` (`Policy_Number`, `Datum_Od`, `Datum_Do`) VALUES ('123456', '2019-02-22', '2020-02-22');
INSERT INTO `AndroidApp`.`PolicyProduct` (`Policy_ID`, `Product_ID`) VALUES ('1', '1');
INSERT INTO `AndroidApp`.`PolicyProduct` (`Policy_ID`, `Product_ID`) VALUES ('1', '2');
INSERT INTO User(username,pass,email,firstname,lastname,Policy_ID) 
VALUES ("Johny", "$2y$10$LO80UboT8kUYimBifkNsgOv4UfeWIQglQ3hH9lGuseAKwc3wBXOmS","jan.pavlat@marsh.com","Jan","Pavlat",1);
INSERT INTO Car_Accident_Photos(Car_Claim_ID, Media) VALUES ((SELECT MAX(ID) FROM Car_Claim),"asd");
INSERT INTO Drone_Insurance_Create(Drone_Insurance_ID, User_ID) VALUES ((SELECT MAX(ID) FROM Drone_Insurance), (SELECT ID FROM User WHERE username = "Johny"));



