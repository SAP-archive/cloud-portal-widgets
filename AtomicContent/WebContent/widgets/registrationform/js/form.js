

			
			var SYSTEM_TYPE_SELECT  = "select";
			var SYSTEM_TYPE_C4C   = "c4c";
			var SYSTEM_TYPE_CRM = "crm";
			var targetSystem;
			
			var prefs = new gadgets.GadgetPrefs();
			var widgetId = prefs.getWidgetId();
			
			var oFeedTypeDropDownBox;
			var oSaveButton;
			var oCancelButton;
			
			var listItems;
			var selectItem;
			
			var mandatoryControlArray = new Array();
				
			function init(){
			
				loadPrefs();
				createSettingsForm();
				initRuntime();					
			}
			

			function getDropdownListItems() {
					if (typeof(listItems)==="undefined") {
						selectItem = new sap.ui.core.ListItem(SYSTEM_TYPE_SELECT,
											{	text: "Select a backend target ", 
												key: SYSTEM_TYPE_SELECT,												
												enabled: false});
						listItems = [
								selectItem,
								new sap.ui.core.ListItem(SYSTEM_TYPE_C4C,
									{text: "C4C", selected: true, key: SYSTEM_TYPE_C4C}),
								new sap.ui.core.ListItem(SYSTEM_TYPE_CRM,
									{text: "On-Premise CRM", key: SYSTEM_TYPE_CRM})
						];
					}
					return listItems;
			}
			
			function loadPrefs() {
				targetSystem = prefs.getPreference("targetSystem");
				if (typeof(targetSystem)==='undefined') {
					targetSystem = SYSTEM_TYPE_SELECT;
				}
			}
									
			function createSettingsForm(){
			
				// create the dropdown selector
				oFeedTypeDropDownBox = new sap.ui.commons.DropdownBox("FeedTypeDropDownBox",{
					tooltip: "Select feed type",
					items: getDropdownListItems(),
					change: function(oEvent){	
						updateButtonsState();
					},
					design: sap.ui.core.Design.Standard,
					width: "100%"
				});				
				
				var oFeedTypeLabel = new sap.ui.commons.Label("FeedTypeLabel",
														{ text: "Select Traget System ", 
														  labelFor: oFeedTypeDropDownBox});		

						
				//create the confirmation and cancel buttons button
				oSaveButton = new sap.ui.commons.Button({
									text : "Save",
									tooltip : "Save",
									height: '30px',
									press : saveBtnCallback
								});

				oCancelButton = new sap.ui.commons.Button({
									text : "Cancel",
									tooltip : "Cancel",
									height: '30px',
									press : cancelBtnCallback
								});
								
				updateButtonsState();	
				
				var oLayout = new sap.ui.commons.layout.MatrixLayout("Matrix1",{
							layoutFixed: true,
							widths: ['10px', '40px', '40px', '60px', '100px'],
							columns: 5,
							rows: [
							   new sap.ui.commons.layout.MatrixLayoutRow("Row0", { height:'60px' ,
									cells:[
										new sap.ui.commons.layout.MatrixLayoutCell(),
										new sap.ui.commons.layout.MatrixLayoutCell(),
										new sap.ui.commons.layout.MatrixLayoutCell(),
										new sap.ui.commons.layout.MatrixLayoutCell(),
										new sap.ui.commons.layout.MatrixLayoutCell()
									]
									}),
							   new sap.ui.commons.layout.MatrixLayoutRow("Row1", {cells:[
										new sap.ui.commons.layout.MatrixLayoutCell(),
										new sap.ui.commons.layout.MatrixLayoutCell("Cell1", {colSpan:1, content:oFeedTypeLabel}),
										new sap.ui.commons.layout.MatrixLayoutCell(),
										new sap.ui.commons.layout.MatrixLayoutCell(),
										new sap.ui.commons.layout.MatrixLayoutCell()
									]}),
								new sap.ui.commons.layout.MatrixLayoutRow("Row2", {cells:[
										new sap.ui.commons.layout.MatrixLayoutCell(),										
										new sap.ui.commons.layout.MatrixLayoutCell("Cell2", {colSpan:3, content:oFeedTypeDropDownBox}),
										new sap.ui.commons.layout.MatrixLayoutCell(),
										new sap.ui.commons.layout.MatrixLayoutCell(),
										new sap.ui.commons.layout.MatrixLayoutCell()
									]}),
							   new sap.ui.commons.layout.MatrixLayoutRow("Row3", {height:'40px' ,cells:[
										new sap.ui.commons.layout.MatrixLayoutCell(),
										new sap.ui.commons.layout.MatrixLayoutCell("Cell6", {colSpan:1, content: [ oSaveButton] }),
										new sap.ui.commons.layout.MatrixLayoutCell("Cell7", {colSpan:1, content: oCancelButton}),
										new sap.ui.commons.layout.MatrixLayoutCell(),
										new sap.ui.commons.layout.MatrixLayoutCell()
									]})
								]
						}).placeAt("formSettingsDiv");			
			}
			
			
			function updateButtonsState() {
				if (targetSystem === SYSTEM_TYPE_SELECT) {
					oSaveButton.setEnabled(true);
					oCancelButton.setEnabled(false);
				} else {
					oSaveButton.setEnabled(true);
					oCancelButton.setEnabled(true);			
				}
			}
			
			
			function saveBtnCallback() {
				var selectedTargetSystem = oFeedTypeDropDownBox.getSelectedKey();
				prefs.setPreference("targetSystem",selectedTargetSystem);
				targetSystem = selectedTargetSystem;								
				displayFormRuntime();						
			}
		
			function cancelBtnCallback() {
				resetToPersistedParams();
				displayFormRuntime();
				restore();
			}
			
			function resetToPersistedParams() {
				loadPrefs();
				if (targetSystem===SYSTEM_TYPE_SELECT) {
					selectItem.setEnabled(true);
					oFeedTypeDropDownBox.setSelectedKey(targetSystem);
					selectItem.setEnabled(false);
				} else {
					oFeedTypeDropDownBox.setSelectedKey(targetSystem);
				}

				updateButtonsState();
			}
			
			function restore() {
				gadgets.rpc.call(
					undefined,
					"widget.minimize.force",
					undefined, {
						moduleId: widgetId,
						timeout: 1000
					}
				);
			}
			
			
			// RUNTIME		
			function initRuntime(){
							
				//Name
				var nameL = new sap.ui.commons.Label({
					id : 'L-F-Name',
					required: true,
					text : 'Name' });
				
				var nameTF = new sap.ui.commons.TextField({
					id : 'TF-F-Name',
					tooltip : 'Name',
					placeholder: "First and last name",
					change: nameChangeEvent,
					width : '100%' });
					
				mandatoryControlArray[0] = nameTF;
				nameL.setLabelFor(nameTF);
				
				//Company
				var companyL = new sap.ui.commons.Label({
					id : 'L-Company',
					text : 'Company' });
				
				var companyTF = new sap.ui.commons.TextField({
					id : 'TF-Company',
					tooltip : 'Company',					
					width : '100%' });
					
				companyL.setLabelFor(companyTF);								
				
				//Role
				var roleL = new sap.ui.commons.Label({
					id : 'L-Role',
					text : 'Title' });
				
				var roleTF = new sap.ui.commons.TextField({
					id : 'TF-Role',
					tooltip : 'Title',
					placeholder: "Description of your role in your company",
					width : '100%' });
				
				roleL.setLabelFor(roleTF);
				
				//Email
				var emailL = new sap.ui.commons.Label({
					id : 'L-Email',
					required: true,
					text : 'Email address' });
				
				var emailTF = new sap.ui.commons.TextField({
					id : 'TF-Email',
					tooltip : 'Email address',
					change: emailChangeEvent,
					width : '100%' });
					
				mandatoryControlArray[1] = emailTF;				
				emailL.setLabelFor(emailTF);
				
				//Phone
				phoneL = new sap.ui.commons.Label({
					id : 'L-Phone',
					required: true,
					text : 'Phone number' });
				
				phoneTF = new sap.ui.commons.TextField({
					id : 'TF-Phone',
					tooltip : 'Phone number',
					placeholder: "Area code and phone number",
					change: phoneChangeEvent,
					width : '100%' });
				
				mandatoryControlArray[2] = phoneTF;
				phoneL.setLabelFor(phoneTF);				
				
				//Comments
				var commentsL = new sap.ui.commons.Label({
					id : 'L-Comments',
					text : 'Comments' });
				
				var commentsTA = new sap.ui.commons.TextArea({
					id : 'TA-Comments',
					tooltip : 'Comments',
					width : '100%',
					height: '120px'	});
				
				commentsL.setLabelFor(commentsTA);	

				var registerLargeImg = new sap.ui.commons.Image("i1");
				//registerLargeImg.setSrc("https://dl.dropboxusercontent.com/u/73925382/opensocial/form/big_register.png");				
				//registerLargeImg.setSrc("/atomiccontent/widgets/registrationform/images/big_register.png");
				
				var applicationURL = gadgets.util.getUrlParameters().url;
				var relativePath = applicationURL.substring(0, applicationURL.indexOf("form.spec.xml"));
				var imagePath = relativePath + "images/big_register.png";
				registerLargeImg.setSrc(imagePath);
				
				//Register
				var registerButton = new sap.ui.commons.Button({
					id : 'B-Register',
					tooltip : "Press to register",
					text : 'Register',
					height: '35px',
					press : function(oEvent){			
								var validData = validateInput();
								if(validData){
									//submit to backend
									formSubmitAjax();
								}else{
									showFormError("Please fill all mandatory fields");
								}
							}
				});
				
				var oMatrix = new sap.ui.commons.layout.MatrixLayout({
						id : 'matrix4',
						layoutFixed : true,
						width : '100%',
						columns : 3,
						widths: ['20px', '250px', '170px'],
						rows: [
								new sap.ui.commons.layout.MatrixLayoutRow("r0", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c01", {content:nameL} ),
									new sap.ui.commons.layout.MatrixLayoutCell()
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r1", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c11", {content:nameTF} ),
									new sap.ui.commons.layout.MatrixLayoutCell()
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r2", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c21", {content:companyL} ),
									new sap.ui.commons.layout.MatrixLayoutCell()
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r3", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c31", {content:companyTF} ),
									new sap.ui.commons.layout.MatrixLayoutCell()
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r4", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c41", {content:roleL} ),
									new sap.ui.commons.layout.MatrixLayoutCell()
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r5", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c51", {content:roleTF} ),
									new sap.ui.commons.layout.MatrixLayoutCell()
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r6", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c61", {content:emailL} ),
									new sap.ui.commons.layout.MatrixLayoutCell()
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r7", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c71", {content:emailTF} ),
									new sap.ui.commons.layout.MatrixLayoutCell()
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r8", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c81", {content:phoneL} ),
									new sap.ui.commons.layout.MatrixLayoutCell()
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r9", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c91", {content:phoneTF} ),
									new sap.ui.commons.layout.MatrixLayoutCell("c92", {content: registerLargeImg, rowSpan: 6})
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r10", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c101", {content:commentsL} ),
									new sap.ui.commons.layout.MatrixLayoutCell()
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r11", {cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c111", {content:commentsTA} ),
									new sap.ui.commons.layout.MatrixLayoutCell()
								]}),
								new sap.ui.commons.layout.MatrixLayoutRow("r12", {height: '40px', cells:
								[
									new sap.ui.commons.layout.MatrixLayoutCell(),
									new sap.ui.commons.layout.MatrixLayoutCell( "c113", {content:registerButton} ),
									new sap.ui.commons.layout.MatrixLayoutCell( )
								]})
						]
						
				});			
			
				//Place the dialod in the html Div 			
				oMatrix.placeAt("oDialog"); 
			};
	
			
			function validateInput(){
				var valid = true;				
				for(var i = 0 ; i < mandatoryControlArray.length ; i++ ){
					var control = mandatoryControlArray[i];
					if( ! mandatoryFieldHasValue(control) ){
						valid = false;
					}
				}
				
				if(!valid){
					return valid;
				}
				
				var oCore = sap.ui.getCore();				
				var emailControl = oCore.getControl('TF-Email');
				var	phoneControl = oCore.getControl('TF-Phone');
					
				valid = validateEmail( emailControl );
				valid = validatePhone( phoneControl );
				
				return valid;
			}; 
			
			function validateEmail( control ){				
				var email = control.getValue();
				if( email === undefined || email.trim() === ""){
					control.setValueState( sap.ui.core.ValueState.Error  ); 
					showFormError("You didn't enter an email address");	
					return false;
				}
				
			    var tfld = email.trim();                        
			    var emailFilter = /^[^@]+@[^@.]+\.[^@]*\w\w$/ ;
			    var illegalChars= /[\(\)\<\>\,\;\:\\\"\[\]]/ ;
			    
			    if (!emailFilter.test(tfld)) {              //test email for illegal characters
			        control.setValueState( sap.ui.core.ValueState.Error  ); 
					showFormError("Please enter a valid email address");
					return false;					
			    } else if (email.match(illegalChars)) {
			    	control.setValueState( sap.ui.core.ValueState.Error  ); 
					showFormError("The email address contains illegal characters");
					return false;
			    } else {
			    	control.setValueState( sap.ui.core.ValueState.None  ); 
					showFormError("");
			    }
				return true;
			}
			
			function validatePhone(control){
				var phone = control.getValue();
				
				if( phone === undefined || phone.trim() === "" ){
					control.setValueState( sap.ui.core.ValueState.Error  );
					showFormError("You didn't enter a phone number");
					return false;					
				}
				
				var stripped = phone.replace(/[\(\)\.\-\ ]/g, '');     

				if (isNaN(parseInt(stripped))) {
					showFormError("The phone number contains illegal characters");
					control.setValueState( sap.ui.core.ValueState.Error  );
					return false;					
				} else if (stripped.length < 6) {
					showFormError("The phone number is in the wrong length. Make sure to includ an area code");
					control.setValueState( sap.ui.core.ValueState.Error  );
					return false;					
				}
				else{
					control.setValueState( sap.ui.core.ValueState.None  ); 
					showFormError("");					
				}
				return true;
			}
			
			function mandatoryFieldHasValue(control){
				var value = control.getValue();      
				if(value === undefined || value.trim() === ""){
					control.setValueState( sap.ui.core.ValueState.Error  );                		
					return false;
				}else{
					control.setValueState( sap.ui.core.ValueState.None  );   
					return true;
				}
			}
			
			function nameChangeEvent(oControlEvent){
				var control = oControlEvent.oSource;	
				control.setValueState( sap.ui.core.ValueState.None  ); 
				showFormError("");
			}
			
			function emailChangeEvent( oControlEvent ){
				validateEmail( oControlEvent.oSource );
			}
			
			
			function phoneChangeEvent( oControlEvent ){
				validatePhone( oControlEvent.oSource );				    
			}
			
			
			function composePostPayload( isCRM, isLocal ) {
				var formData;	
				// Get data from form
				var oCore = sap.ui.getCore();				
				var name = oCore.getControl('TF-F-Name').getValue(),
				email = oCore.getControl('TF-Email').getValue(),
				phone = oCore.getControl('TF-Phone').getValue(),
				role =  oCore.getControl('TF-Role').getValue(),
				company =  oCore.getControl('TF-Company').getValue(),
				message = oCore.getControl('TA-Comments').getValue();
			
				if( isCRM ){						
					// Put inside a JSON and return it
					formData = {
						"d" : {
							"__metadata" : {
								"id" : "http://iltlvh609.tlv.sap.corp:8000/sap/opu/odata/sap/ZDEMO_LEAD_SRV/Leads",
								"uri" : "http://iltlvh609.tlv.sap.corp:8000/sap/opu/odata/sap/ZDEMO_LEAD_SRV/Leads",
								"type" : "ZDEMO_LEAD_SRV.Lead"
							},
							"Name" : name,
							"Email" : email,
							"Country" : phone,
							"Company" : company,
							"Message" : message
						}
					};
					
				
				}
				else{					
					
				formData = {								
								"name" : name,
								"title" : role,
								"email" : email,
								"phone" : phone,
								"company" : company,
								"message" : message,
								"isLocal" : isLocal
							};
					//formData = "name="+name+"&title="+role+"&company="+company+"&email="+email+"&phone="+phone+"&message="+message+"&isLocal="+isLocal;					
				}
				
				return formData;				
			};
			
			function showFormError(errMsg) {
				// Get error container
				var errorContainer = jQuery('#emf-error-msg span');
				// Put the error message
				errorContainer.html(errMsg);
			}
			
			function formSubmitAjax() {
				if(targetSystem === SYSTEM_TYPE_CRM){
					formSubmitAjaxToCRM();
				}
				else{
					formSubmitAjaxToC4C();
				}
			}
			
			/*
			 * Execute ajax request to backend system using makeRequest 
			 */
			function formSubmitAjaxToCRM() {
			
				
				var url = 'http://dest.crm__public/connectivity.proxy/proxy/crm/sap/opu/odata/sap/ZDEMO_LEAD_SRV/Leads';
				var postdata = composePostPayload( true, false );
				var params = {};
				
				var postdataStr = JSON.stringify(postdata);
				
				params[gadgets.io.RequestParameters.POST_DATA] = postdataStr;
				params[gadgets.io.RequestParameters.METHOD] = gadgets.io.MethodType.POST;
		
				var callback = function ajaxCallback(result) {
					if (result['rc'] === 201) {
						showFormSuccess();
					} else {
						showFormError("Could not connect to CRM system.");
					}
				};
		
				gadgets.io.makeRequest(url, callback, params);
			}
			
			
			function showFormSuccess(){
				jQuery('#formSettingsDiv').hide();
				jQuery('#formBodyID').hide();
				jQuery('#formSuccessDiv').show();
				
				var successContainer = jQuery('#emf-success-msg span');
				// Put the error message
				successContainer.html("Thank you for registering.  <br/> We will shortly confirm your application.");			
			}
			
			function formSubmitAjaxToC4C(){
		    	var src;
		    	var isLocal = false;
		    	if(document.URL.indexOf("localhost:8080") > -1 ){
		    		isLocal = true;
		    		src =  window.location.origin + "/atomiccontent/C4CConnectorServlet";
		    	}
		    	else{
		    		var tmp = gadgets.util.getUrlParameters().url;
		    		var applicationURL = tmp.substring(0, tmp.indexOf(".com") + ".com".length );
		    		var fullURL = applicationURL + "/AtomicContent/C4CConnectorServlet";
		    		
		    		src = gadgets.io.getProxyUrl(  fullURL );
		    	}
		    	
		    	var xmlhttp=new XMLHttpRequest();
		    	xmlhttp.onreadystatechange = function() {
		            if (xmlhttp.readyState === 2) {
		            	
		            	showFormSuccess();
		            	
		               /* if (xmlhttp.status === 201) {
		                           // OK
		                	showFormSuccess();
		                } else {
		                           // not OK
		                	 showFormError("Could not connect to C4C system: " + xmlhttp.responseText);
		                }*/
		            }
		    	};
		    	
		    	var oCore = sap.ui.getCore();				
				var name = oCore.getControl('TF-F-Name').getValue();
		    	
		    	xmlhttp.open("POST",src);
		    	xmlhttp.send("name="+name);
							
			}		
			
		    


			function displayFormSettings() {
				jQuery('#formSettingsDiv').show();
				jQuery('#formBodyID').hide();
				jQuery('#formSuccessDiv').hide();
			}
			
			function displayFormRuntime(){
				jQuery('#formSettingsDiv').hide();
				jQuery('#formBodyID').show();
				jQuery('#formSuccessDiv').hide();
			}		
				
		
		function loadViews(){
			
			//add menu option
			gadgets.sap.menu.addMenuItem({
				ID:'properties',
				menuID:gadgets.sap.menu.DEFAULT_MENU_ID,
				text:"Select System",
				separator:true,
				callback: editFeedMenuItemCallback
			});	
		
			init();
			
			if (gadgets.util.getUrlParameters().view === 'authoring') {

				if (targetSystem==='select') {
					displayFormSettings();
				} else {
					displayFormRuntime();
				}
			}else{
				displayFormRuntime();
			}
			
		}
		
		function editFeedMenuItemCallback() {
			resetToPersistedParams();
			displayFormSettings();
		}
		
		

	
