import java.util.ArrayList;

/**
 * Furniture is a parent class that contains variables common to all different
 * categories of furniture.
 */
public class Furniture {
	protected ArrayList<String> IDs;
	protected int totalPrice;
	protected int completeSet;

	/**
	 * Constructor
	 *
	 * @param _ID    ID number of piece
	 * @param _price price of piece
	 * @param set    number of items
	 */
	public Furniture(String _ID, int _price, int set) {
		this.IDs = new ArrayList<>();
		this.IDs.add(_ID);
		this.totalPrice = _price;
		this.completeSet = set;
	}

	/**
	 * Constructor
	 *
	 * @param _IDs   ID numbers of pieces
	 * @param _price price of piece
	 * @param set    number of items
	 */
	public Furniture(ArrayList<String> _IDs, int _price, int set) {
		this.IDs = _IDs;
		this.totalPrice = _price;
		this.completeSet = set;
	}

	/**
	 * Getter for IDS
	 *
	 * @return IDs
	 */
	public ArrayList<String> getIDs() {
		return this.IDs;
	}

	/**
	 * Getter for price
	 *
	 * @return price
	 */
	public int getPrice() {
		return this.totalPrice;
	}
}
