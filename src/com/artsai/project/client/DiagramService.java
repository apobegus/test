package com.artsai.project.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("diagram")
public interface DiagramService extends RemoteService {
	DiagramModel diagramServer(String input) throws ServiceException;
}
