<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['member_id']) && isset($_POST['FridgeID'])) {
    if ($db->dbConnect()) {
        if ($db->fridge_note("fridge_note", $_POST['member_id'], $_POST['FridgeID'])) {
            echo "Sign Up Success";
        } else echo "already have";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>