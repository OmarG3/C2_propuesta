package c2;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Omar
 */
public class Conexion {

    private static String driver = "com.mysql.cj.jdbc.Driver";
    // Ruta para servidor
    //private static String url    = "jdbc:mysql://192.168.1.71/c2?autoReconnect=true&useSSL=false";
   // Ruta para servidor
    private static String url    = "jdbc:mysql://192.168.1.112/c2?autoReconnect=true&useSSL=false";
    // Ruta Local
   // private static String url    = "jdbc:mysql://localhost:3306/c2?autoReconnect=true&useSSL=false";
    private static String user   = "root";
    private static String pass   = "root";
    private static String bd     = "c2";

    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error en el Driver");
        }
    }

    public Connection getConexion() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la Conexión");
        }
        return con;
    }

    public String getUsuario() {
        return user;
    }

    public String getPassword() {
        return pass;
    }

    public String getBD() {
        return bd;
    }
}