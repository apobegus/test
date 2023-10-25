package com.artsai.project.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("table")
public interface TableService extends RemoteService {
	TableModel[] tableServer(String event, String type) throws ServiceException;
}
