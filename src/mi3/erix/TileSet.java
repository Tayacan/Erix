package mi3.erix;

import java.util.HashMap;
import java.util.Map;

public class TileSet {
	/*
	 * This class is supposed to handle 
	 * the status of each point in the 
	 * game area.
	 */
	
	public int x = 0;
	public int y = 0;
	
	public Map<String, Integer> tileStatus = new HashMap<String,Integer>();
	
	// Status koder
	public static final int PLAYER = 1;
	public static final int EMPTY = 2;
	public static final int TAKEN = 3;
	public static final int LINE = 4;
	public static final int ENEMY = 5;
	
	/*
	 * Ville meget hellere have haft en tuple-lignende
	 * datastruktur med 2 ints, men Java har ikke tuples
	 * og vi gider ikke skrive en class bare til det.
	 * Bruger Strings i stedet.
	 */
	public String[][] coords = null;
	public int[][] status = null;
	
	// Constructor version 1
	public TileSet() {
		makeTiles();
		setTiles();
	}
	
	// Constructor version 2
	public TileSet(int x, int y) {
		makeTiles(x,y);
		setTiles();
	}
	
	private void makeTiles () {
		coords = new String[this.x][this.y];
		status = new int[this.x][this.y];
	}
	
	private void makeTiles (int x,int y) {
		this.x = x;
		this.y = 0;
		coords = new String[this.x][this.y];
		status = new int[this.x][this.y];
	}
	
	private boolean setTiles () {
		if(coords == null || status == null || this.x < 1 || this.y < 1) {
			return false; // Hvis makeTiles ikke er blevet kaldt endnu.
		}
		
		// Find koordinater og smid dem ind i vores 2D array
		for(int i = 0; i < this.x; i++) {
			for(int j = 0; j < this.y; j++) {
				coords[i][j] = ((j*10)-10) + "x" + ((i*10)-10); // Hardcodet og grimt
				status[i][j] = TileSet.EMPTY;
				tileStatus.put(coords[i][j], status[i][j]);
			}
		}
		
		return true; // Success!
	}
}
