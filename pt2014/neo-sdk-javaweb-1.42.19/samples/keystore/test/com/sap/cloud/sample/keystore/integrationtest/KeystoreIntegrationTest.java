package com.sap.cloud.sample.keystore.integrationtest;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Tests the availability of the keystore service.
 */
public class KeystoreIntegrationTest {
    private static final String SIGNATURE_VERIFICATION_OK = "Signature verification OK";

    private static final String PARAMETER_NAME_PRIVATE_KEY_PASSWORD = "privateKeyPassword";
    private static final String PARAMETER_NAME_CERTIFICATE_ALIAS = "certificateAlias";
    private static final String PARAMETER_NAME_PRIVATE_KEY_ALIAS = "privateKeyAlias";
    private static final String PARAMETER_NAME_KEYSTORE_NAME = "keystoreName";

    private static String serverUrl;
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
     * Call the main servlet and check that it contains the expected content.
     */
    @Test
    public void testKeystore() throws IOException, SAXException {
        request = new GetMethodWebRequest(serverUrl + "/keystore/");
        request.setParameter(PARAMETER_NAME_KEYSTORE_NAME, "mystore");
        request.setParameter(PARAMETER_NAME_PRIVATE_KEY_ALIAS, "signingkey");
        request.setParameter(PARAMETER_NAME_PRIVATE_KEY_PASSWORD, "secret");
        request.setParameter(PARAMETER_NAME_CERTIFICATE_ALIAS, "verificationcert");

        response = wc.getResponse(request);
        int responseCode = response.getResponseCode();
        String responseMessage = response.getResponseMessage();
        String responseText = response.getText().trim();

        System.out.println("Status code: " + responseCode);
        System.out.println("Status message: " + responseMessage);
        System.out.println("Response: " + responseText);

        Assert.assertEquals(HTTP_OK, responseCode);
        Assert.assertTrue("Test case failed. Response from the server: " + responseText,
                               responseText.contains(SIGNATURE_VERIFICATION_OK));
    }
}