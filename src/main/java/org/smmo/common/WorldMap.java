package org.smmo.common;

import java.util.ArrayList;
import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.Optional;

public class WorldMap implements Serializable {

	private final int columns;
	private final int rows;
	private final int layers;
	private final ImmutableList<Entity>[][][] entities;

	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public int getLayers() {
		return layers;
	}

	public ImmutableList<Entity> getEntities(int i, int j, int k) {
		return entities[i][j][k];
	}

	public Optional<Entity> getEntity(int i, int j, int k, int idx) {
		
		ImmutableList<Entity> entities = getEntities(i, j, k);
		if (entities.size() > 0) {
			return Optional.ofNullable(entities.get(idx));
		} else {
			return Optional.empty();
		}
	}

	public WorldMap getSubmap(int iStart, int jStart, int kStart, int nRows, int nCols, int nLays) {

		final int nRowsThis = getRows();
		final int nColsThis = getColumns();
		final int nLaysThis = getLayers();
		
		WorldMapBuilder builder = new WorldMapBuilder(nRows, nCols, nLays);
		
		for (int iSub = 0; iSub < nRows; iSub++) {
			int iThis = iStart + iSub;

			if ((iThis < 0) || (iThis > nRowsThis))
				continue;
			
			for (int jSub = 0; jSub < nCols; jSub++) {
				int jThis = jStart + jSub;
				
				if ((jThis < 0) || (jThis > nColsThis))
					continue;
				
				for (int kSub = 0; kSub < nLays; kSub++) {
					int kThis = kStart + kSub;
					
					if ((kThis < 0) || (kThis > nLaysThis))
						continue;					
					
					ImmutableList<Entity> entities = this.entities[iThis][jThis][kThis];

					for (int idx = 0; idx < entities.size(); idx++) {
						Entity e = entities.get(idx);
						builder = builder.placeEntity(iSub, jSub, kSub, idx, e);
					}					
				}
			}
		}

		return builder.build();
	}
	
	public WorldMap(WorldMapBuilder builder) {
		this.columns = builder.getColumns();
		this.rows = builder.getRows();
		this.layers = builder.getLayers();
		entities = new ImmutableList[rows][columns][layers];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				for (int k = 0; k < layers; k++) {
					//ArrayList builderList = builder.entities[i][j][k];
					entities[i][j][k] = builder.getEntities(i, j, k);//ImmutableList.copyOf(builderList);
				}
			}
		}
	}

}
