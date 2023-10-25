package com.artsai.project.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TableModel implements IsSerializable {

	private String name;
	private String impression;
	private String ctr;
	private String evpm;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImpression() {
		return impression;
	}

	public void setImpression(String impression) {
		this.impression = impression;
	}

	public String getCtr() {
		return ctr;
	}

	public void setCtr(String ctr) {
		this.ctr = ctr;
	}

	public String getEvpm() {
		return evpm;
	}

	public void setEvpm(String evpm) {
		this.evpm = evpm;
	}

}
