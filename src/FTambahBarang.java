import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
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
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;

public class FTambahBarang {

	private JFrame frame;
	static FTambahBarang window;
	private JTextField txtIdBarang;
	private JTextField txtNamaBarang;
	private JTextField txtHargaBeli;
	private JTextField txtHargaJual;
	private JTextField txtSatuanJual;
	private JTextField txtIsi;
	private JComboBox<String> cmbSupplier;
	private JComboBox<String> cmbSatuanIsi;
	private JComboBox<String> cmbSatuanBeli;
	private JTextField txtJmlBarang;
	private JLabel lblJmlBarang;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FTambahBarang();
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
	public FTambahBarang() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Barang.png"));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				DataBaru();
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
		frame.setTitle("Tambah Data Barang");
		frame.setResizable(false);
		frame.setBounds(100, 100, 368, 258);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblIdBarang = new JLabel("ID Barang");
		lblIdBarang.setBounds(10, 14, 99, 14);
		frame.getContentPane().add(lblIdBarang);
		
		JLabel lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setBounds(10, 39, 99, 14);
		frame.getContentPane().add(lblNamaBarang);
		
		JLabel lblHargaBeli = new JLabel("Harga Beli");
		lblHargaBeli.setBounds(10, 64, 99, 14);
		frame.getContentPane().add(lblHargaBeli);
		
		JLabel label = new JLabel("/");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(226, 64, 12, 14);
		frame.getContentPane().add(label);
		
		JLabel lblHargaJual = new JLabel("Harga Jual");
		lblHargaJual.setBounds(10, 89, 99, 14);
		frame.getContentPane().add(lblHargaJual);
		
		JLabel label_1 = new JLabel("/");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(226, 89, 12, 14);
		frame.getContentPane().add(label_1);
		
		JLabel lblIsi = new JLabel("Isi");
		lblIsi.setBounds(10, 114, 46, 14);
		frame.getContentPane().add(lblIsi);
		
		JLabel lblSupplier = new JLabel("Supplier");
		lblSupplier.setBounds(10, 139, 99, 14);
		frame.getContentPane().add(lblSupplier);
		
