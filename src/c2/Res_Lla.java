package c2;

import static Login.Acceso.Tipe;
import java.awt.Color;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author king_
 */
public class Res_Lla extends javax.swing.JFrame {
    //Mandamos a llamar a la conexión
    Conexion conector = new Conexion();
    //"con" se usara para realizar distintos procesos
    Connection con = conector.getConexion();
    //
    PreparedStatement ps;
    //
    String numPrivado = "";
    public static String numeroLlamada = "";
    //Mandar la Incidencia a la siguiente ventana
    public static String incidenciaTexto = "";

    public Res_Lla() {
        initComponents();
        this.setLocationRelativeTo(null);
        mostrarTabla();
        // Doble clic sobre la tabla para mandar los datos de la incidencia a la siguiente ventana
        dobleClicJtable();
        privilegiosButton();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ImagenesExtras/Icono_Ventanas.png")));
    }
    // Mostrar el boton de regresar a la ventana anterior
    // Solo aplicable para Administradores y Despachadores
    public void privilegiosButton(){
        try {
            ps = con.prepareStatement("select * from Usuarios");
            ResultSet rs = null;
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                        if (Tipe.equals("Recepcionista")){
                            btnRegresarAdmin.setVisible(false);
                            btnregresarAntena.setVisible(false);
                        }else if(Tipe.equals("Despachador")){
                            btnRegresarAdmin.setVisible(false);
                            btnregresarAntena.setVisible(true);
                        }else if(Tipe.equals("Administrador")){
                            
                            btnRegresarAdmin.setVisible(true);
                            btnregresarAntena.setVisible(false);
                        }
                        
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "NO Conectado");
            
        }
    }
    // verificar codigo
    public void anchoCeldas(){
        //Para ajustar el ancho de las celdas de la tabla
        tablaIncidencias.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        tablaIncidencias.getColumnModel().getColumn(0).setPreferredWidth(120); 
        tablaIncidencias.getColumnModel().getColumn(1).setPreferredWidth(626); 
        tablaIncidencias.getColumnModel().getColumn(2).setPreferredWidth(100);
    }
    //Cambiar el color de un boton al acercar el mouse
    private void cambiarColorBoton(JButton b, Color colorEvento){
        b.setBackground(colorEvento);
    }

    public void mostrarTabla() {
    DefaultTableModel modelo = new DefaultTableModel();

    modelo.addColumn("Código");
    modelo.addColumn("Incidencia");
    modelo.addColumn("Prioridad");

    tablaIncidencias.setModel(modelo);

    String datos[] = new String[3];

    Statement st;
    try {
        ps = con.prepareStatement("select * from catalogoIncidencias");
        ResultSet rs = null;
        rs = ps.executeQuery();
        while (rs.next()) {
            datos[0] = rs.getString(1);
            datos[1] = rs.getString(2);
            datos[2] = rs.getString(3);
            modelo.addRow(datos);
        }
        tablaIncidencias.setModel(modelo);
        // Agrega un listener al modelo de la tabla
        modelo.addTableModelListener(new TableModelListener() {
        @Override
        public void tableChanged(TableModelEvent e) {
        // Obtiene el ancho actual de la tabla
        int tableWidth = tablaIncidencias.getWidth();
        
        // Establece el ancho de las columnas
        TableColumnModel columnModel = tablaIncidencias.getColumnModel();
        TableColumn column = columnModel.getColumn(0);
        column.setPreferredWidth((int) Math.round(tableWidth * 0.05)); // El primer 25% del ancho total
        column = columnModel.getColumn(1);
        column.setPreferredWidth((int) Math.round(tableWidth * 0.8)); // El siguiente 50% del ancho total
        column = columnModel.getColumn(2);
        column.setPreferredWidth((int) Math.round(tableWidth * 0.05)); // El último 25% del ancho total
    }
});

        // Modificamos el tamaño de las columnas
        tablaIncidencias.getColumnModel().getColumn(0).setPreferredWidth(10);
        tablaIncidencias.getColumnModel().getColumn(1).setPreferredWidth(420);
        tablaIncidencias.getColumnModel().getColumn(2).setPreferredWidth(10);

    } catch (Exception e) {
        System.err.println(e.toString());
    }
}
    //Enviar a la siguiente ventana
    public void dobleClicJtable() {
        tablaIncidencias.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent Mouse_evt) {
                JTable table = (JTable) Mouse_evt.getSource();
                Point point = Mouse_evt.getPoint();
                int row = table.rowAtPoint(point);
                if (txtNumeroLlamada.getText().length() >= 10) {
                    if (Mouse_evt.getClickCount() == 2) {
                    incidenciaTexto = (tablaIncidencias.getValueAt(tablaIncidencias.getSelectedRow(), 1).toString());
                    numeroLlamada = txtNumeroLlamada.getText();
                    //Nos manda a la ventana del monitorista
                    VENTANA_LLAMADA ven = new VENTANA_LLAMADA();
                    ven.setVisible(true);
                    dispose();
                } 
                } else {
                    Icon icono = new ImageIcon(getClass().getResource("/ImagenesExtras/ErrorX.png"));
                    JOptionPane.showMessageDialog(null, "NUMERO INCOMPLETO", "Error", JOptionPane.PLAIN_MESSAGE, icono);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        FondoPrincipal = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaIncidencias = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        txtNumeroLlamada = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtBuscarIncidencia = new javax.swing.JTextField();
        BarraHorizontal = new javax.swing.JPanel();
        btnCerrarVentana = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnRegresarAdmin = new javax.swing.JButton();
        btnregresarAntena = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Recepción de Llamada");
        setUndecorated(true);
        setResizable(false);

        tablaIncidencias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Incidente", "Prioridad"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaIncidencias);
        if (tablaIncidencias.getColumnModel().getColumnCount() > 0) {
            tablaIncidencias.getColumnModel().getColumn(0).setResizable(false);
            tablaIncidencias.getColumnModel().getColumn(0).setHeaderValue("Código");
            tablaIncidencias.getColumnModel().getColumn(1).setResizable(false);
            tablaIncidencias.getColumnModel().getColumn(1).setHeaderValue("Incidente");
            tablaIncidencias.getColumnModel().getColumn(2).setResizable(false);
            tablaIncidencias.getColumnModel().getColumn(2).setHeaderValue("Prioridad");
        }

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 100));

        btnGuardar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(0, 0, 0));
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/numPriv.png"))); // NOI18N
        btnGuardar.setText("Num. Priv.");
        btnGuardar.setBorder(null);
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        txtNumeroLlamada.setFont(new java.awt.Font("Courier New", 1, 34)); // NOI18N
        txtNumeroLlamada.setText("77");
        txtNumeroLlamada.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 11), new java.awt.Color(255, 255, 255))); // NOI18N
        txtNumeroLlamada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumeroLlamadaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtNumeroLlamada, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                    .addComponent(txtNumeroLlamada))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/RLL-buscar.png"))); // NOI18N

        txtBuscarIncidencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarIncidenciaKeyPressed(evt);
            }
        });

        BarraHorizontal.setBackground(new java.awt.Color(179, 179, 179));
        BarraHorizontal.setForeground(new java.awt.Color(153, 0, 51));

        btnCerrarVentana.setBackground(new java.awt.Color(102, 0, 51));
        btnCerrarVentana.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/CerrarVentana.png"))); // NOI18N
        btnCerrarVentana.setContentAreaFilled(false);
        btnCerrarVentana.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCerrarVentanaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCerrarVentanaMouseExited(evt);
            }
        });
        btnCerrarVentana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarVentanaActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/MinimizarVentana.png"))); // NOI18N
        jButton2.setContentAreaFilled(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("   RECEPCIÓN DE LLAMADAS");

        javax.swing.GroupLayout BarraHorizontalLayout = new javax.swing.GroupLayout(BarraHorizontal);
        BarraHorizontal.setLayout(BarraHorizontalLayout);
        BarraHorizontalLayout.setHorizontalGroup(
            BarraHorizontalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BarraHorizontalLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCerrarVentana, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        BarraHorizontalLayout.setVerticalGroup(
            BarraHorizontalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
            .addComponent(btnCerrarVentana, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btnRegresarAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/RLL-regresar.png"))); // NOI18N
        btnRegresarAdmin.setToolTipText("Regresar");
        btnRegresarAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarAdminActionPerformed(evt);
            }
        });

        btnregresarAntena.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/RLL-regresar.png"))); // NOI18N
        btnregresarAntena.setToolTipText("Regresar");
        btnregresarAntena.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnregresarAntenaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FondoPrincipalLayout = new javax.swing.GroupLayout(FondoPrincipal);
        FondoPrincipal.setLayout(FondoPrincipalLayout);
        FondoPrincipalLayout.setHorizontalGroup(
            FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BarraHorizontal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(FondoPrincipalLayout.createSequentialGroup()
                .addGroup(FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FondoPrincipalLayout.createSequentialGroup()
                        .addGap(204, 204, 204)
                        .addGroup(FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(FondoPrincipalLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtBuscarIncidencia, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 209, Short.MAX_VALUE))
                    .addGroup(FondoPrincipalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoPrincipalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnregresarAntena)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRegresarAdmin)))
                .addContainerGap())
        );
        FondoPrincipalLayout.setVerticalGroup(
            FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FondoPrincipalLayout.createSequentialGroup()
                .addComponent(BarraHorizontal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegresarAdmin)
                    .addComponent(btnregresarAntena))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(FondoPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBuscarIncidencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(FondoPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(FondoPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarIncidenciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarIncidenciaKeyPressed
        DefaultTableModel modelo;
        try {
            ResultSet rs = null;
            ps = con.prepareStatement("select * from catalogoIncidencias where codigo like '%" + txtBuscarIncidencia.getText() + "%'"
                    + "or incidencia like '%" + txtBuscarIncidencia.getText() + "%'");
            rs = ps.executeQuery();
            String[] titulos = {"Código", "Incidencia", "Prioridad"};
            String datos[] = new String[3];

            // Guardar ancho de las columnas
            int[] anchoColumnas = new int[3];
            TableColumnModel columnModel = tablaIncidencias.getColumnModel();
            for (int i = 0; i < 3; i++) {
                TableColumn column = columnModel.getColumn(i);
                anchoColumnas[i] = column.getWidth();
            }

            modelo = new DefaultTableModel(null, titulos);
            try {
                while (rs.next()) {
                    datos[0] = rs.getString("codigo");
                    datos[1] = rs.getString("incidencia");
                    datos[2] = rs.getString("prioridad");
                    modelo.addRow(datos);
                }
                tablaIncidencias.setModel(modelo);
                // Restaurar ancho de las columnas
                for (int i = 0; i < 3; i++) {
                    TableColumn column = columnModel.getColumn(i);
                    column.setPreferredWidth(anchoColumnas[i]);
                }

            } catch (SQLException ex) {
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No Conectado");
        }

    }//GEN-LAST:event_txtBuscarIncidenciaKeyPressed

    private void txtNumeroLlamadaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroLlamadaKeyTyped
        /// Limitar JTextField a 10 caracteres
        if (txtNumeroLlamada.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNumeroLlamadaKeyTyped

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        numPrivado = "Num Priv..";
        txtNumeroLlamada.setText(numPrivado);
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Para Minimizar la vista de la ventana
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnCerrarVentanaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarVentanaActionPerformed
        //Para cerrar la ventana 
        this.setVisible(false);
        System.exit(0);
    }//GEN-LAST:event_btnCerrarVentanaActionPerformed

    private void btnCerrarVentanaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarVentanaMouseEntered
        cambiarColorBoton(this.btnCerrarVentana, Color.RED);
    }//GEN-LAST:event_btnCerrarVentanaMouseEntered

    private void btnCerrarVentanaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarVentanaMouseExited
        cambiarColorBoton(this.btnCerrarVentana, new Color(153,0,51));
    }//GEN-LAST:event_btnCerrarVentanaMouseExited

    private void btnRegresarAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarAdminActionPerformed
        Administrador admin = new Administrador();
        admin.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnRegresarAdminActionPerformed

    private void btnregresarAntenaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregresarAntenaActionPerformed
        //regresar a la ventana despachador
        ventana_antena ant = new ventana_antena();
        ant.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnregresarAntenaActionPerformed

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
            java.util.logging.Logger.getLogger(Res_Lla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Res_Lla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Res_Lla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Res_Lla.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Res_Lla().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BarraHorizontal;
    private javax.swing.JPanel FondoPrincipal;
    private javax.swing.JButton btnCerrarVentana;
    public javax.swing.JButton btnGuardar;
    public javax.swing.JButton btnRegresarAdmin;
    public javax.swing.JButton btnregresarAntena;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaIncidencias;
    private javax.swing.JTextField txtBuscarIncidencia;
    private javax.swing.JTextField txtNumeroLlamada;
    // End of variables declaration//GEN-END:variables
}
