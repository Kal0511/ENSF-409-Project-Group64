import java.util.ArrayList;

/**
 * Lamp is a class that determines the cheapest valid combination of all lamps
 * of the specified type to fulfill the requested order.
 */
public class Lamp extends Furniture{
    private int numOfBase;
    private int numOfBulb;

    public int getBase() {
        return this.numOfBase;
    }

    public int getBulb() {
        return this.numOfBulb;
    }

    /**
     * Constructor.
     *
     * @param _ID ID of piece
     * @param _base Whether piece has base or not
     * @param _bulb Whether piece has base or not
     * @param _price Whether piece has base or not
     */
    public Lamp(String _ID, int _base, int _bulb, int _price) {
        super(_ID, _price, Math.min(_base, _bulb));
        this.numOfBase = _base;
        this.numOfBulb = _bulb;
    }

    /**
     * simply adds piece of completed item order to requested item order.
     *
     * @param add Lamp object to be added
     * @return Lamp object with all pieces
     */
    public Lamp addItem(Lamp add) {
        Lamp temp = new Lamp(null, numOfBase, numOfBulb, totalPrice);
        temp.IDs = new ArrayList<String>(IDs);
        temp.IDs.add(add.IDs.get(0));
        temp.numOfBase += add.numOfBase;
        temp.numOfBulb += add.numOfBulb;
        temp.totalPrice += add.totalPrice;
        temp.completeSet = Math.min(temp.numOfBase, temp.numOfBulb);
        return temp;
    }

    /**
     * This method does initial checking of user input and calls
     * cheapestGroupRecursion() if user requested a valid number of items.
     *
     * @param list List of pieces needed to make lamp item
     * @param requestSize User-requested amount of item
     * @return Cheapest lamp item
     */
    public static Lamp processRequest(ArrayList<Lamp> list, int requestSize) {
        if (requestSize == 0) {
            return null;
        }
        Lamp cheapest = null;
        while (list.size() != 0) {
            Lamp curr = list.get(0);
            list.remove(0);
            cheapest = cheapestGroupRecursion(new ArrayList<>(list), curr, cheapest, requestSize);
        }
//		if (cheapest == null) {
//			return null;
//		}
//		return new Furniture(cheapest.IDs, cheapest.totalPrice, cheapest.completeSet);
        return cheapest;
    }

    /**
     * This method iterates through possible combinations of pieces and determines
     * which combination is the cheapest. It returns the cheapest combination of
     * pieces, which is the complete item.
     *
     * @param list List of complete lamp items
     * @param curr Current lamp item
     * @param best Cheapest lamp item
     * @param requestSize User-requested amount of item
     * @return returns lamp object with cheapest price
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
            Lamp temp = curr.addItem(list.get(0));
            list.remove(0);
            best = cheapestGroupRecursion(new ArrayList<>(list), temp, best, requestSize);
        }
        return best;
    }
}
