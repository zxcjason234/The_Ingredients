<?php

$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$one = $_REQUEST['member_id'];
$two = $_REQUEST['MorV'];

$qry_Select="SELECT * FROM `user_m_or_v` WHERE member_id = '$one'";

$qry_INSERT="INSERT INTO `user_m_or_v`(`member_id`, `M_or_V`) VALUES ('$one','$two')";

$qry_UPDATE="UPDATE `user_m_or_v` SET `M_or_V`='$two' WHERE `member_id`='$one'";



if (mysqli_query($conn, $qry_Select)){

	if(mysqli_affected_rows($conn)>0){

		if(mysqli_query($conn, $qry_UPDATE)){

			echo "UPDAYTE sucess";
		}
		else{
			echo "UPDAYTE FAIL";
			}

	}

	else{
		if(mysqli_query($conn, $qry_INSERT))
		{
			echo "INSERT sucess";
		}

		else
		{
			echo "INSERT FAIL";
		}
		}

}

else{
	echo "SELECT FAIL";
}




?>