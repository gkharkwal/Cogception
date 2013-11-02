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
if(isset($_SESSION['id'])){
	unset($_SESSION['id']);
	session_destroy();
}
else{
	header("location:index.php");
}

?>

<html>
<body>

<center>
<table width="90%" height="100%" bgcolor="white" border="0">
<tr height="30%"> <th colspan="2"> <br><br> <img src="seal.gif" width="175" height="175"> 
<br><br><br>
<hr width=75% color=#333366> </th> </tr>

<tr> 
	<td align="center" class="td"> 
	&nbsp;&nbsp;&nbsp; <h4 class="body">You have successfully logged out of the system. </h4>
	<br> <br>
	<a href="./index.php">Log in</a>
	<br><br> <br><br> <br><br> <br><br> <br><br> <br><br> 
	</td> 
</tr>
</table>
</center>
</body>

</html>
