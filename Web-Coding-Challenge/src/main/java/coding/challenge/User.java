package coding.challenge;

public class User {
	private String name = "";
	private int uid = Integer.MIN_VALUE;
	private int gid = Integer.MIN_VALUE;
	private String comment = "";
	private String home = "";
	private String shell = "";

	public User() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getShell() {
		return shell;
	}

	public void setShell(String shell) {
		this.shell = shell;
	}

	public String toString() {
		return String.join(":", name, Integer.toString(uid), Integer.toString(gid), comment, home, shell);
	}

}
