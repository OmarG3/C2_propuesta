package Splash;
import Login.Acceso;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;
/**
 *
 * @author Omar
 */
public class splash extends javax.swing.JFrame {
    //Para bloquear el sistema
    public static final LocalDate FECHA_BLOQUEO = LocalDate.of(2024, 10, 01);
    Timer timer;

    public splash() {
        ActionListener actlis = (ActionEvent e) -> {
            if (jProgressBar1.getValue() < 100) {
                jProgressBar1.setValue(jProgressBar1.getValue() + 2);
                lblPorcentaje.setText(jProgressBar1.getValue() + " %");
            } else {
                timer.stop();
                AbrirLogin();
            }
        };
        timer = new Timer(80, actlis);
        //Transparencia del splash
        setUndecorated(true);
        setBackground(new Color(108, 246, 248, 0));
        initComponents();
        //AWTUtilities
        jProgressBar1.setBackground(new Color(0, 0, 153));
        //UIManager.put(jProgressBar1, Color.BLUE);
        UIManager.put("nimbusOrange", new Color(0, 166, 31));
        this.setLocationRelativeTo(null);
        timer.start();
        jPanel1.setOpaque(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ImagenesExtras/Icono_Ventanas.png")));
    }

    public void AbrirLogin() {
        Acceso acceso = new Acceso();
        acceso.setVisible(true);
        this.dispose();
    }    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblc2splash = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        lblPorcentaje = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblc2splash.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblc2splash.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesExtras/CENACI_SPLASH.png"))); // NOI18N
        getContentPane().add(lblc2splash, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 12, 1420, 460));
        getContentPane().add(jProgressBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 1350, 40));

        lblPorcentaje.setFont(new java.awt.Font("Courier New", 1, 20)); // NOI18N
        lblPorcentaje.setForeground(new java.awt.Color(0, 153, 0));
        getContentPane().add(lblPorcentaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(1370, 490, 60, 40));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 200, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public static void main(String args[]) {
    LocalDate currentDate = LocalDate.now();
    LocalDate fechaLimite = FECHA_BLOQUEO.minusMonths(1); // Fecha límite para mostrar el mensaje
    if (currentDate.isAfter(FECHA_BLOQUEO)) {
        //System.out.println("El programa ha caducado y ya no se puede utilizar.");
        JOptionPane.showMessageDialog(null, "EL PROGRAMA HA CADUCADO Y YA NO SE PUEDE UTILIZAR." +
                "\nPor favor contacta a los desarrolladores:" + "\nOmar Guerra Maldonado: omargm1998@gmail.com \nCésar Guadalupe Mendoza Angeles");
        System.exit(1);
    } else if (currentDate.isAfter(fechaLimite)) {
        long diasRestantes = ChronoUnit.DAYS.between(currentDate, FECHA_BLOQUEO);
        JOptionPane.showMessageDialog(null, "Quedan " + diasRestantes + " días antes del bloqueo del sistema."
                + "\n Por favor contacta a los desarrolladores "
                + "\n Para agendar la renovacion de la licencia"
                + "\n GRACIAS!!");
    }
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
            java.util.logging.Logger.getLogger(splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(splash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new splash().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JLabel lblPorcentaje;
    private javax.swing.JLabel lblc2splash;
    // End of variables declaration//GEN-END:variables
}