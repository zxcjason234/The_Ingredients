<?php
    // 設定 MySQL 的連線資訊並開啟連線
    // 資料庫位置、使用者名稱、使用者密碼、資料庫名稱
    $conn=mysqli_connect("localhost","root","","123");
    $conn -> set_charset("UTF8"); // 設定語系避免亂碼
    $Dishes_id = $_REQUEST['Dishes_id'];
    $qry_fetch_step="SELECT * FROM dishes_step 
    WHERE Dishes_id=$Dishes_id GROUP BY Dishes_step_Num";  
    // SELECT *, ROW_NUMBER()OVER (PARTITION BY Dishes_id ORDER BY step_pic is null,step_pic asc) AS Length FROM dishes_step WHERE Dishes_id=$Dishes_id

    
    // $qry_fetch_step="SELECT *, ROW_NUMBER()OVER(PARTITION BY Dishes_id ORDER BY step) AS Length FROM dishes_step WHERE Dishes_id=$Dishes_id";

    $raw=mysqli_query($conn,$qry_fetch_step);

    while($res=mysqli_fetch_array($raw))
    {
         $data[]=$res;
    }
    print(json_encode($data, JSON_UNESCAPED_UNICODE));
    $conn -> close(); // 關閉資料庫連線

?>