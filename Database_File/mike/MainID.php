<?php
require_once "DataBase.php";

$sql = "select * from ingredient_type";

if(!$conn->query($sql)){
	echo "Error in connecting to database";
}
else{
	$result = $conn->query($sql);
	if($result->num_rows > 0){
		$return_arr['ingredient_type'] = array();
		while($row = $result->fetch_array()){
			array_push($return_arr['ingredient_type'],array(
			'Ingredient_ID_type'=>$row['Ingredient_ID_type'],
			'ingredient_type'=>$row['ingredient_type']
			));
		}
		echo json_encode($return_arr);
	}
}
?>