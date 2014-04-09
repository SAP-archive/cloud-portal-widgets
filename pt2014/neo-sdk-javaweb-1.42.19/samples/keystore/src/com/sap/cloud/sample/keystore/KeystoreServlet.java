package com.sap.cloud.sample.keystore;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.sap.cloud.crypto.keystore.api.KeyStoreService;

/**
 * Servlet demonstrating the usage of the keystore service in SAP HANA Cloud Platform.
 */
public class KeystoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String SIGNATURE_ALGORITHM_SHA256_WITH_RSA = "SHA256withRSA";
    private static final String DATA_TO_SIGN = "data to sign";

    private static final String SIGNATURE_VERIFICATION_OK = "Signature verification OK.";
    private static final String SIGNATURE_VERIFICATION_FAILED = "Signature verification failed.";

    private static final String PARAMETER_NAME_PRIVATE_KEY_PASSWORD = "privateKeyPassword";
    private static final String PARAMETER_NAME_CERTIFICATE_ALIAS = "certificateAlias";
    private static final String PARAMETER_NAME_PRIVATE_KEY_ALIAS = "privateKeyAlias";
    private static final String PARAMETER_NAME_KEYSTORE_NAME = "keystoreName";

    private static final Logger LOGGER = LoggerFactory.getLogger(KeystoreServlet.class);

    /** {@inheritDoc} */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();

        // Get a handle to the service by performing a JNDI lookup; KeyStoreService must be a <resource-ref> in the
        // web.xml
        KeyStoreService keystoreService = null;
        String keystoreServiceLookupName = "java:comp/env/" + "KeyStoreService";
        printInfo("Lookup keystore service via JNDI with name '" + keystoreServiceLookupName + "'.", writer);
        try {
            InitialContext ctx = new InitialContext();
            keystoreService = (KeyStoreService) ctx.lookup(keystoreServiceLookupName);
        } catch (NamingException e) {
            throw new ServletException("Lookup of keystore service '" + keystoreServiceLookupName
                    + "' via JNDI failed with reason: " + e.getMessage(), e);
        }
        printInfo("Keystore service obtained successfully from JNDI.", writer);

        writer.println();
        // Read and validate input parameters
        String keystoreName = request.getParameter(PARAMETER_NAME_KEYSTORE_NAME);
        String privateKeyAlias = request.getParameter(PARAMETER_NAME_PRIVATE_KEY_ALIAS);
        String certificateAlias = request.getParameter(PARAMETER_NAME_CERTIFICATE_ALIAS);
        String privateKeyPassword = request.getParameter(PARAMETER_NAME_PRIVATE_KEY_PASSWORD);

        printInfo("Received parameters:", writer);
        printInfo(PARAMETER_NAME_KEYSTORE_NAME + ": " + keystoreName, writer);
        printInfo(PARAMETER_NAME_PRIVATE_KEY_ALIAS + ": " + privateKeyAlias, writer);
        printInfo(PARAMETER_NAME_PRIVATE_KEY_PASSWORD + ": " + privateKeyPassword, writer);
        printInfo(PARAMETER_NAME_CERTIFICATE_ALIAS + ": " + certificateAlias, writer);

        writer.println();

        if (keystoreName == null || privateKeyAlias == null || privateKeyPassword == null || certificateAlias == null) {
            printMissingParametersErrorMessage(writer);
            return;
        }

        // Get a private key from the keystore service and use it to sign a string,
        // then verify the signature with the corresponding certificate
        KeyStore keyStore = null;
        try {
            // Get keystore without integrity check; to perform integrity check a password should be provided
            printInfo("Getting keystore with name '" + keystoreName + "'.", writer);
            keyStore = keystoreService.getKeyStore(keystoreName, null);
            printInfo("Keystore instance obtained.", writer);
            writer.println();

            printInfo("Getting private key with alias '" + privateKeyAlias + "'.", writer);
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(privateKeyAlias, privateKeyPassword.toCharArray());
            if (privateKey != null) {
                printInfo("Private key instance obtained.", writer);
                writer.println();
            } else {
                printError("ERROR: Keystore '" + keystoreName + "' does not contain a private key with alias '"
                          + privateKeyAlias + "'", writer, null);
                return;
            }

            printInfo("Getting certificate with alias '" + certificateAlias + "'.", writer);
            Certificate cert = keyStore.getCertificate(certificateAlias);
            if (cert != null) {
                printInfo("Certificate instance obtained.", writer);
                writer.println();
            } else {
                printError("ERROR: Keystore '" + keystoreName + "' does not contain a certificate with alias '"
                          + certificateAlias + "'", writer, null);
                return;
            }

            printInfo("Generating signature for data: '" + DATA_TO_SIGN + "' with the private key.", writer);
            byte[] signature = sign(DATA_TO_SIGN, privateKey);
            printInfo("Signature generated.", writer);
            writer.println();

            PublicKey publicKey = cert.getPublicKey();
            try {
                printInfo("Verifying signature with the certificate.", writer);
                boolean verificationResult = verifySignature(DATA_TO_SIGN, signature, publicKey);
                if (!verificationResult) {
                    printError(SIGNATURE_VERIFICATION_FAILED, writer, null);
                    return;
                }
                printInfo(SIGNATURE_VERIFICATION_OK, writer);
            } catch (SignatureException se) {
                printError(SIGNATURE_VERIFICATION_FAILED, writer, se);
            }
        } catch (Exception e) {
            printError("Internal error", writer, e);
        }
    }

    private byte[] sign(String inputString, PrivateKey privateKey) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {
        byte[] dataToBeSigned = inputString.getBytes();
        Signature signer = Signature.getInstance(SIGNATURE_ALGORITHM_SHA256_WITH_RSA);
        signer.initSign(privateKey);
        signer.update(dataToBeSigned);
        byte[] signature = signer.sign();
        return signature;
    }

    private boolean verifySignature(String data, byte[] signature, PublicKey publicKey) throws InvalidKeyException,
            NoSuchAlgorithmException, SignatureException {
        Signature verifier = Signature.getInstance(SIGNATURE_ALGORITHM_SHA256_WITH_RSA);
        verifier.initVerify(publicKey);
        verifier.update(data.getBytes());
        boolean result = verifier.verify(signature);
        return result;
    }

    private void printMissingParametersErrorMessage(PrintWriter writer) throws IOException {
        writer.println("ERROR: One or more mandatory parameter(s) missing!");
        writer.println("Please supply following parameters in the request (all are mandatory):");
        writer.println(PARAMETER_NAME_KEYSTORE_NAME + ": the name of the keystore "
                    + "from which the parameters will be read");
        writer.println(PARAMETER_NAME_PRIVATE_KEY_ALIAS + ": the alias of a private key in this keystore");
        writer.println(PARAMETER_NAME_PRIVATE_KEY_PASSWORD + ": the password for this private key");
        writer.println(PARAMETER_NAME_CERTIFICATE_ALIAS + ": the certificate that corresponds to the "
                    + "private key passed in the '" + PARAMETER_NAME_PRIVATE_KEY_ALIAS + "' parameter");
        writer.println("Example: http://<host>:<port>/keystore?"
                        + PARAMETER_NAME_KEYSTORE_NAME + "=mystore&"
                        + PARAMETER_NAME_PRIVATE_KEY_ALIAS + "=signingkey&"
                        + PARAMETER_NAME_PRIVATE_KEY_PASSWORD + "=secret&"
                        + PARAMETER_NAME_CERTIFICATE_ALIAS + "=verificationcert");
    }

    private void printError(String message, PrintWriter writer, Throwable exception) throws IOException {
        writer.println(message);
        if (exception != null) {
            LOGGER.error(message, exception);
            exception.printStackTrace(writer);
        } else {
            LOGGER.error(message);
        }
    }

    private void printInfo(String message, PrintWriter writer) {
        writer.println(message);
        LOGGER.info(message);
    }
}
