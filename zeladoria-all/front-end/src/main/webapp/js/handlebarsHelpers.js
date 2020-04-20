
			function getUrlParam(key) {
				var result = new RegExp(key + "=([^&]*)", "i")
						.exec(window.location.search);
				return result && unescape(result[1]) || "";
			}
	
			function _render(baseTemplate, selector, data) {
				var template = Handlebars.compile(baseTemplate);
				var html = template(data);
				selector.html(html);
			}

function viewMedia(media) {
	var str = "<div align='center' class='item active'>" + "<img src='"
			+ media.medias[0].img + "' alt='Foto 1'>" + "</div>";
	var i;
	for (i = 1; i < media.medias.length; i++) {
		str += "<div align='center' class='item'>" + "<img src='"
				+ media.medias[i].img + "' alt='Foto " + i + " '>" + "</div>";
	}
	return str;
}

function mediaList(media) {
	var str = "<li data-target='#carousel-example-generic' data-slide-to='0' class='active'></li>";
	for (i = 1; i < media.medias.length; i++) {
		str += "<li data-target='#carousel-example-generic' data-slide-to='"
				+ i + "'></li>";
	}
	return str;
}

function tempViewMedia(photo1, photo2) {
	$("#lista-midia").html(photo2);
	$("#carousel-inner").html(photo1);
}

function getByRel(arr, rel) {
	  var result  = arr.filter(function(o){return o.rel == rel;} );
	  return result? result[0] : null; // or undefined
	}
function checkCondition(v1, operator, v2) {
	switch (operator) {
	case '==':
		return (v1 == v2);
	case '===':
		return (v1 === v2);
	case '!==':
		return (v1 !== v2);
	case '<':
		return (v1 < v2);
	case '<=':
		return (v1 <= v2);
	case '>':
		return (v1 > v2);
	case '>=':
		return (v1 >= v2);
	case '&&':
		return (v1 && v2);
	case '||':
		return (v1 || v2);
	default:
		return false;
	}
};

Handlebars.registerHelper('ifCond', function(v1, operator, v2, options) {
	return checkCondition(v1, operator, v2) ? options.fn(this) : options
			.inverse(this);
});

Handlebars.registerHelper('ifExist', function(v1, options) {
	return (v1 !== null && v1 !== "" && v1 !== undefined) ? options.fn(this) : options
			.inverse(this);
});

Handlebars.registerHelper('ifUndefined', function(v1, options) {
	return (v1 === undefined) ? options.fn(this) : options.inverse(this);
});

Handlebars.registerHelper('ifNull', function(v1, options) {
	return (v1 !== null) ? options.fn(this) : options.inverse(this);
});

Handlebars.registerHelper('ifAuth', function(v1, v2, options) {
	if (typeof (v2) !== 'undefined')
		v2 = JSON.parse(v2);
	var bool = false;
	for (var ii = 0; ii < v2.length; ii++) {
		if (v2[ii].authority === v1) {
			bool = true;
		}
	}
	if (bool) {
		return options.fn(this);
	}
});

Handlebars.registerHelper('substr', function(passedString, startstring,
		endstring) {
	var out = passedString.substring(startstring, endstring);
	if (passedString.length > endstring)
		out += '(...)';
	return new Handlebars.SafeString(out)
});

Handlebars.registerHelper('getLink', function(links, position) {	
	var out = getByRel(links,position).link;
	return new Handlebars.SafeString(out)
});

Handlebars.registerHelper('replaceJson', function(passedString, startstring,
		endstring) {
	passedString = passedString.replace(/"/g, "");
	passedString = passedString.replace(/{/g, "");
	passedString = passedString.replace(/}/g, "");
	passedString = passedString.replace(/\//g, "");
	passedString = passedString.replace(/,/g, ", ");
	passedString = passedString.replace(/:/g, ": ");
	passedString = passedString.replace(/\\/g, "");
	passedString = passedString.replace(/defe12aad396f90e6b179c239de260d4/g,
			"******");

	var out = passedString.substring(startstring, endstring);
	if (passedString.length > endstring)
		out += '(...)';
	return new Handlebars.SafeString(out)
});

Handlebars
		.registerHelper('byteTobase64',
				function(passedString) {
					var base64String;
					if (passedString !== null) {
						var binaryString = '', bytes = new Uint8Array(
								passedString[0]), length = bytes.length;
						for (var i = 0; i < length; i++) {
							binaryString += String.fromCharCode(bytes[i]);
						}
						base64String = 'data:image/png;base64,'
								+ btoa(binaryString);
					} else {

						base64String = "../images/noImage.png";

					}
					return new Handlebars.SafeString(base64String)
				});


Handlebars
.registerHelper('imgShow',
		function(images) {
	var base64String;
	if (images !== null) {
			base64String='data:image/png;base64,'+images[0];
		} else {
			base64String = "../images/noImage.png";
		}
			return new Handlebars.SafeString(base64String)
		});