		txtIdBarang = new JTextField();
		txtIdBarang.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char keyChar = arg0.getKeyChar();
			    if (Character.isLowerCase(keyChar)) {
			      arg0.setKeyChar(Character.toUpperCase(keyChar));
			    }
			}
		});
		txtIdBarang.setBounds(105, 11, 241, 20);
		frame.getContentPane().add(txtIdBarang);
		txtIdBarang.setColumns(10);
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char keyChar = arg0.getKeyChar();
			    if (Character.isLowerCase(keyChar)) {
			      arg0.setKeyChar(Character.toUpperCase(keyChar));
			    }
			}
		});
		txtNamaBarang.setBounds(105, 36, 241, 20);
		frame.getContentPane().add(txtNamaBarang);
		txtNamaBarang.setColumns(10);
		
		txtHargaBeli = new JTextField();
		txtHargaBeli.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') ||
				   (c == KeyEvent.VK_BACK_SPACE) ||
				   (c == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (! (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)){
					try {
						txtHargaBeli.setText(FMain.FormatAngka(Integer.valueOf(txtHargaBeli.getText().replace(",", ""))));
					} catch (Exception er) {
						if (!txtHargaBeli.getText().equals(""))
							JOptionPane.showMessageDialog(null, "Batas maksimum = 2.147.483.647!!!");
						txtHargaBeli.setText("");
					}
				}
			}
		});
		txtHargaBeli.setBounds(105, 61, 111, 20);
		frame.getContentPane().add(txtHargaBeli);
		txtHargaBeli.setColumns(10);
		
		cmbSatuanBeli = new JComboBox<String>();
		cmbSatuanBeli.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				txtSatuanJual.setText(cmbSatuanBeli.getSelectedItem().toString());
				lblJmlBarang.setText(cmbSatuanBeli.getSelectedItem().toString());
			}
		});
		cmbSatuanBeli.setBounds(248, 61, 98, 20);
		frame.getContentPane().add(cmbSatuanBeli);
		
		txtHargaJual = new JTextField();
		txtHargaJual.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (! (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)){
					try {
						txtHargaJual.setText(FMain.FormatAngka(Integer.valueOf(txtHargaJual.getText().replace(",", ""))));
					} catch (Exception er) {
						if (!txtHargaJual.getText().equals(""))
							JOptionPane.showMessageDialog(null, "Batas maksimum = 2.147.483.647!!!");
						txtHargaJual.setText("");
					}
				}
			}
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
		txtHargaJual.setBounds(105, 86, 111, 20);
		frame.getContentPane().add(txtHargaJual);
		txtHargaJual.setColumns(10);
		
		txtSatuanJual = new JTextField();
		txtSatuanJual.setBounds(248, 86, 98, 20);
		frame.getContentPane().add(txtSatuanJual);
		txtSatuanJual.setColumns(10);
		
		txtIsi = new JTextField();
		txtIsi.setBounds(105, 111, 61, 20);
		frame.getContentPane().add(txtIsi);
		txtIsi.setColumns(10);
		
		cmbSatuanIsi = new JComboBox<String>();
		cmbSatuanIsi.setBounds(176, 111, 98, 20);
		frame.getContentPane().add(cmbSatuanIsi);
		
		cmbSupplier = new JComboBox<String>();
		cmbSupplier.setBounds(105, 136, 241, 20);
		frame.getContentPane().add(cmbSupplier);
		
		txtJmlBarang = new JTextField();
		txtJmlBarang.setBounds(105, 161, 86, 20);
		frame.getContentPane().add(txtJmlBarang);
		txtJmlBarang.setColumns(10);
		
		JButton btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int konfir;
				konfir = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menyimpan data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (konfir == JOptionPane.YES_OPTION){
					SimpanTabelBarang();
					SimpanTabelDetailBarang();
					JOptionPane.showMessageDialog(null, "Data Tersimpan");
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
		btnSimpan.setBounds(158, 192, 89, 23);
		frame.getContentPane().add(btnSimpan);
		
		JButton btnTutup = new JButton("Tutup");
		btnTutup.setMnemonic(KeyEvent.VK_X);
		btnTutup.addActionListener(new ActionListener() {
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
		btnTutup.setBounds(257, 192, 89, 23);
		frame.getContentPane().add(btnTutup);
		
		JLabel lblJumlahBarang = new JLabel("Jumlah Barang");
		lblJumlahBarang.setBounds(10, 164, 99, 14);
		frame.getContentPane().add(lblJumlahBarang);
		
		lblJmlBarang = new JLabel("New label");
		lblJmlBarang.setBounds(201, 164, 46, 14);
		frame.getContentPane().add(lblJmlBarang);
	}
	
	private void TampilSupplier(){
		cmbSupplier.removeAllItems();
		cmbSupplier.addItem("-PILIH SUPPLIER-");
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			Statement st;
			
			query = "select id, nama from tb_supplier";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next()){
				cmbSupplier.addItem(rs.getString("id") + " - " +rs.getString("nama"));
			}
			
			st.close();
			rs.close();
			conn.close();
			
		}
	
		catch(Exception e){System.out.println("Ada error bro tampil supplier " + e);}
	}
	
	String AmbilID(String Teks){
		return Teks.substring(0, Teks.indexOf(" "));
	}
	
	private void TampilSatuanBeli(){
		cmbSatuanBeli.removeAllItems();
		cmbSatuanBeli.addItem("-SATUAN-");
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
				cmbSatuanBeli.addItem(rs.getString("satuan"));
			}
			
			st.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil satuan beli " + e);}
	}
	
	private void TampilSatuanIsi(){
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
		catch(Exception e){System.out.println("Ada error bro tampil satuan isi " + e);}
	}
	
	private void SimpanTabelBarang(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			PreparedStatement psQuery=null;
			String query = "insert into tb_barang values (?,?,?,?)";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtIdBarang.getText());
			psQuery.setString(2, txtNamaBarang.getText());
			psQuery.setString(3, txtHargaBeli.getText().replace(",", ""));
			psQuery.setString(4, AmbilID(cmbSupplier.getSelectedItem().toString()));
			psQuery.execute();
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan transaksi " + e);}
	}
	
	private void SimpanTabelDetailBarang(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			PreparedStatement psQuery=null;
			String query = "insert into tb_detail_barang values (?,?,?,?,?,?)";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtIdBarang.getText());
			psQuery.setString(2, cmbSatuanBeli.getSelectedItem().toString());
			psQuery.setString(3, txtJmlBarang.getText());
			psQuery.setString(4, txtHargaJual.getText().replace(",", ""));
			psQuery.setString(5, txtIsi.getText());
			psQuery.setString(6, cmbSatuanIsi.getSelectedItem().toString());
			psQuery.execute();
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan transaksi " + e);}
	}
	
	private void DataBaru(){
		TampilSatuanBeli();
		TampilSatuanIsi();
		TampilSupplier();
		txtIdBarang.setText("");
		txtNamaBarang.setText("");
		txtHargaJual.setText("");
		txtHargaBeli.setText("");
		txtIsi.setText("");
		txtSatuanJual.setText("");
		txtJmlBarang.setText("");
		lblJmlBarang.setText("");
		txtIdBarang.requestFocus();
	}
}
