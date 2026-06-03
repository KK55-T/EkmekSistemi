import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ekmek Dağıtım ve Bayi Yönetim Sistemi");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(820, 560);
            frame.setLocationRelativeTo(null);

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Bayi Yönetimi", new FormBayiler());
            tabs.addTab("Ürün & Teslimat", new FormUrunTeslimat());

            frame.add(tabs);
            frame.setVisible(true);
        });
    }
}