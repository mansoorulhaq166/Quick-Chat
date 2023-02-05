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

// Get the user input from the API request
$name = $_POST['name'];
$email = $_POST['email'];
$profile_pic = $_POST['profile_pic'];
$mobile = $_POST['mobile'];
$password = $_POST['password'];
$status = $_POST['status'];

// Check if email already exists
$check_email_query = "SELECT * FROM user_tbl WHERE email = ?";
$check_stmt = $conn->prepare($check_email_query);
$check_stmt->bind_param("s", $email);
$check_stmt->execute();
$result = $check_stmt->get_result();

$img_name = "IMG".rand().".jpg";
file_put_contents("images/".$img_name, base64_decode($profile_pic));

if ($result->num_rows > 0) {
    $response['message'] = "Email exists";
} else {
 
// Prepare the query to prevent SQL injection
$query = "INSERT INTO user_tbl (name, email, profile_pic, mobile, password, status) VALUES (?,?, ?, ?, ?, ?)";
$stmt = $conn->prepare($query);

// Bind the parameters to the query
$stmt->bind_param("ssssss", $name, $email, $img_name, $mobile, $password, $status);

// Execute the query
$stmt->execute();
// Check if the query was successful
if ($stmt->affected_rows > 0) {
    $response['message'] = "registered";
} else {
    $response['message'] = "failed";
 }
}
echo json_encode($response);

$conn->close();
?>