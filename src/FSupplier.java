import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import javax.swing.ListSelectionModel;

public class FSupplier {

	private JFrame frame;
	static FSupplier window;
	private JTextField txtId;
	private JTextField txtNama;
	private JTextField txtAlamat;
	private JTextField txtTelp;
	private JTable table;
	private JButton btnTambah, btnEdit, btnHapus, btnSimpan, btnBatal, btnTutup;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FSupplier();
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
	public FSupplier() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Supplier.png"));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				txtId.setText("");
				txtNama.setText("");
				txtAlamat.setText("");
				txtTelp.setText("");
				txtId.setEditable(false);
				txtNama.setEditable(false);
				txtAlamat.setEditable(false);
				txtTelp.setEditable(false);
				btnTambah.setEnabled(true);
				btnEdit.setEnabled(true);
				btnHapus.setEnabled(true);
				btnSimpan.setEnabled(false);
				btnBatal.setEnabled(false);
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
		frame.setTitle("Data Supplier");
		frame.setResizable(false);
		frame.setBounds(100, 100, 311, 471);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblIdSupplier = new JLabel("ID Supplier");
		lblIdSupplier.setBounds(10, 11, 75, 14);
		frame.getContentPane().add(lblIdSupplier);
		
		JLabel lblNama = new JLabel("Nama");
		lblNama.setBounds(10, 36, 75, 14);
		frame.getContentPane().add(lblNama);
		
		JLabel lblAlamat = new JLabel("Alamat");
		lblAlamat.setBounds(10, 61, 75, 14);
		frame.getContentPane().add(lblAlamat);
		
		JLabel lblTelp = new JLabel("Telp");
		lblTelp.setBounds(10, 86, 75, 14);
		frame.getContentPane().add(lblTelp);
		
		txtId = new JTextField();
		txtId.setBounds(95, 8, 198, 20);
		frame.getContentPane().add(txtId);
		txtId.setColumns(10);
		
		txtNama = new JTextField();
		txtNama.setBounds(95, 33, 198, 20);
		frame.getContentPane().add(txtNama);
		txtNama.setColumns(10);
		
		txtAlamat = new JTextField();
		txtAlamat.setBounds(95, 58, 198, 20);
		frame.getContentPane().add(txtAlamat);
		txtAlamat.setColumns(10);
		
		txtTelp = new JTextField();
		txtTelp.setBounds(95, 83, 198, 20);
		frame.getContentPane().add(txtTelp);
		txtTelp.setColumns(10);
		
