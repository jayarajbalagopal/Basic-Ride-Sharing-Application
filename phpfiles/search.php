<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}
     if(isset($_POST['date']))
     {
         $flat=$_POST['flat'];
         $flng=$_POST['flng'];
         $tlat=$_POST['tlat'];
         $tlng=$_POST['tlng'];
         $date=$_POST['date'];
         $nos=$_POST['nos'];
      
         
         $stmt=$pdo->prepare("SELECT username,host_id,email,flocation,tlocation,date,nos,(3959*acos(cos(radians('$flat'))*cos(radians(flat))*cos( radians(flng)-radians('$flng'))+sin(radians('$flat'))*sin(radians(flat)))) AS distance,(3959*acos(cos(radians('$tlat'))*cos(radians(tlat))*cos( radians(tlng)-radians('$tlng'))+sin(radians('$tlat'))*sin(radians(tlat)))) AS distanceto FROM user_info,hosting WHERE nos>='$nos' AND date='$date' AND user_id=host_id HAVING distance < 2 AND distanceto < 2.5");
         
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
