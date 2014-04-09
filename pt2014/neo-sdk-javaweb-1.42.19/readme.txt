##############################################
# Welcome to the SAP HANA Cloud Platform SDK #
##############################################

Runtime    : Java Web
Version    : 1.42.19
Build Date : 2013-11-22


1. Introduction

The SAP HANA Cloud Platform SDK contains everything required to build SAP HANA Cloud Platform applications. The following section will show you its inner structure.
It gives you the libraries required for compilation of SAP HANA Cloud Platform applications, contains documentation, samples, and the tools for command line usage.

2. File System Structure

   <root>
   |- api           Platform API JARs required for compilation of SAP HANA Cloud Platform applications
   |- javadoc       JavaDoc of the above platform API
   |- repository    Repository from which the local server runtime can be installed
   |- samples       Samples demonstrating how to develop for SAP HANA Cloud Platform
   |- server        Initially not present, but created once you install a local server runtime
   |- tools         SAP HANA Cloud Platform console client required for development, e.g. to install a SAP HANA Cloud Platform local runtime 
   |- licenses.txt  Licenses of third party components contained in the SAP HANA Cloud Platform SDK 
   |- readme.txt    Brief introduction into the SAP HANA Cloud Platform SDK, its content and how to set it up
   |- sdk.version   SAP HANA Cloud Platform SDK version information for use by other tools interacting with the SDK

3. Online Documentation

Instructions, tutorials, and samples can be found at https://help.hana.ondemand.com/help/.

4. Update

SDKs for all platform runtimes in their latest version are available at https://tools.hana.ondemand.com/#cloud.

5. SAP HANA Cloud Platform Tools for Java (Eclipse)

The SAP HANA Cloud Platform SDK is used by the SAP HANA Cloud Platform Tools for Java (Eclipse). 
Quick start developer information is available at https://tools.hana.ondemand.com/#cloud.
