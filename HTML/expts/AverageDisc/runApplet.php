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
$fname 	   = $_SESSION['avgDisc_op'];
?>

<html>
<head>
	<title>Average Disc Size Experiment</title>
</head>

<body>
<table width=100%> <tr>
<td align="left"> <a href="../../home.php">Home</a> </td>
<td align="right"><?php if($_SESSION['id'] == 'guest'){ echo "<a href=\"../../index.php\">Login</a>"; } else { echo "<a href=\"../../Logout.php\">Logout</a>";} ?> </td>
</tr> </table>
<center>
<applet name="avgDisc" code="AverageDisc.class" 
	archive="avgDisc.jar"
	 width="800" height="600">
	<param name="runType" value="<?php echo $runType; ?>" \>
	<param name="numTrials" value="<?php echo $numTrials; ?>" \>
	<param name="filePath" value="<?php echo $fname; ?>" \>
</applet>
</center>
</body>
</html>
