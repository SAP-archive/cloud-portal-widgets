################################################################
# Welcome to the SAP HANA Cloud Platform Document Store Sample #
################################################################

The SAP HANA Cloud Platform document store sample is a web application consuming the SAP HANA Cloud Platform document service.
The sample demonstrates connecting to the server, listing the root folder and creating a document and a
folder in the respository.

1. Prerequisites for execution on your local server from within Eclipse

1.1. Authentication

In order to run the sample from within your Eclipse installation you will have to create users for the local server.
You can do that by double-clicking on your local server in the Servers view and then switching to the Users tab.
There please add a new user with the user id john and the password doe.

This is done automatically by the Maven build. It uses the json files located here and copies them (check out pom.xml)
to the local server that it creates on-the-fly during the build. By this the integration tests find the sample user.
For the local server that you create yourself in Eclipse, you have to do it separately/conveniently in the server view.

1.2. MongoDB

For the local server the Document Service requires a MongoDB instance on your local machine. MongoDB can be downloaded
from here: http://www.mongodb.org/downloads. Installation guide for MongoDB can be found here:
http://www.mongodb.org/display/DOCS/Quickstart. You have to configure a local directory where MongoDB stores the data.

You can check your installation by opening the following URL in a browser: http://localhost:27017. If your MongoDB
installation is working you should see a text "You are trying to access MongoDB on the native driver port..." If you
don't see this text check your installation.

If you run the integration tests from Maven MongoDB will automatically be downloaded, installed and run. Be sure in
this case not to run your local installation to avoid a port conflict.
