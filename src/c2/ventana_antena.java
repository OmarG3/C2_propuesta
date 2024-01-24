package c2;

import static Login.Acceso.IDuser;
import static Login.Acceso.Tipe;
import java.awt.Color;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author king_
 */
public class ventana_antena extends javax.swing.JFrame implements Runnable{
    //Mandamos a llamar a la conexión
    Conexion conector = new Conexion();
    //"con" se usara para realizar distintos procesos
    Connection con = conector.getConexion();
    ///
    PreparedStatement ps;
    ///
    String hora, minutos, segundos, ampm;
    Thread h1;
    //horas de informes
    String horaInforme = "NULL";
    String horaDespacho = "NULL";
    String horaInsidente = "NULL";
    //
    String horasNotas = "NULL";
    //
    String estatus = "Cerrado";
    ///TIMER
    Timer timer = new Timer();
    
    public ventana_antena() {
        initComponents();
        this.setLocationRelativeTo(null);
        txtIDAnt.setText(IDuser);
        txtHora.setEditable(false);
        txtFecha.setEditable(false);
        txtEstadoReporte.setEditable(false);
        txtEquiMon.setEditable(false);
        txtIDAnt.setEditable(false);
        txtIDMon.setEditable(false);
        txtIDEquiAnt.setEditable(false);
        txtNotasCompletas.setText(null);
        //Para que el administrador pueda regresar
        privilegiosButton();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ImagenesExtras/Icono_Ventanas.png")));
        //
        h1 = new Thread(this);
        h1.start();
        setVisible(true);
        //
        btnSeguridadPublica.setVisible(false);
        btnAguaPotable.setVisible(false);
        btnInstanciaMujer.setVisible(false);
        btnOtrosServicios.setVisible(false);
        btnProteccionCivil.setVisible(false);
        btnUnidadUno.setVisible(false);
        // Mostrar la consulta a los 2 segundos cada 8 segundos
        timer.schedule(tarea, 2000, 8000);
        // Linea de doble clic para el folio mandado a llamar desde la tabla
        dobleClicJtable();
        dobleClicNotas();
        // CAJAS DE TEXTO DE REPORTE COMPLETO
        txtIncidenteDenuncia.setEditable(false);
        txtUbicacion.setEditable(false);
        txtDescripcionLugar.setEditable(false);
        txtHeridos.setEditable(false);
        txtReporte.setEditable(false);
        txtLlamadaRecibida.setEditable(false);
        txtNombreDenunciante.setEditable(false);
        txtNumeroProporcionado.setEditable(false);
        // Establecer el JLabel de Informe de reporte de estos colores
        JlabelSemaforo.setBackground(Color.BLUE);
        JlabelSemaforo.setForeground(Color.WHITE);
        //
        txtFolio.setEditable(false);
        // Para mostrar el nombre del equipo usado actualmente
        java.net.InetAddress localMachine = null; 
        try {
            localMachine = java.net.InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(VENTANA_LLAMADA.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtIDEquiAnt.setText(localMachine.getHostName());
        // Dar saltos de linea al llegar al borde de un area texto
        txtIncidenteDenuncia.setLineWrap(true);
        txtIncidenteDenuncia.setWrapStyleWord(true);
        txtUbicacion.setLineWrap(true);
        txtUbicacion.setWrapStyleWord(true);
        txtDescripcionLugar.setLineWrap(true);
        txtDescripcionLugar.setWrapStyleWord(true);
        txtHeridos.setLineWrap(true);
        txtHeridos.setWrapStyleWord(true);
        txtReporte.setLineWrap(true);
        txtReporte.setWrapStyleWord(true);
        txtNotasCompletas.setLineWrap(true);
        txtNotasCompletas.setWrapStyleWord(true);
        // Tamaño de las celdas
        tablaNotas.setModel(tableModel);
        txtreporteAntena.setEditable(false);
        // Inhabilitar botones para evitar que se hagan registros inecesarios
        btnHoraInforme.setEnabled(false);
        btnHoraDespacho.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnHorallegadaIncidente.setEnabled(false);
        jButton3.setEnabled(false);
        txtreporteAntena.setEditable(false);
    }
    // instance table model
    DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            //all cells false
            return false;
        }
    };
    // Mostrar y ocultar boton de regresar en modo administrador
    public void privilegiosButton(){
        try {
            ps = con.prepareStatement("select * from Usuarios");
            ResultSet rs = null;
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                        if (Tipe.equals("Recepcionista")){
                            btnRegresarAdmin.setVisible(false);
                        }else if(Tipe.equals("Despachador")){
                            btnRegresarAdmin.setVisible(false);
                        }else if(Tipe.equals("Administrador")){
                            btnRegresarAdmin.setVisible(true);
                        }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "NO Conectado");
            
        }
    }
    //LIMPIAR TABLA
    private void limpiarTabla() {
        DefaultTableModel temp = (DefaultTableModel) tablaNotas.getModel();
        int filas = tablaNotas.getRowCount();
        for (int a = 0; filas > a; a++) {
            temp.removeRow(0);
        }
    }
    //Mostramos la tabla de las incidencias cada 8 segundos
    TimerTask tarea = new TimerTask(){
        @Override
        public void run() {
            IncidenciasActuales();
            ColorSemaforo semaforo = new ColorSemaforo(1);
            tablaIncidencias.getColumnModel().getColumn(1).setCellRenderer(semaforo);
        }
    };
    
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    public void BuscarFolio(){
        try {
            ps = con.prepareStatement("select * from Emergencia where estatus = 'Abierto'");
            ResultSet rs = null;
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString(1).equals(txtFolio.getText())) {
                        JOptionPane.showMessageDialog(null, "Dato Encontrado");
                        txtHora.setText(rs.getString(2));
                        txtFecha.setText(rs.getString(3));
                        txtIDMon.setText(rs.getString(4));
                        txtEquiMon.setText(rs.getString(5));
                        txtIncidenteDenuncia.setText(rs.getString(17));
                        txtUbicacion.setText(rs.getString(18));
                        txtDescripcionLugar.setText(rs.getString(19));
                        txtHeridos.setText(rs.getString(9));
                        txtReporte.setText(rs.getString(10));
                        /// Agregamos privilegios sobre los numeros que se mostraran
                        // Solo el admin puede visualizar los numeros privados
                        if (Tipe.equals("Administrador")){
                            txtLlamadaRecibida.setText(rs.getString(6));
                            txtNombreDenunciante.setText(rs.getString(21));
                            txtNumeroProporcionado.setText(rs.getString(22));
                        } else if (Tipe.equals("Despachador") && rs.getString(6).equals("Num Priv..") || rs.getString(6).equals("Num Priv")){
                            txtLlamadaRecibida.setText(rs.getString(6));
                            txtNombreDenunciante.setText("Privado");
                            txtNumeroProporcionado.setText("Privado");
                        }else if (Tipe.equals("Despachador") && !rs.getString(6).equals("Num Priv..") || !rs.getString(6).equals("Num Priv")){
                            txtLlamadaRecibida.setText(rs.getString(6));
                            txtNombreDenunciante.setText(rs.getString(21));
                            txtNumeroProporcionado.setText(rs.getString(22));
                        }
                        txtEstadoReporte.setText(rs.getString(23));
                        if (rs.getString(11).equals("Seguridad Publica")){
                            btnSeguridadPublica.setVisible(true);
                        }if(rs.getString(12).equals("Agua Potable")){
                            btnAguaPotable.setVisible(true);
                        }if(rs.getString(13).equals("Proteccion Civil")){
                            btnProteccionCivil.setVisible(true);
                        }if(rs.getString(14).equals("Instancia de la Mujer")){
                            btnInstanciaMujer.setVisible(true);
                        }if(rs.getString(15).equals("Unidad de 1er Contacto")){
                            btnUnidadUno.setVisible(true);
                        }if(rs.getString(16).equals("Otros Servicios")){
                            btnOtrosServicios.setVisible(true);
                        }
                        if (rs.getString(7).equals("Rojo")) {
                            JlabelSemaforo.setBackground(Color.RED);
                            JlabelSemaforo.setForeground(Color.BLACK);
                        } else if(rs.getString(7).equals("Amarillo")){
                            JlabelSemaforo.setBackground(Color.YELLOW);
                            JlabelSemaforo.setForeground(Color.BLACK);
                        } else if(rs.getString(7).equals("Verde")){
                            JlabelSemaforo.setBackground(Color.GREEN);
                            JlabelSemaforo.setForeground(Color.BLACK);
                        }
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "NO Conectado");
        }
        ////modificar la fase del semaforo
        String fase1 = "Pendiente";
        try {
            ps = con.prepareStatement("UPDATE Emergencia SET fase=? WHERE folio=?");
            ps.setString(1, fase1);
            ps.setString(2, txtFolio.getText());
            int res = ps.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
        try {
            ps = con.prepareStatement("select * from Antena where folio ='" + txtFolio.getText()+"'");
            ResultSet rs = null;
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString(1).equals(txtFolio.getText())) {
                        txtUnidades.setText(rs.getString(10));
                        txtElementos.setText(rs.getString(11));
                        if (rs.getString(8).equals("NULL")){
                            btnHoraInforme.setEnabled(true);
                            txtreporteAntena.setEditable(false);
                            }else{
                            btnHoraInforme.setEnabled(false);
                            txtreporteAntena.setEditable(true);
                        }if(rs.getString(9).equals("NULL")){
                            btnHoraDespacho.setEnabled(true);
                            }else{
                            btnHoraDespacho.setEnabled(false);
                        }if(rs.getString(12).equals("NULL")){
                            btnHorallegadaIncidente.setEnabled(true);
                            }else{
                            btnHorallegadaIncidente.setEnabled(false);
                        }
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "NO Conectado");
        }
        
    }
    /// MANDAR A LLAMAR A TABLA LAS NOTAS INGRESADAS
    public void tablaNotas(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nota");//1
        modelo.addColumn("Hora");//7
        tablaNotas.setModel(modelo);
        String datos[] = new String[2];
        com.mysql.cj.xdevapi.Statement st;
        try {
            ps = con.prepareStatement("select * from Antena where folio ='" + txtFolio.getText()+"'");
            ResultSet rs = null;
            rs = ps.executeQuery();
            while (rs.next()) {
                datos[0] = rs.getString(13);
                datos[1] = rs.getString(14);
                modelo.addRow(datos);
            }
            tablaNotas.getColumnModel().getColumn(0).setPreferredWidth(745);
            tablaNotas.getColumnModel().getColumn(1).setPreferredWidth(5);
            tablaNotas.setModel(modelo);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    
    //Limpiar campos
    private void limpiarCajas() {
        txtFolio.setText(null);
        txtHora.setText(null);
        txtFecha.setText(null);
        txtIDMon.setText(null);
        txtEquiMon.setText(null);
        txtUnidades.setText(null);
        txtElementos.setText(null);
        txtReporte.setText(null);
        txtEstadoReporte.setText(null);
        btnHoraDespacho.setEnabled(true);
        btnHoraInforme.setEnabled(true);
        btnHorallegadaIncidente.setEnabled(true);
        btnSeguridadPublica.setVisible(false);
        btnAguaPotable.setVisible(false);
        btnInstanciaMujer.setVisible(false);
        btnOtrosServicios.setVisible(false);
        btnProteccionCivil.setVisible(false);
        btnUnidadUno.setVisible(false);
        txtIncidenteDenuncia.setText(null);
        txtUbicacion.setText(null);
        txtDescripcionLugar.setText(null);
        txtHeridos.setText(null);
        txtLlamadaRecibida.setText(null);
        txtNombreDenunciante.setText(null);
        txtNumeroProporcionado.setText(null);
        JlabelSemaforo.setBackground(Color.BLUE);
        JlabelSemaforo.setForeground(Color.WHITE);
    }
    
    public void BotonGuardarNuevoFolio(){
        btnGuardar.setEnabled(true);
    }
    
    public void IncidenciasActuales(){
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Folio");//1
        modelo.addColumn("Estado");//7
        modelo.addColumn("Fase");//8
        tablaIncidencias.setModel(modelo);
        String datos[] = new String[3];
        com.mysql.cj.xdevapi.Statement st;
        try {
            ps = con.prepareStatement("select * from Emergencia where estatus = 'Abierto'");
            ResultSet rs = null;
            rs = ps.executeQuery();
            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(7);
                datos[2] = rs.getString(8);
                modelo.addRow(datos);
            }
            tablaIncidencias.getColumnModel().getColumn(0).setPreferredWidth(50);
            tablaIncidencias.getColumnModel().getColumn(1).setPreferredWidth(10);
            tablaIncidencias.getColumnModel().getColumn(2).setPreferredWidth(25);
            tablaIncidencias.setModel(modelo);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    ///Mandar el folio con doble clic al JTextField 
    public void dobleClicJtable() {
        tablaIncidencias.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt) {
                JTable table = (JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 2) {
                    limpiarCajas();
                    limpiarTabla();
                    // Habilitar botones para realizar operaciones normales
                    btnHoraInforme.setEnabled(true);
                    btnHoraDespacho.setEnabled(true);
                    btnGuardar.setEnabled(true);
                    btnHorallegadaIncidente.setEnabled(true);
                    jButton3.setEnabled(true);
                    txtreporteAntena.setEditable(true);
                    //
                    horaInforme = "NULL";
                    horaDespacho = "NULL";
                    horaInsidente = "NULL";
                    txtreporteAntena.setEditable(false);
                    txtNotasCompletas.setText(null);
                    BotonGuardarNuevoFolio();
                    txtFolio.setText(tablaIncidencias.getValueAt(tablaIncidencias.getSelectedRow(), 0).toString());
                    BuscarFolio();
                }
            }
        });
    }
    //notas
    public void dobleClicNotas(){
        tablaNotas.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt){
            JTable table = (JTable) Mouse_evt.getSource();
            Point point = Mouse_evt.getPoint();
            int row = table.rowAtPoint(point);
                if (Mouse_evt.getClickCount() == 1) {
                    txtNotasCompletas.setText(tablaNotas.getValueAt(tablaNotas.getSelectedRow(), 0).toString());
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FondoPrincipal = new javax.swing.JPanel();
        BarraHorizontal = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtElementos = new javax.swing.JTextField();
        txtUnidades = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btnHoraDespacho = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnHorallegadaIncidente = new javax.swing.JButton();
        btnHoraInforme = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        txtEstadoReporte = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tablaNotas = new javax.swing.JTable();
        txtreporteAntena = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        btnRegresarAdmin = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNotasCompletas = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        btnSeguridadPublica = new javax.swing.JButton();
        btnInstanciaMujer = new javax.swing.JButton();
        btnAguaPotable = new javax.swing.JButton();
        btnUnidadUno = new javax.swing.JButton();
        btnProteccionCivil = new javax.swing.JButton();
        btnOtrosServicios = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtIDMon = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtEquiMon = new javax.swing.JTextPane();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtIDAnt = new javax.swing.JTextPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtIDEquiAnt = new javax.swing.JTextPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFolio = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtHora = new javax.swing.JTextField();
        txtFecha = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        JlabelSemaforo = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtHeridos = new javax.swing.JTextArea();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtReporte = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtIncidenteDenuncia = new javax.swing.JTextArea();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtDescripcionLugar = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtUbicacion = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        txtLlamadaRecibida = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtNombreDenunciante = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtNumeroProporcionado = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaIncidencias = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DESPACHADOR");
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BarraHorizontal.setBackground(new java.awt.Color(151, 157, 172));
        BarraHorizontal.setToolTipText("");

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/CerrarVentana.png"))); // NOI18N
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/MinimizarVentana.png"))); // NOI18N
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel22.setText("   DESPACHADOR");

        javax.swing.GroupLayout BarraHorizontalLayout = new javax.swing.GroupLayout(BarraHorizontal);
        BarraHorizontal.setLayout(BarraHorizontalLayout);
        BarraHorizontalLayout.setHorizontalGroup(
            BarraHorizontalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BarraHorizontalLayout.createSequentialGroup()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        BarraHorizontalLayout.setVerticalGroup(
            BarraHorizontalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(151, 157, 172));

        txtElementos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtElementosActionPerformed(evt);
            }
        });

        txtUnidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUnidadesActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jLabel9.setText("HORA DE DESPACHO:");

        jLabel10.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jLabel10.setText("HORA DE INFORME:");

        jLabel15.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jLabel15.setText("UNIDADES:");

        jLabel14.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jLabel14.setText("ELEMENTOS:");

        btnHoraDespacho.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnHoraDespacho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/VA-reloj.png"))); // NOI18N
        btnHoraDespacho.setText(" REGISTRO DE HORA ");
        btnHoraDespacho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoraDespachoActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel3.setText("HORA DE LLEGADA A LOS HECHOS:");

        btnHorallegadaIncidente.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnHorallegadaIncidente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/VA-reloj.png"))); // NOI18N
        btnHorallegadaIncidente.setText(" REGISTRO DE HORA ");
        btnHorallegadaIncidente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHorallegadaIncidenteActionPerformed(evt);
            }
        });

        btnHoraInforme.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnHoraInforme.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/VA-reloj.png"))); // NOI18N
        btnHoraInforme.setText(" REGISTRO DE HORA ");
        btnHoraInforme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoraInformeActionPerformed(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/VA-guardar.png"))); // NOI18N
        btnGuardar.setText(" GUARDAR UNIDADES");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtElementos, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                            .addComponent(txtUnidades)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel3))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel15))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel14))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(btnGuardar))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(btnHoraDespacho, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGap(61, 61, 61)
                            .addComponent(btnHoraInforme))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnHorallegadaIncidente)
                .addGap(60, 60, 60))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHoraInforme)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHoraDespacho)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtElementos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHorallegadaIncidente)
                .addGap(43, 43, 43))
        );

        jPanel5.setBackground(new java.awt.Color(151, 157, 172));

        jLabel13.setFont(new java.awt.Font("Courier New", 1, 22)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("ESTADO DEL REPORTE:");
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel13.setMaximumSize(new java.awt.Dimension(252, 31));
        jLabel13.setMinimumSize(new java.awt.Dimension(252, 31));
        jLabel13.setPreferredSize(new java.awt.Dimension(252, 31));

        jButton6.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/VA-cerrar.png"))); // NOI18N
        jButton6.setText(" CERRAR ARCHIVO");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        txtEstadoReporte.setBackground(new java.awt.Color(0, 153, 51));
        txtEstadoReporte.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtEstadoReporte.setForeground(new java.awt.Color(255, 255, 255));
        txtEstadoReporte.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEstadoReporte.setPreferredSize(new java.awt.Dimension(51, 24));
        txtEstadoReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEstadoReporteActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/VA-crear.png"))); // NOI18N
        jButton5.setText(" CREAR FOLIO");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEstadoReporte, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEstadoReporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(151, 157, 172));

        tablaNotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane13.setViewportView(tablaNotas);

        txtreporteAntena.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtreporteAntenaKeyPressed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/revisarNotas.png"))); // NOI18N
        jButton3.setText("  REVISAR NOTAS");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnRegresarAdmin.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        btnRegresarAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/VA-regresar.png"))); // NOI18N
        btnRegresarAdmin.setText(" REGRESAR");
        btnRegresarAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarAdminActionPerformed(evt);
            }
        });

        txtNotasCompletas.setColumns(20);
        txtNotasCompletas.setRows(5);
        jScrollPane1.setViewportView(txtNotasCompletas);

        jButton4.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/A-limpiar24.png"))); // NOI18N
        jButton4.setText(" LIMPIAR");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtreporteAntena, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 54, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRegresarAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtreporteAntena, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRegresarAdmin))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        jPanel12.setBackground(new java.awt.Color(92, 103, 125));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSeguridadPublica.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        btnSeguridadPublica.setForeground(new java.awt.Color(255, 255, 255));
        btnSeguridadPublica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresiAntena/segPublica.png"))); // NOI18N
        btnSeguridadPublica.setToolTipText("Seguridad Pública");
        btnSeguridadPublica.setAlignmentY(0.2F);
        btnSeguridadPublica.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSeguridadPublica.setIconTextGap(3);
        btnSeguridadPublica.setMargin(new java.awt.Insets(2, 16, 2, 16));
        btnSeguridadPublica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeguridadPublicaActionPerformed(evt);
            }
        });
        jPanel12.add(btnSeguridadPublica, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 165, 70));

        btnInstanciaMujer.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        btnInstanciaMujer.setForeground(new java.awt.Color(255, 255, 255));
        btnInstanciaMujer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresiAntena/INSDLM.png"))); // NOI18N
        btnInstanciaMujer.setToolTipText("Instancia de la Mujer");
        btnInstanciaMujer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel12.add(btnInstanciaMujer, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 164, 70));

        btnAguaPotable.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        btnAguaPotable.setForeground(new java.awt.Color(255, 255, 255));
        btnAguaPotable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresiAntena/Capaschh.png"))); // NOI18N
        btnAguaPotable.setToolTipText("Agua Potable");
        btnAguaPotable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel12.add(btnAguaPotable, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 165, 70));

        btnUnidadUno.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        btnUnidadUno.setForeground(new java.awt.Color(255, 255, 255));
        btnUnidadUno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresiAntena/DIF.png"))); // NOI18N
        btnUnidadUno.setToolTipText("Unidad 1° Cont.");
        btnUnidadUno.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel12.add(btnUnidadUno, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 110, 165, 70));

        btnProteccionCivil.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        btnProteccionCivil.setForeground(new java.awt.Color(255, 255, 255));
        btnProteccionCivil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresiAntena/protCivil.png"))); // NOI18N
        btnProteccionCivil.setToolTipText("Protección Civil");
        btnProteccionCivil.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel12.add(btnProteccionCivil, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 20, 165, 70));

        btnOtrosServicios.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        btnOtrosServicios.setForeground(new java.awt.Color(255, 255, 255));
        btnOtrosServicios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresiAntena/logoPresidencia.png"))); // NOI18N
        btnOtrosServicios.setToolTipText("Otros Servicios");
        btnOtrosServicios.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOtrosServicios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtrosServiciosActionPerformed(evt);
            }
        });
        jPanel12.add(btnOtrosServicios, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, 165, 70));

        jPanel3.setBackground(new java.awt.Color(92, 103, 125));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("ID EQUIPO");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, -1, 20));

        jLabel5.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("ID USUARIO");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, 20));

        txtIDMon.setEditable(false);
        jScrollPane2.setViewportView(txtIDMon);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 210, -1));

        jScrollPane5.setViewportView(txtEquiMon);

        jPanel3.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 210, -1));

        jLabel6.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("ID USUARIO");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, 20));

        jLabel7.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("ID EQUIPO");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, -1, -1));

        txtIDAnt.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        txtIDAnt.setForeground(new java.awt.Color(0, 0, 102));
        jScrollPane6.setViewportView(txtIDAnt);

        jPanel3.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 210, -1));

        txtIDEquiAnt.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        txtIDEquiAnt.setForeground(new java.awt.Color(0, 0, 102));
        txtIDEquiAnt.setText("Equi-03");
        jScrollPane7.setViewportView(txtIDEquiAnt);

        jPanel3.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 210, -1));

        jPanel4.setBackground(new java.awt.Color(92, 103, 125));

        jLabel2.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("FOLIO:");

        txtFolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFolioActionPerformed(evt);
            }
        });
        txtFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFolioKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("HORA:");

        txtHora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoraActionPerformed(evt);
            }
        });

        txtFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("FECHA:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel2))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel8))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel1)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        jPanel6.setBackground(new java.awt.Color(151, 157, 172));

        JlabelSemaforo.setBackground(new java.awt.Color(51, 153, 0));
        JlabelSemaforo.setFont(new java.awt.Font("Courier New", 1, 20)); // NOI18N
        JlabelSemaforo.setForeground(new java.awt.Color(255, 255, 255));
        JlabelSemaforo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        JlabelSemaforo.setText("INFORME COMPLETO");
        JlabelSemaforo.setOpaque(true);

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("INCIDENTE, DENUNCIA:");

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("UBICACIÓN:");

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("DESCRIPCIÓN DEL LUGAR:");

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("HERIDOS:");

        txtHeridos.setColumns(20);
        txtHeridos.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtHeridos.setRows(5);
        jScrollPane4.setViewportView(txtHeridos);

        txtReporte.setColumns(20);
        txtReporte.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtReporte.setRows(5);
        jScrollPane12.setViewportView(txtReporte);

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("REPORTE:");

        txtIncidenteDenuncia.setColumns(20);
        txtIncidenteDenuncia.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtIncidenteDenuncia.setRows(5);
        jScrollPane8.setViewportView(txtIncidenteDenuncia);

        txtDescripcionLugar.setColumns(20);
        txtDescripcionLugar.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtDescripcionLugar.setRows(5);
        jScrollPane9.setViewportView(txtDescripcionLugar);

        txtUbicacion.setColumns(20);
        txtUbicacion.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtUbicacion.setRows(5);
        jScrollPane10.setViewportView(txtUbicacion);

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("LLAMADA RECIBIDA:");
        jLabel11.setToolTipText("");

        txtLlamadaRecibida.setToolTipText("");

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("NOMBRE DEL DENUNCIANTE:");

        txtNombreDenunciante.setToolTipText("");

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("NÚMERO PROPORCIONADO:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel17)))
                            .addComponent(jLabel23))
                        .addGap(197, 197, 197))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel18))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel21))
                            .addComponent(txtNumeroProporcionado)
                            .addComponent(JlabelSemaforo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane8)
                            .addComponent(txtNombreDenunciante)
                            .addComponent(txtLlamadaRecibida)
                            .addComponent(jScrollPane10)
                            .addComponent(jScrollPane9)
                            .addComponent(jScrollPane4)
                            .addComponent(jScrollPane12))
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JlabelSemaforo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLlamadaRecibida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombreDenunciante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNumeroProporcionado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel16.setBackground(new java.awt.Color(0, 102, 153));
        jLabel16.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("INCIDENCIAS");
        jLabel16.setOpaque(true);

        tablaIncidencias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Estado", "Fase"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tablaIncidencias);

        javax.swing.GroupLayout FondoPrincipalLayout = new javax.swing.GroupLayout(FondoPrincipal);
        FondoPrincipal.setLayout(FondoPrincipalLayout);
        FondoPrincipalLayout.setHorizontalGroup(
            FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BarraHorizontal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(FondoPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(FondoPrincipalLayout.createSequentialGroup()
                        .addGroup(FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoPrincipalLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 613, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)))
        );
        FondoPrincipalLayout.setVerticalGroup(
            FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoPrincipalLayout.createSequentialGroup()
                .addComponent(BarraHorizontal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FondoPrincipalLayout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 939, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoPrincipalLayout.createSequentialGroup()
                        .addGroup(FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(FondoPrincipalLayout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(FondoPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1039));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFolioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFolioActionPerformed

    private void txtHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoraActionPerformed

    private void txtFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaActionPerformed

    private void btnSeguridadPublicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeguridadPublicaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSeguridadPublicaActionPerformed

    private void btnOtrosServiciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtrosServiciosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnOtrosServiciosActionPerformed

    private void txtElementosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtElementosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtElementosActionPerformed

    private void txtUnidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUnidadesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnidadesActionPerformed

    private void txtEstadoReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoReporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEstadoReporteActionPerformed

    private void txtFolioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFolioKeyPressed
        // Para Buscar el folio
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BuscarFolio();
        }
    }//GEN-LAST:event_txtFolioKeyPressed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            ps = con.prepareStatement("UPDATE Antena SET hora=?,fecha=?,idUserMon=?,idEquipoMon=?,idUserAnt=?,idEquipoAnt=?,unidades=?," +
            "elementos=? WHERE folio=?");
            ps.setString(1, txtHora.getText());
            ps.setString(2, txtFecha.getText());
            ps.setString(3, txtIDMon.getText());
            ps.setString(4, txtEquiMon.getText());
            ps.setString(5, txtIDAnt.getText());
            ps.setString(6, txtIDEquiAnt.getText());
            ps.setString(7, txtUnidades.getText());
            ps.setString(8, txtElementos.getText());
            ps.setString(9, txtFolio.getText());
            int res = ps.executeUpdate();
            if (res > 0) {
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/Aceptar.png"));
                JOptionPane.showMessageDialog(null, "REPORTE GUARDADO", "Mensaje", JOptionPane.PLAIN_MESSAGE, icono);
            } else {
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/ErrorX.png"));
                JOptionPane.showMessageDialog(null, "REPORTE NO GUARDADO", "Error", JOptionPane.PLAIN_MESSAGE, icono);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed
    // Registrar Folio en BD
    public void BotonHoraInforme() {
        horaInforme = hora + ":" + minutos + ":" + segundos;
        btnHoraInforme.setEnabled(false);
        try {
            ps = con.prepareStatement("insert into Antena (folio,horaInforme,horaDespacho,horaLlegadaIn) values(?,?,?,?)");
            ps.setString(1, txtFolio.getText());
            ps.setString(2, horaInforme);
            ps.setString(3, horaDespacho);
            ps.setString(4, horaInsidente);
            int res = ps.executeUpdate();
            if (res > 0) {
            } else {
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    private void btnHoraInformeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoraInformeActionPerformed
        BotonHoraInforme();
        txtreporteAntena.setEditable(true);
    }//GEN-LAST:event_btnHoraInformeActionPerformed

    private void btnHoraDespachoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoraDespachoActionPerformed
        horaDespacho = hora + ":" + minutos + ":" + segundos;
        btnHoraDespacho.setEnabled(false);
        try {
            ps = con.prepareStatement("UPDATE Antena SET horaDespacho=? WHERE folio=?");
            ps.setString(1, horaDespacho);
            ps.setString(2, txtFolio.getText());
            int res = ps.executeUpdate();
            if (res > 0) {
            } else {
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }//GEN-LAST:event_btnHoraDespachoActionPerformed

    private void btnHorallegadaIncidenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHorallegadaIncidenteActionPerformed
        horaInsidente = hora + ":" + minutos + ":" + segundos;
        btnHorallegadaIncidente.setEnabled(false);
        try {
            ps = con.prepareStatement("UPDATE Antena SET horaLlegadaIn=? WHERE folio=?");
            ps.setString(1, horaInsidente);
            ps.setString(2, txtFolio.getText());
            int res = ps.executeUpdate();
            if (res > 0) {
            } else {
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }//GEN-LAST:event_btnHorallegadaIncidenteActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String botones[] = {"Cerrar Reporte", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(this, "¿Esta seguro de cerrar el reporte? \n No se podran agregar nuevos datos", "Cerrar Reporte", 0, 0, null, botones, this);
        if (eleccion == JOptionPane.YES_OPTION) {
            //Editar el estatus
            try{
            ps = con.prepareStatement("UPDATE Emergencia SET estatus=? WHERE folio=?");
            ps.setString(1, estatus);
            ps.setString(2, txtFolio.getText());
            int res = ps.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Reporte Cerrado");
            }else{
                JOptionPane.showMessageDialog(null, "Error al cerrar");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        } else if (eleccion == JOptionPane.NO_OPTION) {
            System.out.println("Cierre Cancelado");
        }
        limpiarCajas();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Entrar al modo Monitorista
        String botones[] = {"Modo Monitorista", "Cancelar"};
        int eleccion = JOptionPane.showOptionDialog(this, "¿Esta seguro de entrar a modo Monitorista?", "Modo Monitorista", 0, 0, null, botones, this);
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
    public String saltosDeLinea(String descripción) {
    String convertido = null;
    String sinSaltos = descripción.replaceAll("\n", "<br> ");
    convertido = "<HTML> " + sinSaltos + " </HTML>";
    return convertido;
    }
    private void guardarNotas(){
        // Guardar Reporte  
        horasNotas = hora + ":" + minutos + ":" + segundos;
        try {
            ps = con.prepareStatement("insert into Antena (folio,hora,fecha,idUserMon,idEquipoMon,idUserAnt,idEquipoAnt,unidades,elementos," +
            "reporteAnt,horaReportes) values (?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, txtFolio.getText());
            ps.setString(2, txtHora.getText());
            ps.setString(3, txtFecha.getText());
            ps.setString(4, txtIDMon.getText());
            ps.setString(5, txtEquiMon.getText());
            ps.setString(6, txtIDAnt.getText());
            ps.setString(7, txtIDEquiAnt.getText());
            ps.setString(8, txtUnidades.getText());
            ps.setString(9, txtElementos.getText());
            ps.setString(10, txtreporteAntena.getText());
            ps.setString(11, horasNotas);
            int res = ps.executeUpdate();
            if (res > 0) {
                // BUSCAR NOTAS
                tablaNotas();
                txtreporteAntena.setText(null);
            } else {
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/ErrorX.png"));
                JOptionPane.showMessageDialog(null, "REPORTE NO GUARDADO", "Error", JOptionPane.PLAIN_MESSAGE, icono);
                txtreporteAntena.setText(null);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Para cerrar la ventana 
        this.setVisible(false);
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Para Minimizar la Ventana
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtreporteAntenaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtreporteAntenaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            guardarNotas();
        }
    }//GEN-LAST:event_txtreporteAntenaKeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // mostrar tabla
        tablaNotas();
        txtNotasCompletas.setText(null);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnRegresarAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarAdminActionPerformed
        Administrador admin = new Administrador();
        admin.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnRegresarAdminActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // limpiar todos los campos
        limpiarCajas();
        limpiarTabla();
        btnHoraInforme.setEnabled(false);
        btnHoraDespacho.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnHorallegadaIncidente.setEnabled(false);
        jButton3.setEnabled(false);
        txtreporteAntena.setEditable(false);
    }//GEN-LAST:event_jButton4ActionPerformed

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
            java.util.logging.Logger.getLogger(ventana_antena.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventana_antena.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventana_antena.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventana_antena.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ventana_antena().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BarraHorizontal;
    private javax.swing.JPanel FondoPrincipal;
    private javax.swing.JLabel JlabelSemaforo;
    private javax.swing.JButton btnAguaPotable;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnHoraDespacho;
    private javax.swing.JButton btnHoraInforme;
    private javax.swing.JButton btnHorallegadaIncidente;
    private javax.swing.JButton btnInstanciaMujer;
    private javax.swing.JButton btnOtrosServicios;
    private javax.swing.JButton btnProteccionCivil;
    private javax.swing.JButton btnRegresarAdmin;
    private javax.swing.JButton btnSeguridadPublica;
    private javax.swing.JButton btnUnidadUno;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable tablaIncidencias;
    private javax.swing.JTable tablaNotas;
    private javax.swing.JTextArea txtDescripcionLugar;
    private javax.swing.JTextField txtElementos;
    private javax.swing.JTextPane txtEquiMon;
    private javax.swing.JTextField txtEstadoReporte;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtFolio;
    private javax.swing.JTextArea txtHeridos;
    private javax.swing.JTextField txtHora;
    private javax.swing.JTextPane txtIDAnt;
    private javax.swing.JTextPane txtIDEquiAnt;
    private javax.swing.JTextPane txtIDMon;
    private javax.swing.JTextArea txtIncidenteDenuncia;
    private javax.swing.JTextField txtLlamadaRecibida;
    private javax.swing.JTextField txtNombreDenunciante;
    private javax.swing.JTextArea txtNotasCompletas;
    private javax.swing.JTextField txtNumeroProporcionado;
    private javax.swing.JTextArea txtReporte;
    private javax.swing.JTextArea txtUbicacion;
    private javax.swing.JTextField txtUnidades;
    private javax.swing.JTextField txtreporteAntena;
    // End of variables declaration//GEN-END:variables
}