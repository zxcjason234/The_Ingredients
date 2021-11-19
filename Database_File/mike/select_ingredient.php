<?php
require_once "DataBase.php";

$sql = "SELECT MIN(ingredient_ID) AS ingredient_ID, ingredient_title FROM ingredient GROUP BY ingredient_title";

if(!$conn->query($sql)){
	echo "Error in connecting to database";
}
else{
	$result = $conn->query($sql);
	if($result->num_rows > 0){
		$return_arr['ingredient'] = array();
		while($row = $result->fetch_array()){
			array_push($return_arr['ingredient'],array(
			'ingredient_ID'=>$row['ingredient_ID'],
			'ingredient_title'=>$row['ingredient_title']
			));
		}
		echo json_encode($return_arr);
	}
}
?>
