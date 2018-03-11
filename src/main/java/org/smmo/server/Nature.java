package org.smmo.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.math3.analysis.function.Abs;

import org.smmo.common.*;

public class Nature {

	private static final int SPIRIT_MAP_N_ROWS = 9;
	private static final int SPIRIT_MAP_N_COLS = 9;
	private static final int SPIRIT_MAP_N_LAYS = 3;
	
	private WorldMap worldMap;
	private BiMap<Spirit, Location> spiritLocations;

	
	public Nature(WorldMap worldMap) {
		this(worldMap, HashBiMap.create());
	}
	
	public Nature(WorldMap worldMap, BiMap spiritLocations) {
		this.worldMap = new WorldMapBuilder(worldMap).build();
		this.spiritLocations = HashBiMap.create(spiritLocations);
	}

	private void moveEntity(int iSrc, int jSrc, int kSrc, int idxSrc, int iDst, int jDst, int kDst, int idxDst) {
		// TODO: checks for legality of move!

		worldMap = new WorldMapBuilder(worldMap)
			.moveEntity(iSrc, jSrc, kSrc, idxSrc,
						iDst, jDst, kDst, idxDst)
			.build();

		for (Spirit spirit : spiritLocations.keySet()) {
			final Location spiritLoc = spiritLocations.get(spirit);

			final int MAX_ROWS = (int)((SPIRIT_MAP_N_ROWS - 1) / 2);
			final int MAX_COLS = (int)((SPIRIT_MAP_N_COLS - 1) / 2);
			final int MAX_LAYS = (int)((SPIRIT_MAP_N_LAYS - 1) / 2);
			
			final Abs abs = new Abs();
			
			if ((abs.value(spiritLoc.i - iSrc) > MAX_ROWS)  || (abs.value(spiritLoc.i - iDst) > MAX_ROWS))
				continue;
			
			if ((abs.value(spiritLoc.j - jSrc) > MAX_COLS)  || (abs.value(spiritLoc.j - jDst) > MAX_COLS))
				continue;
			
			if ((abs.value(spiritLoc.k - kSrc) > MAX_LAYS)  || (abs.value(spiritLoc.k - kDst) > MAX_LAYS))
				continue;

			WorldMap wm = worldMap.getSubmap(spiritLoc.i - MAX_ROWS,
											 spiritLoc.j - MAX_COLS,
											 spiritLoc.k - MAX_LAYS,
											 SPIRIT_MAP_N_ROWS,
											 SPIRIT_MAP_N_COLS,
											 SPIRIT_MAP_N_LAYS);			
			spirit.onMapUpdate(wm);			
		}			
	}

	public void moveEntity(Spirit actor, int iSrc, int jSrc, int kSrc, int idxSrc, int iDst, int jDst, int kDst, int idxDst) {
		Location src = transformFromSpiritFrame(actor, new Location(iSrc, jSrc, kSrc, idxSrc));
		Location dst = transformFromSpiritFrame(actor, new Location(iDst, jDst, kDst, idxDst));
		// TODO: checks for legality of move!
		moveEntity(src.i, src.j, src.k, src.idx, dst.i, dst.j, dst.k, dst.idx);
	}


	private Location transformFromSpiritFrame(Spirit spirit, Location loc) {
		Location spiritLoc = getSpiritLocation(spirit);

		loc = new Location(loc.i + spiritLoc.i - (int)((SPIRIT_MAP_N_ROWS - 1) / 2),
						   loc.j + spiritLoc.j - (int)((SPIRIT_MAP_N_COLS - 1) / 2),
						   loc.k + spiritLoc.k - (int)((SPIRIT_MAP_N_LAYS - 1) / 2),
						   loc.idx);
		return loc;
	}

	private Optional<Spirit> getEntitySpirit(int i, int j, int k, int idx) {
		Location loc = new Location(i, j, k, idx);
		Spirit spirit = spiritLocations.inverse().get(loc);

		if (spirit != null) {
			return Optional.of(spirit);
		} else {
			return Optional.empty();
		}
	}
	
	public void possessEntity(int i, int j, int k, int idx, Spirit spirit) {
		
		if (getEntitySpirit(i, j, k, idx).isPresent()) {
			throw new IllegalArgumentException("Entity already possessed!");
		}		
		
		if (worldMap.getEntity(i, j, k, idx).isPresent()) {
			Location loc = new Location(i, j, k, idx);
			spiritLocations.put(spirit, loc);
		} else {
			throw new IllegalArgumentException("No entity at position!");
		}
	}

	public void forsakeEntity(int i, int j, int k, int idx) {

		Optional<Spirit> spiritOpt = getEntitySpirit(i, j, k, idx);
		
		if (spiritOpt.isPresent()) {
			spiritLocations.remove(spiritOpt.get());			
		} else {
			throw new IllegalArgumentException("Entity not possessed!");
		}
	}

	private Location getSpiritLocation(Spirit spirit) {
		Location loc = spiritLocations.get(spirit);

		if (loc != null) {
			return loc;
		} else {
			throw new IllegalArgumentException("Unknown spirit!");
		}		
	}
	
	
	public static class Location {
		public final int i;
		public final int j;
		public final int k;
		public final int idx;

		public Location(int i, int j, int k, int idx) {
			this.i = i;
			this.j = j;
			this.k = k;
			this.idx = idx;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;

			if (!(o instanceof Location))
				return false;
			
			Location other = (Location) o;
			return ((i == other.i) && (j == other.j) && (k == other.k) && (idx == other.idx));				
		}
		
		@Override
		public int hashCode() {
			int code = (5 + i) * (3 + j) * 20  +  (1 + k) * (idx + 17) * 32;
			return code;
		}		
	}
}
