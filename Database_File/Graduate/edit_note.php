<?php

$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$conn -> set_charset("UTF8"); // 資料庫連結
mysqli_select_db($conn,"123"); // 資料庫連結

$one = $_REQUEST['Fridge_ID']; // 接受android端傳來的值
$two = $_REQUEST['note']; // 接受android端傳來的值

$qry_Select_ID="SELECT `Fridge_ID` FROM `fridge_note` WHERE `Fridge_ID` = '$one'"; // 查詢使用者輸入的食物名稱是否在資料庫中
$result = mysqli_query($conn, $qry_Select_ID); // 上方的執行結果丟給result變數

if (mysqli_affected_rows($conn)>0){
	while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
		
		$qry_Select="SELECT * FROM `fridge_note` WHERE `Fridge_ID` = '$one'";
		$qry_UPDATE="UPDATE `fridge_note` SET `Fridge_ID`='$one',`note`='$two'";
		
		if(isset($one) and isset($two)){
			if(mysqli_query($conn, $qry_Select)){
				if (mysqli_affected_rows($conn)>0){
					if(mysqli_query($conn, $qry_UPDATE)){
						echo "UPDATE sucess";
					}else{
						echo "UPDATE FAIL";
					}
				}
			}
		}
	}
}
?>