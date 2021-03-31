import java.util.ArrayList;

public class Desk {
	private ArrayList<String> IDs;
	private int numOfLegs;
	private int numOfTop;
	private int numOfDrawer;
	private int totalPrice;
	private int completeSet;
	
	public ArrayList<String> getIDs() {
		return this.IDs;
	}

	public int getLegs() {
		return this.numOfLegs;
	}

	public int getTop() {
		return this.numOfTop;
	}
	
	public int getDrawer() {
		return this.numOfDrawer;
	}

	public int getPrice() {
		return this.totalPrice;
	}

	public Desk(String _ID, int _legs, int _top, int _drawer, int _price) {
		this.IDs = new ArrayList<String>();
		this.IDs.add(_ID);
		this.numOfLegs = _legs;
		this.numOfTop = _top;
		this.numOfDrawer = _drawer;
		this.totalPrice = _price;
		this.completeSet = Math.min(numOfLegs, Math.min(numOfTop, numOfDrawer));
	}
	
	public void addItem(Desk add) {
		if (IDs.contains(add.IDs.get(0))) {
			return;
		}
		IDs.add(add.IDs.get(0));
		numOfLegs += add.numOfLegs;
		numOfTop += add.numOfTop;
		numOfDrawer += add.numOfDrawer;
		totalPrice += add.totalPrice;
		completeSet = Math.min(numOfLegs, Math.min(numOfTop, numOfDrawer));
	}

	public static Desk processRequest(ArrayList<Desk> list, int requestSize) {
		if (requestSize == 0) {
			return null;
		}
		Desk cheapest = null;
		while (list.size() != 0) {
			Desk curr = list.get(0);
			list.remove(0);
			cheapest = cheapestGroupRecursion(new ArrayList<Desk>(list), curr, cheapest, requestSize);
		}
		if(cheapest==null) {
			return new Desk(null, 0, 0, 0, 0);
		}
		return cheapest;
	}

	public static Desk cheapestGroupRecursion(ArrayList<Desk> list, Desk curr, Desk best, int requestSize) {
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
			best = cheapestGroupRecursion(new ArrayList<Desk>(list), curr, best, requestSize);
		}
		return best;
	}
}