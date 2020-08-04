const express = require('express');
const config = require('config');
const bodyParser = require('body-parser');
const http = require('http');
const { configWebSocket } = require('./utils/webSockets');

const buyersRouter = require('./buyers/buyersRouter');
const auctionsRouter = require('./auctions/auctionsRouter');

const app = express();
const port = config.get('app.port');

const server = http.createServer(app);

server.listen(port);
server.on('listening', () => console.log(`Escuchando en puerto ${port}`));

configWebSocket(server);


app.use(bodyParser.json());

app.use('/buyers', buyersRouter);
app.use('/auctions', auctionsRouter);

app.use((err, req, res, next) => {
  console.error(err);

  res.status(500).json({
    message: "Se rompio todo"
  });
})

app.set('port', port);
