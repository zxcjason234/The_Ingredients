<?php
$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$conn -> set_charset("UTF8"); // 資料庫連結
mysqli_select_db($conn,"123"); // 資料庫連結

$one = $_REQUEST['ingredient_title']; // 接受android端傳來的值
$two = $_REQUEST['ingredient_type'];// 接受android端傳來的值
$three = $_REQUEST['expiration_date'];// 接受android端傳來的值
$four = $_REQUEST['FridgeID'];// 接受android端傳來的值
$five = $_REQUEST['barcode']; // 接受android端傳來的值

$qry_Select_type="SELECT `Ingredient_ID_type` FROM `ingredient_type` WHERE `ingredient_type` = $two";
$result = mysqli_query($conn,$qry_Select_type); // 上方的執行結果丟給result變數

if (mysqli_affected_rows($conn)>0){
	while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
		$id = $row['Ingredient_ID_type'];
		
		$bar="SELECT * FROM `ingredient` WHERE `barcode`  = $five";
		$result_bar = mysqli_query($conn,$bar);
		$row_bar = mysqli_fetch_row($result_bar);
		
		$ing_id="SELECT `ingredient_ID` FROM ingredient WHERE `barcode` = $five";
		$result_id = mysqli_query($conn,$ing_id);
		$row_id = mysqli_fetch_array($result_id, MYSQLI_ASSOC);
		echo $row_id;
		$id_2=$row_id['ingredient_ID'];
		
		
		$qry_firdge_INSERT="INSERT INTO `fridge_contain`(`FridgeID`, `Ingredient_ID`, `expiration_date`) VALUES ('$four',$id_2,'$three')";
		$qry_ing_INSERT="INSERT INTO `ingredient`(`ingredient_type_ID`, `ingredient_title`) VALUES ($id, '$one')";
		
		if ($row_bar>0)
		{
			echo '食材中已有該筆資料!請返回冰箱新增該食材!';
		}
	
		else if(isset($one) and isset($two) and isset($three) and isset($four)and isset($five)){
			if (mysqli_query($conn, $qry_ing_INSERT)){
				if (mysqli_query($conn, $qry_firdge_INSERT)){
					echo "Insert Sucess";
				}else{
					echo "Insert fridge Fail";
				}
			}else{
				echo "Insert ingredient Fail";
			}
		}else{
			echo "缺少資料";
		}
	}
}else{
	echo "No Data!";
}


?>