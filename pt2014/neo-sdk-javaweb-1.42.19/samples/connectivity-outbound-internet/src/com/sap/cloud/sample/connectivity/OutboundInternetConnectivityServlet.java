package com.sap.cloud.sample.connectivity;

import java.io.IOException;
import java.io.InputStream;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.connectivity.api.http.HttpDestination;

/**
 * Servlet implementing a connectivity example to an outbound Internet resource using HTTP destinations
 * which are provided as part of the SAP HANA Cloud Platform connectivity service.
 */
public class OutboundInternetConnectivityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int HTTP_STATUS_200_OK = 200;
    private static final int COPY_CONTENT_BUFFER_SIZE = 1024;
    private static final Logger LOGGER = LoggerFactory.getLogger(OutboundInternetConnectivityServlet.class);

    /** {@inheritDoc} */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpClient httpClient = null;
        try {
            // Get HTTP destination
            Context ctx = new InitialContext();
            HttpDestination destination = (HttpDestination) ctx.lookup("java:comp/env/outbound-internet-destination");

            // Create HTTP client
            httpClient = destination.createHttpClient();

            // Execute HTTP request
            HttpGet httpGet = new HttpGet();
            HttpResponse httpResponse = httpClient.execute(httpGet);

            // Check response status code
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HTTP_STATUS_200_OK) {
                throw new ServletException("Expected response status code is 200 but it is " + statusCode + " .");
            }

            // Copy content from the incoming response to the outgoing response
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    byte[] buffer = new byte[COPY_CONTENT_BUFFER_SIZE];
                    int len;
                    while ((len = instream.read(buffer)) != -1) {
                        response.getOutputStream().write(buffer, 0, len);
                    }
                } catch (IOException e) {
                    // In case of an IOException the connection will be released
                    // back to the connection manager automatically
                    throw e;
                } catch (RuntimeException e) {
                    // In case of an unexpected exception you may want to abort
                    // the HTTP request in order to shut down the underlying
                    // connection immediately.
                    httpGet.abort();
                    throw e;
                } finally {
                    // Closing the input stream will trigger connection release
                    try {
                        instream.close();
                    } catch (Exception e) {
                        // Ignore
                    }
                }
            }
        } catch (NamingException e) {
            // Lookup of destination failed
            String errorMessage = "Lookup of destination failed with reason: "
                    + e.getMessage()
                    + ". See "
                    + "logs for details. Hint: Make sure to have the destination "
                    + "'connectivity-outbound-internet' configured.";
            LOGGER.error("Lookup of destination failed", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    errorMessage);
        } catch (Exception e) {
            // Connectivity operation failed
            String errorMessage = "Connectivity operation failed with reason: "
                    + e.getMessage()
                    + ". See "
                    + "logs for details. Hint: Make sure to have an HTTP proxy configured in your "
                    + "local Eclipse environment in case your environment uses "
                    + "an HTTP proxy for the outbound Internet "
                    + "communication.";
            LOGGER.error("Connectivity operation failed", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    errorMessage);
        } finally {
            // When HttpClient instance is no longer needed, shut down the connection manager to ensure immediate
            // deallocation of all system resources
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }
}
