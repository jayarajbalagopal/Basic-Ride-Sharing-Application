<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}


         $req_id=$_POST['r_id']; 
   
         $stmt=$pdo->prepare("SELECT host_id FROM service_enabled WHERE r_id='$req_id'");
         $stmt->execute();
         $host_id=$stmt->fetchColumn(0);


  
         $stmt=$pdo->prepare("SELECT username,email,gender,user_id FROM user_info WHERE user_id='$host_id'");
         
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
