import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ListSelectionModel;

public class FPelanggan {

	private JFrame frame;
	static FPelanggan window;
	private JTextField txtId;
	private JTextField txtNama;
	private JTextField txtNamaToko;
	private JTextField txtAlamat;
	private JTextField txtTelp;
	private JTextField txtCari;
	private JTextField txtJmlPelanggan;
	private JTable tblPelanggan;
	private JButton btnSimpan;
	private JButton btnBatal;
	private JButton btnTambah;
	private JButton btnEdit;
	private JButton btnHapus;
	private JPanel panel;
	private String IdPelanggan = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FPelanggan();
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
	public FPelanggan() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Data Pelanggan");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				Inisialisasi();
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
		frame.setResizable(false);
		frame.setBounds(100, 100, 417, 494);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		txtId = new JTextField();
		txtId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					txtNama.requestFocus();
				}
			}
		});
		txtId.setBounds(124, 31, 168, 20);
		frame.getContentPane().add(txtId);
		txtId.setColumns(10);
		
		txtNama = new JTextField();
		txtNama.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					txtNamaToko.requestFocus();
				}
			}
		});
		txtNama.setBounds(124, 56, 168, 20);
		frame.getContentPane().add(txtNama);
		txtNama.setColumns(10);
		
		txtNamaToko = new JTextField();
		txtNamaToko.setBounds(124, 81, 168, 20);
		frame.getContentPane().add(txtNamaToko);
		txtNamaToko.setColumns(10);
		
		txtAlamat = new JTextField();
		txtAlamat.setBounds(124, 106, 168, 20);
		frame.getContentPane().add(txtAlamat);
		txtAlamat.setColumns(10);
		
		txtTelp = new JTextField();
		txtTelp.setBounds(124, 131, 168, 20);
		frame.getContentPane().add(txtTelp);
		txtTelp.setColumns(10);
		
		JLabel lblIdPelanggan = new JLabel("ID Pelanggan");
		lblIdPelanggan.setFont(new Font("Tahoma", lblIdPelanggan.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblIdPelanggan.getFont().getSize()));
		lblIdPelanggan.setBounds(10, 34, 104, 14);
		frame.getContentPane().add(lblIdPelanggan);
		
		JLabel lblNamaPelanggan = new JLabel("Nama Pelanggan");
		lblNamaPelanggan.setFont(new Font("Tahoma", lblNamaPelanggan.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblNamaPelanggan.getFont().getSize()));
		lblNamaPelanggan.setBounds(10, 59, 104, 14);
		frame.getContentPane().add(lblNamaPelanggan);
		
		JLabel lblNamaToko = new JLabel("Nama Toko");
		lblNamaToko.setFont(new Font("Tahoma", lblNamaToko.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblNamaToko.getFont().getSize()));
		lblNamaToko.setBounds(10, 84, 104, 14);
		frame.getContentPane().add(lblNamaToko);
		
		JLabel lblAlamat = new JLabel("Alamat");
		lblAlamat.setFont(new Font("Tahoma", lblAlamat.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblAlamat.getFont().getSize()));
		lblAlamat.setBounds(10, 109, 104, 14);
		frame.getContentPane().add(lblAlamat);
		
		JLabel lblTelp = new JLabel("Telp.");
		lblTelp.setFont(new Font("Tahoma", lblTelp.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblTelp.getFont().getSize()));
		lblTelp.setBounds(10, 134, 104, 14);
		frame.getContentPane().add(lblTelp);
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Daftar Data Pelanggan", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 164, 392, 257);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 51, 372, 157);
		panel.add(scrollPane);
		
		tblPelanggan = new JTable();
		tblPelanggan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblPelanggan.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				try {
					IdPelanggan = tblPelanggan.getValueAt(tblPelanggan.getSelectedRow(), 0).toString();
					TampilData(IdPelanggan);
					IdPelanggan = null;
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
		tblPelanggan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				try {
					IdPelanggan = tblPelanggan.getValueAt(tblPelanggan.getSelectedRow(), 0).toString();
					TampilData(IdPelanggan);
					IdPelanggan = null;
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
		scrollPane.setViewportView(tblPelanggan);
		
		JLabel lblCariData = new JLabel("Cari Data");
		lblCariData.setFont(new Font("Tahoma", lblCariData.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblCariData.getFont().getSize()));
		lblCariData.setBounds(10, 26, 64, 14);
		panel.add(lblCariData);
		
		txtCari = new JTextField();
		txtCari.setForeground(Color.LIGHT_GRAY);
		txtCari.setFont(new Font("Tahoma", txtCari.getFont().getStyle() & ~Font.BOLD | Font.ITALIC, txtCari.getFont().getSize()));
		txtCari.setText("Masukkan ID atau Nama Pelanggan");
		txtCari.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if (txtCari.getText().equals("Masukkan ID atau Nama Pelanggan")){
					txtCari.setText("");
					txtCari.setForeground(Color.BLACK);
					txtCari.setFont(new Font("Tahoma", Font.PLAIN, 11));
				}
				
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (txtCari.getText().equals("")){
					txtCari.setText("Masukkan ID atau Nama Pelanggan");
					txtCari.setForeground(Color.LIGHT_GRAY);
					txtCari.setFont(new Font("Tahoma", Font.ITALIC, 11));
					TampilSemuaData();
				}
			}
		});
		txtCari.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				EventChange();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				EventChange();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				EventChange();
			}
			private void EventChange(){
				CariData(txtCari.getText());
			}
		});
		txtCari.setBounds(63, 23, 319, 20);
		panel.add(txtCari);
		txtCari.setColumns(10);
		
		btnTambah = new JButton("Tambah");
		btnTambah.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtId.setText("");
				txtNama.setText("");
				txtNamaToko.setText("");
				txtAlamat.setText("");
				txtTelp.setText("");
				btnSimpan.setVisible(true);
				btnBatal.setVisible(true);
				btnEdit.setEnabled(false);
				btnHapus.setEnabled(false);
				btnTambah.setEnabled(false);
				txtCari.setEnabled(false);
				tblPelanggan.setEnabled(false);
				txtId.requestFocus();
				
			}
		});
		btnTambah.setFont(new Font("Tahoma", btnTambah.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, btnTambah.getFont().getSize()));
		btnTambah.setBounds(10, 219, 71, 23);
		panel.add(btnTambah);
		
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtId.getText().equals("Umum")){
					JOptionPane.showMessageDialog(null, "Data dengan id ini tidak boleh diedit!!!");
				}
				else{
					txtId.setEditable(false);
					btnSimpan.setVisible(true);
					btnBatal.setVisible(true);
					btnEdit.setEnabled(false);
					btnHapus.setEnabled(false);
					btnTambah.setEnabled(false);
					txtCari.setEnabled(false);
					tblPelanggan.setEnabled(false);
					txtNama.requestFocus();
				}
				
			}
		});
		btnEdit.setFont(new Font("Tahoma", btnEdit.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, btnEdit.getFont().getSize()));
		btnEdit.setBounds(91, 219, 71, 23);
		panel.add(btnEdit);
		
		btnHapus = new JButton("Hapus");
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtId.getText().equals("Umum")){
					JOptionPane.showMessageDialog(null, "Data dengan id ini tidak boleh dihapus!!!");
				}
				else{
					IdPelanggan = tblPelanggan.getValueAt(tblPelanggan.getSelectedRow(), 0).toString();
					FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin menghapus data pelanggan " + IdPelanggan + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
					if (FMain.konfir == JOptionPane.YES_OPTION){
						HapusData(IdPelanggan);
						Inisialisasi();
						IdPelanggan = null;
					}
				}
			}
		});
		btnHapus.setFont(new Font("Tahoma", btnEdit.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, btnEdit.getFont().getSize()));
		btnHapus.setBounds(172, 219, 71, 23);
		panel.add(btnHapus);
		
		JLabel lblJumlahPelanggan = new JLabel("Jml Pelanggan");
		lblJumlahPelanggan.setFont(new Font("Tahoma", lblJumlahPelanggan.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblJumlahPelanggan.getFont().getSize()));
		lblJumlahPelanggan.setBounds(260, 223, 98, 14);
		panel.add(lblJumlahPelanggan);
		
		txtJmlPelanggan = new JTextField();
		txtJmlPelanggan.setEditable(false);
		txtJmlPelanggan.setHorizontalAlignment(SwingConstants.CENTER);
		txtJmlPelanggan.setBounds(344, 220, 38, 20);
		panel.add(txtJmlPelanggan);
		txtJmlPelanggan.setColumns(10);
		
		JButton btnTutup = new JButton("Tutup");
		btnTutup.setMnemonic(KeyEvent.VK_X);
		btnTutup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FMain.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnTutup.setFont(new Font("Tahoma", btnEdit.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, btnEdit.getFont().getSize()));
		btnTutup.setBounds(298, 432, 104, 23);
		frame.getContentPane().add(btnTutup);
		
		btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtId.isEditable()){
					FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin menyimpan data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
					if (FMain.konfir == JOptionPane.YES_OPTION){
						SimpanData();
					}
				}
				else {
					IdPelanggan = tblPelanggan.getValueAt(tblPelanggan.getSelectedRow(), 0).toString();
					FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin mengedit data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
					if (FMain.konfir == JOptionPane.YES_OPTION){
						EditData(IdPelanggan);
					}
				}
				Inisialisasi();
				IdPelanggan = null;				
			}
		});
		btnSimpan.setBounds(302, 30, 89, 23);
		frame.getContentPane().add(btnSimpan);
		
		btnBatal = new JButton("Batal");
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtId.setEditable(true);
				btnSimpan.setVisible(false);
				btnBatal.setVisible(false);
				btnEdit.setEnabled(true);
				btnHapus.setEnabled(true);
				btnTambah.setEnabled(true);
				txtCari.setEnabled(true);
				tblPelanggan.setEnabled(true);
				txtJmlPelanggan.setText(JmlPelanggan());
			}
		});
		btnBatal.setBounds(302, 55, 89, 23);
		frame.getContentPane().add(btnBatal);
		//frame.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txtId, txtNama, txtNamaToko, txtAlamat, txtTelp}));
	}
	
	private void CariData(String param1){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"ID",
					"Nama Pelanggan",
					"Nama Toko",
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
				       return false;
				}
			};
			model.setColumnIdentifiers(columnNames);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT * from tb_pelanggan where id like ? or nama like ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,"%" + param1 + "%");
			psQuery.setString(2,"%" + param1 + "%");
			psQuery.execute();
			rs = psQuery.getResultSet();
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("id"),
						rs.getString("nama"),
						rs.getString("nama_toko"),
						rs.getString("alamat"),
						rs.getString("telp")
				});
			};	
			
			tblPelanggan.setModel(model);
			tblPelanggan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblPelanggan.getColumnModel().getColumn(0).setPreferredWidth(100);
			tblPelanggan.getColumnModel().getColumn(1).setPreferredWidth(100);
			tblPelanggan.getColumnModel().getColumn(2).setPreferredWidth(100);
			tblPelanggan.getColumnModel().getColumn(3).setPreferredWidth(100);
			tblPelanggan.getColumnModel().getColumn(4).setPreferredWidth(100);
			
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
	}
	
	private void TampilSemuaData(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"ID",
					"Nama Pelanggan",
					"Nama Toko",
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
			query = "select * from tb_pelanggan where not id = 'Umum'";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("id"),
						rs.getString("nama"),
						rs.getString("nama_toko"),
						rs.getString("alamat"),
						rs.getString("telp")
				});
			};	
			
			tblPelanggan.setModel(model);
			tblPelanggan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblPelanggan.getColumnModel().getColumn(0).setPreferredWidth(100);
			tblPelanggan.getColumnModel().getColumn(1).setPreferredWidth(100);
			tblPelanggan.getColumnModel().getColumn(2).setPreferredWidth(100);
			tblPelanggan.getColumnModel().getColumn(3).setPreferredWidth(100);
			tblPelanggan.getColumnModel().getColumn(4).setPreferredWidth(100);
			
			st.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
	}
	
	private void TampilData(String IdPelanggan){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT * from tb_pelanggan where id = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdPelanggan);
			psQuery.execute();
			rs = psQuery.getResultSet();
			while (rs.next()){
				txtId.setText(rs.getString("id"));
				txtNama.setText(rs.getString("nama"));
				txtNamaToko.setText(rs.getString("nama_toko"));
				txtAlamat.setText(rs.getString("alamat"));
				txtTelp.setText(rs.getString("telp"));
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
	}
	
	private String JmlPelanggan(){
		String jml=null;
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);

			String query;
			Statement st;
			ResultSet rs;
			query = "select count(id) as jmldata from tb_pelanggan where not id = 'Umum'";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				jml = rs.getString("jmldata");
			}
			
			st.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
		return jml;
	}
	
	private void SimpanData(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "insert into tb_pelanggan values (?,?,?,?,?)";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtId.getText());
			psQuery.setString(2, txtNama.getText());
			psQuery.setString(3, txtNamaToko.getText());
			psQuery.setString(4, txtAlamat.getText());
			psQuery.setString(5, txtTelp.getText());
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			btnSimpan.setVisible(false);
			btnBatal.setVisible(false);
			btnEdit.setEnabled(true);
			btnHapus.setEnabled(true);
			btnTambah.setEnabled(true);
			txtCari.setEnabled(true);
			tblPelanggan.setEnabled(true);
			txtJmlPelanggan.setText(JmlPelanggan());
			
			JOptionPane.showMessageDialog(btnSimpan, "Data berhasil tersimpan");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Simpan Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private void HapusData(String IdPel){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "delete from tb_pelanggan where id = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, IdPel);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Pelanggan dengan id " + IdPel + " berhasil dihapus");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Hapus Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private void EditData(String IdPel){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_pelanggan set nama=?, nama_toko=?, alamat=?, telp=? where id = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtNama.getText());
			psQuery.setString(2, txtNamaToko.getText());
			psQuery.setString(3, txtAlamat.getText());
			psQuery.setString(4, txtTelp.getText());
			psQuery.setString(5, IdPel);
			psQuery.execute();
			psQuery.close();
			conn.close();
			
			txtId.setEditable(true);
			btnSimpan.setVisible(false);
			btnBatal.setVisible(false);
			btnEdit.setEnabled(true);
			btnHapus.setEnabled(true);
			btnTambah.setEnabled(true);
			txtCari.setEnabled(true);
			tblPelanggan.setEnabled(true);
			txtJmlPelanggan.setText(JmlPelanggan());
			
			JOptionPane.showMessageDialog(null, "Pelanggan dengan id " + IdPel + " berhasil diedit...");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private void Inisialisasi(){
		btnSimpan.setVisible(false);
		btnBatal.setVisible(false);
		txtJmlPelanggan.setText(JmlPelanggan());
		txtId.setText("");
		txtNama.setText("");
		txtNamaToko.setText("");
		txtTelp.setText("");
		txtAlamat.setText("");
		txtCari.setText("");
		TampilSemuaData();
	}
}
