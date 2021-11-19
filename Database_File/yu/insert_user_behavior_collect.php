<?php

$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$one = $_REQUEST['member_id'];
$two = $_REQUEST['Dishes_ID'];

$qry_Select_FROM_collrction="SELECT * FROM `user_collection` WHERE member_id = '$one' AND Dishes_id =$two ";

$qry_INSERT_to_collection="INSERT INTO `user_collection`(`member_id`, `Dishes_id`) VALUES ('$one','$two')";



$qry_SELECT_FROM_ratings="SELECT * FROM `user_ratings` WHERE member_id ='$one' AND Dishes_ID =$two ";


$qry_INSERT_to_RATINGS="INSERT INTO `user_ratings`(`member_id`, `Dishes_ID`, `rating`) VALUES ('$one',$two,3)";


$qry_UPDATE_FROM_ratings="update user_ratings set rating=rating+3 where Dishes_ID=$two AND member_id='$one'";


if (mysqli_query($conn, $qry_Select_FROM_collrction))
{

	if(mysqli_affected_rows($conn)>0)
	{
		echo "ALREADY IN COLLECTION";
	}

	else
	{

		if(mysqli_query($conn, $qry_INSERT_to_collection))
		{
			if(mysqli_query($conn, $qry_SELECT_FROM_ratings))
			{
				if(mysqli_affected_rows($conn)>0)
				{
					if(mysqli_query($conn, $qry_UPDATE_FROM_ratings))
					{
						echo "INSERT sucess";
					}
					else
					{
						echo "INSERT FAIL";
					}					
				}
				else
				{
					if(mysqli_query($conn, $qry_INSERT_to_RATINGS))
					{
						echo "INSERT sucess";
					}
					else
					{
						echo "INSERT sucess";
					}	
				}					
			}			
		}

	}

}

else{
	echo "SELECT FAIL";
}




?>