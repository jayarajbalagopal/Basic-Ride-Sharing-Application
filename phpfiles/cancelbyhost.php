<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}

  $host_id = $_POST['host_id'];


  $stmtb=$pdo->prepare("DELETE FROM requested WHERE host_id='$host_id'");
  $stmtb->execute();
  
  $stmtc=$pdo->prepare("DELETE FROM service_enabled WHERE host_id='$host_id'");
  $stmtc->execute();

  $stmtd=$pdo->prepare("DELETE FROM hosting WHERE host_id='$host_id'");
  $stmtd->execute();

  echo $stmtd->rowCount(); 

?>
