package org.smmo.common;

public interface UniqueEntity extends Entity {

	public long getId();
	
	@Override
	public boolean equals(Object other);
	
	@Override
	public int hashCode();
}
