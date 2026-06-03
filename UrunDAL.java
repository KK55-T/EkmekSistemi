import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class UrunDAL {

    public void ekle(String ad, float fiyat, int stok) throws Exception {
        Connection con = DbConnection.getConnection();
        CallableStatement cs = con.prepareCall("{call UrunEkle(?,?,?)}");
        cs.setString(1, ad);
        cs.setFloat(2, fiyat);
        cs.setInt(3, stok);
        cs.executeUpdate();
        con.close();
    }

    public DefaultTableModel listele() throws Exception {
        Connection con = DbConnection.getConnection();
        CallableStatement cs = con.prepareCall("{call UrunListele()}");
        ResultSet rs = cs.executeQuery();
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
        con.close();
        return new DefaultTableModel(rows, colNames) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }
}