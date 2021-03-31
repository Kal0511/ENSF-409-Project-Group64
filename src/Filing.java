import java.util.ArrayList;

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

	public Filing(String _ID, int _rails, int _cabinet, int _drawer, int _price) {
		this.IDs = new ArrayList<String>();
		this.IDs.add(_ID);
		this.numOfRails = _rails;
		this.numOfCabinet = _cabinet;
		this.numOfDrawer = _drawer;
		this.totalPrice = _price;
		this.completeSet = Math.min(numOfCabinet, Math.min(numOfRails, numOfDrawer));
	}
	
	public void addItem(Filing add) {
		if (IDs.contains(add.IDs.get(0))) {
			return;
		}
		IDs.add(add.IDs.get(0));
		numOfRails += add.numOfRails;
		numOfCabinet += add.numOfCabinet;
		numOfDrawer += add.numOfDrawer;
		totalPrice += add.totalPrice;
		completeSet = Math.min(numOfCabinet, Math.min(numOfRails, numOfDrawer));
	}

	public static Filing processRequest(ArrayList<Filing> list, int requestSize) {
		if (requestSize == 0) {
			return null;
		}
		Filing cheapest = null;
		while (list.size() != 0) {
			Filing curr = list.get(0);
			list.remove(0);
			cheapest = cheapestGroupRecursion(new ArrayList<Filing>(list), curr, cheapest, requestSize);
		}
		if(cheapest==null) {
			return new Filing(null,  0,  0,  0,  0);
		}
		return cheapest;
	}

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
			curr.addItem(list.get(0));
			list.remove(0);
			best = cheapestGroupRecursion(new ArrayList<Filing>(list), curr, best, requestSize);
		}
		return best;
	}
}
