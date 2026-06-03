import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FormBayiler extends JPanel {

    private BayiDAL bayiDAL = new BayiDAL();
    private BayiBL bayiBL = new BayiBL();
    private int seciliId = 0;

    private JTextField txtAd = new JTextField();
    private JTextField txtYetkili = new JTextField();
    private JTextField txtTel = new JTextField();
    private JTextField txtAdres = new JTextField();
    private JTable table = new JTable();

    public FormBayiler() {
        setLayout(null);

        addLabel("Bayi Adı:", 20, 20);
        txtAd.setBounds(110, 20, 160, 25);
        add(txtAd);

        addLabel("Yetkili:", 20, 55);
        txtYetkili.setBounds(110, 55, 160, 25);
        add(txtYetkili);

        addLabel("Telefon:", 300, 20);
        txtTel.setBounds(390, 20, 160, 25);
        add(txtTel);

        addLabel("Adres:", 300, 55);
        txtAdres.setBounds(390, 55, 160, 25);
        add(txtAdres);

        JButton btnEkle = new JButton("Ekle");
        btnEkle.setBounds(570, 20, 100, 25);
        add(btnEkle);

        JButton btnGuncelle = new JButton("Güncelle");
        btnGuncelle.setBounds(570, 55, 100, 25);
        add(btnGuncelle);

        JButton btnSil = new JButton("Sil");
        btnSil.setBounds(680, 20, 90, 60);
        add(btnSil);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 100, 750, 380);
        add(sp);

        yenile();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    seciliId = Integer.parseInt(table.getValueAt(row, 0).toString());
                    txtAd.setText(table.getValueAt(row, 1).toString());
                    txtYetkili.setText(table.getValueAt(row, 2).toString());
                    txtTel.setText(table.getValueAt(row, 3).toString());
                    txtAdres.setText(table.getValueAt(row, 4).toString());
                }
            }
        });

        btnEkle.addActionListener(e -> {
            String msg = bayiBL.kontrol(txtAd.getText(), txtYetkili.getText(), txtTel.getText(), txtAdres.getText());
            if (!msg.equals("OK")) { JOptionPane.showMessageDialog(null, msg); return; }
            try {
                bayiDAL.ekle(txtAd.getText(), txtYetkili.getText(), txtTel.getText(), txtAdres.getText());
                JOptionPane.showMessageDialog(null, "Bayi eklendi.");
                yenile(); temizle();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
            }
        });

        btnGuncelle.addActionListener(e -> {
            if (seciliId == 0) { JOptionPane.showMessageDialog(null, "Tablodan bir bayi seçin."); return; }
            String msg = bayiBL.kontrol(txtAd.getText(), txtYetkili.getText(), txtTel.getText(), txtAdres.getText());
            if (!msg.equals("OK")) { JOptionPane.showMessageDialog(null, msg); return; }
            try {
                bayiDAL.guncelle(seciliId, txtAd.getText(), txtYetkili.getText(), txtTel.getText(), txtAdres.getText());
                JOptionPane.showMessageDialog(null, "Bayi güncellendi.");
                yenile(); temizle();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
            }
        });

        btnSil.addActionListener(e -> {
            if (seciliId == 0) { JOptionPane.showMessageDialog(null, "Tablodan bir bayi seçin."); return; }
            int onay = JOptionPane.showConfirmDialog(null, "Silmek istediğinizden emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);
            if (onay == JOptionPane.YES_OPTION) {
                try {
                    bayiDAL.sil(seciliId);
                    JOptionPane.showMessageDialog(null, "Bayi silindi.");
                    yenile(); temizle();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
                }
            }
        });
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 90, 25);
        add(lbl);
    }

    private void yenile() {
        try { table.setModel(bayiDAL.listele()); }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Bağlantı hatası: " + ex.getMessage()); }
    }

    private void temizle() {
        txtAd.setText(""); txtYetkili.setText(""); txtTel.setText(""); txtAdres.setText("");
        seciliId = 0;
    }
}