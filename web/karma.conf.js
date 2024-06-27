module.exports = function(config) {
  config.set({
    browsers: ['ChromeHeadless'],
    customLaunchers: {
      ChromeHeadless: {
        base: 'Chrome',
        flags: ['--no-sandbox', '--headless', '--disable-gpu', '--remote-debugging-port=9222']
      }
    }
  });
};
