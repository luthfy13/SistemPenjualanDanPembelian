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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Toolkit;

public class FKreditBulanan {

	private JFrame frame;
	Connection conn = null;
	Map<String, Object> parametersMap = new HashMap<String, Object>();
	static FKreditBulanan window;
	private JComboBox<String> cmbTahun;
	private JComboBox<String> cmbBulan;
	private JComboBox<String> cmbJenisTransaksi;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FKreditBulanan();
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
	public FKreditBulanan() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Laporan.png"));
		frame.setResizable(false);
		frame.setTitle("Laporan Bulanan");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				cmbJenisTransaksi.removeAllItems();
				cmbJenisTransaksi.addItem("-PILIH-");
				cmbJenisTransaksi.addItem("Grosir");
				cmbJenisTransaksi.addItem("Sales");
				cmbJenisTransaksi.addItem("Semua");
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
		frame.setBounds(100, 100, 315, 125);
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
		cmbTahun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmbBulan.removeAllItems();
				TampilBulan();
			}
		});
		
		cmbJenisTransaksi = new JComboBox<String>();
		cmbJenisTransaksi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cmbTahun.removeAllItems();
				cmbBulan.removeAllItems();
				TampilTahun();
			}
		});
		cmbJenisTransaksi.setBounds(109, 12, 89, 20);
		frame.getContentPane().add(cmbJenisTransaksi);
		cmbTahun.setBounds(109, 36, 89, 22);
		frame.getContentPane().add(cmbTahun);
		
		cmbBulan = new JComboBox<String>();
		cmbBulan.setBounds(109, 61, 89, 22);
		frame.getContentPane().add(cmbBulan);
		
		JLabel lblJenisTransaksi = new JLabel("Jenis Transaksi");
		lblJenisTransaksi.setFont(new Font("Tahoma", lblJenisTransaksi.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblJenisTransaksi.getFont().getSize()));
		lblJenisTransaksi.setBounds(10, 15, 89, 14);
		frame.getContentPane().add(lblJenisTransaksi);
		
		JLabel lblTahun = new JLabel("Tahun");
		lblTahun.setFont(new Font("Tahoma", lblTahun.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblTahun.getFont().getSize()));
		lblTahun.setBounds(10, 40, 46, 14);
		frame.getContentPane().add(lblTahun);
		
		JLabel lblBulan = new JLabel("Bulan");
		lblBulan.setFont(new Font("Tahoma", lblBulan.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblBulan.getFont().getSize()));
		lblBulan.setBounds(10, 65, 46, 14);
		frame.getContentPane().add(lblBulan);
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
		btnTutup.setBounds(208, 61, 89, 23);
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
        String reportName = ".\\Laporan\\LapTransaksiKreditBulanan.jasper";
        parametersMap.put(
        		"paramBulanTahun",
        		cmbBulan.getSelectedItem().toString() + "/" + cmbTahun.getSelectedItem().toString()
        );
        if (cmbJenisTransaksi.getSelectedItem().toString().equals("Semua")){
        	parametersMap.put(
            		"paramJnsTransaksi",
            		"%%"
            );
        }
        else{
        	parametersMap.put(
            		"paramJnsTransaksi",
            		"%"+cmbJenisTransaksi.getSelectedItem().toString()+"%"
            );
        }
        
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
			
			String query=null;
			ResultSet rs;
			
			query = "select year(tanggal) as tahun from tb_transaksi_kredit where jns_transaksi like ? group by tahun";
			PreparedStatement psQuery = conn.prepareStatement(query);
			if (cmbJenisTransaksi.getSelectedItem().toString().equals("Semua")){
				psQuery.setString(1,"%%");
			}
			else{
				psQuery.setString(1,"%"+cmbJenisTransaksi.getSelectedItem().toString()+"%");
			}
			
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			while (rs.next()){
				cmbTahun.addItem(rs.getString("tahun"));
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro set diskon " + e);}
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
			
			query = "SELECT date_format(tanggal, '%m') as bulan from tb_transaksi_kredit " +
					"where year(tanggal) = ? and jns_transaksi like ? group by bulan";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,cmbTahun.getSelectedItem().toString());
			if (cmbJenisTransaksi.getSelectedItem().toString().equals("Semua")){
				psQuery.setString(2,"%%");
			}
			else{
				psQuery.setString(2,"%"+cmbJenisTransaksi.getSelectedItem().toString()+"%");
			}
			
			psQuery.execute();
			rs = psQuery.getResultSet();

			while (rs.next()) cmbBulan.addItem(rs.getString("bulan"));
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil bulan " + e);}
	}
	
	private void ExportExcel(){
        String reportName = ".\\Laporan\\LapTransaksiKreditBulanan.jasper";
        parametersMap.put(
        		"paramBulanTahun",
        		cmbBulan.getSelectedItem().toString() + "/" + cmbTahun.getSelectedItem().toString()
        );
        if (cmbJenisTransaksi.getSelectedItem().toString().equals("Semua")){
        	parametersMap.put(
            		"paramJnsTransaksi",
            		"%%"
            );
        }
        else{
        	parametersMap.put(
            		"paramJnsTransaksi",
            		"%"+cmbJenisTransaksi.getSelectedItem().toString()+"%"
            );
        }
        
        try {
        	JasperPrint jp = JasperFillManager.fillReport(reportName, parametersMap, conn);
	        
	        JRXlsExporter xlsExporter = new JRXlsExporter();
	        xlsExporter.setExporterInput(new SimpleExporterInput(jp));
	        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(System.getProperty("user.home") + 
	        		"\\Desktop\\LaporanTransaksiKreditBulanan_" + 
	        		cmbJenisTransaksi.getSelectedItem().toString() + 
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
        			"dengan nama file LaporanTransaksiKreditBulanan_" + 
	        		cmbJenisTransaksi.getSelectedItem().toString() + 
	        		"_" +
	        		cmbBulan.getSelectedItem().toString() +
	        		"_" +
	        		cmbTahun.getSelectedItem().toString() +
	        		".xls");
        
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}
