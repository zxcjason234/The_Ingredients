<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['member_id'])) {
    if ($db->dbConnect()) {
        if ($db->afterlogin_fridge("fridge_link", $_POST['member_id'])){
            echo "Login Success";
        } else echo "member_id or Password wrong";
    } else echo "Error: Database connection";
} else echo "All fields are required1";
?>