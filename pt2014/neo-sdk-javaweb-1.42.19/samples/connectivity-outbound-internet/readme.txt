################################################################################
# Welcome to the SAP HANA Cloud Platform Outbound Internet Connectivity Sample #
################################################################################

1. Prerequisites for execution on your local server from within Eclipse

1.1 Configure the destination 'outbound-internet-destination'

In order to run the sample from within your Eclipse installation you will have to import a destination into the local server.
You can do that by double-clicking on your local server in the Servers view and then switching to the Connectivity tab.
There please import the destination outbound-internet-destination from inside your sample root folder.

This is done automatically by the Maven build. It uses the destination located here and copies it (check out pom.xml)
to the local server that it creates on-the-fly during the build. By this the integration tests find the destination.
For the local server that you create yourself in Eclipse, you have to do it separately/conveniently in the server view.

1.2 (Optional) Configure an HTTP proxy for the outbound internet connection

The sample shows how to consume the Internet resource "https://help.hana.ondemand.com/terms_of_use.html". In case you run the sample
from behind an outgoing HTTP proxy, make sure you have configured the proxy as part of the SAP HANA Cloud Platform local runtime
in Eclipse. To do this, double-click the local server in the Servers view, then click the "Open launch
configuration" link on the Overview tab, this opens the "Edit launch configuration properties" view. Navigate there to
the "Arguments" tab and add your proxy definition as "VM arguments" in following format:
-DproxyHost=<proxy-host> -DproxyPort=<proxy-port>
