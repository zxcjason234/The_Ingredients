<?php
//header("Content-Type:text/html; charset=utf-8");
$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");
$one = $_REQUEST['member_id'];

$qry_Delete="DELETE FROM `user_dislike_type` WHERE `member_id`='$one'";

$raw=mysqli_query($conn,$qry_Delete);



print($raw);


?>