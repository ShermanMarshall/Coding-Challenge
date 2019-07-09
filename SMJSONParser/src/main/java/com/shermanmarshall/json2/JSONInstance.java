package com.shermanmarshall.json2;

public class JSONInstance {
	protected byte[] src;
	protected int idx;
	
	protected JSONInstance() {}
	
	public void isWhiteSpace(JSONInstance instance) {
		byte input = instance.src[instance.idx];
		while (input == ' ' || input == '\t' || input == '\n' || input == '\r') {
			input = instance.src[++instance.idx];
		}
	}
	
	public boolean isInbounds() {
		return idx < src.length;
	}
	
	public byte at() {
		return src[idx];
	}
	
	public byte at(int i) {
		return src[i];
	}
	
	public byte atpp() {
		return src[idx++];
	}

}
