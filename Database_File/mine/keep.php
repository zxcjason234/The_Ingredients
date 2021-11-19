<?php

$one = $_GET['FridgeID'];
$conn=mysqli_connect("localhost","root","");
$data=[];
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$qry="SELECT ingredient.ingredient_title FROM ingredient, fridge_keep WHERE fridge_keep.FridgeID = '$one' and fridge_keep.Ingredient_ID = ingredient.ingredient_ID";

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