package com.sap.cloud.sample.documentstore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;

/**
 * Servlet connecting to the document store service in SAP HANA Cloud Platform (requires authentication).
 */
public class DocumentStoreConnectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentStoreConnectServlet.class);

    private Session openCmisSession = null;

    /** {@inheritDoc} */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Connect to the document service in SAP HANA Cloud Platform
            connectToEcm(request, response);

            // List content of root folder
            listRootFolder(request, response);

            // Create a folder in the root folder and upload a document
            createFolderAndDoc(request, response);
        } catch (Exception e) {
            response.getWriter().println("Access of ECM service failed with reason: " + e.getMessage());
            LOGGER.error("Access of ECM service failed", e);
        }
    }

    /**
     * Connect to the document store service in SAP HANA Cloud Platform and create or look up sample repository.
     */
    private void connectToEcm(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        // Get a handle to the service by performing a JNDI lookup; EcmService must be a <resource-ref> in the web.xml
        EcmService ecmService = null;
        String ecmServiceLookupName = "java:comp/env/" + "EcmService";
        try {
            InitialContext ctx = new InitialContext();
            ecmService = (EcmService) ctx.lookup(ecmServiceLookupName);
            response.getWriter().println("<p>Retrieved service instance for " + ecmServiceLookupName + "</p>");
        } catch (NamingException e) {
            throw new RuntimeException("Lookup of ECM service " + ecmServiceLookupName
                    + " via JNDI failed with reason: " + e.getMessage());
        }

        // Create a key secured repository identified by a unique name and a secret key (minimum length 10 characters)
        String uniqueName = "SampleRepository";
        String secretKey = "abcdef0123456789";
        try {
            // Connect to ECM service accessing the repository
            openCmisSession = ecmService.connect(uniqueName, secretKey);

            // Continue if connection was successful
            response.getWriter().println("<p>Found an existing repository with name " + uniqueName + "</p>");
        } catch (CmisObjectNotFoundException e) {
            // If the session couldn't be created the repository is missing so create it now
            response.getWriter().println(
                    "<p>Could not find an existing repository with name " + uniqueName + ", creating one.</p>");
            RepositoryOptions options = new RepositoryOptions();
            options.setUniqueName(uniqueName);
            options.setRepositoryKey(secretKey);
            options.setVisibility(Visibility.PROTECTED);
            options.setMultiTenantCapable(true);
            ecmService.createRepository(options);

            // Now try again and connect to ECM service accessing the newly created repository
            openCmisSession = ecmService.connect(uniqueName, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Connect to ECM service failed with reason: " + e.getMessage());
        }

        // Print some information about the repository
        RepositoryInfo repositoryInfo = openCmisSession.getRepositoryInfo();
        if (repositoryInfo == null) {
            response.getWriter().println("<p>Failed to get repository info!</p>");
        } else {
            response.getWriter().println("<p>Product Name: " + repositoryInfo.getProductName() + "</p>");
            response.getWriter().println("<p>Repository Id: " + repositoryInfo.getId() + "</p>");
            response.getWriter().println("<p>Root Folder Id: " + repositoryInfo.getRootFolderId() + "</p>");
        }
    }

    /**
     * List content of root folder in sample repository.
     */
    private void listRootFolder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // If we didn't get a session fail with an appropriate message
        if (openCmisSession == null) {
            response.getWriter().println("<p>Cannot list folders, no connection to repository.</p>");
            return;
        }

        // Get root folder from CMIS session
        Folder rootFolder = openCmisSession.getRootFolder();

        // List content of root folder
        ItemIterable<CmisObject> children = rootFolder.getChildren();
        if (children.iterator().hasNext()) {
            response.getWriter().println(
                    "The root folder of the repository with id " + rootFolder.getId()
                            + " contains the following objects:");
            response.getWriter().println("<ul>");
            for (CmisObject o : children) {
                response.getWriter().print("<li>" + o.getName());
                if (o instanceof Folder) {
                    response.getWriter().println(", type: Folder, createdBy: " + o.getCreatedBy() + "</li>");
                } else {
                    Document doc = (Document) o;
                    response.getWriter().println(
                            ", type: Document,  createdBy: " + o.getCreatedBy() + " filesize: "
                                    + doc.getContentStreamLength() + " bytes" + "</li>");
                }
            }
            response.getWriter().println("</ul>");
        } else {
            response.getWriter().println(
                    "The root folder of the repository with id " + rootFolder.getId() + " contains no objects.");
        }
    }

    /**
     * Create folder and document in root folder in sample repository if not yet done.
     */
    private void createFolderAndDoc(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // If we didn't get a session fail with an appropriate message
        if (openCmisSession == null) {
            response.getWriter().println("<p>Cannot create folder or document, no connection to repository.</p>");
            return;
        }

        // Get root folder from CMIS session
        Folder rootFolder = openCmisSession.getRootFolder();

        // Create new folder (requires at least type id and name of the folder)
        Map<String, String> folderProps = new HashMap<String, String>();
        folderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
        folderProps.put(PropertyIds.NAME, "My Folder");
        try {
            rootFolder.createFolder(folderProps);
            response.getWriter().println("<p>A folder with name 'My Folder' was created.</p>");
        } catch (CmisNameConstraintViolationException e) {
            // If the folder already exists, we get a CmisNameConstraintViolationException
            response.getWriter().println("<p>A folder with name 'My Folder' already exists, nothing created.</p>");
        }

        // Create new document (requires at least type id and name of the document and some content)
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
        properties.put(PropertyIds.NAME, "HelloWorld.txt");
        byte[] helloContent = "Hello World!".getBytes("UTF-8");
        InputStream stream = new ByteArrayInputStream(helloContent);
        ContentStream contentStream = openCmisSession.getObjectFactory().createContentStream("HelloWorld.txt",
                helloContent.length, "text/plain; charset=UTF-8", stream);
        try {
            rootFolder.createDocument(properties, contentStream, VersioningState.NONE);
            response.getWriter().println("<p>A document with name 'HelloWorld.txt' was created.</p>");
        } catch (CmisNameConstraintViolationException e) {
            // If the document already exists, we get a CmisNameConstraintViolationException
            response.getWriter().println(
                    "<p>A document with name 'HelloWorld.txt' already exists, nothing created.</p>");
        }
    }
}