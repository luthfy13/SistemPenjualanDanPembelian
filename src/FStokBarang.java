import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class FStokBarang {

	JFrame frame;
	static FStokBarang window;
	private JTable table;
	private JTextField txtIdBarang;
	private JTextField txtNamaBarang;
	private JTextField txtHargaBeli;
	private JTextField txtSatuanBeli;
	static JTable tblDetailBarang;
	private JTextField txtSupplier;
	private String IdBarang = null;
	private JTextField txtCari;
	private JComboBox<String> cmbCari;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FStokBarang();
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
	public FStokBarang() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Barang.png"));
		frame.setTitle("Master Stok Barang");
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBackground(new Color(255, 255, 255));
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				TampilData();
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
		frame.setBounds(100, 100, 765, 466);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JButton btnTutup = new JButton("Tutup");
		btnTutup.setMnemonic(KeyEvent.VK_X);
		btnTutup.setBackground(new Color(255, 255, 255));
		btnTutup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				table = null;
				txtIdBarang = null;
				txtNamaBarang = null;
				txtHargaBeli = null;
				txtSatuanBeli = null;
				tblDetailBarang = null;
				txtSupplier = null;
				IdBarang = null;
				txtCari = null;
				cmbCari = null;
				FMain.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnTutup.setBounds(647, 400, 102, 23);
		frame.getContentPane().add(btnTutup);
		
		JLabel lblIdBarang = new JLabel("ID Barang");
		lblIdBarang.setFont(new Font("Tahoma", lblIdBarang.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblIdBarang.getFont().getSize()));
		lblIdBarang.setBounds(10, 11, 75, 14);
		frame.getContentPane().add(lblIdBarang);
		
		JLabel lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setFont(new Font("Tahoma", lblNamaBarang.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblNamaBarang.getFont().getSize()));
		lblNamaBarang.setBounds(10, 36, 75, 14);
		frame.getContentPane().add(lblNamaBarang);
		
		JLabel lblHargaBeli = new JLabel("Harga Beli");
		lblHargaBeli.setFont(new Font("Tahoma", lblHargaBeli.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblHargaBeli.getFont().getSize()));
		lblHargaBeli.setBounds(10, 61, 75, 14);
		frame.getContentPane().add(lblHargaBeli);
		
		txtIdBarang = new JTextField();
		txtIdBarang.setEditable(false);
		txtIdBarang.setBounds(90, 8, 86, 20);
		frame.getContentPane().add(txtIdBarang);
		txtIdBarang.setColumns(10);
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.setEditable(false);
		txtNamaBarang.setBounds(90, 33, 262, 20);
		frame.getContentPane().add(txtNamaBarang);
		txtNamaBarang.setColumns(10);
		
		txtHargaBeli = new JTextField();
		txtHargaBeli.setEditable(false);
		txtHargaBeli.setBounds(90, 58, 166, 20);
		frame.getContentPane().add(txtHargaBeli);
		txtHargaBeli.setColumns(10);
		
		JLabel label = new JLabel("/");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Arial", label.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, label.getFont().getSize()));
		label.setBounds(257, 61, 9, 14);
		frame.getContentPane().add(label);
		
		txtSatuanBeli = new JTextField();
		txtSatuanBeli.setEditable(false);
		txtSatuanBeli.setBounds(266, 58, 86, 20);
		frame.getContentPane().add(txtSatuanBeli);
		txtSatuanBeli.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Detail Barang", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(410, 114, 340, 153);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 21, 215, 88);
		scrollPane_1.getViewport().setBackground(Color.WHITE);
		panel.add(scrollPane_1);
		
		tblDetailBarang = new JTable();
		tblDetailBarang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblDetailBarang.setBackground(Color.WHITE);
		tblDetailBarang.setGridColor(Color.WHITE);
		scrollPane_1.setViewportView(tblDetailBarang);
		
		JButton btnTambah = new JButton("Pecah Satuan");
		btnTambah.setBackground(Color.WHITE);
		btnTambah.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tblDetailBarang.getSelectedRow() >= 0){
					FPecahSatuan.main(null);
					FPecahSatuan.IdBarang = txtIdBarang.getText();
					FPecahSatuan.NamaBarang = txtNamaBarang.getText();
					window.frame.setEnabled(false);
				}
			}
		});
		btnTambah.setToolTipText("Pecah Satuan Barang");
		btnTambah.setFont(new Font("Arial", btnTambah.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, btnTambah.getFont().getSize()));
		btnTambah.setBounds(10, 119, 116, 23);
		panel.add(btnTambah);
		
		JButton btnEdit = new JButton("Edit Harga");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tblDetailBarang.getSelectedRow() >= 0){
					FEditHarga.main(null);
					FEditHarga.IdBarang = txtIdBarang.getText();
					FEditHarga.NamaBarang = txtNamaBarang.getText();
					FEditHarga.Satuan = tblDetailBarang.getValueAt(FStokBarang.tblDetailBarang.getSelectedRow(), 0).toString();
					FEditHarga.Harga = tblDetailBarang.getValueAt(FStokBarang.tblDetailBarang.getSelectedRow(), 2).toString().replace(",", "");
					window.frame.setEnabled(false);
				}
			}
		});
		btnEdit.setBackground(Color.WHITE);
		btnEdit.setToolTipText("Ubah Harga Barang");
		btnEdit.setFont(new Font("Arial", btnTambah.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, btnTambah.getFont().getSize()));
		btnEdit.setBounds(235, 18, 95, 23);
		panel.add(btnEdit);
		
		JButton btnHapus = new JButton("Hapus Satuan");
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin menghapus satuan barang?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (FMain.konfir == JOptionPane.YES_OPTION){
					if (tblDetailBarang.getRowCount() < 2){
						JOptionPane.showMessageDialog(null, "Detail Barang tidak boleh kosong");
					}
					else
						HapusSatuan(txtIdBarang.getText(), tblDetailBarang.getValueAt(tblDetailBarang.getSelectedRow(), 0).toString());
					TampilDetailBarang(txtIdBarang.getText());
				}
			}
		});
		btnHapus.setBackground(Color.WHITE);
		btnHapus.setToolTipText("Hapus Satuan Barang");
		btnHapus.setFont(new Font("Arial", btnTambah.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, btnTambah.getFont().getSize()));
		btnHapus.setBounds(235, 86, 95, 23);
		panel.add(btnHapus);
		
		JButton btnEditSatuan = new JButton("Edit Satuan Isi");
		btnEditSatuan.setBackground(Color.WHITE);
		btnEditSatuan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tblDetailBarang.getSelectedRow() >= 0){
					FEditSatuan.main(null);
					FEditSatuan.IdBarang = txtIdBarang.getText();
					FEditSatuan.Satuan = tblDetailBarang.getValueAt(tblDetailBarang.getSelectedRow(), 0).toString();
					window.frame.setEnabled(false);
				}
			}
		});
		btnEditSatuan.setBounds(136, 120, 116, 23);
		panel.add(btnEditSatuan);
		
		JButton btnEditJumlahStok = new JButton("Tambah Stok");
		btnEditJumlahStok.setBackground(Color.WHITE);
		btnEditJumlahStok.setBounds(235, 52, 95, 23);
		panel.add(btnEditJumlahStok);
		btnEditJumlahStok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tblDetailBarang.getSelectedRow() >= 0){
					FTambahStok.main(null);
					FTambahStok.IdBarang = txtIdBarang.getText();
					FTambahStok.NamaBarang = txtNamaBarang.getText();
					FTambahStok.Stok = Integer.valueOf(tblDetailBarang.getValueAt(FStokBarang.tblDetailBarang.getSelectedRow(), 1).toString());
					FTambahStok.Satuan = tblDetailBarang.getValueAt(FStokBarang.tblDetailBarang.getSelectedRow(), 0).toString();
					window.frame.setEnabled(false);
				}
			}
		});
		
		JLabel lblSupplier = new JLabel("Supplier");
		lblSupplier.setFont(new Font("Tahoma", lblSupplier.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblSupplier.getFont().getSize()));
		lblSupplier.setBounds(10, 86, 75, 14);
		frame.getContentPane().add(lblSupplier);
		
		txtSupplier = new JTextField();
		txtSupplier.setEditable(false);
		txtSupplier.setBounds(90, 83, 262, 20);
		frame.getContentPane().add(txtSupplier);
		txtSupplier.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Master Data Barang", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 114, 390, 309);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 58, 370, 203);
		panel_1.add(scrollPane);
		scrollPane.getViewport().setBackground(Color.WHITE);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				try {
					IdBarang = table.getValueAt(table.getSelectedRow(), 0).toString();
					TampilDataBarang(IdBarang);
					TampilDetailBarang(IdBarang);
					IdBarang = null;
				} catch (Exception e) {
					System.out.println("Ada error sedikit: " + e);
				}
			}
		});
		table.setBackground(Color.WHITE);
		table.setGridColor(Color.WHITE);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				try {
					IdBarang = table.getValueAt(table.getSelectedRow(), 0).toString();
					TampilDataBarang(IdBarang);
					TampilDetailBarang(IdBarang);
					IdBarang = null;
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
		scrollPane.setViewportView(table);
		
		txtCari = new JTextField();
		txtCari.setToolTipText("Tekan ENTER untuk mencari data");
		txtCari.setBounds(194, 27, 186, 20);
		panel_1.add(txtCari);
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					CariBarang(cmbCari.getSelectedIndex(), txtCari.getText());
				}
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				char keyChar = arg0.getKeyChar();
			    if (Character.isLowerCase(keyChar)) {
			      arg0.setKeyChar(Character.toUpperCase(keyChar));
			    }
			}
		});
		txtCari.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				table.scrollRectToVisible(new Rectangle(table.getCellRect(0, 0, true)));
				table.clearSelection();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				table.scrollRectToVisible(new Rectangle(table.getCellRect(0, 0, true)));
				table.clearSelection();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				table.scrollRectToVisible(new Rectangle(table.getCellRect(0, 0, true)));
				table.clearSelection();
			}
		});
		txtCari.setColumns(10);
		
		cmbCari = new JComboBox<String>();
		cmbCari.setBounds(98, 27, 86, 20);
		panel_1.add(cmbCari);
		cmbCari.setModel(new DefaultComboBoxModel<String>(new String[] {"ID Brg", "Nama Brg"}));
		cmbCari.setSelectedIndex(1);
		
		JLabel lblCariBerdasarkan = new JLabel("Cari berdasarkan");
		lblCariBerdasarkan.setBounds(10, 30, 86, 14);
		panel_1.add(lblCariBerdasarkan);
		
		JButton btnTambahItem = new JButton("Tambah Item");
		btnTambahItem.setBounds(10, 272, 105, 23);
		panel_1.add(btnTambahItem);
		btnTambahItem.setBackground(new Color(255, 255, 255));
		
		JButton btnHapusItem = new JButton("Hapus Item");
		btnHapusItem.setBounds(125, 272, 105, 23);
		panel_1.add(btnHapusItem);
		btnHapusItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin menghapus data barang\n" + 
							 txtIdBarang.getText() + " - " + txtNamaBarang.getText() +
							 "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (FMain.konfir == JOptionPane.YES_OPTION){
					if (EksistensiDataBarangTunai() || EksistensiDataBarangKredit())
						JOptionPane.showMessageDialog(null, "Data barang tidak boleh dihapus!!!\nBarang digunakan dalam transaksi...");
					else{
						HapusDiskon(txtIdBarang.getText());
						HapusItem(txtIdBarang.getText());
					}	
				}
			}
		});
		btnHapusItem.setBackground(new Color(255, 255, 255));
		btnTambahItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FTambahBarang.main(null);
				window.frame.setEnabled(false);
			}
		});
	}
	
	private void TampilDataBarang(String IdBarang){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;

			query = "SELECT " +
					"tb_barang.id, " +
					"tb_barang.nama, " +
					"tb_barang.harga_beli, " +
					"tb_barang.id_supplier, " +
					"tb_detail_barang.satuan, " +
					"tb_supplier.nama AS nama_supplier " +
					"FROM " +
					"tb_barang " +
					"INNER JOIN tb_detail_barang ON tb_barang.id = tb_detail_barang.id_barang " +
					"INNER JOIN tb_supplier ON tb_barang.id_supplier = tb_supplier.id " +
					"GROUP BY tb_barang.id " +
					"HAVING " +
					"tb_barang.id = ?";

			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdBarang);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			while (rs.next()){
				txtIdBarang.setText(rs.getString("id"));
				txtNamaBarang.setText(rs.getString("nama"));
				txtHargaBeli.setText(FMain.FormatAngka(rs.getInt("harga_beli")));
				txtSatuanBeli.setText(rs.getString("satuan"));
				txtSupplier.setText(rs.getString("nama_supplier"));
			};	
			
			psQuery.close();
			rs.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void TampilDetailBarang(String IdBarang){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"Satuan",
					"Sisa Stok",
					"Harga Jual"
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
			
			String query=null;
			ResultSet rs;

			query = "SELECT * from tb_detail_barang where id_barang = ? order by harga_jual desc";

			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdBarang);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("satuan"),
						rs.getString("stok"),
						FMain.FormatAngka(rs.getInt("harga_jual"))
				});
			};	
			
			tblDetailBarang.setModel(model);
			tblDetailBarang.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblDetailBarang.getColumnModel().getColumn(0).setPreferredWidth(55);
			tblDetailBarang.getColumnModel().getColumn(1).setPreferredWidth(55);
			tblDetailBarang.getColumnModel().getColumn(2).setPreferredWidth(75);
			
			psQuery.close();
			rs.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void TampilData(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"Kode Barang",
					"Nama Barang"
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
			query = "select * from tb_barang";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("id"),
						rs.getString("nama")
				});
			};	
			
			table.setModel(model);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(200);
			
			st.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void HapusItem(String IdBarang){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "delete from tb_barang where id = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, IdBarang);
			psQuery.execute();
			
			query = "delete from tb_detail_barang where id_barang = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, IdBarang);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Barang dengan id " + IdBarang + " berhasil dihapus");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Hapus Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private void HapusSatuan(String IdBarang, String Satuan){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "delete from tb_detail_barang where id_barang = ? and satuan = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, IdBarang);
			psQuery.setString(2, Satuan);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Satuan berhasil dihapus");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Hapus Data Gagal");
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
	
			query = "SELECT id_barang from tb_detail_transaksi where id_barang = ?";
				
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,txtIdBarang.getText());
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
	
			query = "SELECT id_barang from tb_detail_transaksi_kredit where id_barang = ?";
				
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,txtIdBarang.getText());
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
	
	private void HapusDiskon(String IdBarang){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "delete from tb_diskon where id_barang = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, IdBarang);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
		} catch (Exception eee) {
			System.out.println("error hapus data diskon: " + eee);
		}
	}
	
	private void CariBarang(int kolom, String teks){
		int i=0, startIndex=0;
		if (table.getSelectedRow() != -1) startIndex = table.getSelectedRow()+1;
		for (i=startIndex; i<table.getRowCount(); i++){
			if (table.getValueAt(i, kolom).toString().matches(".*" + teks + ".*")){
				table.setRowSelectionInterval(i, i);
				table.scrollRectToVisible(new Rectangle(table.getCellRect(i, 0, true)));
				IdBarang = table.getValueAt(table.getSelectedRow(), 0).toString();
				TampilDataBarang(IdBarang);
				TampilDetailBarang(IdBarang);
				IdBarang = null;
				break;
			}
		}
		if (i == table.getRowCount()) {
			JOptionPane.showMessageDialog(null, "Proses cari data selesai...");
			table.scrollRectToVisible(new Rectangle(table.getCellRect(0, 0, true)));
			table.clearSelection();
			txtCari.selectAll();
			txtCari.requestFocus();
		}
		
	}
}
