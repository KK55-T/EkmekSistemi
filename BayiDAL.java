import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class BayiDAL {

    public DefaultTableModel listele() throws Exception {
        Connection con = DbConnection.getConnection();
        CallableStatement cs = con.prepareCall("{call BayiListele()}");
        ResultSet rs = cs.executeQuery();
        DefaultTableModel model = buildModel(rs);
        con.close();
        return model;
    }

    public void ekle(String ad, String yetkili, String tel, String adres) throws Exception {
        Connection con = DbConnection.getConnection();
        CallableStatement cs = con.prepareCall("{call BayiEkle(?,?,?,?)}");
        cs.setString(1, ad);
        cs.setString(2, yetkili);
        cs.setString(3, tel);
        cs.setString(4, adres);
        cs.executeUpdate();
        con.close();
    }

    public void guncelle(int id, String ad, String yetkili, String tel, String adres) throws Exception {
        Connection con = DbConnection.getConnection();
        CallableStatement cs = con.prepareCall("{call BayiGuncelle(?,?,?,?,?)}");
        cs.setInt(1, id);
        cs.setString(2, ad);
        cs.setString(3, yetkili);
        cs.setString(4, tel);
        cs.setString(5, adres);
        cs.executeUpdate();
        con.close();
    }

    public void sil(int id) throws Exception {
        Connection con = DbConnection.getConnection();
        CallableStatement cs = con.prepareCall("{call BayiSil(?)}");
        cs.setInt(1, id);
        cs.executeUpdate();
        con.close();
    }

    private DefaultTableModel buildModel(ResultSet rs) throws Exception {
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        Vector<String> colNames = new Vector<>();
        for (int i = 1; i <= cols; i++) colNames.add(meta.getColumnName(i));
        Vector<Vector<Object>> rows = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= cols; i++) row.add(rs.getObject(i));
            rows.add(row);
        }
        return new DefaultTableModel(rows, colNames) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }
}