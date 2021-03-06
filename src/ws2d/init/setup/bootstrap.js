(function() {
    window.WebSocket = window.WebSocket || window.MozWebSocket;
    if(!window.WebSocket) {
        document.write("WebSocket not supported.");
        return;
    }
    window.requestAnimFrame = (function() {
        return window.requestAnimationFrame ||
        window.webkitRequestAnimationFrame  ||
        window.mozRequestAnimationFrame     ||
        window.oRequestAnimationFrame       ||
        window.msRequestAnimationFrame      ||
        function(callback) {
            return window.setTimeout(callback, 1000 / 60);
        };
    })();
    window.cancelRequestAnimFrame = (function() {
        return window.cancelAnimationFrame       ||
        window.webkitCancelRequestAnimationFrame ||
        window.mozCancelRequestAnimationFrame    ||
        window.oCancelRequestAnimationFrame      ||
        window.msCancelRequestAnimationFrame     ||
        clearTimeout;
    })();
})();