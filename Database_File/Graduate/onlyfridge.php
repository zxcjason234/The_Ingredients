<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['FridgeID']) && isset($_POST['FridgeName'])) {
    if ($db->dbConnect()) {
        if ($db->onlyfridge("only_fridge", $_POST['FridgeID'], $_POST['FridgeName'])) {
            echo "Sign Up Success";
        } else echo "FridgeID already have, Please try another one";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>