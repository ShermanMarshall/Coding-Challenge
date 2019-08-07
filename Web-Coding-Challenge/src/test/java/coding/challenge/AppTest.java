package coding.challenge;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import coding.challenge.utils.Constants;
import coding.challenge.utils.GroupPasswordInfo;
import coding.challenge.utils.HTTPUtils;

/**
 * Unit test for Coding-Challenge
 */
public class AppTest {

	@Before
	public void setup() {
	}

	public static void saveOutput(String testName, String json) {
		try (PrintWriter pw = new PrintWriter("src/test/resources/" + testName + ".json")) {
			pw.write(json);
			pw.flush();
		} catch (IOException ioe) {
			System.out.println(ioe);
			System.out.println(testName + " output could not be saved");
		}
	}

	@Test
	public void testGetUsers() {
		try {
			LinkedHashMap[] users = (LinkedHashMap[]) HTTPUtils.GET("http://localhost:8080/users",
					LinkedHashMap[].class);
			Assert.assertTrue("Users is not null and is size > 0", users != null && users.length > 0);

			saveOutput("testGetUsers", HTTPUtils.om.writeValueAsString(users));
		} catch (JsonParseException | JsonMappingException jsonException) {
			Assert.fail(jsonException.getMessage());
		} catch (IOException ioe) {
			Assert.fail(ioe.getMessage());
		}
	}

	@Test
	public void testGetUsersByQuery() {
		try {
			LinkedHashMap[] users = (LinkedHashMap[]) HTTPUtils
					.GET("http://localhost:8080/users/query?shell=%2Fusr%2Fbin%2Ffalse", LinkedHashMap[].class);
			Assert.assertTrue("Users is not null and is size > 0", users != null && users.length > 0);
			for (LinkedHashMap map : users) {
				Assert.assertTrue(map.get("shell").equals("/usr/bin/false"));
			}

			saveOutput("testGetUsersByQuery", HTTPUtils.om.writeValueAsString(users));
		} catch (JsonParseException | JsonMappingException jsonException) {
			Assert.fail(jsonException.getMessage());
		} catch (IOException ioe) {
			Assert.fail(ioe.getMessage());
		}
	}

	@Test
	public void testFailGetUsersByQuery() {
		try {
			LinkedHashMap[] users = (LinkedHashMap[]) HTTPUtils
					.GET("http://localhost:8080/users/query?shell=%2Fusr%2Fbin%2Ffalse", LinkedHashMap[].class);
			Assert.assertTrue("Users is not null and is size > 0", users != null && users.length > 0);

			for (LinkedHashMap map : users) {
				Assert.assertFalse(map.get("shell").equals("/http/www/false"));
			}
			saveOutput("testFailGetUsersByQuery", HTTPUtils.om.writeValueAsString(users));
		} catch (JsonParseException | JsonMappingException jsonException) {
			Assert.fail(jsonException.getMessage());
		} catch (IOException ioe) {
			Assert.fail(ioe.getMessage());
		}
	}

	@Test
	public void testGetUsersByUid() {
		final String uid = "1";
		try {
			User user = (User) HTTPUtils.GET("http://localhost:8080/users/" + uid, User.class);
			Assert.assertTrue("User is not null", user != null);

			Assert.assertEquals("Verify uid supplied is equal", user.getUid(), Integer.parseInt(uid));

			saveOutput("testGetUsersByUid", HTTPUtils.om.writeValueAsString(user));
		} catch (JsonParseException | JsonMappingException jsonException) {
			Assert.fail(jsonException.getMessage());
		} catch (IOException ioe) {
			Assert.fail(ioe.getMessage());
		}
	}

	@Test
	public void testFailGetUsersByUid() {
		final String uid = "1111111111";
		int responseCode = HTTPUtils.GET404("http://localhost:8080/users/" + uid);
		Assert.assertEquals("Response returned is: " + responseCode, responseCode, 404);
	}

	@Test
	public void testGetGroupsByUid() {
		final String uid = "1";
		try {
			Group[] groups = (Group[]) HTTPUtils.GET("http://localhost:8080/users/" + uid + "/groups", Group[].class);
			Assert.assertTrue("User is not null", groups != null);
			saveOutput("testGetGroupsByGid", HTTPUtils.om.writeValueAsString(groups));

			User user = (User) HTTPUtils.GET("http://localhost:8080/users/" + uid, User.class);

			if (user != null) {
				boolean hasMatchInAllGroups = true;
				for (Group group : groups) {
					List<String> members = group.getMembers();
					boolean isPresent = false;
					for (String s : members) {
						if (s.equals(user.getName())) {
							isPresent = true;
						}
					}
					hasMatchInAllGroups &= isPresent;
				}
				Assert.assertTrue("Found a matching member in every group", hasMatchInAllGroups);
			} else {
				Assert.fail("Failed to retrieve the user with uid: " + uid);
			}
		} catch (JsonParseException | JsonMappingException jsonException) {
			Assert.fail(jsonException.getMessage());
		} catch (IOException ioe) {
			Assert.fail(ioe.getMessage());
		}
	}

