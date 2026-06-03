import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FormUrunTeslimat extends JPanel {

    private UrunDAL urunDAL = new UrunDAL();
    private UrunBL urunBL = new UrunBL();
    private BayiDAL bayiDAL = new BayiDAL();
    private TeslimatDAL teslimatDAL = new TeslimatDAL();
    private TeslimatBL teslimatBL = new TeslimatBL();

    private JTextField txtUrunAd = new JTextField();
    private JTextField txtFiyat = new JTextField();
    private JTextField txtStok = new JTextField();
    private JTable tableUrun = new JTable();

    private JComboBox<ComboItem> cbBayi = new JComboBox<>();
    private JComboBox<ComboItem> cbUrun = new JComboBox<>();
    private JTextField txtAdet = new JTextField();

    public FormUrunTeslimat() {
        setLayout(null);

        JLabel lblUrunBaslik = new JLabel("— Ürün Ekle —");
        lblUrunBaslik.setFont(new Font("Arial", Font.BOLD, 13));
        lblUrunBaslik.setBounds(20, 10, 200, 25);
        add(lblUrunBaslik);

        addLabel("Ürün Adı:", 20, 40);
        txtUrunAd.setBounds(110, 40, 150, 25);
        add(txtUrunAd);

        addLabel("Fiyat (₺):", 280, 40);
        txtFiyat.setBounds(370, 40, 100, 25);
        add(txtFiyat);

        addLabel("Stok:", 490, 40);
        txtStok.setBounds(560, 40, 80, 25);
        add(txtStok);

        JButton btnUrunEkle = new JButton("Ürün Ekle");
        btnUrunEkle.setBounds(660, 40, 110, 25);
        add(btnUrunEkle);

        JScrollPane spUrun = new JScrollPane(tableUrun);
        spUrun.setBounds(20, 80, 750, 180);
        add(spUrun);

        JSeparator sep = new JSeparator();
        sep.setBounds(20, 270, 750, 10);
        add(sep);

        JLabel lblTeslimatBaslik = new JLabel("— Teslimat Yap —");
        lblTeslimatBaslik.setFont(new Font("Arial", Font.BOLD, 13));
        lblTeslimatBaslik.setBounds(20, 285, 200, 25);
        add(lblTeslimatBaslik);

        addLabel("Bayi:", 20, 320);
        cbBayi.setBounds(110, 320, 200, 25);
        add(cbBayi);

        addLabel("Ürün:", 330, 320);
        cbUrun.setBounds(420, 320, 200, 25);
        add(cbUrun);

        addLabel("Adet:", 640, 320);
        txtAdet.setBounds(700, 320, 70, 25);
        add(txtAdet);

        JButton btnTeslimat = new JButton("Teslimat Yap");
        btnTeslimat.setBounds(300, 360, 180, 30);
        add(btnTeslimat);

        urunTablosunuYenile();
        comboYenile();

        btnUrunEkle.addActionListener(e -> {
            String msg = urunBL.kontrol(txtUrunAd.getText(), txtFiyat.getText(), txtStok.getText());
            if (!msg.equals("OK")) { JOptionPane.showMessageDialog(null, msg); return; }
            try {
                urunDAL.ekle(txtUrunAd.getText(), Float.parseFloat(txtFiyat.getText()), Integer.parseInt(txtStok.getText()));
                JOptionPane.showMessageDialog(null, "Ürün eklendi.");
                txtUrunAd.setText(""); txtFiyat.setText(""); txtStok.setText("");
                urunTablosunuYenile();
                comboYenile();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
            }
        });

        btnTeslimat.addActionListener(e -> {
            if (cbBayi.getSelectedItem() == null || cbUrun.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Bayi ve ürün seçilmeli.");
                return;
            }
            String msg = teslimatBL.kontrol(txtAdet.getText());
            if (!msg.equals("OK")) { JOptionPane.showMessageDialog(null, msg); return; }
            try {
                int bayiId = ((ComboItem) cbBayi.getSelectedItem()).id;
                int urunId = ((ComboItem) cbUrun.getSelectedItem()).id;
                int adet = Integer.parseInt(txtAdet.getText());
                teslimatDAL.teslimatYap(bayiId, urunId, adet);
                JOptionPane.showMessageDialog(null, "Teslimat başarıyla yapıldı.");
                txtAdet.setText("");
                urunTablosunuYenile();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage());
            }
        });
    }

    private void urunTablosunuYenile() {
        try { tableUrun.setModel(urunDAL.listele()); }
        catch (Exception ex) { JOptionPane.showMessageDialog(null, "Hata: " + ex.getMessage()); }
    }

    private void comboYenile() {
        try {
            cbBayi.removeAllItems();
            javax.swing.table.DefaultTableModel bModel = bayiDAL.listele();
            for (int i = 0; i < bModel.getRowCount(); i++)
                cbBayi.addItem(new ComboItem(Integer.parseInt(bModel.getValueAt(i, 0).toString()), bModel.getValueAt(i, 1).toString()));

            cbUrun.removeAllItems();
            javax.swing.table.DefaultTableModel uModel = urunDAL.listele();
            for (int i = 0; i < uModel.getRowCount(); i++)
                cbUrun.addItem(new ComboItem(Integer.parseInt(uModel.getValueAt(i, 0).toString()), uModel.getValueAt(i, 1).toString()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Combo yükleme hatası: " + ex.getMessage());
        }
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 90, 25);
        add(lbl);
    }

    static class ComboItem {
        int id;
        String ad;
        ComboItem(int id, String ad) { this.id = id; this.ad = ad; }
        public String toString() { return ad; }
    }
}