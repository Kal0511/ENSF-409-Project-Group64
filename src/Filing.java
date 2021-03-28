  import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Filing extends Furniture {
    boolean rails = false;
    boolean drawers = false;
    boolean cabinet = false;
    boolean check = false;
    int minPrice = 9999999;
    int price = 0;
    String result = "";
    StringBuffer itemIDs = new StringBuffer("");
    LinkedList<Integer> prices = new LinkedList<Integer>();
    LinkedList<String> IDs = new LinkedList<String>();

    public void checkRequest(LinkedList<String[]> results) {

        if (rails && drawers && cabinet) {
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

            if (arr[2].equals("Y")) {
                this.rails = true;
            }
            if (arr[3].equals("Y")) {
                this.drawers = true;
            }
            if (arr[4].equals("Y")) {
                this.cabinet = true;
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
        result += IDs.get(tmp).substring(1);
    }
}
