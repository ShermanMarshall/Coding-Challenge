package com.shermanmarshall.json2;

import java.util.ArrayList;
import java.util.List;

public class JSONArray extends JSONInstance {

	private List<Object> elements = new ArrayList();
	
	public JSONArray (byte[] src) {
		this.src = src;
	} 
	
	public static JSONArray parse(byte[] src) throws JSONError {
		return parse(src, null);
	}
	
	public static JSONArray parse(byte[] src, JSONArray instance) throws JSONError {
		instance = instance == null ? new JSONArray(src) : instance;
		JSONParseUtils.isWhiteSpace(instance);
		
		System.out.println("JSONArray: " + new String(src));
		if (instance.isInbounds() && instance.atpp() == '[') {
			boolean isComplete = false;
			for (; instance.isInbounds() && !isComplete; instance.idx++) {
				if (src[instance.idx] == ']') {
					isComplete = true;
				} else {
					Object value = JSONParseUtils.getValue(instance);
					instance.elements.add(value);
					
					System.out.println(value.toString());
					
					JSONParseUtils.isWhiteSpace(instance);
					if (instance.at() != ',') {
						throw new JSONError(JSONError.CANNOT_PARSE + " 4");
					}
				}
			}
		}
		
		System.out.println("end");

		return instance;
	}
	
	public int getIdx() {
		return idx;
	}
	
	public void setIdx(int idx) {
		this.idx = idx;
	}
}
