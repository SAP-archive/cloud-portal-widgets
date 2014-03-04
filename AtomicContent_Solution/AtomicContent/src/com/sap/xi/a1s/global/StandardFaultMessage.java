
/**
 * StandardFaultMessage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

package com.sap.xi.a1s.global;

public class StandardFaultMessage extends java.lang.Exception{

    private static final long serialVersionUID = 1392620812530L;
    
    private com.sap.xi.a1s.global.ServiceStub.StandardFaultMessage faultMessage;

    
        public StandardFaultMessage() {
            super("StandardFaultMessage");
        }

        public StandardFaultMessage(java.lang.String s) {
           super(s);
        }

        public StandardFaultMessage(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public StandardFaultMessage(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.sap.xi.a1s.global.ServiceStub.StandardFaultMessage msg){
       faultMessage = msg;
    }
    
    public com.sap.xi.a1s.global.ServiceStub.StandardFaultMessage getFaultMessage(){
       return faultMessage;
    }
}
    