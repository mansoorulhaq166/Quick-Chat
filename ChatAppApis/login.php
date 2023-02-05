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
$email = $_POST['email'];
$password = $_POST['password'];

// Prepare the query to prevent SQL injection
$query = "SELECT * FROM user_tbl WHERE email = ? AND password = ?";
$stmt = $conn->prepare($query);

// Bind the parameters to the query
$stmt->bind_param("ss", $email, $password);

// Execute the query
$stmt->execute();

// Get the result from the query
$result = $stmt->get_result();

// Check if the query was successful
if ($result->num_rows > 0) {
    $user = $result->fetch_assoc();
    $response['message'] = "success";
   // $response['user'] = $user;
} else {
    $response['message'] = "failed";
}

// Return the response
//header('Content-Type: application/json');
echo json_encode($response);

// Close the database connection
$conn->close();

?>