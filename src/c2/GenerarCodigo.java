package c2;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class GenerarCodigo {

    //Prueba de folio 2
    public String NroSerie() {
        // Mandamos a llamar a la conexión
        Conexion conector = new Conexion();
        // "con" se usara para realizar distintos procesos
        Connection con = conector.getConexion();
        PreparedStatement ps;
        String serie = "";
        Statement st;
        try {
            //st = con.createStatement();
            ps = con.prepareStatement("select (folio) from Emergencia");
            ResultSet rs = null;
            rs = ps.executeQuery();
            //ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                serie = rs.getString(1);
            }
        } catch (Exception e) {
        }
        return serie;
    }
}