package org.smmo.test;

import org.smmo.common.*;

public class IdEntity implements UniqueEntity {
	private long id;
	public IdEntity(long id) {
		this.id = id;
	}
		
	public long getId() {
		return id;
	}

	@Override
	public boolean equals(Object other) {
		return ((IdEntity) other).getId() == getId();
	}

	@Override
	public int hashCode() {
		return (int) getId();
	}
}
