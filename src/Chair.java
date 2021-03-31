import java.util.ArrayList;

/*
* Chair is a class that determines the cheapest valid combination of all lamps 
* of the specified type to fulfill the order. 
*/
public class Chair {
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
		this.completeSet = Math.min(Math.min(numOfLegs, numOfArms), Math.min(numOfSeat, numOfCushion));
	}

	public Chair addItem(Chair add) {
		Chair temp = new Chair(null, numOfLegs, numOfArms, numOfSeat, numOfCushion, totalPrice);
		temp.IDs = new ArrayList<String>(IDs);
//		if (IDs.contains(add.IDs.get(0))) {
//			return temp;
//		}
		temp.IDs.add(add.IDs.get(0));
		temp.numOfLegs += add.numOfLegs;
		temp.numOfArms += add.numOfArms;
		temp.numOfSeat += add.numOfSeat;
		temp.numOfCushion += add.numOfCushion;
		temp.totalPrice += add.totalPrice;
		temp.completeSet = Math.min(Math.min(temp.numOfLegs, temp.numOfArms), Math.min(temp.numOfSeat, temp.numOfCushion));
		return temp;
	}

	/*
	 * checkRequest takes in a linked list of String arrays and has no return type.
	 * method uses recursion to determine any possible valid combinations of chairs.
	 * 
	 */
	public static Chair processRequest(ArrayList<Chair> list, int requestSize) {
		if (requestSize == 0) {
			return new Chair(null, 0, 0, 0, 0, 0);
		}
		Chair cheapest = null;
		while (list.size() != 0) {
			Chair curr = list.get(0);
			list.remove(0);
			cheapest = cheapestGroupRecursion(new ArrayList<Chair>(list), curr, cheapest, requestSize);
		}
		if (cheapest == null) {
			return new Chair(null, 0, 0, 0, 0, 0);
		}
		return cheapest;
	}

	/*
	 * checkPrices is a method with no arguments and no return type. method goes
	 * through possible combinations and determines which one is the cheapest. It
	 * then updates the results string to represent the combination the corresponds
	 * to that price.
	 */
	public static Chair cheapestGroupRecursion(ArrayList<Chair> list, Chair curr, Chair best, int requestSize) {
		if (curr.completeSet >= requestSize) {
			if (best == null) {
				return curr;
			}
			if (curr.totalPrice < best.totalPrice) {
				return curr;
			}
		}
		if (best != null) {
			if (curr.totalPrice > best.totalPrice) {
				return best;
			}
		}
		while (list.size() != 0) {
			Chair temp = curr.addItem(list.get(0));
			list.remove(0);
			best = cheapestGroupRecursion(new ArrayList<Chair>(list), temp, best, requestSize);
		}
		return best;
	}
}
