<?php



$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");




$one = $_POST['member_id'];
$two = $_POST['Dishes_ID'];

$qry_Select="SELECT * FROM `user_ratings` WHERE member_id ='$one' AND Dishes_ID =$two ";
$qry_INSERT="INSERT INTO `user_ratings`(`member_id`, `Dishes_ID`, `rating`) VALUES ('$one',$two,1)";
$qry_UPDATE="update user_ratings set rating=rating+1 where Dishes_ID=$two AND member_id='$one'";

if (isset($one) AND isset($two)) {
	if (mysqli_query($conn, $qry_Select)){

	//檢查使用者是否有跟此時相關的行為
	if(mysqli_affected_rows($conn)>0){
		// echo "SELECT sucess";

		//有ㄉ話就更新評分
		if(mysqli_query($conn, $qry_UPDATE)){
			echo "UPDAYTE sucess";
		}
		else{
			echo "UPDAYTE FAIL";
		}

	}

	//沒有ㄉ話就新增跟此食譜相關的評分
	//此為記錄點擊(+1分)
	else{

		if(mysqli_query($conn, $qry_INSERT)){
			echo "INSERT sucess";
		}
		else{
			echo "INSERT FAIL";
		}
		
	}
}

else{
	echo "SELECT FAIL";
}
} else echo "All fields are required1";




?>