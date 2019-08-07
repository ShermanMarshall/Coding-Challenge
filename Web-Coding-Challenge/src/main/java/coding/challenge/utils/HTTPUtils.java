package coding.challenge.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HTTPUtils {
	public static final String GET_REQUEST = "GET";

	public static final ObjectMapper om = new ObjectMapper();
	private static final int BUFFER_SIZE = 4096;

	public static InputStream GET(String url) {
		HttpURLConnection hurlc = null;
		int responseCode = -1;
		InputStream is = null;

		try {
			hurlc = (HttpURLConnection) new URL(url).openConnection();
			hurlc.setRequestMethod(GET_REQUEST);

			responseCode = hurlc.getResponseCode();
			is = hurlc.getInputStream();
			hurlc.disconnect();
		} catch (IOException ioe) {
			System.out.println(ioe);
		}

		boolean isSuccessful = (hurlc != null) && (responseCode == 200) && (is != null);
		return isSuccessful ? is : null;
	}

	public static Object GET(String url, Class<?> returnClazz)
			throws JsonParseException, JsonMappingException, IOException {
		HttpURLConnection hurlc = null;
		int responseCode = -1;
		InputStream is = null;

		Object responseEntity = null;
		try {
			hurlc = (HttpURLConnection) new URL(url).openConnection();
			hurlc.setRequestMethod(GET_REQUEST);

			responseCode = hurlc.getResponseCode();
			is = hurlc.getInputStream();

			boolean isSuccessful = (hurlc != null) && (responseCode == 200) && (is != null);

			responseEntity = isSuccessful ? parseContent(is, returnClazz) : null;

			hurlc.disconnect();
		} catch (IOException ioe) {
			System.out.println(ioe);
		}

		return responseEntity;
	}
	
	public static int GET404(String url) {
		HttpURLConnection hurlc = null;
		int responseCode = -1;
		InputStream is = null;

		try {
			hurlc = (HttpURLConnection) new URL(url).openConnection();
			hurlc.setRequestMethod(GET_REQUEST);

			responseCode = hurlc.getResponseCode();
			is = hurlc.getInputStream();

			hurlc.disconnect();
		} catch (IOException ioe) {
			System.out.println(ioe);
		}

		return responseCode;
	}

	public static String readInputStream(InputStream is) {
		String content = null;
		try {
			int bytesRead;
			byte[] data = new byte[BUFFER_SIZE], bytes;
			StringBuilder sb = new StringBuilder();
			while ((bytesRead = is.read(data)) != -1) {
				if (bytesRead != data.length) {
					byte[] subData = new byte[bytesRead];
					System.arraycopy(data, 0, subData, 0, bytesRead);
					bytes = subData;
				} else {
					bytes = data;
				}
				sb.append(new String(bytes));
			}
			is.close();
			content = sb.toString();
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
		return content;
	}

	public static final Object parseContent(InputStream is, Class<?> returnClass)
			throws JsonParseException, JsonMappingException, IOException {
		BufferedInputStream bis = new BufferedInputStream(is);

		byte[] data = new byte[BUFFER_SIZE];

		int size = 0;
		StringBuilder sb = new StringBuilder();
		do {
			size = bis.read(data);
			if (size == data.length) {
				sb.append(new String(data));
			} else {
				if (size > 0) {
					byte[] subData = new byte[size];
					System.arraycopy(data, 0, subData, 0, size);
					sb.append(new String(subData));
				}
			}
		} while (size > 0);

		return returnClass != null ? om.readValue(sb.toString(), returnClass) : sb.toString();
	}
}
