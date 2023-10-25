package com.artsai.project.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DiagramModel implements IsSerializable {

	private List<String> dates = new ArrayList<String>();

	private List<Integer> ctr = new ArrayList<Integer>();
	private List<Integer> evpm = new ArrayList<Integer>();

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public List<Integer> getCtr() {
		return ctr;
	}

	public void setCtr(List<Integer> ctr) {
		this.ctr = ctr;
	}

	public List<Integer> getEvpm() {
		return evpm;
	}

	public void setEvpm(List<Integer> evpm) {
		this.evpm = evpm;
	}

}
