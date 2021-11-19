<?php
//header("Content-Type:text/html; charset=utf-8");
$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");
$one = $_REQUEST['memID'];
// $user_mOv=$_REQUEST['m'];

$qry_check_MorV="SELECT * FROM `user_m_or_v` WHERE member_id='$one'";

$qry_m="SELECT * FROM dishes WHERE M_or_V='葷食' AND Dishes_id ORDER BY RAND() LIMIT 5";

$qry_v="SELECT * FROM dishes WHERE M_or_V='素食' AND Dishes_id ORDER BY RAND() LIMIT 5";

$raw=mysqli_query($conn,$qry_check_MorV);

while($res=mysqli_fetch_array($raw))
{
	 $user_mOv=$res["M_or_V"];
}


// echo $user_mOv;

if($user_mOv == "葷食")
{

	$rawM=mysqli_query($conn,$qry_m);
	while($resM=mysqli_fetch_array($rawM))
	{
		$data[]=$resM;
	}

	// echo "a";
}
else
{
	$rawV=mysqli_query($conn,$qry_v);
	while($resV=mysqli_fetch_array($rawV))
	{
		$data[]=$resV;
	}
	// echo "b";

}
print(json_encode($data, JSON_UNESCAPED_UNICODE));
?>