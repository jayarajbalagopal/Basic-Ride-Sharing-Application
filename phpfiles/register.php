<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}

$username=$_POST['username'];
$email=$_POST['email'];
$password=$_POST['password'];
$age=$_POST['age'];
$gender=$_POST['gender'];
$address=$_POST['address'];
$carname=$_POST['carname']; 
$fcm=$_POST['fcm'];         

    


try {
    $pdo->beginTransaction();
   
        
    $stmt = $pdo->prepare("INSERT INTO user_info (username,password,email,gender,address,age,carname,fcm) VALUES (?,?,?,?,?,?,?,?)");
        $stmt->execute([$username,$password,$email,$gender,$address,$age,$carname,$fcm]); 
        $pdo->commit();
        
       

     }
catch (Exception $e){
   
    $pdo->rollback();
    throw $e;
}


if($stmt->rowCount())
   {
    echo "Successfully Registered";
   }
   else
   {
   echo "Could not Register";
   }

?>
