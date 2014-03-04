$(".box-a").click(function() {
	sendReq($(this).attr("href"));
	return false;
});

function sendReq(row) {
	publish("urlSubscriber_contextURL", row);
}

/*
 * Used for publishing via sap-context feature
 */
function publish(key, val) {

	gadgets.sapcontext.publish(key, val);

}