package c2;

import static Login.Acceso.IDuser;
import static Login.Acceso.Name;
import java.awt.Color;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author king_
 */
public class Administrador extends javax.swing.JFrame implements Runnable {
    //Mandamos a llamar a la conexión
    Conexion conector = new Conexion();
    //"con" se usara para realizar distintos procesos
    Connection con = conector.getConexion();
    ///
    PreparedStatement ps;
    
    DefaultTableModel modelo = new DefaultTableModel();
    ///////////////////////////////////////
    String hora, minutos, segundos, ampm;
    Calendar calendario;
    Thread h1;
    //////////////////////////////////////
    Calendar fecha_Actual = new GregorianCalendar();
    
    // Variables para el registro del folio eliminado completo
    String Ehora, Efecha, EidUsuario, EidEquipo, EtelefonoLlamada, Esemaforo, Efase,
            EdescripcionHeridos, Ereporte, EseguridadPublica, EaguaPotable, EproteccionCivil,
            EinstanciaMujer, EunidadUno, EotrosServicios, EincidenteDenuncia, Eubicacion,
            EdescripcionLugar, Einvolucrados, EnombreDenunciante, Etelefono, Eestatus;
    
    public Administrador() {
        initComponents();
        this.setLocationRelativeTo(null);
        //hora
        h1 = new Thread(this);
        h1.start();
        setVisible(true);
        EliminacionUsuarios();
        dobleClicJtable();
        dobleClicJtableConsultas();
        dobleClicJtableFoliosEliminados();
        listaUsusariosConsulta();
        tablaReportes();
        TablaFolios();
        FoliosEliminados();
        txtEstatusUser.setEditable(false);
        txtDomicilioArea.setEditable(false);
        txtConsultaEmergencia.setEditable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ImagenesExtras/Icono_Ventanas.png")));
        //
        fecha.setCalendar(fecha_Actual);
        fecha.setVisible(false);
        // Dar saltos de linea al llegar al borde de un area texto
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        //
        txtAutorizado.setText(Name);
        txtAutorizado.setEditable(false);
        // Para que las cajas de texto no se puedan editar
        txtFolioinfo.setEditable(false);
        txtHorainfo.setEditable(false);
        txtFechainfo.setEditable(false);
        txtidUsuarioinfo.setEditable(false);
        txtidEquipoinfo.setEditable(false);
        txtTelefonoinfo.setEditable(false);
        txtSemaforoinfo.setEditable(false);
        txtFaseinfo.setEditable(false);
        txtDescripcioninfo.setEditable(false);
        txtReporteinfo.setEditable(false);
        txtSeguridadinfo.setEditable(false);
        txtAguainfo.setEditable(false);
        txtProteccioninfo.setEditable(false);
        txtInstanciainfo.setEditable(false);
        txtUnidadinfo.setEditable(false);
        txtOtrosinfo.setEditable(false);
        txtIncidenteinfo.setEditable(false);
        txtUbicacioninfo.setEditable(false);
        txtDescripcionLugarinfo.setEditable(false);
        txtInvolucradosinfo.setEditable(false);
        txtDenuncianteinfo.setEditable(false);
        txtTelefono2info.setEditable(false);
        txtEstatusinfo.setEditable(false);
        // Dar saltos de linea al llegar al borde de un area texto
        txtDescripcioninfo.setLineWrap(true);
        txtDescripcioninfo.setWrapStyleWord(true);
        txtReporteinfo.setLineWrap(true);
        txtReporteinfo.setWrapStyleWord(true);
        txtIncidenteinfo.setLineWrap(true);
        txtIncidenteinfo.setWrapStyleWord(true);
        txtUbicacioninfo.setLineWrap(true);
        txtUbicacioninfo.setWrapStyleWord(true);
        txtDescripcionLugarinfo.setLineWrap(true);
        txtDescripcionLugarinfo.setWrapStyleWord(true);
    }
    
    private void limpiarCajas() {
        txtNombre.setText(null);
        txtApellidoP.setText(null);
        txtApellidoM.setText(null);
        txtIDUser.setText(null);
        txtPass.setText(null);
        cbxTipoCargo.setSelectedIndex(0);
        txtFechaNacimiento.setText(null);
        txtFacebook.setText(null);
        txtTwitter.setText(null);
        txtInstagram.setText(null);
        txtTiktok.setText(null);
        txtCorreo.setText(null);
        txtDomicilioEmpleado.setText(null);
        txtTelCelular.setText(null);
        txtTelCasa.setText(null);
        txtNombreCompletoE.setText(null);
        txtTelefonoE.setText(null);
        txtDomicilioE.setText(null);
        txtIDUsuarioEliminar.setText(null);
    }
    
    private void limpiarCajasConsulta() {
        lblNombre.setText(null);
        txtCIDUsuario.setText(null);
    }
    
