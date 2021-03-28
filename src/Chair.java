import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Chair extends Furniture {
    boolean legs = false;
    boolean arms = false;
    boolean seat = false;
    boolean cushion = false;
    boolean check = false;
    int minPrice = 9999999;
    int price = 0;
    String result = "";
    StringBuffer itemIDs = new StringBuffer("");
    LinkedList<Integer> prices = new LinkedList<Integer>();
    LinkedList<String> IDs = new LinkedList<String>();

    public void checkRequest(LinkedList<String[]> results) {

        if (legs && arms && seat && cushion) {

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
            boolean armsBefore = arms;
            boolean seatBefore = seat;
            boolean cushionBefore = cushion;

            if (arr[2].equals("Y")) {
                this.legs = true;
            }
            if (arr[3].equals("Y")) {
                this.arms = true;
            }
            if (arr[4].equals("Y")) {
                this.seat = true;
            }
            if (arr[5].equals("Y")) {
                this.cushion = true;
            }

            LinkedList<String[]> resultsRecursion = new LinkedList<String[]>();

            for (int j = 0; j < results.size(); j++) {
                String[] copy = Arrays.copyOf(results.get(j), results.get(j).length);
                resultsRecursion.add(copy);
            }
            price += Integer.parseInt(results.get(i)[6]);
            itemIDs.append(" " + results.get(i)[0]);
            resultsRecursion.remove(i);
            checkRequest(resultsRecursion);

            legs = legsBefore;
            arms = armsBefore;
            seat = seatBefore;
            cushion = cushionBefore;
            price -= Integer.parseInt(results.get(i)[6]);
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
