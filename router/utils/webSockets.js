const Server = require('socket.io');
const { addNode, removeNode } = require('./nodes');

let io;

const configWebSocket = server => {
  io = new Server(server);

  io.on('connection', function(socket) {
    const node = socket.handshake.query.node;
    if (node === undefined) {
      return socket.disconnect();
    }

    // socket.join('nodes');
    addNode(node);

    socket.on('error', function(err){
      console.error(err);
    });

    socket.on('disconnect', function(reason) {
    if (reason === 'io server disconnect') {
      // the disconnection was initiated by the server, you need to reconnect manually
      socket.connect();
    }
    // else the socket will automatically try to reconnect
    removeNode(node, reason);
    });
  });
};

module.exports = { configWebSocket };
