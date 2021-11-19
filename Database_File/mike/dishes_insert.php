<?php
$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$conn -> set_charset("UTF8"); // 資料庫連結
mysqli_select_db($conn,"123"); // 資料庫連結

$one = $_REQUEST['barcode']; // 接受android端傳來的值
$two = $_REQUEST['FridgeID'];// 接受android端傳來的值
$three = $_REQUEST['expiration_date'];// 接受android端傳來的值

$qry_Select_barcode="SELECT `ingredient_ID` FROM ingredient WHERE `barcode` = $one";
$result = mysqli_query($conn, $qry_Select_barcode); // 上方的執行結果丟給result變數

if(mysqli_affected_rows($conn)>0){
	while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
		
		$id = $row['ingredient_ID'];
		$qry_INSERT="INSERT INTO `fridge_contain`(`FridgeID`, `Ingredient_ID`, `expiration_date`) VALUES ('$two', '$id', '$three')";
		
		if(isset($three) and isset($two)){
			if (mysqli_query($conn, $qry_INSERT)){
				echo "Insert Sucess";
			}else{
				echo "Insert Fail";
			}
		}
	}
}else{
	echo "No Data";
}
?>