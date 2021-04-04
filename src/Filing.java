import java.util.ArrayList;

/**
 * Filing is a class that determines the cheapest valid combination of all
 * filings of the specified type to fulfill the requested order.
 */
public class Filing extends Furniture{
    private int numOfRails;
    private int numOfCabinet;
    private int numOfDrawer;

    public int getRails() {
        return this.numOfRails;
    }

    public int getCabinet() {
        return this.numOfCabinet;
    }

    public int getDrawer() {
        return this.numOfDrawer;
    }

    /**
     * Constructor.
     *
     * @param _ID ID number of piece
     * @param _rails number of rails
     * @param _cabinet number of cabinets
     * @param _drawer number of drawers
     * @param _price number of filing item
     */
    public Filing(String _ID, int _rails, int _cabinet, int _drawer, int _price) {
        super(_ID, _price, Math.min(_cabinet, Math.min(_rails, _drawer)));
        this.numOfRails = _rails;
        this.numOfCabinet = _cabinet;
        this.numOfDrawer = _drawer;
    }

    /**
     * simply adds piece of completed item order to requested item order.
     *
     * @param add Filing object to be added
     * @return Filing object with all pieces
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
     * @param list List of pieces needed to make filing item
     * @param requestSize filing object with all pieces
     * @return Cheapest filing object
     */
    public static Filing processRequest(ArrayList<Filing> list, int requestSize) {
        if (requestSize == 0) {
            return null;
        }
        Filing cheapest = null;
        while (list.size() != 0) {
            Filing curr = list.get(0);
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
     * @param list List of complete filing items
     * @param curr Current filing item
     * @param best Cheapest filing item
     * @param requestSize User-requested amount of item
     * @return returns filing object with cheapest price
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
