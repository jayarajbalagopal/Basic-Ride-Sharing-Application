<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}

  
  $ratings=$_POST['ratings'];
  $id = $_POST['id'];
  $comments=$_POST['comments'];

 

try {
    $pdo->beginTransaction();
    $stmt = $pdo->prepare("INSERT INTO reviews (rev_id,ratings) VALUES (?,?)");

        $stmt->execute([$id,$ratings]);
        $pdo->commit();
        
        $pdo->beginTransaction();
        $stmt = $pdo->prepare("INSERT INTO comments (com_id,comments) VALUES (?,?)");

        $stmt->execute([$id,$comments]);
        $pdo->commit();
        
        echo $stmt->rowCount();

}
catch (Exception $e){
   
    $pdo->rollback();
    throw $e;
}
?>
