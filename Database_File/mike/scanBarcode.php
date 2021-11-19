<?php
$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$conn -> set_charset("utf8"); // 資料庫連結
$data=[];
mysqli_select_db($conn,"123"); // 資料庫連結

$one = $_REQUEST['barcode']; // 接受android端傳來的值

$qry_check_barcode="SELECT `ingredient_title` FROM `ingredient` WHERE `barcode` = '$one'";

$result = mysqli_query($conn, $qry_check_barcode);

if (mysqli_affected_rows($conn)>0){
	if (mysqli_num_rows($result)>0) {
		while ($res = mysqli_fetch_array($result, MYSQLI_ASSOC)) {
			if($res == null || $res == "")
			{
				$data[]=[];
			}
			else
			{
				$data[]=$res["ingredient_title"];
				// echo "sucess";
			}
		}print(json_encode($data[0], JSON_UNESCAPED_UNICODE));
	}
}
else
{
	echo "No Data";
}

?>