import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ListSelectionModel;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class FPembayaran {

	JFrame frame;
	static FPembayaran window;
	private JTextField textField;
	private static JTable table;
	private static JTextField txtNoFaktur;
	private static JTextField txtTglTransaksi;
	private static JTextField txtTotalKredit;
	private static JTextField txtIdPelanggan;
	private static JTextField txtNamaPelanggan;
	private static JTextField txtNamaToko;
	private static JTextField txtSisaHutang;
	private static JTextField txtTglJatuhTempo;
	private static JTextField txtNamaKaryawan;
	private static JTable tblBarang;
	private static JTable tblDetailBayar;
	private int IndeksTabel=-1;
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private Date date = new Date();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FPembayaran();
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
	public FPembayaran() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Pembayaran Transaksi Kredit");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				TampilTransaksiKredit();
				try {
					table.setRowSelectionInterval(IndeksTabel, IndeksTabel);
					textField.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
					TampilDetailTransaksiKredit(table.getValueAt(table.getSelectedRow(), 0).toString());
					TampilTabelBarang(table.getValueAt(table.getSelectedRow(), 0).toString());
					TampilDetailBayar(table.getValueAt(table.getSelectedRow(), 0).toString());
				} catch (Exception e1) {
					System.out.println(e1);
				}
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
		frame.setBounds(100, 100, 804, 621);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Cari No. Faktur Transaksi Kredit", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 458, 255);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 55, 438, 189);
		panel.add(scrollPane);
		
		table = new JTable();
		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				IndeksTabel = table.getSelectedRow();
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					textField.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
					TampilDetailTransaksiKredit(table.getValueAt(table.getSelectedRow(), 0).toString());
					TampilTabelBarang(table.getValueAt(table.getSelectedRow(), 0).toString());
					TampilDetailBayar(table.getValueAt(table.getSelectedRow(), 0).toString());
				} catch (Exception e1) {
					System.out.println(e1);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2 && !arg0.isConsumed()){
					if (txtSisaHutang.getText().replace(",", "").equals("0"))
						JOptionPane.showMessageDialog(null, "Faktur sudah lunas!");
					else{
						FBayar.main(null);
						FBayar.NoFaktur = txtNoFaktur.getText();
						FBayar.SisaHutang = txtSisaHutang.getText().replace(",", "");
						window.frame.setEnabled(false);
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				textField.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
				TampilDetailTransaksiKredit(table.getValueAt(table.getSelectedRow(), 0).toString());
				TampilTabelBarang(table.getValueAt(table.getSelectedRow(), 0).toString());
				TampilDetailBayar(table.getValueAt(table.getSelectedRow(), 0).toString());
			}
		});
		scrollPane.setViewportView(table);
		
		textField = new JTextField();
		textField.setToolTipText("Masukkan kata kunci lalu tekan ENTER untuk mencari data");
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					CariData(textField.getText());
				}
			}
		});
		textField.setBounds(71, 24, 205, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnBayar = new JButton("Bayar");
		btnBayar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() < 0)
					JOptionPane.showMessageDialog(null, "No Faktur belum dipilih!");
				else{
					if (txtSisaHutang.getText().replace(",", "").equals("0"))
						JOptionPane.showMessageDialog(null, "Faktur sudah lunas!");
					else{
						FBayar.main(null);
						FBayar.NoFaktur = txtNoFaktur.getText();
						FBayar.SisaHutang = txtSisaHutang.getText().replace(",", "");
						window.frame.setEnabled(false);
					}
				}
			}
		});
		btnBayar.setBounds(359, 23, 89, 23);
		panel.add(btnBayar);
		
		JLabel lblNoFaktur = new JLabel("No Faktur");
		lblNoFaktur.setBounds(10, 27, 79, 14);
		panel.add(lblNoFaktur);
		
		JButton btnTutup = new JButton("Tutup");
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
		btnTutup.setBounds(697, 559, 89, 23);
		frame.getContentPane().add(btnTutup);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Detail Transaksi Kredit", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 277, 776, 267);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel label1 = new JLabel("No. Faktur");
		label1.setFont(new Font("Tahoma", label1.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, label1.getFont().getSize()));
		label1.setBounds(10, 27, 100, 14);
		panel_1.add(label1);
		
		JLabel label2 = new JLabel("Tanggal Transaksi");
		label2.setFont(new Font("Tahoma", label2.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, label2.getFont().getSize()));
		label2.setBounds(10, 52, 100, 14);
		panel_1.add(label2);
		
		JLabel label3 = new JLabel("Total Kredit");
		label3.setFont(new Font("Tahoma", label2.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, label2.getFont().getSize()));
		label3.setBounds(10, 77, 100, 14);
		panel_1.add(label3);
		
		JLabel label4 = new JLabel("ID Pelanggan");
		label4.setFont(new Font("Tahoma", label2.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, label2.getFont().getSize()));
		label4.setBounds(10, 102, 100, 14);
		panel_1.add(label4);
		
		JLabel label5 = new JLabel("Nama Pelanggan");
		label5.setFont(new Font("Tahoma", label2.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, label2.getFont().getSize()));
		label5.setBounds(10, 127, 100, 14);
		panel_1.add(label5);
		
		JLabel label6 = new JLabel("Nama Toko");
		label6.setFont(new Font("Tahoma", label2.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, label2.getFont().getSize()));
		label6.setBounds(10, 152, 100, 14);
		panel_1.add(label6);
		
		JLabel label7 = new JLabel("Sisa Hutang");
		label7.setFont(new Font("Tahoma", label2.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, label2.getFont().getSize()));
		label7.setBounds(10, 177, 100, 14);
		panel_1.add(label7);
		
		JLabel label8 = new JLabel("Tgl Jatuh Tempo");
		label8.setFont(new Font("Tahoma", label2.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, label2.getFont().getSize()));
		label8.setBounds(10, 202, 100, 14);
		panel_1.add(label8);
		
		JLabel label9 = new JLabel("Nama Karyawan");
		label9.setFont(new Font("Tahoma", label2.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, label2.getFont().getSize()));
		label9.setBounds(10, 227, 100, 14);
		panel_1.add(label9);
		
		txtNoFaktur = new JTextField();
		txtNoFaktur.setBackground(Color.WHITE);
		txtNoFaktur.setEditable(false);
		txtNoFaktur.setBounds(120, 24, 152, 20);
		panel_1.add(txtNoFaktur);
		txtNoFaktur.setColumns(10);
		
		txtTglTransaksi = new JTextField();
		txtTglTransaksi.setBackground(Color.WHITE);
		txtTglTransaksi.setEditable(false);
		txtTglTransaksi.setBounds(120, 49, 152, 20);
		panel_1.add(txtTglTransaksi);
		txtTglTransaksi.setColumns(10);
		
		txtTotalKredit = new JTextField();
		txtTotalKredit.setBackground(Color.WHITE);
		txtTotalKredit.setEditable(false);
		txtTotalKredit.setBounds(120, 74, 152, 20);
		panel_1.add(txtTotalKredit);
		txtTotalKredit.setColumns(10);
		
		txtIdPelanggan = new JTextField();
		txtIdPelanggan.setBackground(Color.WHITE);
		txtIdPelanggan.setEditable(false);
		txtIdPelanggan.setBounds(120, 99, 152, 20);
		panel_1.add(txtIdPelanggan);
		txtIdPelanggan.setColumns(10);
		
		txtNamaPelanggan = new JTextField();
		txtNamaPelanggan.setBackground(Color.WHITE);
		txtNamaPelanggan.setEditable(false);
		txtNamaPelanggan.setBounds(120, 124, 152, 20);
		panel_1.add(txtNamaPelanggan);
		txtNamaPelanggan.setColumns(10);
		
		txtNamaToko = new JTextField();
		txtNamaToko.setBackground(Color.WHITE);
		txtNamaToko.setEditable(false);
		txtNamaToko.setBounds(120, 149, 152, 20);
		panel_1.add(txtNamaToko);
		txtNamaToko.setColumns(10);
		
		txtSisaHutang = new JTextField();
		txtSisaHutang.setBackground(Color.WHITE);
		txtSisaHutang.setEditable(false);
		txtSisaHutang.setBounds(120, 174, 152, 20);
		panel_1.add(txtSisaHutang);
		txtSisaHutang.setColumns(10);
		
		txtTglJatuhTempo = new JTextField();
		txtTglJatuhTempo.setBackground(Color.WHITE);
		txtTglJatuhTempo.setEditable(false);
		txtTglJatuhTempo.setBounds(120, 199, 152, 20);
		panel_1.add(txtTglJatuhTempo);
		txtTglJatuhTempo.setColumns(10);
		
		txtNamaKaryawan = new JTextField();
		txtNamaKaryawan.setBackground(Color.WHITE);
		txtNamaKaryawan.setEditable(false);
		txtNamaKaryawan.setBounds(120, 224, 152, 20);
		panel_1.add(txtNamaKaryawan);
		txtNamaKaryawan.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Detail Barang", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(282, 11, 484, 245);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 21, 464, 213);
		panel_2.add(scrollPane_1);
		
		tblBarang = new JTable();
		tblBarang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(tblBarang);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Detail Pembayaran", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(478, 11, 308, 255);
		frame.getContentPane().add(panel_3);
		panel_3.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 24, 288, 220);
		panel_3.add(scrollPane_2);
		
		tblDetailBayar = new JTable();
		tblDetailBayar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_2.setViewportView(tblDetailBayar);
		
		JButton btnCetakLapPiutang = new JButton("Cetak Lap. Piutang");
		btnCetakLapPiutang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CetakLaporan();
			}
		});
		btnCetakLapPiutang.setBounds(429, 559, 136, 23);
		frame.getContentPane().add(btnCetakLapPiutang);
		
		JButton btnExportExcel = new JButton("Export Excel");
		btnExportExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExportExcel();
			}
		});
		btnExportExcel.setBounds(575, 559, 112, 23);
		frame.getContentPane().add(btnExportExcel);
	}
	
	private static void TampilTransaksiKredit(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"No. Faktur",
					"Sisa Hutang",
					"Nama Toko",
					"Tgl Jatuh Tempo"
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
			query = "SELECT " +
					"tb_transaksi_kredit.no_faktur, " +
					"tb_hutang.sisa_hutang, " +
					"tb_pelanggan.nama_toko, " +
					"date_format(tb_transaksi_kredit.tanggal_jatuh_tempo, '%d-%m-%Y') as tanggal_jatuh_tempo  " +
					"FROM " +
					"tb_transaksi_kredit " +
					"INNER JOIN tb_hutang ON tb_transaksi_kredit.no_faktur = tb_hutang.no_faktur " +
					"INNER JOIN tb_pelanggan ON tb_transaksi_kredit.id_pelanggan = tb_pelanggan.id";
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("no_faktur"),
						FMain.FormatAngka(rs.getInt("sisa_hutang")),
						rs.getString("nama_toko"),
						rs.getString("tanggal_jatuh_tempo")
				});
			};	
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			
			
			table.setModel(model);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
			
			table.getColumnModel().getColumn(3).setPreferredWidth(100);
			table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
			
			st.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
	}
	
	private static void TampilDetailTransaksiKredit(String NoFaktur){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT " +
					"tb_transaksi_kredit.no_faktur, " +
					"date_format(tb_transaksi_kredit.tanggal, '%d-%m-%Y') as tanggal, " +
					"tb_transaksi_kredit.total_bayar, " +
					"tb_transaksi_kredit.id_pelanggan, " +
					"tb_pelanggan.nama, " +
					"tb_pelanggan.nama_toko, " +
					"tb_hutang.sisa_hutang, " +
					"date_format(tb_transaksi_kredit.tanggal_jatuh_tempo, '%d-%m-%Y') as tanggal_jatuh_tempo, " +
					"tb_karyawan.nama as nama_karyawan " +
					"FROM " +
					"tb_transaksi_kredit " +
					"INNER JOIN tb_pelanggan ON tb_transaksi_kredit.id_pelanggan = tb_pelanggan.id " +
					"INNER JOIN tb_hutang ON tb_transaksi_kredit.no_faktur = tb_hutang.no_faktur " +
					"INNER JOIN tb_karyawan ON tb_transaksi_kredit.id_karyawan = tb_karyawan.id " +
					"where tb_transaksi_kredit.no_faktur = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,NoFaktur);
			psQuery.execute();
			rs = psQuery.getResultSet();
			while (rs.next()){
				txtNoFaktur.setText(rs.getString("no_faktur"));
				txtTglTransaksi.setText(rs.getString("tanggal"));
				txtTotalKredit.setText(FMain.FormatAngka(rs.getInt("total_bayar")));
				txtIdPelanggan.setText(rs.getString("id_pelanggan"));
				txtNamaPelanggan.setText(rs.getString("nama"));
				txtNamaToko.setText(rs.getString("nama_toko"));
				txtSisaHutang.setText(FMain.FormatAngka(rs.getInt("sisa_hutang")));
				txtTglJatuhTempo.setText(rs.getString("tanggal_jatuh_tempo"));
				txtNamaKaryawan.setText(rs.getString("nama_karyawan"));
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
	}
	
	private static void TampilTabelBarang(String NoFaktur){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"ID Barang",
					"Nama Barang",
					"Harga Jual",
					"Satuan",
					"Qty"
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
	
			query = "SELECT " +
					"tb_detail_transaksi_kredit.id_barang, " +
					"tb_barang.nama, " +
					"tb_detail_transaksi_kredit.harga_jual - (tb_detail_transaksi_kredit.harga_jual*(tb_detail_transaksi_kredit.diskon/100)) as hrg, " +
					"tb_detail_transaksi_kredit.satuan, " +
					"tb_detail_transaksi_kredit.qty " +
					"FROM " +
					"tb_detail_transaksi_kredit " +
					"INNER JOIN tb_barang ON tb_detail_transaksi_kredit.id_barang = tb_barang.id " +
					"WHERE " +
					"tb_detail_transaksi_kredit.no_faktur = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,NoFaktur);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("id_barang"),
						rs.getString("nama"),
						FMain.FormatAngka(rs.getInt("hrg")),
						rs.getString("satuan"),
						rs.getString("qty")
				});
			};	
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			
			tblBarang.setModel(model);
			tblBarang.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblBarang.getColumnModel().getColumn(0).setPreferredWidth(100);
			tblBarang.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			
			tblBarang.getColumnModel().getColumn(1).setPreferredWidth(100);
			tblBarang.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			
			tblBarang.getColumnModel().getColumn(2).setPreferredWidth(100);
			tblBarang.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
			
			tblBarang.getColumnModel().getColumn(3).setPreferredWidth(100);
			tblBarang.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
			
			tblBarang.getColumnModel().getColumn(4).setPreferredWidth(50);
			tblBarang.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil tabel barang: " + e);
		}
	}
	
	private static void TampilDetailBayar(String NoFaktur){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"No.",
					"Tgl Bayar",
					"Jumlah Bayar"
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
	
			query = "SELECT "
					+ "id,"
					+ "no_faktur,"
					+ "date_format(tgl_bayar, '%d-%m-%Y') as tgl_bayar,"
					+ "jml_bayar "
					+ "from tb_detail_hutang where no_faktur = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,NoFaktur);
			psQuery.execute();
			rs = psQuery.getResultSet();
			byte i=0;
			while (rs.next()){
				i++;
				model.addRow(new Object[] {
						i,
						rs.getString("tgl_bayar"),
						FMain.FormatAngka(rs.getInt("jml_bayar"))
				});
			};	
			
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			
			tblDetailBayar.setModel(model);
			tblDetailBayar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblDetailBayar.getColumnModel().getColumn(0).setPreferredWidth(30);
			tblDetailBayar.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			
			tblDetailBayar.getColumnModel().getColumn(1).setPreferredWidth(100);
			tblDetailBayar.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			
			tblDetailBayar.getColumnModel().getColumn(2).setPreferredWidth(100);
			tblDetailBayar.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil tabel barang: " + e);
		}
	}
	
	private void CetakLaporan(){
		try{
			System.out.println(dateFormat.format(date).toString());
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			Map<String, Object> parametersMap = new HashMap<String, Object>();
			
			String reportName = ".\\Laporan\\LaporanPiutang.jasper";
	        parametersMap.put(
	        		"paramTglCetak",
	        		""
	        );
	        
	        JasperPrint jp = JasperFillManager.fillReport(reportName, parametersMap, conn);
        	JRViewer jv = new JRViewer(jp);
        	final JFrame jf = new JFrame();
        	jf.getContentPane().add(jv);
        	jf.validate();
        	jf.setVisible(true);
        	jf.setExtendedState(jf.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        	jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);        	
        	conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cetak laporan" + e);}
	}
	
	private void ExportExcel(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			Map<String, Object> parametersMap = new HashMap<String, Object>();
			
			String reportName = ".\\Laporan\\LaporanPiutang.jasper";
	        parametersMap.put("paramTglCetak", "");
	        
	        JasperPrint jp = JasperFillManager.fillReport(reportName, parametersMap, conn);
	        
	        JRXlsExporter xlsExporter = new JRXlsExporter();
	        xlsExporter.setExporterInput(new SimpleExporterInput(jp));
	        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("user.home") + 
	        		"\\Desktop\\LaporanPiutang" + 
	        		dateFormat.format(date).toString().replace("/", "-") + 
	        		".xls"));
	        SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
	        xlsReportConfiguration.setOnePagePerSheet(false);
	        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
	        xlsReportConfiguration.setDetectCellType(false);
	        xlsReportConfiguration.setWhitePageBackground(false);
	        xlsExporter.setConfiguration(xlsReportConfiguration);
	        
	        xlsExporter.exportReport();
	               	
        	conn.close();
        	JOptionPane.showMessageDialog(null, "Faktur berhasil di-export ke file Ms. Excel\n" +
        			"File tersimpan di Desktop\n" +
        			"dengan nama file LaporanPiutang" + 
	        		dateFormat.format(date).toString().replace("/", "-") + 
	        		".xls");
			
		}
		catch(Exception e){System.out.println("Ada error bro cetak laporan" + e);}
	}
	
	private void CariData(String teks){
		int i=0, startIndex=0;
		if (table.getSelectedRow() != -1) startIndex = table.getSelectedRow()+1;
		for (i=startIndex; i<table.getRowCount(); i++){
			if (table.getValueAt(i, 0).toString().matches(".*" + teks + ".*")){
				table.setRowSelectionInterval(i, i);
				table.scrollRectToVisible(new Rectangle(table.getCellRect(i, 0, true)));
				TampilDetailTransaksiKredit(table.getValueAt(table.getSelectedRow(), 0).toString());
				TampilTabelBarang(table.getValueAt(table.getSelectedRow(), 0).toString());
				TampilDetailBayar(table.getValueAt(table.getSelectedRow(), 0).toString());
				break;
			}
		}
		if (i == table.getRowCount()) {
			JOptionPane.showMessageDialog(null, "Proses cari data selesai...");
			table.scrollRectToVisible(new Rectangle(table.getCellRect(0, 0, true)));
			table.clearSelection();
			textField.selectAll();
			textField.requestFocus();
		}
		
	}
}
