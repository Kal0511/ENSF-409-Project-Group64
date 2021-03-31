import java.util.ArrayList;

public class CheapestGroup {
	public static <T> T processRequest(ArrayList<T> list, int requestSize) {
		if (requestSize == 0) {
			return null;
		}
		T cheapest = null;
		while (list.size() != 0) {
			T curr = new T((T)list.get(0));
			list.remove(0);
			cheapest = cheapestGroupRecursion(new ArrayList<T>(list), curr, cheapest, requestSize);
		}
		return cheapest;
	}
	
	public static <T> T cheapestGroupRecursion(ArrayList<T> list, T curr, T best,
			int requestSize) {
		if (curr.getCompleteSet() >= requestSize) {
			if (best == null) {
				best = curr;
				return best;
			}
			if (curr.lessThan(best)) {
				best = curr;
				return best;
			}
		}
		if (best != null) {
			if (curr.moreThan(best)) {
				return best;
			}
		}
		while (list.size() != 0) {
			curr.addItem(list.get(0));
			list.remove(0);
			best = cheapestGroupRecursion(new ArrayList<T>(list), curr, best, requestSize);
		}
		return best;
	}
	
	public static FurnitureGroup processRequest(ArrayList<Desk> list, int requestSize) {
		if (requestSize == 0) {
			return null;
		}
		FurnitureGroup cheapest = null;
		while (list.size() != 0) {
			FurnitureGroup curr = new FurnitureGroup(list.get(0));
			list.remove(0);
			cheapest = cheapestGroupRecursion(new ArrayList<Desk>(list), curr, cheapest, requestSize);
		}
		return cheapest;
	}
	
	public static FurnitureGroup cheapestGroupRecursion(ArrayList<Desk> list, FurnitureGroup curr, FurnitureGroup best,
			int requestSize) {
		if (curr.getCompleteSet() >= requestSize) {
			if (best == null) {
				best = curr;
				return best;
			}
			if (curr.lessThan(best)) {
				best = curr;
				return best;
			}
		}
		if (best != null) {
			if (curr.moreThan(best)) {
				return best;
			}
		}
		while (list.size() != 0) {
			curr.addItem(list.get(0));
			list.remove(0);
			best = cheapestGroupRecursion(new ArrayList<Desk>(list), curr, best, requestSize);
		}
		return best;
	}
}

public class CheapestGroup<F, G> {
	
	private ArrayList<F> list;
	private G group;
	
	CheapestGroup(ArrayList<F> _list, int requestSize){
		list = _list;
		group = null;
	}
	
	public G processRequest(int requestSize) {
		if (requestSize == 0) {
			return null;
		}
		G cheapest = null;
		while (list.size() != 0) {
			G curr = new G(list.get(0));
			list.remove(0);
			cheapest = cheapestGroupRecursion(new ArrayList<F>(list), curr, cheapest, requestSize);
		}
		return cheapest;
	}
	
	public G cheapestGroupRecursion(ArrayList<F> list, G curr, G best,
			int requestSize) {
		if (curr.getCompleteSet() >= requestSize) {
			if (best == null) {
				best = curr;
				return best;
			}
			if (curr.lessThan(best)) {
				best = curr;
				return best;
			}
		}
		if (best != null) {
			if (curr.moreThan(best)) {
				return best;
			}
		}
		while (list.size() != 0) {
			curr.addItem(list.get(0));
			list.remove(0);
			best = cheapestGroupRecursion(new ArrayList<F>(list), curr, best, requestSize);
		}
		return best;
	}
}

public class CheapestGroup {
	public static <T, K> K processRequest(ArrayList<T> list, int requestSize) {
		if (requestSize == 0) {
			return null;
		}
		K cheapest = null;
		while (list.size() != 0) {
			K curr = new K(list.get(0));
			list.remove(0);
			cheapest = cheapestGroupRecursion(new ArrayList<T>(list), curr, cheapest, requestSize);
		}
		return cheapest;
	}
	public static <T, K> K cheapestGroupRecursion(ArrayList<T> arrayList, K curr, K best,
			int requestSize) {
		if (curr.getCompleteSet() >= requestSize) {
			if (best == null) {
				best = curr;
				return best;
			}
			if (curr.lessThan(best)) {
				best = curr;
				return best;
			}
		}
		if (best != null) {
			if (curr.moreThan(best)) {
				return best;
			}
		}
		while (arrayList.size() != 0) {
			curr.addFurniture(arrayList.get(0));
			arrayList.remove(0);
			best = cheapestGroupRecursion(new ArrayList<T>(arrayList), curr, best, requestSize);
		}
		return best;
	}
}
