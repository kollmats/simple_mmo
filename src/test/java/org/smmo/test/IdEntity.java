package org.smmo.test;

import org.smmo.common.*;

public class IdEntity extends UniqueEntity {
	private long id;
	public IdEntity(long id) {
		this.id = id;
	}
		
	public long getId() {
		return id;
	}
}
