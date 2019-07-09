package com.shermanmarshall.json2;

public class JSONParseUtils {
	
	public static void isWhiteSpace(byte[] src, int[] idx) {
		byte input = src[idx[0]];
		while (input == ' ' || input == '\t' || input == '\n' || input == '\r') {
			input = src[++idx[0]];
		}
	}
	
	public static void isWhiteSpace(JSONInstance instance) {
		byte input = instance.src[instance.idx];
		while (input == ' ' || input == '\t' || input == '\n' || input == '\r') {
			input = instance.src[++instance.idx];
		}
	}

	public static String readStringOrKey(JSONInstance instance) {
		String content = null;
		if (instance.atpp() == '"') {
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
				sb.append((char) instance.atpp());
	
			} while ((instance.isInbounds()) && (instance.at() != '"'));
			instance.idx++;
			content = sb.toString();
		} else {
			//System.out.println((idx[0] -1) + " " + ((char) src[idx[0] -1]) + " " + (src[idx[0]-1]));
			//System.out.println(new String(src));
			System.out.println("here");
		}
		return content;
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
	
	public static Object getValue(JSONInstance instance) throws JSONError {
			//byte[] src, int[] idx) throws JSONError {
		//JSONParseUtils.isWhiteSpace(src, idx);
		JSONParseUtils.isWhiteSpace(instance);
		if (instance.at() == '"') {
			return JSONParseUtils.readStringOrKey(instance);
		} else if (instance.at() == '{') {
			return readObject(instance);
		} else if (instance.at() == '[') {
			return readArray(instance);
		} else if (Character.isDigit(instance.at()) || instance.at() == '-') {
			instance.atpp();
			return readNumber(instance);
			//int value = 0; if (idx + 4 < length) { value = readInt(src, idx, length); }
			//char nextChar = (char) src[idx + 4];
			//if (JSONParseUtils.isWhiteSpace((byte) nextChar) || nextChar == ',' || nextChar == '}')
			//	return (Integer) value;
		} else {
			return readBoolean(instance);
		}
	}
	
	public static Number readNumber(JSONInstance instance) throws JSONError {
		StringBuilder sb = new StringBuilder().append((char) instance.atpp());
		boolean isDecimalPresent = false;
		while (Character.isDigit(instance.at())) {
			sb.append((char) instance.atpp());
			if (instance.at() == '.') {
				if (!isDecimalPresent) {
					isDecimalPresent = true; //!isDecimalPresent;
					sb.append(".");
					instance.atpp();
				} else {
					throw new JSONError(JSONError.INVALID_PRIMITIVE + " Number");
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
		throw new JSONError(JSONError.INVALID_PRIMITIVE + " Number 2");
	}
	
	public static Boolean readBoolean(JSONInstance instance) throws JSONError {
        if (instance.idx < (instance.src.length + 4)) {
            if (instance.atpp() == 't') {
                if (instance.atpp() == 'r') {
                    if (instance.atpp() == 'u') {
                        if (instance.atpp() == 'e') {
                            return true;
                        }
                    }
                }
                throw new JSONError(JSONError.INVALID_PRIMITIVE + " Boolean 1");
            } else if (instance.atpp() == 'f') {
                if (instance.atpp() == 'a') {
                    if (instance.atpp() == 'l') {
                        if ((instance.atpp() == 's')) {
                            if (instance.isInbounds() && (instance.atpp() == 'e')) {
                                return false;
                            }
                        }
                    }
                }
                throw new JSONError(JSONError.INVALID_PRIMITIVE + " Boolean 2");
            } else if (instance.atpp() == 'n') {
                if (instance.atpp() == 'u') {
                    if (instance.atpp() == 'l') {
                        if (instance.atpp() == 'l') {
                            return null;
                        }
                    }
                }
            }
        }
        throw new JSONError(JSONError.INVALID_PRIMITIVE + " Boolean 3");
    }
	
	public static byte[] getSubset(int srcOffset, int size, byte[] src) {
		byte[] subset = new byte[1 + size - srcOffset];
		System.out.println(srcOffset + " " + size + " " + subset.length);
		System.arraycopy(src, srcOffset, subset, 0, subset.length);
		return subset;
	}
	
	public static JSONObject readObject(JSONInstance instance) throws JSONError {
		int endOfObject = instance.idx + 1, jsonObjectCount = 0;
		
		JSONParseUtils.isWhiteSpace(instance);
		byte current = instance.at();
		while (endOfObject < instance.src.length && (current != '}' || jsonObjectCount != 0)) {
			if (current == '{') {
				jsonObjectCount++;
			} else if (current == '}') {
				jsonObjectCount--;
			}
			if (++endOfObject < instance.src.length)
				current = instance.at(endOfObject);
		}
		byte[] subset = getSubset(instance.idx, endOfObject, instance.src);
		instance.idx = endOfObject;
		//new byte[1 + endOfObject - idx[0]];
		//System.arraycopy(src, idx[0], subset, 0, subset.length);
		//System.out.println(subset.length + " " + new String(subset));
		return JSONObject.parse(subset);
	}
	
	public static JSONArray readArray(JSONInstance instance) throws JSONError {
		int endOfArray = instance.idx + 1, jsonArrayCount = 0;
		
		JSONParseUtils.isWhiteSpace(instance);
		byte current = instance.at();
		while (endOfArray < instance.src.length && current != ']' || jsonArrayCount != 0) {
			if (current == '[') {
				jsonArrayCount++;
			} else if (current == ']') {
				jsonArrayCount--;
			}
			if (++endOfArray < instance.src.length)
				current = instance.at(endOfArray);
		}
		byte[] subset = getSubset(instance.idx, endOfArray, instance.src);
		instance.idx = endOfArray;
		//new byte[1 + endOfArray - idx[0]];
		//System.arraycopy(src, idx[0], subset, 0, subset.length);
		//System.out.println(subset.length + " " + new String(subset));
		return JSONArray.parse(subset);
	}
	
}
