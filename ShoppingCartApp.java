import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

// Main class representing the Shopping Cart Application
public class ShoppingCartApp {

    // MainFrame class extends JFrame for the main application window
    private static class MainFrame extends JFrame {
        private JTabbedPane tabbedPane;
        private LoginPanel loginPanel;
        private ShoppingCartPanel shoppingCartPanel;
        private HomePanel homePanel;

        public MainFrame() {
            super("Shopping Cart App");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 600);
            setLocationRelativeTo(null);

            tabbedPane = new JTabbedPane();

            // Create login panel
            loginPanel = new LoginPanel(this);

            // Add login panel to the tabbed pane
            tabbedPane.addTab("Login", loginPanel);

            add(tabbedPane);

            setVisible(true);
        }

        // Method to switch to the shopping cart view
        public void showShoppingCart(String username) {
            // Remove the login panel
            tabbedPane.remove(loginPanel);

            // Create and add the shopping cart panel
            shoppingCartPanel = new ShoppingCartPanel(username);
            tabbedPane.addTab("Shopping Cart", shoppingCartPanel);

            // Create and add the home panel
            homePanel = new HomePanel(shoppingCartPanel);
            tabbedPane.addTab("Home", homePanel);

            // Show the home tab
            tabbedPane.setSelectedComponent(homePanel);
        }
    }

    // LoginPanel class extends JPanel for the login functionality
    private static class LoginPanel extends JPanel {
        private JTextField usernameField;
        private JPasswordField passwordField;
        private MainFrame mainFrame;

        public LoginPanel(MainFrame mainFrame) {
            this.mainFrame = mainFrame;
            setLayout(new BorderLayout());

            // Input panel for username and password
            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

            JLabel usernameLabel = new JLabel("Username:");
            JLabel passwordLabel = new JLabel("Password:");

            usernameField = new JTextField();
            passwordField = new JPasswordField();

            inputPanel.add(usernameLabel);
            inputPanel.add(usernameField);
            inputPanel.add(passwordLabel);
            inputPanel.add(passwordField);

            // Button panel for login, cancel, and account type selection
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton loginButton = new JButton("Login");
            JButton cancelButton = new JButton("Cancel");
            JButton selectAccountTypeButton = new JButton("Select Account Type");

            // ActionListener for login button
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText();
                    char[] password = passwordField.getPassword();

                    if (authenticate(username, password)) {
                        mainFrame.showShoppingCart(username);
                    } else {
                        JOptionPane.showMessageDialog(LoginPanel.this, "Invalid credentials. Please try again.");
                    }
                }
            });

            // ActionListener for cancel button
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            // ActionListener for account type selection button
            selectAccountTypeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showAccountTypeDialog();
                }
            });

            buttonPanel.add(loginButton);
            buttonPanel.add(cancelButton);
            buttonPanel.add(selectAccountTypeButton);

            add(inputPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        // Placeholder authentication logic, replace with actual logic
        private boolean authenticate(String username, char[] password) {
            return "1".equals(username) && "1".equals(String.valueOf(password));
        }

        // Method to show account type selection dialog
        private void showAccountTypeDialog() {
            String[] options = { "Buyer", "Seller" };
            int choice = JOptionPane.showOptionDialog(LoginPanel.this,
                    "Select your account type:", "Account Type", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            // Handle the chosen account type
            if (choice == 0) {
                // Buyer selected
                // You can add buyer-specific logic here
            } else if (choice == 1) {
                // Seller selected
                // You can add seller-specific logic here
            }
        }
    }

    // ShoppingCartPanel class extends JPanel for the shopping cart functionality
    private static class ShoppingCartPanel extends JPanel {
        private DefaultTableModel tableModel;
        private JTable cartTable;
        private JComboBox<ProductItem> productsDropdown;
        private Map<String, ProductItem> availableProducts;

        public ShoppingCartPanel(String username) {
            setLayout(new BorderLayout());

            // Title label for the shopping cart
            JLabel titleLabel = new JLabel("Your Shopping Cart", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
            add(titleLabel, BorderLayout.NORTH);

            // Table to display the shopping cart items
            tableModel = new DefaultTableModel(new Object[] { "Product", "Quantity", "Price" }, 0);
            cartTable = new JTable(tableModel);
            cartTable.setFont(new Font("Arial", Font.PLAIN, 24));
            cartTable.setRowHeight(40);
            cartTable.setPreferredScrollableViewportSize(new Dimension(500, 300));

            JScrollPane scrollPane = new JScrollPane(cartTable);
            add(scrollPane, BorderLayout.CENTER);

            // Buttons for removing product and checkout
            JButton removeProductButton = new JButton("Remove Product");
            removeProductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = cartTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String productName = (String) tableModel.getValueAt(selectedRow, 0);
                        int removedQuantity = (int) tableModel.getValueAt(selectedRow, 1);

                        // Increment the available quantity in the store
                        ProductItem product = availableProducts.get(productName);
                        product.setQuantity(product.getQuantity() + removedQuantity);

                        // Remove the row from the table
                        tableModel.removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(ShoppingCartPanel.this, "Please select a product to remove.");
                    }
                }
            });

            JButton checkoutButton = new JButton("Checkout");
            checkoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double totalAmount = calculateTotalAmount();
                    showCheckoutDialog(username, totalAmount);
                }
            });

            // Panel for control buttons
            JPanel controlPanel = new JPanel(new FlowLayout());
            controlPanel.add(removeProductButton);
            controlPanel.add(checkoutButton);

            add(controlPanel, BorderLayout.SOUTH);

            // Map to store available products with their quantities
            availableProducts = new HashMap<>();
            availableProducts.put("Orange", new ProductItem("Orange", 1.99, "orange.jpg", 20));
            availableProducts.put("Apple", new ProductItem("Apple", 2.49, "apple.jpg", 20));
            availableProducts.put("Pear", new ProductItem("Pear", 1.79, "pear.jpg", 20));
            availableProducts.put("Grape", new ProductItem("Grape", 3.99, "grape.jpg", 20));
            availableProducts.put("Lychee", new ProductItem("Lychee", 0.50, "lychee.jpg", 20));
            availableProducts.put("Dragon Fruit", new ProductItem("Dragon Fruit", 4.99, "dragon.jpg", 20));
            availableProducts.put("Longan", new ProductItem("Longan", 2.49, "longan.jpg", 20));
        }

        // Method to add a product to the shopping cart
        public void addProductToCart(ProductItem product) {
            int rowIndex = findProductRowIndex(product.getName());

            if (rowIndex == -1 && product.getQuantity() > 0) {
                // Decrease the available quantity
                product.setQuantity(product.getQuantity() - 1);

                double price = product.getPrice();
                tableModel.addRow(new Object[] { product.getName(), 1, String.format("%.2f", price) });

                updateProductAvailabilityLabel(product);
            } else if (rowIndex != -1) {
                int currentQuantity = (int) tableModel.getValueAt(rowIndex, 1);
                int newQuantity = currentQuantity + 1;

                double productPrice = availableProducts.get(product.getName()).getPrice();
                double newTotal = newQuantity * productPrice;

                // Update the quantity and total amount with two decimal places
                tableModel.setValueAt(newQuantity, rowIndex, 1);
                tableModel.setValueAt(String.format("%.2f", newTotal), rowIndex, 2);

                // Decrease the available quantity
                product.setQuantity(product.getQuantity() - 1);

                updateProductAvailabilityLabel(product);
            } else {
                JOptionPane.showMessageDialog(ShoppingCartPanel.this, "Product out of stock.");
            }

            int lastRow = tableModel.getRowCount() - 1;
            Rectangle rect = cartTable.getCellRect(lastRow, 0, true);
            cartTable.scrollRectToVisible(rect);
            cartTable.setRowSelectionInterval(lastRow, lastRow);

            // Notify the user about the updated quantity
            JOptionPane.showMessageDialog(ShoppingCartPanel.this,
                    product.getName() + " added to the cart. Remaining quantity: " + product.getQuantity());
        }

        // Method to find the row index of a product in the table
        private int findProductRowIndex(String productName) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(productName)) {
                    return i;
                }
            }
            return -1;
        }

        // Method to calculate the total amount of the items in the shopping cart
        private double calculateTotalAmount() {
            double totalAmount = 0.0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String productName = (String) tableModel.getValueAt(i, 0);
                int quantity = (int) tableModel.getValueAt(i, 1);
                double productPrice = availableProducts.get(productName).getPrice();
                totalAmount += productPrice * quantity;
            }
            return totalAmount;
        }

        // Method to show the checkout dialog
        private void showCheckoutDialog(String username, double totalAmount) {
            JDialog checkoutDialog = new JDialog((Frame) null, "Checkout", true);
            checkoutDialog.setSize(300, 150);
            checkoutDialog.setLocationRelativeTo(this);

            JPanel panel = new JPanel(new GridLayout(3, 1));

            JLabel messageLabel = new JLabel("Checkout completed for " + username, SwingConstants.CENTER);
            JLabel amountLabel = new JLabel("Total Amount: $" + String.format("%.2f", totalAmount),
                    SwingConstants.CENTER);

            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    checkoutDialog.dispose(); // close the dialog
                }
            });

            panel.add(messageLabel);
            panel.add(amountLabel);
            panel.add(okButton);

            checkoutDialog.add(panel);
            checkoutDialog.setVisible(true);
        }

        // Method to update the availability label for a product
        private void updateProductAvailabilityLabel(ProductItem product) {
            int availableQuantity = product.getQuantity();
            String availabilityText = "Available: " + availableQuantity;
            // Update the availability label for the corresponding product
            // (you need to implement a way to associate components with products)
        }
    }

    // HomePanel class extends JPanel for the home view
    private static class HomePanel extends JPanel {
        private ShoppingCartPanel shoppingCartPanel;

        public HomePanel(ShoppingCartPanel shoppingCartPanel) {
            this.shoppingCartPanel = shoppingCartPanel;
            setLayout(new BorderLayout());

            // Title label for the home panel
            JLabel titleLabel = new JLabel("Shopping Application", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
            add(titleLabel, BorderLayout.NORTH);

            // Panel to display products available for purchase
            JPanel productPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns, adjust as needed

            ProductItem[] products = {
                    new ProductItem("Orange", 1.99, "orange.jpg", 20),
                    new ProductItem("Apple", 2.49, "apple.jpg", 20),
                    new ProductItem("Pear", 1.79, "pear.jpg", 20),
                    new ProductItem("Grape", 3.99, "grape.jpg", 20),
                    new ProductItem("Lychee", 0.50, "lychee.jpg", 20),
                    new ProductItem("Dragon Fruit", 3.99, "dragon.jpg", 20),
                    new ProductItem("Longan", 2.49, "longan.jpg", 20),
            };

            for (ProductItem product : products) {
                JButton addToCartButton = new JButton("Add to Cart");
                JLabel availabilityLabel = new JLabel("Available: " + product.getQuantity(), SwingConstants.CENTER);

                addToCartButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        shoppingCartPanel.addProductToCart(product);
                        availabilityLabel.setText("Available: " + product.getQuantity());
                    }
                });

                try {
                    // Load the image using ImageIO
                    Image image = new ImageIcon(getClass().getResource(product.getImagePath())).getImage();
                    // Resize the image to a smaller size
                    Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    ImageIcon resizedIcon = new ImageIcon(resizedImage);

                    JLabel imageLabel = new JLabel(resizedIcon, SwingConstants.CENTER);

                    // Set a larger font size for the product label
                    Font productFont = new Font("Arial", Font.PLAIN, 20);

                    // Create a JLabel for the product with HTML formatting to center text
                    JLabel productLabel = new JLabel("<html><center>" + product.getName() + "<br> $"
                            + String.format("%.2f", product.getPrice()) + "</center></html>", SwingConstants.CENTER);

                    // Create a JPanel for the product button and label with a vertical layout
                    JPanel productButtonPanel = new JPanel(new GridLayout(2, 1));
                    productButtonPanel.add(imageLabel); // Add the product image label to the panel
                    productButtonPanel.add(productLabel); // Add the product label to the panel

                    // Create a JPanel for buttons and availability label using a border layout
                    JPanel buttonPanel = new JPanel(new BorderLayout());
                    buttonPanel.add(addToCartButton, BorderLayout.NORTH); // Add the "Add to Cart" button
                    buttonPanel.add(availabilityLabel, BorderLayout.SOUTH); // Add the availability label

                    // Create a combined panel with a border layout for the product and button
                    // panels
                    JPanel combinedPanel = new JPanel(new BorderLayout());
                    combinedPanel.add(productButtonPanel, BorderLayout.CENTER); // Add product button panel to the
                                                                                // center
                    combinedPanel.add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the botto

                    productPanel.add(combinedPanel); // Add the combined panel to the product panel
                } catch (Exception ex) {
                    ex.printStackTrace(); // Print stack trace if an exception occurs
                }
            }

            // Create a scroll pane for the product panel
            JScrollPane productScrollPane = new JScrollPane(productPanel);
            // Add the scroll pane to the main frame's center area
            add(productScrollPane, BorderLayout.CENTER);
        }
    }

    // Definition of a ProductItem class representing a product in the shopping
    // application
    private static class ProductItem {
        private String name;
        private double price;
        private String imagePath;
        private int quantity;

        // Constructor to initialize a ProductItem object with the given parameters

        public ProductItem(String name, double price, String imagePath, int quantity) {
            this.name = name;
            this.price = price;
            this.imagePath = imagePath;
            this.quantity = quantity;
        }

        // Getter method for retrieving the name of the product
        public String getName() {
            return name;
        }

        // Getter method for retrieving the price of the product
        public double getPrice() {
            return price;
        }

        // Getter method for retrieving the image path of the product
        public String getImagePath() {
            return imagePath;
        }

        // Getter method for retrieving the quantity of the product
        public int getQuantity() {
            return quantity;
        }

        // Setter method for updating the quantity of the product
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        // Override toString to provide a string representation of the product
        @Override
        public String toString() {
            return name + " - $" + String.format("%.2f", price);
        }
    }

    // Main method to launch the shopping application using Swing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create a new instance of the MainFrame to start the application
                new MainFrame();
            }
        });
    }
}
