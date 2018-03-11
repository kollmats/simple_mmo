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
			return Optional.ofNullable(entities.get(0));
		} else {
			return Optional.empty();
		}
	}

	public WorldMap getSubmap(int iStart, int jStart, int kStart, int rows, int columns, int layers) {
		WorldMapBuilder builder = new WorldMapBuilder(rows, columns, layers);
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				for (int k = 0; k < layers; k++) {
					ImmutableList<Entity> entities;
					entities = this.entities[iStart + i][jStart + j][kStart + k];

					for (int idx = 0; idx < entities.size(); idx++) {
						Entity e = entities.get(idx);
						builder = builder.placeEntity(i, j, k, idx, e);
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
