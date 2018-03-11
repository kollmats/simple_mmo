package org.smmo.common.actions;

import org.smmo.common.Context;

public interface Action {
	public abstract boolean isValid(Context context);
	public abstract Context execute(Context context);
}
