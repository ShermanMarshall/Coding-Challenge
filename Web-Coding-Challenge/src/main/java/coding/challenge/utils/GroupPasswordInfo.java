package coding.challenge.utils;

import java.util.List;

import coding.challenge.Group;

//Group data associated with /etc/group
public enum GroupPasswordInfo {
	GROUP_NAME(Constants.NAME), PASSWORD(Constants.PASSWD), GID(Constants.GID), MEMBER_STRING(Constants.MEMBER);

	private String param;

	private GroupPasswordInfo(String param) {
		this.param = param;
	}

	public String getValue() {
		return param;
	}

	public static GroupPasswordInfo getParam(String value) {
		GroupPasswordInfo param = null;
		switch (value) {
		case Constants.NAME:
			param = GROUP_NAME;
			break;
		case Constants.GID:
			param = GID;
			break;
		case Constants.MEMBER:
			param = MEMBER_STRING;
			break;
		}
		return param;
	}

	public boolean isEqual(Group group, String paramValue) {
		boolean isMemberCheck = false, isEqual = false;
		String groupValue = "";
		switch (this.param) {
		case Constants.NAME:
			groupValue = group.getName();
			break;
		case Constants.GID:
			groupValue = Integer.toString(group.getGid());
			break;
		case Constants.MEMBER:
			isMemberCheck = true;
			List<String> members = group.getMembers();
			for (String member : members) {
				if (member.equals(paramValue)) {
					isEqual = true;
					break;
				}
			}
			break;
		}

		return isMemberCheck ? isEqual : groupValue.equals(paramValue);
	}

	public void setValue(Group group, String value) {
		switch (this.param) {
		case Constants.NAME:
			group.setName(value);
			break;
		case Constants.GID:
			group.setGid(Integer.parseInt(value));
			break;
		case Constants.MEMBER:
			List<String> members = group.getMembers();
			String[] set = value.split(",");
			for (String s : set) {
				members.add(s.trim());
			}
			group.setMembers(members);
			break;
		}
	}
}