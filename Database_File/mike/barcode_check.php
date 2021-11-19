<?php
$conn=mysqli_connect("localhost","root","");
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$barcode = $_REQUEST['barcode'];  #條碼
$FridgeID = $_REQUEST['FridgeID'];  #冰箱號碼

$expiration_date = $_REQUEST['expiration_date'];  #到期日
$ingredient_type = $_REQUEST['ingredient_type'];  #食物類別
$ingredient_title = $_REQUEST['ingredient_title'];  #食物名稱

#判斷Android端傳進來的"食物類別"是否存在ID
$Select_Ingredient_ID_type = "SELECT Ingredient_ID_type FROM ingredient_type WHERE ingredient_type = '$ingredient_type'";

#上方的執行結果丟給Ingredient_ID_type_result變數
$Ingredient_ID_type_result = mysqli_query($conn, $Select_Ingredient_ID_type);

#表示"食物類別"的ID存在
if (mysqli_affected_rows($conn)>0){
	
	while ($row = mysqli_fetch_array($Ingredient_ID_type_result, MYSQLI_ASSOC)){
		
		#Ingredient_ID_type對應的值，丟給變數Ingredient_ID_type
		$Ingredient_ID_type = $row['Ingredient_ID_type'];
		// echo "$Ingredient_ID_type";

		#判斷Android端傳進來的"條碼"是否存在
		$Select_Barcode_ID = "SELECT barcode FROM ingredient WHERE ingredient_title = '$ingredient_title'";
		
		#加入食材資料表
		$Insert_into_ingredient = "INSERT INTO `ingredient`(`ingredient_type_ID`, `ingredient_ID`, `ingredient_title`, `barcode`) 
		VALUES ('$Ingredient_ID_type', '', '$ingredient_title', $barcode)";
		
		
		if(isset($barcode) and isset($FridgeID) and isset($expiration_date) and isset($ingredient_type) and isset($ingredient_title)){
			
			if (mysqli_query($conn, $Select_Barcode_ID)){ #執行判斷"條碼"是否存在
			
				if (mysqli_affected_rows($conn)>0){ #"條碼"存在，加入冰箱內容物資料表
					
					#透過barcode條碼，抓取Ingredient_ID
					$Select_Ingredient_ID = "SELECT `ingredient_ID` FROM ingredient WHERE `barcode` = $barcode";
					
					#上方的執行結果丟給Ingredient_ID_result變數
					$Ingredient_ID_result = mysqli_query($conn, $Select_Ingredient_ID);
					
					while ($row2 = mysqli_fetch_array($Ingredient_ID_result, MYSQLI_ASSOC)){
						
						#Ingredient_ID對應的值，丟給變數Ingredient_ID
						$Ingredient_ID = $row2['ingredient_ID'];
						// echo "$Ingredient_ID";
						
						#檢查內容物是否有這筆食材紀錄
						$Select_history = "SELECT `Ingredient_ID` FROM fridge_contain WHERE `Ingredient_ID` = $Ingredient_ID AND `expiration_date` = '$expiration_date' AND `FridgeID` = '$FridgeID'";
						
						#加入冰箱內容物資料表
						$Insert_into_fridge_contain = "INSERT INTO `fridge_contain` (`contain_ID`, `FridgeID`, `Ingredient_ID`, `Quantity`, `expiration_date`) 
						VALUES (NULL, '$FridgeID', $Ingredient_ID, 1, '$expiration_date')";
						
						#更新冰箱內容物數量語法
						$Update_Contain_Quantity = "UPDATE `fridge_contain` SET `Ingredient_ID`= $Ingredient_ID, `Quantity`=`Quantity` + 1 WHERE `FridgeID` = '$FridgeID' AND `expiration_date` = '$expiration_date'";
						
						if (mysqli_query($conn, $Select_history)){ #執行檢查內容物是否有這筆食材紀錄
						
							if (mysqli_affected_rows($conn)>0){ #內容物有歷史紀錄
							
								if (mysqli_query($conn, $Update_Contain_Quantity)){ #執行更新冰箱內容物
									echo "Update Contain Quantity sucess";
								}else{ #新增失敗
									echo "Update Contain Quantity fail";
								}
									
							}else{ #內容物沒有歷史紀錄，直接新增
								if (mysqli_query($conn, $Insert_into_fridge_contain)){ #執行新增至冰箱內容物資料表
									echo "Insert into fridge contain sucess";
								}else{ #新增失敗
									echo "Insert into fridge contain fail";
								}
							}
						}
					}
					
				}else{ #"條碼"不存在，加入食材資料表
				
					if (mysqli_query($conn, $Insert_into_ingredient)){ #執行新增至食材資料表
						// echo "Insert into ingredient sucess";
						
						if (mysqli_affected_rows($conn)>0){
							
							#透過新建的Ingredient Title，抓取Ingredient_ID
							$Select_Ingredient_ID_new = "SELECT `ingredient_ID` FROM ingredient WHERE `ingredient_title` = '$ingredient_title'";
							
							#上方的執行結果丟給Ingredient_ID_result_new變數
							$Ingredient_ID_result_new = mysqli_query($conn, $Select_Ingredient_ID_new);
							
							while ($row3 = mysqli_fetch_array($Ingredient_ID_result_new, MYSQLI_ASSOC)){
								
								#Ingredient_ID對應的值，丟給變數Ingredient_ID_new，接到新建的Ingredient_ID
								$Ingredient_ID_new = $row3['ingredient_ID'];
								
								#加入冰箱內容物資料表，以新建的Ingredient_ID執行
								$Insert_into_fridge_contain = "INSERT INTO `fridge_contain` (`contain_ID`, `FridgeID`, `Ingredient_ID`, `Quantity`, `expiration_date`) 
								VALUES (NULL, '$FridgeID', $Ingredient_ID_new, 1, '$expiration_date')";
								
								if (mysqli_query($conn, $Insert_into_fridge_contain)){
									echo "Insert into fridge contain sucess";
								}else{
									echo "Insert into fridge contain fail";
								}
							}
						}
						
					}else{ #新增失敗
						echo "Insert into ingredient fail";
					}
				}
			}
			
		}else{
			#表示輸入的資料內容有缺少
			echo "Data inComplete";
		}
	}
	
}else{
	#表示"食物類別"的ID不存在
	echo "ID_type does not exist";
}

?>