<?php

$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$conn -> set_charset("UTF8"); // 資料庫連結
mysqli_select_db($conn,"123"); // 資料庫連結

$one = $_REQUEST['Fridge_ID']; // 接受android端傳來的值
$two = $_REQUEST['note']; // 接受android端傳來的值
$three = $_REQUEST['member_id'];
$today = date("Y-m-d");

// $qry_Select_ID="SELECT `Fridge_ID` 
// FROM `fridge_note` 
// WHERE `Fridge_ID` = '$one'"; // 查詢使用者輸入的食物名稱是否在資料庫中

$qry_Select="SELECT * FROM `fridge_note` WHERE `Fridge_ID` = '$one' AND member_id='$three' ";



// $result = mysqli_query($conn, $qry_Select_ID); // 上方的執行結果丟給result變數

// if (mysqli_affected_rows($conn)>0){
// }

$qry_UPDATE="UPDATE `fridge_note` SET `note`= '$two',`upload_date`= '$today'
WHERE `Fridge_ID` = '$one'
AND `member_id`='$three'";

		// "UPDATE `fridge_note` SET `note` = '$two' WHERE Fridge_ID = 'asd' AND member_id='bbb' "
		
if(isset($one) and isset($two))
{

	if(mysqli_query($conn, $qry_Select))
	{

		if (mysqli_affected_rows($conn)>0)
		{
			if(mysqli_query($conn, $qry_UPDATE))
			{

				echo "update sucess";
			}
			else
			{	
				echo "update FAIL";
					
			}
		}
	}
}
?>