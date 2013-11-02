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

if($_SESSION['id'] == 'guest'){
	// he shouldn't be here!
	header('location:./expt.php');
	// bye, bye!
}
?>

<html>

<head>
	<title>Decision Making Experiment</title>
</head>


<body>
<table width=100%> <tr>
<td align="left"> <a href="../../home.php">Home</a> </td>
<td align="right"><?php if($_SESSION['id'] == 'guest'){ echo "<a href=\"../../index.php\">Login</a>"; } else { echo "<a href=\"../../Logout.php\">Logout</a>";} ?> </td>
</tr> </table>
<center>
<h3>Decision Making Experiment - Data</h3>
<br/><hr/>
<br/>

<table cellpadding="5">

<?php
if($_SESSION['role'] == 'Instructor'){
	//check if data folder exists, crash otherwise!
	if(!is_dir('../../data')){
		die('There is no data folder!');
	}
	
	// check if section's data folder exists
	if(!is_dir("../../data/".str_replace(':','-',$_SESSION['secid']))){
		die("There is no data folder for ". $_SESSION['secid']);
	}

	
	$h1 = opendir('../../data/'.str_replace(':','-',$_SESSION['secid']).'/');

	while (false !== ($user = readdir($h1))) {
		if ($user == '.' || $user == '..')
			continue;

		$path = '../../data/'.str_replace(':','-',$_SESSION['secid']).'/'.$user.'/DecisionMaking/';

		$first = true;
		if(is_dir($path)){ // just being safe
			$h2 = opendir($path);
			while (false !== ($opFile = readdir($h2))) {
				if($opFile == '.' || $opFile == '..')
					continue;
					
				if ($first){
					$first = false;
					echo '<tr><td colspan="2">';
					echo "<b>$user</b>";
					echo "</td></tr>\n";
					echo "<tr>\n<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n<td>";
					echo "<ol>";
				}
				echo "<li><a href=\"".$path.$opFile.'">'.$opFile.'</a></li>';
				if (!strpos($opFile, '.csv',1)){
					echo '(this file shouldn\'t have been displayed!)';
				}
			}
			echo "</ol>\n";
		}
		echo "</td></tr>\n";
	}
}
else {
	$path = '../../data/'.str_replace(':','-',$_SESSION['secid']).'/'.$_SESSION['id'].'/DecisionMaking';
		
	if(is_dir($path)){ // just being safe
		$h2 = opendir($path);
		$first = true;
		while (false !== ($opFile = readdir($h2))) {
			if($opFile == '.' || $opFile == '..')
				continue;
				
			if ($first){
				$first = false;
				echo '<tr><td>';
				echo '<ol>';
			}
			echo '<li><a href="'.$path.'/'.$opFile.'">'.$opFile.'</a></li>';
			if (!strpos($opFile, '.csv',1)){
				echo '(this file shouldn\'t have been displayed!)';
			}
		}
		echo '</ol>';
		echo '</td></tr>';
	}
}
?>

</table>

</center>
</body>
</html>
