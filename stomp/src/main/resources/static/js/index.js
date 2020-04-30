let socket = new SockJS("/websocket");
const text = $("#text");
const queue = $('#queue');
const connectionDiv = $('#connection-div');
const stomp = Stomp.over(socket);
const queueValue = location.pathname.substring(1);
const subscriptionMessage = {
    "command": "subscribe",
    "queueName": queueValue
};

stomp.connect({}, function () {
    stomp.subscribe("/topic/" + queueValue, function (msg) {
        onmessage(JSON.parse(msg.body));
    })
});

function send(message) {
    stomp.send("/app/" + queueValue, {}, JSON.stringify(message));
}


function sendMessage() {
    const message = {
        "command": "send",
        "body": {
            "text": text.val()
        }
    };
    send(message);
    // stomp.send("/app/" + queueValue, {}, JSON.stringify(message));
}

function onmessage(message) {
    for (var variable in message) {
        const messageId = JSON.parse(message[variable].payload).messageId;

        const receiveMessage = {
          "command" : "accepted",
          "messageId" : messageId
        };
        send(receiveMessage);

        setTimeout(() => {
            const completedMessage = {
              "command" : "completed",
              "messageId" : messageId
            };
            send(completedMessage);
        }, 10000)
    }
}

function connect() {
    send(subscriptionMessage);
}

function disconnect() {
    socket.close();
    connectionDiv.empty();
    connectionDiv.append("Connect to queue: ");
    connectionDiv.append("<input id=\"queue\" type=\"text\">");
    connectionDiv.append("<button onclick=\"connect()\">Connect</button>");
}
