import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JTable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;

public class FKaryawan {

	private JFrame frame;
	static FKaryawan window;
	private JTextField txtID;
	private JTextField txtNama;
	private JTextField txtAlamat;
	private JTextField txtTelp;
	private JComboBox<String> cmbBagian;
	private JTable table;
	private JButton btnTambah,btnEdit,btnHapus,btnSimpan,btnTutup,btnBatal;
	private String UserName=null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FKaryawan();
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
	public FKaryawan() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				cmbBagian.removeAllItems();
				cmbBagian.addItem("-PILH BAGIAN-");
				cmbBagian.addItem("Admin");
				cmbBagian.addItem("Kasir");
				cmbBagian.addItem("Sales");
//				cmbBagian.addItem("Supir");
				btnBatal.setEnabled(false);
				btnEdit.setEnabled(true);
				btnHapus.setEnabled(true);
				btnSimpan.setEnabled(false);
				btnTambah.setEnabled(true);
				btnTutup.setEnabled(true);
				TampilData();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				FMain.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		frame.setTitle("Data Karyawan");
		frame.setResizable(false);
		frame.setBounds(100, 100, 314, 433);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblId = new JLabel("ID Karyawan");
		lblId.setBounds(10, 11, 100, 14);
		frame.getContentPane().add(lblId);
		
		JLabel lblNamaKaryawan = new JLabel("Nama Karyawan");
		lblNamaKaryawan.setBounds(10, 36, 100, 14);
		frame.getContentPane().add(lblNamaKaryawan);
		
		JLabel lblBagian = new JLabel("Bagian");
		lblBagian.setBounds(10, 61, 100, 14);
		frame.getContentPane().add(lblBagian);
		
		JLabel lblAlamat = new JLabel("Alamat");
		lblAlamat.setBounds(10, 86, 100, 14);
		frame.getContentPane().add(lblAlamat);
		
		JLabel lblTelp = new JLabel("Telp");
		lblTelp.setBounds(10, 111, 100, 14);
		frame.getContentPane().add(lblTelp);
		
		txtID = new JTextField();
		txtID.setEditable(false);
		txtID.setBounds(120, 8, 177, 20);
		frame.getContentPane().add(txtID);
		txtID.setColumns(10);
		
		txtNama = new JTextField();
		txtNama.setEditable(false);
		txtNama.setBounds(120, 33, 177, 20);
		frame.getContentPane().add(txtNama);
		txtNama.setColumns(10);
		
		txtAlamat = new JTextField();
		txtAlamat.setEditable(false);
		txtAlamat.setBounds(120, 83, 177, 20);
		frame.getContentPane().add(txtAlamat);
		txtAlamat.setColumns(10);
		
