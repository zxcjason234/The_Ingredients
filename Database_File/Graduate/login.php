<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['member_id']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->logIn("member", $_POST['member_id'], $_POST['password'])){
            echo "Login Success";
        } else echo "member_id or Password wrong";
    } else echo "Error: Database connection";
} else echo "All fields are required1";
?>