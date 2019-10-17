import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Font;
import org.jdesktop.swingx.JXBusyLabel;

public class FLogin {

	private JFrame frame;
	private JTextField txtUsername;
	private JPasswordField txtPwd;
	private JButton btnLogin;
	static FLogin window;	
	private String user,pass,host;
	static boolean ReportInitialized = false;
	private int konfir=0;
	private JXBusyLabel bslblTest;
	private String query, user1, pwd, level=null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
		    //UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FLogin();
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
	public FLogin() { 
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.CYAN);
		frame.setTitle("Log In");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Login.png"));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				//busyLabel.setVisible(false);
				txtUsername.requestFocus();
				BacaUser();
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				konfir = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menutup aplikasi?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (konfir == JOptionPane.YES_OPTION) System.exit(0);
			}
		});
		
		frame.setResizable(false);
		frame.setBounds(100, 100, 338, 171);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel llblUsername = new JLabel("Username");
		llblUsername.setFont(new Font("Tahoma", llblUsername.getFont().getStyle() | Font.BOLD, 11));
		llblUsername.setForeground(Color.DARK_GRAY);
		llblUsername.setBounds(24, 26, 81, 14);
		frame.getContentPane().add(llblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", lblPassword.getFont().getStyle() | Font.BOLD, 11));
		lblPassword.setForeground(Color.DARK_GRAY);
		lblPassword.setBounds(24, 62, 81, 14);
		frame.getContentPane().add(lblPassword);
		
		txtUsername = new JTextField();
		txtUsername.setHorizontalAlignment(SwingConstants.CENTER);
		txtUsername.setBounds(115, 21, 191, 25);
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPwd = new JPasswordField();
		txtPwd.setEchoChar('Φ');
		txtPwd.setHorizontalAlignment(SwingConstants.CENTER);
		txtPwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					ProsesLogin();
				}
			}
		});
		txtPwd.setBounds(115, 57, 191, 25);
		frame.getContentPane().add(txtPwd);
		
		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", btnLogin.getFont().getStyle() & ~Font.ITALIC | Font.BOLD, 12));
		btnLogin.setBorder(null);
		btnLogin.setIcon(new ImageIcon(".\\icons\\Log_in.png"));
		btnLogin.setBackground(Color.LIGHT_GRAY);
		btnLogin.setForeground(Color.DARK_GRAY);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ProsesLogin();
			}
		});
		btnLogin.setBounds(115, 93, 91, 25);
		frame.getContentPane().add(btnLogin);
		
		JButton btnBatal = new JButton("Batal");
		btnBatal.setFont(new Font("Tahoma", btnBatal.getFont().getStyle() & ~Font.ITALIC | Font.BOLD, 12));
		btnBatal.setBorder(null);
		btnBatal.setIcon(new ImageIcon(".\\icons\\Tutup.png"));
		btnBatal.setBackground(Color.LIGHT_GRAY);
		btnBatal.setForeground(Color.DARK_GRAY);
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				konfir = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menutup aplikasi?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (konfir == JOptionPane.YES_OPTION) System.exit(0);
			}
		});
		btnBatal.setBounds(215, 93, 91, 25);
		frame.getContentPane().add(btnBatal);
		
		bslblTest = new JXBusyLabel();
		bslblTest.setVisible(false);
		bslblTest.setBusy(true);
		bslblTest.setBounds(44, 92, 26, 26);
		frame.getContentPane().add(bslblTest);
	}
	
	private void Inisialisasi(){
		txtUsername.setText("");
		txtPwd.setText("");
		txtUsername.requestFocus();
	}

	@SuppressWarnings("deprecation")
	private void ProsesLogin(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, user, pass);
			
			
			ResultSet rs;
			int jmldata = 0;
			
			user1 = txtUsername.getText();
			pwd = txtPwd.getText();
	
			query = "select * from tb_user where user = ? and pass = password(?)";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, user1);
			psQuery.setString(2, pwd);
			psQuery.execute();
			
			rs = psQuery.getResultSet();
			while (rs.next()){
				jmldata++;
				level = rs.getString("level");
			}
			if (jmldata == 0) {
				JOptionPane.showMessageDialog(btnLogin, "Username atau Password tidak valid!!!");
				Inisialisasi();
			}
			else {
				if (ReportInitialized == false){
					bslblTest.setVisible(true);
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							InisialisasiReport();
							FMain.main(null);
							FMain.LevelUser = level;
							FMain.Username = "User login: " + AmbilNama(user1) + " (" + level + ")";
							FMain.id_karyawan = AmbilIdKaryawan(user1);
							query = user1 = pwd = level = null;
							window.frame.dispose();
							frame.dispose();
							window.frame = null;
							frame = null;
							window = null;
						}
					});
					t.start();
					t=null;
					System.gc();
				}
				else{
					FMain.main(null);
					FMain.LevelUser = level;
					FMain.Username = "User login: " + AmbilNama(user1) + " (" + level + ")";
					FMain.id_karyawan = AmbilIdKaryawan(user1);
					query = user1 = pwd = level = null;
					window.frame.dispose();
					frame.dispose();
					window.frame = null;
					frame = null;
					window = null;
					System.gc();
				}
				
			}
			psQuery.close();
			rs.close();
			conn.close();
		}
		catch(Exception e){System.out.println(e);}
	}

	private String AmbilNama(String UserName){
		String nama=null;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, user, pass);
			
			String query;
			ResultSet rs;
	
			query = "SELECT tb_karyawan.nama as nama FROM tb_karyawan " +
					"INNER JOIN tb_user ON tb_karyawan.username = tb_user.user " +
					"WHERE tb_user.user = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, UserName);
			psQuery.execute();
			
			rs = psQuery.getResultSet();
			while (rs.next()) nama = rs.getString("nama");
			psQuery.close();
			rs.close();
			conn.close();
		}
		catch(Exception e){System.out.println(e);}
		return nama;
	}

	private String AmbilIdKaryawan(String UserName){
		String id=null;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, user, pass);
			
			String query;
			ResultSet rs;
	
			query = "SELECT tb_karyawan.id as id FROM tb_karyawan " +
					"INNER JOIN tb_user ON tb_karyawan.username = tb_user.user " +
					"WHERE tb_user.user = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, UserName);
			psQuery.execute();
			
			rs = psQuery.getResultSet();
			while (rs.next()) id = rs.getString("id");
			psQuery.close();
			rs.close();
			conn.close();
		}
		catch(Exception e){System.out.println(e);}
		return id;
	}

	private void BacaUser(){
		String fileName = "setserver";
		try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            user = bufferedReader.readLine();
            pass = bufferedReader.readLine();
            host = bufferedReader.readLine();
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" + fileName + "'");
        }
	}
	
	private void InisialisasiReport(){
		ReportInitialized = true;
		BacaUser();
		JasperPrint jp = null;
        JRViewer jv = null;
    	JFrame jf = null;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, user, pass);
						
			String reportName = ".\\Laporan\\InitReport.jasper";
	        
	        jp = JasperFillManager.fillReport(reportName, null, conn);
	        jv = new JRViewer(jp);
        	jf = new JFrame();
        	jf.getContentPane().add(jv);
        	jf.validate();
        	jf.setVisible(true);
        	jf.setExtendedState(jf.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        	jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);        	
        	conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cetak struk" + e);}
		//FTransaksi.window.frame.setEnabled(true);
		jf.dispose();
		jf = null;
		System.gc();
	}
	
}
