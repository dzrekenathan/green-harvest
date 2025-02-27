package com.exams.farmingsystem.examsproject;


import com.exams.farmingsystem.examsproject.models.Product;
import com.exams.farmingsystem.examsproject.models.Sales;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Logger;

public class DatabaseClass {


    private static final String URL = "jdbc:mysql://localhost:3306/farmsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    private static Connection connection;


    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established.");
            createSalesTable();
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe("Failed to connect to database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static Connection getConnection() {
        return connection;
    }

    public static void writeToDatabase(String tfFirstname, String tfLastname, String tfEmail, String tfPassword) {

        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS task_user (
                id SERIAL PRIMARY KEY,
                firstname VARCHAR(50),
                lastname VARCHAR(50),
                email VARCHAR(100) UNIQUE,
                password VARCHAR(100)
            )
        """;
        String insertQuery = "INSERT INTO task_user(firstname, lastname, email, password) VALUES(?, ?, ?, ?)";

        try (PreparedStatement createTableStmt = connection.prepareStatement(createTableQuery)) {
            createTableStmt.executeUpdate();
            System.out.println("Table ensured to exist.");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe("Error creating table: " + e.getMessage());
        }

        try (PreparedStatement pst = connection.prepareStatement(insertQuery)) {
            pst.setString(1, tfFirstname);
            pst.setString(2, tfLastname);
            pst.setString(3, tfEmail);
            pst.setString(4, tfPassword);
            pst.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Data successfully saved to the database!");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Failed", "Could not save data: " + e.getMessage());
        }
    }

    public static boolean loginUser(String email, String password) {
        String query = "SELECT * FROM task_user WHERE email = ? AND password = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful!");
                return true;
            } else {
                //showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
                return false;
            }
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe("Error during login: " + e.getMessage());
            //showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
        return false;
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void addProduct(String productName, double unitPrice, int quantity, String unit) {
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS product (
                product_id SERIAL PRIMARY KEY,
                product_name VARCHAR(100),
                unit_price DECIMAL(10, 2),
                quantity INT,
                unit VARCHAR(50),
            )
        """;
        String insertQuery = "INSERT INTO product(product_name, unit_price, quantity, unit) VALUES(?, ?, ?, ?)";

        try (PreparedStatement createTableStmt = connection.prepareStatement(createTableQuery)) {
            createTableStmt.executeUpdate();
            System.out.println("Product table ensured to exist.");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe("Error creating product table: " + e.getMessage());
        }

        try (PreparedStatement pst = connection.prepareStatement(insertQuery)) {
            pst.setString(1, productName);
            pst.setDouble(2, unitPrice);
            pst.setInt(3, quantity);
            pst.setString(4, unit);
            pst.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product successfully added to the database!");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Failed", "Could not add product: " + e.getMessage());
        }
    }

//    public static void updateProduct(int productId, String productName, double unitPrice, int quantity, String unit) {
//        String updateQuery = "UPDATE product SET product_name = ?, unit_price = ?, quantity = ?, unit = ? WHERE product_id = ?";
//
//        try (PreparedStatement pst = connection.prepareStatement(updateQuery)) {
//            pst.setString(1, productName);
//            pst.setDouble(2, unitPrice);
//            pst.setInt(3, quantity);
//            pst.setString(4, unit);
//            pst.setInt(6, productId);
//            pst.executeUpdate();
//            showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully!");
//        } catch (SQLException e) {
//            Logger.getLogger(DatabaseClass.class.getName()).severe("Error updating product: " + e.getMessage());
//            showAlert(Alert.AlertType.ERROR, "Failed", "Could not update product: " + e.getMessage());
//        }
//    }

    public static boolean recordSale(int productId, int quantity, double amount) {
        String insertQuery = "INSERT INTO sales (product_id, sale_date, quantity, amount) VALUES (?, ?, ?, ?)";
        String updateQuery = "UPDATE product SET quantity = quantity - ? WHERE product_id = ?";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            insertStmt.setInt(1, productId);
            insertStmt.setString(2, LocalDate.now().toString());
            insertStmt.setInt(3, quantity);
            insertStmt.setDouble(4, amount);
            insertStmt.executeUpdate();

            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, productId);
            updateStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe("Error recording sale: " + e.getMessage());
            return false;
        }
    }

    public static ObservableList<Product> fetchProductsForSale() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "SELECT product_id, product_name, quantity FROM product";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                int quantity = rs.getInt("quantity");

                products.add(new Product(productId, productName, quantity));
            }
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe("Error fetching products for sale: " + e.getMessage());
        }

        return products;
    }


    public static ObservableList<Product> fetchProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "SELECT * FROM product";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double unitPrice = rs.getDouble("unit_price");
                int quantity = rs.getInt("quantity");
                String unit = rs.getString("unit");

                products.add(new Product(productId, productName, unitPrice, quantity, unit));
            }
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe("Error fetching products: " + e.getMessage());
        }

        return products;
    }

