package com.shermanmarshall.json2;

public class JSONParseUtils {

	public static boolean isWhiteSpace(byte input) {
		return input == ' ' || input == '\t' || input == '\n' || input == '\r';
	}

	public static String readStringOrKey(byte[] src, int[] idx, int length) {
		StringBuilder sb = new StringBuilder();
		do {
			/* if (src[idx] == '/') {	//escape strings
				if (((idx + 1) < length) && (src[idx + 1] == '"')) {
					if (isEscapeQuoted) {
						if (((idx + 2) < length) && (src[idx + 2] != ',' || src[idx + 2] != ':')) continue;
					}
					sb.append((char) src[idx++]);
				}
			}*/
			sb.append((char) src[idx[0]++]);

		} while (idx[0] < length && (src[idx[0]] != '"'));
		return sb.toString();
		/*
		if (++idx < json.length()) {
			// Adjust position if the next character of the string is '}'
			if (json.charAt(idx) == '}') {
				// idx--;
			}
			return sb.toString();
		} else {
			return null;
		}
		*/
	}
	
	public static Object getValue(byte[] src, int[] idx, int length) throws JSONError {
		if (src[idx[0]] == '"') {
			return JSONParseUtils.readStringOrKey(src, idx, length);
		} else if (src[idx[0]] == '{') {
			return readObject(src, idx, length);
		} else if (src[idx[0]] == '[') {
			return readArray(src, idx, length);
		} else if (Character.isDigit(src[idx[0]]) || src[idx[0]] == '-') {
			return readNumber(src, idx, length);
			//int value = 0; if (idx + 4 < length) { value = readInt(src, idx, length); }
			//char nextChar = (char) src[idx + 4];
			//if (JSONParseUtils.isWhiteSpace((byte) nextChar) || nextChar == ',' || nextChar == '}')
			//	return (Integer) value;
		} else {
			return readBoolean(src, idx[0], length);
		}
	}
	
	public static Number readNumber(byte[] src, int[] idx, int length) throws JSONError {
		StringBuilder sb = new StringBuilder().append((char) src[idx[0]++]);
		boolean isDecimalPresent = false;
		while (Character.isDigit(src[idx[0]])) {
			sb.append((char) src[idx[0]++]);
			if (src[idx[0]] == '.') {
				if (!isDecimalPresent) {
					isDecimalPresent = true; //!isDecimalPresent;
					sb.append(".");
					idx[0]++;
				} else {
					throw new JSONError(JSONError.INVALID_PRIMITIVE);
				}
			}
		}
		try {
			String content = sb.toString();
			if (isDecimalPresent) {
				return Double.parseDouble(content);
			} else {
				return Long.parseLong(content);
			}
		} catch (NumberFormatException nfe) {
			//Impossible
		}
		throw new JSONError(JSONError.INVALID_PRIMITIVE);
	}
	
	public static Boolean readBoolean(byte[] src, int idx, int length) throws JSONError {
        if (idx < (length + 4)) {
            if (src[idx++] == 't') {
                if (src[idx++] == 'r') {
                    if (src[idx++] == 'u') {
                        if (src[idx++] == 'e') {
                            return true;
                        }
                    }
                }
                throw new JSONError(JSONError.INVALID_PRIMITIVE);
            } else if (src[idx++] == 'f') {
                if (idx < length && (src[idx++] == 'a')) {
                    if (idx < length && (src[idx++] == 'l')) {
                        if (idx < length && (src[idx++] == 's')) {
                            if (idx < length && (src[idx++] == 'e')) {
                                return false;
                            }
                        }
                    }
                }
                throw new JSONError(JSONError.INVALID_PRIMITIVE);
            } else if (src[idx++] == 'n') {
                if (idx < length && (src[idx++] == 'u')) {
                    if (idx < length && (src[idx++] == 'l')) {
                        if (idx < length && (src[idx++] == 'l')) {
                            return null;
                        }
                    }
                }
            }
        }
        throw new JSONError(JSONError.INVALID_PRIMITIVE);
    }
	
	public static int readInt(byte[] src, int idx, int length) {
		int value = 0;
		for (int x = 0; x < 4; x++) {
			value |= src[idx + x] << (8 * (3 - x));
		}
		return value;
	}

	public static JSONObject readObject(byte[] src, int[] idx, int length) throws JSONError {
		int endOfObject = idx[0];
		while (endOfObject < length && src[endOfObject] != '{') {
			endOfObject++;
		}
		byte[] subset = new byte[endOfObject - idx[0]];
		System.arraycopy(src, idx[0], subset, 0, endOfObject);
		return JSONObject.parse(subset);
	}
	

	
	public static JSONArray readArray(byte[] src, int[] idx, int length) {
		return null;
	}
}
