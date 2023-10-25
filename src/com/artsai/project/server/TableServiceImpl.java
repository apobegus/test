package com.artsai.project.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.artsai.project.client.ServiceException;
import com.artsai.project.client.TableModel;
import com.artsai.project.client.TableService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TableServiceImpl extends RemoteServiceServlet implements TableService {

	private final static long serialVersionUID = 605L;

	@Override
	public TableModel[] tableServer(String event, String type) throws ServiceException {
		Connection connection = ServiceHelper.getConnection();

		try {
			Map<String, Map<String, Integer>> maps = ServiceHelper.getEventsMap(connection, getServletContext(), event);

			return getTableModel(connection, maps, type);
		} 
		catch (Exception e) {
			throw new ServiceException("Load diagram data error !", e.getMessage());
		} 
		finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) { }
		}

	}

	private TableModel[] getTableModel(Connection connection, Map<String, Map<String, Integer>> maps, String type) throws Exception {
		List<TableModel> list = new ArrayList<TableModel>();

		Map<String, Integer> clickMap = maps.get("click");
		Map<String, Integer> eventMap = maps.get("event");

		String path = ServiceHelper.getFilePath(getServletContext(), "/interview.X.csv");

		PreparedStatement statement = connection.prepareStatement("SELECT " + type + ", uid FROM \"" + path + "\"");
		ResultSet result = statement.executeQuery();

		Map<String, Integer> click_count = new HashMap<String, Integer>();
		Map<String, Integer> event_count = new HashMap<String, Integer>();

		SortedMap<String, Integer> imprs_count = new TreeMap<String, Integer>();

		while (result.next()) {
			String key = result.getString(1);
			String uid = result.getString(2);

			if (imprs_count.containsKey(key))
				imprs_count.put(key, Integer.valueOf(imprs_count.get(key).intValue() + 1));
			else
				imprs_count.put(key, Integer.valueOf(1));

			if (clickMap.containsKey(uid)) {
				if (click_count.containsKey(key)) {
					int val = click_count.get(key).intValue();

					click_count.put(key, Integer.valueOf(clickMap.get(uid).intValue() + val));
				} 
				else click_count.put(key, Integer.valueOf(clickMap.get(uid).intValue()));
			}

			if (eventMap.containsKey(uid)) {
				if (event_count.containsKey(key)) {
					int val = event_count.get(key).intValue();

					event_count.put(key, Integer.valueOf(eventMap.get(uid).intValue() + val));
				}
				else event_count.put(key, Integer.valueOf(eventMap.get(uid).intValue()));
			}
		}

		Iterator<String> it = imprs_count.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			int val = imprs_count.get(key).intValue();

			double ctr = 0.0;
			double evpm = 0.0;

			if (click_count.containsKey(key)) ctr = 100 * click_count.get(key).doubleValue() / val;
			if (event_count.containsKey(key)) evpm = 1000 * event_count.get(key).doubleValue() / val;

			String strCtr = String.format("%.2f", ctr).replace(",", ".");
			String strEvpm = String.format("%.2f", evpm).replace(",", ".");

			TableModel model = new TableModel();

			model.setName(key);
			model.setImpression(String.valueOf(val));
			model.setCtr(strCtr);
			model.setEvpm(strEvpm);

			list.add(model);
		}

		result.close();
		statement.close();

		return list.toArray(new TableModel[list.size()]);
	}

}
