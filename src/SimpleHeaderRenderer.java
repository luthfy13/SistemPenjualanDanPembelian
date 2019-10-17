import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
 
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
 
/**
 * A simple renderer class for JTable component.
 * @author www.codejava.net
 *
 */
public class SimpleHeaderRenderer extends JLabel implements TableCellRenderer {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimpleHeaderRenderer() {
        setFont(new Font("Tahoma", Font.BOLD, 18));
        setOpaque(true);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        setBackground(Color.BLUE);
        setHorizontalAlignment(CENTER);
    }
     
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
        return this;
    }
 
}