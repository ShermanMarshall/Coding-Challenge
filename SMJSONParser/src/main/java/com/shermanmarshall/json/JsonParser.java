package com.shermanmarshall.json;

import java.math.BigDecimal;

public class JsonParser {
	private Boolean isObject = null;
	private String json;
	private Object parsedObject = null;
	private int idx = 0;

	public JsonParser(String src) {
		json = src;
		try {
			if (isObject()) {
				parsedObject = readObject(new JSONObject());
				((JSONObject) parsedObject).init();
			} else {
				parsedObject = readArray(new JSONArray());
			}
		} catch (JSONError e) {
			System.out.println(e.getMessage());
		}
	}

	public JSONArray getJSONArray() {
		return (JSONArray) parsedObject;
	}

	public JSONObject getJSONObject() {
		return (JSONObject) parsedObject;
	}

	public JSONObject readObject(JSONObject o) throws JSONError {
		boolean complete = false;
		while (isWhiteSpace()) {
			idx++;
		}
		if (canContinue() && (json.charAt(idx++) == '{')) {
			for (; (idx < (json.length())) && !complete && (json.charAt(idx) != '}'); idx++) {
				// Ignore whitespace
				while (isWhiteSpace()) {
					idx++;
				}
				// If dealing with a string
				if (canContinue() && (json.charAt(idx) == '"')) {
					String str = readStringOrName();
					if (str == null) {
						throw new JSONError(failedToReadString);
					}
					// Ignore whiteSpace
					while ((isWhiteSpace())) {
						idx++;
					}
					if (canContinue() && (json.charAt(idx++) == ':')) {
						while (isWhiteSpace()) {
							idx++;
						}
						if (canContinue() && (json.charAt(idx) == '"')) {
							String value = readStringOrName();
							if (value != null) {
								o.put(str, value);
							} else {
								throw new JSONError(failedToReadString);
							}
						} else if (isNumber()) {
							StringBuilder sb = new StringBuilder();
							int oneE = 0;
							do {
								if (isNumber()) {
									sb.append(json.charAt(idx));
								} else {
									if (((json.charAt(idx) == 'E') || (json.charAt(idx) == 'e')) && (oneE <= 1)) {
										sb.append("E");
										oneE++;
									} else {
										if (!((json.charAt(idx) == '+') && (json.charAt(idx - 1) == 'E'))) {
											throw new JSONError(err);
										}
									}
								}
							} while (canContinue());
							o.put(str, Double.parseDouble(sb.toString()));
						} else if (canContinue() && (json.charAt(idx) == '{')) {
							JSONObject obj = readObject(new JSONObject());
							o.put(str, obj);
						} else if (canContinue() && (json.charAt(idx) == '[')) {
							JSONArray array = readArray(new JSONArray());
							o.put(str, array);
						} else {
							Boolean value = readValidValue();
							o.put(str, value);
						}
					} else {
						throw new JSONError(err);
					}
					while (isWhiteSpace()) {
						idx++;
					}
					if (canContinue() && ((json.charAt(idx) == '}') || (json.charAt(idx) == ','))) {
						if (json.charAt(idx) == '}') {
							while (isWhiteSpace()) {
								idx++;
							}
							if (!canContinue()) {
								complete = true;
							} else {
								// throw new JSONError(err);
							}
						}
					}
				} else if (canContinue() && (json.charAt(idx) == ',')) {
					return o;
				} else if (canContinue() && (json.charAt(idx) == ']')) {
					return o;
				} else if (canContinue() && (json.charAt(idx) == '}')) {
					return o;
				} else {
					while (isWhiteSpace()) {
						idx++;
					}
					if (canContinue()) {
						throw new JSONError(err);
					}
				}
			}
		}
		return o;
	}

