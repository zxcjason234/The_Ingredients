<?php

$conn=mysqli_connect("localhost","root","");
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$FridgeID = $_REQUEST['FridgeID'];  #冰箱號碼
$expiration_date = $_REQUEST['expiration_date'];  #到期日
$ingredient_type = $_REQUEST['ingredient_type'];  #食物類別
$ingredient_title = $_REQUEST['ingredient_title'];  #食物名稱

#判斷Android端傳進來的"食物類別"是否存在ID
$Select_Ingredient_ID_type = "SELECT Ingredient_ID_type 
FROM ingredient_type WHERE ingredient_type = '$ingredient_type'";

#上方的執行結果丟給Ingredient_ID_type_result變數
$Ingredient_ID_type_result = mysqli_query($conn, $Select_Ingredient_ID_type);

#表示"食物類別"的ID存在
if (mysqli_affected_rows($conn)>0){
	// echo "a";
	
	while ($row = mysqli_fetch_array($Ingredient_ID_type_result, MYSQLI_ASSOC))
	{
		// echo "b";

		#Ingredient_ID_type對應的值，丟給變數Ingredient_ID_type
		$Ingredient_ID_type = $row['Ingredient_ID_type'];
	}
		// echo "$Ingredient_ID_type";

		#判斷Android端傳進來的"食物名稱"是否存在
		$Select_Ingredient_Title = "SELECT `ingredient_title` 
		FROM ingredient WHERE ingredient_title = '$ingredient_title'";
		
		#加入食材資料表
		$Insert_into_ingredient = "INSERT INTO `ingredient`(`ingredient_type_ID`, `ingredient_ID`, `ingredient_title`, `barcode`) 
		VALUES ($Ingredient_ID_type, NULL, '$ingredient_title', NULL)";
		
						
		if(isset($FridgeID) and isset($expiration_date) and isset($ingredient_type) and isset($ingredient_title)){
						// echo "c";
			if (mysqli_query($conn, $Select_Ingredient_Title))
			{ #執行判斷"食物名稱"是否存在
						// echo "d";
				if (mysqli_affected_rows($conn)>0)
				{ #食物名稱存在，不能新增資料到ingredient資料表
											// echo "e";

					#透過Ingredient Title，抓取Ingredient_ID
					$Select_Ingredient_ID = "SELECT `ingredient_ID` 
					FROM ingredient 
					WHERE `ingredient_title` = '$ingredient_title'";
					
					#上方的執行結果丟給Ingredient_ID_result變數
					$Ingredient_ID_result = mysqli_query($conn, $Select_Ingredient_ID);
					
					while ($row2 = mysqli_fetch_array($Ingredient_ID_result, MYSQLI_ASSOC))
					{
						// echo "f";
						#Ingredient_ID對應的值，丟給變數Ingredient_ID
						$Ingredient_ID = $row2['ingredient_ID'];
						
					}
					// echo "$Ingredient_ID";

											#檢查內容物是否有這筆食材紀錄
						$Select_history = "SELECT `Ingredient_ID` FROM fridge_contain 
						WHERE `Ingredient_ID` = $Ingredient_ID 
						AND `expiration_date` = '$expiration_date' 
						AND `FridgeID` = '$FridgeID'";
						
						#加入冰箱內容物資料表
						$Insert_into_fridge_contain = "INSERT INTO `fridge_contain` 
						(`contain_ID`, `FridgeID`, `Ingredient_ID`, `Quantity`, `expiration_date`) 
						VALUES (NULL, '$FridgeID', $Ingredient_ID, 1, '$expiration_date')";
						


						// echo "   $Ingredient_ID   ";
						#更新冰箱內容物數量語法
						$Update_Contain_Quantity = "UPDATE `fridge_contain` 
						SET `Ingredient_ID`= $Ingredient_ID, `Quantity`= `Quantity` + 1 
						WHERE `FridgeID` = '$FridgeID' 
						AND `expiration_date` = '$expiration_date'";
						
						if (mysqli_query($conn, $Select_history))
						{ #執行檢查內容物是否有這筆食材紀錄
							// echo "g";
							if (mysqli_affected_rows($conn)>0)
							{ 
								#內容物有歷史紀錄
								// echo "h";
								if($Ingredient_ID > 0)
								{
									if(mysqli_query($conn, $Update_Contain_Quantity))
									{ #執行更新冰箱內容物
										// echo "i1";
										echo "成功更新此現有食材數量";
									}
									else
									{ #新增失敗
										// echo "i2";
										echo "無法更新此現有食材數量";
									}
								}

							}
							else
							{
								if (mysqli_query($conn, $Insert_into_fridge_contain)){ #執行新增至冰箱內容物資料表
									// echo "g2";
									echo "成功新增至冰箱";

								}else{ #新增失敗
																// echo "g3";
									echo "無法新增至冰箱";
								}
							}

						}else{ #內容物沒有歷史紀錄，直接新增

							echo "資料庫查詢失敗";

						}
				}
				else
				{ #"食物名稱"不存在，加入ingredient資料表
				
					if (mysqli_query($conn, $Insert_into_ingredient)){ #執行新增至食材資料表
					
						// echo "Insert into ingredient sucess";
						
						if (mysqli_affected_rows($conn)>0){
							
							#透過新建的Ingredient Title，抓取Ingredient_ID
							$Select_Ingredient_ID_new = "SELECT `ingredient_ID` 
							FROM ingredient WHERE `ingredient_title` = '$ingredient_title'";
							
							#上方的執行結果丟給Ingredient_ID_result_new變數
							$Ingredient_ID_result_new = mysqli_query($conn, $Select_Ingredient_ID_new);
							
							while ($row3 = mysqli_fetch_array($Ingredient_ID_result_new, MYSQLI_ASSOC))
							{
								#Ingredient_ID對應的值，丟給變數Ingredient_ID_new，接到新建的Ingredient_ID
								$Ingredient_ID_new = $row3['ingredient_ID'];
							}
															#加入冰箱內容物資料表，以新建的Ingredient_ID執行
							$Insert_into_fridge_contain = "INSERT INTO `fridge_contain` 
							(`contain_ID`, `FridgeID`, `Ingredient_ID`, `Quantity`, `expiration_date`) 
								VALUES (NULL, '$FridgeID', $Ingredient_ID_new, 1, '$expiration_date')";
								
							if(mysqli_query($conn, $Insert_into_fridge_contain))
							{
								echo "成功新增至冰箱";
							}
							else
							{
								echo "無法新增至冰箱";
							}
						
						}
						
					}else{ #新增失敗
						echo "無法新增至食材資料庫";
					}
				}
			}
		}
		else{
			#表示輸入的資料內容有缺少
			echo "資料輸入不完整";
		}


}else{
	#表示"食物類別"的ID不存在
	echo "ID_type does not exist";
}
?>