		btnTambah = new JButton("Tambah");
		btnTambah.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtId.setText("");
				txtNama.setText("");
				txtAlamat.setText("");
				txtTelp.setText("");
				txtId.setEditable(true);
				txtNama.setEditable(true);
				txtAlamat.setEditable(true);
				txtTelp.setEditable(true);
				btnTambah.setEnabled(false);
				btnEdit.setEnabled(false);
				btnHapus.setEnabled(false);
				btnSimpan.setEnabled(true);
				btnBatal.setEnabled(true);
				btnTutup.setEnabled(false);
				txtId.requestFocus();
			}
		});
		btnTambah.setBounds(10, 111, 89, 23);
		frame.getContentPane().add(btnTambah);
		
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtId.setEditable(false);
				txtNama.setEditable(true);
				txtAlamat.setEditable(true);
				txtTelp.setEditable(true);
				btnTambah.setEnabled(false);
				btnEdit.setEnabled(false);
				btnHapus.setEnabled(false);
				btnSimpan.setEnabled(true);
				btnBatal.setEnabled(true);
				btnTutup.setEnabled(false);
				txtId.requestFocus();
			}
		});
		btnEdit.setBounds(105, 111, 89, 23);
		frame.getContentPane().add(btnEdit);
		
		btnHapus = new JButton("Hapus");
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (FMain.konfir == JOptionPane.YES_OPTION){
					HapusDataSupplier(txtId.getText());
					TampilData();
				}
			}
		});
		btnHapus.setBounds(204, 111, 89, 23);
		frame.getContentPane().add(btnHapus);
		
		btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtNama.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Data belum lengkap!!!");
				}
				else{
					if (txtId.isEditable()){
						SimpanDataSupplier();
					}
					else{
						EditDataSupplier(txtId.getText());
					}
					txtId.setText("");
					txtNama.setText("");
					txtAlamat.setText("");
					txtTelp.setText("");
					txtId.setEditable(false);
					txtNama.setEditable(false);
					txtAlamat.setEditable(false);
					txtTelp.setEditable(false);
					btnTambah.setEnabled(true);
					btnEdit.setEnabled(true);
					btnHapus.setEnabled(true);
					btnSimpan.setEnabled(false);
					btnBatal.setEnabled(false);
					btnTutup.setEnabled(true);
					TampilData();
				}
			}
		});
		btnSimpan.setBounds(10, 145, 89, 23);
		frame.getContentPane().add(btnSimpan);
		
		btnBatal = new JButton("Batal");
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtId.setText("");
				txtNama.setText("");
				txtAlamat.setText("");
				txtTelp.setText("");
				txtId.setEditable(false);
				txtNama.setEditable(false);
				txtAlamat.setEditable(false);
				txtTelp.setEditable(false);
				btnTambah.setEnabled(true);
				btnEdit.setEnabled(true);
				btnHapus.setEnabled(true);
				btnSimpan.setEnabled(false);
				btnBatal.setEnabled(false);
				btnTutup.setEnabled(true);
			}
		});
		btnBatal.setBounds(105, 145, 89, 23);
		frame.getContentPane().add(btnBatal);
		
		btnTutup = new JButton("Tutup");
		btnTutup.setMnemonic(KeyEvent.VK_X);
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
		btnTutup.setBounds(204, 145, 89, 23);
		frame.getContentPane().add(btnTutup);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 179, 283, 253);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				try {
					TampilDataSupplier(table.getValueAt(table.getSelectedRow(), 0).toString());
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				TampilDataSupplier(table.getValueAt(table.getSelectedRow(), 0).toString());
			}
		});
		scrollPane.setViewportView(table);
	}
	
	private void TampilData(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"ID Supplier",
					"Nama",
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
			query = "select * from tb_supplier";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("id"),
						rs.getString("nama"),
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
			
			st.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data supplier: " + e);
		}
	}
	
	private void SimpanDataSupplier(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			PreparedStatement psQuery=null;
			String query = "insert into tb_supplier values (?,?,?,?)";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtId.getText());
			psQuery.setString(2, txtNama.getText());
			psQuery.setString(3, txtAlamat.getText());
			psQuery.setString(4, txtTelp.getText());
			psQuery.execute();
				
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Data Berhasil Tersimpan...");
		}
		catch(Exception e){System.out.println("Ada error bro simpan transaksi " + e);}
	}
	
	private void EditDataSupplier(String IdSupplier){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_supplier set nama=?, alamat=?, telp=? where id = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtNama.getText());
			psQuery.setString(2, txtAlamat.getText());
			psQuery.setString(3, txtTelp.getText());
			psQuery.setString(4, IdSupplier);
			psQuery.execute();
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Supplier dengan id " + IdSupplier + " berhasil diedit...");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Supplier Gagal:" + eee);
		}
	}
	
	private void TampilDataSupplier(String IdSupplier){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT * from tb_supplier where id = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdSupplier);
			psQuery.execute();
			rs = psQuery.getResultSet();
			while (rs.next()){
				txtId.setText(rs.getString("id"));
				txtNama.setText(rs.getString("nama"));
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
	
	private void HapusDataSupplier(String IdSupplier){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "delete from tb_supplier where id = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, IdSupplier);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Supplier dengan id " + IdSupplier + " berhasil dihapus");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Hapus Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
}
