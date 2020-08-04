const auctionCreated = buyer => {
  console.log(`Se le avisa a ${buyer.username} que se creo una subasta`);
};

const newHighestBid = (buyer, bid) => {
  console.log(`Se le avisa a ${buyer.username} que el nuevo precio es ${bid}`);
};

const closedAuction = (buyer, auction) => {
  console.log(`Se le avisa a ${buyer.username} que la subasta ${auction._id} se cerro y no gano`)
};

const closedAuctionWinner = (buyer, auction) => {
  console.log(`Se le avisa a ${buyer.username} que la subasta ${auction._id} se cerro y es el ganador`)
};

const canceledAuction = (buyer, auction) => {
  console.log(`Se le avisa a ${buyer.username} que la subasta ${auction._id} se cancelo`)
};

module.exports = { auctionCreated, newHighestBid, closedAuction, closedAuctionWinner, canceledAuction };
