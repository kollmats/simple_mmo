package org.smmo.common.readers;

import org.smmo.common.Context;
import org.smmo.common.util.Vec4i;
import org.smmo.common.PacketInputStream;
import org.smmo.common.actions.MoveEntityAction;
import java.lang.ClassNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MoveEntityReader implements PacketReader {
	private final long subjectId;
	private final Context sourceContext;
	private final Context targetContext;
	
	@Override
	public Object readFrom(InputStream inputStream) throws IOException, ClassNotFoundException {
		PacketInputStream stream = new PacketInputStream(inputStream);
		Vec4i objPos = stream.readVec4i();
		Vec4i dstPos = stream.readVec4i();		

		return new MoveEntityAction(subjectId, objPos, dstPos);		
	}
	
	public MoveEntityReader(long subjectId, Context sourceContext) {
		this(subjectId, sourceContext, sourceContext);
	}
	
	public MoveEntityReader(long subjectId, Context sourceContext, Context targetContext) {
		this.subjectId = subjectId;
		this.sourceContext = sourceContext;
		this.targetContext = targetContext;
	}	
}
