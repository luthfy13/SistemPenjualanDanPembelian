import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.swing.JRViewer;

public class FBarangPerTahun {

	private JFrame frame;
	static FBarangPerTahun window;
	private JComboBox<String> cmbTahun;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FBarangPerTahun();
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
	public FBarangPerTahun() {
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
		frame.setTitle("Barang per tahun");
		frame.setResizable(false);
		frame.setBounds(100, 100, 270, 105);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTahun = new JLabel("Tahun");
		lblTahun.setBounds(10, 11, 46, 14);
		frame.getContentPane().add(lblTahun);
		
		cmbTahun = new JComboBox<String>();
		cmbTahun.setBounds(66, 8, 85, 20);
		frame.getContentPane().add(cmbTahun);
		
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
		btnTutup.setBounds(161, 41, 89, 23);
		frame.getContentPane().add(btnTutup);
		
		JButton btnExportExcel = new JButton("Export Excel");
		btnExportExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExportExcel();
			}
		});
		btnExportExcel.setBounds(10, 41, 141, 23);
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
	
	private void CetakLaporan(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			Map<String, Object> parametersMap = new HashMap<String, Object>();
			
			String reportName = ".\\Laporan\\BarangPerTahun.jasper";
	        parametersMap.put(
	        		"param1",
	        		cmbTahun.getSelectedItem().toString()
	        );
	        parametersMap.put(
	        		"param2",
	        		cmbTahun.getSelectedItem().toString()
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
			
			String reportName = ".\\Laporan\\BarangPerTahun.jasper";
	        parametersMap.put(
	        		"param1",
	        		cmbTahun.getSelectedItem().toString()
	        );
	        parametersMap.put(
	        		"param2",
	        		cmbTahun.getSelectedItem().toString()
	        );
	        
	        JasperPrint jp = JasperFillManager.fillReport(reportName, parametersMap, conn);
	        
	        JRXlsExporter xlsExporter = new JRXlsExporter();
	        xlsExporter.setExporterInput(new SimpleExporterInput(jp));
	        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("user.home") + 
	        		"\\Desktop\\LaporanBarangKeluarTahunan_" + 
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
        			"dengan nama file LaporanBarangKeluarTahunan_" + 
	        		cmbTahun.getSelectedItem().toString() +
	        		".xls");
			
		}
		catch(Exception e){System.out.println("Ada error bro cetak laporan" + e);}
	}
	
}