	@Test
	public void testGetGroups() {
		try {
			Group[] groups = (Group[]) HTTPUtils.GET("http://localhost:8080/groups", Group[].class);
			Assert.assertTrue("Groups is not null", groups != null);

			for (Group group : groups) {
				Assert.assertTrue("Group name initialized from file. Name: " + group.getName(),
						!group.getName().equals(""));
				Assert.assertTrue("Group gid initialized from file. Gid: " + group.getGid(),
						group.getGid() != Integer.MIN_VALUE);
				Assert.assertTrue("Group members initialized with instance.", group.getMembers() != null);
			}

			saveOutput("testGetGroups", HTTPUtils.om.writeValueAsString(groups));
		} catch (JsonParseException | JsonMappingException jsonException) {
			Assert.fail(jsonException.getMessage());
		} catch (IOException ioe) {
			Assert.fail(ioe.getMessage());
		}
	}

	@Test
	public void testGetGroupsByQuery() {
		String paramKey = "name";
		String paramValue = "wheel";
		try {
			Group[] groups = (Group[]) HTTPUtils
					.GET("http://localhost:8080/groups/query?" + paramKey + "=" + paramValue, Group[].class);
			Assert.assertTrue("Groups is not null", groups != null);

			boolean hasGroupWithParam = false;
			GroupPasswordInfo param = GroupPasswordInfo.getParam(paramKey);
			for (Group group : groups) {
				if (param.isEqual(group, paramValue)) {
					hasGroupWithParam = !hasGroupWithParam;
					break;
				}
			}
			Assert.assertTrue("All groups returned has matching " + paramKey + " " + paramValue, hasGroupWithParam);
			saveOutput("testGetGroupsByQuery", HTTPUtils.om.writeValueAsString(groups));
		} catch (JsonParseException | JsonMappingException jsonException) {
			Assert.fail(jsonException.getMessage());
		} catch (IOException ioe) {
			Assert.fail(ioe.getMessage());
		}
	}

	@Test
	public void testGetGroupsByQueryMultipleMembers() {
		String paramKey = "member";
		String paramValue = "root";
		String paramValue2 = "_jabber";

		try {
			Group[] groups = (Group[]) HTTPUtils.GET("http://localhost:8080/groups/query?" + paramKey + "=" + paramValue
					+ "&" + paramKey + "=" + paramValue2, Group[].class);

			Assert.assertTrue("Groups is not null", groups != null);

			boolean hasGroupWithParam = false;
			GroupPasswordInfo param = GroupPasswordInfo.getParam(paramKey);
			for (Group group : groups) {
				if (param.isEqual(group, paramValue)) {
					hasGroupWithParam = !hasGroupWithParam;
					break;
				}
			}
			Assert.assertTrue("All groups returned has matching " + paramKey + " " + paramValue + " & " + paramValue2,
					hasGroupWithParam);
			saveOutput("testGetGroupsByQueryMultipleMembers", HTTPUtils.om.writeValueAsString(groups));
		} catch (JsonParseException | JsonMappingException jsonException) {
			Assert.fail(jsonException.getMessage());
		} catch (IOException ioe) {
			Assert.fail(ioe.getMessage());
		}
	}

	@Test
	public void testGetGroupsByGid() {
		String gid = "1";
		try {
			Group group = (Group) HTTPUtils.GET("http://localhost:8080/groups/" + gid, Group.class);
			Assert.assertTrue("Retrieved group with matching gid: " + gid, group != null);
			saveOutput("testGetGroupsByGid", HTTPUtils.om.writeValueAsString(group));
		} catch (JsonParseException | JsonMappingException jsonException) {
			Assert.fail(jsonException.getMessage());
		} catch (IOException ioe) {
			Assert.fail(ioe.getMessage());
		}
	}

	@Test
	public void testFailGroupsByGid() {
		String gid = "1111111";
		int responseCode = HTTPUtils.GET404("http://localhost:8080/groups/" + gid);
		Assert.assertTrue("Response code retrieved: " + responseCode, responseCode == 404);
	}

	@Test
	public void testInvalidFilePath() throws IOException {
		File file = new File(Constants.ETC_PASSWD);
		file.delete();
		int responseCode = HTTPUtils.GET404("http://localhost:8080/users");
		Assert.assertTrue("Expecting a ContentUnavailableException here causing a 404", responseCode == 404);
	}
}
