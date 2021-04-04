import java.util.ArrayList;

/**
 * Desk is a class that determines the cheapest valid combination of all desks
 * of the specified type to fulfill the requested order.
 */

public class Desk extends Furniture{
    private int numOfLegs;
    private int numOfTop;
    private int numOfDrawer;

    public int getLegs() {
        return this.numOfLegs;
    }

    public int getTop() {
        return this.numOfTop;
    }

    public int getDrawer() {
        return this.numOfDrawer;
    }

    /**
     * Constructor.
     *
     * @param _ID ID number of piece
     * @param _legs number of legs
     * @param _top number of top
     * @param _drawer number of drawer
     * @param _price price of desk item
     */
    public Desk(String _ID, int _legs, int _top, int _drawer, int _price) {
        super(_ID, _price, Math.min(_legs, Math.min(_top, _drawer)));
        this.numOfLegs = _legs;
        this.numOfTop = _top;
        this.numOfDrawer = _drawer;
    }

    /**
     * simply adds piece of completed item order to requested item order.
     *
     * @param add desk object to be added
     * @return desk object with all pieces
     */
    public Desk addItem(Desk add) {
        Desk temp = new Desk(null, numOfLegs, numOfTop, numOfDrawer, totalPrice);
        temp.IDs = new ArrayList<>(IDs);
        temp.IDs.add(add.IDs.get(0));
        temp.numOfLegs += add.numOfLegs;
        temp.numOfTop += add.numOfTop;
        temp.numOfDrawer += add.numOfDrawer;
        temp.totalPrice += add.totalPrice;
        temp.completeSet = Math.min(temp.numOfLegs, Math.min(temp.numOfTop, temp.numOfDrawer));
        return temp;
    }

    /**
     * This method does initial checking of user input and calls
     * cheapestGroupRecursion() if user requested a valid number of items.
     *
     * @param list List of pieces needed to make desk item
     * @param requestSize User-requested amount
     * @return cheapest desk object
     */
    public static Desk processRequest(ArrayList<Desk> list, int requestSize) {
        if (requestSize == 0) {
            return null;
        }
        Desk cheapest = null;
        while (list.size() != 0) {
            Desk curr = list.get(0);
            list.remove(0);
            cheapest = cheapestGroupRecursion(new ArrayList<>(list), curr, cheapest, requestSize);
        }
//		if (cheapest == null) {
//			return null;
//		}
        //return new Furniture(cheapest.IDs,cheapest.totalPrice,cheapest.completeSet);
        return cheapest;
    }

    /**
     * This method iterates through possible combinations of pieces and determines
     * which combination is the cheapest. It returns the cheapest combination of
     * pieces, which is the complete item.
     *
     * @param list List of complete desk items
     * @param curr Current desk item
     * @param best Cheapest desk item
     * @param requestSize User-requested amount of item
     * @return returns desk object with cheapest price
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
            best = cheapestGroupRecursion(new ArrayList<>(list), temp, best, requestSize);
        }
        return best;
    }
}
