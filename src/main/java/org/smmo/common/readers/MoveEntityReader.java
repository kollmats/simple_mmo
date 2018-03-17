package org.smmo.common.readers;

import org.smmo.common.actions.Action;
import org.smmo.common.Context;
import org.smmo.common.util.Vec4i;
import org.smmo.common.PacketInputStream;
import org.smmo.common.actions.MoveEntityAction;
import java.lang.ClassNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MoveEntityReader extends PacketReader {
	private final long actorId;
	
	@Override
	public Action readFrom(InputStream inputStream) throws IOException, ClassNotFoundException {
		PacketInputStream stream = new PacketInputStream(inputStream);
		Vec4i objPos = stream.readVec4i();
		Vec4i dstPos = stream.readVec4i();		

		return new MoveEntityAction(actorId, objPos, dstPos);		
	}

	public MoveEntityReader(long actorId) {
		super(actorId);
		this.actorId = actorId;
	}	
}
