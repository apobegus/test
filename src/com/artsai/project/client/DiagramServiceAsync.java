package com.artsai.project.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DiagramServiceAsync {
	void diagramServer(String input, AsyncCallback<DiagramModel> callback);
}
