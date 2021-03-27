import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class Chair extends Furniture {
        boolean legs;
        boolean arms;
        boolean seat;
        boolean cushion;
        LinkedList <String[]> result;
        
        public Chair() {
        	 result = new LinkedList<String[]>();
        }
        
        public String checkRequest(ResultSet resultSet) throws SQLException {
        	while (resultSet.next()){
        		String[] arr = {resultSet.getString("ID"),resultSet.getString("Type"),
        				resultSet.getString("Legs"),resultSet.getString("Arms"),resultSet.getString("Seat"),
        				resultSet.getString("Cushion"),resultSet.getString("Price"),resultSet.getString("ManuID")};
        		result.add(arr);
        		System.out.println(arr[0]);

        		System.out.println("asds");
            }
        	return "";
        }
    }
