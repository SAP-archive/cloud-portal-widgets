######################################################
# Welcome to the SAP HANA Cloud Platform Mail Sample #
######################################################

The mail sample shows how to use the SAP HANA Cloud Platform connectivity service APIs to send e-mail.
The sample provides a simple web UI to compose an e-mail message and send it.

1. Execution on Your Local Server

You can run the sample on a local server without any additional configuration.
The sample UI can be reached under 'http://localhost:8080/mail/'. The local mail 
sample stores e-mails as '*.msg' files on the file system under '<local-server>/work/mailservice'.

2. Prerequisites for Execution on the Cloud

2.1 Deploy the mail sample to the cloud and start it. The Eclipse internal web browser will show the web UI when the startup
has been completed.

2.2 In order to run the sample on the Cloud you will have to configure a mail destination.
Technically, mail destinations are a type of connectivity destination. As with connectivity destinations that are specified
in a properties file and uploaded to the cloud, you also need to upload the mail destination configuration file for the
configuration to be effective.

2.2.1 Create a text file named 'Session' and open it in a text editor. Add the listed properties below and enter values for
the parameters 'mail.user', 'mail.password' and 'mail.smtp.host'. Save your changes.

Name=Session
Type=MAIL
mail.transport.protocol=smtp
mail.smtp.host=<enter SMTP host name>
mail.smtp.auth=true
mail.smtp.starttls.enable=true
mail.smtp.port=587
mail.user=<enter account user>
mail.password=<enter account password

2.2.2 To upload the file, use the SAP HANA Cloud Platform console client and enter a command in the following form:

neo put-destination --account <account_name> --application <application_name> --user <user_SDN_ID> --localpath Session

3. Prerequisites for Maven Build with Cloud Integration Tests

3.1 In order for the mail cloud integration tests to pass, you must provide the mail server account details. Set the following environment variables:

Windows:
  set MAIL_USER=your_mail_username
  set MAIL_PASSWORD=your_mail_password
  set MAIL_HOST=your_mail_host
  set MAIL_FROM_ADDRESS=mail_from_address
  set MAIL_TO_ADDRESS=mail_to_address

Linux:
  export MAIL_USER=your_mail_username
  export MAIL_PASSWORD=your_mail_password
  export MAIL_HOST=your_mail_host
  export MAIL_FROM_ADDRESS=mail_from_address
  export MAIL_TO_ADDRESS=mail_to_address

The mail destination is configured automatically by the Maven build. It uses the mail destination located in 'Session'. Required
values for 'mail.user', 'mail.password' and 'mail.smtp.host' are injected from the environment variables above. After that the mail
destination is uploaded to the Cloud using the SAP HANA Cloud Platform console client (check out pom.xml).