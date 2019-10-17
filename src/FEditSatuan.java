import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FEditSatuan {

	JFrame frame;
	static FEditSatuan window;
	private JTextField txtIsi;
	private JComboBox<String> cmbSatuanIsi;
	static String IdBarang, Satuan;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FEditSatuan();
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
	public FEditSatuan() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Edit Satuan Isi");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				String Pesan = "Data satuan isi sudah ada,\nPilih satuan yang belum memiliki satuan isi!";
				if (! AmbilSatuanIsi(IdBarang, Satuan).equals("-")){
					JOptionPane.showMessageDialog(null, Pesan);
					FStokBarang.window.frame.setEnabled(true);
					window.frame.dispose();
					frame.dispose();
					window.frame = null;
					frame = null;
					window = null;
					System.gc();
				}
				TampilSatuan();
				Pesan = null;
			}
			@Override
			public void windowClosing(WindowEvent e) {
				FStokBarang.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 230, 122);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblSatuanIsi = new JLabel("Satuan Isi");
		lblSatuanIsi.setBounds(10, 11, 82, 14);
		frame.getContentPane().add(lblSatuanIsi);
		
		cmbSatuanIsi = new JComboBox<String>();
		cmbSatuanIsi.setBounds(102, 8, 112, 20);
		frame.getContentPane().add(cmbSatuanIsi);
		
		JLabel lblIsi = new JLabel("Isi");
		lblIsi.setBounds(10, 36, 46, 14);
		frame.getContentPane().add(lblIsi);
		
		txtIsi = new JTextField();
		txtIsi.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
		          if (!((c >= '0') && (c <= '9') ||
		             (c == KeyEvent.VK_BACK_SPACE) ||
		             (c == KeyEvent.VK_DELETE))) {
		            e.consume();
		          }
			}
		});
		txtIsi.setBounds(102, 33, 46, 20);
		frame.getContentPane().add(txtIsi);
		txtIsi.setColumns(10);
		
		JButton btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String teks = "Apakah Anda yakin ingin menyimpan data ini?";
				int konfir;
				konfir = JOptionPane.showConfirmDialog(null, teks, "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (konfir == JOptionPane.YES_OPTION){
					SimpanData();
					FStokBarang.window.frame.setEnabled(true);
					window.frame.dispose();
					frame.dispose();
					window.frame = null;
					frame = null;
					window = null;
					System.gc();
				}
			}
		});
		btnSimpan.setBounds(10, 61, 89, 23);
		frame.getContentPane().add(btnSimpan);
		
		JButton btnBatal = new JButton("Batal");
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FStokBarang.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnBatal.setBounds(125, 61, 89, 23);
		frame.getContentPane().add(btnBatal);
	}
	
	private void TampilSatuan(){
		cmbSatuanIsi.removeAllItems();
		cmbSatuanIsi.addItem("-SATUAN-");
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			Statement st;
			
			query = "select satuan from tb_satuan";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next()){
				cmbSatuanIsi.addItem(rs.getString("satuan"));
			}
			
			st.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil satuan beli " + e);}
	}
	
	private void SimpanData(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_detail_barang set isi = ?, satuan_isi = ? where id_barang = ? and satuan = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtIsi.getText());
			psQuery.setString(2, cmbSatuanIsi.getSelectedItem().toString());
			psQuery.setString(3, IdBarang);
			psQuery.setString(4, Satuan);
			psQuery.execute();
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan ubah stok satuan asal" + e);}
	}
	
	private String AmbilSatuanIsi(String Id, String Satuan){
		String SatuanIsi="";
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;

			query = "SELECT satuan_isi from tb_detail_barang where id_barang = ? and satuan = ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,Id);
			psQuery.setString(2,Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();

			while (rs.next()) SatuanIsi = rs.getString("satuan_isi");
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro ambil satuan_isi " + e);}
		return SatuanIsi;
	}
}
