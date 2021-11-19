<?php

$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$conn -> set_charset("UTF8"); // 資料庫連結
mysqli_select_db($conn,"123"); // 資料庫連結

$one = $_REQUEST['FridgeID']; // 接受android端傳來的值

$qry_Select_ID="SELECT `FridgeID`, `Ingredient_ID` FROM fridge_contain WHERE `FridgeID` = '$one'"; // 先檢查冰箱中有沒有食材
$result = mysqli_query($conn, $qry_Select_ID); // 上方的執行結果丟給result變數

if (mysqli_affected_rows($conn)>0){
	$qry_Check="SELECT fridge_contain.FridgeID, ingredient.ingredient_title, fridge_contain.expiration_date FROM ingredient, fridge_contain WHERE fridge_contain.expiration_date < DATE(NOW()) AND fridge_contain.FridgeID = '$one' AND fridge_contain.Ingredient_ID = ingredient.ingredient_ID";
		
	if(isset($one)){
		if(mysqli_query($conn, $qry_Check)){
			if (mysqli_affected_rows($conn)>0){
				$raw=mysqli_query($conn,$qry_Check);
				while($res=mysqli_fetch_array($raw)){
					$data[]=$res;
				}
				print(json_encode($data, JSON_UNESCAPED_UNICODE));
			}
		}
	}
}else{
	echo "No Data in the fridge";
}
?>