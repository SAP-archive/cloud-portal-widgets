package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

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
		
		  	log.error("TestServlet: doGet");

		  	String name =request.getParameter("name");
		    String title =  request.getParameter("title");
		    String company =   request.getParameter("company");
		    String email =   request.getParameter("email");
		    String phone =  request.getParameter("phone");	 
		  	
		    boolean isLocal= false;
		    
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
				log.error("TestServlet doGET: name= " + name + " Failed to invoke Maintain Leads web service: ", e);
				String failed = "failed : " + e.getMessage();			    
				String res = "{ \"data\": \"" + failed + "\" }";			
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
				response.getWriter().print( res );	
			}
	    	
	  
	}
	

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String body = getBody(request);
		String name= "";
		StringTokenizer st = new StringTokenizer(body, "=");
		if( st.hasMoreTokens()){
			String element = (String)st.nextElement();
			if(element.equals("name")){
				name = (String)st.nextToken();
			}
		}
		
		log.error("TestServlet:  request.getParameter(name): " + name);
		
		MaintainLeadWSInvoker wsInvoker = new MaintainLeadWSInvoker();
		    
		try {
				
		    	LeadMaintainConfirmationBundleMessage_sync leadMaintainConfirmationBundleMessage = 
		    			wsInvoker.invokeWS(name, "", "", "", "", false);
		    	
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
				log.error("TestServlet doPost: Failed to invoke Maintain Leads web service: ", e);
				String failed = "failed : " + e.getMessage();			    
				String res = "{ \"data\": \"" + failed + "\" }";			
				response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
				response.getWriter().print( res );	
			}
	    

	}
	
	
	public static String getBody(HttpServletRequest request) throws IOException {

	    String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;
	}

}