		txtTelp = new JTextField();
		txtTelp.setEditable(false);
		txtTelp.setBounds(120, 108, 177, 20);
		frame.getContentPane().add(txtTelp);
		txtTelp.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 204, 287, 155);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				try {
					TampilDataKaryawan(table.getValueAt(table.getSelectedRow(), 0).toString());
				} catch (Exception e) {}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				TampilDataKaryawan(table.getValueAt(table.getSelectedRow(), 0).toString());
			}
		});
		scrollPane.setViewportView(table);
		
		btnTambah = new JButton("Tambah");
		btnTambah.setIcon(new ImageIcon(".\\icons\\TambahUser.png"));
		btnTambah.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtID.setText("");
				txtNama.setText("");
				txtAlamat.setText("");
				txtTelp.setText("");
				txtID.setEditable(true);
				txtNama.setEditable(true);
				txtAlamat.setEditable(true);
				txtTelp.setEditable(true);
				cmbBagian.setSelectedIndex(0);
				btnBatal.setEnabled(true);
				btnEdit.setEnabled(false);
				btnHapus.setEnabled(false);
				btnSimpan.setEnabled(true);
				btnTambah.setEnabled(false);
				btnTutup.setEnabled(false);
				txtID.requestFocus();
			}
		});
		btnTambah.setBounds(10, 136, 138, 23);
		frame.getContentPane().add(btnTambah);
		
		btnEdit = new JButton("Edit");
		btnEdit.setIcon(new ImageIcon(".\\icons\\EditKaryawan.png"));
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnBatal.setEnabled(true);
				btnEdit.setEnabled(false);
				btnHapus.setEnabled(false);
				btnSimpan.setEnabled(true);
				btnTambah.setEnabled(false);
				btnTutup.setEnabled(false);
				
				txtID.setEditable(false);
				txtNama.setEditable(true);
				txtAlamat.setEditable(true);
				txtTelp.setEditable(true);
				txtNama.requestFocus();
				
			}
		});
		btnEdit.setBounds(159, 136, 138, 23);
		frame.getContentPane().add(btnEdit);
		
		btnHapus = new JButton("Hapus");
		btnHapus.setIcon(new ImageIcon(".\\icons\\HapusUser.png"));
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin menghapus data karyawan " + txtID.getText() + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (FMain.konfir == JOptionPane.YES_OPTION){
					if (EksistensiDataBarangTunai() || EksistensiDataBarangKredit())
						JOptionPane.showMessageDialog(null, "Data karyawan tidak boleh dihapus!!!\nData karyawan ini digunakan dalam data transaksi...!!!");
					else{
						if (!UserName.equals("-")) HapusDataUser(UserName);
						HapusDataKaryawan(txtID.getText());
						TampilData();
					}
				}
			}
		});
		btnHapus.setBounds(10, 370, 138, 23);
		frame.getContentPane().add(btnHapus);
		
		btnSimpan = new JButton("Simpan");
		btnSimpan.setIcon(new ImageIcon(".\\icons\\Simpan.png"));
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtNama.getText().equals("") || cmbBagian.getSelectedIndex() < 1){
					JOptionPane.showMessageDialog(null, "Data Belum Lengkap!");
				}
				else{
					if (txtID.isEditable())
						SimpanDataKaryawan();
					else
						EditData(txtID.getText());
					btnBatal.setEnabled(false);
					btnEdit.setEnabled(true);
					btnHapus.setEnabled(true);
					btnSimpan.setEnabled(false);
					btnTambah.setEnabled(true);
					btnTutup.setEnabled(true);
					txtID.setEditable(false);
					txtNama.setEditable(false);
					txtAlamat.setEditable(false);
					txtTelp.setEditable(false);
					TampilData();
				}
			}
		});
		btnSimpan.setBounds(159, 170, 138, 23);
		frame.getContentPane().add(btnSimpan);
		
		btnBatal = new JButton("Batal");
		btnBatal.setIcon(new ImageIcon(".\\icons\\Batal.png"));
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnBatal.setEnabled(false);
				btnEdit.setEnabled(true);
				btnHapus.setEnabled(true);
				btnSimpan.setEnabled(false);
				btnTambah.setEnabled(true);
				btnTutup.setEnabled(true);
				
				txtID.setEditable(false);
				txtNama.setEditable(false);
				txtAlamat.setEditable(false);
				txtTelp.setEditable(false);
			}
		});
		btnBatal.setBounds(10, 170, 138, 23);
		frame.getContentPane().add(btnBatal);
		
		cmbBagian = new JComboBox<String>();
		cmbBagian.setBounds(120, 58, 177, 20);
		frame.getContentPane().add(cmbBagian);
		
		btnTutup = new JButton("Tutup");
		btnTutup.setMnemonic(KeyEvent.VK_X);
		btnTutup.setIcon(new ImageIcon(".\\icons\\Tutup.png"));
		btnTutup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FMain.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnTutup.setBounds(159, 370, 138, 23);
		frame.getContentPane().add(btnTutup);
	}
	
	private void TampilData(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"ID",
					"Nama",
					"Bagian",
					"Alamat",
					"Telp"
			};
			DefaultTableModel model = new DefaultTableModel(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
				       //all cells false
				       return false;
				}
			};
			model.setColumnIdentifiers(columnNames);
			
			String query;
			Statement st;
			ResultSet rs;
			query = "select * from tb_karyawan";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("id"),
						rs.getString("nama"),
						rs.getString("bagian"),
						rs.getString("alamat"),
						rs.getString("telp")
				});
			};	
			
			table.setModel(model);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(3).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			
			st.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
	}
	
	private void SimpanDataKaryawan(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			PreparedStatement psQuery=null;
			String query = "insert into tb_karyawan values (?,?,?,?,?,?)";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtID.getText());
			psQuery.setString(2, "-");
			psQuery.setString(3, txtNama.getText());
			psQuery.setString(4, cmbBagian.getSelectedItem().toString());
			psQuery.setString(5, txtAlamat.getText());
			psQuery.setString(6, txtTelp.getText());
			psQuery.execute();
				
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Data Berhasil Tersimpan...");
		}
		catch(Exception e){System.out.println("Ada error bro simpan transaksi " + e);}
	}
	
	private void TampilDataKaryawan(String IdKaryawan){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT * from tb_karyawan where id = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdKaryawan);
			psQuery.execute();
			rs = psQuery.getResultSet();
			while (rs.next()){
				txtID.setText(rs.getString("id"));
				txtNama.setText(rs.getString("nama"));
				txtAlamat.setText(rs.getString("alamat"));
				txtTelp.setText(rs.getString("telp"));
				if (rs.getString("bagian").equals("Admin")) cmbBagian.setSelectedIndex(1);
				else if (rs.getString("bagian").equals("Kasir")) cmbBagian.setSelectedIndex(2);
				//else if (rs.getString("bagian").equals("Sales")) cmbBagian.setSelectedIndex(3);
				UserName = rs.getString("username");
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
	}
	
	private void HapusDataKaryawan(String IdKaryawan){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "delete from tb_karyawan where id = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, IdKaryawan);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Karyawan dengan id " + IdKaryawan + " berhasil dihapus");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Hapus Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private void HapusDataUser(String username){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "delete from tb_user where user = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, username);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Karyawan dengan id " + username + " berhasil dihapus");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Hapus Data Gagal");
			System.out.println("error hapus data: " + eee);
		}
	}
	
	private void EditData(String IdKaryawan){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_karyawan set nama=?, bagian=?, alamat=?, telp=? where id = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtNama.getText());
			psQuery.setString(2, cmbBagian.getSelectedItem().toString());
			psQuery.setString(3, txtAlamat.getText());
			psQuery.setString(4, txtTelp.getText());
			psQuery.setString(5, IdKaryawan);
			psQuery.execute();
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Karyawan dengan id " + IdKaryawan + " berhasil diedit...");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private boolean EksistensiDataBarangTunai(){
		boolean hasil=false;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT id_karyawan from tb_transaksi where id_karyawan = ?";
				
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,txtID.getText());
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			
			if (rowcount > 0 )				
				hasil = true;
			else
				hasil = false;
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cari datanya " + e);}
		return hasil;
	}
	
	private boolean EksistensiDataBarangKredit(){
		boolean hasil=false;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT id_karyawan from tb_transaksi_kredit where id_karyawan = ?";
				
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,txtID.getText());
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			
			if (rowcount > 0 )				
				hasil = true;
			else
				hasil = false;
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cari datanya " + e);}
		return hasil;
	}
}
