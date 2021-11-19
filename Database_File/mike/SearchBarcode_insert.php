<?php

$conn=mysqli_connect("localhost","root","");
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$barcode = $_REQUEST['barcode']; #條碼
$FridgeID = $_REQUEST['FridgeID']; #冰箱編號
$expiration_date = $_REQUEST['expiration_date']; #過期日

$qry_Select_barcode="SELECT `ingredient_ID` FROM ingredient WHERE `barcode` = $barcode";

$result = mysqli_query($conn, $qry_Select_barcode);

if(mysqli_affected_rows($conn)>0){
	
	while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
		
		$id = $row['ingredient_ID'];
		
		$qry_Select = "SELECT * FROM `fridge_contain` WHERE `Ingredient_ID` = $id and `FridgeID` = '$FridgeID' and `expiration_date`= '$expiration_date'";
		
		$qry_INSERT = "INSERT INTO `fridge_contain`(`contain_ID`, `FridgeID`, `Ingredient_ID`, `Quantity`, `expiration_date`) VALUES (NULL, '$FridgeID', $id, 1, '$expiration_date')";
		
		$qry_UPDATE = "UPDATE `fridge_contain` SET `Quantity`= Quantity + 1 WHERE `FridgeID` = '$FridgeID' and `expiration_date`= '$expiration_date' and `Ingredient_ID` = $id";
		
		if(isset($barcode) and isset($FridgeID) and isset($expiration_date)){
			if (mysqli_query($conn, $qry_Select)){
				if (mysqli_affected_rows($conn)>0){
					if(mysqli_query($conn, $qry_UPDATE)){
						echo "UPDATE sucess";
					}else{
						echo "UPDATE fail";
					}
				}else{
					if(mysqli_query($conn, $qry_INSERT)){
						echo "INSERT sucess";
					}else{
						echo "INSERT fail";
					}
				}
			}
		}
	}
	
}else{
	echo "No Data";
}

?>