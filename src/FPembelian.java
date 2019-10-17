import java.awt.EventQueue;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.jdesktop.swingx.JXDatePicker;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class FPembelian {

	private JFrame frame;
	static FPembelian window;
	private JTextField txtNoFaktur;
	private JTextField txtIdSupplier;
	private JTextField txtNama;
	static JTextField txtNamaBarang;
	static JTable table;
	static DefaultTableModel model = new DefaultTableModel(){
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
	static JTextField txtTotalBayar;
	private JButton btnHapus;
	private JButton btnSimpan;
	private JXDatePicker dpTglJatuhTempo;
	static int TotalBayar=0;
	private JTextField txtHarga;
	private JTextField txtQty;
	private JTextField txtSatuan;
	private JXDatePicker datePicker;
	private JCheckBox chckbxTglJatuhTempo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FPembelian();
					window.frame.setVisible(true);
					window.frame.setExtendedState(window.frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FPembelian() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Transaksi Pembelian");
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Buy.png"));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 1280, 768);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				AturTabel();
				Date date = new Date();
				datePicker.setDate(date);
				for(int i = table.getRowCount()-1; i>=0; i--){
					model.removeRow(i);
				}
				
				Calendar cal = GregorianCalendar.getInstance();
				cal.setTime(date);
				cal.add(GregorianCalendar.MONTH, 1);
				dpTglJatuhTempo.setDate(cal.getTime());
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
		
		JLabel lblNoFaktur = new JLabel("No. Faktur");
		lblNoFaktur.setBounds(37, 31, 80, 14);
		frame.getContentPane().add(lblNoFaktur);
		
		JLabel lblTanggal = new JLabel("Tgl Transaksi");
		lblTanggal.setBounds(37, 56, 80, 14);
		frame.getContentPane().add(lblTanggal);
		
		JLabel lblIdPelanggan = new JLabel("ID Supplier");
		lblIdPelanggan.setBounds(37, 81, 80, 14);
		frame.getContentPane().add(lblIdPelanggan);
		
		JLabel lblNama = new JLabel("Nama");
		lblNama.setBounds(37, 106, 80, 14);
		frame.getContentPane().add(lblNama);
		
		txtNoFaktur = new JTextField();
		txtNoFaktur.setBounds(127, 28, 218, 20);
		txtNoFaktur.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtNoFaktur.setFont(new Font("Tahoma", txtNoFaktur.getFont().getStyle(), 14));
		txtNoFaktur.setBackground(new Color(230, 230, 250));
		frame.getContentPane().add(txtNoFaktur);
		txtNoFaktur.setColumns(10);
		
		txtIdSupplier = new JTextField();
		txtIdSupplier.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					CariDataSupplier(txtIdSupplier.getText());
				}
			}
		});
		txtIdSupplier.setBounds(127, 78, 218, 20);
		txtIdSupplier.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtIdSupplier.setFont(new Font("Tahoma", txtNoFaktur.getFont().getStyle(), 14));
		txtIdSupplier.setBackground(new Color(230, 230, 250));
		frame.getContentPane().add(txtIdSupplier);
		txtIdSupplier.setColumns(10);
		
		txtNama = new JTextField();
		txtNama.setEditable(false);
		txtNama.setBounds(127, 103, 218, 20);
		txtNama.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtNama.setFont(new Font("Tahoma", txtNoFaktur.getFont().getStyle(), 14));
		txtNama.setBackground(new Color(230, 230, 250));
		frame.getContentPane().add(txtNama);
		txtNama.setColumns(10);
		
		btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!txtNama.getText().equals("")){
					if (!txtNoFaktur.getText().equals("")){
						if (table.getRowCount() > 0){
							SimpanData();
							AturTabel();
							txtNoFaktur.setText("");
							txtIdSupplier.setText("");
							txtNama.setText("");
							txtTotalBayar.setText("0");
							Date date = new Date();
							datePicker.setDate(date);
							for(int i = table.getRowCount()-1; i>=0; i--){
								model.removeRow(i);
							}
							
							Calendar cal = GregorianCalendar.getInstance();
							cal.setTime(date);
							cal.add(GregorianCalendar.MONTH, 1);
							dpTglJatuhTempo.setDate(cal.getTime());
							txtNoFaktur.requestFocus();
						}
						else
							JOptionPane.showMessageDialog(null, "Item barang belum diisi!!!");
					}
					else
						JOptionPane.showMessageDialog(null, "Isi No Faktur!!");
				}
				else
					JOptionPane.showMessageDialog(null, "Isi supplier!!");
				
				
				
			}
		});
		btnSimpan.setMnemonic(KeyEvent.VK_S);
		btnSimpan.setIcon(new ImageIcon(".\\icons\\Simpan.png"));
		btnSimpan.setBounds(138, 651, 91, 23);
		btnSimpan.setForeground(Color.BLACK);
		btnSimpan.setFont(new Font("Tahoma", btnSimpan.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		btnSimpan.setBackground(Color.LIGHT_GRAY);
		btnSimpan.setBorder(null);
		frame.getContentPane().add(btnSimpan);
		
		JButton btnTutup = new JButton("Tutup");
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
		btnTutup.setMnemonic(KeyEvent.VK_X);
		btnTutup.setIcon(new ImageIcon(".\\icons\\Tutup.png"));
		btnTutup.setBounds(1173, 709, 91, 23);
		btnTutup.setForeground(Color.BLACK);
		btnTutup.setFont(new Font("Tahoma", btnSimpan.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		btnTutup.setBackground(Color.LIGHT_GRAY);
		btnTutup.setBorder(null);
		frame.getContentPane().add(btnTutup);
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					txtSatuan.requestFocus();
				}
			}
		});
		txtNamaBarang.setBounds(37, 190, 218, 20);
		txtNamaBarang.setFont(new Font("Tahoma", txtNamaBarang.getFont().getStyle(), 14));
		txtNamaBarang.setBackground(new Color(230, 230, 250));
		txtNamaBarang.setBorder(new EmptyBorder(0, 0, 0, 0));
		frame.getContentPane().add(txtNamaBarang);
		txtNamaBarang.setColumns(10);
		
		txtSatuan = new JTextField();
		txtSatuan.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					txtQty.requestFocus();
				}
			}
		});
		txtSatuan.setColumns(10);
		txtSatuan.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtSatuan.setBackground(new Color(230, 230, 250));
		txtSatuan.setBounds(265, 190, 218, 20);
		frame.getContentPane().add(txtSatuan);
		
		txtQty = new JTextField();
		txtQty.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					txtHarga.requestFocus();
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
		txtQty.setColumns(10);
		txtQty.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtQty.setBackground(new Color(230, 230, 250));
		txtQty.setBounds(493, 190, 80, 20);
		frame.getContentPane().add(txtQty);
		
		txtHarga = new JTextField();
		txtHarga.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					if (
							txtNamaBarang.getText().equals("") ||
							txtSatuan.getText().equals("") ||
							txtHarga.getText().equals("") ||
							txtQty.getText().equals("")
						){
						JOptionPane.showMessageDialog(null, "Data belum lengkap!!!");
						txtNamaBarang.requestFocus();
					}
					else{
						TambahData();
						int total = 0,x=0;
						for (x=0; x<table.getRowCount(); x++){
							total = total + Integer.valueOf(table.getValueAt(x, 4).toString().replace(",", ""));
						}
						txtNamaBarang.setText("");
						txtSatuan.setText("");
						txtHarga.setText("");
						txtQty.setText("");
						txtTotalBayar.setText(FMain.FormatAngka(total));
						txtNamaBarang.requestFocus();
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
		txtHarga.setColumns(10);
		txtHarga.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtHarga.setBackground(new Color(230, 230, 250));
		txtHarga.setBounds(583, 190, 218, 20);
		frame.getContentPane().add(txtHarga);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(36, 221, 1228, 411);
		scrollPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setBackground(Color.WHITE);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setGridColor(Color.GRAY);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setBackground(Color.WHITE);
		table.setFont(new Font("Tahoma", table.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 18));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		
		JLabel lblTotalBayar = new JLabel("Total");
		lblTotalBayar.setFont(new Font("Tahoma", lblTotalBayar.getFont().getStyle(), 20));
		lblTotalBayar.setBounds(1010, 643, 56, 34);
		frame.getContentPane().add(lblTotalBayar);
		
		txtTotalBayar = new JTextField();
		txtTotalBayar.setEditable(false);
		txtTotalBayar.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTotalBayar.setBounds(1076, 642, 188, 37);
		txtTotalBayar.setFont(new Font("Tahoma", txtTotalBayar.getFont().getStyle() & ~Font.ITALIC, 20));
		txtTotalBayar.setBackground(new Color(230, 230, 250));
		txtTotalBayar.setBorder(new EmptyBorder(0, 0, 0, 0));
		frame.getContentPane().add(txtTotalBayar);
		txtTotalBayar.setColumns(10);
		
		btnHapus = new JButton("Hapus");
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (table.getRowCount() > 0)
					HapusBarisTabel();
				else
					JOptionPane.showMessageDialog(btnHapus, "Data barang belum ada!!!");
			}
		});
		btnHapus.setIcon(new ImageIcon(".\\icons\\Hapus.png"));
		btnHapus.setBounds(37, 651, 91, 23);
		btnHapus.setForeground(Color.BLACK);
		btnHapus.setFont(new Font("Tahoma", btnSimpan.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		btnHapus.setBackground(Color.LIGHT_GRAY);
		btnHapus.setBorder(null);
		frame.getContentPane().add(btnHapus);
		
		dpTglJatuhTempo = new JXDatePicker();
		dpTglJatuhTempo.setVisible(false);
		dpTglJatuhTempo.setFormats(new String[] {"dd - MM - yyyy"});
		dpTglJatuhTempo.setBounds(501, 27, 188, 22);
		frame.getContentPane().add(dpTglJatuhTempo);
		
		datePicker = new JXDatePicker();
		datePicker.setFormats(new String[] {"dd - MM - yyyy"});
		datePicker.setBounds(127, 52, 218, 22);
		frame.getContentPane().add(datePicker);
		
		JLabel lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setBounds(37, 165, 80, 14);
		frame.getContentPane().add(lblNamaBarang);
		
		JLabel lblHarga = new JLabel("Harga per satuan");
		lblHarga.setBounds(583, 165, 106, 14);
		frame.getContentPane().add(lblHarga);
		
		JLabel lblQty = new JLabel("Qty");
		lblQty.setBounds(493, 165, 80, 14);
		frame.getContentPane().add(lblQty);
		
		JLabel lblSatuan = new JLabel("Satuan");
		lblSatuan.setBounds(265, 165, 80, 14);
		frame.getContentPane().add(lblSatuan);
		
		JButton btnTambah = new JButton("+");
		btnTambah.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (
						txtNamaBarang.getText().equals("") ||
						txtSatuan.getText().equals("") ||
						txtHarga.getText().equals("") ||
						txtQty.getText().equals("")
					){
					JOptionPane.showMessageDialog(null, "Data belum lengkap!!!");
					txtNamaBarang.requestFocus();
				}
				else{
					TambahData();
					int total = 0,x=0;
					for (x=0; x<table.getRowCount(); x++){
						total = total + Integer.valueOf(table.getValueAt(x, 4).toString().replace(",", ""));
					}
					txtNamaBarang.setText("");
					txtSatuan.setText("");
					txtHarga.setText("");
					txtQty.setText("");
					txtTotalBayar.setText(FMain.FormatAngka(total));
					txtNamaBarang.requestFocus();
				}
			}
		});
		btnTambah.setForeground(Color.BLACK);
		btnTambah.setBorder(null);
		btnTambah.setBackground(Color.LIGHT_GRAY);
		btnTambah.setBounds(815, 190, 23, 23);
		frame.getContentPane().add(btnTambah);
		
		chckbxTglJatuhTempo = new JCheckBox("Tgl Jatuh Tempo");
		chckbxTglJatuhTempo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (chckbxTglJatuhTempo.isSelected())
					dpTglJatuhTempo.setVisible(true);
				else
					dpTglJatuhTempo.setVisible(false);
			}
		});
		chckbxTglJatuhTempo.setBackground(Color.WHITE);
		chckbxTglJatuhTempo.setBounds(376, 27, 124, 23);
		frame.getContentPane().add(chckbxTglJatuhTempo);
	}
	
	private void AturTabel(){
		
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		String[] columnNames = {"Nama Barang", "Satuan", "Harga", "Qty", "Sub Total" };
		
		model.setColumnIdentifiers(columnNames);
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setPreferredWidth(75);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setPreferredWidth(200);
		table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		table.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
	}
	
	private void TambahData(){
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		int total = Integer.valueOf(txtQty.getText()) * Integer.valueOf(txtHarga.getText());
		String[] columnNames = {"Nama Barang", "Satuan", "Harga", "Qty", "Sub Total" };
		model.setColumnIdentifiers(columnNames);
		
		model.addRow(new Object[] {
				txtNamaBarang.getText(),
				txtSatuan.getText(),
				FMain.FormatAngka(Integer.valueOf(txtHarga.getText())),
				txtQty.getText(),
				FMain.FormatAngka(total)
		});
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setPreferredWidth(75);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setPreferredWidth(200);
		table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
	}
	
	private void HapusBarisTabel(){
		int a = Integer.valueOf(txtTotalBayar.getText().replace(",", ""));
		int b = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 4).toString().replace(",", ""));
		TotalBayar =  a-b ;
		txtTotalBayar.setText(FMain.FormatAngka(TotalBayar));
		model.removeRow(table.getSelectedRow());
	}
	
	private void CariDataSupplier(String IdSupplier){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			
			query = "select * from tb_supplier where id = ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdSupplier);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			
			if (rowcount > 0){
				while (rs.next()) {
					txtNama.setText(rs.getString("nama"));
				}
				txtNamaBarang.requestFocus();
			}
			else {
				JOptionPane.showMessageDialog(null, "ID Supplier tidak ditemukan!!!");
				txtIdSupplier.requestFocus();
			}
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cari datanya supplier " + e);}
	}
	
	private void SimpanData(){
		if (chckbxTglJatuhTempo.isSelected())
			SimpanDataKredit();
		else
			SimpanDataTunai();
	}
	
	private void SimpanDataTunai(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			PreparedStatement psQuery=null;
			for (byte i=0; i<table.getRowCount(); i++){
				String query = "insert into tb_pembelian values (?,?,?,?,?,?,?,?)";
				psQuery = conn.prepareStatement(query);
				psQuery.setString(1, txtNoFaktur.getText());
				psQuery.setString(2, table.getValueAt(i, 0).toString());
				psQuery.setString(3, table.getValueAt(i, 1).toString());
				psQuery.setString(4, table.getValueAt(i, 2).toString().replace(",", ""));
				psQuery.setString(5, table.getValueAt(i, 3).toString());
				psQuery.setString(6, txtNama.getText());
				psQuery.setString(7, dateFormat.format(datePicker.getDate()));
				psQuery.setString(8, dateFormat.format(dpTglJatuhTempo.getDate()));
				
				psQuery.execute();
			}
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan detail transaksi " + e);}
	}
	
	private void SimpanDataKredit(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			PreparedStatement psQuery=null;
			for (byte i=0; i<table.getRowCount(); i++){
				String query = "insert into tb_pembelian (no_faktur,nama_barang,satuan,harga,qty,supplier,tanggal) values (?,?,?,?,?,?,?)";
				psQuery = conn.prepareStatement(query);
				psQuery.setString(1, txtNoFaktur.getText());
				psQuery.setString(2, table.getValueAt(i, 0).toString());
				psQuery.setString(3, table.getValueAt(i, 1).toString());
				psQuery.setString(4, table.getValueAt(i, 2).toString().replace(",", ""));
				psQuery.setString(5, table.getValueAt(i, 3).toString());
				psQuery.setString(6, txtNama.getText());
				psQuery.setString(7, dateFormat.format(datePicker.getDate()));
				
				psQuery.execute();
			}
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan detail transaksi " + e);}
	}
}
