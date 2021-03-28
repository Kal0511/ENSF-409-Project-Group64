import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Desk extends Furniture {
    boolean legs = false;
    boolean top = false;
    boolean drawer = false;
    boolean check = false;
    int numOfLegs = 0;
    int numOfTop = 0;
    int numOfDrawer = 0;
    int minPrice = 9999999;
    int price = 0;
    int amount;
    String result = "";
    StringBuffer itemIDs = new StringBuffer("");
    LinkedList<Integer> prices = new LinkedList<Integer>();
    LinkedList<String> IDs = new LinkedList<String>();

    public Desk(int amount) {
        this.amount = amount;
    }

    public void checkRequest(LinkedList<String[]> results) {

        if (legs && top && drawer && numOfLegs >= amount && numOfTop >= amount && numOfDrawer >= amount) {

            this.prices.add(price);
            this.IDs.add(itemIDs.toString());
//    		System.out.println("combination found: "+itemIDs.toString());
            return;
        }
        if (results.size() < 1) {
//    		System.out.println("no combination found: "+itemIDs.toString());
            return;
        }

        for (int i = 0; i < results.size(); i++) {
            String[] arr = results.get(i);

            boolean legsBefore = legs;
            boolean topBefore = top;
            boolean drawerBefore = drawer;
            int numOfLegsBefore = numOfLegs;
            int numOfTopBefore = numOfTop;
            int numOfDrawerBefore = numOfDrawer;


            if (arr[2].equals("Y")) {
                this.legs = true;
                numOfLegs++;
            }
            if (arr[3].equals("Y")) {
                this.top = true;
                numOfTop++;
            }
            if (arr[4].equals("Y")) {
                this.drawer = true;
                numOfDrawer++;
            }

            LinkedList<String[]> resultsRecursion = new LinkedList<String[]>();

            for (int j = 0; j < results.size(); j++) {
                String[] copy = Arrays.copyOf(results.get(j), results.get(j).length);
                resultsRecursion.add(copy);
            }
            price += Integer.parseInt(results.get(i)[5]);
            itemIDs.append(" " + results.get(i)[0]);
            resultsRecursion.remove(i);
            checkRequest(resultsRecursion);

            legs = legsBefore;
            top = topBefore;
            drawer = drawerBefore;
            numOfLegs = numOfLegsBefore;
            numOfTop = numOfTopBefore;
            numOfDrawer = numOfDrawerBefore;
            price -= Integer.parseInt(results.get(i)[5]);
            itemIDs.delete(itemIDs.length() - results.get(i)[0].length() - 1, itemIDs.length());
        }
    }

    public void checkPrices() {
        int tmp = 0;
        for (int i = 0; i < prices.size(); i++) {
            //System.out.print(prices.get(i) + " ");
            if (prices.get(i) < minPrice) {
                minPrice = prices.get(i);
                tmp = i;
            }
        }
        if (!IDs.isEmpty()) {
            result += IDs.get(tmp).substring(1);
        }

    }
}
