<?php
//header("Content-Type:text/html; charset=utf-8");
$conn=mysqli_connect("localhost","root","");
$one = $_REQUEST['Dishes_id'];
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");
$qry="SELECT * FROM `dishes` WHERE Dishes_id =$one";

$raw=mysqli_query($conn,$qry);

while($res=mysqli_fetch_array($raw))
{
	 $data[]=$res;
}


print(json_encode($data, JSON_UNESCAPED_UNICODE));


?>