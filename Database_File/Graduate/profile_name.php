<?php
include"connect.php";
$member_id=$_GET['member_id'];
$member_name=$_GET['member_name'];//Query String var




$qry="update member set member_name='$member_name' where member_id ='$member_id'";echo "Login Success";

mysqli_query($con,$qry);
?>

