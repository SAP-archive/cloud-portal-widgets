package com.sap.cloud.sample.documentstore;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet allowing to connect to the document store service in SAP HANA Cloud Platform.
 */
public class DocumentStoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /** {@inheritDoc} */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // To connect to document service use /connect in the URL (requires authentication)
        response.getWriter().println(
                "<p><a href=\"connect\">Connect</a> to the SAP HANA Cloud Platform document service "
                        + "(in the local scenario the user name is 'john' and the password is 'doe' and "
                        + "in cloud scenario use your SAP HANA Cloud Platform credentials)</p>");
    }
}
