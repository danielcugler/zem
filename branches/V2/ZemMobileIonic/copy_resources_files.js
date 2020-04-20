#!/usr/bin/env node

// each object in the array consists of a key which refers to the source and
// the value which is the destination.
var filestocopy = [{
    "resources/android/images/logo.png":
    "platforms/android/res/drawable/logo.png"
}];

var fs = require('fs');
var path = require('path');

// no need to configure below
var rootdir = process.argv[2];

// Create res/raw directory so that copy does not fail.
fs.mkdir('platforms/android/res/raw/',0777, function(err) {
  if(err) {
    console.error(err);
  }
  else {
    console.log("the directory create success");
  }
});

filestocopy.forEach(function(obj) {
    Object.keys(obj).forEach(function(key) {
        var val = obj[key];
        var srcfile = path.join(rootdir, key);
        var destfile = path.join(rootdir, val);
        //console.log("copying "+srcfile+" to "+destfile);
        var destdir = path.dirname(destfile);
        if (fs.existsSync(srcfile) && fs.existsSync(destdir)) {
            fs.createReadStream(srcfile).pipe(
               fs.createWriteStream(destfile));
        }
    });
});