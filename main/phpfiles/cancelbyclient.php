<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}

  $req_id = $_POST['req_id'];
  
  $stmtc=$pdo->prepare("DELETE FROM service_enabled WHERE r_id='$req_id'");
  $stmtc->execute();

  echo $stmtc->rowCount(); 

?>
