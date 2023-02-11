<?php

// Connect to the database
$host = "localhost";
$username = "root";
$password = "";
$dbname = "chatapp";
// Create a connection
$conn = new mysqli($host, $username, $password, $dbname);

// Check the connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
    }

$sender_email = $_POST['sender_email'];
$receiver_email = $_POST['receiver_email'];
$message = $_POST['message'];

$sql = "INSERT INTO tbl_msgs(sender_email, receiver_email, message)
    VALUES ('$sender_email', '$receiver_email', '$message')";

if (mysqli_query($conn, $sql)) {
    $response['status'] = "success";
   // echo json_encode(array("status" => "success"));
  } else {
    $response['message'] = "fail";
 //   echo json_encode(array("status" => "fail"));
  }
  
  echo json_encode($response);
 
  mysqli_close($conn);
?>