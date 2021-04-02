import java.util.ArrayList;

/**
 * Chair is a class that determines the cheapest valid combination of all chairs
 * of the specified type to fulfill the requested order.
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

	/**
	 * Constructor.
	 *
	 * @param _ID
	 * @param _legs
	 * @param _arms
	 * @param _seat
	 * @param _cushion
	 * @param _price
	 */
	public Chair(String _ID, int _legs, int _arms, int _seat, int _cushion, int _price) {
		this.IDs = new ArrayList<>();
		this.IDs.add(_ID);
		this.numOfLegs = _legs;
		this.numOfArms = _arms;
		this.numOfSeat = _seat;
		this.numOfCushion = _cushion;
		this.totalPrice = _price;
		this.completeSet = Math.min(Math.min(numOfLegs, numOfArms), Math.min(numOfSeat, numOfCushion));
	}

	/**
	 * simply adds piece of completed item order to requested item order.
	 *
	 * @param add
	 * @return
	 */
	public Chair addItem(Chair add) {
		Chair temp = new Chair(null, numOfLegs, numOfArms, numOfSeat, numOfCushion, totalPrice);
		temp.IDs = new ArrayList<>(IDs);
		temp.IDs.add(add.IDs.get(0));
		temp.numOfLegs += add.numOfLegs;
		temp.numOfArms += add.numOfArms;
		temp.numOfSeat += add.numOfSeat;
		temp.numOfCushion += add.numOfCushion;
		temp.totalPrice += add.totalPrice;
		temp.completeSet = Math.min(Math.min(temp.numOfLegs, temp.numOfArms),
				Math.min(temp.numOfSeat, temp.numOfCushion));
		return temp;
	}

	/**
	 * This method does initial checking of user input and calls
	 * cheapestGroupRecursion() if user requested a valid number of items.
	 *
	 * @param list
	 * @param requestSize
	 * @return
	 */
	public static Chair processRequest(ArrayList<Chair> list, int requestSize) {
		if (requestSize == 0) {
			return new Chair(null, 0, 0, 0, 0, 0);
		}
		Chair cheapest = null;
		while (list.size() != 0) {
			Chair curr = list.get(0);
			list.remove(0);
			cheapest = cheapestGroupRecursion(new ArrayList<>(list), curr, cheapest, requestSize);
		}
		if (cheapest == null) {
			return new Chair(null, 0, 0, 0, 0, 0);
		}
		return cheapest;
	}

	/**
	 * This method iterates through possible combinations of pieces and determines
	 * which combination is the cheapest. It returns the cheapest combination of
	 * pieces, which is the complete item.
	 *
	 * @param list
	 * @param curr
	 * @param best
	 * @param requestSize
	 * @return
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
			best = cheapestGroupRecursion(new ArrayList<>(list), temp, best, requestSize);
		}
		return best;
	}
}
