<%-- 
    Document   : login
    Created on : 23-Dec-2018
    Author     : Tarek Foyz Ahmed
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>Aston SnowDome - Login</title>
        <style type='text/css'>
            * {
                font-family: Palatino; 
            }
            body { 
                background-color: rgb(168,157,186); 
                display: flex; 
                align-items: center; 
                flex-direction: column;
            }
            #loginSection { 
                background-color: white; 
                height: 250px;
                width: 500px; 
                padding: 20px; 
                margin-top: 1%; 
                display: flex; 
                align-items: center; 
                flex-direction: column; 
                box-shadow: 0 6px 20px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
                border-radius: 8px;
            }
            #registerSection { 
                background-color: #DCDCDC; 
                height: 100%;
                width: 500px; 
                padding: 20px; 
                margin-top: 1%; 
                display: flex; 
                align-items: center; 
                flex-direction: column; 
                box-shadow: 0 6px 20px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
                border-radius: 8px;
            }
            form{
                display:flex;
                align-items: center;
                flex-direction: column;
            }
            .loginFormInput {
                padding: 5px; 
                margin: 2px; 
                width: 450px; 
                height:30px; 
                background-color: #DCDCDC; 
                border-style: none none solid none; 
                border-color: white;
            }
            .registerFormInput {
                padding: 5px; 
                margin: 2px; 
                width: 450px; 
                height:30px; 
                background-color: white; 
                border-style: none none solid none; 
                border-color: white;
            }
            input[type=submit] {
                margin-top: 10px; 
                font-size: 10pt; 
                height: 30px; 
                width: 100px; 
                background-color: rgb(168,157,186); 
                color:white;
            }
            .inactiveButton{
                opacity: 0.5;
                cursor: not-allowed;
            }
            .activeButton{
                opacity: 1;
                cursor: pointer;
            }
            .error{
                font-size: 10pt;
                color: #B22222;
            }
        </style>
        <script>
            function validateLoginFields(){
                if(document.getElementById("login_usernameInput").value.match("^([a-zA-Z0-9_-]){0,}$") == null){
                    document.getElementById("loginError").innerHTML = "Username can only contain alphanumeric characters";
                    document.getElementById("loginButton").className = "inactiveButton";
                    document.getElementById("loginButton").disabled = "disabled";                    
                }
                else if(document.getElementById("login_passwordInput").value.match("^([a-zA-Z0-9_-]){0,}$") == null){
                    document.getElementById("loginError").innerHTML = "Password can only contain alphanumeric characters";
                    document.getElementById("loginButton").className = "inactiveButton";
                    document.getElementById("loginButton").disabled = "disabled";                    
                }
                else{
                    document.getElementById("loginError").innerHTML = "";
                    document.getElementById("loginButton").className = "activeButton";
                    document.getElementById("loginButton").disabled = "";
                }
            }
            
            function validateRegisterFields(){
                if(document.getElementById("register_usernameInput").value.match("^([a-zA-Z0-9_-]){7,}$") == null){
                    document.getElementById("registerError").innerHTML = "Username must contain at least 7 alphanumeric characters";
                    document.getElementById("registerButton").className = "inactiveButton";
                    document.getElementById("registerButton").disabled = "disabled";                    
                }
                else if(document.getElementById("register_passwordInput").value.match("^([a-zA-Z0-9_-]){7,}$") == null){
                    document.getElementById("registerError").innerHTML = "Password must contain at least 7 alphanumeric characters";
                    document.getElementById("registerButton").className = "inactiveButton";
                    document.getElementById("registerButton").disabled = "disabled";                    
                }
                else{
                    checkValidUsername();
                }
            }
            
            function checkValidUsername(){
                var connection;
                
                connection = new XMLHttpRequest();

                if(connection){
                    connection.open("POST", "/coursework/do/checkName", true);
                    connection.onreadystatechange = function() {
                        updateMessage(connection);
                    }
                    connection.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                    connection.send("newUsername=" + document.getElementById("register_usernameInput").value);   
                }
                return;
            }
            
            function updateMessage(connection){
                if(connection.readyState === 4 && connection.status === 200){
                    if(connection.responseXML.documentElement.childNodes[0].data === "false"){
                        document.getElementById("registerError").innerHTML = "Username is unavailable.";
                        document.getElementById("registerButton").className = "inactiveButton";
                        document.getElementById("registerButton").disabled = "disabled";
                    }else{
                        document.getElementById("registerError").innerHTML = "";
                        document.getElementById("registerButton").className = "activeButton";
                        document.getElementById("registerButton").disabled = "";
                    };
                }
                return;
            }
        </script>
    </head>
    <body>
        <h1>Welcome to Aston SnowDome</h1>
        <div id="loginSection">
            <h3 id="loginLabel">Log in to access lessons</h3>
            <form method="POST" action="/coursework/do/login">
                <p class="error" id="loginError"></p>
                <br><input class="loginFormInput" onchange="validateLoginFields()" id="login_usernameInput" type="text" name="username" placeholder="Username"/>
                <br><input class="loginFormInput" onchange="validateLoginFields()" id="login_passwordInput" type="password" name="password" placeholder="Password"/>
                <br><input class="activeButton" id="loginButton" type="submit" value="Login"/>
            </form>
        </div>
        
        <div id="registerSection">
            <h3 id="registerLabel">Don't have an account? Register now!</h3>
            <form method="POST" action="/coursework/do/addUser">
                <p class="error" id="registerError"></p>
                <br><input class="registerFormInput" onchange="validateRegisterFields()" id="register_usernameInput" type="text" name="newUsername" placeholder="Set a username"/>
                <br><input class="registerFormInput" onchange="validateRegisterFields()" id="register_passwordInput" type="password" name="newPassword" placeholder="Set a password"/>
                <br><input class="inactiveButton" id="registerButton" disabled="disabled" type="submit" value="Register"/>
            </form>
        </div>
    </body>
</html>
