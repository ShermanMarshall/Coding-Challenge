package com.shermanmarshall.json2;

import java.io.IOException;

public class JSONError extends IOException {
	public static final String IO_ERROR = "Could not read input from InputStream"; 
	public static final String INVALID_JSON = "Invalid JSON: structure is not of an Object or Array";
	public static final String CANNOT_PARSE = "Invalid JSON: cannot parse into Object or Array";
	public static final String INVALID_PRIMITIVE = "Invalid JSON: cannot parse primitive value";
	
	public String message = "";
    public JSONError(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
    
    public static JSONError CANNOT_PARSE() {
    	return new JSONError(CANNOT_PARSE);
    }
}
