package coding.challenge.utils;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import coding.challenge.Group;
import coding.challenge.User;

public class Constants {
	// Request Mappings
	public static final String USERS = "/users";
	public static final String USERS_BY_ID = "/users/{uid}";
	public static final String USERS_BY_QUERY = "/users/query";
	public static final String GROUPS_BY_USER_ID = "/users/{uid}/groups";
	public static final String GROUPS = "/groups";
	public static final String GROUPS_BY_ID = "/groups/{gid}";
	public static final String GROUPS_BY_QUERY = "/groups/query";

	// Application constants (associated with /etc/password & /etc/group)
	public static final String ETC_PASSWD = System.getenv("ETC_PASSWD");
	public static final String ETC_GROUP = System.getenv("ETC_GROUP");
	public static final String NAME = "name";
	public static final String PASSWD = "passwd";
	public static final String UID = "uid";
	public static final String GID = "gid";
	public static final String COMMENT = "comment";
	public static final String HOME = "home";
	public static final String SHELL = "shell";
	public static final String MEMBER = "member";

	// Errors for when content is unavailable or mising
	@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Content unavailable. Could not read both necessary /etc files")
	public static class ContentUnavailableException extends RuntimeException {
	}

	@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Content requested was not found")
	public static class ContentNotFoundException extends RuntimeException {
	}

}
