package org.smmo.common.switchers;

import org.smmo.common.Context;
import org.smmo.common.actions.Action;

public interface ContextSwitcher {
	public Action apply(Action action, Context target, Context source);
}

