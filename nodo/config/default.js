const port = Number.parseInt(process.argv[2]);

module.exports = {
  mongodb: {
    url: 'mongodb://localhost:27017/auction'
  },
  app: {
    port,
    url: `http://localhost:${port}`
  },
  router: {
    url: 'http://localhost:3000/nodes'
  }
}
