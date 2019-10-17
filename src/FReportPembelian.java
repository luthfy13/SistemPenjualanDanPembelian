import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.jdesktop.swingx.JXDatePicker;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.swing.JRViewer;

public class FReportPembelian {

	private JFrame frame;
	static FReportPembelian window;
	private JComboBox<String> cmbJenisTransaksi;
	private JLabel lblPeriode;
	private DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private JXDatePicker tgl1;
	private JXDatePicker tgl2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FReportPembelian();
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
	public FReportPembelian() {
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
			}
			@Override
			public void windowOpened(WindowEvent e) {
				cmbJenisTransaksi.removeAllItems();
				cmbJenisTransaksi.addItem("-PILIH-");
				cmbJenisTransaksi.addItem("Tunai");
				cmbJenisTransaksi.addItem("Kredit");
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
		frame.setTitle("Laporan Pembelian");
		frame.setBounds(100, 100, 416, 120);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblJenisTransaksi = new JLabel("Jenis Transaksi");
		lblJenisTransaksi.setBounds(10, 11, 91, 14);
		frame.getContentPane().add(lblJenisTransaksi);
		
		cmbJenisTransaksi = new JComboBox<String>();
		cmbJenisTransaksi.setBounds(111, 8, 126, 20);
		frame.getContentPane().add(cmbJenisTransaksi);
		
		lblPeriode = new JLabel("Periode");
		lblPeriode.setBounds(10, 36, 46, 14);
		frame.getContentPane().add(lblPeriode);
		
		tgl1 = new JXDatePicker();
		tgl1.setFormats(new String[] {"dd - MM - yyyy"});
		tgl1.setBounds(111, 32, 126, 22);
		frame.getContentPane().add(tgl1);
		
		JLabel lblSd = new JLabel("s.d.");
		lblSd.setBounds(247, 36, 26, 14);
		frame.getContentPane().add(lblSd);
		
		tgl2 = new JXDatePicker();
		tgl2.setFormats(new String[] {"dd - MM - yyyy"});
		tgl2.setBounds(273, 32, 126, 22);
		frame.getContentPane().add(tgl2);
		
		JButton btnPrint = new JButton("Print");
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbJenisTransaksi.getSelectedIndex() == 0){
					JOptionPane.showMessageDialog(null, "Pilih Jenis Transaksi!!!");
				}
				else
					CetakLaporan();
			}
		});
		btnPrint.setBounds(10, 61, 89, 23);
		frame.getContentPane().add(btnPrint);
		
		JButton btnExportExcel = new JButton("Export Excel");
		btnExportExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbJenisTransaksi.getSelectedIndex() == 0){
					JOptionPane.showMessageDialog(null, "Pilih Jenis Transaksi!!!");
				}
				else
					ExportExcel();
			}
		});
		btnExportExcel.setBounds(109, 61, 128, 23);
		frame.getContentPane().add(btnExportExcel);
		
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
		btnTutup.setBounds(247, 61, 89, 23);
		frame.getContentPane().add(btnTutup);
	}
	
	private void CetakLaporan(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			Map<String, Object> parametersMap = new HashMap<String, Object>();
			
			String reportName = null;
			
			switch (cmbJenisTransaksi.getSelectedIndex()){
				case 1 :
						reportName = ".\\Laporan\\LaporanPembelianTunai.jasper";
						break;
				case 2 :
						reportName = ".\\Laporan\\LaporanPembelianKredit.jasper";
						break;
			}
			
	        parametersMap.put("paramTgl1",dateFormat.format(tgl1.getDate()));
	        parametersMap.put("paramTgl2",dateFormat.format(tgl2.getDate()));
	        
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
			
			String reportName = null;
			
			switch (cmbJenisTransaksi.getSelectedIndex()){
				case 1 :
						reportName = ".\\Laporan\\LaporanPembelianTunai.jasper";
						break;
				case 2 :
						reportName = ".\\Laporan\\LaporanPembelianKredit.jasper";
						break;
			}
			
	        parametersMap.put("paramTgl1",dateFormat.format(tgl1.getDate()));
	        parametersMap.put("paramTgl2",dateFormat.format(tgl2.getDate()));
	        
	        JasperPrint jp = JasperFillManager.fillReport(reportName, parametersMap, conn);
	        
	        JRXlsExporter xlsExporter = new JRXlsExporter();
	        xlsExporter.setExporterInput(new SimpleExporterInput(jp));
	        switch (cmbJenisTransaksi.getSelectedIndex()){
			case 1 :
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("user.home") + 
		        		"\\Desktop\\LaporanPembelianTunai_" + 
		        		dateFormat.format(tgl1.getDate()).toString() + "_" +
		        		dateFormat.format(tgl2.getDate()).toString() +
		        		".xls"));
					break;
			case 2 :
				xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("user.home") + 
		        		"\\Desktop\\LaporanPembelianKredit_" + 
		        		dateFormat.format(tgl1.getDate()).toString() + "_" +
		        		dateFormat.format(tgl2.getDate()).toString() +
		        		".xls"));
					break;
	        }
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
        			"dengan nama file LaporanPembelian_" + 
	        		dateFormat.format(tgl1.getDate()).toString() + "_" +
	        		dateFormat.format(tgl2.getDate()).toString() +
	        		".xls");
		}
		catch(Exception e){System.out.println("Ada error bro cetak laporan" + e);}
	}
}
