package org.smmo.server;

import org.smmo.common.WorldMap;

public abstract class Spirit {
	private int id;
	
	public Spirit(int id) {
		this.id = id;
	}
	
	abstract public void onMapUpdate(WorldMap newMap);

	public int getId() {
		return id;
	}
	
	public boolean equals(Spirit other) {
		return other.getId() == id;
	}
	public int hashCode() {
		return id;
	}
}
