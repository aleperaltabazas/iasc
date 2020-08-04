const io = require('socket.io-client');
const config = require('config');

const configWebSocket = () => {
  socket = io(config.get('router.url'), {
    query: { node: config.get('app.url') }
  });

  socket.on('connect', () => {
    console.info('ROUTER: conectado');
  });

  socket.on('disconnect', () => {
    console.log('ROUTER: desconectado');
  });
};

module.exports = { configWebSocket };
