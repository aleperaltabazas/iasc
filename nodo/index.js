const app = require('./app');
const config = require('config');
const mongoose = require('mongoose');

const { configWebSocket } = require('./utils/websockets');

configWebSocket();

const mongoURL = config.get('mongodb.url');

mongoose.connect(mongoURL,
  {
    useNewUrlParser: true,
    useCreateIndex: true,
    useUnifiedTopology: true,
    useFindAndModify: false
  })
  .catch(console.log);

const port = config.get('app.port');

app.listen(port, () => { console.log(`Corriendo en puerto ${port}`) });
