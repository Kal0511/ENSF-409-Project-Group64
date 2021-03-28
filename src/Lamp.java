import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Lamp extends Furniture {
    boolean base = false;
    boolean bulb = false;
    boolean check = false;
    int numOfBase = 0;
    int numOfBulb = 0;
    int minPrice = 9999999;
    int price = 0;
    int amount;
    String result = "";
    StringBuffer itemIDs = new StringBuffer("");
    LinkedList<Integer> prices = new LinkedList<Integer>();
    LinkedList<String> IDs = new LinkedList<String>();

    public Lamp(int amount) {
        this.amount = amount;
    }

    public void checkRequest(LinkedList<String[]> results) {

        if (base && bulb && numOfBase >= amount && numOfBulb >= amount) {

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

            boolean baseBefore = base;
            boolean bulbBefore = bulb;
            int numOfBaseBefore = numOfBase;
            int numOfBulbBefore = numOfBulb;


            if (arr[2].equals("Y")) {
                this.base = true;
                numOfBase++;
            }
            if (arr[3].equals("Y")) {
                this.bulb = true;
                numOfBulb++;
            }

            LinkedList<String[]> resultsRecursion = new LinkedList<String[]>();

            for (int j = 0; j < results.size(); j++) {
                String[] copy = Arrays.copyOf(results.get(j), results.get(j).length);
                resultsRecursion.add(copy);
            }
            price += Integer.parseInt(results.get(i)[4]);
            itemIDs.append(" " + results.get(i)[0]);
            resultsRecursion.remove(i);
            checkRequest(resultsRecursion);

            base = baseBefore;
            bulb = bulbBefore;
            numOfBase = numOfBaseBefore;
            numOfBulb = numOfBulbBefore;
            price -= Integer.parseInt(results.get(i)[4]);
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
