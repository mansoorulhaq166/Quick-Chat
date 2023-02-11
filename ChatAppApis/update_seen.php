<?php

// Connect to the database
$host = "localhost";
$username = "root";
$password = "";
$dbname = "chatapp";
$table_name = "tbl_msgs";

// Create a connection
$conn = new mysqli($host, $username, $password, $dbname);

// Check the connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
$message_id = $_POST['message_id'];
$sql = "UPDATE tbl_msgs SET seen_status=1 WHERE message_id='$message_id'";

if (mysqli_query($conn, $sql) && mysqli_affected_rows($conn) > 0) {
    $response['message'] = "success";
} else {
    $response['message'] = "fail";
}

echo json_encode($response);

mysqli_close($conn);
?>