package org.smmo.common.writers;

import org.smmo.common.actions.MoveEntityAction;
import org.smmo.common.*;
import org.smmo.common.util.*;
import org.smmo.common.actions.*;
import java.io.IOException;
import java.io.OutputStream;

public class MoveEntityWriter implements PacketWriter {
	private final MoveEntityAction action;
	private final Context sourceContext;
	private final Context targetContext;	
	
	@Override
	public void writeTo(OutputStream outputStream) throws IOException {
		PacketOutputStream stream = new PacketOutputStream(outputStream);
		
		Vec4i objPos = action.objectPos;
		Vec4i dstPos = action.targetPos;
		
		if (sourceContext != targetContext) {
			Vec3i offset = Vec.subtract(sourceContext.getOrigin(), targetContext.getOrigin());
			objPos = Vec.add(objPos, new Vec4i(offset, 0));
			dstPos = Vec.add(dstPos, new Vec4i(offset, 0));			
		}
		
		stream.writeVec4i(objPos);
		stream.writeVec4i(dstPos);		
	}

	public MoveEntityWriter(MoveEntityAction action, Context sourceContext) {
		this(action, sourceContext, sourceContext);
	}
	
	public MoveEntityWriter(MoveEntityAction action, Context sourceContext, Context targetContext) {
		this.action = action;
		this.sourceContext = sourceContext;
		this.targetContext = targetContext;
	}
	
}

