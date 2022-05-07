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