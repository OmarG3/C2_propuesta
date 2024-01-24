package c2;
import static Login.Acceso.IDuser;
import static c2.Res_Lla.incidenciaTexto;
import static c2.Res_Lla.numeroLlamada;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class VENTANA_LLAMADA extends javax.swing.JFrame implements Runnable{

    // Mandamos a llamar a la conexión
    Conexion conector = new Conexion();
    // "con" se usara para realizar distintos procesos
    Connection con = conector.getConexion();
    PreparedStatement ps;
    ///////////////////////////////////////
    String hora, minutos, segundos, ampm;
    Calendar calendario;
    Thread h1;
    //////////////////////////////////////
    Calendar fecha_Actual = new GregorianCalendar();
    //
    String SeguridadPublica = "NULL";
    String AguaPotable = "NULL";
    String ProteccionCivil = "NULL";
    String InstanciaMujer = "NULL";
    String UnidadPrimerContacto = "NULL";
    String OtrosServicios = "NULL";
    ////////////////////////
    String rojo;
    String amarillo;
    String verde;
    /// Variable del folio//
    int n = 0;
    
    GenerarCodigo codigoFolio = new GenerarCodigo();
    
    public VENTANA_LLAMADA(){
        initComponents();
        this.setLocationRelativeTo(null);
        //hora
        h1 = new Thread(this);
        h1.start();
        setVisible(true);
        // Inhabilitar boton enviar, para no enviar reporte antes de seleccionar dependencia
        btnEnviarReporte.setEnabled(false);
        // Mostrar fecha actual y hora
        fecha.setCalendar(fecha_Actual);
        fecha.setEnabled(false);
        txtHora.setEditable(false);
        // Inhabilitar edición de los TextField
        txtIDequipo.setEditable(false);
        txtIDusuario.setEditable(false);
        // Mostrar el usuario logeado
        txtIDusuario.setText(IDuser);
        //
        txtTelefono.setText(numeroLlamada);
        txtTelefono.setEditable(false);
        // FOLIO
        generarSerie();
        //generarFolio(); -----------
        txtFolio.setEditable(false);
        // Registrar Folio luego luego de que abre la ventana
        folioBaseDatos(); 
        //
        txtIncidenteDenuncia.setText(incidenciaTexto);
        txtIncidenteDenuncia.setEditable(false);
        // Para mostrar el nombre del equipo usado actualmente
        java.net.InetAddress localMachine = null; 
        try {
            localMachine = java.net.InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(VENTANA_LLAMADA.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtIDequipo.setText(localMachine.getHostName());
        // Dar saltos de linea al llegar al borde de un area texto
        txtDescripcionHeridos.setLineWrap(true);
        txtDescripcionHeridos.setWrapStyleWord(true);
        txtReporte.setLineWrap(true);
        txtReporte.setWrapStyleWord(true);
        txtDescripcionHeridos.setEditable(false);
        txtReporte.setEditable(false);
        txtUbicacion.setLineWrap(true);
        txtUbicacion.setWrapStyleWord(true);
        txtDescripcionlugar.setLineWrap(true);
        txtDescripcionlugar.setWrapStyleWord(true);
        //
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ImagenesExtras/Icono_Ventanas.png")));  
    }
    // Mostrar la hora actual en tiempo real en TextField  
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
            txtHora.setText(hora + ":" + minutos + ":" + segundos + " " + ampm);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
    // Limpiar campos
    private void limpiarCajas() {
        txtFolio.setText(null);
        txtTelefono.setText(null);
        txtDescripcionHeridos.setText(null);
        txtReporte.setText(null);
        txtIncidenteDenuncia.setText(null);
        txtUbicacion.setText(null);
        txtDescripcionlugar.setText(null);
        txtInvolucrados.setText(null);
        txtNombreDenunciante.setText(null);
        txtTelefono2.setText(null);
        btnRojo.setEnabled(true);
        btnAmarillo.setEnabled(true);
        btnVerde.setEnabled(true);
        btnSeguridadPublica.setEnabled(true);
        btnProteccionCivil.setEnabled(true);
        btnInstanciaMujer.setEnabled(true);
        btnAguaPotable.setEnabled(true);
        btnUnidadUno.setEnabled(true);
        btnOtrosServicios.setEnabled(true);
        SeguridadPublica = "NULL";
        AguaPotable = "NULL";
        ProteccionCivil = "NULL";
        InstanciaMujer = "NULL";
        UnidadPrimerContacto = "NULL";
        OtrosServicios = "NULL";
        rojo = "";
        amarillo = "";
        verde = "";
        txtDescripcionHeridos.setEditable(true);
        txtReporte.setEditable(true);
    }
    ///Registrar Folio en BD
    public void folioBaseDatos() {
        try {
            ps = con.prepareStatement("insert into Emergencia (folio) values(?)");
            ps.setString(1, txtFolio.getText());

            int res = ps.executeUpdate();

            if (res > 0) {
            } else {
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }  
 
    // codigo funcional de folio
    void generarSerie(){
        String serie = codigoFolio.NroSerie();
        
        if (serie == null) {
            txtFolio.setText("0001");
        }else{
            int increment = Integer.parseInt(serie);
            increment = increment + 1;
            txtFolio.setText("" + increment);
        }
    
    }
    
//    public String generarFolio() {
//    String folio = "";
//    DateFormat dateFormat = new SimpleDateFormat("yyyy");
//    Date date = new Date();
//    String anioActual = dateFormat.format(date);
//
//    try {
//        // Consulta para obtener el último folio generado
//        String query = "SELECT MAX(folio) FROM Emergencia WHERE folio LIKE '" + anioActual + "%'";
//        PreparedStatement ps = con.prepareStatement(query);
//        ResultSet rs = ps.executeQuery();
//        if (rs.next()) {
//            String ultimoFolio = rs.getString(1);
//            if (ultimoFolio != null) {
//                int numFolio = Integer.parseInt(ultimoFolio.substring(4)) + 1;
//                txtFolio.setText(anioActual + String.format("%05d", numFolio));
//            } else {
//                txtFolio.setText(anioActual + "00001");
//            }
//        }
//        // Verificación de folio único
//        query = "SELECT * FROM Emergencia WHERE folio = ?";
//        ps = con.prepareStatement(query);
//        ps.setString(1, txtFolio.getText()); // Usamos el valor del textfield
//        rs = ps.executeQuery();
//        if (rs.next()) {
//            return generarFolio(); // Si ya existe el folio, se genera uno nuevo
//        }
//        // Inserción del folio en la base de datos
//        query = "INSERT INTO Emergencia (folio) VALUES (?)";
//        ps = con.prepareStatement(query);
//        ps.setString(1, txtFolio.getText()); // Usamos el valor del textfield
//        ps.executeUpdate();
//        folio = txtFolio.getText(); // Asignamos el valor generado a la variable "folio"
//    } catch (Exception ex) {
//        System.out.println("Error: " + ex.getMessage());
//    }
//    return folio;
//}


 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel10 = new javax.swing.JPanel();
        BarraHoizontal = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtInvolucrados = new javax.swing.JTextField();
        txtNombreDenunciante = new javax.swing.JTextField();
        txtTelefono2 = new javax.swing.JTextField();
        txtIncidenteDenuncia = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescripcionlugar = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtUbicacion = new javax.swing.JTextArea();
        jPanel12 = new javax.swing.JPanel();
        btnSeguridadPublica = new javax.swing.JButton();
        btnInstanciaMujer = new javax.swing.JButton();
        btnUnidadUno = new javax.swing.JButton();
        btnProteccionCivil = new javax.swing.JButton();
        btnOtrosServicios = new javax.swing.JButton();
        btnAguaPotable = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtFolio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtHora = new javax.swing.JTextField();
        fecha = new com.toedter.calendar.JDateChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtIDusuario = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtIDequipo = new javax.swing.JTextPane();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtDescripcionHeridos = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        txtTelefono = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnRojo = new javax.swing.JButton();
        btnAmarillo = new javax.swing.JButton();
        btnVerde = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtReporte = new javax.swing.JTextArea();
        jLabel23 = new javax.swing.JLabel();
        btnEnviarReporte = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MONITORISTA");
        setUndecorated(true);
        setResizable(false);

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BarraHoizontal.setBackground(new java.awt.Color(179, 179, 179));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/CerrarVentana.png"))); // NOI18N
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/MinimizarVentana.png"))); // NOI18N
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("   MONITORISTA");

        javax.swing.GroupLayout BarraHoizontalLayout = new javax.swing.GroupLayout(BarraHoizontal);
        BarraHoizontal.setLayout(BarraHoizontalLayout);
        BarraHoizontalLayout.setHorizontalGroup(
            BarraHoizontalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BarraHoizontalLayout.createSequentialGroup()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1673, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        BarraHoizontalLayout.setVerticalGroup(
            BarraHoizontalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel10.add(BarraHoizontal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, -1));

        jPanel5.setPreferredSize(new java.awt.Dimension(541, 100));

        jPanel13.setBackground(new java.awt.Color(128, 139, 150));

        jLabel7.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("INCIDENTE, DENUNCIA:");

        jLabel8.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("UBICACION:");

        jLabel12.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("TELEFONO:");

        jLabel17.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("INVOLUCRADOS:");

        jLabel18.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("NOMBRE DEL DENUNCIANTE:");

        jLabel19.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("DESCRIPCION DEL LUGAR:");

        txtInvolucrados.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtInvolucrados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvolucradosKeyPressed(evt);
            }
        });

        txtNombreDenunciante.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNombreDenunciante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDenuncianteKeyPressed(evt);
            }
        });

        txtTelefono2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtTelefono2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefono2KeyTyped(evt);
            }
        });

        txtIncidenteDenuncia.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        txtDescripcionlugar.setColumns(20);
        txtDescripcionlugar.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtDescripcionlugar.setRows(5);
        txtDescripcionlugar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescripcionlugarKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtDescripcionlugar);

        txtUbicacion.setColumns(20);
        txtUbicacion.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtUbicacion.setRows(5);
        txtUbicacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUbicacionKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(txtUbicacion);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtIncidenteDenuncia)
                        .addComponent(jScrollPane1)
                        .addComponent(txtInvolucrados)
                        .addComponent(jScrollPane6)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTelefono2)
                        .addComponent(txtNombreDenunciante, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 705, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(107, 107, 107))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIncidenteDenuncia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtInvolucrados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombreDenunciante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTelefono2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );

        jPanel12.setBackground(new java.awt.Color(128, 139, 150));

        btnSeguridadPublica.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        btnSeguridadPublica.setForeground(new java.awt.Color(0, 0, 0));
        btnSeguridadPublica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresidencia/segPublica.png"))); // NOI18N
        btnSeguridadPublica.setText("  Seguridad Publica");
        btnSeguridadPublica.setAlignmentY(0.2F);
        btnSeguridadPublica.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSeguridadPublica.setIconTextGap(3);
        btnSeguridadPublica.setMargin(new java.awt.Insets(2, 16, 2, 16));
        btnSeguridadPublica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeguridadPublicaActionPerformed(evt);
            }
        });

        btnInstanciaMujer.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnInstanciaMujer.setForeground(new java.awt.Color(0, 0, 0));
        btnInstanciaMujer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresidencia/INSDLM.png"))); // NOI18N
        btnInstanciaMujer.setText("Instancia de la Mujer");
        btnInstanciaMujer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstanciaMujerActionPerformed(evt);
            }
        });

        btnUnidadUno.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnUnidadUno.setForeground(new java.awt.Color(0, 0, 0));
        btnUnidadUno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresidencia/DIF.png"))); // NOI18N
        btnUnidadUno.setText("   Unidad 1° Cont.");
        btnUnidadUno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnidadUnoActionPerformed(evt);
            }
        });

        btnProteccionCivil.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        btnProteccionCivil.setForeground(new java.awt.Color(0, 0, 0));
        btnProteccionCivil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresidencia/protCivil.png"))); // NOI18N
        btnProteccionCivil.setText("  Protección Civil");
        btnProteccionCivil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProteccionCivilActionPerformed(evt);
            }
        });

        btnOtrosServicios.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnOtrosServicios.setForeground(new java.awt.Color(0, 0, 0));
        btnOtrosServicios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresidencia/logoPresidencia.png"))); // NOI18N
        btnOtrosServicios.setText(" Otros servicios");
        btnOtrosServicios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtrosServiciosActionPerformed(evt);
            }
        });

        btnAguaPotable.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        btnAguaPotable.setForeground(new java.awt.Color(0, 0, 0));
        btnAguaPotable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogosPresidencia/Capaschh.png"))); // NOI18N
        btnAguaPotable.setText("   Agua Potable");
        btnAguaPotable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAguaPotableActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnInstanciaMujer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSeguridadPublica, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAguaPotable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUnidadUno, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnProteccionCivil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOtrosServicios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(btnSeguridadPublica, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInstanciaMujer, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnAguaPotable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnProteccionCivil, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnUnidadUno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnOtrosServicios, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(311, 311, 311))
        );

        jPanel10.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(948, 26, 972, 890));

        jPanel2.setPreferredSize(new java.awt.Dimension(535, 100));

        jPanel4.setBackground(new java.awt.Color(128, 139, 150));

        jLabel1.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("FOLIO:");

        txtFolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFolioActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("HORA:");

        jLabel14.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("FECHA:");

        txtHora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoraActionPerformed(evt);
            }
        });

        fecha.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel1)
                        .addComponent(jLabel14)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(txtFolio)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        jPanel1.setBackground(new java.awt.Color(128, 139, 150));

        jLabel3.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("ID DE EQUIPO:");

        jLabel2.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("ID DE USUARIO:");

        txtIDusuario.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        txtIDusuario.setForeground(new java.awt.Color(0, 0, 102));
        jScrollPane2.setViewportView(txtIDusuario);

        txtIDequipo.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        txtIDequipo.setForeground(new java.awt.Color(153, 0, 0));
        txtIDequipo.setText("Equi-01");
        jScrollPane3.setViewportView(txtIDequipo);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(128, 139, 150));
        jPanel7.setPreferredSize(new java.awt.Dimension(918, 225));

        jLabel16.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("DESCRIPCION DE HERIDOS O INCIDENTE:");

        txtDescripcionHeridos.setColumns(20);
        txtDescripcionHeridos.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtDescripcionHeridos.setRows(5);
        jScrollPane5.setViewportView(txtDescripcionHeridos);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(383, 383, 383))
                    .addComponent(jScrollPane5))
                .addGap(26, 26, 26))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        jPanel3.setBackground(new java.awt.Color(128, 139, 150));

        txtTelefono.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        txtTelefono.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("TELEFONO DE LLAMADA");

        btnRojo.setBackground(new java.awt.Color(204, 0, 0));
        btnRojo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnRojo.setForeground(new java.awt.Color(255, 255, 255));
        btnRojo.setText("Alta");
        btnRojo.setBorder(null);
        btnRojo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRojoActionPerformed(evt);
            }
        });

        btnAmarillo.setBackground(new java.awt.Color(153, 153, 0));
        btnAmarillo.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnAmarillo.setForeground(new java.awt.Color(255, 255, 255));
        btnAmarillo.setText("Media");
        btnAmarillo.setBorder(null);
        btnAmarillo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAmarilloActionPerformed(evt);
            }
        });

        btnVerde.setBackground(new java.awt.Color(0, 153, 0));
        btnVerde.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnVerde.setForeground(new java.awt.Color(255, 255, 255));
        btnVerde.setText("Baja");
        btnVerde.setBorder(null);
        btnVerde.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerdeActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("SEMAFORO DE EMERGENCIA:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTelefono)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(141, 141, 141)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnRojo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAmarillo, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnVerde, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnVerde, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAmarillo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRojo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(34, 34, 34))))
        );

        jPanel8.setBackground(new java.awt.Color(128, 139, 150));
        jPanel8.setPreferredSize(new java.awt.Dimension(916, 225));

        jLabel15.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("DESCRIPCION DEL REPORTE:");

        txtReporte.setColumns(20);
        txtReporte.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txtReporte.setRows(5);
        jScrollPane4.setViewportView(txtReporte);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(404, 404, 404))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jScrollPane4)
                        .addGap(23, 23, 23))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/CENACI_llamada_Logo.png"))); // NOI18N
        jLabel23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(268, 268, 268))
        );

        jPanel10.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 28, 942, 890));

        btnEnviarReporte.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        btnEnviarReporte.setForeground(new java.awt.Color(0, 0, 0));
        btnEnviarReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/VLL-enviar-reporte.png"))); // NOI18N
        btnEnviarReporte.setText("  ENVIAR REPORTE");
        btnEnviarReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarReporteActionPerformed(evt);
            }
        });
        jPanel10.add(btnEnviarReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 930, 310, 90));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel10.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 930, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 1050, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFolioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFolioActionPerformed

    private void txtHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoraActionPerformed

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void btnRojoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRojoActionPerformed
        // TODO add your handling code here:
        rojo = "Rojo";
        btnRojo.setEnabled(false);
        btnAmarillo.setEnabled(false);
        btnVerde.setEnabled(false);
        txtReporte.setEditable(false);
        txtDescripcionHeridos.setEditable(true);
    }//GEN-LAST:event_btnRojoActionPerformed

    private void btnAmarilloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAmarilloActionPerformed
        // TODO add your handling code here:
        amarillo = "Amarillo";
        btnRojo.setEnabled(false);
        btnAmarillo.setEnabled(false);
        btnVerde.setEnabled(false);
        txtReporte.setEditable(false);
        txtDescripcionHeridos.setEditable(true);
    }//GEN-LAST:event_btnAmarilloActionPerformed

    private void btnSeguridadPublicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeguridadPublicaActionPerformed
        SeguridadPublica = "Seguridad Publica";
        btnSeguridadPublica.setEnabled(false);
        btnEnviarReporte.setEnabled(true);
    }//GEN-LAST:event_btnSeguridadPublicaActionPerformed

    private void btnOtrosServiciosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtrosServiciosActionPerformed
        // TODO add your handling code here:
        OtrosServicios = "Otros Servicios";
        btnOtrosServicios.setEnabled(false);
        btnEnviarReporte.setEnabled(true);
    }//GEN-LAST:event_btnOtrosServiciosActionPerformed

    private void btnEnviarReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarReporteActionPerformed
        // Para no dejar campos vacios
        if( txtIncidenteDenuncia.getText().trim().isEmpty() || txtUbicacion.getText().trim().isEmpty() || txtDescripcionlugar.getText().trim().isEmpty() || txtInvolucrados.getText().trim().isEmpty()){
            Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/Warning.png"));
            JOptionPane.showMessageDialog(null,"POR FAVOR VERIFIQUE..!! \nNO DEJAR DATOS INCOMPLETOS", "Warning", JOptionPane.PLAIN_MESSAGE, icono);       
        }else{
        // Guardar Reporte  
        String estatus = "Abierto";
        String fase = "Nuevo";
        //
        try {
            ps = con.prepareStatement("UPDATE Emergencia SET hora=?,fecha=?,idUsuario=?,idEquipo=?,telefonoLlamada=?,semaforo=?,fase=?,descripcionHeridos=?,reporte=?,seguridadPublica=?,"
                    + "aguaPotable=?,proteccionCivil=?,instanciaMujer=?,unidadUno=?,otrosServicios=?,incidenteDenuncia=?,ubicacion=?,descripcionLugar=?,involucrados=?,nombreDenunciante=?,telefono=?,estatus=? WHERE folio=?");
            ps.setString(1, hora + ":" + minutos + ":" + segundos);
            ps.setString(2, ((JTextField) fecha.getDateEditor().getUiComponent()).getText());
            ps.setString(3, txtIDusuario.getText());
            ps.setString(4, txtIDequipo.getText());
            ps.setString(5, txtTelefono.getText());
            if ("Rojo".equals(rojo)) {
                ps.setString(6, rojo);
            } else if("Amarillo".equals(amarillo)){
                ps.setString(6, amarillo);
            } else if("Verde".equals(verde)){
                ps.setString(6, verde);
            }
            ps.setString(7, fase);
            ps.setString(8, txtDescripcionHeridos.getText());
            ps.setString(9, txtReporte.getText());
            ps.setString(10, SeguridadPublica);
            ps.setString(11, AguaPotable);
            ps.setString(12, ProteccionCivil);
            ps.setString(13, InstanciaMujer);
            ps.setString(14, UnidadPrimerContacto);
            ps.setString(15, OtrosServicios);
            ps.setString(16, txtIncidenteDenuncia.getText());
            ps.setString(17, txtUbicacion.getText());
            ps.setString(18, txtDescripcionlugar.getText());
            ps.setString(19, txtInvolucrados.getText());
            ps.setString(20, txtNombreDenunciante.getText());
            ps.setString(21, txtTelefono2.getText());
            ps.setString(22, estatus);
            ps.setString(23, txtFolio.getText());

            int res = ps.executeUpdate();

            if (res > 0) {
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/Aceptar.png"));
                JOptionPane.showMessageDialog(null, "REPORTE ENVIADO", "Mensaje", JOptionPane.PLAIN_MESSAGE, icono);
                limpiarCajas();
            } else {
                Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/ErrorX.png"));
                JOptionPane.showMessageDialog(null, "REPORTE NO ENVIADO", "Error", JOptionPane.PLAIN_MESSAGE, icono);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        Res_Lla num = new Res_Lla();
        num.setVisible(true);
        this.setVisible(false);
        dispose();
        }
    }//GEN-LAST:event_btnEnviarReporteActionPerformed

    private void txtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyTyped
        
    }//GEN-LAST:event_txtTelefonoKeyTyped

    private void txtTelefono2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefono2KeyTyped
        char validar = evt.getKeyChar();
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(rootPane, "INGRESE SOLO NUMEROS");
        }
        /// Limitar JTextField a 10 caracteres
        if (txtTelefono2.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtTelefono2KeyTyped

    private void btnAguaPotableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAguaPotableActionPerformed
        AguaPotable = "Agua Potable";
        btnAguaPotable.setEnabled(false);
        btnEnviarReporte.setEnabled(true);
    }//GEN-LAST:event_btnAguaPotableActionPerformed

    private void btnVerdeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerdeActionPerformed
        // TODO add your handling code here:
        verde = "Verde";
        btnRojo.setEnabled(false);
        btnAmarillo.setEnabled(false);
        btnVerde.setEnabled(false);
        txtDescripcionHeridos.setEditable(false);
        txtReporte.setEditable(true);
    }//GEN-LAST:event_btnVerdeActionPerformed

    private void btnProteccionCivilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProteccionCivilActionPerformed
        // TODO add your handling code here:
        ProteccionCivil = "Proteccion Civil";
        btnProteccionCivil.setEnabled(false);
        btnEnviarReporte.setEnabled(true);
    }//GEN-LAST:event_btnProteccionCivilActionPerformed

    private void btnInstanciaMujerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstanciaMujerActionPerformed
        // TODO add your handling code here:
        InstanciaMujer = "Instancia de la Mujer";
        btnInstanciaMujer.setEnabled(false);
        btnEnviarReporte.setEnabled(true);
    }//GEN-LAST:event_btnInstanciaMujerActionPerformed

    private void btnUnidadUnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnidadUnoActionPerformed
        // TODO add your handling code here:
        UnidadPrimerContacto = "Unidad de 1er Contacto";
        btnUnidadUno.setEnabled(false);
        btnEnviarReporte.setEnabled(true);
    }//GEN-LAST:event_btnUnidadUnoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Para cerrar la ventana 
        this.setVisible(false);
        System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtInvolucradosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvolucradosKeyPressed
        // dar enter y pasar al campo siguiente
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNombreDenunciante.requestFocus();
        }
    }//GEN-LAST:event_txtInvolucradosKeyPressed

    private void txtNombreDenuncianteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDenuncianteKeyPressed
        // dar enter y pasar al campo siguiente
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtTelefono2.requestFocus();
        }
    }//GEN-LAST:event_txtNombreDenuncianteKeyPressed

    private void txtUbicacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUbicacionKeyPressed
        // dar enter y pasar al campo siguiente
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtDescripcionlugar.requestFocus();
        }
    }//GEN-LAST:event_txtUbicacionKeyPressed

    private void txtDescripcionlugarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionlugarKeyPressed
        // dar enter y pasar al campo siguiente
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtInvolucrados.requestFocus();
        }
    }//GEN-LAST:event_txtDescripcionlugarKeyPressed

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
            java.util.logging.Logger.getLogger(VENTANA_LLAMADA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VENTANA_LLAMADA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VENTANA_LLAMADA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VENTANA_LLAMADA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VENTANA_LLAMADA().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BarraHoizontal;
    private javax.swing.JButton btnAguaPotable;
    private javax.swing.JButton btnAmarillo;
    private javax.swing.JButton btnEnviarReporte;
    private javax.swing.JButton btnInstanciaMujer;
    private javax.swing.JButton btnOtrosServicios;
    private javax.swing.JButton btnProteccionCivil;
    private javax.swing.JButton btnRojo;
    private javax.swing.JButton btnSeguridadPublica;
    private javax.swing.JButton btnUnidadUno;
    private javax.swing.JButton btnVerde;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextArea txtDescripcionHeridos;
    private javax.swing.JTextArea txtDescripcionlugar;
    private javax.swing.JTextField txtFolio;
    private javax.swing.JTextField txtHora;
    private javax.swing.JTextPane txtIDequipo;
    private javax.swing.JTextPane txtIDusuario;
    private javax.swing.JTextField txtIncidenteDenuncia;
    private javax.swing.JTextField txtInvolucrados;
    private javax.swing.JTextField txtNombreDenunciante;
    private javax.swing.JTextArea txtReporte;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtTelefono2;
    private javax.swing.JTextArea txtUbicacion;
    // End of variables declaration//GEN-END:variables
}