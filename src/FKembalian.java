import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

public class FKembalian {

	private JFrame frmBayarTransaksi;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FKembalian window = new FKembalian();
					window.frmBayarTransaksi.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FKembalian() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBayarTransaksi = new JFrame();
		frmBayarTransaksi.setTitle("Bayar Transaksi");
		frmBayarTransaksi.setBounds(100, 100, 705, 460);
		frmBayarTransaksi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBayarTransaksi.getContentPane().setLayout(null);
		
		JLabel lblJumlahBayar = new JLabel("Jumlah Bayar");
		lblJumlahBayar.setFont(new Font("Tahoma", lblJumlahBayar.getFont().getStyle() & ~Font.ITALIC, lblJumlahBayar.getFont().getSize() + 26));
		lblJumlahBayar.setBounds(53, 72, 263, 41);
		frmBayarTransaksi.getContentPane().add(lblJumlahBayar);
		
		textField = new JTextField();
		textField.setBounds(308, 72, 348, 41);
		frmBayarTransaksi.getContentPane().add(textField);
		textField.setColumns(10);
	}
}
