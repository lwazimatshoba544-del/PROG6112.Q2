package productapp;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ProductApp extends JFrame {

    private static final String FILE_NAME = "data.txt";

    private final JTextField txtCustomerName = new JTextField(20);
    private final JComboBox<String> cmbProduct = new JComboBox<>(new String[]{
            "Cozy Nights Monarch",
            "Restonic Barbados",
            "Sealy Phuket"
    });
    private final JTextField txtPrice = new JTextField(20);
    private final JButton btnProcess = new JButton("PROCESS");

    public ProductApp() {
        super("Product App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 350);
        setLocationRelativeTo(null);

        buildMenu();
        buildForm();
    }

    private void buildMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem processItem = new JMenuItem("Process");
        processItem.addActionListener(e -> processSale());
        JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.addActionListener(e -> clearForm());
        toolsMenu.add(processItem);
        toolsMenu.add(clearItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);
    }

    private void buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtCustomerName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Select Product:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(cmbProduct, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Enter Price:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtPrice, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        btnProcess.addActionListener(e -> processSale());
        panel.add(btnProcess, gbc);

        add(panel);
    }

    // Shared by the PROCESS button and the Tools > Process menu item
    private void processSale() {
        String customerName = txtCustomerName.getText().trim();
        String product = (String) cmbProduct.getSelectedItem();

        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the customer name.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(txtPrice.getText().trim());
            if (price <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price greater than 0.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        IProductCalculations calc = new ProductCalculations(price);
        double vat = calc.GetVat();
        double commission = calc.GetCommission();
        double total = calc.GetTotal();

        String message = "Customer Name: " + customerName + "\n"
                + "Product: " + product + "\n"
                + "VAT: R " + vat + "\n"
                + "COMMISSION: R " + commission + "\n"
                + "TOTAL: R " + total;

        JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);
        saveToFile(customerName, product, vat, commission, total);
    }

    private void saveToFile(String customerName, String product,
                            double vat, double commission, double total) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            out.println("DATA LOG");
            out.println("**************************************");
            out.println("Customer Name: " + customerName);
            out.println("Product: " + product);
            out.println("VAT: R " + vat);
            out.println("COMMISSION: R " + commission);
            out.println("TOTAL: R " + total);
            out.println("**************************************");
            out.println();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save to " + FILE_NAME + ": " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtCustomerName.setText("");
        txtPrice.setText("");
        cmbProduct.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductApp().setVisible(true));
    }
}

