package com.lab.commons;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class AccessToken {

	private static final String SHARED_SECRET = "ARandomStringWithGoodEntropy";
	private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	
	/**
	 * AccessTokenDemo
	 * If no args are passed create a token with the defualt identifier and default scope
	 * When started the demo from the command line call the method like this
	 * java -jar
	 * @param args
	 */
	public static void main(String[] args) {
		String identifier = null;
		String[] scope;

		// Should have at minimun one identifier and one scope arg
		if (args.length >= 2)
		{
			identifier = args[0];
			scope = new String[args.length-1];
			int scopeNr = 0;
			for (int i = 1; i < args.length; i++) {
				scope[scopeNr++] = args[i];
			}
		}
		else
		{
			scope = new String[2]; 
			scope[0] = "payment";
			scope[1] = "scope";
			identifier = "wackadoo";
		}

		String res = generateAccessToken(identifier, scope);

		System.out.println(res);
	}
	
	/**
	 * Constructs an AccessToken object for the given access token sent from a client
	 * @param identifier
	 * @param scope
	 * @param timestamp
	 * @return the generated accesstoken
	 */
	public static String generateAccessToken(String identifier, String[] scope) {
		return calcToken(identifier, scope, new Date());
	}
		
	private static String calcToken(String identifier, String[] scope, Date timestamp) {
		String timestampString = timestampFormat.format(timestamp);
		String token = calcTokenString(identifier, scope, timestampString);
		
		return Base64.encodeBase64String(token.getBytes());
	}
	
	/**
	 * Create a ruby like json string
	 * Unfortunately simple json has a different format then rubys to_json
	 * {"token":{"identifier":"wackadoo","scope":["payment","scope"],"timestamp":"2014-12-02T16:34:34+01:00"},"signature":"a4139a8301d103abf560875353d66a857140af47"}
	 * @param identifier
	 * @param scope
	 * @param timestamp
	 * @return a json string
	 */
	private static String calcTokenString(String identifier, String[] scope, String timestamp) {	
		StringBuilder jsonBuilder = new StringBuilder("{");
		jsonBuilder.append("\"identifier\":");
		jsonBuilder.append(quote(identifier));
		jsonBuilder.append(",\"scope\":");
		
		if (scope.length > 0) {
			jsonBuilder.append("[");
		}
		
		for (int i = 0; i < scope.length; i++) {
			jsonBuilder.append(quote(scope[i]));
			if (i < scope.length-1) {
				jsonBuilder.append(",");
			}
		}
		if (scope.length > 0) {
			jsonBuilder.append("],");
		}
		
		jsonBuilder.append("\"timestamp\":");
		jsonBuilder.append(quote(timestamp));
		jsonBuilder.append("}");
		
		String tokenContent = jsonBuilder.toString();
		String signature = quote(calcSignature(tokenContent));
		
		// Clean the string builder
		jsonBuilder.setLength(0);
		
		jsonBuilder.append("{\"token\":");
		jsonBuilder.append(tokenContent);
		jsonBuilder.append(",\"signature\":");
		jsonBuilder.append(signature);
		jsonBuilder.append("}");
		
		return jsonBuilder.toString();
	}
	
	private static String calcSignature(String tokenContent) {
		return DigestUtils.sha1Hex(tokenContent + "." + SHARED_SECRET);
	}
	
	
	public static String quote(String value) {
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("\"");
		jsonBuilder.append(value);
		jsonBuilder.append('\"');
		
		return jsonBuilder.toString();
	}	
}
