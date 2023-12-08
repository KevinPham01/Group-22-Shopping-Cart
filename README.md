# Group-22-Shopping-Cart

The ShoppingCartApp is a Java-based desktop application designed to simulate a shopping cart system. It is built using Java Swing for the graphical user interface. The application features a login system, a product catalog, and a shopping cart where users can add and remove items.

# Features

Login System: Allows users to login to the application. The authentication is placeholder and should be replaced with actual logic.
Product Catalog: Displays a range of products with their prices and availability. Users can add products to their shopping cart.
Shopping Cart: Users can view the products they've added, change quantities, remove items, and proceed to checkout.

# Classes and Their Functions

MainFrame Class: The main window of the application. It manages different panels (Login, ShoppingCart, and Home) using a JTabbedPane.
LoginPanel Class: A panel for user login. It validates user credentials and transitions to the shopping cart upon successful login.
ShoppingCartPanel Class: Displays the shopping cart. It allows users to add or remove products, and view the total amount.
HomePanel Class: Shows the product catalog. Users can browse products and add them to their cart.
ProductItem Class: Represents a product with properties like name, price, image path, and quantity.

# Usage

Starting the Application: Run main method in ShoppingCartApp. It initializes the MainFrame.
Login: Enter credentials in the login panel. Currently, it accepts '1' as both username and password.
Browsing Products: In the Home panel, browse through available products and click "Add to Cart" to add items to your cart.
Managing Cart: In the ShoppingCart panel, view added items, remove products, or proceed to checkout.

# Requirements

Java Development Kit (JDK)
Java Swing for the GUI components

# Installation

Clone the repository or download the source code.
Compile and run the ShoppingCartApp class using a Java IDE or command line.
