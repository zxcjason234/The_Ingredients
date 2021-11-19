<?php

$one = $_GET['FridgeID'];
$conn=mysqli_connect("localhost","root","");
$data=[];
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$qry="SELECT ingredient.Ingredient_title, fridge_contain.expiration_date, fridge_contain.Quantity FROM fridge_contain, ingredient where fridge_contain.FridgeID ='$one' and fridge_contain.Ingredient_ID = ingredient.Ingredient_ID";

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