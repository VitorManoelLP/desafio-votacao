const target = process.env.API_URL || 'http://localhost:3000';
console.log(process.env.API_URL);
const proxyConfig = {
  "/api/**": {
    "target": target,
    "secure": false,
    "ws": true,
    "headers": {
      "Connection": "Keep-Alive"
    }
  }
};

module.exports = proxyConfig;
