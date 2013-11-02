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
	
	$stmt = $db_conn->stmt_init();
	if($stmt->prepare("SELECT DISTINCT(sectionid) FROM Users")) 
	{
		$stmt->execute();
		$stmt->bind_result($tmp);
		
		$secids = array();
		while($stmt->fetch()) {
			$secids[] = $tmp;
		}
		$stmt->close();
	}
?>

<html>

<body>
<center>
<form action="./CheckLogin.php" method="post" class="form">

<br> <img src="seal.gif" width="175" height="175"> 
<br><br><br>
<hr width=50%>
	<td align="center"> 
	<h3 class="body">Perception and Cognition Lab </h3><br><br><br>
	
<table cellpadding="10">
<tr>
	<td> Username </td>
	<td> <input type="text" name="username" style="width: 300px; height: 25px; border: 1px solid gray" /> </td>
</tr>
<tr>
	<td> Section </td>
	<td> <select name="sectionid" style="width:300px">
		<?php
		echo "<option value=\"\" style=\"display:none\"></option>\n"; // blank has to be first
		foreach ($secids as $secid){
			if ($secid)
				echo "<option value=\"$secid\">$secid</option>\n";			
		}
		?>
	</select>
	</td>
</tr>
<tr>
	<td> Password </td> 
	<td> <input type="password" name="password" style="width: 300px; height: 25px; border: 1px solid gray" /> </td>
</tr>
<tr>
	<td align="center" colspan="2">
	<input type="submit" value="Log In" style="width: 100px; height: 30px"/>
	<br><br>
	</td> 
</tr>

<tr>
	<td align="center" colspan="2">
	<a href="./SetUpGuest.php" class="fplink">Run as Guest</a>
	<br><br> 
	</td>
</tr>
<tr></tr>
</table>
</form>
</center>
</body>
</html>
