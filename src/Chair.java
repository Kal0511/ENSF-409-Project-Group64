import java.util.ArrayList;

public class Chair{
    private ArrayList<String> IDs;
    private int numOfLegs;
    private int numOfArms;
    private int numOfSeat;
    private int numOfCushion;
	private int totalPrice;
	private int completeSet;
	
	public ArrayList<String> getIDs() {
		return this.IDs;
	}

	public int getLegs() {
		return this.numOfLegs;
	}

	public int getArms() {
		return this.numOfArms;
	}
	
	public int getSeat() {
		return this.numOfSeat;
	}
	
	public int getCushion() {
		return this.numOfCushion;
	}

	public int getPrice() {
		return this.totalPrice;
	}

	public Chair(String _ID, int _legs, int _arms, int _seat, int _cushion, int _price) {
		this.IDs = new ArrayList<String>();
		this.IDs.add(_ID);
		this.numOfLegs = _legs;
		this.numOfArms = _arms;
		this.numOfSeat = _seat;
		this.numOfCushion = _cushion;
		this.totalPrice = _price;
		this.completeSet = Math.min(Math.min(numOfLegs,numOfArms), Math.min(numOfSeat, numOfCushion));
	}
    
	public void addItem(Chair add) {
		if (IDs.contains(add.IDs.get(0))) {
			return;
		}
		IDs.add(add.IDs.get(0));
		numOfLegs += add.numOfLegs;
		numOfArms += add.numOfArms;
		numOfSeat += add.numOfSeat;
		numOfCushion += add.numOfCushion;
		totalPrice += add.totalPrice;
		completeSet = Math.min(Math.min(numOfLegs,numOfArms), Math.min(numOfSeat, numOfCushion));
	}

	public static Chair processRequest(ArrayList<Chair> list, int requestSize) {
		if (requestSize == 0) {
			return null;
		}
		Chair cheapest = null;
		while (list.size() != 0) {
			Chair curr = list.get(0);
			list.remove(0);
			cheapest = cheapestGroupRecursion(new ArrayList<Chair>(list), curr, cheapest, requestSize);
		}
		if(cheapest==null) {
			return new Chair(null,  0,  0,  0,0,  0);
		}
		return cheapest;
	}

	public static Chair cheapestGroupRecursion(ArrayList<Chair> list, Chair curr, Chair best, int requestSize) {
		if (curr.completeSet >= requestSize) {
			if (best == null) {
				best = curr;
				return best;
			}
			if (curr.totalPrice < best.totalPrice) {
				best = curr;
				return best;
			}
		}
		if (best != null) {
			if (curr.totalPrice > best.totalPrice) {
				return best;
			}
		}
		while (list.size() != 0) {
			curr.addItem(list.get(0));
			list.remove(0);
			best = cheapestGroupRecursion(new ArrayList<Chair>(list), curr, best, requestSize);
		}
		return best;
	}
}
