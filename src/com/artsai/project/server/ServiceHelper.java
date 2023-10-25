package com.artsai.project.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import com.artsai.project.client.ServiceException;

public class ServiceHelper {

	/*
	 * static { try { DriverManager.registerDriver(new CsvDriver()); } catch
	 * (SQLException e) { e.printStackTrace(); } }
	 */

	public static Connection getConnection() throws ServiceException {
		try {
			Class.forName("org.relique.jdbc.csv.CsvDriver");
			Connection connection = DriverManager.getConnection("jdbc:relique:csv:");

			return connection;
		}
		catch (Exception e) {
			throw new ServiceException("CSV Connection Error !", e.getMessage());
		}
	}

	public static String getFilePath(ServletContext context, String file) {
		return context.getRealPath(file).replaceFirst(".csv", "");
	}

	public static Map<String, Map<String, Integer>> getEventsMap(Connection connection, ServletContext context, String event) throws Exception {
		Map<String, Map<String, Integer>> map = new HashMap<String, Map<String, Integer>>();

		Map<String, Integer> clickMap = new HashMap<String, Integer>();
		Map<String, Integer> eventMap = new HashMap<String, Integer>();

		String path = ServiceHelper.getFilePath(context, "/interview.y.csv");

		PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"" + path + "\"");
		ResultSet result = statement.executeQuery();

		while (result.next()) {
			String uid = result.getString(1);
			String tag = result.getString(2);

			if (tag != null) {
				if (tag.equals(event)) {
					if (clickMap.containsKey(uid))
						clickMap.put(uid, Integer.valueOf(clickMap.get(uid).intValue() + 1));
					else
						clickMap.put(uid, Integer.valueOf(1));
				}

				if (tag.equals("vregistration") || tag.equals("registration")) {
					if (clickMap.containsKey(uid)) {
						if (eventMap.containsKey(uid))
							eventMap.put(uid, Integer.valueOf(eventMap.get(uid).intValue() + 1));
						else
							eventMap.put(uid, Integer.valueOf(1));
					}
				}
			}
		}

		result.close();
		statement.close();

		map.put("click", clickMap);
		map.put("event", eventMap);

		return map;
	}

}
