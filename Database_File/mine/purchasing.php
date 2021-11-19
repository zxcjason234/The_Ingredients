<?php

$one = $_GET['FridgeID'];
$conn=mysqli_connect("localhost","root","");
$data=[];
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$qry="SELECT ingredient.Ingredient_title, Quantity FROM ingredient, fridge_purchasing_list WHERE fridge_purchasing_list.Fridge_ID = '$one' and fridge_purchasing_list.Ingredient_ID = ingredient.ingredient_ID";

$raw=mysqli_query($conn,$qry);

while($res=mysqli_fetch_array($raw))
{
	if($res == null || $res == ""){
		$data[]=[];
	}else{
		$data[]=$res;
	}
}
print(json_encode($data, JSON_UNESCAPED_UNICODE));

?>