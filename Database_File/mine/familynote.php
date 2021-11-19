<?php

$one = $_GET['Fridge_ID'];
$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$data=[];
$conn -> set_charset("UTF8"); // 資料庫連結
mysqli_select_db($conn,"123"); // 資料庫連結

$qry="SELECT `note` FROM `fridge_note` WHERE `Fridge_ID` = '$one'";
$qry_INSERT="INSERT INTO `fridge_note`(`Fridge_ID`, `note`) VALUES ('$one','開始使用備忘錄')"; // 新增資料進去的語法

if (mysqli_affected_rows($conn)>0){
	$raw=mysqli_query($conn,$qry);
	while($res=mysqli_fetch_array($raw)){
		if($res == null || $res == ""){
			$data[]=[];
		}else{
			$data[]=$res;
		}
	}
	print(json_encode($data, JSON_UNESCAPED_UNICODE));
}else{ // 如果資料庫中完全沒有資料時
	$raw=mysqli_query($conn,$qry_INSERT); // 我自己給他新增資料，內容已經寫死
	$raw=mysqli_query($conn,$qry); // 上方新增完之後，只會回傳T或F，所以這邊要再讀一次
	while($res=mysqli_fetch_array($raw)){
		if($res == null || $res == ""){
			$data[]=[];
		}else{
			$data[]=$res;
		}
	}
	print(json_encode($data, JSON_UNESCAPED_UNICODE));
}

?>