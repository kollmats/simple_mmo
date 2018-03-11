package org.smmo.server;

import java.lang.Thread;

public class Connection {
	private final int id;
	private final Thread thread;

	public Thread getThread() {
		return thread;
	}

	public int getId() {
		return id;
	}
	
	public Connection(int id, Thread thread) {
		this.id = id;
		this.thread = thread;
	}

	public Connection(int id) {
		this(id, null);
	}
	
	public boolean equals(Object other) {
		if ( this == other )
			return true;
		
		if ( !(other instanceof Connection) )
			return false;

		return ((Connection) other).getId() == id;
	}

	public int hashCode() {
		return id;
	}
	

	
}
