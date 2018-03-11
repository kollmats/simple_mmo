package org.smmo.common;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.Optional;

public class WorldMapBuilder {
	private final int rows;
	private final int columns;
	private final int layers;
	private final ArrayList<Entity>[][][] entities;
	
	public WorldMapBuilder(WorldMap other) {
		this.rows = other.getRows();
		this.columns = other.getColumns();
		this.layers = other.getLayers();
		entities = new ArrayList[rows][columns][layers];
			
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				for (int k = 0; k < layers; k++) {
					entities[i][j][k] = new ArrayList<Entity>(other.getEntities(i, j, k));
				}
			}
		}
	}
	
	public WorldMapBuilder(int rows, int columns, int layers) {
		this.rows = rows;
		this.columns = columns;
		this.layers = layers;
		entities = new ArrayList[rows][columns][layers];
			
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				for (int k = 0; k < layers; k++) {
					entities[i][j][k] = new ArrayList<Entity>();
				}
			}
		}
	}

	public WorldMap build() {
		return new WorldMap(this);
	}

	public WorldMapBuilder placeEntity(int i, int j, int k, int idx, Entity entity) {
		entities[i][j][k].add(idx, entity);
		return this;
	}		

	public WorldMapBuilder moveEntity(int iSrc, int jSrc, int kSrc, int idxSrc, int iDst, int jDst, int kDst, int idxDst) {
		Entity entity = entities[iSrc][jSrc][kSrc].remove(idxSrc);
		System.out.println("success");
		entities[iDst][jDst][kDst].add(idxDst, entity);
		return this;
	}

	public WorldMapBuilder removeEntity(int i, int j, int k, int idx) {
		Entity entity = entities[i][j][k].remove(idx);
		return this;
	}

	public ImmutableList<Entity> getEntities(int i, int j, int k) {
		return ImmutableList.copyOf(entities[i][j][k]);
	}

	public WorldMapBuilder shift(int di, int dj, int dk) {
		WorldMapBuilder builder = new WorldMapBuilder(rows, columns, layers);
		
		for (int i = 0; i < rows; i++) {
			if ((i + di < 0) || (i + di >= rows))
				continue;
			
			for (int j = 0; j < columns; j++) {
				if ((j + dj < 0) || (j + dj >= columns))
					continue;
				
				for (int k = 0; k < layers; k++) {
					if ((k + dk < 0) || (k + dk >= layers))
						continue;
					
					for (int idx = 0; idx < entities[i][j][k].size(); idx++) {
						Entity e = entities[i][j][k].get(idx);
						builder = builder.placeEntity(i + di, j + dj, k + dk, idx, e);
					}					
				}
			}
		}

		return builder;
	}

	public WorldMapBuilder append(WorldMap other, int axis) {
		int newRows = rows + (axis == 0 ? 1 : 0);
		int newColumns = columns + (axis == 1 ? 1 : 0);
		int newLayers = layers + (axis == 2 ? 1 : 0);
		
		if ((axis < 0) || (axis > 2)) {
			throw new IllegalArgumentException("Axis must be 0, 1 or 2!");				
		}

		if ((axis != 0) && (rows != other.getRows())) {
			throw new IllegalArgumentException("Number of rows must be equal!");				
		}
		
		if ((axis != 1) && (columns != other.getColumns())) {
			throw new IllegalArgumentException("Number of columns must be equal!");				
		}
		
		if ((axis != 2) && (layers != other.getLayers())) {
			throw new IllegalArgumentException("Number of layers must be equal!");				
		}

		WorldMapBuilder builder = new WorldMapBuilder(newRows, newColumns, newLayers);

		for (int i = 0; i < newRows; i++) {
			for (int j = 0; j < newColumns; j++) {
				for (int k = 0; k < newLayers; k++) {

					ImmutableList<Entity> entities;
					
					if ((i < rows) && (j < columns) && (k < layers)) {
						entities = getEntities(i, j, k);
					} else {
						int ii = axis == 0 ? i - rows : i;
						int jj = axis == 1 ? j - columns : j;
						int kk = axis == 2 ? k - layers : k;
						
						entities = other.getEntities(ii, jj, kk);
					}
					
					for (int idx = 0; idx < entities.size(); idx++) {
						Entity e = entities.get(idx);
						builder = builder.placeEntity(i, j, k, idx, e);
					}					
				}
			}
		}
		return builder;
	}
	
	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public int getLayers() {
		return layers;
	}
}	
