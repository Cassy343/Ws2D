(function() {

const servers = [
    '50.4.106.119',
    '127.0.0.1'
];
const port = 3272;
const packets = {
    'heartbeat': 0,
    'uid_assign': 1
};

var client = {
    uid: -1,
    canvas: document.getElementById('game-canvas'),
    ctx: null,
    socket: null,
    loop: null,
    nodes: []
};

// For testing
client.nodes.push({
    x: 12000,
    y: 12000,
    w: 50,
    h: 50
});

function start() {
    connect(0);
    window.addEventListener('resize', resizeCanvas, false);
    resizeCanvas();
    client.ctx = client.canvas.getContext('2d');
    update();
}

function resizeCanvas() {
    client.canvas.width = window.innerWidth;
    client.canvas.height = window.innerHeight;
}

function connect(serverIdx) {
    if(serverIdx >= servers.length)
        serverIdx = 0;
    client.socket = new window.WebSocket('ws://' + servers[serverIdx] + ':' + port);
    client.socket.binaryType = 'arraybuffer';
    client.socket.onopen = function(event) {
        console.log('Connected to server.');
    };
    client.socket.onmessage = function(event) {
        handleMessage(event.data);
    };
    client.socket.onclose = function(event) {
        console.log('Disconnected.');
    };
    window.setTimeout(function(socket, connectFn, currentServerIdx) {
        if(socket.readyState !== socket.OPEN) {
            socket.close();
            connectFn(currentServerIdx + 1);
        }
    }, 1500, client.socket, connect, serverIdx);
}

function disconnect() {
    client.socket.close();
}

function sendUserInput() {
    
}

function sendBytes(packetType, bytes) {
    var buffer = new Uint8Array(new ArrayBuffer(2 + bytes.length));
    buffer[0] = packets[packetType];
    buffer[1] = client.uid;
    if(bytes.length > 0) {
        for(var i = 0;i < bytes.length;++ i) {
            buffer[i + 2] = bytes[i];
        }
    }
    client.socket.send(buffer.buffer);
}

function handleMessage(data) {
    var view = new DataView(data);
    switch(view.getUint8(0)) {
        case packets['heartbeat']:
            sendBytes('heartbeat', []);
            break;
        case packets['uid_assign']:
            client.uid = view.getUint8(1);
            break;
    }
}

function render() {
    var canvas = client.canvas, ctx = client.ctx;
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    
    ctx.fillStyle = "#a7a7a7";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    
    ctx.translate((client.canvas.width / 2.0) - client.nodes[0].x, (client.canvas.height / 2.0) - client.nodes[0].y);
    
    ctx.fillStyle = '#e6e6e6';
    ctx.fillRect(0, 0, 12000, 12000);

    ctx.strokeStyle = '#d8d8d8';
    ctx.lineWidth = 1;

    for(var x = 0;x < 12000;x += 25) {
        ctx.beginPath();
        ctx.moveTo(x, 0);
        ctx.lineTo(x, 12000);
        ctx.stroke();
    }
    for(var y = 0;y < 12000;y += 25) {
        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(12000, y);
        ctx.stroke();
    }
    
    ctx.fillStyle = '#ff0000';
    ctx.lineWidth = 4;
    ctx.strokeStyle = '#cc0000';
    ctx.beginPath();
    ctx.arc(client.nodes[0].x, client.nodes[0].y, client.nodes[0].w / 2, 0, 2 * Math.PI);
    ctx.fill();
    ctx.stroke();
    
    ctx.translate(-((client.canvas.width / 2.0) - client.nodes[0].x), -((client.canvas.height / 2.0) - client.nodes[0].y));
}

function update() {
    render();
    client.loop = window.requestAnimFrame(update);
}

start();

})();