package org.smmo.common;

import org.smmo.common.actions.*;
import org.smmo.common.writers.*;
import org.smmo.common.readers.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PacketType {

	MOVE_ENTITY(0x01, MoveEntityAction.class, MoveEntityWriter.class, MoveEntityReader.class);

	private final int id;
	private final Class<? extends Action> action;
	private final Class<? extends PacketWriter> writer;
	private final Class<? extends PacketReader> reader;
	
	private static final Map<Integer, PacketType> table = new HashMap<Integer, PacketType>();	
	static {
		for (PacketType t : EnumSet.allOf(PacketType.class)) {
			table.put(t.id, t);
		}
	}

	public static PacketType get(int id) {
		return table.get(id);
	}

	private PacketType(int id, Class<? extends Action> action, Class<? extends PacketWriter> writer, Class<? extends PacketReader> reader) {
		this.id = id;
		this.action = action;
		this.writer = writer;
		this.reader = reader;
	}
}
