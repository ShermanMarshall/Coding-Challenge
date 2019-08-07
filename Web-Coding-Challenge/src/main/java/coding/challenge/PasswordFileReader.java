package coding.challenge;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import coding.challenge.utils.Constants;
import coding.challenge.utils.GroupPasswordInfo;
import coding.challenge.utils.UserPasswordInfo;

public class PasswordFileReader {

	public static boolean canAccessContent() {
		File password = new File(Constants.ETC_PASSWD);
		File groups = new File(Constants.ETC_GROUP);

		return password.canRead() && groups.canRead();
	}

	public static String readFile(String filename) {
		final int LIMIT = 4096;
		ByteBuffer buffer = ByteBuffer.allocate(LIMIT);
		StringBuilder sb = new StringBuilder();

		try (FileChannel channel = new FileInputStream(new File(filename)).getChannel()) {
			byte[] data;
			int bytesRead = -1;

			while ((bytesRead = channel.read(buffer)) != -1) {
				buffer.rewind();
				if (bytesRead < LIMIT) {
					data = new byte[bytesRead];
					buffer.get(data);
				} else {
					data = buffer.array();
				}
				sb.append(new String(data));
			}
		} catch (IOException ioe) {
			throw new Constants.ContentUnavailableException();
		}
		String content = sb.toString();

		return sb.toString();
	}

	public static List<User> getUsers() {
		String content = readFile(Constants.ETC_PASSWD);
		String[] records = content.split("\n");
		UserPasswordInfo[] set = UserPasswordInfo.values();
		List<User> users = new ArrayList();

		int counter = 0;
		for (String row : records) {
			String[] record = row.split(":");
			// If user record doesn't contain necessary parameters, skip
			if (record.length < 7) {
				continue;
			}

			User user = new User();
			for (String component : record) {
				UserPasswordInfo param = set[counter++];
				param.setValue(user, component.trim());
			}

			counter = 0;
			users.add(user);
		}

		return users;
	}

	public static List<Group> getGroups() {
		String content = readFile(Constants.ETC_GROUP);

		GroupPasswordInfo[] set = GroupPasswordInfo.values();
		List<Group> groups = new ArrayList();

		int counter = 0;
		String[] records = content.split("\n");
		for (String row : records) {
			String[] record = row.split(":");
			// Exclude commented, or ill-formed rows
			if (record.length < 4 && (row.charAt(row.length() - 1) != ':')) {
				continue;
			}

			Group group = new Group();
			for (String component : record) {
				GroupPasswordInfo param = set[counter++];
				param.setValue(group, component.trim());
			}

			counter = 0;
			groups.add(group);
		}

		return groups;
	}

}
