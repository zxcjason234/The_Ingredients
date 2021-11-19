<?php

$conn=mysqli_connect("localhost","root","");
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$ingredient_title = $_REQUEST['ingredient_title'];
$FridgeID = $_REQUEST['FridgeID'];

$qry_Select_ID="SELECT `ingredient_ID` FROM `ingredient` WHERE `ingredient_title` = '$ingredient_title'";
$result = mysqli_query($conn, $qry_Select_ID);

if (mysqli_affected_rows($conn)>0){
	
	while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
		
		$id = $row['ingredient_ID'];
		$qry_Select = "SELECT * FROM `fridge_purchasing_list` WHERE `Fridge_ID`= '$FridgeID' AND `Ingredient_ID` = $id";
		$qry_UPDATE = "UPDATE fridge_purchasing_list SET Quantity = Quantity + 1 WHERE Fridge_ID = '$FridgeID' AND Ingredient_ID = $id";
		
		if(isset($ingredient_title) and isset($FridgeID)){
			if(mysqli_query($conn, $qry_Select)){
				if (mysqli_affected_rows($conn)>0){
					if(mysqli_query($conn, $qry_UPDATE)){
						echo "UPDATE success";
					}else{
						echo "UPDATE fail";
					}
				}
			}
		}
	}
	
}else{
	echo "No Data";
}
?>