######################################################
# Welcome to the SAP HANA Cloud Platform SDK Samples #
######################################################

Runtime    : Java Web
Version    : 1.42.19
Build Date : 2013-11-22


1. Introduction

All samples can be imported as Eclipse projects or as Maven projects.
We focused on Eclipse and Maven for their wide adoption, but the principles apply also to other IDEs and build systems.

2. Prerequisites and Import Steps (for Eclipse projects)

(1) As a prerequisite you will need to install the SAP HANA Cloud Platform Tools for Java (Eclipse).
For more information about installing and configuring these tools, see https://tools.hana.ondemand.com.

(2) As a prerequisite you will have to create a server runtime environment called "SAP HANA Cloud".
You can do so through the menu: Window->Preferences->Server->Runtime Environment->Add...->SAP->SAP HANA Cloud...
If you imported a sample not having done the above you will have an error: "Faceted Project Problem: Target runtime SAP HANA Cloud is not defined".
You can now do as described above or simply run the sample which will guide you through the creation of the runtime: Run As->Run on Server...

Import Steps for Eclipse projects:
  Import as Eclipse Project:
    Menu: File -> Import... -> General -> Existing Projects into Workspace

2. Prerequisites and Import Steps (for Maven projects)

(1) As a prerequisite for the Maven import you will need to do the above and in addition install the appropriate Eclipse Maven tools as well.
You require the Maven-Eclipse integration and the Maven-Eclipse-WTP integration that bridges between Maven, Eclipse, and the WTP tooling which is useful for web projects.

To install the Maven Integration for Eclipse WTP, proceed as follows:
  From the main menu of the Eclipse IDE, choose  Help -> Eclipse Marketplace.
  Enter 'Maven' in the Find field and choose Go.
  Locate the 'Maven Integration for Eclipse WTP' item and choose the Install button.

Import Steps for Maven projects:
  Import as Maven Project:
    Menu: File -> Import... -> Maven -> Existing Maven Projects

The Maven build shows how to completely automate a headless build & test that:
- builds a Java EE application based on the SAP HANA Cloud Platform API
- shows how to run rudimentary unit tests (not present in all samples)
- installs, starts, waits for, and stops the local server runtime
- deploys the application to the local server runtime and runs the integration test
- starts, waits for, and stops the cloud server runtime
- deploys the application to the cloud server runtime and runs the integration test

You can activate the local integration tests with the following profile:
mvn verify -P local-integration-tests

You can activate the cloud integration tests with the following profile:
mvn verify -P cloud-integration-tests

In order for the cloud integration test to pass, you need to provide your proxy settings and SAP HANA Cloud Platform account details.
Please note: In case you don't have a proxy, you will have to remove all occurrences of the proxy properties in the pom.xml. 

Examples:
  Windows:
    set PROXY_HOST=proxy_host
    set PROXY_PORT=proxy_port
    set SAP_CLOUD_ACCOUNT=your_account
    set SAP_CLOUD_USERNAME=your_username
    set SAP_CLOUD_PASSWORD=your_password

  Linux:
    export PROXY_HOST=proxy_host
    export PROXY_PORT=proxy_port
    export SAP_CLOUD_ACCOUNT=your_account
    export SAP_CLOUD_USERNAME=your_username
    export SAP_CLOUD_PASSWORD=your_password
    
If you are using a trial account, you need to change the cloud host in the pom.xml file to hanatrial.ondemand.com.
