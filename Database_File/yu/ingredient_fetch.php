<?php
$one = $_REQUEST['Dishes_id'];
$two= $_GET['FridgeID'];
$conn=mysqli_connect("localhost","root","");
$data=[];
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");
$qry_select="SELECT fridge_contain.FridgeID AS FridgeID
            ,dishes_ingredient.Ingredient_ID
            ,ingredient.ingredient_title 
            ,dishes_ingredient.Quantity AS 'dishes_ingredient_Quantity'
            ,ifnull(fridge_contain.Quantity,0) AS 'fridge_Quantity'
            ,((CASE 
          WHEN dishes_ingredient.Ingredient_ID=6    THEN 1
         WHEN State_and_Unit='支' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='隻' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='片' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='整顆' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='付' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='束'   THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='顆' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='盒' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='根' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='罐' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='斤' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='條' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='袋' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='盒' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='包' THEN dishes_ingredient.Quantity
         WHEN State_and_Unit='桶' THEN dishes_ingredient.Quantity
        WHEN State_and_Unit='把' THEN dishes_ingredient.Quantity
     ELSE 1
    END)-ifnull(fridge_contain.Quantity,0))AS Subtract_Quantity
     ,dishes_ingredient.State_and_Unit 
     ,ingredient.company
     ,ingredient.website
     ,ingredient.product
     ,ingredient.price
     ,ingredient.Unit
FROM (fridge_contain RIGHT JOIN dishes_ingredient ON dishes_ingredient.Ingredient_ID=fridge_contain.Ingredient_ID)
        RIGHT JOIN ingredient ON dishes_ingredient.Ingredient_ID=ingredient.ingredient_ID
        WHERE dishes_ingredient.Dishes_id=$one AND (fridge_contain.FridgeID IN ('$two') OR fridge_contain.FridgeID IS NULL)";



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