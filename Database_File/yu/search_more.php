<?php
//header("Content-Type:text/html; charset=utf-8");

$one = $_REQUEST['keyword'];
$two = $_REQUEST['RowNum'];
$conn=mysqli_connect("localhost","root","");

$conn -> set_charset("UTF8");
mysqli_select_db($conn,"123");

	// if (isset($_POST['title']))
	// {
		// echo "Sign Up Success";
	// }

// $qry="SELECT * FROM `dishes` 
// WHERE `title` LIKE '%$one%' 
// OR `dishes_type` LIKE '%$one%' 
// OR `food_main` LIKE '%$one%' 
// OR `country_cusine` LIKE '%$one%' 
// OR `M_or_V` LIKE '%$one%' ";

// $qry = "SELECT  dishes.Dishes_id
//         ,dishes.title
//         ,dishes.dishes_type
//         ,dishes.food_main
//         ,dishes.country_cusine
//         ,dishes.M_or_V
//         ,dishes.img
//         ,GROUP_CONCAT(ingredient_type.Ingredient_type)as ingredient_type
//         ,group_concat(Ingredient_title) 
// as Ingredient_title FROM ((dishes_ingredient LEFT JOIN dishes ON dishes.Dishes_id=dishes_ingredient.Dishes_id)
//     LEFT JOIN ingredient on dishes_ingredient.Ingredient_ID=ingredient.Ingredient_ID)
// INNER JOIN ingredient_type ON ingredient.ingredient_type_ID = ingredient_type.Ingredient_ID_type 
// where ingredient_title LIKE '%$one%' 
// OR ingredient_type LIKE '%$one%' 
// OR dishes_type LIKE '%$one%' 
// OR food_main LIKE '%$one%' 
// OR country_cusine LIKE '%$one%' 
// OR M_or_V LIKE '%$one%' GROUP BY Dishes_id LIMIT 0,25";


// $qry="SELECT 
// dishes.Dishes_id ,
// dishes.title ,
// dishes.img  FROM `dishes` 
// WHERE `title` LIKE '%$one%' 
// OR `dishes_type` LIKE '%$one%' 
// OR `food_main` LIKE '%$one%' 
// OR `country_cusine` LIKE '%$one%' 
// OR `M_or_V` LIKE '%$one%' ";





// $qry="SELECT 
// dishes.Dishes_id ,
// dishes.title ,
// dishes.img 
// FROM ((dishes_ingredient 
//     LEFT JOIN dishes ON dishes.Dishes_id=dishes_ingredient.Dishes_id) 
//     LEFT JOIN ingredient on dishes_ingredient.Ingredient_ID=ingredient.Ingredient_ID) 
// INNER JOIN ingredient_type ON ingredient.ingredient_type_ID = ingredient_type.Ingredient_ID_type 
// where ingredient_title LIKE '%$one%' OR 
// ingredient_type LIKE '%$one%' OR 
// dishes_type LIKE '%$one%' OR 
// food_main LIKE '%$one%' OR 
// country_cusine LIKE '%$one%' OR 
// M_or_V LIKE '%$one%' GROUP BY Dishes_id ";
// $raw=mysqli_query($conn,$qry);
// $num=mysqli_affected_rows($conn);



$limit=$two+1;
$qry_get="SELECT 
    dishes.Dishes_id ,
    dishes.title ,
    dishes.img 
    FROM ((dishes_ingredient 
        LEFT JOIN dishes ON dishes.Dishes_id=dishes_ingredient.Dishes_id) 
        LEFT JOIN ingredient on dishes_ingredient.Ingredient_ID=ingredient.Ingredient_ID) 
    INNER JOIN ingredient_type ON ingredient.ingredient_type_ID = ingredient_type.Ingredient_ID_type 
    where ingredient_title LIKE '%$one%' OR 
    ingredient_type LIKE '%$one%' OR 
    dishes_type LIKE '%$one%' OR 
    food_main LIKE '%$one%' OR 
    country_cusine LIKE '%$one%' OR 
    M_or_V LIKE '%$one%' GROUP BY Dishes_id limit $limit,8";

$data=[];
$raw1=mysqli_query($conn,$qry_get);
while($res=mysqli_fetch_array($raw1))
{
    if($res == null || $res == ""){
        $data[]=[];
    }else{
        $data[]=$res;
    }

}
print(json_encode($data, JSON_UNESCAPED_UNICODE));

// $index=0;
// echo "Affected rows: $num";
// $data = array();

// while ( $limit<$num) 
// {
    
//     $raw1=mysqli_query($conn,$qry_get);
//     $res=mysqli_fetch_assoc($raw1);

//     if($res == null || $res == "")
//     {
//         $data[$index]=[];
//     }

//     $data[$index]=$res;
//     $index++;
//     $limit++;

//     if ($limit>$num) 
//     {
//         break;
//     }
// }



// while($res=mysqli_fetch_array($raw))
// {
//     if($res == null || $res == ""){
//         $data[]=[];
//     }else{
//         $data[]=$res;
//     }
// }
?>