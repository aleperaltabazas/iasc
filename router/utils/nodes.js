const { sample, isEmpty, get, pull } = require('lodash');
const { serviceUnavailable } = require('./statusResponses');

const nodes = [];

const verifyNodes = (req, res, next) => {
  if (isEmpty(nodes))
    serviceUnavailable(res);
  else
    next();
};

const selectRandom = (req, res, next) => {
  req.node = sample(nodes);
  next();
};

const redirect = (req, res, next) => {
  console.log(`REDIRECT: ${req.method} ${req.originalUrl} a ${req.node}`);
  res.redirect(307, req.node + req.originalUrl);
};

const addNode = node => {
  nodes.push(node);
  console.log(`NODES: agregado el nodo ${node}`);
};

const removeNode = (node, reason) => {
  pull(nodes, node);
  console.log(`NODES: se desconecto ${node} por ${reason}`);
};

const selectBy = path => (req, res, next) => {
  const id = get(req, path);
  const lastCharValue = id.slice(-1).charCodeAt(0);
  const nodeIndex = lastCharValue % nodes.length;

  req.node = nodes[nodeIndex];
  next();
};

module.exports = { verifyNodes, selectRandom, redirect, addNode, removeNode, selectBy };
