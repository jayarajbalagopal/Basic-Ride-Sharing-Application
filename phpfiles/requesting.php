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
  $r_id = $_POST['r_id'];
  $nos=$_POST['nos'];
 

try {
    $pdo->beginTransaction();
    $stmt = $pdo->prepare("INSERT INTO requested (r_id,host_id,nos) VALUES (?,?,?)");

        $stmt->execute([$r_id,$host_id,$nos]);
        $pdo->commit();
        echo $stmt->rowCount();
       

}
catch (Exception $e){
   
    $pdo->rollback();
    throw $e;
}
?>
