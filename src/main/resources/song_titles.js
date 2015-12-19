var titles = [];
Array.prototype.slice.call(document.getElementsByTagName('a')).forEach(function (elem) { if (elem.href.match('http://www.uta-net.com/song/\\d+/')) { titles.push(elem.text); } });
titles.join();
