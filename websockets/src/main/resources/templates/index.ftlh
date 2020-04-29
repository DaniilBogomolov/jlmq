<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.0/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js"></script>
    <title>Document</title>
</head>
<body>
<div id="connection-div">
    Connect to queue!
    <input id="queue" type="text">
    <button onclick="connect()">Connect</button>
</div>
<hr>
<input id="text" type="text" placeholder="Message text">
<button onclick="sendMessage()">Send</button>
<script src="js/index.js"></script>
<hr>
<button onclick="disconnect()">Disconnect</button>
</body>
</html>