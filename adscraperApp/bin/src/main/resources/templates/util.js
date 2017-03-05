/**
 *
 */

function getRunningSince(statData) {
	statRows = statData.byDay.rows
	var runningSince = "didn' run yet";
	if (statRows.length > 0)
		runningSince = toDate(statRows[0][0]).toDateString();
	return runningSince;
}

function getAdCount(statData) {
	statRows = statData.byDay.rows
	var sum = 0;

	for (var i = 0; i < statRows.length; i++) {
		r = statRows[i];
		sum += parseInt(r[r.length - 1])
	}

	return sum;
}

function httpGet(theUrl) {
	try {
		var notFound = "404 Not Found";
		var request = new XMLHttpRequest();
		request.open("GET", theUrl, false);
		request.send(null);
		if (request.responseText.indexOf(notFound) >= 0)
			return {
				success : false,
				data : notFound
			};
		return {
			success : true,
			data : request.responseText
		};
	} catch (err) {
		return {
			success : false,
			data : err.message
		}
	}
}

function toDate(utcStrValue) {
	var d = new Date(0);
	// 0 to set the thing to epoch
	fstUTCDate = parseInt(utcStrValue);
	d.setUTCSeconds(fstUTCDate / 1000);
	return d;
}

// from http://dygraphs.com/gallery/#g/highlighted-weekends
function highlightWeekends(canvas, area, g) {

	canvas.fillStyle = "rgba(000, 200, 000, 0.3)";

	function highlight_period(x_start, x_end) {
		var canvas_left_x = g.toDomXCoord(x_start);
		var canvas_right_x = g.toDomXCoord(x_end);
		var canvas_width = canvas_right_x - canvas_left_x;
		canvas.fillRect(canvas_left_x, area.y, canvas_width, area.h);
	}

	var min_data_x = g.getValue(0, 0);
	var max_data_x = g.getValue(g.numRows() - 1, 0);

	// get day of week
	var d = new Date(min_data_x);
	var dow = d.getUTCDay();

	var w = min_data_x;
	// starting on Sunday is a special case
	if (dow === 0) {
		highlight_period(w, w + 12 * 3600 * 1000);
	}
	// find first saturday
	while (dow != 6) {
		w += 24 * 3600 * 1000;
		d = new Date(w);
		dow = d.getUTCDay();
	}
	// shift back 1/2 day to center highlight around the
	// point for the day
	w -= 12 * 3600 * 1000;
	while (w < max_data_x) {
		var start_x_highlight = w;
		var end_x_highlight = w + 2 * 24 * 3600 * 1000;
		// make sure we don't try to plot outside the
		// graph
		if (start_x_highlight < min_data_x) {
			start_x_highlight = min_data_x;
		}
		if (end_x_highlight > max_data_x) {
			end_x_highlight = max_data_x;
		}
		highlight_period(start_x_highlight, end_x_highlight);
		// calculate start of highlight for next
		// Saturday
		w += 7 * 24 * 3600 * 1000;
	}
}

// dataI = JSON
// .parse('{"cols":["Date",
// "willhaben","bazar","jobwohnen"],"rows":[[1432339200000,0,1,0],[1432425600000,0,2,0],[1432512000000,0,0,0],[1432598400000,0,1,2]]}');

// var d = new Date("1959/01/01 13:12:27");

