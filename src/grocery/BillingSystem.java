package grocery;
import javax.swing.SwingUtilities;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class BillingSystem extends JFrame {

    // Declare UI components
    private JTextField nameField, priceField, quantityField, totalField;
    private JTextArea billArea;
    private JButton addButton, calculateButton, saveButton;
    private Bill bill;

    public BillingSystem() {
        // Frame properties
        setTitle("Grocery Billing System");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize Bill
        bill = new Bill();

        // Initialize UI components
        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();
        totalField = new JTextField();
        totalField.setEditable(false);

        billArea = new JTextArea();
        billArea.setEditable(false);

        addButton = new JButton("Add Item");
        calculateButton = new JButton("Calculate Total");
        saveButton = new JButton("Save Bill");

        // Layout setup
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Item Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(addButton);
        inputPanel.add(calculateButton);
        inputPanel.add(new JLabel("Total Amount:"));
        inputPanel.add(totalField);

        JScrollPane scrollPane = new JScrollPane(billArea);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        // Add components to frame
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add event listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemToBill();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotal();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBillToDatabase();
            }
        });
    }

    private void addItemToBill() {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            Item item = new Item(name, price, quantity);
            bill.addItem(item);

            billArea.append(name + " | " + price + " | " + quantity + " | " + item.getTotal() + "\n");
            nameField.setText("");
            priceField.setText("");
            quantityField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for price and quantity.");
        }
    }

    private void calculateTotal() {
        totalField.setText(String.valueOf(bill.getTotalAmount()));
    }

    private void saveBillToDatabase() {
        try {
            DatabaseOperations.saveBill(bill);
            JOptionPane.showMessageDialog(this, "Bill saved successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving the bill: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BillingSystem().setVisible(true);
            }
        });
    }
}

