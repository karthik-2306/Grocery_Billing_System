package grocery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseOperations {
    // Add new item to database
    public static void addItemToDatabase(Item item) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO items (name, price, quantity) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, item.getName());
            pst.setDouble(2, item.getPrice());
            pst.setInt(3, item.getQuantity());
            pst.executeUpdate();
        }
    }

    // Fetch all items from the database
    public static ResultSet getItemsFromDatabase() throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM items";
            PreparedStatement pst = con.prepareStatement(query);
            return pst.executeQuery();
        }
    }

    // Save the bill to the database
    public static void saveBill(Bill bill) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO bills (total_amount) VALUES (?)";
            PreparedStatement pst = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setDouble(1, bill.getTotalAmount());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                int billId = rs.getInt(1);
                for (Item item : bill.getItems()) {
                    saveBillItem(billId, item);
                }
            }
        }
    }

    // Save the items of the bill in the database
    private static void saveBillItem(int billId, Item item) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO bill_items (bill_id, item_name, price, quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, billId);
            pst.setString(2, item.getName());
            pst.setDouble(3, item.getPrice());
            pst.setInt(4, item.getQuantity());
            pst.executeUpdate();
        }
    }
}

