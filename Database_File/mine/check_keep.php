<?php
$conn = mysqli_connect("localhost","root","");
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$one = $_GET['FridgeID'];

$Keep_I_Lost = "SELECT Ingredient_ID FROM fridge_keep WHERE 
NOT EXISTS (select null from fridge_contain where 
fridge_contain.Ingredient_ID = fridge_keep.Ingredient_ID) 
AND NOT EXISTS (select null from fridge_purchasing_list where 
fridge_purchasing_list.Ingredient_ID = fridge_keep.Ingredient_ID) 
AND fridge_keep.FridgeID='$one'";

$result = mysqli_query($conn, $Keep_I_Lost);

if(mysqli_affected_rows($conn)>0){
    while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){

        $food_id = $row['Ingredient_ID'];

        $Insert_into_ShoppingList = "INSERT INTO fridge_purchasing_list(Fridge_ID, Ingredient_ID, Quantity) 
        VALUES ('$one',$food_id,1)";

        if(isset($one)){
            if(mysqli_query($conn, $Insert_into_ShoppingList)){
                echo "INSERT sucess";
            }else{
                echo "INSERT FAIL";
            }
        }
    }
}

?>