import java.util.ArrayList;

/*
* Desk is a class that determines the cheapest valid combination of all lamps 
* of the specified type to fulfill the order. 
*/

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

	public Desk addItem(Desk add) {
		Desk temp = new Desk(null, 	numOfLegs, numOfTop, numOfDrawer, totalPrice);
		temp.IDs = new ArrayList<String>(IDs);
//		if (IDs.contains(add.IDs.get(0))) {
//			return temp;
//		}
		temp.IDs.add(add.IDs.get(0));
		temp.numOfLegs += add.numOfLegs;
		temp.numOfTop += add.numOfTop;
		temp.numOfDrawer += add.numOfDrawer;
		temp.totalPrice += add.totalPrice;
		temp.completeSet = Math.min(temp.numOfLegs, Math.min(temp.numOfTop, temp.numOfDrawer));
		return temp;
	}

	/*
	 * checkRequest takes in a linked list of String arrays and has no return type.
	 * method uses recursion to determine any possible valid combinations of desks
	 * 
	 */
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
		if (cheapest == null) {
			return new Desk(null, 0, 0, 0, 0);
		}
		return cheapest;
	}

	/*
	 * checkPrices is a method with no arguments and no return type. method goes
	 * through possible combinations and determines which one is the cheapest. It
	 * then updates the results string to represent the combination the corresponds
	 * to that price.
	 */
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
			Desk temp = curr.addItem(list.get(0));
			list.remove(0);
			best = cheapestGroupRecursion(new ArrayList<Desk>(list), temp, best, requestSize);
		}
		return best;
	}
}