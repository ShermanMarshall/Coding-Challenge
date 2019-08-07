package coding.challenge;

import java.util.ArrayList;
import java.util.List;

public class Group {

	private String name = "";
	private int gid = Integer.MIN_VALUE;
	private List<String> members = new ArrayList();

	public Group() {
	}

	@Override
	public int hashCode() {
		return (name + gid).hashCode();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public String toString() {
		return String.join(":", name, Integer.toString(gid), members.toString());
	}

}
