var express = require('express');
var assert = require('assert');
var app = require('./app.js');
var ejs = require('ejs');
var fs = require('fs');
var AM = require('./account-manager.js');
var fs = require('fs');
var router = express.Router();

////////////////////////////////////////////////////////////////////////////////
// Login / Logout

router.post('/login', function(req, res) {
    console.log(req.session.user);
    if (    req.param('email').length > 90 || req.param('email').length < 4  
        ||  req.param('password').length > 20 || req.param('password').length < 4 ) {
            res.status(403).send("Informations entered are incompletes !").end();
    }
    else {
        // attempt manual login & open collection Users
        AM.manualLogin(req.param('email'), req.param('password'), function(e, o) {
            if (!o) {
                if (e == 'user-not-found')
                    res.status(403).send( "User not found").end();
                else if (e == 'invalid-password')
                    res.status(403).send( "Password not match").end();
                else
                    res.status(403).send( "Unexpected Error").end();
            } else {
                console.log(o);
                req.session.user =  o;
                res.status(200).send().end();
            }
        });
    }
});

router.get('/logout', function(req, res) {
    req.session.destroy(function() {
        res.status(200).send( "disconnected").end();
    });
});

router.get('/isConnected', function(req, res) {
    if (req.session.user != null) {
        res.status(200).send( "true").end();
    } else {
        res.status(200).send( "false").end();
    }
});

////////////////////////////////////////////////////////////////////////////////
// Subscription

router.post('/validateSubscription', function(req, res) {
    //Verify informations
    var username = req.param('username');
    var fullname = req.param('fullname');
    var pass = req.param('pass');
    if (    username.length > 20 || username.length < 4  || !username.match("^([-_A-z0-9]){3,}$")
        ||  fullname.length > 20 || fullname.length < 4 || !fullname.match("^([- _A-z0-9]){3,}$")
        ||  pass.length > 20 || pass.length < 4 ) {
            res.status(403).send("Informations entered are incorrects (well try) !").end();
    }
    // attempt registe & open collection Users
    AM.addNewAccount({'username': username, 'pass': pass, 
        'fullname': fullname}, function(e, o) {
            if(e == 'username-taken') {
                res.status(403).send( "Username already taken").end();
            } else {
                res.status(200).send( "Success").end();
            }
    });
});



/** Return informations about user **/

router.get('/usr/:username', function(req, res) {
    if (req.session.user === null) {
        res.status(403).send("not authentificated").end();
    }
    var username= req.session.user.username;
    var result = {isFollowing: false, fullname: ""};

    AM.isFollowing(req.session.user.username, req.param('username'), function(e, o) {
        if (e != null)
            return console.log(e);
        result.isFollowing = o;
        AM.getFullName(req.param('username'), function(e, o) {
            if (e != null)
                return console.log(e);
            result.fullname = o.fullname;
            res.status(200).send(result).end();
        });
    });
});


////////////////////////////////////////////////////////////////////////////////

module.exports = router;