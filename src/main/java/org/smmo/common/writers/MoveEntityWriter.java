package org.smmo.common.writers;

import org.smmo.common.actions.MoveEntityAction;
import org.smmo.common.*;
import org.smmo.common.util.*;
import org.smmo.common.actions.*;
import java.io.IOException;
import java.io.OutputStream;

public class MoveEntityWriter extends PacketWriter {
	private final MoveEntityAction action;
	
	@Override
	public void writeTo(OutputStream outputStream) throws IOException {
		PacketOutputStream stream = new PacketOutputStream(outputStream);
		
		Vec4i objPos = action.objectPos;
		Vec4i dstPos = action.targetPos;
		
		stream.writeVec4i(objPos);
		stream.writeVec4i(dstPos);		
	}

	public MoveEntityWriter(MoveEntityAction action) {
		super(action);
		this.action = action;
	}	
}

