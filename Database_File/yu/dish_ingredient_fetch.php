<?php
//header("Content-Type:text/html; charset=utf-8");
$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");
$qry="SELECT * FROM `ingredient`";

$raw=mysqli_query($conn,$qry);

while($res=mysqli_fetch_array($raw))
{
	 $data[]=$res;
}


print(json_encode($data, JSON_UNESCAPED_UNICODE));


?>