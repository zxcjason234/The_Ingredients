<?php

$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$conn -> set_charset("UTF8"); // 資料庫連結
mysqli_select_db($conn,"123"); // 資料庫連結

$one = $_REQUEST['ingredient_title']; // 接受android端傳來的值
$two = $_REQUEST['FridgeID']; // 接受android端傳來的值

$qry_Select_ID="SELECT `ingredient_ID` FROM `ingredient` WHERE `ingredient_title` = '$one'"; // 查詢使用者輸入的食物名稱是否在資料庫中
$result = mysqli_query($conn, $qry_Select_ID); // 上方的執行結果丟給result變數

if (mysqli_affected_rows($conn)>0){
	
	while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){ // result變數的結果丟到陣列裡面
	
		$id = $row['ingredient_ID']; // ingredient_ID對應的值，丟給變數id
		
		$qry_Select="SELECT * FROM `fridge_keep` WHERE `FridgeID`='$two' AND `Ingredient_ID` =$id";
		$qry_INSERT="INSERT INTO `fridge_keep`(`FridgeID`, `Ingredient_ID`) VALUES ('$two',$id)"; // 新增資料進去的語法
		$qry_UPDATE="UPDATE `fridge_keep` SET `FridgeID`='$two',`Ingredient_ID`=$id WHERE `Fridge_ID`='$two' AND `Ingredient_ID`=$id";
		
		if(isset($two) and isset($one)){ // 如果fridgeID有東西，數量也有東西
			if (mysqli_query($conn, $qry_Select)){ // 判斷是否執行成功
				if (mysqli_affected_rows($conn)>0){ // 如果輸入的食材"有"之前的相關資料
					if(mysqli_query($conn, $qry_UPDATE)){ // 判斷數量是否更新成功
						echo "UPDATE sucess";
					}else{
						echo "The data is already exists";
					}
				}else{
					if(mysqli_query($conn, $qry_INSERT)){ // 判斷是否新增資料成功
						echo "INSERT sucess";
					}else{
						echo "INSERT FAIL";
					}
				}
			}
		}
	}
}else{
	echo "No Data";
}
?>