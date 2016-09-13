package simulation;

public class Sender {
	
	public Point dest;
	public int freq;
	public Component owner;

	public Sender(Point destination, int frequency, Component own){
		dest = destination;
		freq = frequency;
		owner = own;
	}
	
	public void update(int cycle){
		if (cycle % freq == 0){
			if (owner != null){
				owner.handleMessage(new Message(dest));
			}
		}
	}
	
	public void setOwner(Component component){
		owner = component;
	}

}
