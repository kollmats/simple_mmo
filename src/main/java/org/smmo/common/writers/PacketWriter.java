package org.smmo.common.writers;

import org.smmo.common.*;
import org.smmo.common.actions.*;
import java.io.IOException;

public abstract class PacketWriter {
	private final Action action;

	private final Context sourceContext;
	private final Context targetContext;	
	
	public abstract void writeTo(PacketOutputStream outputStream) throws IOException;

	protected Action getAction() {
		return action;
	}

	protected Context getSourceContext() {
		return sourceContext;
	}
	
	protected Context getTargetContext() {
		return targetContext;
	}

	protected PacketWriter() {
		throw new UnsupportedOperationException();
	}
	
	public PacketWriter(Action action) {
		this.action = action;
		this.sourceContext = null;
		this.targetContext = null;
	}
	
	private PacketWriter(Action action, Context sourceContext, Context targetContext) {
		this.action = action;
		this.sourceContext = sourceContext;
		this.targetContext = targetContext;
	}
	
	public PacketWriter setSourceContext(Context context) throws Exception {
		Class[] cArg = {Action.class, Context.class, Context.class};
		Class<? extends PacketWriter> runtimeClass = getClass();
		return getClass().getDeclaredConstructor(cArg).newInstance(action, context, context);
	}
	
	public PacketWriter setTargetContext(Context context) throws Exception {
		if (context != null) {
			Class[] cArg = {Action.class, Context.class, Context.class};
			Class<? extends PacketWriter> runtimeClass = getClass();
			return getClass().getDeclaredConstructor(cArg).newInstance(action, sourceContext, context);				
		} else
			throw new IllegalArgumentException("Source context not set!");		
	}
	
}
