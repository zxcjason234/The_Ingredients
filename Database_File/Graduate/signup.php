<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['member_id']) && isset($_POST['member_name']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("member", $_POST['member_id'], $_POST['member_name'], $_POST['password'])) {
            echo "Sign Up Success";
        } else echo "Sign up Failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
