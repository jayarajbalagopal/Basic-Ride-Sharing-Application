<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}

   $r_id=$_POST['r_id'];

 
   $stmta=$pdo->prepare("SELECT * FROM service_enabled WHERE r_id='$r_id'");
   $stmta->execute();
   if($stmta->rowCount()){
          echo $stmta->rowcount(); 
          }
   else{
        $stmtb=$pdo->prepare("SELECT * FROM requested WHERE r_id='$r_id'");
        $stmtb->execute();
        if($stmtb->rowCount()){
         echo 2;}
        else 
         echo $stmtb->rowcount();
    }
  
?>
