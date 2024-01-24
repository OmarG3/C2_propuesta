package Login;

import PlaceHolder.placeHolder;
import c2.Administrador;
import c2.Conexion;
import c2.Res_Lla;
import c2.ventana_antena;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Omar
 */
public class Acceso extends javax.swing.JFrame {

    //Mandamos a llamar a la conexión
    Conexion conector = new Conexion();
    //"con" se usara para realizar distintos procesos
    Connection con = conector.getConexion();
    ///
    PreparedStatement ps;
    //Guardar Nombre
    String Nombre = "";
    public static String Name;
    String Usuario = "";
    public static String User;
    String Tipo = "";
    public static String Tipe;
    String IDusuario = "";
    public static String IDuser;

    public Acceso() {
        initComponents();
        this.setLocationRelativeTo(null);
        placeHolder user = new placeHolder("USUARIO", txtUsuario);
        placeHolder pass = new placeHolder("CONTRASEÑA", txtPass);
        // Poner JTextField en transparente
        txtUsuario.setBackground(new java.awt.Color(0, 0, 0, 1));
        txtPass.setBackground(new java.awt.Color(0, 0, 0, 1));
        //
        ImageIcon logo = new ImageIcon(getClass().getResource("/FondosImage/fondoLogin.jpg"));
        Icon fondoLogo = new ImageIcon(logo.getImage().getScaledInstance(lblFondoPrincipal.getWidth(), lblFondoPrincipal.getHeight(), Image.SCALE_DEFAULT));
        lblFondoPrincipal.setIcon(fondoLogo);
        this.repaint();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ImagenesExtras/Icono_Ventanas.png")));
    }

    public void fondoLogin() {
        ImageIcon logo = new ImageIcon(getClass().getResource("/FondosImage/fondo ric.png"));
        Icon fondoLogo = new ImageIcon(logo.getImage().getScaledInstance(Fondologin.getWidth(), Fondologin.getHeight(), Image.SCALE_DEFAULT));
        Fondologin.setIcon(fondoLogo);
        this.repaint();
    }

    private void Ingresar() {
        String usuario = txtUsuario.getText();
        String password = txtPass.getText();
        String url = "select nombre, idUsuario, pass, tipoCargo from Usuarios where idUsuario='" + usuario + "'";
        try {
            ps = con.prepareStatement(url);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //Si existe el usuario
                String us = rs.getString("idUsuario");
                String pa = rs.getString("pass");
                String tip = rs.getString("tipoCargo");
                //Almacenar el nombre para llamarlo a la otra ventana
                IDusuario = rs.getString(2);
                IDuser = IDusuario;
                Tipe = rs.getString(4);
                Name = rs.getString(1);

                if (password.equals(pa)) {
                    //Vamos a Jframe que queremos
                    if (tip.equals("Recepcionista")) {
                        Res_Lla ventaPrin = new Res_Lla();
                        ventaPrin.setVisible(true);
                        ventaPrin.btnRegresarAdmin.setVisible(false);
                        if(tip.equals("Administrador")){
                            ventaPrin.btnRegresarAdmin.setVisible(true);
                        }
                        this.setVisible(false);
                        dispose();
                    } else if(tip.equals("Despachador")) {
                        ventana_antena ant = new ventana_antena();
                        ant.setVisible(true);
                        this.setVisible(false);
                    }else if (tip.equals("Administrador")) {
                        Administrador admin = new Administrador();
                        admin.setVisible(true);
                        this.setVisible(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "CONTRASEÑA INCORRECTA");
                    txtPass.setText(null);
                }
            } else {
                //El usuario mo existe
                JOptionPane.showMessageDialog(null, "USUARIO NO EXISTENTE");
                txtUsuario.setText(null);
            }
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        txtPass = new javax.swing.JPasswordField();
        btnIngresar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        Bienvenido = new javax.swing.JLabel();
        Fondologin = new javax.swing.JLabel();
        lblFondoPrincipal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Acceso al Sistema");
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Login/Iconos/LoginUsuario.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 400, 30, 30));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Login/Iconos/LoginPassword.png"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 470, 30, 30));

        txtUsuario.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtUsuario.setForeground(new java.awt.Color(255, 255, 255));
        txtUsuario.setBorder(null);
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyPressed(evt);
            }
        });
        getContentPane().add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 400, 210, 30));

        txtPass.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtPass.setForeground(new java.awt.Color(255, 255, 255));
        txtPass.setBorder(null);
        txtPass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPassKeyPressed(evt);
            }
        });
        getContentPane().add(txtPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 470, 210, 30));

        btnIngresar.setBackground(new java.awt.Color(204, 204, 204));
        btnIngresar.setFont(new java.awt.Font("Courier New", 1, 20)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(0, 0, 0));
        btnIngresar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Login/Iconos/iniciar-sesion.png"))); // NOI18N
        btnIngresar.setText(" INGRESAR");
        btnIngresar.setBorder(null);
        btnIngresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIngresarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIngresarMouseExited(evt);
            }
        });
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });
        getContentPane().add(btnIngresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 580, 330, 50));

        btnSalir.setBackground(new java.awt.Color(204, 204, 204));
        btnSalir.setFont(new java.awt.Font("Courier New", 1, 20)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(0, 0, 0));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Login/Iconos/cerrar-sesion.png"))); // NOI18N
        btnSalir.setText(" SALIR");
        btnSalir.setBorder(null);
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalirMouseExited(evt);
            }
        });
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 650, 310, 50));

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator2.setOpaque(true);
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 500, 250, -1));

        jSeparator3.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));
        jSeparator3.setOpaque(true);
        getContentPane().add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 430, 250, -1));

        Bienvenido.setBackground(new java.awt.Color(255, 255, 255));
        Bienvenido.setFont(new java.awt.Font("Century", 1, 52)); // NOI18N
        Bienvenido.setForeground(new java.awt.Color(255, 255, 255));
        Bienvenido.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Bienvenido.setText("CENACI-C2");
        Bienvenido.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(Bienvenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 260, 420, -1));

        Fondologin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Fondologin.setName(""); // NOI18N
        getContentPane().add(Fondologin, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 260, 370, 450));
        getContentPane().add(lblFondoPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -5, 1920, 1050));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // Cerar sistema
        this.setVisible(false);
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtPass.requestFocus();
        }
    }//GEN-LAST:event_txtUsuarioKeyPressed

    private void txtPassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPassKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            Ingresar();
        }
    }//GEN-LAST:event_txtPassKeyPressed

    private void btnSalirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseEntered
//        // Cuando acercas el mouse se pone del color seleccionado
//        btnIngresar.setBackground(Color.blue);
    }//GEN-LAST:event_btnSalirMouseEntered

    private void btnSalirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseExited
//        // cuando se aleja el mouse regresa a su color original
//       btnIngresar.setBackground(new Color(16,40,128));
    }//GEN-LAST:event_btnSalirMouseExited

    private void btnIngresarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIngresarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIngresarMouseEntered

    private void btnIngresarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIngresarMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIngresarMouseExited

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        Ingresar();
    }//GEN-LAST:event_btnIngresarActionPerformed

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
            java.util.logging.Logger.getLogger(Acceso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Acceso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Acceso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Acceso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Acceso().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Bienvenido;
    private javax.swing.JLabel Fondologin;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblFondoPrincipal;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
