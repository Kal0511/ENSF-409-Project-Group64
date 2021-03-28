import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Chair extends Furniture {
    boolean legs;
    boolean arms;
    boolean seat;
    boolean cushion;
    int minPrice = 9999999;

    public Chair() {
    }


//    public Set<String[]> calcAllCombos(Set<String[]> originalSet) {
//        Set<String[]>[] x;
//        Set<Set<String[]>> sets = new HashSet<Set<String[]>>();
//        if (originalSet.isEmpty())
//            return null;
//
//        List<String[]> list = new ArrayList<String[]>(originalSet);
//        String[] head = list.get(0);
//        Set<String[]> rest = new HashSet<String[]>(list.subList(1, list.size()));
//        for (Set<String[]> set : calcAllCombos(rest)) {
//            Set<String[]> newSet = new HashSet<String[]>();
//            newSet.add(head);
//            newSet.addAll(set);
//            sets.add(newSet);
//            sets.add(set);
//        }
//        for (int i = 0; i < sets.size(); i++)
//            if (checkValidCombo(sets.toArray(x)[i]))
//                return sets.toArray(x)[i];
//    }
//
//    public boolean checkValidCombo(Set<String[]> combo) {
//        boolean ifLegs = false;
//        boolean ifArms = false;
//        boolean ifSeat = false;
//        boolean ifCushion = false;
//        for (String[] x : combo) {
//            if (x[2] == "Y")
//                ifLegs = true;
//            if (x[3] == "Y")
//                ifArms = true;
//            if (x[4] == "Y")
//                ifSeat = true;
//            if (x[5] == "Y")
//                ifCushion = true;
//        }
//        int price = 0;
//        if (ifLegs && ifArms && ifSeat && ifCushion)
//            for (String[] x : combo)
//                price += Integer.parseInt(x[6]);
//        if (price < minPrice)
//            minPrice = price;
//        return ifLegs && ifArms && ifSeat && ifCushion;
//    }
}
