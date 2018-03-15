package org.smmo.common.actions;

import org.smmo.common.Context;
import org.smmo.common.NotImplementedException;

public abstract class Action {
	public abstract boolean isValid(Context context);
	public abstract Context execute(Context context);
	
	public Action switchContext(Context src, Context dst) {
		throw new NotImplementedException();
	}
}
