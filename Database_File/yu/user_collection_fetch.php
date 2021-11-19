<?php
//header("Content-Type:text/html; charset=utf-8");

$one = $_REQUEST['member_id'];
$conn=mysqli_connect("localhost","root","");
$data=[];
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

	// if (isset($_POST['title']))
	// {
		// echo "Sign Up Success";
	// }

$qry="SELECT member_id,
dishes.Dishes_id,
dishes.title,
dishes.img FROM user_collection 
RIGHT JOIN dishes 
ON user_collection.Dishes_id=dishes.Dishes_id 
WHERE member_id ='$one'";

$raw=mysqli_query($conn,$qry);

while($res=mysqli_fetch_array($raw))
{
	if($res == null || $res == ""){
		$data[]=[];
	}else{
		$data[]=$res;
	}
}
print(json_encode($data, JSON_UNESCAPED_UNICODE));
?>