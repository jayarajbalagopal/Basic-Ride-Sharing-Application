<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}


         $host_id=$_POST['host_id'];
         
         $stmt=$pdo->prepare("SELECT username,email,gender,address,age,carname,AVG(ratings) FROM user_info,reviews WHERE user_id='$host_id' AND rev_id=user_id");
         
         $stmt->execute();
         
      
      if($stmt->rowCount())
      {
	    $row_all = $stmt->fetchall(PDO::FETCH_ASSOC);
	    header('Content-type: application/json');
   	    echo json_encode($row_all);
      }  
          elseif(!$stmt->rowCount())
      {
	    echo "no rows"; 
      }
      
       
?>
