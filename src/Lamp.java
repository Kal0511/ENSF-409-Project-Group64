import java.util.ArrayList;

/*
* Lamp is a class that determines the cheapest valid combination of all lamps 
* of the specified type to fulfill the order. 
*/

public class Lamp {
	private ArrayList<String> IDs;
	private int numOfBase;
	private int numOfBulb;
	private int totalPrice;
	private int completeSet;

	public ArrayList<String> getIDs() {
		return this.IDs;
	}

	public int getBase() {
		return this.numOfBase;
	}

	public int getBulb() {
		return this.numOfBulb;
	}

	public int getPrice() {
		return this.totalPrice;
	}

	public Lamp(String _ID, int _base, int _bulb, int _price) {
		this.IDs = new ArrayList<String>();
		this.IDs.add(_ID);
		this.numOfBase = _base;
		this.numOfBulb = _bulb;
		this.totalPrice = _price;
		this.completeSet = Math.min(numOfBase, numOfBulb);
	}

	public void addItem(Lamp add) {
		if (IDs.contains(add.IDs.get(0))) {
			return;
		}
		IDs.add(add.IDs.get(0));
		numOfBase += add.numOfBase;
		numOfBulb += add.numOfBulb;
		totalPrice += add.totalPrice;
		completeSet = Math.min(numOfBase, numOfBulb);
	}

	/*
	 * checkRequest takes in a linked list of String arrays and has no return type.
	 * method uses recursion to determine any possible valid combinations of filings
	 * 
	 */
	public static Lamp processRequest(ArrayList<Lamp> list, int requestSize) {
		if (requestSize == 0) {
			return null;
		}
		Lamp cheapest = null;
		while (list.size() != 0) {
			Lamp curr = list.get(0);
			list.remove(0);
			cheapest = cheapestGroupRecursion(new ArrayList<Lamp>(list), curr, cheapest, requestSize);
		}
		if (cheapest == null) {
			return new Lamp(null, 0, 0, 0);
		}
		return cheapest;
	}

	/*
	 * checkPrices is a method with no arguments and no return type. method goes
	 * through possible combinations and determines which one is the cheapest. It
	 * then updates the resulsts string to represent the combination the corresponds
	 * to that price.
	 */
	public static Lamp cheapestGroupRecursion(ArrayList<Lamp> list, Lamp curr, Lamp best, int requestSize) {
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
			best = cheapestGroupRecursion(new ArrayList<Lamp>(list), curr, best, requestSize);
		}
		return best;
	}
}

//	public void printIDs() {
//		for (int i = 0; i < IDs.size(); i++) {
//			System.out.print(IDs.get(i) + " ");
//		}
//		System.out.println();
//		System.out.print(numOfBase + " : " + numOfBulb + " : " + completeSet + " : " + totalPrice);
//		System.out.println();
//	}
//public void addLamp(Lamp add) {
//if (IDs.contains(add.getID())) {
//	return;
//}
//IDs.add(add.getID());
//if (add.getBase()) {
//	numOfBase++;
//}
//if (add.getBulb()) {
//	numOfBulb++;
//}
//totalPrice += add.getPrice();
//completeSet = Math.min(numOfBase, numOfBulb);
//}
//
//public Lamp(String _ID, boolean _base, boolean _bulb, int _price) {
//this.ID = _ID;
//this.base = _base;
//this.bulb = _bulb;
//this.price = _price;
//}
//class LampGroup {
//private ArrayList<String> IDs;
//private int numOfBase;
//private int numOfBulb;
//private int totalPrice;
//private int completeSet;
//
////public LampGroup() {
////	this.IDs = new ArrayList<String>();
////	this.numOfBase = 0;
////	this.numOfBulb = 0;
////	this.totalPrice = 0;
////	this.completeSet = 0;
////}
//
//public int getCompleteSet() {
//	return this.completeSet;
//}
//
//public LampGroup(Lamp add) {
//	this.IDs = new ArrayList<String>();
//	IDs.add(add.getID());
//	if (add.getBase()) {
//		numOfBase = 1;
//	}else {
//		numOfBase = 0;
//	}
//	if (add.getBulb()) {
//		numOfBulb = 1;
//	}else {
//		numOfBulb = 0;
//	}
//	totalPrice = add.getPrice();
//	completeSet = Math.min(numOfBase, numOfBulb);
//}
//
//
//	public boolean lessThan(LampGroup lg) {
//		if (this.totalPrice < lg.totalPrice) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public boolean moreThan(LampGroup lg) {
//		if (this.totalPrice > lg.totalPrice) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public void printIDs() {
//		for(int i=0;i<IDs.size();i++) {
//			System.out.print(IDs.get(i) + " ");
//		}
//		System.out.println();
//		System.out.print(numOfBase + " : " + numOfBulb + " : " + completeSet + " : " + totalPrice);
//		System.out.println();
//	}
//}
