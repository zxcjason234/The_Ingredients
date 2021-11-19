<?php
//header("Content-Type:text/html; charset=utf-8");

$one = $_REQUEST['member_id'];
$two = $_REQUEST['Dishes_id'];
$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

	// if (isset($_POST['title']))
	// {
		// echo "Sign Up Success";
	// }

$qry_delete="DELETE FROM `user_collection` WHERE `member_id`='$one' AND `Dishes_id` ='$two'";

if(mysqli_query($conn,$qry_delete))
{
	echo "Delete Success";
}

?>