package coding.challenge.utils;

import coding.challenge.User;

//User enum for /etc/password data
public enum UserPasswordInfo {
	NAME(Constants.NAME), PASSWORD(Constants.PASSWD), UID(Constants.UID), GID(Constants.GID),
	COMMENT(Constants.COMMENT), HOME(Constants.HOME), SHELL(Constants.SHELL);

	private String param;

	private UserPasswordInfo(String param) {
		this.param = param;
	}

	public String getValue() {
		return param;
	}

	public static UserPasswordInfo getParam(String value) {
		UserPasswordInfo param = null;
		switch (value) {
		case Constants.NAME:
			param = NAME;
			break;
		case Constants.UID:
			param = UID;
			break;
		case Constants.GID:
			param = GID;
			break;
		case Constants.COMMENT:
			param = COMMENT;
			break;
		case Constants.HOME:
			param = HOME;
			break;
		case Constants.SHELL:
			param = SHELL;
			break;
		}
		return param;
	}

	public boolean isEqual(User user, String paramValue) {
		String userValue = "";

		switch (this.param) {
		case Constants.NAME:
			userValue = user.getName();
			break;
		case Constants.UID:
			userValue = Integer.toString(user.getUid());
			break;
		case Constants.GID:
			userValue = Integer.toString(user.getGid());
			break;
		case Constants.COMMENT:
			userValue = user.getComment();
			break;
		case Constants.HOME:
			userValue = user.getHome();
			break;
		case Constants.SHELL:
			userValue = user.getShell();
			break;
		}

		return userValue.equals(paramValue);
	}

	public void setValue(User user, String value) {
		switch (this.param) {
		case Constants.NAME:
			user.setName(value);
			break;
		case Constants.UID:
			user.setUid(Integer.parseInt(value));
			break;
		case Constants.GID:
			user.setGid(Integer.parseInt(value));
			break;
		case Constants.COMMENT:
			user.setComment(value);
			break;
		case Constants.HOME:
			user.setHome(value);
			break;
		case Constants.SHELL:
			user.setShell(value);
			break;
		}
	}
}
