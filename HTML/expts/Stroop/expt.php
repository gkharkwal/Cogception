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

<form action="instructions.php" method="POST">
<table cellpadding="20">

<tr>
	<td>Run Type</td>
	<td><input type="radio" name="runType" value="demo"/>Demo &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <input type="radio" name="runType" value="expt"/>Experiment
	</td>
</tr>

<tr>
	<td>Number of Trials (a multiple of 12)</td>
	<td><input type="text" name="numTrials"/>
</tr>

</table>

<input type="reset" value="Reset"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type="submit" value="Submit"/>
</form>

</center>
</body>
</html>
