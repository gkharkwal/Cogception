<!--
 Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 
 The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 but leaves it open for academic use.
 Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 person bad karma and is thus not recommended. 
-->

<?php

require "./dbinfo.php";

$db_conn = new mysqli($dbhost, $dbuser, $dbpass, $dbname);

if (mysqli_connect_errno()) 
{
	printf("Can't connect to MySQL Server. Error code: %s\n", mysqli_connect_error());
	exit();
}

$username = $_POST['username'];
$initial_password = $_POST['password'];
$password = sha1($initial_password);
$sectionid = $_POST['sectionid'];
if(!($username && $initial_password && $sectionid)){
	header("location:index_Fail.php");
	die();
}

$count = 0;

//echo "initial_password".$initial_password;
//echo "password is: ".$password;

$stmt = $db_conn->stmt_init();

if($stmt->prepare("SELECT * FROM Users where id = ? and password = ?")) 
{
	$stmt->bind_param("ss", $username, $password);
	$stmt->execute();
	$stmt->bind_result($id, $pswd, $secid, $role);

	while($stmt->fetch()) {
		$count++;
		if ($role == "Student" && $sectionid != $secid)
			$count = 0;
	}

	$stmt->close();
}

if($count != 0)
{
	echo "Count is 1";
	session_start();
	$_SESSION['id'] = $username;
	$_SESSION['role'] = $role;
	$_SESSION['secid'] = $sectionid;
	header("location:Login_Success.php");
}
else
	header("location:index_Fail.php");
?>
