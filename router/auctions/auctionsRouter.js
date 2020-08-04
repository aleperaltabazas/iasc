const auctionsRouter = require('express').Router();

const { selectRandom, redirect, verifyNodes, selectBy } = require('../utils/nodes');

auctionsRouter.use(verifyNodes);

auctionsRouter
  .route('/')
  .post(selectRandom, redirect);

auctionsRouter
  .route('/:id')
  .delete(selectBy('params.id'), redirect);

auctionsRouter
  .route('/:id/bids')
  .post(selectBy('params.id'), redirect);

module.exports = auctionsRouter;
