package com.sap.cloud.sample.documentstore.integrationtest;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * Integration test using HttpUnit.
 */
public class DocumentStoreIntegrationTest {
    private static final long WAIT_TIME = 1000;
    private static final int MAX_ATTEMPTS = 100;
    private static final int MS_PER_S = 1000;

    private static String serverUrl, username, password;

    private WebConversation wc;
    private WebRequest request;
    private WebResponse response;

    /**
     * Take a provided server URL usually fed in from outside through the build process or default to the local server
     * as provided through Eclipse to run the integration test against.
     */
    @BeforeClass
    public static void setupSuite() {
        // Get integration test server URL
        serverUrl = System.getProperty("integration.test.server.url");
        if (serverUrl == null) {
            serverUrl = "http://localhost:8080";
        }
        System.out.println("Running against " + serverUrl);

        // Guess integration test scenario, set user name and password and wait for server if locally started
        if (serverUrl.contains("localhost")) {
            username = "john";
            password = "doe";
            checkLocalMongoDBServer();
            waitForEcmServer();
        } else {
            username = System.getProperty("sap.cloud.username");
            password = System.getProperty("sap.cloud.password");
        }
        System.out.println("Using user " + username);
    }

    /**
     * Check for running local Mongo DB server and throw exception if not found.
     */
    private static void checkLocalMongoDBServer() {
        try {
            new URL("http://localhost:27017/").openStream().close();
            System.out.println("Found local MongoDB");
        } catch (Exception exception) {
            throw new RuntimeException("Failed to access local Mongo DB server. See readme.txt for more details.");
        }
    }

    /**
     * Check for running local ECM server and throw exception if not available.
     */
    private static void waitForEcmServer() {
        try {
            boolean ready = false;
            int attempts = 0;

            // Use the ping servlet to wait for a response indicating that
            URL url = new URL(serverUrl + "/n/ping");
            // the ECM is started
            while (!ready) {
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int responseCode = conn.getResponseCode();
                    ready = responseCode == HttpURLConnection.HTTP_OK;
                    ++attempts;
                    if (ready) {
                        System.out.println("ECM server is ready.");
                    }
                } catch (IOException ioEx) {
                    System.out.println("Error while connecting to ECM server: " + ioEx);
                }
                Thread.sleep(WAIT_TIME);
            }
            if (attempts >= MAX_ATTEMPTS) {
                throw new RuntimeException("Timeout: Failed to access ECM server, server not available after "
                                + MAX_ATTEMPTS * MS_PER_S + " seconds");
            }
        } catch (Exception exception) {
            throw new RuntimeException("Failed to access ECM server, server not available: " + exception);
        }
    }

    /**
     * Prepare a new web conversation for each test (grouping the requests/responses exchanged with the server).
     */
    @Before
    public void setupTest() {
        // Open web conversation
        wc = new WebConversation();
    }

    /**
     * Call the main servlet and check that it contains the expected content. This tests basic setup of the application,
     * but does not connect to document service.
     */
    @Test
    public void testDocumentStoreServlet() throws IOException, SAXException {
        // Call servlet
        request = new GetMethodWebRequest(serverUrl + "/document-store/");
        response = wc.getResponse(request);
        Assert.assertThat(response.getText(), containsString("SAP HANA Cloud Platform document service"));
    }

    /**
     * Call the connect servlet to verify that the connection to the document service works and document service returns
     * the expected results.
     */
    @Test
    public void testDocumentStoreConnectServlet() throws IOException, SAXException {
        // Call connect servlet
        wc.setAuthorization(username, password);
        request = new GetMethodWebRequest(serverUrl + "/document-store/connect");
        response = wc.getResponse(request);

        // Check that the JNDI lookup succeeded
        Assert.assertThat(response.getText(), containsString("Retrieved service instance for java:comp/env/EcmService"));

        // Check that we either could create a repository or found one
        Assert.assertThat(response.getText(), containsString("root folder of the repository with id"));

        // Check that we could upload a document and create a folder
        Assert.assertThat(
                response.getText(),
                anyOf(containsString("A folder with name 'My Folder' was created."),
                        containsString("A folder with name 'My Folder' already exists, nothing created.")));
        Assert.assertThat(
                response.getText(),
                anyOf(containsString("A document with name 'HelloWorld.txt' was created"),
                        containsString("A document with name 'HelloWorld.txt' already exists, nothing created.")));
    }
}
