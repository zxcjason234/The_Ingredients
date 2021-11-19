<?php


// header("Content-type: application/json; charset=utf-8");


$one = $_GET['test'];


$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$qry="SELECT ingredient.ingredient_ID, 
ingredient.Ingredient_title,
Quantity,
State_and_Unit FROM 
(dishes_ingredient LEFT JOIN dishes 
	ON dishes.Dishes_id=dishes_ingredient.Dishes_id)
LEFT JOIN ingredient on 
dishes_ingredient.Ingredient_ID=ingredient.Ingredient_ID 
WHERE dishes_ingredient.Dishes_id=$one ";

$raw=mysqli_query($conn,$qry);
mysqli_query($conn,$qry);

while($res=mysqli_fetch_array($raw))
{
	 $data[]=$res;
}
print(json_encode($data, JSON_UNESCAPED_UNICODE));


?>