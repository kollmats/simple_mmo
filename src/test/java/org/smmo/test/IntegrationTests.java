package org.smmo.test;

import org.smmo.server.*;
import org.smmo.common.*;
import org.smmo.common.actions.*;
import org.smmo.common.writers.*;
import org.smmo.common.readers.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class IntegrationTests {


	@Test
	public void testWriteActionPacketAndUpdateMap() {
		/*
		// Setup
		Context mainContext;
		Context playerContext;

		
		// Client side
		MoveEntityAction mea; // Set this up somehow. Change to use (nulled) ids and only positions
		MoveEntityWriter mew; // Implement this!

		mew.writeTo(stream);

		// Server side
		MoveEntityReader mer; // Implement!

		playerId = mapWithPlayerIdsAndSocketIds.get(socket);

		entity = mainContext.getEntityById(playerId);
		Context playerContext = mainContext.getPerspective(entity);
		
		MoveEntityAction mea = mer
			.switchContext(playerContext, mainContext) // Call a private consturctor. This method is implemented by default but with "not implemented exception"
			.readFrom(stream);

		mea.execute(mainContext);

		// Now broadcast changes!*/
	}

}
