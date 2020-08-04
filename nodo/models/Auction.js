const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Bid = require('./Bid');

const STATUS = ['open', 'closed', 'cancel'];

const Auction = new Schema({
  // TODO deberia ser un json
  article: { type: String },
  status: { type: String, enum: STATUS, default: 'open' },
  basePrice: { type: Number },
  highestBid: { type: Bid },
  bids: [{ type: Bid, default: [] }],
  bidders: [{ type: Schema.Types.ObjectId, ref: 'Buyer' }],
  seller: { type: String },
  closeDate: { type: Date },
  tags: [{ type: String }]
});

module.exports = mongoose.model('Auction', Auction);
