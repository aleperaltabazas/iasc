const ms = require('ms');
const { created, ok, notFound } = require('../utils/statusResponses');
const { auctionCreated, newHighestBid, closedAuction,
  closedAuctionWinner, canceledAuction } = require('../utils/alerts');

const Auction = require('../models/Auction');
const Buyer = require('../models/Buyer');

const postAuction = (req, res, next) => {
  const { body } = req;
  const duration = ms(body.duration);

  const newAuction = new Auction({
    ...body,
    closeDate: new Date(Date.now() + duration)
  });

  newAuction.save()
    .then(() => {
      setTimeout(closeAuction, duration, newAuction._id);
      return Buyer.find({ interestTags: { $in: newAuction.tags }});
    })
    .then(buyers => {
      buyers.forEach(buyer => auctionCreated(buyer));
      console.log(`POST AUCTION: se creo la subasta ${newAuction.id}`);
      created(res, newAuction.id);
    })
    .catch(next);
};

const findAuction = (req, res, next) => {
  const { id } = req.params;

  Auction.findById(id)
    .then(auction => {
      if (!auction) {
        notFound(res, "No exista la subasta");
      } else if (auction.status !== 'open') {
        ok(res, "La subasta ya no esta abierta")
      } else if (auction.closeDate <= new Date()) {
        closeAuction(auction.id);
      } else {
        req.auction = auction;
        next();
      }
    })
    .catch(next);
};

const postBid = (req, res, next) => {
  const { auction, body: { bid, buyer } } = req;
  let currentOffer;

  if (auction.highestBid)
    currentOffer = auction.highestBid.offer;
  else
    currentOffer = auction.basePrice;

  if (bid > currentOffer) {
    auction.updateOne({ $set: { highestBid: { offer: bid, buyer } }, $addToSet: { bidders: buyer } })
      .then(() => {
        console.log(`POST BID: nuevo precio para ${auction.id} de ${bid}`);
        ok(res, "Se acepto su oferta");
        return auction.execPopulate({ path: 'bidders' });
      })
      .then(auction => auction.bidders.forEach(bidder => newHighestBid(bidder, bid)))
      .catch(next);
  } else {
    console.log(`POST BID: no cambio el precio de ${auction.id}`);
    ok(res, "Su oferta no supera a la actual");
  }
};

const closeAuction = (id) => {
  Auction.findByIdAndUpdate({ _id: id }, { status: 'closed' })
    .populate('bidders')
    .then(auction => {
      if (!auction.highestBid) {
        return console.log(`CLOSE AUCTION: Se cerro la suabasta ${auction.id} sin un ganador`)
      }
      const winner = auction.bidders.find(bidder => bidder._id.equals(auction.highestBid.buyer));

      auction.bidders
        .filter(bidder => !bidder._id.equals(winner._id))
        .forEach(bidder => closedAuction(bidder, auction));

        closedAuctionWinner(winner, auction);
    })
    .catch(console.error);
};

const cancelAuction = (req, res, next) => {
  const { auction } = req;

  auction.status = 'cancel';

  auction.save()
    .then(() => {
      ok(res, "Se cancelo la subasta correctamente");
      console.log(`DELETE AUCTION: se cancelo la subasta ${auction.id}`)
      return auction.execPopulate({ path: 'bidders' });
    })
    .then(auction => auction.bidders.forEach(bidder => canceledAuction(bidder, auction)))
    .catch(next);
};

module.exports = { postAuction, findAuction, postBid, cancelAuction };
