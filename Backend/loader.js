var fs = require('fs');
var byline = require('byline');
var mongo = require('mongodb').MongoClient;
var assert = require('assert');
var crypto    = require('crypto');

mongo.connect('mongodb://localhost:27017/twitter', function(err, db) {
  assert.equal(null, err);

  db.dropCollection("sessions");
  db.dropCollection("Users");
  db.dropCollection("Tweets");
  db.dropCollection("counters");
  db.dropCollection("Images");
  console.log("DB truncates");
  db.collection('Users').dropAllIndexes(function() {})
  
  var semaphore = 2;
  function callback(err) {
      --semaphore;
      if (semaphore !== 0) return;
      db.close();
  }


  function getNextSequence(name, callback) {
     var ret = db.collection('counters').findAndModify(
         { "_id": name },
         [['_id','asc']],
         { $inc: { "seq": 1 } },
         {new: true, upsert: true},
         function (e, o) {
            callback(o.seq);
         }
      );
  }

  var u = byline(fs.createReadStream(__dirname + '/users.json'));

  db.collection('counters').insert(
  {
    _id: "tweetid",
    seq: 1
  }, function(){});

  db.collection('counters').insert(
  {
    _id: "imgid",
    seq: 1
  }, function(){});

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
      saltAndHash("test", function(pass) {
        /** Add  RANDOM tweet to the user feed */ 
        obj.tweets = [];
        obj.pass = pass;
        db.collection('Users').insert(obj, {safe: true}, function(e, o) {});
      });
      // NOTE: obj represents a user and contains three fields:
      //obj.username: //the username
      //obj.name: //the full name
      //obj.followers: //array of users following this user
      //obj.following: //array of followed users
      //obj.newsFeed: //array of message destined to user
      // (including the information of which users this user is following)
    } catch (err) {
      console.log("Error:", err);
    }
  });
  u.on('end', callback);


  var t = byline(fs.createReadStream(__dirname + '/sample.json'));

  t.on('data', function(line) {
    try {
      var obj = JSON.parse(line);
      getNextSequence('tweetid', function(seq) {
        obj._id = seq;
        db.collection('Tweets').insert(obj, {safe: true}, function(e, o) {
            db.collection('Users').update({username: obj.username},
                {$addToSet: {tweets: seq}}, {upsert: true}, function(e, o){});
            });
        });

      // NOTE: obj represents a tweet and contains three fields:
      //obj.created_at: UTC time when this tweet was created
      //obj.text: The actual UTF-8 text of the tweet
      //obj.username: The user who posted this tweet
    } catch (err) {
      console.log("Error:", err);
    }
  });
  t.on('end', callback);

  // Build indexes
  db.ensureIndex("Users", { username:1}, { unique: true }, function(err, indexname) {
    assert.equal(null, err);
  });

});
