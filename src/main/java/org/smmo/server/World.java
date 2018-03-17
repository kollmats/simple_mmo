package org.smmo.server;

import org.smmo.common.actions.Action;
import org.smmo.common.UniqueEntity;
import org.smmo.common.Context;
import org.smmo.common.ActionInfo;
import org.smmo.common.ContextReducer;
import org.smmo.common.switchers.ContextSwitcher;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class World {

	public static interface OnBroadcastFailureListener {
		public void onBroadcastFailure(long entityId);
	}
	
	public static interface OnBroadcastActionListener {
		public void onBroadcastAction(long entityId, Action action);
	}	
	
	private final OnBroadcastFailureListener onBroadcastFailureListener; 	
	private final OnBroadcastActionListener onBroadcastActionListener; 
    private final Object lock = new Object();
	private Context globalContext;


	public World(Context initialContext, OnBroadcastActionListener onBroadcastActionListener, OnBroadcastFailureListener onBroadcastFailureListener) {
		this.globalContext = initialContext;
		this.onBroadcastActionListener = onBroadcastActionListener;
		this.onBroadcastFailureListener = onBroadcastFailureListener;		
	}	
	
	public boolean onAction(long actorId, Action localAction) {
		final Map<Long, Action> localActions = executeActionAndGetBroadcastInformation(actorId, localAction);
		
		if (localActions != null) {
			broadcastActions(localActions);
			return true;
		} else {
			broadcastFailure(actorId);
			return false;
		}
	}

	private void broadcastActions(Map<Long, Action> localActions) {
		
		if (onBroadcastActionListener != null) {
			for (Map.Entry<Long, Action> entry : localActions.entrySet()) {
				long spectatorId = entry.getKey();
				Action action = entry.getValue();				
				onBroadcastActionListener.onBroadcastAction(spectatorId, action);
			}
		}		
	}

	private void broadcastFailure(long actorId) {
		if (onBroadcastFailureListener != null)
			onBroadcastFailureListener.onBroadcastFailure(actorId);
	}

	private Map<Long, Action> executeActionAndGetBroadcastInformation(long actorId, Action localAction) {
		final Map<Long, Action> localActions = new HashMap<Long, Action>();
		
		synchronized(lock) {
			final UniqueEntity actor = globalContext.getEntity(actorId);
			Context localContext = ContextReducer.reduceToPerspectiveOf(actor, globalContext);

			if (!localAction.isValid(localContext))
				return null;

			final ContextSwitcher contextSwitcher = ActionInfo.get(localAction).getContextSwitcher();
			final Action globalAction = contextSwitcher.apply(localAction, globalContext, localContext);

			if(!globalAction.isValid(globalContext))
				return null;

			final List<UniqueEntity> spectators = globalContext.getSpectators(globalAction);

			globalContext = globalAction.execute(globalContext);
			
			//localActions.put(actor, localContext);
			
			for (UniqueEntity spectator : spectators) {

				if (spectator.equals(actor)) 
					continue;
				
				localContext = ContextReducer.reduceToPerspectiveOf(spectator, globalContext);
				localAction = contextSwitcher.apply(globalAction, localContext, globalContext);
				localActions.put(spectator.getId(), localAction);
			}			
		}

		return localActions;
	}
}
