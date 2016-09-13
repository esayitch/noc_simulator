package simulation;

import gui.NOCSimulatorGUI;

public class Network {

	public static Point size;
	public static Network network;
	
	protected Component components[][];
        protected String names[];
	protected int cycle;	

	public Network(int rows, int columns){
		size = new Point(rows,columns);
		network = this;
		components = new Component[rows][columns];
                names = new String[rows*columns];
		//Initializing default component array
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				components[i][j] = new Component(i,j);
                                if (i == 0 && j == 0){
                                    Component.selectComponent(components[i][j]);
                                }
			}
		}
                
                updateNames();
		//Initializing cycle to be 1
		cycle = 1;
	}
	
	public Results simulate(int cycleNumber){
		while (cycle <= cycleNumber){
			update();
			cycle++;
		}
		cycle = 1;
		Results results = new Results(this.size.x, this.size.y);
		results.populate(components);
		return results;
	}
        
        public void updateNames(){
            for (int i = 0; i < size.x; i++){
		for (int j = 0; j < size.y; j++){
                    names[i*size.y + j] = components[i][j].name;
		}
            }
            
            NOCSimulatorGUI.current.updateBoxes(names);
        }
        
        public String getNetworkInfo(){
            String info = "This is a " + size.x + " X " + size.y + " NOC. These are the components and any messages they are sending:\n";
            for (int i = 0; i < this.size.x; i++){
                info += "\n--------- ROW " + i + " ----------";
                for (int j = 0; j < this.size.y; j++){
                    info += "\n" + components[i][j].name + ":" + components[i][j].getSenderInfo();
                    info += "\n-------------";
                }
            }
            return info;
        }
	
	public boolean addSender(Sender sender, Point pos, boolean override){
		sender.setOwner(components[pos.x][pos.y]);
		components[pos.x][pos.y].addSender(sender);
		return true;
	}
	
	public boolean removeSender(Point from, Point to, int frequency) {
		return components[from.x][from.y].removeSender(to,frequency);
	}
	
	public void resetTraffic(){
            int rows = size.x;
            int columns = size.y;
            
            for (int i = 0; i < rows; i++){
		for (int j = 0; j < columns; j++){
                    components[i][j].resetComponentTraffic();
                }
            }
	}
	
	protected void update(){
		for (int i = 0; i < components.length; i++){
			for (int j = 0; j < components[i].length; j++){
				components[i][j].update(cycle);
			}
		}
	}

	
	
}
