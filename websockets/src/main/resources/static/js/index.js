let websocket = new WebSocket("ws://localhost:8080/queue");
const text = $("#text");
const queue = $('#queue');
const connectionDiv = $('#connection-div');

function sendMessage() {
    const message = {
        "command": "send",
        "body": {
            "text": text.val()
        }
    };
    websocket.send(JSON.stringify(message));
}

function connect() {
    const value = queue.val();
    const message = {
        "command": "subscribe",
        "queueName": value
    };
    websocket.send(JSON.stringify(message));
    connectionDiv.empty();
    connectionDiv.append("Connected to queue ".concat(value));
}

function disconnect() {
    websocket.close();
    connectionDiv.empty();
    connectionDiv.append("Connect to queue: ");
    connectionDiv.append("<input id=\"queue\" type=\"text\">");
    connectionDiv.append("<button onclick=\"connect()\">Connect</button>");
}

websocket.onmessage = (event) => {
    const data = JSON.parse(event.data);

    alert(JSON.stringify(data));

    const receiveMessage = {
        "command" : "accepted",
        "messageId" : data.messageId
    };

    websocket.send(JSON.stringify(receiveMessage));

    setTimeout( () => {
        const completedMessage = {
            "command" : "completed",
            "messageId" : data.messageId
        };
        websocket.send(JSON.stringify(completedMessage));
    }, 10000)
};


