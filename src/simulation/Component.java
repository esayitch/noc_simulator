package simulation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Component {

        protected static Component selected;    
	protected static int LEFT = 1;
	protected static int UP = 2;
	protected static int RIGHT = 3;
	protected static int DOWN = 4;
	
	public int traffic;
	public Point position;
        public String name;
	public LinkedList<Sender> senders;
	public LinkedBlockingQueue<Message>[] messages;
	public LinkedBlockingQueue<Message> recievedMessages;
	Message[] outgoing;
	Boolean[] directionValidity;
	
	@SuppressWarnings("unchecked")
	public Component(int x, int y){
		traffic = 0;
		position = new Point(x,y);
                name = "r"+ x + "c" + y;
		senders = new LinkedList<Sender>();
		messages = new LinkedBlockingQueue[5];
		outgoing = new Message[5];
		directionValidity = new Boolean[5];
		initializeMessageQueues();
		initializeDirections();
	}
	
        public static Component getSelected(){
            return selected;
        }
        
        public static void selectComponent(Component compo){
            selected = compo;
        }
        
	public void update(int cycle){
		updateSenders(cycle);
		applyOutgoing();
		sendMessages();
	}
	
	public void addSender(Sender sender){
		senders.add(sender);
		sender.setOwner(this);
	}
	
	public boolean removeSender(Point to, int freq){
		for (Sender sender: senders){
			if (sender.dest.x == to.x && sender.dest.y == to.y && sender.freq == freq){
				senders.remove(senders.indexOf(sender));
				return true;
			}
		}
		return false;
	}
	
        public void resetComponentTraffic(){
            traffic = 0;
            initializeMessageQueues();
            outgoing = new Message[5];
        }
        
	/*
	 * Here the routing is performed small sons.
	 */
	public void handleMessage(Message message){
		traffic++;
		if (message.dest.y > position.y){
			messages[RIGHT].offer(message);
		}
		else if (message.dest.y < position.y){
			messages[LEFT].offer(message);
		}
                else if (message.dest.x > position.x){
			messages[DOWN].offer(message);
		}
		else if (message.dest.x < position.x){
			messages[UP].offer(message);
		}
	}
        
        public String getInfo(){
            String info = getBasicInfo() + getSenderInfo();  
            return info;
        }
        
        public String getSenderInfo(){
            String info = "";
            Iterator<Sender> iter = senders.iterator();
            Sender curr = null;
            while (iter.hasNext()){
                curr = iter.next();
                info += "\nTo " + Network.network.components[curr.dest.x][curr.dest.y].name + " at (" + curr.dest.x + "," + curr.dest.y + ") every " +curr.freq + " cycles.";  
            }
            
            return info;
        }
        
        protected String getBasicInfo(){
            String info = "";
            info = "Component Name: " + name + "\nRow: " + position.x + "\nColumn: " + position.y;
            info += "\n\nThis component is sending messages to the following components:";
            return info;
        }
	
	protected void initializeDirections(){
		int i = 1;
		while (i < 5){
			directionValidity[i] = true;
			i++;
		}
		if (position.x == 0){
			directionValidity[UP] = false;
		}
		if (position.x == Network.size.x - 1){
			directionValidity[DOWN] = false;
		}
		if (position.y == 0){
			directionValidity[LEFT] = false;
		}
		if (position.y == Network.size.y - 1){
			directionValidity[RIGHT] = false;
		}
	}
	
	protected void initializeMessageQueues(){
		int i = 1;
		while (i < 5){
			messages[i] = new LinkedBlockingQueue<Message>();
			i++;
		}
	}
	
	protected void updateSenders(int cycle){
		Iterator<Sender> iterator = senders.iterator();
		Sender curr;
		while (iterator.hasNext()){
			curr = iterator.next();
			curr.update(cycle);
		}
	}
	
	protected void applyOutgoing(){
		if (outgoing[UP] != null && directionValidity[UP]){
                    //System.out.println("UP: " + position.x + " "  + position.y);
                    Network.network.components[position.x-1][position.y].handleMessage(outgoing[UP]);
		}
		if (outgoing[RIGHT] != null && directionValidity[RIGHT]){
                    //System.out.println("RIGHT: " + position.x + " "  + position.y);
                    Network.network.components[position.x][position.y+1].handleMessage(outgoing[RIGHT]);
		}
		if (outgoing[DOWN] != null && directionValidity[DOWN]){
                    //System.out.println("DOWN: " + position.x + " "  + position.y);
                    Network.network.components[position.x+1][position.y].handleMessage(outgoing[DOWN]);
		}
		if (outgoing[LEFT] != null && directionValidity[LEFT]){
                    //System.out.println("LEFT: " + position.x + " "  + position.y);
                    Network.network.components[position.x][position.y-1].handleMessage(outgoing[LEFT]);
		}
		outgoing[LEFT] = null;
		outgoing[UP] = null;
		outgoing[RIGHT] = null;
		outgoing[DOWN] = null;
	}
	
	protected void sendMessages(){
		Message curr;
		int i = 1;
		while (i <5){
			curr = messages[i].poll();
			sendMessage(curr,i);
			i++;
		}
	}
	
	protected void sendMessage(Message message, int direction){
		if (message == null){
			return;
		}
		outgoing[direction] = message;
	}
	
	protected void recieveMessage(Message message){
		
	}
}
