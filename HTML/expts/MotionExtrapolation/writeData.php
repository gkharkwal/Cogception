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

if (isset($_REQUEST['output'])){
	// error checking
	if(!isset($_REQUEST['fp']))
		die('Error: output file path not set!');

	$fname = $_REQUEST['fp'];
	$of = fopen($fname,"w");
	fwrite($of, $_REQUEST['output']);
	fclose($of);
	echo ('output file generated.');
}
?>
