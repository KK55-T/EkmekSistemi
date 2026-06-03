import java.sql.*;

public class TeslimatDAL {

    public void teslimatYap(int bayiId, int urunId, int adet) throws Exception {
        Connection con = DbConnection.getConnection();
        CallableStatement cs = con.prepareCall("{call TeslimatYap(?,?,?)}");
        cs.setInt(1, bayiId);
        cs.setInt(2, urunId);
        cs.setInt(3, adet);
        cs.executeUpdate();
        con.close();
    }
}