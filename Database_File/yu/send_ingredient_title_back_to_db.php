<?php
$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$one = $_REQUEST['member_id'];
$two = $_REQUEST['ingredient_title'];

$qry_Select_ID="SELECT `ingredient_ID` FROM `ingredient` WHERE `ingredient_title`='$two'";
$result = mysqli_query($conn, $qry_Select_ID);

while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)) 
{
	$id = $row['ingredient_ID'];
	// echo $one;
	// echo $two;
	// echo $id;
	
	if (isset($id)) 
	{
		$qry_INSERT="INSERT INTO `user_dislike` (`member_id`, `Ingredient_ID`) VALUES ('$one', '$id') ";
		if(mysqli_query($conn, $qry_INSERT)){
			echo "INSERT sucess";
		}
		else{
			echo "INSERT FAIL";
		}
	}


}
?>