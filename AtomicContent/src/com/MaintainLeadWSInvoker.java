package com;

import org.apache.axis2.databinding.types.Token;
import org.apache.axis2.transport.http.HttpTransportProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.xi.a1s.global.ServiceStub;
import com.sap.xi.a1s.global.ServiceStub.BusinessDocumentBasicMessageHeader;
import com.sap.xi.a1s.global.ServiceStub.BusinessDocumentMessageID;
import com.sap.xi.a1s.global.ServiceStub.Indicator;
import com.sap.xi.a1s.global.ServiceStub.LeadBundleMaintainConfirmation_sync;
import com.sap.xi.a1s.global.ServiceStub.LeadBundleMaintainRequest_sync;
import com.sap.xi.a1s.global.ServiceStub.LeadMaintainConfirmationBundleMessage_sync;
import com.sap.xi.a1s.global.ServiceStub.LeadMaintainRequestBundleLead;
import com.sap.xi.a1s.global.ServiceStub.LeadMaintainRequestBundleMessage_sync;
import com.sap.xi.a1s.global.ServiceStub.LeadMaintainRequestBundleProspectParty;
import com.sap.xi.a1s.global.ServiceStub.LeadProspectPartyContactPartyMaintainBundleRequestMessageDataType;

public class MaintainLeadWSInvoker {
	
	
	public static final String NAMESPACE_URI = "com.sap";
	
	private static final Logger log = LoggerFactory
			.getLogger(MaintainLeadWSInvoker.class.getName());

