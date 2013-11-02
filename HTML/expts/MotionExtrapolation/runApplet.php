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
$fname 	   = $_SESSION['extrap_op'];
?>

<html>
<head>
	<title>Motion Extrapolation Experiment</title>
</head>

<body>
<table width=100%> <tr>
<td align="left"> <a href="../../home.php">Home</a> </td>
<td align="right"><?php if($_SESSION['id'] == 'guest'){ echo "<a href=\"../../index.php\">Login</a>"; } else { echo "<a href=\"../../Logout.php\">Logout</a>";} ?> </td>
</tr> </table>
<center>
<applet name="extrap" code="MotionExtrapolation.class" 
	archive="extrap.jar"
	 width="800" height="600">
	<param name="runType" value="<?php echo $runType; ?>" \>
	<param name="numTrials" value="<?php echo $numTrials; ?>" \>
	<param name="filePath" value="<?php echo $fname; ?>" \>
	<?php
	/*	if(isset($_REQUEST['velocity']) && $_REQUEST['velocity'] != "")
			echo '<param name="velocity" value="'.$_REQUEST['velocity'].'" \>';
		if(isset($_REQUEST['discRadius']) && $_REQUEST['discRadius'] != "")
			echo '<param name="discRadius" value="'.$_REQUEST['discRadius'].'" \>';
		if(isset($_REQUEST['pathRadius']) && $_REQUEST['pathRadius'] != "")
			echo '<param name="pathRadius" value="'.$_REQUEST['pathRadius'].'" \>';
		if(isset($_REQUEST['pathLength']) && $_REQUEST['pathLength'] != "")
			echo '<param name="pathLength" value="'.$_REQUEST['pathLength'].'" \>';
		//if(isset($_REQUEST['orientationRange']) && $_REQUEST['orientationRange'] != "")
		//	echo '<param name="orientationRange" value="'.$_REQUEST['orientationRange'].'" \>';
	*/
	?>
</applet>
</center>
</body>
</html>
