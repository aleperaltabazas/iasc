// require('dotenv').config();


const express = require('express');
const config = require('config');
const bodyParser = require('body-parser');
// const cors = require('cors');

// const {logError, moduleError, resourceNotFound} = require('./utils/errorsHandler');


const auctionsRouter = require('./auctions/auctionsRouter');
const buyersRouter = require('./buyers/buyersRouter');

const app = express();

// const port = process.env.PORT || 3000;
// const localhostURL = 'http://localhost:' + port;


// const productionURL = config.get('app.url') || 'http://dev.template.dblandit.com';

/**
 * Begin CORS configuration.
 * CORS config must be the first to be used by app (app.use())
 */

/**
 * This Array must contain the production and development URLs.
 * In a production environment, extract the production URL to a config file.
 * @type {[string,string]}
 */
// const allowedOrigins = [productionURL, localhostURL];

// let allowedOriginsCors = function (origin, callback) {
//     if (!origin) return callback(null, true);

//     if (allowedOrigins.indexOf(origin) === -1)
//         return callback(new Error("CORS origin not allowed this API."), false);

//     return callback(null, true);
// };

// app.options('*', cors({origin: allowedOriginsCors, credentials: true}));

// app.use(
//     cors({origin: allowedOriginsCors, credentials: true})
// );


/**
 * CORS End
 */


app.use(bodyParser.json());

app.use('/auctions', auctionsRouter);
app.use('/buyers', buyersRouter);

// app.use(resourceNotFound);
// app.use(logError);
// app.use(moduleError);

app.use((err, req, res, next) => {
  console.error(err);

  res.status(500).json({
      message: "Se rompio todo"
  });
})

module.exports = app;
