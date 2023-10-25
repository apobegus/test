package com.artsai.project.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TableServiceAsync {
	void tableServer(String event, String type, AsyncCallback<TableModel[]> callback);
}
