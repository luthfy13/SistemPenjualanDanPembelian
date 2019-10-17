import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ListSelectionModel;

public class FSettingUser {

	JFrame frame;
	private JTable table;
	static FSettingUser window;
	private JButton btnTutup;
	private JButton btnHapus;
	private JTextField txtId;
	private JTextField txtNama;
	private JTextField txtBagian;
	private JTextField txtAlamat;
	private JTextField txtTelp;
	private JLabel lblUsername;
	private JLabel lblLevelUser;
	private JTextField txtUsername;
	private JTextField txtLevel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FSettingUser();
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
	public FSettingUser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Setting User");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\EditUser.png"));
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
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
		frame.setBounds(100, 100, 399, 366);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 193, 368, 134);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				try {
					TampilDataKaryawan(table.getValueAt(table.getSelectedRow(), 0).toString());
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2 && !arg0.isConsumed()){
					arg0.consume();
					if (txtUsername.getText().equals("-")){
						FTambahUser.main(null);
						FTambahUser.IdKaryawan = txtId.getText();
						FTambahUser.NamaKaryawan = txtNama.getText();
						window.frame.setEnabled(false);
					}
					else
						JOptionPane.showMessageDialog(null, "User sudah ada!!!");
				}
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				TampilDataKaryawan(table.getValueAt(table.getSelectedRow(), 0).toString());
			}
		});
		scrollPane.setViewportView(table);
		
		btnTutup = new JButton("Tutup");
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
		btnTutup.setBounds(262, 164, 116, 23);
		frame.getContentPane().add(btnTutup);
		
		JButton btnEdit = new JButton("Ganti Password");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtUsername.getText().equals("-")){
					JOptionPane.showMessageDialog(null, "User belum ada!!!");
				}
				else{
					FGantiPassword.main(null);
					FGantiPassword.UserName = txtUsername.getText();
					window.frame.setEnabled(false);
				}
					
			}
		});
		btnEdit.setBounds(262, 114, 116, 23);
		frame.getContentPane().add(btnEdit);
		
		JButton btnTambah = new JButton("Tambah");
		btnTambah.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtUsername.getText().equals("-")){
					FTambahUser.main(null);
					FTambahUser.IdKaryawan = txtId.getText();
					FTambahUser.NamaKaryawan = txtNama.getText();
					window.frame.setEnabled(false);
				}
				else
					JOptionPane.showMessageDialog(null, "User sudah ada!!!");
			}
		});
		btnTambah.setBounds(262, 89, 116, 23);
		frame.getContentPane().add(btnTambah);
		
		btnHapus = new JButton("Hapus");
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtUsername.getText().equals("-")){
					JOptionPane.showMessageDialog(null, "User belum ada!!!");
				}
				else{
					FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
					if (FMain.konfir == JOptionPane.YES_OPTION){
						HapusData(txtUsername.getText());
						UpdateDataKaryawan(txtUsername.getText());
					}
				}
			}
		});
		btnHapus.setBounds(262, 139, 116, 23);
		frame.getContentPane().add(btnHapus);
		
		JLabel lblIdKaryawan = new JLabel("ID");
		lblIdKaryawan.setBounds(10, 14, 84, 14);
		frame.getContentPane().add(lblIdKaryawan);
		
		JLabel lblNamaKaryawan = new JLabel("Nama");
		lblNamaKaryawan.setBounds(10, 41, 84, 14);
		frame.getContentPane().add(lblNamaKaryawan);
		
		JLabel lblBagian = new JLabel("Bagian");
		lblBagian.setBounds(10, 68, 84, 14);
		frame.getContentPane().add(lblBagian);
		
		JLabel lblAlamat = new JLabel("Alamat");
		lblAlamat.setBounds(10, 93, 84, 14);
		frame.getContentPane().add(lblAlamat);
		
		JLabel lblTelp = new JLabel("Telp");
		lblTelp.setBounds(10, 118, 84, 14);
		frame.getContentPane().add(lblTelp);
		
		txtId = new JTextField();
		txtId.setBounds(86, 11, 166, 20);
		frame.getContentPane().add(txtId);
		txtId.setColumns(10);
		
		txtNama = new JTextField();
		txtNama.setBounds(86, 38, 166, 20);
		frame.getContentPane().add(txtNama);
		txtNama.setColumns(10);
		
		txtBagian = new JTextField();
		txtBagian.setBounds(86, 65, 166, 20);
		frame.getContentPane().add(txtBagian);
		txtBagian.setColumns(10);
		
		txtAlamat = new JTextField();
		txtAlamat.setBounds(86, 90, 166, 20);
		frame.getContentPane().add(txtAlamat);
		txtAlamat.setColumns(10);
		
		txtTelp = new JTextField();
		txtTelp.setBounds(86, 115, 166, 20);
		frame.getContentPane().add(txtTelp);
		txtTelp.setColumns(10);
		
		lblUsername = new JLabel("Username");
		lblUsername.setBounds(10, 143, 84, 14);
		frame.getContentPane().add(lblUsername);
		
		lblLevelUser = new JLabel("Level User");
		lblLevelUser.setBounds(10, 168, 84, 14);
		frame.getContentPane().add(lblLevelUser);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(86, 140, 166, 20);
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		txtLevel = new JTextField();
		txtLevel.setBounds(86, 165, 166, 20);
		frame.getContentPane().add(txtLevel);
		txtLevel.setColumns(10);
		//frame.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{frame.getContentPane(), scrollPane, table, btnTambah, btnEdit, btnHapus, btnTutup}));
	}
	
	private void TampilData(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"ID Karyawan",
					"Nama Karyawan",
					"Bagian"
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
			query = "select id,nama,bagian from tb_karyawan order by id";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("id"),
						rs.getString("nama"),
						rs.getString("bagian")
				});
			};	
			
			table.setModel(model);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);

			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void TampilDataKaryawan(String IdKaryawan){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT "
					+ "tb_karyawan.id, "
					+ "tb_karyawan.username, "
					+ "tb_karyawan.nama, "
					+ "tb_karyawan.bagian, "
					+ "tb_karyawan.alamat, "
					+ "tb_karyawan.telp, "
					+ "tb_user.`level` "
					+ "FROM tb_karyawan "
					+ "LEFT JOIN "
					+ "tb_user ON tb_karyawan.username = tb_user.`user` "
					+ "where tb_karyawan.id = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdKaryawan);
			psQuery.execute();
			rs = psQuery.getResultSet();
			while (rs.next()){
				txtId.setText(rs.getString("id"));
				txtNama.setText(rs.getString("nama"));
				txtBagian.setText(rs.getString("bagian"));
				txtAlamat.setText(rs.getString("alamat"));
				txtTelp.setText(rs.getString("telp"));
				txtUsername.setText(rs.getString("username"));
				txtLevel.setText(rs.getString("level"));
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data karyawan: " + e);
		}
	}
	
	private void HapusData(String UserName){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "delete from tb_user where user = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, UserName);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "User berhasil dihapus...");
			
		} catch (Exception eee) {
			System.out.println("hapus data error : " + eee);
		}
	}
	
	private void UpdateDataKaryawan(String UserName){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_karyawan set username = '-' where username = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, UserName);
			psQuery.execute();
			psQuery.close();
			conn.close();
			
		} catch (Exception eee) {
			System.out.println("error update data karyawan: " + eee);
		}
	}
}
