var express = require('express');
var app = module.exports = express();
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var session = require('express-session');
var assert = require('assert');
var mongo = require('mongodb').MongoClient;
var AM = require('./account-manager.js');
var MongoStore = require('connect-mongo')(session);

////////////////////////////////////////////////////////////////////////////////

app.set('env', 'development');

////////////////////////////////////////////////////////////////////////////////
// MIDDLEWARE

app.use(cookieParser());
// Http only false to allow connection in ajax
// XSS leak but not the time to implement
// Nice session mechanism
// Store the session data on mongo
// to make the sessions avalaible
// on all the nodeJs nodes.
app.use(session({ secret: 'super-duper-secret-secret',
                  store: new MongoStore({
                    db: 'twitter',
                    host: 'localhost',
                    port: 27017
                  }),
                  saveUninitialized: true,
                  cookie: { httpOnly: false },
                  resave: true}));

// serve static content from the public directory
app.use(express.static(path.join(__dirname, 'public')));

// parse the parameters of POST requests (available through `req.body`)
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

// routes
app.use('/', require('./routes.js'));

// catch 404 and forward to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

////////////////////////////////////////////////////////////////////////////////
// ERROR HANDLERS

// development error handler, will print stacktrace
if (app.get('env') === 'development') {
    app.use(function(err, req, res, next) {
        res.status(err.status || 500);
        console.log('[' + err.status + '] ' + err.message);
    });
}

// production error handler, no stacktraces leaked to user
app.use(function(err, req, res, next) {
    res.status(err.status || 500);
});

////////////////////////////////////////////////////////////////////////////////
// START APP

// 1) Connect to MongoDB
// 2) Connect to Kafka
// 3) Start the HTTP server
mongo.connect('mongodb://localhost:27017/twitter', function(err, db) {
    
    // TODO: error handling
    if (err) {
        console.log("Cannot connect to MongoDB: " + err);
    } else {
        console.log("connected to MongoDB");
        app.db = db;

        var server = app.listen(3002, function () {
            var host = "192.168.1.15";
            var port = server.address().port;
            console.log('listening at http://%s:%s', host, port);
        });
    }
    
});

////////////////////////////////////////////////////////////////////////////////
