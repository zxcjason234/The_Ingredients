<?php

$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$conn -> set_charset("UTF8"); // 資料庫連結
mysqli_select_db($conn,"123"); // 資料庫連結

$one = $_REQUEST['ingredient_title']; // 接受android端傳來的值
$two = $_REQUEST['expiration_date']; // 接受android端傳來的值
$three = $_REQUEST['FridgeID']; // 接受android端傳來的值

$qry_Select_ID="SELECT `ingredient_ID` FROM `ingredient` WHERE `ingredient_title` = '$one'";
$result = mysqli_query($conn, $qry_Select_ID); // 上方的執行結果丟給result變數

if (mysqli_affected_rows($conn)>0){
	while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
		
		$id = $row['ingredient_ID']; // ingredient_ID對應的值，丟給變數id
		
		$qry_Select="SELECT * FROM `fridge_contain` WHERE `FridgeID`='$three' AND `Ingredient_ID` =$id";
		
		$qry_quantity_minus_one = "UPDATE `fridge_contain` SET `Quantity` = Quantity - 1 WHERE `Ingredient_ID` = $id AND `FridgeID` = '$three'";
		
		if(isset($two) and isset($three)){
			if (mysqli_query($conn, $qry_Select)){
				if (mysqli_affected_rows($conn)>0){
					if(mysqli_query($conn, $qry_quantity_minus_one)){
						echo "Delete sucess";
					}else{
						echo "Delete FAIL";
					}
				}
			}
		}
	}
}
?>