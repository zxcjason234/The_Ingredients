<?php

$fid = $_REQUEST['Fridge_ID'];
$qty = $_REQUEST['qty'];
$ing_title = $_REQUEST['ingredient_title'];

$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

$qry_Select_ID="SELECT `ingredient_ID` FROM `ingredient` WHERE `ingredient_title` = '$ing_title' ";
$result = mysqli_query($conn, $qry_Select_ID);


while ($row = mysqli_fetch_array($result, MYSQLI_ASSOC)) {
    // print_r( $row['ingredient_ID']);
    $id = $row['ingredient_ID'];
    if (isset($id)) {


    	$qry_Select="SELECT * FROM `fridge_purchasing_list` WHERE `Fridge_ID`='$fid' AND `Ingredient_ID` =$id ";
    	$qry_INSERT="INSERT INTO `fridge_purchasing_list`(`Fridge_ID`, `Ingredient_ID`, `Quantity`) VALUES ('$fid',$id,$qty )";
    	$qry_UPDATE="UPDATE `fridge_purchasing_list` SET `Quantity`=`Quantity`+$qty WHERE `Fridge_ID`='$fid' AND `Ingredient_ID`=$id";

    	if (isset($fid) AND isset($qty)) {
    		if (mysqli_query($conn, $qry_Select)) {
    			if (mysqli_affected_rows($conn)>0) {

					if(mysqli_query($conn, $qry_UPDATE)){
						echo "UPDATE sucess";
					}
					else{
						echo "UPDATE FAIL";
					}

    			}

    			else{
    				
					if(mysqli_query($conn, $qry_INSERT)){
						echo "INSERT sucess";
					}
					else{
						echo "INSERT FAIL";
					}
    			}
    		}
    	}

    }






}



?>