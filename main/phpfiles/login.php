<?php

try{
$pdo=new PDO('mysql:host=localhost;dbname=carpool','kannan','password');
}
catch(PDOException $e)
{
 echo 'Error:' . $e->getMessage();
exit();
}



 $email =$_POST['email'];
 $password =$_POST['password'];
 $fcm=$_POST['fcm'];

 $stmt=$pdo->prepare("SELECT user_id,username FROM user_info WHERE email='$email' AND password='$password'");
 $stmt->execute();
 
 if($stmt->rowCount()){
            $stmta=$pdo->prepare("UPDATE user_info SET fcm='$fcm' WHERE email='$email'");
            $stmta->execute();
            $row_all = $stmt->fetchall(PDO::FETCH_ASSOC);
	    header('Content-type: application/json');
   	    echo json_encode($row_all);
 }else{

 echo "failure";
 }

?>
