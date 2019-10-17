import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXDatePicker;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FEditJatuhTempo {

	private JFrame frame;
	static FEditJatuhTempo window;
	static String NoFaktur=null;
	private JXDatePicker datePicker;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FEditJatuhTempo();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FEditJatuhTempo() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				FCetakFaktur.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
			@Override
			public void windowOpened(WindowEvent arg0) {
				datePicker.setDate(new Date());
			}
		});
		frame.setTitle("Update Tanggal Jatuh Tempo");
		frame.setResizable(false);
		frame.setBounds(100, 100, 265, 75);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		datePicker = new JXDatePicker();
		datePicker.setFormats(new String[] {"dd - MM - yyyy"});
		datePicker.setBounds(10, 11, 136, 22);
		frame.getContentPane().add(datePicker);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpdateJatuhTempo();
				FCetakFaktur.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnUpdate.setBounds(156, 11, 91, 23);
		frame.getContentPane().add(btnUpdate);
	}
	
	private void UpdateJatuhTempo(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String query = "update tb_transaksi_kredit set tanggal_jatuh_tempo = ? where no_faktur = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, dateFormat.format(datePicker.getDate()).toString());
			psQuery.setString(2, NoFaktur);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Tanggal jatuh tempo berhasil diedit...");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
}
