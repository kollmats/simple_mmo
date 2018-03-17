package org.smmo.common.actions;

import org.smmo.common.Context;
import org.smmo.common.NotImplementedException;
import org.smmo.common.util.Vec3i;

import java.util.List;

public abstract class Action {
	public abstract boolean isValid(Context context);
	public abstract Context execute(Context context);
	public abstract List<Vec3i> getAffectedLocations();
}
