package com.shermanmarshall.json;

import java.util.ArrayList;

public class JSONArray {
    ArrayList<Object> set = new ArrayList();
    public void add(Object o) {
        set.add(o);
    }
    public String toJson() {
        return "";
    }
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Object o : set) {
            sb.append(o.toString()).append(",");
        }
        return new StringBuilder(sb.substring(0, sb.length()-2).toString()).append("]").toString();
    }
}
