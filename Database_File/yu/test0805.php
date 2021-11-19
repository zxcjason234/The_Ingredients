<?php
$one = $_GET['FridgeID'];
$conn=mysqli_connect("localhost","root","");
$data=[];
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");
$qry_select="SELECT 
fridge_contain.FridgeID,fridge_contain.Ingredient_ID,
ingredient.ingredient_title,Quantity 
FROM fridge_contain LEFT JOIN ingredient 
ON fridge_contain.Ingredient_ID=ingredient.ingredient_ID 
WHERE FridgeID='$one'";



$raw=mysqli_query($conn,$qry_select);

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