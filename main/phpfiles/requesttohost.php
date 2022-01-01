<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}
     if(isset($_POST['host_id']))
    {     
       $host_id=$_POST['host_id'];
      
         
         $stmt=$pdo->prepare("SELECT username,email,nos,r_id from user_info,requested WHERE host_id='$host_id' AND user_id=r_id");
         
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
      
      }	  
?>
