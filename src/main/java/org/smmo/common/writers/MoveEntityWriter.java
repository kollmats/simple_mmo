package org.smmo.common.writers;

import org.smmo.common.actions.MoveEntityAction;
import org.smmo.common.*;
import org.smmo.common.util.*;
import java.io.IOException;

public class MoveEntityWriter extends PacketWriter {

	@Override
	public void writeTo(PacketOutputStream stream) throws IOException {
		MoveEntityAction moa = (MoveEntityAction) getAction();
		Context srcContext = getSourceContext();
		Context dstContext = getTargetContext();

		UniqueEntity object = moa.object;
		Vec4i objPos = srcContext.getEntityPosition(object);
		Vec4i dstPos = moa.dst;
		
		if ((srcContext != null) && (srcContext != dstContext)) {
			Vec3i offset = Vec.subtract(srcContext.getOrigin(), dstContext.getOrigin());
			objPos = Vec.add(objPos, new Vec4i(offset, 0));
			dstPos = Vec.add(dstPos, new Vec4i(offset, 0));			
		}

		stream.writeVec4i(objPos);
		stream.writeVec4i(dstPos);		
	}
}

