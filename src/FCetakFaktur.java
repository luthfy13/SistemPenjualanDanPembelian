import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
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
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ListSelectionModel;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class FCetakFaktur {

	JFrame frame;
	static FCetakFaktur window;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField txtNoFaktur;
	private JTable table;
	static JRadioButton rdbtnTransaksiTunai;
	static JRadioButton rdbtnTransaksiKredit;
	private JTable tblDetailTransaksi;
	private JButton btnEditTglJatuh;
	private int IndeksTabelTransaksi=-1,IndeksTabelTransaksiKredit=-1;
	private int selisih=0, harga=0, Total=0,  potongan=0;
	private JButton btnKeKredit;
	private JButton btnKeTunai;
	private DefaultTableModel modelDetailTransaksi = new DefaultTableModel(){
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
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FCetakFaktur();
					window.frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("Ada error form!!!: " + e);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FCetakFaktur() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				if (rdbtnTransaksiTunai.isSelected()){
					modelDetailTransaksi.setRowCount(0);
					TampilTransaksiTunai();
					table.requestFocus();
				}
				else if (rdbtnTransaksiKredit.isSelected()){
					modelDetailTransaksi.setRowCount(0);
					TampilTransaksiKredit();
					table.requestFocus();
				}
				try {
					table.setRowSelectionInterval(IndeksTabelTransaksi, IndeksTabelTransaksi);
					txtNoFaktur.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
					TampilTabelBarang(txtNoFaktur.getText());
					tblDetailTransaksi.setRowSelectionInterval(IndeksTabelTransaksiKredit, IndeksTabelTransaksiKredit);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				txtNoFaktur = null;
				table = null;
				rdbtnTransaksiTunai = null;
				rdbtnTransaksiKredit = null;
				tblDetailTransaksi = null;
				btnEditTglJatuh = null;
				FMain.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Laporan.png"));
		frame.setTitle("Cetak Faktur");
		frame.setResizable(false);
		frame.setBounds(100, 100, 1024, 705);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Jenis Transaksi", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 125, 76);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		rdbtnTransaksiTunai = new JRadioButton("Transaksi Tunai");
		rdbtnTransaksiTunai.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				btnEditTglJatuh.setEnabled(false);
				btnKeKredit.setEnabled(true);
				btnKeTunai.setEnabled(false);
				txtNoFaktur.setText("");
				modelDetailTransaksi.setRowCount(0);
				TampilTransaksiTunai();
				table.requestFocus();
			}
		});
		buttonGroup.add(rdbtnTransaksiTunai);
		rdbtnTransaksiTunai.setBounds(6, 18, 115, 23);
		panel.add(rdbtnTransaksiTunai);
		
		rdbtnTransaksiKredit = new JRadioButton("Transaksi Kredit");
		rdbtnTransaksiKredit.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				btnEditTglJatuh.setEnabled(true);
				btnKeKredit.setEnabled(false);
				btnKeTunai.setEnabled(true);
				txtNoFaktur.setText("");
				modelDetailTransaksi.setRowCount(0);
				TampilTransaksiKredit();
				table.requestFocus();
			}
		});
		buttonGroup.add(rdbtnTransaksiKredit);
		rdbtnTransaksiKredit.setBounds(6, 44, 115, 23);
		panel.add(rdbtnTransaksiKredit);
		
		JButton btnTutup = new JButton("Tutup");
		btnTutup.setMnemonic(KeyEvent.VK_X);
		btnTutup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtNoFaktur = null;
				table = null;
				rdbtnTransaksiTunai = null;
				rdbtnTransaksiKredit = null;
				tblDetailTransaksi = null;
				btnEditTglJatuh = null;
				FMain.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnTutup.setBounds(924, 642, 89, 23);
		frame.getContentPane().add(btnTutup);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 98, 518, 502);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					IndeksTabelTransaksi = table.getSelectedRow();
				} catch (Exception e1) {
					e1 = null;
					System.gc();
				}
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				try {
					txtNoFaktur.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
					TampilTabelBarang(txtNoFaktur.getText());
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				txtNoFaktur.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
				TampilTabelBarang(txtNoFaktur.getText());
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2 && !arg0.isConsumed()){
					if (rdbtnTransaksiKredit.isSelected()){
						FEditJatuhTempo.main(null);
						FEditJatuhTempo.NoFaktur = txtNoFaktur.getText();
						window.frame.setEnabled(false);
					}
				}
			}
		});
		scrollPane.setViewportView(table);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(538, 98, 475, 536);
		frame.getContentPane().add(scrollPane_1);
		
		tblDetailTransaksi = new JTable();
		tblDetailTransaksi.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				IndeksTabelTransaksiKredit = tblDetailTransaksi.getSelectedRow();
			}
		});
		tblDetailTransaksi.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2 && !arg0.isConsumed()){
					FEditHargaTransaksi.main(null);
					FEditHargaTransaksi.NoFaktur = txtNoFaktur.getText();
					FEditHargaTransaksi.IdBarang = tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 0).toString();
					FEditHargaTransaksi.NamaBarang = tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 1).toString();
					FEditHargaTransaksi.harga_lama = Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 2).toString().replace(",", ""));
					FEditHargaTransaksi.Qty = Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 4).toString());
					window.frame.setEnabled(false);
				}
			}
		});
		scrollPane_1.setViewportView(tblDetailTransaksi);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Tampil Data", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(145, 11, 489, 76);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNoFaktur = new JLabel("No. Faktur");
		lblNoFaktur.setBounds(10, 17, 70, 14);
		panel_1.add(lblNoFaktur);
		
		txtNoFaktur = new JTextField();
		txtNoFaktur.setBounds(10, 43, 165, 20);
		panel_1.add(txtNoFaktur);
		txtNoFaktur.setColumns(10);
		
		JButton btnTampilDetailTransaksi = new JButton("Tampil Detail Transaksi");
		btnTampilDetailTransaksi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txtNoFaktur.getText().equals("")){
					JOptionPane.showMessageDialog(null, "No Faktur tidak boleh kosong!!!");
				}
				else{
					if (!((rdbtnTransaksiKredit.isSelected()) || (rdbtnTransaksiTunai.isSelected()))){
						JOptionPane.showMessageDialog(null, "Pilih jenis transaksi!!!");
					}
					else{
						CariData(txtNoFaktur.getText());
					}
				}
				
				
			}
		});
		btnTampilDetailTransaksi.setBounds(185, 13, 146, 23);
		panel_1.add(btnTampilDetailTransaksi);
		
		btnEditTglJatuh = new JButton("Edit Tgl Jatuh Tempo");
		btnEditTglJatuh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtNoFaktur.getText().equals("")){
					JOptionPane.showMessageDialog(null, "No. Faktur tidak boleh kosong!!!");
					txtNoFaktur.requestFocus();
				}
				else{
					FEditJatuhTempo.main(null);
					FEditJatuhTempo.NoFaktur = txtNoFaktur.getText();
					window.frame.setEnabled(false);
				}
			}
		});
		btnEditTglJatuh.setEnabled(false);
		btnEditTglJatuh.setBounds(185, 42, 146, 23);
		panel_1.add(btnEditTglJatuh);
		
		JButton btnCetak = new JButton("Cetak");
		btnCetak.setBounds(341, 13, 136, 23);
		panel_1.add(btnCetak);
		
		JButton btnExportExcel = new JButton("Export Excel");
		btnExportExcel.setBounds(341, 42, 136, 23);
		panel_1.add(btnExportExcel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Edit Data Transaksi", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(644, 11, 369, 76);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnEditQty = new JButton("Edit Qty");
		btnEditQty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tblDetailTransaksi.getSelectedRow() >= 0){
					FEditQtyTransaksi.main(null);
					FEditQtyTransaksi.NoFaktur = txtNoFaktur.getText();
					FEditQtyTransaksi.IdBarang = tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 0).toString();
					FEditQtyTransaksi.NamaBarang = tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 1).toString();
					FEditQtyTransaksi.Qty = Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 4).toString());
					FEditQtyTransaksi.HargaSatuan = Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 2).toString().replace(",", ""));
					FEditQtyTransaksi.Satuan = tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 3).toString();
					FEditQtyTransaksi.Diskon = Float.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 6).toString().replace("%", ""));
					window.frame.setEnabled(false);
				}
			}
		});
		btnEditQty.setBounds(249, 13, 110, 23);
		panel_2.add(btnEditQty);
		
		JButton btnEditHarga = new JButton("Edit Harga");
		btnEditHarga.setBounds(130, 13, 110, 23);
		panel_2.add(btnEditHarga);
		
		JButton btnBatalTransaksi = new JButton("Batalkan Transaksi");
		btnBatalTransaksi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin membatalkan transaksi ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (FMain.konfir == JOptionPane.YES_OPTION){
					if (FCetakFaktur.rdbtnTransaksiTunai.isSelected()){
						BatalTransaksiTunai(txtNoFaktur.getText());
					}
					else{
						BatalTransaksiKredit(txtNoFaktur.getText());
					}
				}

			}
		});
		btnBatalTransaksi.setBounds(238, 42, 121, 23);
		panel_2.add(btnBatalTransaksi);
		
		JButton btnHapusItem = new JButton("Hapus Item");
		btnHapusItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tblDetailTransaksi.getSelectedRow() >= 0){
					FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin mengapus item ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
					if (FMain.konfir == JOptionPane.YES_OPTION){
						if (FCetakFaktur.rdbtnTransaksiTunai.isSelected()){
							HapusItemTransaksiTunai(
									txtNoFaktur.getText(), 
									tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 0).toString(), 
									tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 3).toString(), 
									Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 4).toString()), 
									Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 2).toString().replace(",", ""))
									);
						}
						else{
							HapusItemTransaksiKredit(
									txtNoFaktur.getText(), 
									tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 0).toString(), 
									tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 3).toString(), 
									Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 4).toString()), 
									Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 2).toString().replace(",", ""))
									);
						}
						
					}
				}
			}
		});
		btnHapusItem.setBounds(130, 42, 98, 23);
		panel_2.add(btnHapusItem);
		
		JButton btnTambahItem = new JButton("Tambah Item");
		btnTambahItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() >= 0){
					FTambahItemTransaksi.main(null);
					FTambahItemTransaksi.NoFaktur = txtNoFaktur.getText();
					window.frame.setEnabled(false);
				}
			}
		});
		btnTambahItem.setBounds(10, 13, 110, 23);
		panel_2.add(btnTambahItem);
		
		JButton btnSetDiskon = new JButton("Set Diskon");
		btnSetDiskon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tblDetailTransaksi.getSelectedRow() >= 0){
					FSetDiskonTransaksi.main(null);
					FSetDiskonTransaksi.NoFaktur = txtNoFaktur.getText();
					FSetDiskonTransaksi.IdBarang = tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 0).toString();
					FSetDiskonTransaksi.NamaBarang = tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 1).toString();
					FSetDiskonTransaksi.Harga = Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 2).toString().replace(",", ""));
					FSetDiskonTransaksi.Diskon = Float.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 6).toString().replace("%", ""));
					FSetDiskonTransaksi.Satuan = tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 3).toString();
					FSetDiskonTransaksi.Qty = Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 4).toString());
					window.frame.setEnabled(false);
				}
			}
		});
		btnSetDiskon.setBounds(10, 42, 110, 23);
		panel_2.add(btnSetDiskon);
		
		btnKeKredit = new JButton("Ubah ke Transaksi Kredit");
		btnKeKredit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TunaiKeKredit(txtNoFaktur.getText());
			}
		});
		btnKeKredit.setEnabled(false);
		btnKeKredit.setBounds(10, 611, 255, 23);
		frame.getContentPane().add(btnKeKredit);
		
		btnKeTunai = new JButton("Ubah ke Transaksi Tunai");
		btnKeTunai.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (FMain.TotalBayarTransaksiKredit(txtNoFaktur.getText()) != FMain.SisaHutang(txtNoFaktur.getText())){
					JOptionPane.showMessageDialog(null, "Transaksi kredit ini tidak diperbolehkan untuk diubah!!!\nTransaksi Kredit ini sudah dibayarkan sebagian!!!");
				}
				else
					KreditKeTunai(txtNoFaktur.getText());
			}
		});
		btnKeTunai.setEnabled(false);
		btnKeTunai.setBounds(273, 611, 255, 23);
		frame.getContentPane().add(btnKeTunai);
		btnEditHarga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tblDetailTransaksi.getSelectedRow() >= 0){
					FEditHargaTransaksi.main(null);
					FEditHargaTransaksi.NoFaktur = txtNoFaktur.getText();
					FEditHargaTransaksi.IdBarang = tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 0).toString();
					FEditHargaTransaksi.NamaBarang = tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 1).toString();
					FEditHargaTransaksi.harga_lama = Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 2).toString().replace(",", ""));
					FEditHargaTransaksi.Qty = Integer.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 4).toString());
					FEditHargaTransaksi.satuan = tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 3).toString();
					FEditHargaTransaksi.Diskon = Float.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 6).toString().replace("%", ""));
					window.frame.setEnabled(false);
				}
			}
		});
		btnExportExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txtNoFaktur.getText().equals("")){
					JOptionPane.showMessageDialog(null, "No Faktur tidak boleh kosong!!!");
				}
				else{
					if (rdbtnTransaksiTunai.isSelected()) ExportExcelFakturTunai();
					else if (rdbtnTransaksiKredit.isSelected()) ExportExcelFakturKredit();
				}
			}
		});
		btnCetak.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txtNoFaktur.getText().equals("")){
					JOptionPane.showMessageDialog(null, "No Faktur tidak boleh kosong!!!");
				}
				else{
					if (rdbtnTransaksiTunai.isSelected()) CetakFakturTunai();
					else if (rdbtnTransaksiKredit.isSelected()) CetakFakturKredit();
				}
				
			}
		});
	}
	
	private void TampilTransaksiTunai(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"No. Faktur",
					"ID Pel.",
					"Nama",
					"Toko",
					"Tanggal",
					"Total Bayar",
					"Transaksi"
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
			query = "SELECT "
					+ "tb_transaksi.no_faktur, "
					+ "tb_transaksi.id_pelanggan, "
					+ "tb_pelanggan.nama, "
					+ "tb_pelanggan.nama_toko, "
					+ "date_format(tb_transaksi.tanggal, '%d-%m-%Y') as tanggal, "
					+ "tb_transaksi.total_bayar, "
					+ "tb_transaksi.jns_transaksi "
					+ "FROM "
					+ "tb_transaksi "
					+ "INNER JOIN tb_pelanggan ON tb_transaksi.id_pelanggan = tb_pelanggan.id "
					+ "where not tb_transaksi.jns_transaksi = 'Eceran'"
					+ "order by tb_transaksi.jns_transaksi";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("no_faktur"),
						rs.getString("id_pelanggan"),
						rs.getString("nama"),
						rs.getString("nama_toko"),
						rs.getString("tanggal"),
						FMain.FormatAngka(rs.getInt("total_bayar")),
						rs.getString("jns_transaksi")
				});
			};	
			
			table.setModel(model);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(120);//NoFaktur
			table.getColumnModel().getColumn(1).setPreferredWidth(40);//IdPel
			table.getColumnModel().getColumn(2).setPreferredWidth(80);//nama
			table.getColumnModel().getColumn(3).setPreferredWidth(80);//NamaToko
			table.getColumnModel().getColumn(4).setPreferredWidth(70);//Tgl
			table.getColumnModel().getColumn(5).setPreferredWidth(70);//TotalBayar
			table.getColumnModel().getColumn(6).setPreferredWidth(60);//jnsTrans
			
			st.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
	}
	
	private void TampilTransaksiKredit(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String[] columnNames = {
					"No. Faktur",
					"ID Pelanggan",
					"Nama",
					"Toko",
					"Tanggal",
					"Jatuh Tempo",
					"Total Bayar",
					"Transaksi"
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
			query = "SELECT "
					+ "tb_transaksi_kredit.no_faktur, "
					+ "tb_transaksi_kredit.id_pelanggan, "
					+ "tb_pelanggan.nama, "
					+ "tb_pelanggan.nama_toko, "
					+ "tb_transaksi_kredit.tanggal, "
					+ "tb_transaksi_kredit.tanggal_jatuh_tempo, "
					+ "tb_transaksi_kredit.total_bayar, "
					+ "tb_transaksi_kredit.jns_transaksi "
					+ "FROM "
					+ "tb_transaksi_kredit "
					+ "INNER JOIN tb_pelanggan ON tb_transaksi_kredit.id_pelanggan = tb_pelanggan.id "
					+ "order by tb_transaksi_kredit.jns_transaksi";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("no_faktur"),
						rs.getString("id_pelanggan"),
						rs.getString("nama"),
						rs.getString("nama_toko"),
						rs.getString("tanggal"),
						rs.getString("tanggal_jatuh_tempo"),
						FMain.FormatAngka(rs.getInt("total_bayar")),
						rs.getString("jns_transaksi")
				});
			};	
			
			table.setModel(model);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(120);//NoFaktur
			table.getColumnModel().getColumn(1).setPreferredWidth(40);//IdPel
			table.getColumnModel().getColumn(2).setPreferredWidth(80);//nama
			table.getColumnModel().getColumn(3).setPreferredWidth(80);//NamaToko
			table.getColumnModel().getColumn(4).setPreferredWidth(70);//Tgl
			table.getColumnModel().getColumn(5).setPreferredWidth(70);//JthTempo
			table.getColumnModel().getColumn(6).setPreferredWidth(70);//total
			table.getColumnModel().getColumn(7).setPreferredWidth(60);//jnsTrans
			
			st.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
	}
	
	private void CetakFakturTunai(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			Map<String, Object> parametersMap = new HashMap<String, Object>();
			
			String reportName = ".\\Laporan\\FakturTunai.jasper";
	        parametersMap.put("NoFaktur", txtNoFaktur.getText());
	        parametersMap.put("AdminSistem", FMain.NamaUserLogin(FMain.Username));
	        
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
	
	private void CetakFakturKredit(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			Map<String, Object> parametersMap = new HashMap<String, Object>();
			
			String reportName = ".\\Laporan\\FakturKredit.jasper";
	        parametersMap.put("NoFaktur", txtNoFaktur.getText());
	        parametersMap.put("AdminSistem", FMain.NamaUserLogin(FMain.Username));
	        
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
	
	private void TampilTabelBarang(String NoFaktur){
		modelDetailTransaksi.setRowCount(0);
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
					"Qty",
					"Sub Total",
					"Diskon",
					"Diskon Harga"
			};
			
			modelDetailTransaksi.setColumnIdentifiers(columnNames);
			
			String query=null;
			ResultSet rs;
			
			if (rdbtnTransaksiTunai.isSelected()){
				query = "SELECT " +
						"tb_detail_transaksi.id_barang, " +
						"tb_barang.nama, " +
						"tb_detail_transaksi.harga_jual, " +
						"tb_detail_transaksi.satuan, " +
						"tb_detail_transaksi.qty, " +
						"tb_detail_transaksi.diskon, " +
						"(tb_detail_transaksi.qty*tb_detail_transaksi.harga_jual) as SubTotal " +
						//"(tb_detail_transaksi.qty*(tb_detail_transaksi.harga_jual-(tb_detail_transaksi.harga_jual*(tb_detail_transaksi.diskon/100)))) as FinalSubTotal " +
						//"(tb_detail_transaksi.harga_jual-(tb_detail_transaksi.harga_jual*(tb_detail_transaksi.diskon/100))) as FinalSubTotal " +
						"FROM " +
						"tb_detail_transaksi " +
						"INNER JOIN tb_barang ON tb_detail_transaksi.id_barang = tb_barang.id " +
						"WHERE " +
						"tb_detail_transaksi.no_faktur = ?";
			}
			else{
				query = "SELECT " +
						"tb_detail_transaksi_kredit.id_barang, " +
						"tb_barang.nama, " +
						"tb_detail_transaksi_kredit.harga_jual, " +
						"tb_detail_transaksi_kredit.satuan, " +
						"tb_detail_transaksi_kredit.qty, " +
						"tb_detail_transaksi_kredit.diskon, " +
						"(tb_detail_transaksi_kredit.qty*tb_detail_transaksi_kredit.harga_jual) as SubTotal " +
						//"(tb_detail_transaksi_kredit.qty*(tb_detail_transaksi_kredit.harga_jual-(tb_detail_transaksi_kredit.harga_jual*(tb_detail_transaksi_kredit.diskon/100)))) as FinalSubTotal " +
						"FROM " +
						"tb_detail_transaksi_kredit " +
						"INNER JOIN tb_barang ON tb_detail_transaksi_kredit.id_barang = tb_barang.id " +
						"WHERE " +
						"tb_detail_transaksi_kredit.no_faktur = ?";
			}
			
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,NoFaktur);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			while (rs.next()){
				modelDetailTransaksi.addRow(new Object[] {
						rs.getString("id_barang"),
						rs.getString("nama"),
						FMain.FormatAngka(rs.getInt("harga_jual")),
						rs.getString("satuan"),
						rs.getString("qty"),
						FMain.FormatAngka(rs.getInt("SubTotal")),
						rs.getString("diskon") + "%",
						FMain.FormatAngka((int) (float) (rs.getInt("SubTotal")*rs.getFloat("diskon")/100))
				});
			};	
			
			
			tblDetailTransaksi.setModel(modelDetailTransaksi);
			tblDetailTransaksi.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblDetailTransaksi.getColumnModel().getColumn(0).setPreferredWidth(50);//id
			tblDetailTransaksi.getColumnModel().getColumn(1).setPreferredWidth(110);//nama
			tblDetailTransaksi.getColumnModel().getColumn(2).setPreferredWidth(50);//hrg
			tblDetailTransaksi.getColumnModel().getColumn(3).setPreferredWidth(50);//satuan
			tblDetailTransaksi.getColumnModel().getColumn(4).setPreferredWidth(30);//qty
			tblDetailTransaksi.getColumnModel().getColumn(5).setPreferredWidth(60);//subtotal
			tblDetailTransaksi.getColumnModel().getColumn(6).setPreferredWidth(40);//diskon
			tblDetailTransaksi.getColumnModel().getColumn(7).setPreferredWidth(80);//finalsubtotal
			
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil tabel barang: " + e);
		}
	}
	
	private void ExportExcelFakturTunai(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			Map<String, Object> parametersMap = new HashMap<String, Object>();
			
			String reportName = ".\\Laporan\\FakturTunai.jasper";
	        parametersMap.put("NoFaktur", txtNoFaktur.getText());
	        parametersMap.put("AdminSistem", FMain.NamaUserLogin(FMain.Username));
	        
	        JasperPrint jp = JasperFillManager.fillReport(reportName, parametersMap, conn);
	        
	        JRXlsExporter xlsExporter = new JRXlsExporter();
	        xlsExporter.setExporterInput(new SimpleExporterInput(jp));
	        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("user.home") + "\\Desktop\\" + txtNoFaktur.getText().replace("/", "_") + ".xls"));
	        SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
	        xlsReportConfiguration.setOnePagePerSheet(false);
	        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
	        xlsReportConfiguration.setDetectCellType(false);
	        xlsReportConfiguration.setWhitePageBackground(false);
	        xlsExporter.setConfiguration(xlsReportConfiguration);
	        
	        xlsExporter.exportReport();
	               	
        	conn.close();
        	JOptionPane.showMessageDialog(null, "Faktur berhasil di-export ke file Ms. Excel\nFile tersimpan di Desktop\ndengan nama file " + txtNoFaktur.getText().replace("/", "_") + ".xls");
			
		}
		catch(Exception e){System.out.println("Ada error bro cetak laporan" + e);}
	}
	
	private void ExportExcelFakturKredit(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			Map<String, Object> parametersMap = new HashMap<String, Object>();
			
			String reportName = ".\\Laporan\\FakturKredit.jasper";
	        parametersMap.put("NoFaktur", txtNoFaktur.getText());
	        parametersMap.put("AdminSistem", FMain.NamaUserLogin(FMain.Username));
	        
	        JasperPrint jp = JasperFillManager.fillReport(reportName, parametersMap, conn);
	        
	        JRXlsExporter xlsExporter = new JRXlsExporter();
	        xlsExporter.setExporterInput(new SimpleExporterInput(jp));
	        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("user.home") + "\\Desktop\\" + txtNoFaktur.getText().replace("/", "_") + ".xls"));
	        SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
	        xlsReportConfiguration.setOnePagePerSheet(false);
	        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
	        xlsReportConfiguration.setDetectCellType(false);
	        xlsReportConfiguration.setWhitePageBackground(false);
	        xlsExporter.setConfiguration(xlsReportConfiguration);
	        
	        xlsExporter.exportReport();
	               	
        	conn.close();
        	JOptionPane.showMessageDialog(null, "Faktur berhasil di-export ke file Ms. Excel\nFile tersimpan di Desktop\ndengan nama file " + txtNoFaktur.getText().replace("/", "_") + ".xls");
			
		}
		catch(Exception e){System.out.println("Ada error bro cetak laporan" + e);}
	}
	
	private void HapusItemTransaksiTunai(String NoFaktur, String IdBarang, String Satuan, int QtyLama, int HargaSatuan){
		FMain.UpdateStok(IdBarang, Satuan, QtyLama, 0);
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = null;
			PreparedStatement psQuery=null;
			
			selisih = 0 - QtyLama;
			potongan = (int) (float) ((HargaSatuan)*(Float.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 6).toString().replace("%", "")) / 100));
			harga = selisih * (HargaSatuan-potongan);
			Total = FMain.TotalBayarTransaksiTunai(NoFaktur) + harga;
			
			query = "update tb_transaksi set total_bayar=? where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setInt(1, Total);
			psQuery.setString(2, NoFaktur);
			psQuery.execute();
			
			query = "delete from tb_detail_transaksi where no_faktur = ? and id_barang = ? and satuan = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.setString(2, IdBarang);
			psQuery.setString(3, Satuan);
			psQuery.execute();
			
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Item berhasil dihapus...");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private void HapusItemTransaksiKredit(String NoFaktur, String IdBarang, String Satuan, int QtyLama, int HargaSatuan){
		if (FMain.TotalBayarTransaksiKredit(NoFaktur) != FMain.SisaHutang(NoFaktur)){
			JOptionPane.showMessageDialog(null, "Hapus item tidak diperbolehkan!!!\nTransaksi Kredit ini sudah dibayarkan sebagian!!!");
		}
		else{
			FMain.UpdateStok(IdBarang, Satuan, QtyLama, 0);
			try {
				String MyDriver = "com.mysql.jdbc.Driver";
				String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
				Class.forName(MyDriver);
				final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
				
				String query = null;
				PreparedStatement psQuery=null;
				
				selisih = 0 - QtyLama;
				potongan = (int) (float) ((HargaSatuan)*(Float.valueOf(tblDetailTransaksi.getValueAt(tblDetailTransaksi.getSelectedRow(), 6).toString().replace("%", "")) / 100));
				harga = selisih * (HargaSatuan-potongan);
				Total = FMain.TotalBayarTransaksiKredit(NoFaktur) + harga;
				
				query = "update tb_transaksi_kredit set total_bayar=? where no_faktur = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setInt(1, Total);
				psQuery.setString(2, NoFaktur);
				psQuery.execute();
				
				query = "update tb_hutang set sisa_hutang=? where no_faktur = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setInt(1, Total);
				psQuery.setString(2, NoFaktur);
				psQuery.execute();
				
				query = "delete from tb_detail_transaksi_kredit where no_faktur = ? and id_barang = ? and satuan = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setString(1, NoFaktur);
				psQuery.setString(2, IdBarang);
				psQuery.setString(3, Satuan);
				psQuery.execute();
				
				
				psQuery.close();
				conn.close();
				
				JOptionPane.showMessageDialog(null, "Item berhasil dihapus...");
				
			} catch (Exception eee) {
				JOptionPane.showMessageDialog(null, "Edit Data Gagal");
				System.out.println("error simpan data: " + eee);
			}
		}
	}
	
	private void BatalTransaksiTunai(String NoFaktur){
		short i = 0;
		for(i=0; i<tblDetailTransaksi.getRowCount(); i++){
			FMain.UpdateStok(
					tblDetailTransaksi.getValueAt(i, 0).toString(), 
					tblDetailTransaksi.getValueAt(i, 3).toString(), 
					Integer.valueOf(tblDetailTransaksi.getValueAt(i, 4).toString()), 
					0
			);
		}
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = null;
			PreparedStatement psQuery=null;
			
			query = "delete from tb_transaksi where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			query = "delete from tb_detail_transaksi where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Transaksi berhasil dibatalkan...");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private void BatalTransaksiKredit(String NoFaktur){
		if (FMain.TotalBayarTransaksiKredit(NoFaktur) != FMain.SisaHutang(NoFaktur)){
			JOptionPane.showMessageDialog(null, "Pembatalan transaksi kredit tidak diperbolehkan!!!\nTransaksi Kredit ini sudah dibayarkan sebagian!!!");
		}
		else{
			short i = 0;
			for(i=0; i<tblDetailTransaksi.getRowCount(); i++){
				FMain.UpdateStok(
						tblDetailTransaksi.getValueAt(i, 0).toString(), 
						tblDetailTransaksi.getValueAt(i, 3).toString(), 
						Integer.valueOf(tblDetailTransaksi.getValueAt(i, 4).toString()), 
						0
				);
			}
			try {
				String MyDriver = "com.mysql.jdbc.Driver";
				String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
				Class.forName(MyDriver);
				final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
				
				String query = null;
				PreparedStatement psQuery=null;
				
				query = "delete from tb_transaksi_kredit where no_faktur = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setString(1, NoFaktur);
				psQuery.execute();
				
				query = "delete from tb_detail_transaksi_kredit where no_faktur = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setString(1, NoFaktur);
				psQuery.execute();
				
				query = "delete from tb_hutang where no_faktur = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setString(1, NoFaktur);
				psQuery.execute();
				
				psQuery.close();
				conn.close();
				
				JOptionPane.showMessageDialog(null, "Transaksi berhasil dibatalkan...");
				
			} catch (Exception eee) {
				JOptionPane.showMessageDialog(null, "Edit Data Gagal");
				System.out.println("error simpan data: " + eee);
			}
		}
	}
	
	private void CariData(String teks){
		int i=0, startIndex=0;
		if (table.getSelectedRow() != -1) startIndex = table.getSelectedRow()+1;
		for (i=startIndex; i<table.getRowCount(); i++){
			if (table.getValueAt(i, 0).toString().matches(".*" + teks + ".*")){
				table.setRowSelectionInterval(i, i);
				table.scrollRectToVisible(new Rectangle(table.getCellRect(i, 0, true)));
				TampilTabelBarang(table.getValueAt(table.getSelectedRow(), 0).toString());
				break;
			}
		}
		if (i == table.getRowCount()) {
			JOptionPane.showMessageDialog(null, "Proses cari data selesai...");
			table.scrollRectToVisible(new Rectangle(table.getCellRect(0, 0, true)));
			table.clearSelection();
			txtNoFaktur.selectAll();
			txtNoFaktur.requestFocus();
		}
		
	}
	
	private void TunaiKeKredit(String NoFaktur){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = null;
			PreparedStatement psQuery=null;
			
			query = "insert into tb_hutang values (?,?)";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.setInt(2, FMain.TotalBayarTransaksiTunai(NoFaktur));
			psQuery.execute();
			
			query = "insert into tb_transaksi_kredit "
					+ "select no_faktur, id_pelanggan, total_bayar, tanggal, id_karyawan, tanggal + interval 1 month, jns_transaksi, nama_supir from tb_transaksi "
					+ "where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			query = "insert into tb_detail_transaksi_kredit select * from tb_detail_transaksi where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			query = "delete from tb_transaksi where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			query = "delete from tb_detail_transaksi where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Transaksi berhasil diubah ke Kredit...");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private void KreditKeTunai(String NoFaktur){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = null;
			PreparedStatement psQuery=null;
			
			query = "insert into tb_transaksi "
					+ "select no_faktur, id_pelanggan, total_bayar, tanggal, id_karyawan, jns_transaksi, nama_supir from tb_transaksi_kredit "
					+ "where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			query = "insert into tb_detail_transaksi select * from tb_detail_transaksi_kredit where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			query = "delete from tb_transaksi_kredit where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			query = "delete from tb_detail_transaksi_kredit where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			query = "delete from tb_hutang where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			query = "delete from tb_detail_hutang where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Transaksi berhasil diubah ke Tunai...");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
}
