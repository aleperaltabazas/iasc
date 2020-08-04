const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Bid = new Schema({
  offer: { type: Number },
  buyer: { type: Schema.Types.ObjectId, ref: 'Buyer' }
});

module.exports = Bid;
