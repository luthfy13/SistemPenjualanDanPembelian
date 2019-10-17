import java.awt.EventQueue;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
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
import javax.swing.JComboBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Toolkit;

public class FCetakDaftarTagihanHarian {

	private JFrame frame;
	static FCetakDaftarTagihanHarian window;
	Connection conn = null;
	Map<String, Object> parametersMap = new HashMap<String, Object>();
	private JComboBox<String> cmbYangMembuat; 
	private JComboBox<String> cmbYangMenerima;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FCetakDaftarTagihanHarian();
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
	public FCetakDaftarTagihanHarian() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Laporan.png"));
		frame.setResizable(false);
		frame.setTitle("Cetak Daftar Tagihan");
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
				TampilUser();
				TampilSales();
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
		
		cmbYangMembuat = new JComboBox<String>();
		cmbYangMembuat.setBounds(109, 11, 188, 22);
		frame.getContentPane().add(cmbYangMembuat);
		
		JLabel lblTahun = new JLabel("Yang Membuat");
		lblTahun.setFont(new Font("Tahoma", lblTahun.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, lblTahun.getFont().getSize()));
		lblTahun.setBounds(10, 15, 89, 14);
		frame.getContentPane().add(lblTahun);
		btnPrint.setBounds(10, 70, 89, 23);
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
		btnTutup.setBounds(109, 70, 89, 23);
		frame.getContentPane().add(btnTutup);
		
		JLabel lblBulan = new JLabel("Yang Menerima");
		lblBulan.setBounds(10, 40, 89, 14);
		frame.getContentPane().add(lblBulan);
		
		cmbYangMenerima = new JComboBox<String>();
		cmbYangMenerima.setBounds(109, 37, 188, 20);
		frame.getContentPane().add(cmbYangMenerima);
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
        String reportName = ".\\Laporan\\DaftarTagihanHarian.jasper";
        parametersMap.put(
        		"paramYangMembuat",
        		cmbYangMembuat.getSelectedItem().toString()
        );
        parametersMap.put(
        		"paramYangMenerima",
        		cmbYangMenerima.getSelectedItem().toString()
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
	
	private void TampilUser(){
		cmbYangMembuat.removeAllItems();
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query;
			Statement st;
			ResultSet rs;
			query = "select nama from tb_karyawan where not bagian='sales'";
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next()){
				cmbYangMembuat.addItem(rs.getString("nama"));
			}
			
			st.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro set diskon " + e);}
	}
	
	private void TampilSales(){
		cmbYangMenerima.removeAllItems();
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query;
			Statement st;
			ResultSet rs;
			query = "select nama from tb_karyawan where bagian='sales'";
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next()){
				cmbYangMenerima.addItem(rs.getString("nama"));
			}
			
			st.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro set diskon " + e);}
	}
	

}