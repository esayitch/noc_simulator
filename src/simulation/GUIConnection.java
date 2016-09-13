package simulation;

import java.util.Iterator;

public class GUIConnection {

    public Point size;
    private Network network;
	
        
	/**
	 * This thing handles the transfer of data from the GUI, creating a simulation, running it,
	 * and returning the results to the GUI to be drawn.
	 * @param args
	 */
    public GUIConnection(){
    }
    
    public boolean buildSimulator(String rows, String columns){
        try {
            int r = Integer.parseInt(rows);
            int c = Integer.parseInt(columns);
            network = new Network(r,c);
            this.size = new Point(r,c);
            return true;
        }
        catch (NumberFormatException nfe){
            return false;
        }
    }	
    
     public boolean addSender(Point from, Point to, int frequency){
    	 if (network == null){
            return false;
        }
    	if (from.x >= network.size.x 
    			|| from.y >= network.size.y 
    			|| to.x >= network.size.x
    			|| to.y >= network.size.y
    			|| to.x < 0
    			|| to.y < 0
    			|| from.x < 0
    			|| from.y < 0){
    		return false;
    	}
    	return network.addSender(new Sender(to,frequency,null),from,true);
    }
    
    public boolean removeSender(Point from, Point to, int frequency){
        if (network == null){
            return false;
        }
    	if (from.x >= network.size.x 
    			|| from.y >= network.size.y 
    			|| to.x >= network.size.x
    			|| to.y >= network.size.y
    			|| to.x < 0
    			|| to.y < 0
    			|| from.x < 0
    			|| from.y < 0){
    		return false;
    	}
    	return network.removeSender(from,to,frequency);
    }
    
    public void clearNetwork() {
        network = null;
    }
    
    public void clearSimulation(){
        if (network == null){
            return;
	}
    	network.resetTraffic();
    }
    
    public void resetSimulation(){
    	network = new Network(network.size.x, network.size.y);   
    }
    
    public Results simulate(String cycles){
    	 try {
            int n = Integer.parseInt(cycles);
            if (network == null){
                System.out.println("No network has been defined.");
    		return null;
            }
            //System.out.println("");
            return network.simulate(n);            
        }
        catch (NumberFormatException nfe){
             System.out.println("Please input an integer.");
            return null;
        }
    }
    
    /*
    * This returns the complete network as a String for saving.
    */
    public String getNetworkData(){
       String data = "";
       if (network == null){
           return data;
       }
       if (network.components.length == 0){
           return data;
       }
       if (network.components[0].length == 0){
           return data;
       }
       data = data + network.components.length + "," + network.components[0].length + ":"; 
       
       int row = 0;
       
       while (row < network.components.length){
           int col = 0;
           while (col < network.components[0].length){
               Iterator<Sender> iter = network.components[row][col].senders.iterator();
               while (iter.hasNext()){
                   Sender sender = iter.next();
                   if (!data.endsWith(":")){
                       data += "-";
                   } 
                   
                   data = data + row + "," + col + "," + sender.dest.x + "," + sender.dest.y + "," +sender.freq;
               }
               col++;
           }
           row++;
       }

       return data;
    }
    
    public void selectComponent(String name){
        if (name == null|| network == null){
            return;
        }
        Component comp = null;
        int rows = Network.size.x;
        int cols = Network.size.y;
        
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
		if (network.components[i][j].name.equals(name)){
                    comp = network.components[i][j];
                }
            }
        }
        
        Component.selectComponent(comp);
    }
    
    public void updateComponentName(String name){
        if (name == null || network == null || Component.getSelected() == null){
            return;
        }
        Component comp = Component.getSelected();
        comp.name = name;
        network.updateNames();
    }
    
    public Component getCompByName(String name){
        Component comp = null;
        int rows = Network.size.x;
        int cols = Network.size.y;
        
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
		if (network.components[i][j].name.equals(name)){
                    comp = network.components[i][j];
                }
            }
        }
        
        return comp;
    }
}


