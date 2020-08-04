const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Buyer = new Schema({
  username: { type: String },
  ip: { type: String },
  interestTags: [{ type: String }]
});

module.exports = mongoose.model('Buyer', Buyer);
