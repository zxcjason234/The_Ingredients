<?php
//header("Content-Type:text/html; charset=utf-8");

$one = $_GET['member_id'];
$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");


$qry="SELECT fridge_link.FridgeID, only_fridge.FridgeName From fridge_link, only_fridge WHERE fridge_link.member_id ='$one' and fridge_link.FridgeID=only_fridge.FridgeID";
$raw=mysqli_query($conn,$qry);

while($res=mysqli_fetch_array($raw))
{
	 $data[]=$res;
}
print(json_encode($data, JSON_UNESCAPED_UNICODE));


?>