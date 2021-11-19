<?php
$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$conn -> set_charset("UTF8"); // 資料庫連結
mysqli_select_db($conn,"123"); // 資料庫連結

$one = $_REQUEST['ingredient_title']; // 接受android端傳來的值
$two = $_REQUEST['FridgeID'];// 接受android端傳來的值
$three = $_REQUEST['expiration_date'];// 接受android端傳來的值

$qry_Select_ID="SELECT `ingredient_ID` FROM ingredient WHERE `ingredient_title` = '$one'";
$result = mysqli_query($conn, $qry_Select_ID); // 上方的執行結果丟給result變數

if(mysqli_affected_rows($conn)>0){
	while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
		
		$id = $row['ingredient_ID'];
		
		$tit_ins="SELECT * FROM `fridge_contain` WHERE `Ingredient_ID` = $id and `FridgeID` = '$two' and `expiration_date`= '$three'" ;
		$result_tit_ins = mysqli_query($conn,$tit_ins);
		$row_tit_ins = mysqli_fetch_array($result_tit_ins);
		
		$qry_UPDATE="UPDATE `fridge_contain` SET Quantity = Quantity +1 WHERE `Ingredient_ID` = $id and `expiration_date`= '$three' and`FridgeID`= '$two'";
		$qry_INSERT="INSERT INTO `fridge_contain`(`FridgeID`, `Ingredient_ID`, `Quantity`,`expiration_date`) VALUES ('$two', $id, 1, '$three')";
		
		if ($row_tit_ins>0)
		{
			if (mysqli_query($conn, $qry_UPDATE)){
				echo "該食材冰箱數量已更新!";
			}else{
				echo "該食材冰箱數量更新失敗!";
			}
		}
		else if(isset($three) and isset($two)and isset($one)){
			if (mysqli_query($conn, $qry_INSERT)){
				echo "新增至冰箱成功!";
			}else{
				echo "新增至冰箱失敗!";
			}
		}else {echo "資料填寫未全!";}
	}
}else{
	echo "食材中沒有該筆資料!請至新增食材新增!";
}
?>