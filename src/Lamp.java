import java.util.ArrayList;

/**
 * Lamp is a class that determines the cheapest valid combination of all lamps
 * of the specified type to fulfill the requested order.
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

    /**
     * Constructor.
     *
     * @param _ID
     * @param _base
     * @param _bulb
     * @param _price
     */
    public Lamp(String _ID, int _base, int _bulb, int _price) {
        this.IDs = new ArrayList<>();
        this.IDs.add(_ID);
        this.numOfBase = _base;
        this.numOfBulb = _bulb;
        this.totalPrice = _price;
        this.completeSet = Math.min(numOfBase, numOfBulb);
    }

    /**
     * simply adds piece of completed item order to requested item order.
     *
     * @param add
     * @return
     */
    public Lamp addItem(Lamp add) {
//		if (IDs.contains(add.IDs.get(0))) {
//			return;
//		}
        Lamp temp = new Lamp(null, numOfBase, numOfBulb, totalPrice);
        temp.IDs = new ArrayList<>(IDs);
        temp.IDs.add(add.IDs.get(0));
        temp.numOfBase += add.numOfBase;
        temp.numOfBulb += add.numOfBulb;
        temp.totalPrice += add.totalPrice;
        temp.completeSet = Math.min(temp.numOfBase, temp.numOfBulb);
        return temp;
    }

    /**
     * This method does initial checking of user input and calls cheapestGroupRecursion() if user requested
     * a valid number of items.
     *
     * @param list
     * @param requestSize
     * @return
     */
    public static Lamp processRequest(ArrayList<Lamp> list, int requestSize) {
        if (requestSize == 0) {
            return new Lamp(null, 0, 0, 0);
        }
        Lamp cheapest = null;
        while (list.size() != 0) {
            Lamp curr = list.get(0);
            list.remove(0);
            cheapest = cheapestGroupRecursion(new ArrayList<>(list), curr, cheapest, requestSize);
        }
        if (cheapest == null) {
            return new Lamp(null, 0, 0, 0);
        }
        return cheapest;
    }

    /**
     * This method iterates through possible combinations of pieces and determines which combination is the cheapest.
     * It returns the cheapest combination of pieces, which is the complete item.
     *
     * @param list
     * @param curr
     * @param best
     * @param requestSize
     * @return
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
