<?php
$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$conn -> set_charset("UTF8"); // 資料庫連結
mysqli_select_db($conn,"123"); // 資料庫連結
              
$qry_Select_type="SELECT `Ingredient_ID_type` FROM ingredient_type 
WHERE `ingredient_type` = '雞肉'";

$barcode_id = "SELECT barcode FROM ingredient WHERE ingredient_title = '去骨雞腿'";
$bar =  mysqli_query($conn,$barcode_id); // 上方的執行結果丟給result變數

$result = mysqli_query($conn,$barcode_id); // 上方的執行結果丟給result變數
while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
		$id = $row['barcode'];
		if ($id != 9789865522506) {
			echo "9789865522506";
		}
		else
		{
			echo "something else";
		}
}

?>