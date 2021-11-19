<?php
$conn=mysqli_connect("localhost","root","");
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$one = $_REQUEST['ingredient_type'];
$two = $_REQUEST['barcode'];
$three = $_REQUEST['ingredient_title'];
$four = $_REQUEST['FridgeID'];
$five = $_REQUEST['expiration_date'];

#$qry_Select_type="SELECT Ingredient_ID_type FROM ingredient_type WHERE ingredient_type = '$one'"; // 抓到食材類別的ID
#$result = mysqli_query($conn,$qry_Select_type);

$qry_Select_ID="SELECT `ingredient_ID` FROM `ingredient` WHERE `ingredient_title` = '$three'"; // 查詢使用者輸入的食物名稱是否在資料庫中
$result = mysqli_query($conn, $qry_Select_ID); // 上方的執行結果丟給result變數

if (mysqli_affected_rows($conn)>0){
	
	while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){ // result變數的結果丟到陣列裡面
	
		$id = $row['ingredient_ID']; // ingredient_ID對應的值，丟給變數id
		
		$qry_ing_INSERT="INSERT INTO `ingredient`(`ingredient_type_ID`, `ingredient_title`,`barcode`) VALUES ($one, '$three', $two)";
		$qry_fridge_INSERT="INSERT INTO `fridge_contain`(`FridgeID`, `Ingredient_ID`, `Quantity`,`expiration_date`) VALUES ('$four', $id, 1, '$five')";
	
		$qry_barcode_id="SELECT barcode FROM ingredient WHERE ingredient_title = '$three'"; // 查詢使用者輸入的食物名稱是否在資料庫中
		#$result = mysqli_query($conn, $qry_barcode_id); // 上方的執行結果丟給result變數
			
		if(isset($three) and isset($two) and isset($four) and isset($five) and isset($one)){
			echo "hi";
			
			if (mysqli_query($conn, $qry_barcode_id)){ // 判斷是否執行成功
			
				if (mysqli_affected_rows($conn)>0){ // 如果輸入的食材"有"之前的相關資料
					
					if (mysqli_query($conn, $qry_ing_INSERT)){
						echo "ing sess";
						if (mysqli_query($conn, $qry_fridge_INSERT)){
							echo "fri sess";
						}else{
							echo "fri fail";
						}	
					}else{
						echo "ing fail";
					}
					
					
				}
			}
			

		}else{
			echo "no data";
		}
			
	
		
		
	
	}//End of while
	
}



?>


