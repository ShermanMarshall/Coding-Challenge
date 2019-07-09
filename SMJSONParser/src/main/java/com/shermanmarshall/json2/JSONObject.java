package com.shermanmarshall.json2;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JSONObject extends JSONInstance {
	private Map<String, Object> elements = new HashMap();
	
	public JSONObject(byte[] src) {
		this.src = src;
	}
	
	public static JSONObject parse(String src) throws JSONError {
		return parse(src.getBytes());
	}

	public static JSONObject parse(InputStream is) throws JSONError {
		byte[] data, output, tmp;
		try {
			if (is.available() <= 0) {
				return null;
			} else {
				data = new byte[4096];
				output = new byte[0];
			}

			int total = 0, bytesRead, newMax;
			while ((bytesRead = is.read(data)) != -1) {
				newMax = total + bytesRead;
				if (output.length < newMax) {
					tmp = new byte[newMax];
					System.arraycopy(output, 0, tmp, 0, total);
					output = tmp;
				}
				System.arraycopy(data, 0, output, total, bytesRead);
				total = newMax;
			}
		} catch (IOException ioe) {
			throw new JSONError(JSONError.CANNOT_PARSE + " - 1");
		}
		return parse(output);
	}

	public static JSONObject parse(byte[] src) throws JSONError {
		return parse (src, null);
	}

	public static JSONObject parse(byte[] src, JSONObject instance) throws JSONError {
		instance = instance == null ? new JSONObject(src) : instance;
		boolean isComplete = false;
		JSONParseUtils.isWhiteSpace(instance);
		if (!(instance.isInbounds() && instance.atpp() == '{')) {
			throw new JSONError(JSONError.INVALID_JSON + " here");
		} else {
			for ( ; instance.isInbounds() && !isComplete; instance.idx++) {
				JSONParseUtils.isWhiteSpace(instance);
				if (instance.at() == '}') {
					isComplete = true;
				} else {
					String key = JSONParseUtils.readStringOrKey(instance);
					System.out.println("Key: " + key);
					if (key == null) {
						throw new JSONError(JSONError.CANNOT_PARSE + " - 2");
					} else {
						JSONParseUtils.isWhiteSpace(instance);
						if (instance.at() == ':') {	//value delimiter
							instance.idx++;
							Object value = JSONParseUtils.getValue(instance);
							instance.elements.put(key, value);
						} else {
							throw new JSONError(JSONError.CANNOT_PARSE + " - 3");
						}
					}
				}
			}
		}
		return instance;
	}
	
	public int getIdx() {
		return idx;
	}
	
	public void setIdx(int idx) {
		this.idx = idx;
	}
}
