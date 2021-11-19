<?php
header("content-type:text/html;charset=utf-8");

$db_name = "123";
$mysql_username = "root";
$mysql_password = "";
$server_name = "localhost";
$conn = mysqli_connect($server_name,$mysql_username,$mysql_password,$db_name);
mysqli_query($conn,"set names'utf8'");
if(!$conn)
	die('conn not seccuss'.mysqli_connect_errno);
?>
