<?php
//header("Content-Type:text/html; charset=utf-8");


$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");
$qry="SELECT * FROM ingredient_type where ingredient_type not LIKE '調味料' AND ingredient_type not like '辛香料'";

$raw=mysqli_query($conn,$qry);

while($res=mysqli_fetch_array($raw))
{
	 $data[]=$res;
}


print(json_encode($data, JSON_UNESCAPED_UNICODE));

// while($row=mysqli_fetch_assoc($raw))
// {
// 	 print_r($row["ingredient_type"]);
// }


?>