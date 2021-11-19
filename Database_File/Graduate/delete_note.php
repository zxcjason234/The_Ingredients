<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['member_id'])) {
    if ($db->dbConnect()) {
        if ($db->delete_note("fridge_note", $_POST['member_id'])){
            echo "Sign Up Success";
        } else echo "member_id or Password wrong";
    } else echo "Error: Database connection";
} else echo "All fields are required1";
?>