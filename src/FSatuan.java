import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ListSelectionModel;

public class FSatuan {

	private JFrame frame;
	static FSatuan window;
	private JTextField txtIdSatuan;
	private JTextField txtNamaSatuan;
	private JTable tblSatuan;
	private JButton btnSimpan;
	private JButton btnBatal;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FSatuan();
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
	public FSatuan() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Set Data Satuan");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				TampilData();
				txtIdSatuan.setText("");
				txtNamaSatuan.setText("");
				btnSimpan.setVisible(false);
				btnBatal.setVisible(false);
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
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
		frame.setBounds(100, 100, 317, 239);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblIdSatuan = new JLabel("ID Satuan");
		lblIdSatuan.setBounds(10, 11, 84, 14);
		frame.getContentPane().add(lblIdSatuan);
		
		JLabel lblNamaSatuan = new JLabel("Nama Satuan");
		lblNamaSatuan.setBounds(10, 36, 84, 14);
		frame.getContentPane().add(lblNamaSatuan);
		
		txtIdSatuan = new JTextField();
		txtIdSatuan.setBounds(91, 8, 102, 20);
		frame.getContentPane().add(txtIdSatuan);
		txtIdSatuan.setColumns(10);
		
		txtNamaSatuan = new JTextField();
		txtNamaSatuan.setBounds(91, 33, 102, 20);
		frame.getContentPane().add(txtNamaSatuan);
		txtNamaSatuan.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 61, 183, 130);
		frame.getContentPane().add(scrollPane);
		
		tblSatuan = new JTable();
		tblSatuan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblSatuan.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				try {
					TampilDataSatuan(tblSatuan.getValueAt(tblSatuan.getSelectedRow(), 0).toString());
				} catch (Exception e) {
					System.out.println(3);
				}
			}
		});
		tblSatuan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				TampilDataSatuan(tblSatuan.getValueAt(tblSatuan.getSelectedRow(), 0).toString());
			}
		});
		scrollPane.setViewportView(tblSatuan);
		
		JButton btnTambah = new JButton("Tambah");
		btnTambah.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtIdSatuan.setText("");
				txtNamaSatuan.setText("");
				btnSimpan.setVisible(true);
				btnBatal.setVisible(true);
				txtIdSatuan.requestFocus();
			}
		});
		btnTambah.setBounds(203, 100, 89, 23);
		frame.getContentPane().add(btnTambah);
		
		JButton btnHapus = new JButton("Hapus");
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (FMain.konfir == JOptionPane.YES_OPTION){
					HapusDataSatuan(txtIdSatuan.getText());
					TampilData();
				}
			}
		});
		btnHapus.setBounds(203, 134, 89, 23);
		frame.getContentPane().add(btnHapus);
		
		btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SimpanData();
			}
		});
		btnSimpan.setBounds(203, 7, 89, 23);
		frame.getContentPane().add(btnSimpan);
		
		btnBatal = new JButton("Batal");
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtIdSatuan.setText("");
				txtNamaSatuan.setText("");
				btnSimpan.setVisible(false);
				btnBatal.setVisible(false);
			}
		});
		btnBatal.setBounds(203, 32, 89, 23);
		frame.getContentPane().add(btnBatal);
		
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
		btnTutup.setBounds(203, 168, 89, 23);
		frame.getContentPane().add(btnTutup);
		//frame.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txtIdSatuan, txtNamaSatuan, lblIdSatuan, lblNamaSatuan, scrollPane, tblSatuan, btnTambah, btnHapus, btnSimpan, btnBatal}));
	}
	
	private void TampilData(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"Id Satuan",
					"Nama Satuan"
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
			query = "select * from tb_satuan";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("id"),
						rs.getString("satuan")
				});
			};	
			
			tblSatuan.setModel(model);
			tblSatuan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblSatuan.getColumnModel().getColumn(0).setPreferredWidth(50);
			tblSatuan.getColumnModel().getColumn(1).setPreferredWidth(70);
			
			st.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void SimpanData(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "insert into tb_satuan values (?,?)";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtIdSatuan.getText());
			psQuery.setString(2, txtNamaSatuan.getText());
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			txtIdSatuan.setText("");
			txtNamaSatuan.setText("");
			btnSimpan.setVisible(false);
			btnBatal.setVisible(false);
			
			
			JOptionPane.showMessageDialog(null, "Data berhasil tersimpan");
			
		} catch (Exception eee) {
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private void TampilDataSatuan(String IdSatuan){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT * from tb_satuan where id = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdSatuan);
			psQuery.execute();
			rs = psQuery.getResultSet();
			while (rs.next()){
				txtIdSatuan.setText(rs.getString("id"));
				txtNamaSatuan.setText(rs.getString("satuan"));
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
	}
	
	private void HapusDataSatuan(String IdSatuan){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "delete from tb_satuan where id = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, IdSatuan);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Satuan dengan id " + IdSatuan + " berhasil dihapus");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Hapus Data Gagal");
			System.out.println("error hapus data satuan: " + eee);
		}
	}
}
