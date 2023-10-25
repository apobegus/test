package com.artsai.project.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import com.artsai.project.client.GreetingService;
import com.artsai.project.client.ServiceException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	private final static long serialVersionUID = 3045L;

	public List<String> greetServer(String input) throws ServiceException {
		Connection connection = ServiceHelper.getConnection();
		List<String> list = new ArrayList<String>();

		try {
			String path = ServiceHelper.getFilePath(getServletContext(), "/interview.y.csv");

			PreparedStatement statement = connection.prepareStatement("SELECT tag FROM \"" + path + "\" group by tag order by tag");
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				list.add(result.getString(1));
			}

			result.close();
			statement.close();

			return list;
		}
		catch (Exception e) {
			throw new ServiceException("Load evens list error !", e.getMessage());
		}
		finally {
			try {
				if (connection != null) connection.close();
			}
			catch (Exception e) { }
		}
	}

}
