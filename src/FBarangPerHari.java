import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.swing.JRViewer;

public class FBarangPerHari {

	private JFrame frame;
	static FBarangPerHari window;
	private JComboBox<String> cmbTahun;
	private JComboBox<String> cmbBulan;
	private JComboBox<String> cmbTanggal;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FBarangPerHari();
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
	public FBarangPerHari() {
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
				TampilTahun();
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
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Laporan.png"));
		frame.setTitle("Barang per hari");
		frame.setResizable(false);
		frame.setBounds(100, 100, 268, 118);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTahun = new JLabel("Tahun");
		lblTahun.setBounds(10, 11, 46, 14);
		frame.getContentPane().add(lblTahun);
		
		JLabel lblBulan = new JLabel("Bulan");
		lblBulan.setBounds(10, 36, 46, 14);
		frame.getContentPane().add(lblBulan);
		
		JLabel lblTanggal = new JLabel("Tanggal");
		lblTanggal.setBounds(10, 61, 46, 14);
		frame.getContentPane().add(lblTanggal);
		
		cmbTahun = new JComboBox<String>();
		cmbTahun.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				TampilBulan();
			}
		});
		cmbTahun.setBounds(66, 8, 85, 20);
		frame.getContentPane().add(cmbTahun);
		
		cmbBulan = new JComboBox<String>();
		cmbBulan.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				TampilTanggal();
			}
		});
		cmbBulan.setBounds(66, 33, 85, 20);
		frame.getContentPane().add(cmbBulan);
		
		cmbTanggal = new JComboBox<String>();
		cmbTanggal.setBounds(66, 58, 85, 20);
		frame.getContentPane().add(cmbTanggal);
		
		JButton btnPrint = new JButton("Print");
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CetakLaporan();
			}
		});
		btnPrint.setBounds(161, 7, 89, 23);
		frame.getContentPane().add(btnPrint);
		
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
		btnTutup.setBounds(161, 57, 89, 23);
		frame.getContentPane().add(btnTutup);
		
		JButton btnExportExcel = new JButton("Export Excel");
		btnExportExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExportExcel();
			}
		});
		btnExportExcel.setBounds(161, 32, 89, 23);
		frame.getContentPane().add(btnExportExcel);
	}
	private void TampilTahun(){
		cmbTahun.removeAllItems();
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			Statement st;
			
			query = "SELECT tahun FROM ( "
					+ "SELECT YEAR (tanggal) AS tahun FROM tb_transaksi "
					+ "UNION ALL "
					+ "SELECT YEAR (tanggal) AS tahun FROM tb_transaksi kredit "
					+ ") a "
					+ "GROUP BY tahun";
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next()) cmbTahun.addItem(rs.getString("tahun"));
			
			st.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil tahun" + e);}
	}
	
	private void TampilBulan(){
		cmbBulan.removeAllItems();
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;

			query = "SELECT a.bulan FROM ( "
					+ "SELECT date_format(tanggal, '%m') AS bulan FROM tb_transaksi WHERE YEAR (tanggal) = ? "
					+ "UNION ALL "
					+ "SELECT date_format(tanggal, '%m') AS bulan FROM tb_transaksi_kredit WHERE YEAR (tanggal) = ? "
					+ ") a "
					+ "GROUP BY a.bulan";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,cmbTahun.getSelectedItem().toString());
			psQuery.setString(2,cmbTahun.getSelectedItem().toString());
			psQuery.execute();
			rs = psQuery.getResultSet();

			while (rs.next()) cmbBulan.addItem(rs.getString("bulan"));
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil bulan " + e);}
	}
	
	private void TampilTanggal(){
		cmbTanggal.removeAllItems();
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;

			query = "SELECT tanggal FROM ( "
					+ "SELECT date_format(tanggal, '%d') AS tanggal FROM tb_transaksi "
					+ "WHERE YEAR (tanggal) = ? AND MONTH (tanggal) = ? "
					+ "UNION ALL "
					+ "SELECT date_format(tanggal, '%d') AS tanggal FROM tb_transaksi_kredit "
					+ "WHERE YEAR (tanggal) = ? AND MONTH (tanggal) = ? "
					+ ") a "
					+ "GROUP BY tanggal";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,cmbTahun.getSelectedItem().toString());
			psQuery.setString(2,cmbBulan.getSelectedItem().toString());
			psQuery.setString(3,cmbTahun.getSelectedItem().toString());
			psQuery.setString(4,cmbBulan.getSelectedItem().toString());
			psQuery.execute();
			rs = psQuery.getResultSet();

			while (rs.next()) cmbTanggal.addItem(rs.getString("tanggal"));
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil tanggal " + e);}
	}
	
	private void CetakLaporan(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			Map<String, Object> parametersMap = new HashMap<String, Object>();
			
			String reportName = ".\\Laporan\\BarangPerHari.jasper";
	        parametersMap.put(
	        		"param1",
	        		cmbTanggal.getSelectedItem().toString()+" "+cmbBulan.getSelectedItem().toString()+" "+cmbTahun.getSelectedItem().toString()
	        );
	        parametersMap.put(
	        		"param2",
	        		cmbTahun.getSelectedItem().toString()+cmbBulan.getSelectedItem().toString()+cmbTanggal.getSelectedItem().toString()
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
			
			String reportName = ".\\Laporan\\BarangPerHari.jasper";
	        parametersMap.put(
	        		"param1",
	        		cmbTanggal.getSelectedItem().toString()+" "+cmbBulan.getSelectedItem().toString()+" "+cmbTahun.getSelectedItem().toString()
	        );
	        parametersMap.put(
	        		"param2",
	        		cmbTahun.getSelectedItem().toString()+cmbBulan.getSelectedItem().toString()+cmbTanggal.getSelectedItem().toString()
	        );
	        
	        JasperPrint jp = JasperFillManager.fillReport(reportName, parametersMap, conn);
	        
	        JRXlsExporter xlsExporter = new JRXlsExporter();
	        xlsExporter.setExporterInput(new SimpleExporterInput(jp));
	        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("user.home") + 
	        		"\\Desktop\\LaporanBarangKeluarHarian_" + 
	        		cmbTanggal.getSelectedItem().toString() +
	        		"_" +
	        		cmbBulan.getSelectedItem().toString() +
	        		"_" +
	        		cmbTahun.getSelectedItem().toString() +
	        		".xls"));
	        SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
	        xlsReportConfiguration.setOnePagePerSheet(false);
	        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
	        xlsReportConfiguration.setDetectCellType(false);
	        xlsReportConfiguration.setWhitePageBackground(false);
	        xlsExporter.setConfiguration(xlsReportConfiguration);
	        
	        xlsExporter.exportReport();
	               	
        	conn.close();
        	JOptionPane.showMessageDialog(null, "Faktur berhasil di-export ke file Ms. Excel\nFile tersimpan di Desktop\n" +
        			"dengan nama file LaporanBarangKeluarHarian_" + 
	        		cmbTanggal.getSelectedItem().toString() +
	        		"_" +
	        		cmbBulan.getSelectedItem().toString() +
	        		"_" +
	        		cmbTahun.getSelectedItem().toString() +
	        		".xls");
			
		}
		catch(Exception e){System.out.println("Ada error bro cetak laporan" + e);}
	}
}
