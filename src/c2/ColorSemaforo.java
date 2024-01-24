package c2;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Omar
 */
public class ColorSemaforo extends DefaultTableCellRenderer {
    private final int columna_patron;

    public ColorSemaforo(int Colpatron) {
        this.columna_patron = Colpatron;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean Selected, boolean hasFocus, int row, int column) {
        switch (table.getValueAt(row, columna_patron).toString()) {
            
            case "Rojo":
                setBackground(Color.RED);
                setForeground(Color.RED);
                break;

            case "Amarillo":
                setBackground(Color.YELLOW);
                setForeground(Color.YELLOW);
                break;
            case "Verde":
                setBackground(Color.GREEN);
                setForeground(Color.GREEN);
                break;
            default:
                break;
        }
        super.getTableCellRendererComponent(table, value, Selected, hasFocus, row, column);
        return this;
    }
}