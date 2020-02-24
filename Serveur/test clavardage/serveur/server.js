var express = require('express');
var app = express();
var http = require('http').createServer(app);
var io = require('socket.io')(http);
const path = require('path');
const PORT = process.env.PORT || 5000;
require('./db/db')

var usernamesMap = new Map();
var usernames = new Set();

app.use(express.json())

app.get('/', function(req, res){
  // console.log(path.resolve(__dirname + '/../client/index.html'))
  // res.sendFile(path.resolve(__dirname + '/index.html'));
});

var usersRouter = require('./player/player.route.js');
app.use(usersRouter);
io.attach(http);
io.on('connection', function(socket){
  usernamesMap.set(socket.id, "Anonymous");
  console.log("User connected");

  socket.on('chat message', (username, message) => {

      var currentDate = new Date();
      // var date = currentDate.getDate();
      // var month = currentDate.getMonth();
      // var year = currentDate.getFullYear();
      var hours = currentDate.getHours();
      var minutes = currentDate.getMinutes();
      var seconds = currentDate.getSeconds();

      var dateString = " à " + hours + ":" + minutes + ":" + seconds;

      let  msg = {"message": message, "username": username, "timestamp": dateString}
      console.log(username + ":" + message)
      io.emit('chat message', msg);
  });
  
  socket.on("changeUsername", function(username){
    var nameUsed = usernames.has(username);  
    if (nameUsed) {
        socket.emit("changeUsername", false);
    }
    else {
        usernamesMap.set(socket.id, username);
        usernames.add(username);
        socket.emit("changeUsername", true);
        console.log(username + "has joined the chat")
    }
  });

  socket.on('room connect', (username, room) => {
    socket.join(room)
    socket.to(room).broadcast.emit('room connect', username)
  });


  socket.on('room chat', (room, message) => {

    var currentDate = new Date();
    // var date = currentDate.getDate();
    // var month = currentDate.getMonth();
    // var year = currentDate.getFullYear();
    var hours = currentDate.getHours();
    var minutes = currentDate.getMinutes();
    var seconds = currentDate.getSeconds();
  
    var dateString = " à " + hours + ":" + minutes + ":" + seconds;
    var currentUsername = usernamesMap.get(socket.id);
    let  msg = {"message": message, "username": currentUsername, "timestamp": dateString}
    console.log(currentUsername + ":" + message)
    socket.to(room).broadcast.emit('room chat', msg);
  });

  socket.on("disconnection", function() {
      usernames.delete(usernamesMap.get(socket.id));
      usernamesMap.delete(socket.id);
      socket.disconnect();
      console.log("User disconnected.");
  });

});

http.listen(PORT, () => {
  console.log('listening on *:', PORT);
});