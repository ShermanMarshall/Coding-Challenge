package com.shermanmarshall.json2;

import java.util.ArrayList;

public class JSONArray extends ArrayList<Object> {
	public static JSONArray parse(byte[] src, int[] idx, int length) throws JSONError {
		JSONArray validArray = new JSONArray();

		while (JSONParseUtils.isWhiteSpace(src[idx[0]])) {
			idx[0]++;
		}

		if (idx[0] < length && src[idx[0]] == '[') {
			boolean isComplete = false;
			for (; idx[0] < length && !isComplete; idx[0]++) {
				if (src[idx[0]] == ']')
					isComplete = true;
				else {
					Object value = JSONParseUtils.getValue(src, idx, length);
					while (JSONParseUtils.isWhiteSpace(src[idx[0]])) {
						idx[0]++;
					}
					if (src[idx[0]] != ',')
						throw new JSONError(JSONError.CANNOT_PARSE);
				}
			}
		}

		return validArray;
	}
}
