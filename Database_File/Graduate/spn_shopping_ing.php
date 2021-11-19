<?php
require_once "DataBase_new.php";

$sql = "select * from ingredient";

if(!$conn->query($sql)){
	echo "Error in connecting to database";
}
else{
	$result = $conn->query($sql);
	if($result->num_rows > 0){
		$return_arr['ingredient_title'] = array();
		while($row = $result->fetch_array()){
			array_push($return_arr['ingredient_title'],array(
			'ingredient_title'=>$row['ingredient_title']
			));
		}
		echo json_encode($return_arr);
	}
}
?>