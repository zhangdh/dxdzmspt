package com.coffice.util;

import ccit.security.bssp.ex.CrypException;

public class Ukey {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		byte[] cert="MIIC0jCCAbqgAwIBAgICEMQwDQYJKoZIhvcNAQEFBQAwZjELMAkGA1UEBhMCQ04xKzApBgNVBAoMIkNISU5BIFNFQ1RSVVNUIENPUlBPUkFUSU9OIExJTUlURUQxDjAMBgNVBAsMBUlQQVNTMRowGAYDVQQDDBFDTkMgSVBBU1MgVVNFUiBDQTAeFw0wOTA5MDIwNTQzMTZaFw0xOTA5MDIwNTQzMTZaMDIxCzAJBgNVBAYTAkNOMQwwCgYDVQQKDANDTkMxFTATBgNVBAMMDFVXWDEwMDEwMDUwNDCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAiqPPCBAvrpcLV2Uq6u2IKAXsyd5bEtsrkzRl5zJ4EpGg+KFiqMe3hhWW+3/sIvQBFJQ86vy6QQ4aSG+f/ro9/6PQhUDuzb5nzH/Mvm646Z9A7zChWe/EXfKYkk8Bkwm1sWj4gUNZ8d5WPz5pS1yH8ZcLDE2OXOV320RMZXrlX4kCAwEAAaNCMEAwCwYDVR0PBAQDAgTwMBMGA1UdJQQMMAoGCCsGAQUFBwMBMAkGA1UdEwQCMAAwEQYKKwYBBAGpQ2QCAQQDEwE0MA0GCSqGSIb3DQEBBQUAA4IBAQCOxpExN18AA3uZ0lVFhxMoHBWXaoqZpsAHMYT9fGrYkzOzwIR/xy4FKuwr6nLhOyJ3QnyP3uLdGIeyCw42u4ouikrCPPimY9woIZ5dxFxsCj29WYIMQLVwQgy17FBsxqoEqJX3C9K5P1FQyMti71U1WQDrhSZcodRNsGXGNRtoAuBJ7lhGxSmIRtYZQcOplruVPNCGEUDx00DmAFxketZN6DCrWsXsCv7lqQzxd7/9d+pl3/jID2dGNwXwLHkHbX2AlJCsbClhJcXWifM6baUQ0G3eNY88gRPIadiIAsObD6+vQWDkkTGclLJSrsahfrpbaPfHyKBBdhWuTFcg2GCn".getBytes();
		byte[] indata="李宁".getBytes();
		byte[] sd="EGcuRzYeFSgnAuiV+8iQ2fj/Af0W27fOofbfoe6nWe1tBvwDogF/Xt43KQhBHewR4PXgNPVn8OIeY2JNMKEFCujnIwkZb85Orq5DFlE/gjdEWQUVWZQqvHAauwkdnMZGb3ChVpvsesxiUIh2zDYkhs/WtKzEswgu8dx8FDdhCDc=".getBytes();
		try {
			int i=ccit.security.bssp.CAUtility.verifyWithCert(0x00000103,cert,indata,sd);
		} catch (CrypException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
