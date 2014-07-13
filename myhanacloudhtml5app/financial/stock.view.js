sap.ui.jsview("financial.stock", {
	getControllerName : function() {
		return "financial.stock";
	},

	createContent : function(oController) {

		// Shell
		var oShell = new sap.ui.ux3.Shell("shell", {
			appTitle : "Stock values",
			showPane : false,
			showSearchTool : false,
			showFeederTool : false,
			showLogoutButton : false,
			designType : sap.ui.ux3.ShellDesignType.Crystal
		});

		// Layout
		var oLayout = new sap.ui.commons.layout.MatrixLayout("layout", {
			width : "100%",
			widths : [ "100%" ]
		});

		// Toolbar Controls
		var oSymbolsLabel = new sap.ui.commons.Label({
			text : "Enter comma separated list of stock symbols (e.g. SAP,MSFT)"
		});

		var oSymbolsValue = new sap.ui.commons.TextField("oSymbolsValue", {
			value : '',
			width : '20em',
			press1 : [ oController.onLiveChange, oController ],
			liveChange : function(oEvent) {
				var searchBtn = sap.ui.getCore().getControl("oSearchButton");
				if (this.getLiveValue() === "") {
					searchBtn.setEnabled(false);
				} else {
					searchBtn.setEnabled(true);
				}
			}
		});

		var oSearchButton = new sap.ui.commons.Button({
			id : "oSearchButton",
			text : "Search",
			press1 : [ oController.onSearch, oController ],
			press : function(event) {
				var symbols = sap.ui.getCore().getControl("oSymbolsValue");
				var layout = sap.ui.getCore().getControl("layout");
				oController.getFinancialData(symbols.getDomRef().value, layout);
			}
		});

		// Stock Values Table
		var oTable = new sap.ui.table.Table("table", {
			width : "100%",
			visibleRowCount : 16,
			visibleRowCount : 10,
			visibleRowCountMode : sap.ui.table.VisibleRowCountMode.Auto,
			toolbar : new sap.ui.commons.Toolbar({
				items : [ oSymbolsLabel, oSymbolsValue, oSearchButton ]
			}),
			noData : new sap.ui.commons.TextView({
				text : "Enter comma separated list of stock symbols (e.g. SAP,MSFT) and press the Search button."
			}),
			columns : [ {
				label : "Name",
				template : "Name",
				sortProperty : "Name"
			}, {
				label : "Symbol",
				template : "symbol",
				sortProperty : "symbol"
			}, {
				label : "Ask Realtime",
				template : "AskRealtime",
				sortProperty : "AskRealtime"
			}, {
				label : "Change",
				template : "Change_PercentChange",
				sortProperty : "Change_PercentChange"
			}, {
				label : "Volume",
				template : "Volume",
				sortProperty : "Volume"
			}, {
				label : "Stock Exchange",
				template : "StockExchange",
				sortProperty : "StockExchange"
			} ],
			rows : {
				path : "/financialData",
				sorter : new sap.ui.model.Sorter("symbol")
			}

		}).bindProperty('visibleRowCount', '/rowCount', function(iRowCount) {
			// controller?
			iRowCount = iRowCount || 2;
			return Math.min(iRowCount, 8);
		});

		oLayout.createRow(new sap.ui.commons.layout.MatrixLayoutCell().addContent(oTable));

		// Chart
		var oChartData = new sap.viz.ui5.data.FlattenedDataset({
			dimensions : [ {
				axis : 1,
				name : 'Name',
				value : "{Name}"
			} ],
			measures : [ {
				group : 1,
				name : 'Days High',
				value : '{DaysHigh}'
			}, {
				group : 1,
				name : 'Days Low',
				value : '{DaysLow}'
			}, {
				group : 1,
				name : 'Years high',
				value : '{YearHigh}'
			}, {
				group : 1,
				name : 'Years low',
				value : '{YearLow}'
			} ],
			data : {
				path : "/financialData"
			}
		});

		var oChart = new sap.viz.ui5.Column("chart", {
			width : "100%",
			dataset : oChartData
		});
		oLayout.createRow(new sap.ui.commons.layout.MatrixLayoutCell().addContent(oChart));

		oShell.addContent(oLayout);

		return oShell;
	}

});