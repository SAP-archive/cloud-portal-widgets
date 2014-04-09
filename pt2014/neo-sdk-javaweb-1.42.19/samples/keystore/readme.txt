##########################################################
# Welcome to the SAP HANA Cloud Platform Keystore Sample #
##########################################################

1. Prerequisites for execution on your local server from within Eclipse

1.1 Copy the keystore into your local server

In order to run the sample from within your Eclipse installation you will need to copy the keystore into your local server.
Origin:                <SAP HANA Cloud Platform SDK Home>\samples\keystore\mystore.jks
Destination (usually): <Eclipse Workspace>\Servers\SAP HANA Cloud local runtime-config\config_master\com.sap.cloud.crypto.keystore
Note: If the folder com.sap.cloud.crypto.keystore is missing, your local server is outdated and doesn't support keystores.
Please destroy and recreate your local server from this SDK.  

This is done automatically by the Maven build. It uses the keystore located here and copies it (check out pom.xml)
to the local server that it creates on-the-fly during the build. By this the integration tests find the keystore.
For the local server that you create yourself in Eclipse, you have to do it manually.
