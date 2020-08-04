const buyersRouter = require('express').Router();

const { postBuyer } = require('./buyersController');

buyersRouter
  .route('/')
  .post(postBuyer);

module.exports = buyersRouter;
