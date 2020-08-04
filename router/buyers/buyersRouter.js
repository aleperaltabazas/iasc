const buyersRouter = require('express').Router();

const { selectRandom, redirect, verifyNodes } = require('../utils/nodes');

buyersRouter.use(verifyNodes);

buyersRouter
  .route('/')
  .post(selectRandom, redirect);

module.exports = buyersRouter;
