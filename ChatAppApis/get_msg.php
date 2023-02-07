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

//$sender_email = mysqli_real_escape_string($conn, $_GET['sender_email']);
//$receiver_email = mysqli_real_escape_string($conn, $_GET['receiver_email']);

$qry = "SELECT * FROM $table_name ";
        // -- WHERE (sender_email = '$sender_email' AND receiver_email = '$receiver_email')";

$result = mysqli_query($conn, $qry);

$data = array();
while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}
echo json_encode($data);

mysqli_close($conn);

?>
