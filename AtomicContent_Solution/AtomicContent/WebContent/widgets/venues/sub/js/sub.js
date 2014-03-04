var urlSubscriber = {};

urlSubscriber.prefix = "urlSubscriber_"; ///this is a reserved prefix for all keys, to avoid conflicts
urlSubscriber.pubsubKey = "contextURL";

/*
* This will be the callback function of a publish event.
*/
urlSubscriber.getURL = function getURL(topic, context) {
  urlSubscriber.changeURL(context.getPropertyByKey(urlSubscriber.prefix + urlSubscriber.pubsubKey)[0] + "");
};
/*
* Holds the logic for getting the right Google map according to a given URL.
*/
urlSubscriber.changeURL = function changeURL(link) {
  if((link !== "") && (typeof link != "undefined") && (link!="undefined") && (link != undefined)) {
    $("iframe").remove();
    $("body").prepend('<iframe src="'+link+'"></iframe>');
  }
};


/*
 * Subscribe to site context with predefined callback.
 * Keeps the subscription ID in urlSubscriber.subId variable.
 */
function subscribe() {
  urlSubscriber.subId = gadgets.sapcontext.subscribe(urlSubscriber.getURL);
}

// Subscribe to site context on widget load
gadgets.sapcontext.registerOnContextLoad(subscribe);

