package com.shermanmarshall.json2;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class JSONObject extends HashMap {

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
		return parse (src, new int[] {0}, src.length);
	}

	public static JSONObject parse(byte[] src, int[] idx, int length) throws JSONError {
		JSONObject validObject = new JSONObject();
		boolean isComplete = false;
		while (JSONParseUtils.isWhiteSpace(src[idx[0]])) {
			idx[0]++;
		}
		if (!(idx[0] < length && src[idx[0]] == '{')) {
			throw new JSONError(JSONError.INVALID_JSON);
		} else {
			for (; (idx[0] < length) && !isComplete; idx[0]++) {
				while (JSONParseUtils.isWhiteSpace(src[idx[0]])) {
					idx[0]++;
				}
				if (src[idx[0]] == '}') {
					isComplete = true;
				} else {
					String key = JSONParseUtils.readStringOrKey(src, idx, length);
					if (key == null) {
						throw new JSONError(JSONError.CANNOT_PARSE + " - 2");
					} else {
						while(JSONParseUtils.isWhiteSpace(src[idx[0]])) {
							idx[0]++;
						}
						if (src[idx[0]] == ':' && (idx[0] + 4 < length)) {	//value delimiter
							Object value = JSONParseUtils.getValue(src, idx, length);
							validObject.put(key, value);
						} else {
							throw new JSONError(JSONError.CANNOT_PARSE + " - 3");
						}
					}
				}
			}
		}
		return validObject;
	}
}
