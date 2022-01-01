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
  $host_id = $_POST['host_id'];
  $flocation = $_POST['flocation'];
  $flat = $_POST['flat'];
  $flng = $_POST['flng'];
  $tlocation = $_POST['tlocation'];
  $tlat = $_POST['tlat'];
  $tlng = $_POST['tlng'];
  $date = $_POST['date'];
  $nos = $_POST['nos'];
 

try {
    $pdo->beginTransaction();
    $stmt = $pdo->prepare("INSERT INTO hosting (host_id,flocation,flat,flng,tlocation,tlat,tlng,date,nos) VALUES (?,?,?,?,?,?,?,?,?)");

        $stmt->execute([$host_id,$flocation,$flat,$flng,$tlocation,$tlat,$tlng,$date,$nos]);
        $pdo->commit();
        echo $stmt->rowCount();
       

}
catch (Exception $e){
   
    $pdo->rollback();
    throw $e;
}
?>
