sap.ui.controller("financial.stock", {

	onInit : function(oEvent) {
		this.model = new sap.ui.model.json.JSONModel();
		this.getView().setModel(this.model);
	},

	getFinancialData : function(symbol, layout) {
		layout.setBusy(true); 
		var encodedParam = 
			encodeURIComponent("select * from yahoo.finance.quotes where symbol in (\"" + symbol + "\")");
		var encodedParamUrl = 
			"/financialdata/v1/public/yql?q="+encodedParam+"&env=store://datatables.org/alltableswithkeys&format=json";
		jQuery.ajax({
			url : encodedParamUrl,
			success : jQuery.proxy(function(result) {
				if (result.query.results.quote.length > 1) {
					this.model.setProperty("/financialData", result.query.results.quote);
					this.model.setProperty("/rowCount", result.query.results.quote.length);
				} else {
					this.model.setProperty("/financialData", new Array(result.query.results.quote));
					this.model.setProperty("/rowCount", 1);
				}
			}, this),
			error : jQuery.proxy(function(jqXHR, textStatus, errorThrown) {
				alert(textStatus + ": " + errorThrown);
			}, this),
			complete : jQuery.proxy(function() {
				layout.setBusy(false);
			}, this)
		});
	}
});