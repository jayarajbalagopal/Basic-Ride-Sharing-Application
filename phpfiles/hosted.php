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
  $host_id =$_POST['host_id'];
  $r_id =$_POST['r_id'];
  $nos=$_POST['nos'];
 

try {
    $pdo->beginTransaction();
    $stmt = $pdo->prepare("INSERT INTO service_enabled (host_id,r_id,nos) VALUES (?,?,?)");

        $stmt->execute([$host_id,$r_id,$nos]);
        $pdo->commit();
       
}
catch (Exception $e){
   
    $pdo->rollback();
    throw $e;
}
 if($stmt->rowCount()){
   $stmta=$pdo->prepare("UPDATE hosting SET nos=nos-'$nos' WHERE host_id='$host_id'");
   $stmta->execute();
   if($stmta->rowCount()){
              try {
               $pdo->beginTransaction();
               $stmt = $pdo->prepare("INSERT INTO buffer SELECT * FROM hosting WHERE nos=0 ");

               $stmt->execute();
               $pdo->commit();
              }
              catch (Exception $e){
               $pdo->rollback();
               throw $e;
              }

          $stmtb=$pdo->prepare("DELETE FROM hosting WHERE nos=0");
          $stmtb->execute();
          $stmtc=$pdo->prepare("DELETE FROM requested WHERE r_id='$r_id'");
          $stmtc->execute();
          if($stmtb->rowCount()){
          $stmtd=$pdo->prepare("DELETE FROM requested WHERE host_id='$host_id'");
          $stmtd->execute();
          }
          echo $stmta->rowCount(); 
          }
  }



?>