	public JSONArray readArray(JSONArray array) throws JSONError {
		boolean complete = false;
		while (isWhiteSpace()) {
			idx++;
		}
		if (canContinue() && (json.charAt(idx++) == '[')) {
			for (; (idx < (json.length())) && !complete; idx++) {
				// Ignore whitespace
				while (isWhiteSpace()) {
					idx++;
				}
				if (canContinue() && (json.charAt(idx) == '"')) {
					String value = readStringOrName();
					if (value != null) {
						array.add(value);
					} else {
						throw new JSONError(err);
					}
				} else if (isNumber()) {
					StringBuilder sb = new StringBuilder();
					int oneE = 0;
					do {
						if (isNumber()) {
							sb.append(json.charAt(idx));
						} else {
							if (((json.charAt(idx) == 'E') || (json.charAt(idx) == 'e')) && (oneE <= 1)) {
								sb.append("E");
								oneE++;
							} else {
								if (!((json.charAt(idx) == '+') && (json.charAt(idx - 1) == 'E'))) {
									throw new JSONError(err);
								}
							}
						}
					} while (canContinue());
					array.add(Double.parseDouble(sb.toString()));
				} else if (canContinue() && (json.charAt(idx) == '{')) {
					JSONObject obj = readObject(new JSONObject());
					array.add(obj);
				} else if (canContinue() && (json.charAt(idx) == '[')) {
					JSONArray arry = readArray(new JSONArray());
					array.add(arry);
				} else if (canContinue() && json.charAt(idx) == ']') {
					while (isWhiteSpace()) {
						idx++;
					}
					if (!canContinue()) {
						complete = true;
					} else {
						throw new JSONError(err);
					}
				} else if (canContinue() && (json.charAt(idx) == ',')) {

				} else {
					Boolean value = readValidValue();
					array.add(value);
				}
				if (!complete) {
					while (isWhiteSpace()) {
						idx++;
					}
					if (canContinue() && (json.charAt(idx) == ']')) {
						return array;
					}
					if (!(canContinue() && (json.charAt(idx) == ','))) {
						// throw new JSONError(err);
					}
				}
			}
		} else {
			throw new JSONError(err);
		}
		return array;
	}

	public Boolean readValidValue() throws JSONError {
		Boolean value = null;
		if (canContinue()) {
			if (json.charAt(idx++) == 't') {
				if (canContinue() && (json.charAt(idx++) == 'r')) {
					if (canContinue() && (json.charAt(idx++) == 'u')) {
						if (canContinue() && (json.charAt(idx++) == 'e')) {
							return true;
						}
					}
				}
				throw new JSONError(err);
			} else if (json.charAt(idx++) == 'f') {
				if (canContinue() && (json.charAt(idx++) == 'a')) {
					if (canContinue() && (json.charAt(idx++) == 'l')) {
						if (canContinue() && (json.charAt(idx++) == 's')) {
							if (canContinue() && (json.charAt(idx++) == 'e')) {
								return false;
							}
						}
					}
				}
				throw new JSONError(err);
			} else if (json.charAt(idx++) == 'n') {
				if (canContinue() && (json.charAt(idx++) == 'u')) {
					if (canContinue() && (json.charAt(idx++) == 'l')) {
						if (canContinue() && (json.charAt(idx++) == 'l')) {
							return null;
						}
					}
				}
			}
		}
		throw new JSONError(err);
	}

	String err = "Parsing terminated. Stream is not well-formed";
	String failedToReadString = "Erro parsing String value. Stream is not well formed";

	public boolean canContinue() {
		return (idx < json.length());
	}

	public boolean isNumber() {
		return (canContinue() && (((json.charAt(idx) - 48) >= 0) && ((json.charAt(idx) - 57) <= 0)));
	}

	public String readStringOrName() {
		StringBuilder sb = new StringBuilder();
		idx++;
		do {
			sb.append(json.charAt(idx++));
		} while (canContinue() && (json.charAt(idx) != '"'));
		if (++idx < json.length()) {
			// Adjust position if the next character of the string is '}'
			if (json.charAt(idx) == '}') {
				// idx--;
			}
			return sb.toString();
		} else {
			return null;
		}
	}

	public boolean isWhiteSpace() {
		return (json.length() > idx) && ((json.charAt(idx) == ' ') || (json.charAt(idx) == '\t') || (json.charAt(idx) == '\r')
				|| (json.charAt(idx) == ((char) 10)));
	}

	public boolean isObject() throws JSONError {
		if (isObject == null) {
			if (json.length() == 0) {
				throw new JSONError("Invalid JSON 0");
			}
			while (isWhiteSpace()) {
				idx++;
			}
			if (canContinue() && (json.charAt(idx) == '[')) {
				isObject = false;
				parsedObject = new JSONArray();
			} else if (canContinue() && (json.charAt(idx) == '{')) {
				isObject = true;
				parsedObject = new JSONObject();
			} else {
				throw new JSONError("Invalid JSON 1");
			}
			idx = 0;
		}
		return isObject;
	}

	public static class JSONError extends Exception {
		public String message = "";

		public JSONError(String message) {
			this.message = message;
		}

		public String getMessage() {
			return this.message;
		}
	}
}
