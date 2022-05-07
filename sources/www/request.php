<?php

//Turn on error reporting
error_reporting(E_ERROR | E_PARSE);

//Init action
if (isset($_POST["action"])) {
    $action = $_POST["action"];
}

//DB connection info
$servername = "localhost";
$username = "root";
$password = "";
$database = "AndroidApp";

//Open connection
$conn = new mysqli($servername, $username, $password, $database);
if ($conn->connect_errno) {
    echo $conn->connect_error;
    exit;
}
$conn->set_charset("utf8");

//Switch for action
switch ($action) {

    case "Login":
        //Init params
        $username = $_POST['username'];
        $password = $_POST['password'];

        //Check if all values exists
        if (checkParamEmpty($username) && checkParamEmpty($password)) {
            echo json_encode(array("response" => "wrong_params"));
            break;
        }

        //SQL statement
        $sql = "SELECT US.pass, US.email, US.firstname, US.lastname, PO.Policy_Number
                FROM User as US LEFT JOIN UserPolicy as UP on UP.User_ID = US.ID
                LEFT JOIN Policy as PO ON UP.Policy_ID = PO.ID
                WHERE username = ?";
        $statement = $conn->prepare($sql);
        $statement->bind_param("s", $username);
        $statement->execute();

        //Bind Result
        $passHash = "";
        $email = "";
        $firstname = "";
        $lastname = "";
        $policyNumber = "";
        $statement->bind_result($passHash, $email, $firstname, $lastname, $policyNumber);

        //Check password hash
        if ($statement->fetch()) {
            if (password_verify($password, $passHash)) {
                echo json_encode(
                    array(
                        "response" => "success",
                        "data" => array(
                            "email" => $email,
                            "firstname" => $firstname,
                            "lastname" => $lastname,
                            "passwordHash" => $passHash,
                            "policyNumber" => $policyNumber != null ? $policyNumber : 0,
                        ),
                    )
                );
            } else {
                echo json_encode(array("response" => "failure"));
            }
        } else {
            echo json_encode(array("response" => "failure"));
        }
        break;

    case "Register":
        //Init params
        $firstname = $_POST['firstname'];
        $lastname = $_POST['lastname'];
        $email = $_POST['email'];
        $username = $_POST['username'];
        $password = $_POST['password'];

        //Check if all params exists
        if (checkParamEmpty($firstname) && checkParamEmpty($lastname) && checkParamEmpty($email) && checkParamEmpty($username) && checkParamEmpty($password)) {
            echo json_encode(array("response" => "wrong_params"));
            break;
        }

        //SQL statement - get user from DB
        $sql = "SELECT * FROM User WHERE username = ? OR email = ?";
        $statement = $conn->prepare($sql);
        $statement->bind_param("ss", $username, $email);
        $statement->execute();
        $statement->store_result();

        //Check if user already exists
        if ($statement->num_rows >= 1) {
            echo json_encode(array("response" => "user_exists"));
            break;
        }

        //SQL statement - Insert user to DB
        $sql = "INSERT INTO User(username,pass,email,firstname,lastname) VALUES(?,?,?,?,?);";
        $statement = $conn->prepare($sql);
        //Create password hash
        $passHash = password_hash($password, PASSWORD_DEFAULT);
        //Default imaginary policy number
        $statement->bind_param("sssss", $username, $passHash, $email, $firstname, $lastname);

        //Execute statement
        if (!$statement->execute()) {
            echo json_encode(array("response" => "failure"));
            break;
        }

        //Success
        echo json_encode(array("response" => "success"));
        break;

    case "ProfileChange":

        //Init new params
        $changePass = false;
        $username = $_POST['username'];
        $email = $_POST['email'];
        $oldPass = $_POST["oldPass"];
        $newPass = $_POST["newPass"];


        //Check if empty - old pass
        if (checkParamEmpty($oldPass)) {
            echo json_encode(array("response" => "wrong_params"));
            break;
        }

        //If pass is set -> change pass
        if (checkParamEmpty($email)) {
            if (checkParamEmpty($newPass)) {
                echo json_encode(array("response" => "wrong_params"));
                break;
            } else {
                $changePass = true;
            }
        }

        //Check if old password is correct
        {
            $sql = "SELECT pass FROM User WHERE username = ?";
            $statement = $conn->prepare($sql);
            $statement->bind_param("s", $username);
            $statement->execute();
            $statement->store_result();

            //Check if user exists
            if ($statement->num_rows >= 1) {

                //Bind Result
                $passHash = "";
                $statement->bind_result($passHash);

                //If old password is correct
                if (!$statement->fetch()) {
                    echo json_encode(array("response" => "failure"));
                    break;
                } else if (!password_verify($oldPass, $passHash)) {
                    echo json_encode(array("response" => "wrong_password"));
                    break;
                }
            } else {
                echo json_encode(array("response" => "user_not_exists"));
                break;
            }
        }

        if ($changePass) {
            //SQL statement - Change Password
            $sql = "UPDATE User SET pass = ? WHERE username = ?";
            $statement = $conn->prepare($sql);
            $statement->bind_param("ss", password_hash($newPass, PASSWORD_DEFAULT), $username);

            //Execute statement
            if (!$statement->execute()) {
                echo json_encode(array("response" => "failure"));
                break;
            }
        } else {
            $sql = "UPDATE User SET email = ? WHERE username = ?";
            $statement = $conn->prepare($sql);
            $statement->bind_param("ss", $email, $username);

            //Execute statement
            if (!$statement->execute()) {
                echo json_encode(array("response" => "failure"));
                break;
            }
        }

        //Success
        echo json_encode(array("response" => "success"));
        break;

    case "SelectClaim":

        //Init params
        $username = $_POST["username"];
        $password = $_POST["password"];

        //Check if user is set
        if (checkParamEmpty($username) && checkParamEmpty($password)) {
            echo json_encode(array("response" => "wrong_params"));
            break;
        }

        //SQL statement
        $sql = "SELECT CC.ID,CC.Datum,CC.Place,CC.SPZ,CC.Car_Type,CC.Contact_Firstname,CC.Contact_Lastname,CC.Contact_Email,CC.Contact_Phone,PO.Policy_Number,CC.Damage_Car,CC.Damage_Car_Other,CC.Damage_Other
                FROM Car_Accident as CA 
                INNER JOIN Car_Claim as CC ON CA.Car_Claim_ID = CC.ID
                INNER JOIN User AS US ON CA.User_ID = US.ID
                INNER JOIN Policy AS PO ON CC.Policy_ID = PO.ID
                WHERE US.username = ? AND US.pass = ?";
        $statement = $conn->prepare($sql);
        $statement->bind_param("ss", $username, $password);
        $statement->execute();

        //Bind result
        $claims = array();
        $cRes = array();
        $ids = array();
        $statement->bind_result($cRes[0], $cRes[1], $cRes[2], $cRes[3], $cRes[4], $cRes[5], $cRes[6], $cRes[7], $cRes[8], $cRes[9], $cRes[10], $cRes[11], $cRes[12]);

        //Fetch data to array
        $claims["response"] = "success";
        while ($statement->fetch()) {
            //Add to JSON
            $ids[] = $cRes[0];
            $claims[$cRes[0]] = array(
                "datum" => $cRes[1],
                "place" => $cRes[2],
                "SPZ" => $cRes[3],
                "carType" => $cRes[4],
                "contactFirstname" => $cRes[5],
                "contactLastname" => $cRes[6],
                "contactEmail" => $cRes[7],
                "contactPhone" => $cRes[8],
                "policyNumber" => $cRes[9],
                "damageCar" => $cRes[10],
                "damageCarOther" => $cRes[11],
                "damageOther" => $cRes[12],
            );
        }

        //Select photos
        foreach ($ids as $value) {
            $sql = "SELECT Media
            FROM Car_Accident_Photos as CAP
            WHERE CAP.Car_Claim_ID = ?";
            $statement = $conn->prepare($sql);
            $statement->bind_param("i", $value);
            $statement->execute();

            //Bind result
            $mediaPhoto = null;
            $count = 1;
            $statement->bind_result($mediaPhoto);
            while ($statement->fetch()) {
                $claims[$value] = array_merge($claims[$value], array("photo" . $count => $mediaPhoto));
                $count++;
            }
        }

        //Return JSON
        echo json_encode(utf8ize($claims));
        break;

    case "RegisterCar":
        //Init params
        $claimDate = $_POST["claimDate"];
        $claimPlace = $_POST["place"];
        $policyNumber = $_POST["policyNumber"];
        $damageCar = $_POST["damageCar"];
        $damageCarOther = $_POST["damageCarOther"];
        $damageOther = $_POST["damageOther"];
        $carType = $_POST["carType"];
        $spz = $_POST["spz"];
        $driverFirstname = $_POST["driverFirstname"];
        $driverLastname = $_POST["driverLastname"];
        $driverEmail = $_POST["driverEmail"];
        $driverPhone = $_POST["driverPhone"];

        //User
        $username = $_POST["username"];

        //Photos
        $photos = array();
        if (isset($_POST["photo1"])) {
            $photos[] = $_POST["photo1"];
        }
        if (isset($_POST["photo2"])) {
            $photos[] = $_POST["photo2"];
        }
        if (isset($_POST["photo3"])) {
            $photos[] = $_POST["photo3"];
        }
        if (isset($_POST["photo4"])) {
            $photos[] = $_POST["photo4"];
        }


        //Check params - isset & empty
        if (checkParamEmpty($claimDate) && checkParamEmpty($claimPlace) && checkParamEmpty($policyNumber) && checkParamEmpty($carType) && checkParamEmpty($spz) && checkParamEmpty($driverFirstname) && checkParamEmpty($driverLastname) && checkParamEmpty($driverEmail) && checkParamEmpty($driverPhone) && checkParamEmpty($username)) {
            echo json_encode(array("response" => "wrong_params"));
            break;
        }

        //If checkboxes are set
        if (!isset($damageCar) || !isset($damageCarOther) || !isset($damageOther)) {
            echo json_encode(array("response" => "wrong_params"));
            break;
        }

        //SQL statement - Insert claim to DB
        $sql = "INSERT INTO Car_Claim(Policy_ID,Damage_Car,Damage_Car_Other,Damage_Other,Datum,Place,SPZ,Car_Type,Contact_Firstname,Contact_Lastname,Contact_Email,Contact_Phone)
                VALUES((SELECT PO.ID FROM Policy as PO INNER JOIN PolicyProduct as PP ON PP.Policy_ID = PO.ID
                INNER JOIN UserPolicy as UP ON UP.Policy_ID = PO.ID INNER JOIN User as US ON UP.User_ID = US.ID
                WHERE PO.Policy_Number = ?  AND US.username = ? AND PP.Product_ID = (SELECT ID FROM Product as PRO WHERE PRO.Product_Code = \"POV\")),?,?,?,?,?,?,?,?,?,?,?)";
        $statement = $conn->prepare($sql);
        $statement->bind_param("isiiissssssss", $policyNumber, $username, $damageCar, $damageCarOther, $damageOther, $claimDate, $claimPlace, $spz, $carType, $driverFirstname, $driverLastname, $driverEmail, $driverPhone);

        //Execute statement
        if (!$statement->execute()) {
            echo json_encode(array("response" => "policy_id"));
            break;
        }

        //SQL statement - Insert Car Accident to DB
        $sql = "INSERT INTO Car_Accident(Car_Claim_ID, User_ID) VALUES ((SELECT MAX(ID) FROM Car_Claim), (SELECT ID FROM User WHERE username = ?))";
        $statement = $conn->prepare($sql);
        $statement->bind_param("s", $username);

        //Execute statement
        if (!$statement->execute()) {
            echo json_encode(array("response" => "failure"));
            break;
        }

        //Insert photo into DB
        if (count($photos) > 0) {
            foreach ($photos as $pht) {
                $val = null;
                $sql = "INSERT INTO Car_Accident_Photos(Car_Claim_ID, Media) VALUES ((SELECT MAX(ID) FROM Car_Claim),?)";
                $statement = $conn->prepare($sql);
                $statement->bind_param("b", $val);
                $statement->send_long_data(0, $pht);

                //Execute statemtent
                if (!$statement->execute()) {
                    echo json_encode(array("response" => "failure"));
                    break;
                }
            }
        }

        //Success
        echo json_encode(array("response" => "success"));
        break;

    case "SelectInsurance":

        //Init params
        $username = $_POST["username"];
        $password = $_POST["password"];

        //Check if user is set
        if (checkParamEmpty($username) && checkParamEmpty($password)) {
            echo json_encode(array("response" => "wrong_params"));
            break;
        }

        //SQL statement
        $sql = "SELECT DI.ID,DI.Datum_Od,DI.Datum_Do,PO.Policy_Number
                FROM Drone_Insurance_Create as DIS
                INNER JOIN Drone_Insurance as DI ON DIS.Drone_Insurance_ID = DI.ID
                INNER JOIN User AS US ON DIS.User_ID = US.ID
                INNER JOIN Policy as PO on DI.Policy_ID = PO.ID
                WHERE US.username = ? AND US.pass = ?";
        $statement = $conn->prepare($sql);
        $statement->bind_param("ss", $username, $password);
        $statement->execute();

        //Bind result
        $insurances = array();
        $insResponse = array();
        $statement->bind_result($insResponse[0], $insResponse[1], $insResponse[2], $insResponse[3]);

        //Fetch data to array
        $insurances["response"] = "success";
        while ($statement->fetch()) {
            $insurances[$insResponse[0]] = array(
                "DatumOd" => $insResponse[1],
                "DatumDo" => $insResponse[2],
                "PolicyNumber" => $insResponse[3],
            );
        }

        //Return JSON
        echo json_encode(utf8ize($insurances));
        break;

    case "RegisterDrone":

        //Init params
        $datumOd = $_POST["datumOd"];
        $datumDo = $_POST["datumDo"];
        $policyNumber = $_POST["policyNumber"];
        $username = $_POST["username"];

        //Check params - isset & empty
        if (checkParamEmpty($datumOd) && checkParamEmpty($datumDo) && checkParamEmpty($policyNumber) && checkParamEmpty($username)) {
            echo json_encode(array("response" => "wrong_params"));
            break;
        }

        //SQL statement - Insert insurance to DB
        $sql = "INSERT INTO Drone_Insurance(Policy_ID,Datum_Od,Datum_Do) VALUES (
                (SELECT PO.ID FROM Policy as PO INNER JOIN PolicyProduct as PP ON PP.Policy_ID = PO.ID
                INNER JOIN UserPolicy as UP ON UP.Policy_ID = PO.ID INNER JOIN User as US ON UP.User_ID = US.ID
                WHERE PO.Policy_Number = ?  AND US.username = ? AND PP.Product_ID = (SELECT ID FROM Product as PRO WHERE PRO.Product_Code = \"DRON\")),?,?)";
        $statement = $conn->prepare($sql);
        $statement->bind_param("isss", $policyNumber, $username, $datumOd, $datumDo);

        //Execute statement
        if (!$statement->execute()) {
            echo json_encode(array("response" => "policy_id"));
            break;
        }

        //SQL statement - Insert insurance create to DB
        $sql = "INSERT INTO Drone_Insurance_Create(Drone_Insurance_ID, User_ID) VALUES ((SELECT MAX(ID) FROM Drone_Insurance), (SELECT ID FROM User WHERE username = ?))";
        $statement = $conn->prepare($sql);
        $statement->bind_param("s", $username);

        //Execute statement
        if (!$statement->execute()) {
            echo json_encode(array("response" => "failure"));
            break;
        }

        //Success
        echo json_encode(array("response" => "success"));
        break;

    default:
        echo json_encode(array("response" => "wrong_action"));
        break;
}


//Close connection
$conn->close();

//Check if is set & not empty
function checkParamEmpty($param)
{
    if (isset($param) && !empty($param)) {
        return false;
    } else {
        return true;
    }
}

//Change data to utf8
function utf8ize($d)
{
    if (is_array($d)) {
        foreach ($d as $k => $v) {
            $d[$k] = utf8ize($v);
        }
    } else if (is_string($d)) {
        return utf8_encode($d);
    }
    return $d;
}