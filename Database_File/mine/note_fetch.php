<?php

$one = $_GET['Fridge_ID'];
// echo "$one";
$two = $_GET['member_id'];
// echo "$two";

$conn=mysqli_connect("localhost","root",""); // 資料庫連結
$data=[];
$conn -> set_charset("UTF8"); // 資料庫連結
mysqli_select_db($conn,"123"); // 資料庫連結
$today = date("Y-m-d");





$qry="SELECT only_fridge.FridgeName,member.member_name,member.member_id,fridge_note.note,fridge_note.upload_date FROM (fridge_note INNER JOIN only_fridge ON fridge_note.Fridge_ID=only_fridge.FridgeID)INNER JOIN member ON fridge_note.member_id=member.member_id 
		WHERE Fridge_ID='$one' AND member.member_id='$two'";


$qry_INSERT="INSERT INTO `fridge_note` (`note_id`, `Fridge_ID`, `member_id`, `note`, `upload_date`) VALUES (NULL, '$one', 
	'$two', '開始使用備忘錄', '$today')"; 

$qry1="SELECT only_fridge.FridgeName,member.member_name,member.member_id,fridge_note.note,fridge_note.upload_date FROM (fridge_note INNER JOIN only_fridge ON fridge_note.Fridge_ID=only_fridge.FridgeID)INNER JOIN member ON fridge_note.member_id=member.member_id 
		WHERE Fridge_ID='$one'";



// 新增資料進去的語法INSERT INTO `fridge_note`(`Fridge_ID`, `note`) VALUES ('$one','開始使用備忘錄')
$raw=mysqli_query($conn,$qry);
if (mysqli_affected_rows($conn)>0){
	$raw=mysqli_query($conn,$qry1);
	while($res=mysqli_fetch_array($raw)){
		if($res == null || $res == ""){
			// echo "a";
			$data[]=[];
		}else{
			// echo "b";
			$data[]=$res;
		}
	}
	print(json_encode($data, JSON_UNESCAPED_UNICODE));
}
else
{ // 如果資料庫中完全沒有資料時
	// echo "c";
	$raw1=mysqli_query($conn,$qry_INSERT); // 我自己給他新增資料，內容已經寫死
	echo "$raw1";
	// echo "e";



		$raw2=mysqli_query($conn,$qry1); // 上方新增完之後，只會回傳T或F，所以這邊要再讀一次
		// echo "e0";
		while($res=mysqli_fetch_array($raw2))
		{
			if($res == null || $res == "")
			{
				// echo "e1";
				$data[]=[];
			}
			else
			{
				// echo "f";
				$data[]=$res;
			}
		}
		print(json_encode($data, JSON_UNESCAPED_UNICODE));

}

?>