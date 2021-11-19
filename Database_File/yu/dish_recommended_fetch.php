<?php
//header("Content-Type:text/html; charset=utf-8");

$one = $_GET['memID'];
$conn=mysqli_connect("localhost","root","");
$data=[];
$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

	// if (isset($_POST['title']))
	// {
		// echo "Sign Up Success";
	// }

$qry="SELECT member_id,member_recommendation.Dishes_id,dishes.title,dishes.img, lacked_Ingredient_title,Length,member_recommendation.Score FROM member_recommendation RIGHT JOIN dishes ON member_recommendation.Dishes_id=dishes.Dishes_id WHERE member_id ='$one' AND LENGTH<=5 ";

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