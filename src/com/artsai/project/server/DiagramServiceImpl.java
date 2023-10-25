package com.artsai.project.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.artsai.project.client.DiagramModel;
import com.artsai.project.client.DiagramService;
import com.artsai.project.client.ServiceException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DiagramServiceImpl extends RemoteServiceServlet implements DiagramService {

	private final static long serialVersionUID = 30L;

	@Override
	public DiagramModel diagramServer(String input) throws ServiceException {
		Connection connection = ServiceHelper.getConnection();

		try {
			Map<String, Map<String, Integer>> maps = ServiceHelper.getEventsMap(connection,getServletContext(), input);

			return getDiagramModel(connection, maps);
		}
		catch (Exception e) {
			throw new ServiceException("Load diagram data error !", e.getMessage());
		}
		finally {
			try {
				if (connection != null) connection.close();
			}
			catch (Exception e) { }
		}
	}

	private DiagramModel getDiagramModel(Connection connection, Map<String, Map<String, Integer>> maps) throws Exception {
		DiagramModel model = new DiagramModel();

		Map<String, Integer> clickMap = maps.get("click");
		Map<String, Integer> eventMap = maps.get("event");

		String path = ServiceHelper.getFilePath(getServletContext(), "/interview.X.csv");

		PreparedStatement statement = connection.prepareStatement("SELECT reg_time, uid FROM \"" + path + "\"");
		ResultSet result = statement.executeQuery();

		Map<String, Integer> click_count = new HashMap<String, Integer>();
		Map<String, Integer> event_count = new HashMap<String, Integer>();
		SortedMap<String, Integer> imprs_count = new TreeMap<String, Integer>();

		while (result.next()) {
			String reg = result.getString(1).substring(0, 10);
			String uid = result.getString(2);

			if (imprs_count.containsKey(reg))
				imprs_count.put(reg, Integer.valueOf(imprs_count.get(reg).intValue() + 1));
			else
				imprs_count.put(reg, Integer.valueOf(1));

			if (clickMap.containsKey(uid)) {
				if (click_count.containsKey(reg)) {
					int val = click_count.get(reg).intValue();

					click_count.put(reg, Integer.valueOf(clickMap.get(uid).intValue() + val));
				} 
				else click_count.put(reg, Integer.valueOf(clickMap.get(uid).intValue()));
			}

			if (eventMap.containsKey(uid)) {
				if (event_count.containsKey(reg)) {
					int val = event_count.get(reg).intValue();

					event_count.put(reg, Integer.valueOf(eventMap.get(uid).intValue() + val));
				}
				else event_count.put(reg, Integer.valueOf(eventMap.get(uid).intValue()));
			}
		}

		Iterator<String> it = imprs_count.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			int val = imprs_count.get(key).intValue();

			double ctr = 0.0;
			double evpm = 0.0;

			if (click_count.containsKey(key))
				ctr = 100 * click_count.get(key).doubleValue() / val;
			
			if (event_count.containsKey(key))
				evpm = 1000 * event_count.get(key).doubleValue() / val;

			String strCtr = String.format("%.2f", ctr).replace(",", ".");
			String strEvpm = String.format("%.2f", evpm).replace(",", ".");

			int intCtr = (int) (Double.parseDouble(strCtr) * 100);
			int intEvpm = (int) (Double.parseDouble(strEvpm) * 100);

			model.getDates().add(key);
			model.getCtr().add(Integer.valueOf(intCtr));
			model.getEvpm().add(Integer.valueOf(intEvpm));
		}

		result.close();
		statement.close();

		return model;
	}

}
