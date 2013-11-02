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
<h3> Motion Extrapolation Experiment </h3>
<br/><hr/>
<br/>

<form action="instructions.php" method="POST">
<table cellpadding="20">

<tr>
	<td>Run Type</td>
	<td><input type="radio" name="runType" value="demo"/>Demo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <input type="radio" name="runType" value="expt"/>Experiment
	</td>
</tr>

<tr>
	<td>Number of Trials (multiple of 12)</td>
	<td><input type="text" name="numTrials"/>
</tr>

<!--
<tr>
	<td>Velocity (1 - 30)</td>
	<td><input type="text" name="velocity"/>
</tr>

<tr>
	<td>Radius of Disc (100 - 200)</td>
	<td><input type="text" name="discRadius"/>
</tr>

<tr>
	<td>Length of the Visible Trajectory (100 - 240)</td>
	<td><input type="text" name="pathLength"/>
</tr>

<tr>
	<td>Radius of Curvature of Path (greater than disc radius; 0 => linear path.)</td>
	<td><input type="text" name="pathRadius"/>
</tr>

<tr>
	<td>Orientation Range (0 - 45, in multiples of 5)</td>
	<td><input type="text" name="orientationRange"/>
</tr>
-->

</table>

<input type="reset" value="Reset"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type="submit" value="Submit"/>
</form>

</center>
</body>
</html>
