<?php
$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$one = $_REQUEST['member_id'];
$two = $_REQUEST['ingredient_type'];




$qry_Select_ID="SELECT `Ingredient_ID_type` FROM `ingredient_type` WHERE `ingredient_type`='$two'";

$result = mysqli_query($conn, $qry_Select_ID);

while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)) 
{

	$id = $row['Ingredient_ID_type'];
	// print_r( $row['Ingredient_ID_type']);

	// print_r( $one);
	// print_r( $two);
	// print_r( $id);



	if (isset($id)) 
	{
		// echo $id;


		$qry_INSERT="INSERT INTO `user_dislike_type`(`member_id`, `ingredient_type_ID`) VALUES ('$one','$id')";
	    if(mysqli_query($conn, $qry_INSERT)){
			echo "INSERT sucess";
		}
		else{
			echo "INSERT FAIL";
		}

	}

}

?>