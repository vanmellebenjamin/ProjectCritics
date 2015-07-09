var fs = require('fs');
var byline = require('byline');
var mongo = require('mongodb').MongoClient;
var assert = require('assert');
var crypto    = require('crypto');

mongo.connect('mongodb://localhost:27017/Critics', function(err, db) {
  assert.equal(null, err);

  db.dropCollection("sessions");
  db.dropCollection("Users");
  console.log("DB truncates");
  db.collection('Users').dropAllIndexes(function() {});

  var semaphore = 2;
  function callback(err) {
      --semaphore;
      if (semaphore !== 0) return;
      db.close();
  }

  var u = byline(fs.createReadStream(__dirname + '/users.json'));

  /* private encryption & validation methods */
  // To insert same password for all the users
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

  u.on('data', function(line) {
    try {
      var obj = JSON.parse(line);
      saltAndHash("azerty", function(pass) {
        /** Add  RANDOM tweet to the user feed */ 
        obj.pass = pass;
        db.collection('Users').insert(obj, {safe: true}, function(e, o) {});
      });
    } catch (err) {
      console.log("Error:", err);
    }
  });
  u.on('end', callback);
  console.log("db test loaded");
});
