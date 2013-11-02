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
		header("location:index.php");
	}
?>

<html>

<body>
<table width=100%> <tr>
<td align="left"> <a href="./home.php">Home</a> </td>
<td align="right"><?php if($_SESSION['id'] == 'guest'){ echo "<a href=\"./index.php\">Login</a>"; } else { echo "<a href=\"./Logout.php\">Logout</a>";} ?> </td>
</tr> </table>
<center>
<img src="seal.gif" width="175" height="175"> 
<br><br>
<hr width=50% color=#333366>
	<h2 class="body">Perception and Cognition Lab </h2>
	<br/>

	<table cellpadding="3">
	
	<tr>
		<td><b style="font-size:large">Average Disc Size:</b></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td><td><a href="./expts/AverageDisc/expt.php">Run Experiment</a></td>
		<td>
		<?php if($_SESSION['id']!="guest") echo '<a href="./expts/AverageDisc/data.php">View Data</a>'?>
		</td>
	</tr>

	<tr>
		<td style="padding-top:15px"><b style="font-size:large">Category Learning:</b></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td><td><a href="./expts/Categorization/expt.php">Run Experiment</a></td>
		<td>
		<?php if($_SESSION['id']!="guest") echo '<a href="./expts/Categorization/data.php">View Data</a>'?>
		</td>
	</tr>

	<tr>
		<td style="padding-top:15px"><b style="font-size:large">Decision Making:</b></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td><td><a href="./expts/DecisionMaking/expt.php">Run Experiment</a></td>
		<td>
		<?php if($_SESSION['id']!="guest") echo '<a href="./expts/DecisionMaking/data.php">View Data</a>'?>
		</td>
	</tr>
	
	<tr>
		<td style="padding-top:15px"><b style="font-size:large">Motion Extrapolation:</b></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td><td><a href="./expts/MotionExtrapolation/expt.php">Run Experiment</a></td>
		<td>
		<?php if($_SESSION['id']!="guest") echo '<a href="./expts/MotionExtrapolation/data.php">View Data</a>'?>
		</td>
	</tr>

	<tr>
		<td style="padding-top:15px"><b style="font-size:large">Object Stability:</b></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td><td><a href="./expts/ObjectStability/expt.php">Run Experiment A: (Stability task)</a></td>
		<td>
		<?php if($_SESSION['id']!="guest") echo '<a href="./expts/ObjectStability/data.php">View Data</a>'?>
		</td>
	</tr>
	<tr>
		<td></td><td><a href="./expts/COMTask/expt.php">Run Experiment B: (COM task)</a></td>
		<td>
		<?php if($_SESSION['id']!="guest") echo '<a href="./expts/COMTask/data.php">View Data</a>'?>
		</td>
	</tr>
	
	<tr>
		<td style="padding-top:15px"><b style="font-size:large">Stroop:</b></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td><td><a href="./expts/Stroop/expt.php">Run Experiment</a></td>
		<td>
		<?php if($_SESSION['id']!="guest") echo '<a href="./expts/Stroop/data.php">View Data</a>'?>
		</td>
	</tr>

	<tr>
		<td style="padding-top:15px"><b style="font-size:large">Tone Discrimination:</b></td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td></td><td><a href="./expts/ToneDiscrimination/expt.php">Run Experiment</a></td>
		<td>
		<?php if($_SESSION['id']!="guest") echo '<a href="./expts/ToneDiscrimination/data.php">View Data</a>'?>
		</td>
	</tr>

<!--	<?php
	/*	$fname = "expts/active_expts.txt";
		$expts = array_map('rtrim', file($fname, FILE_SKIP_EMPTY_LINES));
	
		echo "<tr><td>";
		echo "<ul>";
		foreach($expts as $line){
			//echo "\t\t<a href=\"./expts/".$line."/\"/>".$line;
			echo "\t\t<li><h3>".str_replace("_"," ",$line).":</h3></li>";
			echo "\t\t<ul>";
			echo "\t\t<li><a href=\"./expts/".$line."/expt.php\">Run Expt</a></li><br/>";

			if ($_SESSION['id'] != "guest"){
				// everyone except the guest user can access their data
				echo "\t\t<li><a href=\"./expts/".$line."/data.php\">View Data</a></li><br/>";
			}
			echo "\t\t</ul>";
		}
		echo "</ul>";
		echo "</td></tr>"; */
?>
	-->
	</table>

</center>
</body>

</html>
