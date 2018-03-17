package org.smmo.common;

public abstract class UniqueEntity implements Entity {

	public abstract long getId();
	
	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		
		if (!(other instanceof UniqueEntity)) 
			return false;

		return this.getId() == ((UniqueEntity) other).getId();
	}	
	
	@Override
	public int hashCode() {
		return (int) getId();
	}
}
