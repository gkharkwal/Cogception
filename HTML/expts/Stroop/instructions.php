<!--
 Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 
 The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 but leaves it open for academic use.
 Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 person bad karma and is thus not recommended. 
-->

<?php
session_start();
if(!isset($_SESSION['id'])){
	header('location:../../index.php');
}

if (!isset($_REQUEST['numTrials']) or !isset($_REQUEST['runType'])){
	header('location:./expt.php');
}

$numTrials = $_REQUEST['numTrials'];
$runType   = $_REQUEST['runType'];

if(!is_dir("../../data")){
	// data folder has to exist
	die('There is no data folder');
}

if($_SESSION['id'] != "guest"){
	$path = "../../data/".str_replace(':','-',$_SESSION['secid']);
	if(!is_dir($path)){
		// section's data folder doesn't exist
		// make it
		$ret = mkdir($path, 0777);
		if (!$ret)
			die('Failed to create data folder');
	}

	$path = $path."/".$_SESSION['id'];
}
else{
	$path = "../../data/".$_SESSION['id'];
}
if(!is_dir($path)){
	// user's data folder doesn't exist
	// make it
	$ret = mkdir($path, 0777);
	if (!$ret)
		die('Failed to create data folder');
}

$path = $path."/Stroop";
if(!is_dir($path)){
	// user's data folder corresponding to Stroop doesn't exist
	// make it
	$ret = mkdir($path, 0777);
	if (!$ret)
		die('Failed to create data folder');
}

if($_REQUEST['runType'] == 'demo')
	$fname = $path."/demoOutput.csv";
else {
	$fname = $path."/exptOutput";

	$count = 1;
	while(file_exists($fname."_".strval($count).".csv")){
		$count = $count + 1;
	}
	$fname = $fname."_".strval($count).".csv";
}
$_SESSION['stroop_op'] = $fname;
// create session variable corresponding to the output file
?>

<html>
<head>
	<title>Stroop Experiment</title>
</head>

<body>
<table width=100%> <tr>
<td align="left"> <a href="../../home.php">Home</a> </td>
<td align="right"><?php if($_SESSION['id'] == 'guest'){ echo "<a href=\"../../index.php\">Login</a>"; } else { echo "<a href=\"../../Logout.php\">Logout</a>";} ?> </td>
</tr> </table>

<center>
<h3> Stroop Experiment </h3>
<br/><hr/>
<br/>
<table width=400>
<tr><td>
<?php
	$f = fopen('instructions.txt', 'r');
	while(!feof($f))
	{
		echo fgets($f). "<br />";
	}
	fclose($f);
?>
</td></tr>
</table>
<br/>
<br/>
<form action="runApplet.php" method="POST">
<input type="hidden" name="numTrials" value="<?php echo $numTrials;?>"/>
<input type="hidden" name="runType" value="<?php echo $runType;?>"/>

<input type="submit" value="Continue"/>
</form>
</center>
</body>
</html>