//    public static ObservableList<Sales> fetchAllSales() {
//        ObservableList<Sales> sales = FXCollections.observableArrayList();
//        String query = """
//        SELECT s.sale_id, s.product_id, p.product_name, s.sale_date, s.quantity, s.amount
//        FROM sales s
//        JOIN product p ON s.product_id = p.product_id
//    """;
//
//        try (PreparedStatement pst = connection.prepareStatement(query)) {
//            ResultSet rs = pst.executeQuery();
//
//            while (rs.next()) {
//                int saleId = rs.getInt("sale_id");
//                int productId = rs.getInt("product_id");
//                String productName = rs.getString("product_name");
//                LocalDate saleDate = LocalDate.parse(rs.getString("sale_date"));
//                int quantity = rs.getInt("quantity");
//                double amount = rs.getDouble("amount");
//
//                sales.add(new Sales(saleId, productId, productName, saleDate, quantity, amount));
//            }
//        } catch (SQLException e) {
//            Logger.getLogger(DatabaseClass.class.getName()).severe("Error fetching sales: " + e.getMessage());
//        }
//
//        return sales;
//    }

    public static void createSalesTable() {
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS sales (
                sale_id SERIAL PRIMARY KEY,
                product_id INT REFERENCES product(product_id) ON DELETE CASCADE,
                sale_date VARCHAR(255) NOT NULL,
                quantity INT NOT NULL,
                amount DECIMAL(10, 2) NOT NULL
            )
        """;
        try (PreparedStatement createTableStmt = connection.prepareStatement(createTableQuery)) {
            createTableStmt.executeUpdate();
            System.out.println("Sales table ensured to exist.");
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe("Error creating sales table: " + e.getMessage());
        }
    }


    public static ObservableList<Sales> fetchAllSales() {
        ObservableList<Sales> sales = FXCollections.observableArrayList();
        String query = """
        SELECT s.sale_id, s.product_id, p.product_name, s.sale_date, s.quantity, s.amount
        FROM sales s
        JOIN product p ON s.product_id = p.product_id
    """;

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int saleId = rs.getInt("sale_id");
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                LocalDate saleDate = LocalDate.parse(rs.getString("sale_date"));
                int quantity = rs.getInt("quantity");
                double amount = rs.getDouble("amount");

                sales.add(new Sales(saleId, productId, productName, saleDate, quantity, amount));
            }
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe("Error fetching sales: " + e.getMessage());
        }

        return sales;
    }

    public static void updateProduct(int productId, String productName, double unitPrice, int quantity, String unit) {
        String updateQuery = "UPDATE product SET product_name = ?, unit_price = ?, quantity = ?, unit = ? WHERE product_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(updateQuery)) {
            pst.setString(1, productName);
            pst.setDouble(2, unitPrice);
            pst.setInt(3, quantity);
            pst.setString(4, unit);
            pst.setInt(5, productId); // Fixed: was 6, should be 5
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully!");
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "No product found with ID: " + productId);
            }
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe("Error updating product: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Failed", "Could not update product: " + e.getMessage());
        }
    }

    public static boolean deleteProduct(int productId) {
        String deleteQuery = "DELETE FROM product WHERE product_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(deleteQuery)) {
            pst.setInt(1, productId);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            Logger.getLogger(DatabaseClass.class.getName()).severe("Error deleting product: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Failed", "Could not delete product: " + e.getMessage());
            return false;
        }
    }
}