    public void BuscarFolio(){
        try {
            ps = con.prepareStatement("select * from Usuarios");
            ResultSet rs = null;
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString(4).equals(txtCIDUsuario.getText())) {
                        JOptionPane.showMessageDialog(null, "USUARIO ENCONTRADO");
                        lblNombre.setText(rs.getString(1));
                        lblPass.setText(rs.getString(5));
                        lblApellidoPaterno.setText(rs.getString(2));
                        lblApellidoMaterno.setText(rs.getString(3));
                        lblFechaNacimiento.setText(rs.getString(7));
                        lblCorreoElectronico.setText(rs.getString(12));
                        lblTelCelular.setText(rs.getString(14));
                        lblTelCasa.setText(rs.getString(15));
                        lblCargo.setText(rs.getString(6));
                        txtDomicilioArea.setText(rs.getString(13));
                        lblNombreCompletoConsulta.setText(rs.getString(16));
                        lblTelefonoEmergencia.setText(rs.getString(17));
                        txtConsultaEmergencia.setText(rs.getString(18));
                        lblFacebook.setText(rs.getString(8));
                        lblTwitter.setText(rs.getString(9));
                        lblInstagram.setText(rs.getString(10));
                        lblTiktok.setText(rs.getString(11));
                        lblEstatus.setText(rs.getString(19));
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "NO Conectado");
        }
    }
    // Calcular la hora del sistema
    public void calcula() {
    Calendar calendario = new GregorianCalendar();
    Date fechaHoraActual = new Date();
    calendario.setTimeZone(TimeZone.getTimeZone("America/Monterrey"));
    calendario.setTime(fechaHoraActual);
    calendario.add(Calendar.HOUR_OF_DAY, 0); // Restar una hora
    ampm = calendario.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM";
    if (ampm.equals("PM")) {
        int h = calendario.get(Calendar.HOUR_OF_DAY) - 12;
        hora = h > 9 ? "" + h : "0" + h;
    } else {
        hora = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? "" + calendario.get(Calendar.HOUR_OF_DAY) : "0" + calendario.get(Calendar.HOUR_OF_DAY);
    }
    minutos = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE);
    segundos = calendario.get(Calendar.SECOND) > 9 ? "" + calendario.get(Calendar.SECOND) : "0" + calendario.get(Calendar.SECOND);
}

    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == h1) {
            calcula();
            //txtHora.setText(hora + ":" + minutos + ":" + segundos + " " + ampm);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
    
    public void EliminacionUsuarios(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");//1
        modelo.addColumn("Apellido");//2
        modelo.addColumn("ID Usuario");//4
        modelo.addColumn("Passworrd");//5
        modelo.addColumn("Cargo");//6
        modelo.addColumn("Correo");//12
        modelo.addColumn("Domicilio");//13
        modelo.addColumn("Celular");//14
        modelo.addColumn("Teléfono Casa");//15
        modelo.addColumn("Estatus");//16
        tablaUsuarios.setModel(modelo);

        String datos[] = new String[10];
        com.mysql.cj.xdevapi.Statement st;
        try {
            //st = con.createStatement();
            ps = con.prepareStatement("select * from Usuarios");
            ResultSet rs = null;
            rs = ps.executeQuery();
            //ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(4);
                datos[3] = rs.getString(5);
                datos[4] = rs.getString(6);
                datos[5] = rs.getString(12);
                datos[6] = rs.getString(13);
                datos[7] = rs.getString(14);
                datos[8] = rs.getString(15);
                datos[9] = rs.getString(19);
                modelo.addRow(datos);
            }
            tablaUsuarios.setModel(modelo);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    // Tabla de folios creados
    public void TablaFolios(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Folio");//1
        modelo.addColumn("Hora");//2
        modelo.addColumn("Fecha");//3
        modelo.addColumn("ID Usuario");//4
        modelo.addColumn("ID Equipo");//5
        modelo.addColumn("Estatus");//23
        tablaFolios.setModel(modelo);

        String datos[] = new String[6];
        com.mysql.cj.xdevapi.Statement st;
        try {
            ps = con.prepareStatement("select * from Emergencia");
            ResultSet rs = null;
            rs = ps.executeQuery();
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(23);
                modelo.addRow(datos);
            }
            tablaFolios.setModel(modelo);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    // Tabla de informacion completa de folios eliminados
    public void FoliosEliminados(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Folio");//1
        modelo.addColumn("Fecha");//2
        modelo.addColumn("Hora");//3
        modelo.addColumn("Solicitante");//4
        modelo.addColumn("ID Usuario");//5
        modelo.addColumn("Autorizado por");//6
        modelo.addColumn("Motivo");//7
        tablaFoliosEliminados.setModel(modelo);

        String datos[] = new String[7];
        com.mysql.cj.xdevapi.Statement st;
        try {
            //st = con.createStatement();
            ps = con.prepareStatement("select * from foliosEliminados ");
            ResultSet rs = null;
            rs = ps.executeQuery();
            //ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);
                datos[6] = rs.getString(7);
                
                modelo.addRow(datos);
            }
            tablaFoliosEliminados.setModel(modelo);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    
    public void listaUsusariosConsulta(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");//1
        modelo.addColumn("Apellido");//2
        modelo.addColumn("ID Usuario");//4
        modelo.addColumn("Cargo");//6
        tablaListaConsulta.setModel(modelo);
        String datos[] = new String[4];
        com.mysql.cj.xdevapi.Statement st;
        try {
            ps = con.prepareStatement("select * from Usuarios");
            ResultSet rs = null;
            rs = ps.executeQuery();
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(4);
                datos[3] = rs.getString(6);
                modelo.addRow(datos);
            }
            tablaListaConsulta.setModel(modelo);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    // Mandar el folio con doble clic al JTextField 
    public void dobleClicJtable(){
        tablaUsuarios.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt){
            JTable table = (JTable) Mouse_evt.getSource();
            Point point = Mouse_evt.getPoint();
            int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 2) {
                    limpiarCajas();
                    txtIDUsuarioEliminar.setText(tablaUsuarios.getValueAt(tablaUsuarios.getSelectedRow(), 2).toString());
                }
            }
        });
    }
    // Mandar el folio con doble clic al JTextField 
    public void dobleClicJtableConsultas(){
        tablaListaConsulta.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt){
            JTable table = (JTable) Mouse_evt.getSource();
            Point point = Mouse_evt.getPoint();
            int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 2) {
                    limpiarCajasConsulta();
                    txtCIDUsuario.setText(tablaListaConsulta.getValueAt(tablaListaConsulta.getSelectedRow(), 2).toString());
                    BuscarFolio();
                }
            }
        });
    }
    //////////////////////////////
    public void tablaReportes(){
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Folio");//
            modelo.addColumn("Fecha");//
            modelo.addColumn("Id Usuario");//
            modelo.addColumn("Semaforo");//
            modelo.addColumn("Incidente");//
            modelo.addColumn("Ubicacion");
            modelo.addColumn("Estatus");//
            tablaReportes.setModel(modelo);
          
        String datos[] = new String[7];
        Statement st;
        try {
            //st = con.createStatement();
            ps = con.prepareStatement("select * from Emergencia");
            ResultSet rs = null;
            rs = ps.executeQuery();                
            //ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                datos[0]=rs.getString(1);
                datos[1]=rs.getString(3);
                datos[2]=rs.getString(4);
                datos[3]=rs.getString(7);
                datos[4]=rs.getString(17);
                datos[5]=rs.getString(18);
                datos[6]=rs.getString(23);
                modelo.addRow(datos);
            }
            tablaReportes.setModel(modelo);
            
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    //Reporte Consultas
    public void ConsultaReportesFecha(){
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Folio");//
            modelo.addColumn("Fecha");//
            modelo.addColumn("Id Usuario");//
            modelo.addColumn("Semaforo");//
            modelo.addColumn("Incidente");//
            modelo.addColumn("Ubicacion");
            modelo.addColumn("Estatus");//
            tablaReportes.setModel(modelo);
          
        String datos[] = new String[7];
        Statement st;
        try {
            //st = con.createStatement();
            ps = con.prepareStatement("SELECT * FROM Emergencia WHERE fecha BETWEEN '" + ((JTextField) Fecha1.getDateEditor().getUiComponent()).getText() + "' AND '" + 
                    ((JTextField) Fecha2.getDateEditor().getUiComponent()).getText() + "'");
            ResultSet rs = null;
            rs = ps.executeQuery();                
            //ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                datos[0]=rs.getString(1);
                datos[1]=rs.getString(3);
                datos[2]=rs.getString(4);
                datos[3]=rs.getString(7);
                datos[4]=rs.getString(17);
                datos[5]=rs.getString(18);
                datos[6]=rs.getString(23);
                modelo.addRow(datos);
            }
            tablaReportes.setModel(modelo);
            
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    // Evwnto de doble clic para mostrar los datos completos del folio eliminado
    public void dobleClicJtableFoliosEliminados() {
        tablaFoliosEliminados.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt) {
                JTable table = (JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 2) {
                    txtFolioinfo.setText(tablaFoliosEliminados.getValueAt(tablaFoliosEliminados.getSelectedRow(), 0).toString());
                    infoFoliosEliminados();
                    try {
                        ps = con.prepareStatement("select * from Antena");
                        ResultSet rs = null;
                        rs = ps.executeQuery();
                        if (rs != null) {
                            while (rs.next()) {
                                if (rs.getString(1).equals(txtFolioinfo.getText())) {
                                    labelMensajeNotas.setText("");
                                    limpiarTabla(tablaNotasFoliosEliminados);
                                    tablaNotasFoliosEliminados();
                                    break;
                                } else {
                                    labelMensajeNotas.setBackground(Color.RED);
                                    labelMensajeNotas.setText("ESTE FOLIO NO CUENTA CON NOTAS");
                                    limpiarTabla(tablaNotasFoliosEliminados);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "NO Conectado");
                    }
                    /// Abrir JDialog Comunidades
                    FoliosEliminados.setSize(1358, 915);//Asignas tamaño (x,y)
                    FoliosEliminados.setLocationRelativeTo(null);//posicion
                    FoliosEliminados.setModal(true);//que se ubique al centro
                    FoliosEliminados.setVisible(true);//que se haga visible
                }
            }
        });
    }
    // Para mandar a llamar los datos de la tabla de folioseliminados completos
    public void infoFoliosEliminados(){
    try {
            ps = con.prepareStatement("select * from infoCompFoliosEliminados");
            ResultSet rs = null;
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString(1).equals(txtFolioinfo.getText())) {
                        txtHorainfo.setText(rs.getString(2));
                        txtFechainfo.setText(rs.getString(3));
                        txtidUsuarioinfo.setText(rs.getString(4));
                        txtidEquipoinfo.setText(rs.getString(5));
                        txtTelefonoinfo.setText(rs.getString(6));
                        txtSemaforoinfo.setText(rs.getString(7));
                        txtFaseinfo.setText(rs.getString(8));
                        txtDescripcioninfo.setText(rs.getString(9));
                        txtReporteinfo.setText(rs.getString(10));
                        txtSeguridadinfo.setText(rs.getString(11));
                        txtAguainfo.setText(rs.getString(12));
                        txtProteccioninfo.setText(rs.getString(13));
                        txtInstanciainfo.setText(rs.getString(14));
                        txtUnidadinfo.setText(rs.getString(15));
                        txtOtrosinfo.setText(rs.getString(16));
                        txtIncidenteinfo.setText(rs.getString(17));
                        txtUbicacioninfo.setText(rs.getString(18));
                        txtDescripcionLugarinfo.setText(rs.getString(19));
                        txtInvolucradosinfo.setText(rs.getString(20));
                        txtDenuncianteinfo.setText(rs.getString(21));
                        txtTelefono2info.setText(rs.getString(22));
                        txtEstatusinfo.setText(rs.getString(23));
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "NO Conectado");
        }
    }
    // MANDAR A LLAMAR A TABLA LAS NOTAS INGRESADAS
    public void tablaNotasFoliosEliminados(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nota");//1
        modelo.addColumn("Hora");//7
        
        tablaNotasFoliosEliminados.setModel(modelo);

        String datos[] = new String[2];
        com.mysql.cj.xdevapi.Statement st;
        
        try {
            ps = con.prepareStatement("select * from Antena where folio ='" + txtFolioinfo.getText()+"'");
            ResultSet rs = null;
            rs = ps.executeQuery();
            //ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                datos[0] = rs.getString(13);
                datos[1] = rs.getString(14);
                modelo.addRow(datos);
            }
            tablaNotasFoliosEliminados.getColumnModel().getColumn(0).setPreferredWidth(1220);
            tablaNotasFoliosEliminados.getColumnModel().getColumn(1).setPreferredWidth(5);
            tablaNotasFoliosEliminados.setModel(modelo);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    // Metodo para limpiar una tabla
    public void limpiarTabla(JTable tabla){
        try {
            DefaultTableModel modelo=(DefaultTableModel) tabla.getModel();
            int filas=tabla.getRowCount();
            for (int i = 0;filas>i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ListaUsuariosConsulta = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaListaConsulta = new javax.swing.JTable();
        FoliosEliminados = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        txtFolioinfo = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        txtHorainfo = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        txtFechainfo = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        txtidUsuarioinfo = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        txtidEquipoinfo = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        txtTelefonoinfo = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
        txtSemaforoinfo = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        txtFaseinfo = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtDescripcioninfo = new javax.swing.JTextArea();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtReporteinfo = new javax.swing.JTextArea();
        jLabel71 = new javax.swing.JLabel();
        txtProteccioninfo = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        txtSeguridadinfo = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        txtAguainfo = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        txtInstanciainfo = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        txtUnidadinfo = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        txtOtrosinfo = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        txtIncidenteinfo = new javax.swing.JTextArea();
        jLabel79 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        txtUbicacioninfo = new javax.swing.JTextArea();
        jLabel80 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        txtDescripcionLugarinfo = new javax.swing.JTextArea();
        jLabel81 = new javax.swing.JLabel();
        txtInvolucradosinfo = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        txtDenuncianteinfo = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        txtTelefono2info = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        txtEstatusinfo = new javax.swing.JTextField();
        jLabel85 = new javax.swing.JLabel();
        labelMensajeNotas = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        tablaNotasFoliosEliminados = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnConsulta = new javax.swing.JButton();
        btnAgregarModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnConsulta1 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        btnEliminarFolios = new javax.swing.JButton();
        btnConFoliosElimi = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        AgregarModificar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFechaNacimiento = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtTelCelular = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTelCasa = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDomicilioEmpleado = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        txtApellidoP = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtApellidoM = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtIDUser = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtPass = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        cbxTipoCargo = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();
        txtEstatusUser = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtFacebook = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTwitter = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtInstagram = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTiktok = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtNombreCompletoE = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtTelefonoE = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDomicilioE = new javax.swing.JTextArea();
        jPanel8 = new javax.swing.JPanel();
        btnNuevoUsuario = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        Eliminar = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtIDUsuarioEliminar = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaUsuarios = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        cbxEstatus = new javax.swing.JComboBox<>();
        ConsultaCompleta = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        txtCIDUsuario = new javax.swing.JTextField();
        btnTablaUsusarios = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        lblPass = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblApellidoPaterno = new javax.swing.JLabel();
        lblApellidoMaterno = new javax.swing.JLabel();
        lblFechaNacimiento = new javax.swing.JLabel();
        lblCorreoElectronico = new javax.swing.JLabel();
        lblTelCelular = new javax.swing.JLabel();
        lblTelCasa = new javax.swing.JLabel();
        lblCargo = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtDomicilioArea = new javax.swing.JTextArea();
        jLabel49 = new javax.swing.JLabel();
        lblEstatus = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtConsultaEmergencia = new javax.swing.JTextArea();
        jLabel41 = new javax.swing.JLabel();
        lblNombreCompletoConsulta = new javax.swing.JLabel();
        lblTelefonoEmergencia = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        lblFacebook = new javax.swing.JLabel();
        lblTwitter = new javax.swing.JLabel();
        lblInstagram = new javax.swing.JLabel();
        lblTiktok = new javax.swing.JLabel();
        Reportes = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaReportes = new javax.swing.JTable();
        jLabel51 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        Fecha1 = new com.toedter.calendar.JDateChooser();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        Fecha2 = new com.toedter.calendar.JDateChooser();
        jButton3 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        EliminarFolios = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txtNoFolio = new javax.swing.JTextField();
        btnEliminarFolio = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        tablaFolios = new javax.swing.JTable();
        jButton12 = new javax.swing.JButton();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        txtSolicitante = new javax.swing.JTextField();
        txtIDusuario = new javax.swing.JTextField();
        txtAutorizado = new javax.swing.JTextField();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtMotivo = new javax.swing.JTextArea();
        fecha = new com.toedter.calendar.JDateChooser();
        ConsultaFoliosEliminados = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tablaFoliosEliminados = new javax.swing.JTable();
        btnActFoliosElimi = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();

        ListaUsuariosConsulta.setTitle("Consulta Rapida");

        tablaListaConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tablaListaConsulta);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout ListaUsuariosConsultaLayout = new javax.swing.GroupLayout(ListaUsuariosConsulta.getContentPane());
        ListaUsuariosConsulta.getContentPane().setLayout(ListaUsuariosConsultaLayout);
        ListaUsuariosConsultaLayout.setHorizontalGroup(
            ListaUsuariosConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ListaUsuariosConsultaLayout.setVerticalGroup(
            ListaUsuariosConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        FoliosEliminados.setTitle("DATOS DEL FOLIO");
        FoliosEliminados.setResizable(false);

        jPanel14.setBackground(new java.awt.Color(168, 0, 40));

        jLabel60.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("INFORMACIÓN COMPLETA DE FOLIO ELIMINADO");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel60)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel60, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        jLabel62.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel62.setText("FOLIO:");

        jLabel63.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel63.setText("HORA:");

        jLabel64.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel64.setText("FECHA:");

        txtFechainfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechainfoActionPerformed(evt);
            }
        });

        jLabel65.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel65.setText("ID USUARIO:");

        jLabel66.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel66.setText("ID EQUIPO:");

        jLabel67.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel67.setText("TELEFONO:");

        jLabel68.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel68.setText("SEMAFORO:");

        jLabel69.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel69.setText("FASE:");

        jLabel70.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel70.setText("DESCRIPCION HERIDOS:");

        txtDescripcioninfo.setColumns(20);
        txtDescripcioninfo.setRows(5);
        jScrollPane11.setViewportView(txtDescripcioninfo);

        txtReporteinfo.setColumns(20);
        txtReporteinfo.setRows(5);
        jScrollPane12.setViewportView(txtReporteinfo);

        jLabel71.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel71.setText("REPORTE:");

        jLabel72.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel72.setText("SEGURIDAD PUBLICA:");

        jLabel73.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel73.setText("AGUA POTABLE:");

        txtAguainfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAguainfoActionPerformed(evt);
            }
        });

        jLabel74.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel74.setText("PROTECCION CIVIL:");

        jLabel75.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel75.setText("INSTANCIA DE LA MUJER:");

        jLabel76.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel76.setText("UNIDAD DE 1ER CONTACTO:");

        jLabel77.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel77.setText("OTROS SERVICIOS:");

        jLabel78.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel78.setText("INCIDENTE DENUNCA:");

        txtIncidenteinfo.setColumns(20);
        txtIncidenteinfo.setRows(5);
        jScrollPane13.setViewportView(txtIncidenteinfo);

        jLabel79.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel79.setText("UBICACION:");

        txtUbicacioninfo.setColumns(20);
        txtUbicacioninfo.setRows(5);
        jScrollPane14.setViewportView(txtUbicacioninfo);

        jLabel80.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel80.setText("DESCRIPCION DEL LUGAR:");

        txtDescripcionLugarinfo.setColumns(20);
        txtDescripcionLugarinfo.setRows(5);
        jScrollPane15.setViewportView(txtDescripcionLugarinfo);

        jLabel81.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel81.setText("INVOLUCRADOS:");

        jLabel82.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel82.setText("NOMBRE DEL DENUNCIANTE:");

        jLabel83.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel83.setText("TELEFONO:");

        jLabel84.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel84.setText("ESTATUS:");

        jLabel85.setBackground(new java.awt.Color(0, 0, 0));
        jLabel85.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel85.setText("NOTAS:");

        labelMensajeNotas.setBackground(new java.awt.Color(153, 0, 0));
        labelMensajeNotas.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        labelMensajeNotas.setForeground(new java.awt.Color(153, 0, 0));

        tablaNotasFoliosEliminados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane16.setViewportView(tablaNotasFoliosEliminados);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel62)
                            .addComponent(jLabel67))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(txtFolioinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel63))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(txtTelefonoinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel68)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(txtSemaforoinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel69)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFaseinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel70)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane11))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(txtHorainfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel64)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFechainfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel65)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtidUsuarioinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel66)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtidEquipoinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel71)
                            .addComponent(jLabel81)
                            .addComponent(jLabel78))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(jScrollPane12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel74)
                                    .addComponent(jLabel76)
                                    .addComponent(jLabel72))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtProteccioninfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUnidadinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSeguridadinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel75)
                                    .addComponent(jLabel73)
                                    .addComponent(jLabel77))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtInstanciainfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtAguainfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtOtrosinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(txtInvolucradosinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel82)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtDenuncianteinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel83)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTelefono2info, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel84)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEstatusinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel13Layout.createSequentialGroup()
                                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel79)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel80)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jScrollPane16)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel85)
                        .addGap(11, 11, 11)
                        .addComponent(labelMensajeNotas, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFolioinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62)
                    .addComponent(jLabel63)
                    .addComponent(txtHorainfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64)
                    .addComponent(txtFechainfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65)
                    .addComponent(txtidUsuarioinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66)
                    .addComponent(txtidEquipoinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTelefonoinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel67))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSemaforoinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel68))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFaseinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel69)
                        .addComponent(jLabel70))
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel71)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSeguridadinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel72))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProteccioninfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel74)
                            .addComponent(jLabel75)
                            .addComponent(txtInstanciainfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUnidadinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel76)
                            .addComponent(jLabel77)
                            .addComponent(txtOtrosinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtAguainfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel73)))
                .addGap(31, 31, 31)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel78)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel79)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel80)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInvolucradosinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel81)
                    .addComponent(jLabel82)
                    .addComponent(txtDenuncianteinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel83)
                    .addComponent(txtTelefono2info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel84)
                    .addComponent(txtEstatusinfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel85, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelMensajeNotas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout FoliosEliminadosLayout = new javax.swing.GroupLayout(FoliosEliminados.getContentPane());
        FoliosEliminados.getContentPane().setLayout(FoliosEliminadosLayout);
        FoliosEliminadosLayout.setHorizontalGroup(
            FoliosEliminadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        FoliosEliminadosLayout.setVerticalGroup(
            FoliosEliminadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FoliosEliminadosLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(93, 109, 126));
        jPanel1.setPreferredSize(new java.awt.Dimension(1920, 1039));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(250, 229, 211));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnConsulta.setBackground(new java.awt.Color(250, 229, 211));
        btnConsulta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-Reporte.png"))); // NOI18N
        btnConsulta.setToolTipText("Reporte Incidencias");
        btnConsulta.setBorder(null);
        btnConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultaActionPerformed(evt);
            }
        });
        jPanel3.add(btnConsulta, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 140, 110));

        btnAgregarModificar.setBackground(new java.awt.Color(250, 229, 211));
        btnAgregarModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/addListUsers.png"))); // NOI18N
        btnAgregarModificar.setToolTipText("Agregar y/o Modificar Personal");
        btnAgregarModificar.setBorder(null);
        btnAgregarModificar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarModificarMouseClicked(evt);
            }
        });
        btnAgregarModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarModificarActionPerformed(evt);
            }
        });
        jPanel3.add(btnAgregarModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 140, 110));

        btnEliminar.setBackground(new java.awt.Color(250, 229, 211));
        btnEliminar.setForeground(new java.awt.Color(0, 0, 0));
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-eliminarUser.png"))); // NOI18N
        btnEliminar.setToolTipText("Inhabilitar Personal");
        btnEliminar.setBorder(null);
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel3.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 140, 110));

        btnConsulta1.setBackground(new java.awt.Color(250, 229, 211));
        btnConsulta1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-consultaUsuarios.png"))); // NOI18N
        btnConsulta1.setToolTipText("Consultar Personal");
        btnConsulta1.setBorder(null);
        btnConsulta1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsulta1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnConsulta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 140, 110));

        jButton10.setBackground(new java.awt.Color(250, 229, 211));
        jButton10.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(0, 0, 0));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-BD.png"))); // NOI18N
        jButton10.setText(" RESPALDAR BASE DE DATOS");
        jButton10.setBorder(null);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 300, 40));

        btnEliminarFolios.setBackground(new java.awt.Color(250, 229, 211));
        btnEliminarFolios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-eliminarF.png"))); // NOI18N
        btnEliminarFolios.setToolTipText("Eliminar Folio");
        btnEliminarFolios.setBorder(null);
        btnEliminarFolios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarFoliosActionPerformed(evt);
            }
        });
        jPanel3.add(btnEliminarFolios, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 140, 110));

        btnConFoliosElimi.setBackground(new java.awt.Color(250, 229, 211));
        btnConFoliosElimi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-foliosE.png"))); // NOI18N
        btnConFoliosElimi.setToolTipText("Consulta de Folios Eliminados");
        btnConFoliosElimi.setBorder(null);
        btnConFoliosElimi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConFoliosElimiActionPerformed(evt);
            }
        });
        jPanel3.add(btnConFoliosElimi, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 310, 140, 110));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 320, 640));

        jPanel5.setBackground(new java.awt.Color(224, 224, 224));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton4.setBackground(new java.awt.Color(224, 224, 224));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-antena1.png"))); // NOI18N
        jButton4.setToolTipText("Despachador");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 20, 600, 270));

        jButton5.setBackground(new java.awt.Color(224, 224, 224));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-monitorista.png"))); // NOI18N
        jButton5.setToolTipText("Operador");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 600, 270));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 1530, 310));

        jPanel4.setBackground(new java.awt.Color(254, 249, 231));
        jPanel4.setLayout(new java.awt.CardLayout());

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DATOS DE PERSONAL");
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("NOMBRE(S):");

        jLabel3.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("FECHA DE NACIMIENTO:");

        jLabel4.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("CORREO ELECTRONICO:");

        jLabel5.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("TELEFONO CELULAR:");

        jLabel6.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("TELEFONO CASA:");

        jLabel7.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("DOMICILIO:");

        txtDomicilioEmpleado.setColumns(20);
        txtDomicilioEmpleado.setRows(5);
        jScrollPane1.setViewportView(txtDomicilioEmpleado);

        jLabel17.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("APELLIDO PATERNO:");

        txtApellidoP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApellidoPActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("APELLIDO MATERNO:");

        jLabel19.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("ID USUARIO:");

        txtIDUser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIDUserKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIDUserKeyReleased(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("CONTRASEÑA:");

        jLabel21.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("TIPO DE CARGO:");

        cbxTipoCargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recepcionista", "Despachador", "Administrador" }));

        jLabel47.setForeground(new java.awt.Color(0, 0, 0));
        jLabel47.setText("ESTATUS:");

        txtEstatusUser.setText("Activo");

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-limpiarCampos.png"))); // NOI18N
        jButton14.setToolTipText("Limpiar Campos");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtApellidoM, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addComponent(txtApellidoP, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTelCelular, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTelCasa, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFechaNacimiento, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(314, 314, 314))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(txtIDUser, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton14))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEstatusUser, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47)
                            .addComponent(jLabel21)
                            .addComponent(cbxTipoCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19)
                            .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtIDUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton14))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtApellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApellidoM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxTipoCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel47))
                .addGap(13, 13, 13)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEstatusUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTelCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1)))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(txtTelCasa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(127, 127, 127))
        );

        jLabel8.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("REDES SOCIALES:");
        jLabel8.setOpaque(true);

        jLabel9.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("FACEBOOK:");

        jLabel10.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("TWITTER:");

        jLabel11.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("INSTAGRAM:");

        jLabel12.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("TIKTOK:");

        jLabel13.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("EN CASO DE EMERGENCIA LLAMAR A:");
        jLabel13.setOpaque(true);

        jLabel14.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("NOMBRE COMPLETO:");

        jLabel15.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("TELEFONO:");

        jLabel16.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("DOMICILIO:");

        txtDomicilioE.setColumns(20);
        txtDomicilioE.setRows(5);
        jScrollPane2.setViewportView(txtDomicilioE);

        btnNuevoUsuario.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnNuevoUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-anadirUser.png"))); // NOI18N
        btnNuevoUsuario.setText(" AGREGAR");
        btnNuevoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoUsuarioActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/modificarUser.png"))); // NOI18N
        jButton13.setText(" MODIFICAR");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(btnNuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNuevoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtNombreCompletoE)
                                        .addComponent(txtTelefonoE, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 227, Short.MAX_VALUE))))
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGap(7, 7, 7)
                            .addComponent(jLabel9))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)))
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTiktok, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInstagram, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTwitter, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFacebook, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtFacebook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtTwitter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtInstagram, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtTiktok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtNombreCompletoE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtTelefonoE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout AgregarModificarLayout = new javax.swing.GroupLayout(AgregarModificar);
        AgregarModificar.setLayout(AgregarModificarLayout);
        AgregarModificarLayout.setHorizontalGroup(
            AgregarModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgregarModificarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AgregarModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(AgregarModificarLayout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        AgregarModificarLayout.setVerticalGroup(
            AgregarModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgregarModificarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AgregarModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel4.add(AgregarModificar, "card4");

        jLabel24.setBackground(new java.awt.Color(204, 0, 0));
        jLabel24.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("SUSPENCIÓN DE USUARIO");
        jLabel24.setOpaque(true);

        jLabel25.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel25.setText("ID USUARIO:");

        jButton1.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-suspenderUser.png"))); // NOI18N
        jButton1.setText(" SUSPENDER");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tablaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(tablaUsuarios);

        jButton2.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-actualizarTablas.png"))); // NOI18N
        jButton2.setText(" ACTUALIZAR TABLA");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel48.setText("ESTATUS:");

        cbxEstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        javax.swing.GroupLayout EliminarLayout = new javax.swing.GroupLayout(Eliminar);
        Eliminar.setLayout(EliminarLayout);
        EliminarLayout.setHorizontalGroup(
            EliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EliminarLayout.createSequentialGroup()
                .addGroup(EliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EliminarLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtIDUsuarioEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel48)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxEstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(EliminarLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(EliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1506, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EliminarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(33, 33, 33))
        );
        EliminarLayout.setVerticalGroup(
            EliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EliminarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addGap(22, 22, 22)
                .addGroup(EliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtIDUsuarioEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel48)
                    .addComponent(cbxEstatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(20, 20, 20))
        );

        jPanel4.add(Eliminar, "card3");

        jLabel23.setBackground(new java.awt.Color(0, 102, 153));
        jLabel23.setFont(new java.awt.Font("Courier New", 1, 34)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("CONSULTA DE EMPLEADOS");
        jLabel23.setOpaque(true);

        jLabel26.setBackground(new java.awt.Color(0, 0, 0));
        jLabel26.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("ID USUARIO:");

        btnTablaUsusarios.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnTablaUsusarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/RLL-buscar.png"))); // NOI18N
        btnTablaUsusarios.setText(" BUSCAR USUARIO");
        btnTablaUsusarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTablaUsusariosActionPerformed(evt);
            }
        });

        jLabel27.setBackground(new java.awt.Color(0, 0, 0));
        jLabel27.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 0));
        jLabel27.setText("NOMBRE:");

        jLabel32.setBackground(new java.awt.Color(0, 0, 0));
        jLabel32.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 0));
        jLabel32.setText("CONTRASEÑA:");

        jLabel33.setBackground(new java.awt.Color(0, 0, 0));
        jLabel33.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 0, 0));
        jLabel33.setText("APELLIDO PATERNO:");

        jLabel34.setBackground(new java.awt.Color(0, 0, 0));
        jLabel34.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 0, 0));
        jLabel34.setText("APELLIDO MATERNO:");

        jLabel35.setBackground(new java.awt.Color(0, 0, 0));
        jLabel35.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 0, 0));
        jLabel35.setText("FECHA DE NACIMIENTO:");

        jLabel36.setBackground(new java.awt.Color(0, 0, 0));
        jLabel36.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 0, 0));
        jLabel36.setText("CORREO ELECTRONICO:");

        jLabel37.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 0, 0));
        jLabel37.setText("TELEFONO CELULAR:");

        jLabel38.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 0, 0));
        jLabel38.setText("TELEFONO CASA:");

        jLabel39.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(0, 0, 0));
        jLabel39.setText("CARGO:");

        jLabel40.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 0, 0));
        jLabel40.setText("DOMICILIO:");

        txtDomicilioArea.setColumns(20);
        txtDomicilioArea.setRows(5);
        jScrollPane6.setViewportView(txtDomicilioArea);

        jLabel49.setForeground(new java.awt.Color(0, 0, 0));
        jLabel49.setText("ESTATUS:");

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-limpiar24.png"))); // NOI18N
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel35)
                            .addComponent(jLabel37)
                            .addComponent(jLabel38)
                            .addComponent(jLabel39)
                            .addComponent(jLabel49)
                            .addComponent(jLabel40))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblFechaNacimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCorreoElectronico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTelCelular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCargo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTelCasa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEstatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel34)
                                .addGap(18, 18, 18)
                                .addComponent(lblApellidoMaterno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel33)
                                .addGap(18, 18, 18)
                                .addComponent(lblApellidoPaterno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addGap(103, 103, 103)
                                .addComponent(jLabel27)
                                .addGap(18, 18, 18)
                                .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel32)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCIDUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18)
                        .addComponent(btnTablaUsusarios)
                        .addGap(18, 18, 18)
                        .addComponent(jButton15)))
                .addContainerGap(109, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtCIDUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTablaUsusarios)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(lblPass, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(lblApellidoPaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(lblApellidoMaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(lblFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(lblCorreoElectronico, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(lblTelCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(lblTelCasa, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(lblCargo, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(lblEstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(102, Short.MAX_VALUE))
        );

        jLabel28.setBackground(new java.awt.Color(0, 102, 153));
        jLabel28.setFont(new java.awt.Font("Courier New", 0, 20)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("EN CASO DE EMERGENCIA LLAMAR A:");
        jLabel28.setOpaque(true);

        jLabel29.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 0, 0));
        jLabel29.setText("NOMBRE:");

        jLabel30.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 0));
        jLabel30.setText("TELEFONO:");

        jLabel31.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 0));
        jLabel31.setText("DOMICILIO:");

        txtConsultaEmergencia.setColumns(20);
        txtConsultaEmergencia.setRows(5);
        jScrollPane5.setViewportView(txtConsultaEmergencia);

        jLabel41.setBackground(new java.awt.Color(0, 102, 153));
        jLabel41.setFont(new java.awt.Font("Courier New", 0, 20)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("REDES SOCIALES:");
        jLabel41.setOpaque(true);

        jLabel42.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 0, 0));
        jLabel42.setText("FACEBOOK:");

        jLabel43.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 0, 0));
        jLabel43.setText("TWITTER:");

        jLabel44.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(0, 0, 0));
        jLabel44.setText("INSTAGRAM:");

        jLabel45.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 0, 0));
        jLabel45.setText("TIKTOK:");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel29))
                                .addGap(25, 25, 25)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblNombreCompletoConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTelefonoEmergencia, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel43)
                                    .addComponent(jLabel44)
                                    .addComponent(jLabel42)
                                    .addComponent(jLabel45))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblFacebook, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTwitter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblInstagram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTiktok, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))))
                        .addGap(0, 302, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNombreCompletoConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTelefonoEmergencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addComponent(jLabel41)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(lblFacebook, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(lblTwitter, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(lblInstagram, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTiktok, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ConsultaCompletaLayout = new javax.swing.GroupLayout(ConsultaCompleta);
        ConsultaCompleta.setLayout(ConsultaCompletaLayout);
        ConsultaCompletaLayout.setHorizontalGroup(
            ConsultaCompletaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConsultaCompletaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ConsultaCompletaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(ConsultaCompletaLayout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ConsultaCompletaLayout.setVerticalGroup(
            ConsultaCompletaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConsultaCompletaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ConsultaCompletaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel4.add(ConsultaCompleta, "card2");

        jLabel50.setBackground(new java.awt.Color(0, 102, 153));
        jLabel50.setFont(new java.awt.Font("Courier New", 1, 36)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("Reporte Incidencias");
        jLabel50.setOpaque(true);

        tablaReportes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(tablaReportes);

        jLabel51.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel51.setText("FECHA INICIAL:");

        jButton6.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-actualizarTablas.png"))); // NOI18N
        jButton6.setText(" ACTUALIZAR TABLA");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        Fecha1.setDateFormatString("yyyy-MM-dd");

        jLabel54.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel54.setText("A");

        jLabel55.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel55.setText("FECHA FINAL:");

        Fecha2.setDateFormatString("yyyy-MM-dd");

        jButton3.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-buscarR.png"))); // NOI18N
        jButton3.setText(" BUSCAR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-pdf-IG.png"))); // NOI18N
        jButton9.setText(" REPORTE GENERAL");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton11.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-pdf-IG.png"))); // NOI18N
        jButton11.setText(" REPORTE INDIVIDUAL");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ReportesLayout = new javax.swing.GroupLayout(Reportes);
        Reportes.setLayout(ReportesLayout);
        ReportesLayout.setHorizontalGroup(
            ReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, 1506, Short.MAX_VALUE)
                    .addComponent(jScrollPane7)
                    .addGroup(ReportesLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Fecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel54)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel55)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Fecha2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addGap(47, 47, 47)
                        .addComponent(jButton9)
                        .addGap(18, 18, 18)
                        .addComponent(jButton11)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReportesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addGap(32, 32, 32))
        );
        ReportesLayout.setVerticalGroup(
            ReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel50)
                .addGap(18, 18, 18)
                .addGroup(ReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReportesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(ReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Fecha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(ReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel54))
                            .addComponent(Fecha1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6)
                .addGap(17, 17, 17))
        );

        jPanel4.add(Reportes, "card5");

        jLabel52.setBackground(new java.awt.Color(204, 0, 0));
        jLabel52.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("ELIMINACIÓN DE FOLIOS");
        jLabel52.setOpaque(true);

        jLabel53.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel53.setText("NO. FOLIO:");

        btnEliminarFolio.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnEliminarFolio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-eliminar.png"))); // NOI18N
        btnEliminarFolio.setText(" ELIMINAR");
        btnEliminarFolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarFolioActionPerformed(evt);
            }
        });

        tablaFolios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane8.setViewportView(tablaFolios);

        jButton12.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-actualizarTablas.png"))); // NOI18N
        jButton12.setText(" ACTUALIZAR TABLA");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel56.setText("MOTIVO:");

        jLabel57.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel57.setText("SOLICITANTE:");

        jLabel58.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel58.setText("ID USUARIO:");

        jLabel61.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel61.setText("AUTORIZADO POR:");

        txtMotivo.setColumns(20);
        txtMotivo.setRows(5);
        jScrollPane10.setViewportView(txtMotivo);

        fecha.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout EliminarFoliosLayout = new javax.swing.GroupLayout(EliminarFolios);
        EliminarFolios.setLayout(EliminarFoliosLayout);
        EliminarFoliosLayout.setHorizontalGroup(
            EliminarFoliosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EliminarFoliosLayout.createSequentialGroup()
                .addGroup(EliminarFoliosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EliminarFoliosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, 1506, Short.MAX_VALUE))
                    .addGroup(EliminarFoliosLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(EliminarFoliosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel53)
                            .addComponent(jLabel56))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(EliminarFoliosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(EliminarFoliosLayout.createSequentialGroup()
                                .addComponent(txtNoFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jLabel57)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSolicitante, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(jLabel58)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIDusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addGroup(EliminarFoliosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminarFolio)
                            .addGroup(EliminarFoliosLayout.createSequentialGroup()
                                .addComponent(jLabel61)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAutorizado, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EliminarFoliosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EliminarFoliosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton12)
                .addGap(29, 29, 29))
        );
        EliminarFoliosLayout.setVerticalGroup(
            EliminarFoliosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EliminarFoliosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EliminarFoliosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(EliminarFoliosLayout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addGap(22, 22, 22)
                        .addGroup(EliminarFoliosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel53)
                            .addComponent(txtNoFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel57)
                            .addComponent(txtSolicitante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel58)
                            .addComponent(txtIDusuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel61)
                            .addComponent(txtAutorizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(EliminarFoliosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EliminarFoliosLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(EliminarFoliosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel56)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(EliminarFoliosLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(btnEliminarFolio)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton12)
                .addContainerGap())
        );

        jPanel4.add(EliminarFolios, "card6");

        jLabel59.setBackground(new java.awt.Color(0, 102, 153));
        jLabel59.setFont(new java.awt.Font("Courier New", 1, 34)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel59.setText("FOLIOS ELIMINADOS");
        jLabel59.setOpaque(true);

        tablaFoliosEliminados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane9.setViewportView(tablaFoliosEliminados);

        btnActFoliosElimi.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnActFoliosElimi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-actualizarTablas.png"))); // NOI18N
        btnActFoliosElimi.setText(" ACTUALIZAR TABLA");
        btnActFoliosElimi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActFoliosElimiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ConsultaFoliosEliminadosLayout = new javax.swing.GroupLayout(ConsultaFoliosEliminados);
        ConsultaFoliosEliminados.setLayout(ConsultaFoliosEliminadosLayout);
        ConsultaFoliosEliminadosLayout.setHorizontalGroup(
            ConsultaFoliosEliminadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConsultaFoliosEliminadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ConsultaFoliosEliminadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 1506, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ConsultaFoliosEliminadosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnActFoliosElimi)
                .addGap(27, 27, 27))
        );
        ConsultaFoliosEliminadosLayout.setVerticalGroup(
            ConsultaFoliosEliminadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConsultaFoliosEliminadosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnActFoliosElimi)
                .addGap(19, 19, 19))
        );

        jPanel4.add(ConsultaFoliosEliminados, "card7");

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 370, 1530, 640));

        jPanel2.setBackground(new java.awt.Color(179, 179, 179));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/CerrarVentana.png"))); // NOI18N
        jButton7.setContentAreaFilled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/MinimizarVentana.png"))); // NOI18N
        jButton8.setContentAreaFilled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("   ADMINISTRADOR");
        jLabel22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1648, Short.MAX_VALUE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, -1));

        jPanel12.setBackground(new java.awt.Color(224, 224, 224));

        jLabel46.setBackground(new java.awt.Color(255, 255, 255));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/CENACI_Amin_Logo.png"))); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel1.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 320, 310));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultaActionPerformed
        AgregarModificar.setVisible(false);
        Eliminar.setVisible(false);
        ConsultaCompleta.setVisible(false);
        Reportes.setVisible(true);
        EliminarFolios.setVisible(false);
        ConsultaFoliosEliminados.setVisible(false);
    }//GEN-LAST:event_btnConsultaActionPerformed

    private void btnAgregarModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarModificarActionPerformed
        AgregarModificar.setVisible(true);
        Eliminar.setVisible(false);
        ConsultaCompleta.setVisible(false);
        Reportes.setVisible(false);
        EliminarFolios.setVisible(false);
        ConsultaFoliosEliminados.setVisible(false);
    }//GEN-LAST:event_btnAgregarModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        AgregarModificar.setVisible(false);
        Eliminar.setVisible(true);
        ConsultaCompleta.setVisible(false);
        Reportes.setVisible(false);
        EliminarFolios.setVisible(false);
        ConsultaFoliosEliminados.setVisible(false);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // Cerrar Ventana
        this.setVisible(false);
        System.exit(0);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // Minimizar la Ventana
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void btnNuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoUsuarioActionPerformed
        // Para no dejar campos vacios
        if( txtNombre.getText().trim().isEmpty() || txtApellidoP.getText().trim().isEmpty() || txtApellidoM.getText().trim().isEmpty() || txtIDUser.getText().trim().isEmpty() 
            || txtPass.getText().trim().isEmpty() || txtTelCelular.getText().trim().isEmpty() || txtDomicilioEmpleado.getText().trim().isEmpty()){
            Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/Warning.png"));
            JOptionPane.showMessageDialog(null,"Por Favor Verifique!! \nQue No Haya Dejado Campos Vacios", "Warning", JOptionPane.PLAIN_MESSAGE, icono);
        }else{
        try{
            ps = con.prepareStatement("insert into Usuarios(nombre,apellidoP,apellidoM,idUsuario,pass,tipoCargo,fechaNacimiento,facebook,twitter,instagram,tiktok,correo," +
"domicilio,telCelular,telCasa,nombreCompleto,telefono,domicilio2,estatus)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtApellidoP.getText());
            ps.setString(3, txtApellidoM.getText());
            ps.setString(4, txtIDUser.getText());
            ps.setString(5, txtPass.getText());
            ps.setString(6, cbxTipoCargo.getSelectedItem().toString());
            ps.setString(7, txtFechaNacimiento.getText());
            ps.setString(8, txtFacebook.getText());
            ps.setString(9, txtTwitter.getText());
            ps.setString(10, txtInstagram.getText());
            ps.setString(11, txtTiktok.getText());
            ps.setString(12, txtCorreo.getText());
            ps.setString(13, txtDomicilioEmpleado.getText());
            ps.setString(14, txtTelCelular.getText());
            ps.setString(15, txtTelCasa.getText());
            ps.setString(16, txtNombreCompletoE.getText());
            ps.setString(17, txtTelefonoE.getText());
            ps.setString(18, txtDomicilioE.getText());
            ps.setString(19, txtEstatusUser.getText());
            
            int res = ps.executeUpdate();
            
            if(res > 0){
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/Aceptar.png"));
                JOptionPane.showMessageDialog(null, "Usuario Registrado", "Mensaje", JOptionPane.PLAIN_MESSAGE, icono);
                limpiarCajas();
                EliminacionUsuarios();
            }else{
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/ErrorX.png"));
                JOptionPane.showMessageDialog(null, "Error al Registrar Usuario", "Error", JOptionPane.PLAIN_MESSAGE, icono);
                EliminacionUsuarios();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        }
    }//GEN-LAST:event_btnNuevoUsuarioActionPerformed

    private void btnAgregarModificarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarModificarMouseClicked
        
    }//GEN-LAST:event_btnAgregarModificarMouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Entrar al modo Monitorista
        String botones[] = {"Modo Operador", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(this, "¿Esta seguro de entrar a modo Operador?", "Modo Operador", 0, 0, null, botones, this);
        if (eleccion == JOptionPane.YES_OPTION) {
            //Modo Monitorista
            Res_Lla ventaPrin = new Res_Lla();
            ventaPrin.setVisible(true);
            this.setVisible(false);
            dispose();
        } else if (eleccion == JOptionPane.NO_OPTION) {
            System.out.println("Cierre Cancelado");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Entrar al modo Monitorista
        String botones[] = {"Modo Despachador", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(this, "¿Esta seguro de entrar al modo Despachador?", "Modo Despachador", 0, 0, null, botones, this);
        if (eleccion == JOptionPane.YES_OPTION) {
            //Modo Monitorista
            ventana_antena ventaPrin = new ventana_antena();
            ventaPrin.setVisible(true);
            this.setVisible(false);
        } else if (eleccion == JOptionPane.NO_OPTION) {
            System.out.println("Cierre Cancelado");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Para no dejar campos vacios
        if( txtIDUsuarioEliminar.getText().trim().isEmpty()){
            Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/Warning.png"));
            JOptionPane.showMessageDialog(null,"No ha Seleccionado un ID!!", "Warning", JOptionPane.PLAIN_MESSAGE, icono);
        }else{
        // Eliminar un Usuario
         try {
            ps = con.prepareStatement("UPDATE Usuarios SET estatus=? WHERE idUsuario=?");
            ps.setString(1, cbxEstatus.getSelectedItem().toString());
            ps.setString(2, txtIDUsuarioEliminar.getText());

            int res = ps.executeUpdate();

            if (res > 0) {
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/Aceptar.png"));
                JOptionPane.showMessageDialog(null, "Usuario Inactivo", "Mensaje", JOptionPane.PLAIN_MESSAGE, icono);
                limpiarCajas();
                cbxEstatus.setSelectedIndex(0);
                EliminacionUsuarios();
            } else {
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/ErrorX.png"));
                JOptionPane.showMessageDialog(null, "Error de Modificacion", "Error", JOptionPane.PLAIN_MESSAGE, icono);
                limpiarCajas();
                cbxEstatus.setSelectedIndex(0);
                EliminacionUsuarios();
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnTablaUsusariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTablaUsusariosActionPerformed
        /// Abrir JDialog Localidades
        listaUsusariosConsulta();
        ListaUsuariosConsulta.setSize(370, 470);//Asignas tamaño (x,y)
        ListaUsuariosConsulta.setLocationRelativeTo(null);//posicion
        ListaUsuariosConsulta.setModal(true);//que se ubique al centro
        ListaUsuariosConsulta.setVisible(true);//que se haga visible
    }//GEN-LAST:event_btnTablaUsusariosActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        EliminacionUsuarios();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnConsulta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsulta1ActionPerformed
        AgregarModificar.setVisible(false);
        Eliminar.setVisible(false);
        ConsultaCompleta.setVisible(true);
        Reportes.setVisible(false);
        EliminarFolios.setVisible(false);
        ConsultaFoliosEliminados.setVisible(false);
    }//GEN-LAST:event_btnConsulta1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Actualizar tabla de reportes
        tablaReportes();
        Fecha1.setDate(null);
        Fecha2.setDate(null);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ConsultaReportesFecha();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String fdate = dateformat.format(Fecha1.getDate());

        SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
        String todate = dateformat1.format(Fecha2.getDate());

        HashMap a = new HashMap();
        a.put("fromd", fdate);
        a.put("tod", todate);
        a.put("IDuser", IDuser);
        a.put("Name", Name);
        try {
            // Obtener la ruta del archivo Jasper dentro del classpath
            String reportPath = "Reportes/CENACI.jasper";
            java.io.InputStream inputStream = getClass().getClassLoader().getResourceAsStream(reportPath);

            if (inputStream == null) {
                // Si el archivo no se encuentra, mostrar un mensaje de error o lanzar una excepción
                System.out.println("No se pudo encontrar el archivo Jasper.");
                System.out.println("La ruta es: " + reportPath);
                JOptionPane.showMessageDialog(null, "La ruta es: " + reportPath 
                        + "\ninputStream: " + inputStream);
                return;
            }
            JOptionPane.showMessageDialog(null, "La ruta es: " + reportPath
            + "\ninputStream: " + inputStream);
            JasperReport jr = (JasperReport) JRLoader.loadObject(inputStream);
            JasperPrint print = JasperFillManager.fillReport(jr, a, con);
            JasperViewer view = new JasperViewer(print, false);
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            view.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(Administrador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // Para Respaldar BD
        ExportacionBD export = new ExportacionBD();
        export.setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        TablaFolios();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void btnEliminarFoliosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarFoliosActionPerformed
        AgregarModificar.setVisible(false);
        Eliminar.setVisible(false);
        ConsultaCompleta.setVisible(false);
        Reportes.setVisible(false);
        EliminarFolios.setVisible(true);
        ConsultaFoliosEliminados.setVisible(false);
    }//GEN-LAST:event_btnEliminarFoliosActionPerformed

    private void btnConFoliosElimiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConFoliosElimiActionPerformed
        AgregarModificar.setVisible(false);
        Eliminar.setVisible(false);
        ConsultaCompleta.setVisible(false);
        Reportes.setVisible(false);
        EliminarFolios.setVisible(false);
        ConsultaFoliosEliminados.setVisible(true);
    }//GEN-LAST:event_btnConFoliosElimiActionPerformed

        //salto de linea
    private void btnEliminarFolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarFolioActionPerformed
        // Antes de eliminar el folio de la tabla principal, lo registramos en la tabla de foliosEliminados
        // Para tener el folio que sera eliminado y el motivo de su eliminacion
        // Para no dejar campos vacios
        if( txtNoFolio.getText().trim().isEmpty() || txtSolicitante.getText().trim().isEmpty() || txtIDusuario.getText().trim().isEmpty() || txtAutorizado.getText().trim().isEmpty() 
            || txtMotivo.getText().trim().isEmpty()){
            Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/Warning.png"));
            JOptionPane.showMessageDialog(null,"Por Favor Verifique!! \nQue No Haya Dejado Campos Vacios", "Warning", JOptionPane.PLAIN_MESSAGE, icono);
        }else{
        try{
            ps = con.prepareStatement("insert into foliosEliminados(folio,fecha,hora,solicitante,idUsuario,autorizado,motivo)values(?,?,?,?,?,?,?)");
            ps.setString(1, txtNoFolio.getText());
            ps.setString(2, ((JTextField) fecha.getDateEditor().getUiComponent()).getText());
            ps.setString(3, hora + ":" + minutos + ":" + segundos);
            ps.setString(4, txtSolicitante.getText());
            ps.setString(5, txtIDusuario.getText());
            ps.setString(6, txtAutorizado.getText());
            ps.setString(7, txtMotivo.getText());
            int res = ps.executeUpdate();
            
            if(res > 0){
                txtSolicitante.setText(null);
                txtIDusuario.setText(null);
                txtMotivo.setText(null);
            }else{
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/ErrorX.png"));
                JOptionPane.showMessageDialog(null, "Error en foliosEliminados", "Error", JOptionPane.PLAIN_MESSAGE, icono);
                txtNoFolio.setText(null); 
                txtSolicitante.setText(null);
                txtIDusuario.setText(null);
                txtMotivo.setText(null);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        // Antes de eliminarlo por completo, extraemos toda la informacion de
        // ese folio para registrarlo en otra tabla, donde tendremos la info
        // completa que tenia el folio al ser eliminado.
        try {
            ps = con.prepareStatement("select * from Emergencia");
            ResultSet rs = null;
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString(1).equals(txtNoFolio.getText())) {
                        Ehora = rs.getString(2);
                        Efecha = rs.getString(3);
                        EidUsuario = rs.getString(4);
                        EidEquipo = rs.getString(5);
                        EtelefonoLlamada = rs.getString(6);
                        Esemaforo = rs.getString(7);
                        Efase = rs.getString(8);
                        EdescripcionHeridos = rs.getString(9);
                        Ereporte = rs.getString(10);
                        EseguridadPublica = rs.getString(11);
                        EaguaPotable = rs.getString(12);
                        EproteccionCivil = rs.getString(13);
                        EinstanciaMujer = rs.getString(14);
                        EunidadUno = rs.getString(15);
                        EotrosServicios = rs.getString(16);
                        EincidenteDenuncia = rs.getString(17);
                        Eubicacion = rs.getString(18);
                        EdescripcionLugar = rs.getString(19);
                        Einvolucrados = rs.getString(20);
                        EnombreDenunciante = rs.getString(21);
                        Etelefono = rs.getString(22);
                        Eestatus = rs.getString(23);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error folios completos");
        }
        // Una ves extraidos los datos del folio antes de ser eliminado, los mandamos
        // a la tabla de folios completos eliminados para tener ese registro
        try{
            ps = con.prepareStatement("insert into infoCompFoliosEliminados(efolio,ehora,efecha,eidUsuario," +
    "eidEquipo,etelefonoLlamada,esemaforo,efase,edescripcionHeridos,ereporte,eseguridadPublica,eaguaPotable," +
    "eproteccionCivil,einstanciaMujer,eunidadUno,eotrosServicios,eincidenteDenuncia,eubicacion,edescripcionLugar," +
    "einvolucrados,enombreDenunciante,etelefono,eestatus)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, txtNoFolio.getText());
            ps.setString(2, Ehora);
            ps.setString(3, Efecha);
            ps.setString(4, EidUsuario);
            ps.setString(5, EidEquipo);
            ps.setString(6, EtelefonoLlamada);
            ps.setString(7, Esemaforo);
            ps.setString(8, Efase);
            ps.setString(9, EdescripcionHeridos);
            ps.setString(10, Ereporte);
            ps.setString(11, EseguridadPublica);
            ps.setString(12, EaguaPotable);
            ps.setString(13, EproteccionCivil);
            ps.setString(14, EinstanciaMujer);
            ps.setString(15, EunidadUno);
            ps.setString(16, EotrosServicios);
            ps.setString(17, EincidenteDenuncia);
            ps.setString(18, Eubicacion);
            ps.setString(19, EdescripcionLugar);
            ps.setString(20, Einvolucrados);
            ps.setString(21, EnombreDenunciante);
            ps.setString(22, Etelefono);
            ps.setString(23, Eestatus);
            
            int res = ps.executeUpdate();
            
            if(res > 0){
            }else{
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        // En esta parte ahora si pasaremos a su eliminacion de la tabla principal de folios
        // Codigo para eliminar el folio
        try {
            String sql = "delete from Emergencia where folio='" + txtNoFolio.getText() + "'";
            Statement st = con.createStatement();
            int n = st.executeUpdate(sql);
            if (n > 0) {
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/Aceptar.png"));
                JOptionPane.showMessageDialog(null, "Folio Eliminado Correctamente", "Mensaje", JOptionPane.PLAIN_MESSAGE, icono);
                txtNoFolio.setText(null);
                TablaFolios();
                FoliosEliminados();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en eliminacion" + e.getMessage());
            txtNoFolio.setText(null);
            TablaFolios();
        }
        }
    }//GEN-LAST:event_btnEliminarFolioActionPerformed

    private void btnActFoliosElimiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActFoliosElimiActionPerformed
        FoliosEliminados();
    }//GEN-LAST:event_btnActFoliosElimiActionPerformed

    private void txtFechainfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechainfoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechainfoActionPerformed

    private void txtAguainfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAguainfoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAguainfoActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        ReporteIndividual report = new ReporteIndividual();
        report.setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void txtIDUserKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDUserKeyPressed
       // buscar usuario y modificarlo
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            buscarUsuario();
        }
    }//GEN-LAST:event_txtIDUserKeyPressed
    public void buscarUsuario(){
        try {
            ps = con.prepareStatement("select * from Usuarios");
            ResultSet rs = null;
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString(4).equals(txtIDUser.getText())) {
                        JOptionPane.showMessageDialog(null, "USUARIO ENCONTRADO");
                        txtNombre.setText(rs.getString(1));
                        txtApellidoP.setText(rs.getString(2));
                        txtApellidoM.setText(rs.getString(3));
                        txtFechaNacimiento.setText(rs.getString(7));
                        txtCorreo.setText(rs.getString(12));
                        txtTelCelular.setText(rs.getString(14));
                        txtTelCasa.setText(rs.getString(15));
                        txtPass.setText(rs.getString(5));
                        cbxTipoCargo.setSelectedItem(rs.getString(6));
                        txtDomicilioEmpleado.setText(rs.getString(13));
                        txtFacebook.setText(rs.getString(8));
                        txtTwitter.setText(rs.getString(9));
                        txtInstagram.setText(rs.getString(10));
                        txtTiktok.setText(rs.getString(11));
                        txtNombreCompletoE.setText(rs.getString(16));
                        txtTelefonoE.setText(rs.getString(17));
                        txtDomicilioE.setText(rs.getString(18));
                        txtEstatusUser.setText(rs.getString(19));
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "NO Conectado");
        }
    }
    private void txtApellidoPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidoPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoPActionPerformed

    private void txtIDUserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDUserKeyReleased
         
    }//GEN-LAST:event_txtIDUserKeyReleased

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // Modificar datos de usuario
        try {
            ps = con.prepareStatement("UPDATE Usuarios SET nombre=?,apellidoP=?,apellidoM=?,pass=?,tipoCargo=?,fechaNacimiento=?,facebook=?,twitter=?,instagram=?," +
            "tiktok=?,correo=?,domicilio=?,telCelular=?,telCasa=?,nombreCompleto=?,telefono=?,domicilio2=?,estatus=? WHERE idUsuario=?");
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtApellidoP.getText());
            ps.setString(3, txtApellidoM.getText());
            ps.setString(4, txtPass.getText());
            ps.setString(5, cbxTipoCargo.getSelectedItem().toString());
            ps.setString(6, txtFechaNacimiento.getText());
            ps.setString(7, txtFacebook.getText());
            ps.setString(8, txtTwitter.getText());
            ps.setString(9, txtInstagram.getText());
            ps.setString(10, txtTiktok.getText());
            ps.setString(11, txtCorreo.getText());
            ps.setString(12, txtDomicilioEmpleado.getText());
            ps.setString(13, txtTelCelular.getText());
            ps.setString(14, txtTelCasa.getText());
            ps.setString(15, txtNombreCompletoE.getText());
            ps.setString(16, txtTelefonoE.getText());
            ps.setString(17, txtDomicilioE.getText());
            ps.setString(18, txtEstatusUser.getText());
            ps.setString(19, txtIDUser.getText());

            int res = ps.executeUpdate();
            
            if (res > 0) {
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/Aceptar.png"));
                JOptionPane.showMessageDialog(null, "Registro Modificado", "Mensaje", JOptionPane.PLAIN_MESSAGE, icono);
                limpiarcamposModificados();
            } else {
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/ErrorX.png"));
                JOptionPane.showMessageDialog(null, "Registro No Modificado", "Error", JOptionPane.PLAIN_MESSAGE, icono);
                limpiarcamposModificados();
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // Limpia los campos de modificaciones
        limpiarcamposModificados();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // Limpiar campos
        txtCIDUsuario.setText("");
        lblPass.setText("");
        lblNombre.setText("");
        lblApellidoPaterno.setText("");
        lblApellidoMaterno.setText("");
        lblFechaNacimiento.setText("");
        lblCorreoElectronico.setText("");
        lblTelCelular.setText("");
        lblTelCasa.setText("");
        lblCargo.setText("");
        lblEstatus.setText("");
        txtDomicilioArea.setText("");
        lblNombreCompletoConsulta.setText("");
        lblTelefonoEmergencia.setText("");
        txtConsultaEmergencia.setText("");
        lblFacebook.setText("");
        lblTwitter.setText("");
        lblInstagram.setText("");
        lblTiktok.setText("");
    }//GEN-LAST:event_jButton15ActionPerformed
    public void limpiarcamposModificados(){
        txtNombre.setText("");
        txtApellidoP.setText("");
        txtApellidoM.setText("");
        txtPass.setText("");
        cbxTipoCargo.setSelectedItem(1);
        txtFechaNacimiento.setText("");
        txtFacebook.setText("");
        txtTwitter.setText("");
        txtInstagram.setText("");
        txtTiktok.setText("");
        txtCorreo.setText("");
        txtDomicilioEmpleado.setText("");
        txtTelCelular.setText("");
        txtTelCasa.setText("");
        txtNombreCompletoE.setText("");
        txtTelefonoE.setText("");
        txtDomicilioE.setText("");
        txtIDUser.setText("");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Administrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Administrador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AgregarModificar;
    private javax.swing.JPanel ConsultaCompleta;
    private javax.swing.JPanel ConsultaFoliosEliminados;
    private javax.swing.JPanel Eliminar;
    private javax.swing.JPanel EliminarFolios;
    private com.toedter.calendar.JDateChooser Fecha1;
    private com.toedter.calendar.JDateChooser Fecha2;
    private javax.swing.JDialog FoliosEliminados;
    private javax.swing.JDialog ListaUsuariosConsulta;
    private javax.swing.JPanel Reportes;
    private javax.swing.JButton btnActFoliosElimi;
    private javax.swing.JButton btnAgregarModificar;
    private javax.swing.JButton btnConFoliosElimi;
    private javax.swing.JButton btnConsulta;
    private javax.swing.JButton btnConsulta1;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminarFolio;
    private javax.swing.JButton btnEliminarFolios;
    private javax.swing.JButton btnNuevoUsuario;
    private javax.swing.JButton btnTablaUsusarios;
    private javax.swing.JComboBox<String> cbxEstatus;
    private javax.swing.JComboBox<String> cbxTipoCargo;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JLabel labelMensajeNotas;
    private javax.swing.JLabel lblApellidoMaterno;
    private javax.swing.JLabel lblApellidoPaterno;
    private javax.swing.JLabel lblCargo;
    private javax.swing.JLabel lblCorreoElectronico;
    private javax.swing.JLabel lblEstatus;
    private javax.swing.JLabel lblFacebook;
    private javax.swing.JLabel lblFechaNacimiento;
    private javax.swing.JLabel lblInstagram;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNombreCompletoConsulta;
    private javax.swing.JLabel lblPass;
    private javax.swing.JLabel lblTelCasa;
    private javax.swing.JLabel lblTelCelular;
    private javax.swing.JLabel lblTelefonoEmergencia;
    private javax.swing.JLabel lblTiktok;
    private javax.swing.JLabel lblTwitter;
    private javax.swing.JTable tablaFolios;
    private javax.swing.JTable tablaFoliosEliminados;
    private javax.swing.JTable tablaListaConsulta;
    private javax.swing.JTable tablaNotasFoliosEliminados;
    private javax.swing.JTable tablaReportes;
    private javax.swing.JTable tablaUsuarios;
    private javax.swing.JTextField txtAguainfo;
    private javax.swing.JTextField txtApellidoM;
    private javax.swing.JTextField txtApellidoP;
    private javax.swing.JTextField txtAutorizado;
    private javax.swing.JTextField txtCIDUsuario;
    private javax.swing.JTextArea txtConsultaEmergencia;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDenuncianteinfo;
    private javax.swing.JTextArea txtDescripcionLugarinfo;
    private javax.swing.JTextArea txtDescripcioninfo;
    private javax.swing.JTextArea txtDomicilioArea;
    private javax.swing.JTextArea txtDomicilioE;
    private javax.swing.JTextArea txtDomicilioEmpleado;
    private javax.swing.JTextField txtEstatusUser;
    private javax.swing.JTextField txtEstatusinfo;
    private javax.swing.JTextField txtFacebook;
    private javax.swing.JTextField txtFaseinfo;
    private javax.swing.JTextField txtFechaNacimiento;
    private javax.swing.JTextField txtFechainfo;
    private javax.swing.JTextField txtFolioinfo;
    private javax.swing.JTextField txtHorainfo;
    private javax.swing.JTextField txtIDUser;
    private javax.swing.JTextField txtIDUsuarioEliminar;
    private javax.swing.JTextField txtIDusuario;
    private javax.swing.JTextArea txtIncidenteinfo;
    private javax.swing.JTextField txtInstagram;
    private javax.swing.JTextField txtInstanciainfo;
    private javax.swing.JTextField txtInvolucradosinfo;
    private javax.swing.JTextArea txtMotivo;
    private javax.swing.JTextField txtNoFolio;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombreCompletoE;
    private javax.swing.JTextField txtOtrosinfo;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtProteccioninfo;
    private javax.swing.JTextArea txtReporteinfo;
    private javax.swing.JTextField txtSeguridadinfo;
    private javax.swing.JTextField txtSemaforoinfo;
    private javax.swing.JTextField txtSolicitante;
    private javax.swing.JTextField txtTelCasa;
    private javax.swing.JTextField txtTelCelular;
    private javax.swing.JTextField txtTelefono2info;
    private javax.swing.JTextField txtTelefonoE;
    private javax.swing.JTextField txtTelefonoinfo;
    private javax.swing.JTextField txtTiktok;
    private javax.swing.JTextField txtTwitter;
    private javax.swing.JTextArea txtUbicacioninfo;
    private javax.swing.JTextField txtUnidadinfo;
    private javax.swing.JTextField txtidEquipoinfo;
    private javax.swing.JTextField txtidUsuarioinfo;
    // End of variables declaration//GEN-END:variables
}