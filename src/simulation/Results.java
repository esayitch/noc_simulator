package simulation;

public class Results {
	
	public int[][] traffic;
	public Point size;
	
	public Results(int row, int column){
		size = new Point(row,column);
		traffic = new int[size.x][size.y];
	}
	
	public void populate(Component[][] components){
		for (int i = 0; i < components.length; i++){
			System.out.print("\n");
			for (int j = 0; j < components[i].length; j++){
				traffic[i][j] = components[i][j].traffic;
			}
		}
	}

}
