package org.smmo.common;

import org.smmo.common.actions.*;
import org.smmo.common.writers.*;
import org.smmo.common.readers.*;
import org.smmo.common.switchers.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ActionInfo {

	MOVE_ENTITY(0x01, MoveEntityAction.class, MoveEntityWriter.class, MoveEntityReader.class, MoveEntitySwitcher.class);

	private final int id;
	private final Class<? extends Action> action;
	private final Class<? extends PacketWriter> writer;
	private final Class<? extends PacketReader> reader;
	private final Class<? extends ContextSwitcher> switcher;
	
	private static final Map<Integer, ActionInfo> idTable = new HashMap<Integer, ActionInfo>();
	private static final Map<Class<? extends Action>, ActionInfo> actionTable = new HashMap<Class <? extends Action>, ActionInfo>();
	
	static {
		for (ActionInfo info : EnumSet.allOf(ActionInfo.class)) {
			idTable.put(info.id, info);
			actionTable.put(info.action, info);
		}
	}

	public static ActionInfo get(int id) {
		return idTable.get(id);
	}
	
	public static ActionInfo get(Action action) {
		return actionTable.get(action.getClass());
	}

	private ActionInfo(int id, Class<? extends Action> action, Class<? extends PacketWriter> writer, Class<? extends PacketReader> reader, Class<? extends ContextSwitcher> switcher) {
		this.id = id;
		this.action = action;
		this.writer = writer;
		this.reader = reader;
		this.switcher = switcher;
	}

	public PacketWriter getWriter(Action action) {
		try {
			return writer.getDeclaredConstructor(Action.class).newInstance(action);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public PacketReader getReader() {
		try {
			return reader.newInstance();			
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public ContextSwitcher getContextSwitcher() {
		try {
			return switcher.newInstance();			
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}	
}
