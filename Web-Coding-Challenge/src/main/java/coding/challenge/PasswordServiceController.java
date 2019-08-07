package coding.challenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import coding.challenge.utils.Constants;
import coding.challenge.utils.GroupPasswordInfo;
import coding.challenge.utils.UserPasswordInfo;

@RestController
public class PasswordServiceController {

	@RequestMapping(value = Constants.USERS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> users() {
		return PasswordFileReader.getUsers();
	}

	@RequestMapping(value = Constants.USERS_BY_ID, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public User name(@PathVariable(Constants.UID) String uid) {
		try {
			int uidInt = Integer.parseInt(uid);
			List<User> users = PasswordFileReader.getUsers();
			for (User user : users) {
				if (user.getUid() == uidInt) {
					return user;
				}
			}
		} catch (NumberFormatException nfe) {
		}
		throw new Constants.ContentNotFoundException();
	}

	@RequestMapping(value = Constants.USERS_BY_QUERY, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> usersQuery(@RequestParam Map<String, String> queryParams) {
		List<User> users = PasswordFileReader.getUsers();
		List<User> returnList = new ArrayList(users.size() >> 1);

		for (User user : users) {
			Set<String> keys = queryParams.keySet();

			boolean isAdded = true;
			for (String key : keys) {
				UserPasswordInfo param = UserPasswordInfo.getParam(key);
				String value = queryParams.get(key);

				if (!param.isEqual(user, value)) {
					isAdded = !isAdded;
					break;
				}
			}

			if (isAdded) {
				returnList.add(user);
			}
		}
		return returnList;
	}

	@RequestMapping(value = Constants.GROUPS_BY_USER_ID, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Group> groupsByUserId(@PathVariable(Constants.UID) String uid) {
		List<User> users = PasswordFileReader.getUsers();
		List<Group> groupsIncludingMember = new ArrayList();
		String memberName = null;

		try {
			int uidInt = Integer.parseInt(uid);
			for (User user : users) {
				if (user.getUid() == uidInt) {
					memberName = user.getName();
					break;
				}
			}
		} catch (NumberFormatException nfe) {
		}

		if (memberName != null) {
			List<Group> groups = PasswordFileReader.getGroups();
			for (Group group : groups) {
				List<String> members = group.getMembers();
				for (String member : members) {
					if (memberName.equals(member)) {
						groupsIncludingMember.add(group);
						break;
					}
				}
			}
		}
		return groupsIncludingMember;
	}

	@RequestMapping(value = Constants.GROUPS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Group> groups() {
		return PasswordFileReader.getGroups();
	}

	@RequestMapping(value = Constants.GROUPS_BY_QUERY, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Group> groupsQuery(HttpServletRequest request) {
		List<Group> matchingGroups = new ArrayList();
		String queryString = request.getQueryString();

		if (queryString.contains("=")) {
			String[] queryParams = queryString.split("&");

			Map<String, String> paramMap = new HashMap();
			List<String> membersInQueryString = new ArrayList();

			for (String queryParam : queryParams) {
				String[] qp = queryParam.split("=");

				if (qp[0].equals(Constants.MEMBER)) {
					membersInQueryString.add(qp[1]);
					paramMap.put(Constants.MEMBER, Constants.MEMBER);
				} else {
					paramMap.put(qp[0], qp[1]);
				}
			}

			List<Group> groups = PasswordFileReader.getGroups();
			for (Group group : groups) {
				boolean isAdded = true;

				for (String key : paramMap.keySet()) {
					GroupPasswordInfo param = GroupPasswordInfo.getParam(key);
					if (!key.equals(Constants.MEMBER)) {
						String value = paramMap.get(key);

						if (!param.isEqual(group, value)) {
							isAdded = !isAdded;
							break;
						}
					} else {
						List<String> members = group.getMembers();
						boolean hasAllMembers = true;
						for (String member : membersInQueryString) {
							if (!members.contains(member)) {
								hasAllMembers = false;
								break;
							}
						}
						isAdded &= hasAllMembers;
					}
				}

				if (isAdded) {
					matchingGroups.add(group);
				}
			}
		}
		return matchingGroups;
	}

	@RequestMapping(value = Constants.GROUPS_BY_ID, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Group groupByGid(@PathVariable(Constants.GID) String gid) {
		List<Group> groups = PasswordFileReader.getGroups();
		try {
			int gidInt = Integer.parseInt(gid);
			for (Group group : groups) {
				if (group.getGid() == gidInt) {
					return group;
				}
			}
		} catch (NumberFormatException nfe) {

		}
		throw new Constants.ContentNotFoundException();
	}

}
