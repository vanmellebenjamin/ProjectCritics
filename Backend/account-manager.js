var crypto 		= require('crypto');
var moment 		= require('moment');
var app = require('./app');

var collectionName = "Users";


var openCollection = function(callback) {
	app.db.collection(collectionName, callback);
}

/* login validation methods */

exports.autoLogin = function(user, pass, callback)
{
	openCollection(function(err, collection) {
		collection.findOne({email:user}, function(e, o) {
			if (o){
				o.pass == pass ? callback(o) : callback(null);
			}	else{
				callback(null);
			}
		});
	});
}

exports.manualLogin = function(email, pass, callback)
{
	openCollection(function(err, collection) {
		collection.findOne({email:email}, ['email', 'pass'], function(e, o) {
			if (o == null){
				callback('user-not-found');
			}	else{
				validatePassword(pass, o.pass, function(err, res) {
					if (res){
						callback(null, o);
					}	else {
						callback('invalid-password');
					}
				});
			}
		});
	});
}

/* record insertion, update & deletion methods */

exports.addNewAccount = function(newData, callback)
{
	openCollection(function(err, collection) {
		collection.findOne({email:newData.email}, function(e, o) {
			if (o){
				callback('username-taken');
			}
			else{
				saltAndHash(newData.pass, function(hash){
					newData.pass = hash;
					newData.followers = [];
					newData.following = [];
					newData.tweets = [];
				    // append date stamp when record was created //
					newData.date = moment().format('MMMM Do YYYY, h:mm:ss a');
					collection.insert(newData, {safe: true}, callback);
				});
			}
		});
	});
}


/* get User tweets */

exports.getFullName = function(username, callback) {
	openCollection(function(err, collection) {
		collection.findOne({email: email}, ['fullname'], callback);
	});
}

/* Get user info */

exports.getUserInfos = function (email, callback) {
	openCollection(function(err, collection) {
		collection.findOne({email:email}, ['email'], function(e, o) {
			if (o == null){
				callback('user-not-found');
			} else {
				callback(null, o);
			}
		});
	});
}

/* private encryption & validation methods */

var generateSalt = function()
{
	var set = '0123456789abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ';
	var salt = '';
	for (var i = 0; i < 10; i++) {
		var p = Math.floor(Math.random() * set.length);
		salt += set[p];
	}
	return salt;
}

var md5 = function(str) {
	return crypto.createHash('md5').update(str).digest('hex');
}

var saltAndHash = function(pass, callback)
{
	var salt = generateSalt();
	callback(salt + md5(pass + salt));
}

var validatePassword = function(plainPass, hashedPass, callback)
{
	var salt = hashedPass.substr(0, 10);
	var validHash = salt + md5(plainPass + salt);
	callback(null, hashedPass === validHash);
}
