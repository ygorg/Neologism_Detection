var htmlToText = require('html-to-text');
var path = require('path');
var fs = require('fs');
var cheerio = require('cheerio');
var $ = cheerio.load(
			fs.readFileSync('src.txt', {encoding: 'utf-8'})
		);


var src = $('.article-content.rich-text').html();

console.log(htmlToText.fromString(src, {
	ignoreHref: true,
	ignoreImage: true
}));
