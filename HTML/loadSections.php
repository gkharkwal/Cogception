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

$query = "CREATE TABLE IF NOT EXISTS `Users`(	
	`id` varchar(50) PRIMARY KEY,
	`password` varchar(100),
	`sectionid` varchar(100),
	`role` varchar(50)
)";

if ($db_conn->query($query) === true){
	printf("Table successfully created!<br/><br/>\n");
}
else {
	echo "ERROR: ".$db_conn->error;
}

$query = "DELETE FROM `Users`";
if ($db_conn->query($query) === true){
	printf("Table cleared!<br/><br/>\n");
}
else {
	echo "ERROR: ".$db_conn->error;
}

// open files in sectioninfo folder and load details from them
if(false !== ($handle = opendir('./sectioninfo'))) {
	while (false !== ($entry = readdir($handle))) {
		// skip . and ..
		if ($entry == "." || $entry == "..")
			continue;
			
		// read file from the list of files in the directory
		$header = false;
		if (false !== ($fp = fopen('./sectioninfo/'.$entry, "r"))) {
			while (false !== ($data = fgetcsv($fp))) {
				if($header == false){
					$header = true;
					continue;
				}
				
				$num = count($data);
				for ($c=0; $c < $num; $c++) {
					echo "\"".$data[$c] . "\", ";
				}
				echo "<br />\n";
				
				$stmt = $db_conn->stmt_init();
				if(false !== ($stmt->prepare("INSERT INTO `Users` VALUES (?,?,?,?)"))) {
					$id = $data[1];
					$role = $data[4];
					$secid = $data[2];	
					if ($role == "Student"){
						$password = sha1("rutgers123");
						$toks = explode(":",$secid);
						$l = count($toks)-1; // spurious : at the end
						$secid = $toks[$l-2].":".$toks[$l-1];
						/* for ($i=2; $i<count($toks)-1; $i++){
							$secid = $secid.$toks[$i];
							if ($i<count($toks)-2)
								$secid = $secid.":";
						} */
					}
					else {
						$password = sha1("lingftw");
					}
					
					echo "<br/>$id $role $secid $password<br/>";
					$stmt->bind_param("ssss",$id,$password,$secid,$role);
					$stmt->execute();
					$stmt->close();
				}
				else{
					echo "ERROR";
				}
			}
			
			fclose($fp);
		}
	}
	
	closedir($handle);
}

$db_conn->close();
?>
