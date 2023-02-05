<?php

$host = "localhost";
$username = "root";
$password = "";
$dbname = "chatapp";

$conn = new mysqli($host, $username, $password , $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$result = mysqli_query($conn, "SELECT * FROM user_tbl");

 $data = array();
 while ($row = mysqli_fetch_assoc($result)) {
     $data[] = $row;
 }

 echo json_encode($data);

mysqli_close($conn);

?>