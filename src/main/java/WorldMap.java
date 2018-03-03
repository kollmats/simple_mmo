package simple_mmo;

import java.util.ArrayList;
import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.Optional;

public class WorldMap implements Serializable {

	private final int columns;
	private final int rows;
	private final int depth;
	private final ImmutableList<Entity>[][][] entities;

	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public int getDepth() {
		return depth;
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

	public WorldMap getSubmap(int iStart, int jStart, int kStart, int rows, int columns, int depth) {
		Builder builder = new Builder(rows, columns, depth);
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				for (int k = 0; k < depth; k++) {
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
	
	public WorldMap shift(int di, int dj, int dk) {
		Builder builder = new Builder(rows, columns, depth);
		
		for (int i = 0; i < rows; i++) {
			if ((i + di < 0) || (i + di >= rows))
				continue;
			
			for (int j = 0; j < columns; j++) {
				if ((j + dj < 0) || (j + dj >= columns))
					continue;
				
				for (int k = 0; k < depth; k++) {
					if ((k + dk < 0) || (k + dk >= depth))
						continue;
					
					ImmutableList<Entity> entities = this.entities[i][j][k];

					for (int idx = 0; idx < entities.size(); idx++) {
						Entity e = entities.get(idx);
						builder = builder.placeEntity(i + di, j + dj, k + dk, idx, e);
					}					
				}
			}
		}

		return builder.build();
	}
	
	public WorldMap(WorldMap.Builder builder) {
		this.columns = builder.columns;
		this.rows = builder.rows;
		this.depth = builder.depth;
		entities = new ImmutableList[rows][columns][depth];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				for (int k = 0; k < depth; k++) {
					ArrayList builderList = builder.entities[i][j][k];
					entities[i][j][k] = ImmutableList.copyOf(builderList);
				}
			}
		}
	}

	public static class Builder {
		private final int rows;
		private final int columns;
		private final int depth;
		private final ArrayList<Entity>[][][] entities;

		public Builder(WorldMap other) {
			this.rows = other.getRows();
			this.columns = other.getColumns();
			this.depth = other.getDepth();
			entities = new ArrayList[rows][columns][depth];
			
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					for (int k = 0; k < depth; k++) {
						entities[i][j][k] = new ArrayList<Entity>(other.getEntities(i, j, k));
					}
				}
			}
		}
		
		public Builder(int rows, int columns, int depth) {
			this.rows = rows;
			this.columns = columns;
			this.depth = depth;
			entities = new ArrayList[rows][columns][depth];
			
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					for (int k = 0; k < depth; k++) {
						entities[i][j][k] = new ArrayList<Entity>();
					}
				}
			}
		}

		public WorldMap build() {
			return new WorldMap(this);
		}

		public Builder placeEntity(int i, int j, int k, int idx, Entity entity) {
			entities[i][j][k].add(idx, entity);
			return this;
		}		

		public Builder moveEntity(int iSrc, int jSrc, int kSrc, int idxSrc, int iDst, int jDst, int kDst, int idxDst) {
			Entity entity = entities[iSrc][jSrc][kSrc].remove(idxSrc);			
			entities[iDst][jDst][kDst].add(idxDst, entity);
			return this;
		}

		public Builder removeEntity(int i, int j, int k, int idx) {
			Entity entity = entities[i][j][k].remove(idx);
			return this;
		}
	}	
}
