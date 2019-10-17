import java.awt.EventQueue;

import javax.swing.JFrame;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.swing.JRViewer;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Toolkit;

public class FLapSalesTahunan {

	private JFrame frame;
	static FLapSalesTahunan window;
	Connection conn = null;
	Map<String, Object> parametersMap = new HashMap<String, Object>();
	private JComboBox<String> cmbTahun; 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FLapSalesTahunan();
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
	public FLapSalesTahunan() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Laporan.png"));
		frame.setResizable(false);
		frame.setTitle("Lap. Sales Tahunan");
		frame.addWindowListener(new WindowAdapter() {
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
			@Override
			public void windowOpened(WindowEvent arg0) {
				TampilTahun();
			}
		});
		frame.setBounds(100, 100, 320, 135);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JButton btnPrint = new JButton("Print");
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				initConnection();
				showReport();
			}
		});
		
		cmbTahun = new JComboBox<String>();
		cmbTahun.setBounds(66, 11, 89, 22);
		frame.getContentPane().add(cmbTahun);
		
		JLabel lblTahun = new JLabel("Tahun");
		lblTahun.setFont(new Font("Tahoma", lblTahun.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblTahun.getFont().getSize()));
		lblTahun.setBounds(10, 15, 46, 14);
		frame.getContentPane().add(lblTahun);
		btnPrint.setBounds(208, 11, 89, 23);
		frame.getContentPane().add(btnPrint);
		
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
		btnTutup.setBounds(208, 70, 89, 23);
		frame.getContentPane().add(btnTutup);
		
		JButton btnExportExcel = new JButton("Export Excel");
		btnExportExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initConnection();
				ExportExcel();
			}
		});
		btnExportExcel.setToolTipText("Export Excel");
		btnExportExcel.setBounds(208, 36, 89, 23);
		frame.getContentPane().add(btnExportExcel);
	}
	
	private void initConnection(){
        
        String HOST = "jdbc:mysql://" + FMain.host + "/db_penjualan";
        String USERNAME = FMain.user;
        String PASSWORD = FMain.pass;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
 
        try {
            conn = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
	
	private void showReport(){
        String reportName = ".\\Laporan\\LapPerSalesTahunan.jasper";
        parametersMap.put(
        		"paramTahun",
        		cmbTahun.getSelectedItem().toString()
        );

        try {
        	JasperPrint jp = JasperFillManager.fillReport(reportName, parametersMap, conn);
        	JRViewer jv = new JRViewer(jp);
        	JFrame jf = new JFrame();
        	jf.getContentPane().add(jv);
        	jf.validate();
        	jf.setVisible(true);
        	jf.setExtendedState(jf.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        	jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        	conn.close();
        
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
	private void TampilTahun(){
		cmbTahun.removeAllItems();
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query;
			Statement st;
			ResultSet rs;
			query = "SELECT YEAR (tb_detail_hutang.tgl_bayar) AS tgl FROM tb_detail_hutang "
					+ "INNER JOIN tb_karyawan ON tb_detail_hutang.id_karyawan = tb_karyawan.id "
					+ "INNER JOIN tb_transaksi_kredit ON tb_transaksi_kredit.no_faktur = tb_detail_hutang.no_faktur "
					+ "INNER JOIN tb_pelanggan ON tb_transaksi_kredit.id_pelanggan = tb_pelanggan.id "
					+ "WHERE tb_karyawan.bagian = 'sales' GROUP BY tgl "
					+ "UNION "
					+ "SELECT YEAR (tb_transaksi.tanggal) AS tgl FROM tb_transaksi "
					+ "INNER JOIN tb_karyawan ON tb_transaksi.id_karyawan = tb_karyawan.id "
					+ "INNER JOIN tb_pelanggan ON tb_transaksi.id_pelanggan = tb_pelanggan.id "
					+ "WHERE tb_karyawan.bagian = 'sales' GROUP BY tgl";
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next()){
				cmbTahun.addItem(rs.getString("tgl"));
			}
			
			st.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro set diskon " + e);}
	}
	
	private void ExportExcel(){
        String reportName = ".\\Laporan\\LapPerSalesTahunan.jasper";
        parametersMap.put(
        		"paramTahun",
        		cmbTahun.getSelectedItem().toString()
        );

        try {
        	JasperPrint jp = JasperFillManager.fillReport(reportName, parametersMap, conn);
	        
	        JRXlsExporter xlsExporter = new JRXlsExporter();
	        xlsExporter.setExporterInput(new SimpleExporterInput(jp));
	        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("user.home") + 
	        		"\\Desktop\\LaporanSalesTahunan_" +  
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
        			"dengan nama file LaporanSalesTahunan_" + 
	        		"_" +
	        		cmbTahun.getSelectedItem().toString() +
	        		".xls");
        
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

}