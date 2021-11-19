<?php
include"connect.php";
$member_id=$_GET['member_id'];
$password=$_GET['password'];//Query String variable

$password = password_hash($password, PASSWORD_DEFAULT);
$qry="update member set password='$password' where member_id ='$member_id'";echo "Login Success";

mysqli_query($con,$qry);
?>
