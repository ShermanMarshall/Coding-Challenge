import java.math.BigDecimal;

public class JsonParser {
    Integer objectCount = 0;
    Boolean isObject = null, isValid = null;
    String json;
    Object parsedObject = null;
    int x = 0;
    
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
        while (isWhiteSpace()) { x++; }
        if (canContinue() && (json.charAt(x++) == '{')) {
            for (; (x < (json.length())) && !complete && (json.charAt(x) != '}'); x++) {
                //Ignore whitespace
                while (isWhiteSpace()) { x++; }
                //If dealing with a string
                if (canContinue() && (json.charAt(x) == '"')) {
                    String str = readStringOrName();
                    if (str == null) {
                        throw new JSONError(failedToReadString);
                    }
                    //Ignore whiteSpace
                    while((isWhiteSpace())) { x++; }
                    if (canContinue() && (json.charAt(x++) == ':')) {
                        while(isWhiteSpace()) { x++; } 
                        if (canContinue() && (json.charAt(x) == '"')) {
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
                                    sb.append(json.charAt(x));
                                } else {
                                    if (((json.charAt(x) == 'E') || (json.charAt(x) == 'e')) && (oneE <= 1)) {
                                        sb.append("E");
                                        oneE++;
                                    } else {
                                        if (!((json.charAt(x) == '+') && (json.charAt(x-1) == 'E'))) {
                                            throw new JSONError(err);
                                        }
                                    }
                                }
                            } while (canContinue());
                            o.put(str, Double.parseDouble(sb.toString()));
                        } else if (canContinue() && (json.charAt(x) == '{')) {
                            JSONObject obj = readObject(new JSONObject());
                            o.put(str, obj);
                        } else if (canContinue() && (json.charAt(x) == '[')) {
                            JSONArray array = readArray(new JSONArray());
                            o.put(str, array);
                        } else {
                            Boolean value = readValidValue();
                            o.put(str, value);
                        }
                    } else {
                        throw new JSONError(err);
                    }
                    while(isWhiteSpace()) { x++; }
                    if (canContinue() && ((json.charAt(x) == '}') || (json.charAt(x) == ','))) {
                        if (json.charAt(x) == '}') {
                            while (isWhiteSpace()) { x++; }
                            if (!canContinue()) {
                                complete = true;
                            } else {
                                //throw new JSONError(err);
                            }
                        }
                    }
                } else if (canContinue() && (json.charAt(x) == ',')) {
                    return o;
                } else if (canContinue() && (json.charAt(x) == ']')) {
                    return o;
                } else if (canContinue() && (json.charAt(x) == '}')) {
                    return o;
                } else {
                    while (isWhiteSpace()) { x++;}
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
        while (isWhiteSpace()) { x++; }
        if (canContinue() && (json.charAt(x++) == '[')) {
            for (; (x < (json.length())) && !complete; x++) {
                //Ignore whitespace
                while (isWhiteSpace()) { x++; }
                if (canContinue() && (json.charAt(x) == '"')) {
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
                            sb.append(json.charAt(x));
                        } else {
                            if (((json.charAt(x) == 'E') || (json.charAt(x) == 'e')) && (oneE <= 1)) {
                                sb.append("E");
                                oneE++;
                            } else {
                                if (!((json.charAt(x) == '+') && (json.charAt(x-1) == 'E'))) {
                                    throw new JSONError(err);
                                }
                            }
                        }
                    } while (canContinue());
                    array.add(Double.parseDouble(sb.toString()));
                } else if (canContinue() && (json.charAt(x) == '{')) {
                    JSONObject obj = readObject(new JSONObject());
                    array.add(obj);
                } else if (canContinue() && (json.charAt(x) == '[')) {
                    JSONArray arry = readArray(new JSONArray());
                    array.add(arry);
                } else if (canContinue() && json.charAt(x) == ']') {
                    while (isWhiteSpace()) { x++; }
                    if(!canContinue()) {
                        complete = true;
                    } else {
                        throw new JSONError(err);
                    }
                } else if (canContinue() && (json.charAt(x) == ',')) {
                    
                }
                else {
                    Boolean value = readValidValue();
                    array.add(value);
                }
                if (!complete) {
                    while(isWhiteSpace()) { x++; }
                    if (canContinue() && (json.charAt(x) == ']')) {
                        return array;
                    }
                    if (!(canContinue() && (json.charAt(x) == ','))) {
                        //throw new JSONError(err);
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
            if (json.charAt(x++) == 't') {
                if (canContinue() && (json.charAt(x++) == 'r')) {
                    if (canContinue() && (json.charAt(x++) == 'u')) {
                        if (canContinue() && (json.charAt(x++) == 'e')) {
                            return true;
                        }
                    }
                }
                throw new JSONError(err);
            } else if (json.charAt(x++) == 'f') {
                if (canContinue() && (json.charAt(x++) == 'a')) {
                    if (canContinue() && (json.charAt(x++) == 'l')) {
                        if (canContinue() && (json.charAt(x++) == 's')) {
                            if (canContinue() && (json.charAt(x++) == 'e')) {
                                return false;
                            }
                        }
                    }
                }
                throw new JSONError(err);
            } else if (json.charAt(x++) == 'n') {
                if (canContinue() && (json.charAt(x++) == 'u')) {
                    if (canContinue() && (json.charAt(x++) == 'l')) {
                        if (canContinue() && (json.charAt(x++) == 'l')) {
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
        return ( x < json.length());
    }
    
    public boolean isNumber() {
        return (canContinue() && (((json.charAt(x) - 48) >=  0) && ((json.charAt(x) - 57) <= 0)));
    }
    
    public String readStringOrName() {
        StringBuilder sb = new StringBuilder();
        x++;
        do {
           sb.append(json.charAt(x++));
        } while (canContinue() && (json.charAt(x) != '"'));
        if (++x < json.length()) {
            //Adjust position if the next character of the string is '}'
            if (json.charAt(x) == '}') {
                //x--;
            }
            return sb.toString();
        } else {
            return null;
        }
    }
    
    public boolean isWhiteSpace() {
        return (json.length() > x) && ((json.charAt(x) == ' ' ) || (json.charAt(x) == '\t') ||
                (json.charAt(x) == '\r') || (json.charAt(x) == ((char) 10)));
    }
    
    public boolean isObject() throws JSONError {
        if (isObject == null) {
            if (json.length() == 0) {
                throw new JSONError("Invalid JSON 0");
            } 
            while(isWhiteSpace()) { x++; }
            if (canContinue() && (json.charAt(x) == '[')) {
                isObject = false;
                parsedObject = new JSONArray();
            } else if (canContinue() && (json.charAt(x) == '{')) {
                isObject = true;
                parsedObject = new JSONObject();
            } else {
                throw new JSONError("Invalid JSON 1");
            }
            x = 0;
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
