<?php
include"connect.php";
$FridgeID=$_GET['FridgeID'];
$FridgeName=$_GET['FridgeName'];//Query String variable

$qry="update only_fridge set FridgeName='$FridgeName' where FridgeID ='$FridgeID'";echo "Login Success";

mysqli_query($con,$qry);
?>
