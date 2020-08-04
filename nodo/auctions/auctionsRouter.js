const auctionsRouter = require('express').Router();

const { postAuction, findAuction, postBid, cancelAuction } = require('./auctionsController');

auctionsRouter
  .route('/')
  .post(postAuction);

auctionsRouter
  .route('/:id')
  .delete(findAuction, cancelAuction);

auctionsRouter
  .route('/:id/bids')
  .post(findAuction, postBid);

module.exports = auctionsRouter;
