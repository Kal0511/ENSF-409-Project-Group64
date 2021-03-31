import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Filing extends Furniture {
    boolean rails = false;
    boolean drawers = false;
    boolean cabinet = false;
    boolean check = false;
    int numOfRails = 0;
    int numOfDrawers = 0;
    int numOfCabinet = 0;
    int minPrice = 9999999;
    int price = 0;
    int amount;
    String result = "";
    StringBuffer itemIDs = new StringBuffer("");
    LinkedList<Integer> prices = new LinkedList<Integer>();
    LinkedList<String> IDs = new LinkedList<String>();

    public Filing(int amount) {
        this.amount = amount;
    }

    public void checkRequest(LinkedList<String[]> results) {

        if (rails && drawers && cabinet && numOfRails >= amount && numOfDrawers >= amount && numOfCabinet >= amount) {

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

            boolean railsBefore = rails;
            boolean drawersBefore = drawers;
            boolean cabinetBefore = cabinet;
            int numOfRailsBefore = numOfRails;
            int numOfDrawersBefore = numOfDrawers;
            int numOfCabinetBefore = numOfCabinet;


            if (arr[2].equals("Y")) {
                this.rails = true;
                numOfRails++;
            }
            if (arr[3].equals("Y")) {
                this.drawers = true;
                numOfDrawers++;
            }
            if (arr[4].equals("Y")) {
                this.cabinet = true;
                numOfCabinet++;
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

            rails = railsBefore;
            drawers = drawersBefore;
            cabinet = cabinetBefore;
            numOfRails = numOfRailsBefore;
            numOfDrawers = numOfDrawersBefore;
            numOfCabinet = numOfCabinetBefore;
            price -= Integer.parseInt(results.get(i)[5]);
            itemIDs.delete(itemIDs.length() - results.get(i)[0].length() - 1, itemIDs.length());
        }
    }
    /*
    *checkPrices is a method with no arguments and no return type.
    * method goes through possible combinations and determines which one
    * is the cheapest. It then updates the resulsts string to represent the
    * combination the corresponds to that price.
    */ 
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
