var express = require('express');
var app = express();
var sassMiddleware = require('node-sass-middleware');

app.use(sassMiddleware({
  src: __dirname + '/public',
  dest: '/tmp'
}));

// http://expressjs.com/en/starter/static-files.html
app.use(express.static('public'));
app.use(express.static('/tmp'));

// http://expressjs.com/en/starter/basic-routing.html
app.get("/", function (request, response) {
  response.sendFile(__dirname + '/views/index.html');
});

// listen for requests :)
var listener = app.listen(3000, function () {
  console.log('Your app is on http://localhost:' + listener.address().port);
});
