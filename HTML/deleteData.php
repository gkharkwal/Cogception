<!--
 Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 
 The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 but leaves it open for academic use.
 Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 person bad karma and is thus not recommended. 
-->

<?php 
// ensure $dir ends with a slash 
function delTree($dir) { 
    $files = glob( $dir . '*', GLOB_MARK ); 
    foreach( $files as $file ){ 
        if( substr( $file, -1 ) == '/' ) 
            delTree( $file ); 
        else 
            unlink( $file ); 
    } 
    rmdir( $dir ); 
} 

delTree('./data/');
echo 'data folder deleted...';
?>
