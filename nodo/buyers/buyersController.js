const { created } = require('../utils/statusResponses');

const Buyer = require('../models/Buyer');

const postBuyer = (req, res, next) => {
  const { body } = req;

  const newBuyer = new Buyer(body);

  newBuyer.save()
    .then(() => {
      console.log(`POST BUYER: se creo el usuario ${newBuyer.username}`);
      created(res, { _id: newBuyer.id })
    })
    .catch(next);
};

module.exports = { postBuyer };
