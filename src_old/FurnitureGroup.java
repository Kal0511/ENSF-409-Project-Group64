import java.util.ArrayList;

public class FurnitureGroup {
	private ArrayList<String> IDs;
	private int[] numOfParts;
	private int totalPrice;
	private int completeSet;

	public FurnitureGroup(Lamp add) {
		this.IDs = new ArrayList<String>();
		IDs.add(add.getID());
		numOfParts = new int[2];
		if (add.getBase()) {
			numOfParts[0] = 1;
		} else {
			numOfParts[0] = 0;
		}
		if (add.getBulb()) {
			numOfParts[1] = 1;
		} else {
			numOfParts[1] = 0;
		}
		totalPrice = add.getPrice();
		completeSet = Math.min(numOfParts[0], numOfParts[1]);
	}
	
	public FurnitureGroup(Desk add) {
		this.IDs = new ArrayList<String>();
		IDs.add(add.getID());
		numOfParts = new int[2];
		if (add.getBase()) {
			numOfParts[0] = 1;
		} else {
			numOfParts[0] = 0;
		}
		if (add.getBulb()) {
			numOfParts[1] = 1;
		} else {
			numOfParts[1] = 0;
		}
		totalPrice = add.getPrice();
		completeSet = Math.min(numOfParts[0], numOfParts[1]);
	}
	
	public FurnitureGroup(Lamp add) {
		this.IDs = new ArrayList<String>();
		IDs.add(add.getID());
		numOfParts = new int[2];
		if (add.getBase()) {
			numOfParts[0] = 1;
		} else {
			numOfParts[0] = 0;
		}
		if (add.getBulb()) {
			numOfParts[1] = 1;
		} else {
			numOfParts[1] = 0;
		}
		totalPrice = add.getPrice();
		completeSet = Math.min(numOfParts[0], numOfParts[1]);
	}
	
	public FurnitureGroup(Lamp add) {
		this.IDs = new ArrayList<String>();
		IDs.add(add.getID());
		numOfParts = new int[2];
		if (add.getBase()) {
			numOfParts[0] = 1;
		} else {
			numOfParts[0] = 0;
		}
		if (add.getBulb()) {
			numOfParts[1] = 1;
		} else {
			numOfParts[1] = 0;
		}
		totalPrice = add.getPrice();
		completeSet = Math.min(numOfParts[0], numOfParts[1]);
	}

	public void addItem(Lamp add) {
		if (IDs.contains(add.getID())) {
			return;
		}
		IDs.add(add.getID());
		if (add.getBase()) {
			numOfParts[0] += 1;
		}
		if (add.getBulb()) {
			numOfParts[1] += 1;
		}
		totalPrice += add.getPrice();
		completeSet = Math.min(numOfParts[0], numOfParts[1]);
	}

	public int getCompleteSet() {
		return this.completeSet;
	}

	public boolean lessThan(FurnitureGroup fg) {
		if (this.totalPrice < fg.totalPrice) {
			return true;
		} else {
			return false;
		}
	}

	public boolean moreThan(FurnitureGroup fg) {
		if (this.totalPrice > fg.totalPrice) {
			return true;
		} else {
			return false;
		}
	}
}