	public static void main(String[] args) {
		try {
		
			new MaintainLeadWSInvoker().invokeWS("name", "title", "company", "email", "phoneNumber", "true");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	
	public LeadMaintainConfirmationBundleMessage_sync invokeWS(String name, String title, String company, String email, String phoneNumber, String isLocal) 
			throws Exception{
		
		log.error("TestServlet: in servlet - invokeWS: Start");

		ServiceStub stub = new ServiceStub("https://my302559.crm.ondemand.com/sap/bc/srt/scs/sap/manageleadin2");
		 	//= new ServiceStub("https://my302559.crm.ondemand.com/sap/bc/srt/scs/sap/manageleadin2?sap-vhost=my302559.crm.ondemand.com");
			
			
		 HttpTransportProperties.Authenticator basicAuthentication = new HttpTransportProperties.Authenticator();
		 basicAuthentication.setUsername("_MARKETO"); //"Administration01"
		 basicAuthentication.setPassword("Welcome1");
		
		 stub._getServiceClient().getOptions().
			setProperty(org.apache.axis2.transport.http.HTTPConstants.AUTHENTICATE, basicAuthentication);
		 
		 stub._getServiceClient().getOptions().
		 			setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
		
    		 
		 log.error("TestServlet: in servlet - invokeWS: building payload for Web Service");
		 LeadBundleMaintainRequest_sync leadBundleMaintainRequest_sync = buildPayload(name);
		 
		 log.error("TestServlet: in servlet - invokeWS: calling Web Service");
		 LeadBundleMaintainConfirmation_sync maintainBundle = 
				 stub.maintainBundle(leadBundleMaintainRequest_sync);		 		
		 
		 log.error("TestServlet: in servlet - invokeWS: Return from Web Service");
		 LeadMaintainConfirmationBundleMessage_sync leadBundleMaintainConfirmation_sync =
				 maintainBundle.getLeadBundleMaintainConfirmation_sync();
		 
		 //System.out.println( "Service Log:   " + leadBundleMaintainConfirmation_sync.getLog().toString() );
		 
		 return leadBundleMaintainConfirmation_sync;
	}
	
	
	private LeadBundleMaintainRequest_sync buildPayload(String name)
	{
		 /*
		  *      <Lead actionCode="01">
				     <Name>Lead Creation - DV</Name>
				     <QualificationLevelCode>01</QualificationLevelCode>
				     <OriginTypeCode>003</OriginTypeCode>
				     <ProspectParty actionCode="01">
				     <BusinessPartnerInternalID>MC9785</BusinessPartnerInternalID>
				     <ContactParty actionCode="01">
				     <BusinessPartnerInternalID>MC2471</BusinessPartnerInternalID>
				     <MainIndicator>true</MainIndicator>
				     </ContactParty>
				     </ProspectParty>
			     </Lead>
		  */
		 LeadBundleMaintainRequest_sync leadBundleMaintainRequest_sync0 = new LeadBundleMaintainRequest_sync();
		 LeadMaintainRequestBundleMessage_sync leadMaintainReqBundleMsg = new LeadMaintainRequestBundleMessage_sync();
		 LeadMaintainRequestBundleLead leadMaintainReqBundleLead = new LeadMaintainRequestBundleLead();
		 
		 //Lead actionCode="01"
		 leadMaintainReqBundleLead.setActionCode(ServiceStub.ActionCode.Factory.fromString("01", NAMESPACE_URI));
		 
		 //Name
		 leadMaintainReqBundleLead.setName(ServiceStub.LANGUAGEINDEPENDENT_LONG_Description.Factory.fromString( name, NAMESPACE_URI));
         
		 //QualificationLevelCode
		 leadMaintainReqBundleLead.setQualificationLevelCode(ServiceStub.LeadQualificationLevelCode.Factory.fromString("01", NAMESPACE_URI));
		 
		 //OriginTypeCode
		 leadMaintainReqBundleLead.setOriginTypeCode(ServiceStub.CustomerTransactionDocumentOriginTypeCode.Factory.fromString("003", NAMESPACE_URI) );
		 
		 //ProspectParty - action code
		 LeadMaintainRequestBundleProspectParty leadMaintainRequestBundleProspectParty = new ServiceStub.LeadMaintainRequestBundleProspectParty();
		 leadMaintainRequestBundleProspectParty.setActionCode( ServiceStub.ActionCode.Factory.fromString("01", NAMESPACE_URI));
		 
		 //ProspectParty -  BusinessPartnerInternalID
		 leadMaintainRequestBundleProspectParty.setBusinessPartnerInternalID(ServiceStub.BusinessPartnerInternalID.Factory.fromString("1001", NAMESPACE_URI)); //MC9785
		 
		//ProspectParty - ContactParty
		 LeadProspectPartyContactPartyMaintainBundleRequestMessageDataType contactParty = 
				 new LeadProspectPartyContactPartyMaintainBundleRequestMessageDataType();
		 
		//ProspectParty - ContactParty - ActionCode
		 contactParty.setActionCode(ServiceStub.ActionCode.Factory.fromString("01", NAMESPACE_URI));
		//ProspectParty - ContactParty - BusinessPartnerInternalID
		 contactParty.setBusinessPartnerInternalID(ServiceStub.BusinessPartnerInternalID.Factory.fromString("1000654", NAMESPACE_URI)); //MC2471
		//ProspectParty - ContactParty - MainIndicator
		 Indicator indicator = new Indicator();
		 indicator.setIndicator(true);
		 contactParty.setMainIndicator(indicator);
		 
		 contactParty.setObjectNodeSenderTechnicalID(ServiceStub.ObjectNodePartyTechnicalID.Factory.fromString("45784578", NAMESPACE_URI));
		 leadMaintainRequestBundleProspectParty.setContactParty(new LeadProspectPartyContactPartyMaintainBundleRequestMessageDataType[]{contactParty});
		 
		 leadMaintainReqBundleLead.setProspectParty(leadMaintainRequestBundleProspectParty );			 
		 
		leadMaintainReqBundleMsg.setLead( new LeadMaintainRequestBundleLead[]{leadMaintainReqBundleLead});
		
		BusinessDocumentBasicMessageHeader businessDocumentBasicMessageHeader = 
													new ServiceStub.BusinessDocumentBasicMessageHeader();
		BusinessDocumentMessageID businessDocumentMessageID = new BusinessDocumentMessageID();
		Token token = new Token("?");
		businessDocumentMessageID.setBusinessDocumentMessageIDContent(token);		
	
		businessDocumentBasicMessageHeader.setID(businessDocumentMessageID);
		leadMaintainReqBundleMsg.setBasicMessageHeader(businessDocumentBasicMessageHeader );

		 
		 log.error("TestServlet: in servlet - invokeWS: Setting request");
		 leadBundleMaintainRequest_sync0.setLeadBundleMaintainRequest_sync(leadMaintainReqBundleMsg);
		 
		 return leadBundleMaintainRequest_sync0;
	}

	

}
