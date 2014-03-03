package com;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.xi.a1s.global.ServiceStub.LeadMaintainConfirmationBundleMessage_sync;



/**
 * Servlet implementation class TestServlet
 */
public class C4CConnectorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory
			.getLogger(C4CConnectorServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public C4CConnectorServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.error("TestServlet: in servlet - doGet");
		response.getWriter().print("doGet:  Yo ");
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log.error("TestServlet: in servlet - doPost");
	    
	    String name = request.getParameter("name");
	    String title = request.getParameter("title");
	    String company = request.getParameter("company");
	    String email = request.getParameter("email");
	    String phone = request.getParameter("phone");	  
	    String isLocal = request.getParameter("isLocal");	 
	    
	    log.error("TestServlet:  request.getParameter(name): " + name);
	    log.error("TestServlet: request.getParameter(title): " + title);
	    log.error("TestServlet: request.getParameter(email): " + email);
	    
	    MaintainLeadWSInvoker wsInvoker = new MaintainLeadWSInvoker();
	    
	    try {
			
	    	LeadMaintainConfirmationBundleMessage_sync leadMaintainConfirmationBundleMessage = 
	    			wsInvoker.invokeWS(name, title, company, email, phone, isLocal);
	    	
	    	if( leadMaintainConfirmationBundleMessage.isLeadSpecified() ){
	    		String success = "success";
	  	    
	    		String res = "{ \"data\": \"" + success + "\" }";
	    		response.setStatus(HttpServletResponse.SC_CREATED);
	    		response.getWriter().print( res );
	    	}
	    	else
	    	{
	    		String failed = "failed : Registration was not persisted on C4C.";			 
	    		String res = "{ \"data\": \"" + failed + "\" }";			
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
				response.getWriter().print( res );	
	    	}
			
		} catch (Exception e) {			  	
			log.error("TestServlet: Failed to invoke Maintain Leads web service: ", e);
			String failed = "failed : " + e.getMessage();			    
			String res = "{ \"data\": \"" + failed + "\" }";			
			response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
			response.getWriter().print( res );	
		}
	    
	}

}
