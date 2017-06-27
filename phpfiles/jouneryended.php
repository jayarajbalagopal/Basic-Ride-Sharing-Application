<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}

 
  $host_id =$_POST['host_id'];
  
  $stmt=$pdo->prepare("SELECT * FROM service_enabled WHERE host_id='$host_id'");
  $stmt->execute();
 


 if($stmt->rowCount()){
    echo 1;
  }
 else {
  echo 0;
  }


?>
