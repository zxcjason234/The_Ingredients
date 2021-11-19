<?php



$one=$_REQUEST['FridgeID'];
$data=[];

$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");


$qry="SELECT fridge_contain.FridgeID,fridge_contain.Ingredient_ID,ingredient.ingredient_title FROM fridge_contain RIGHT JOIN ingredient ON fridge_contain.Ingredient_ID=ingredient.ingredient_ID WHERE FridgeID ='$one' ";
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