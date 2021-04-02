import java.util.ArrayList;

/**
 * Filing is a class that determines the cheapest valid combination of all
 * filings of the specified type to fulfill the requested order.
 */
public class Filing {
	private ArrayList<String> IDs;
	private int numOfRails;
	private int numOfCabinet;
	private int numOfDrawer;
	private int totalPrice;
	private int completeSet;

	public ArrayList<String> getIDs() {
		return this.IDs;
	}

	public int getRails() {
		return this.numOfRails;
	}

	public int getCabinet() {
		return this.numOfCabinet;
	}

	public int getDrawer() {
		return this.numOfDrawer;
	}

	public int getPrice() {
		return this.totalPrice;
	}

	/**
	 * Constructor.
	 *
	 * @param _ID
	 * @param _rails
	 * @param _cabinet
	 * @param _drawer
	 * @param _price
	 */
	public Filing(String _ID, int _rails, int _cabinet, int _drawer, int _price) {
		this.IDs = new ArrayList<>();
		this.IDs.add(_ID);
		this.numOfRails = _rails;
		this.numOfCabinet = _cabinet;
		this.numOfDrawer = _drawer;
		this.totalPrice = _price;
		this.completeSet = Math.min(this.numOfCabinet, Math.min(this.numOfRails, this.numOfDrawer));
	}

	/**
	 * simply adds piece of completed item order to requested item order.
	 *
	 * @param add
	 * @return
	 */
	public Filing addItem(Filing add) {
		Filing temp = new Filing(null, numOfRails, numOfCabinet, numOfDrawer, totalPrice);
		temp.IDs = new ArrayList<>(IDs);
		temp.IDs.add(add.IDs.get(0));
		temp.numOfRails += add.numOfRails;
		temp.numOfCabinet += add.numOfCabinet;
		temp.numOfDrawer += add.numOfDrawer;
		temp.totalPrice += add.totalPrice;
		temp.completeSet = Math.min(temp.numOfCabinet, Math.min(temp.numOfRails, temp.numOfDrawer));
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
	public static Filing processRequest(ArrayList<Filing> list, int requestSize) {
		if (requestSize == 0) {
			return new Filing(null, 0, 0, 0, 0);
		}
		Filing cheapest = null;
		while (list.size() != 0) {
			Filing curr = list.get(0);
			list.remove(0);
			cheapest = cheapestGroupRecursion(new ArrayList<>(list), curr, cheapest, requestSize);
		}
		if (cheapest == null) {
			return new Filing(null, 0, 0, 0, 0);
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
	public static Filing cheapestGroupRecursion(ArrayList<Filing> list, Filing curr, Filing best, int requestSize) {
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
			Filing temp = curr.addItem(list.get(0));
			list.remove(0);
			best = cheapestGroupRecursion(new ArrayList<>(list), temp, best, requestSize);
		}
		return best;
	}
}
