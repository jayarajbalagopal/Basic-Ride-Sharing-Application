<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}

  $result='';
  $r_id = $_POST['r_id'];

  $stmtb=$pdo->prepare("DELETE FROM requested WHERE r_id='$r_id'");
  $stmtb->execute();
  echo $stmtb->rowCount(); 
    
  



?>
