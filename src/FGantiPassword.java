import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FGantiPassword {

	private JFrame frame;
	static FGantiPassword window;
	private JPasswordField txtPwd1;
	private JPasswordField txtPwd2;
	static String UserName=null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FGantiPassword();
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
	public FGantiPassword() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				FSettingUser.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		frame.setTitle("Ganti Password");
		frame.setResizable(false);
		frame.setBounds(100, 100, 375, 125);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblPasswordLama = new JLabel("Password Baru");
		lblPasswordLama.setBounds(10, 11, 153, 14);
		frame.getContentPane().add(lblPasswordLama);
		
		JLabel lblKonfirmasiPasswordBaru = new JLabel("Konfirmasi Password Baru");
		lblKonfirmasiPasswordBaru.setBounds(10, 36, 153, 14);
		frame.getContentPane().add(lblKonfirmasiPasswordBaru);
		
		txtPwd1 = new JPasswordField();
		txtPwd1.setBounds(173, 8, 188, 20);
		frame.getContentPane().add(txtPwd1);
		
		txtPwd2 = new JPasswordField();
		txtPwd2.setBounds(173, 33, 188, 20);
		frame.getContentPane().add(txtPwd2);
		
		JButton btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				if (txtPwd1.getText().equals(txtPwd2.getText())){
					FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin mengganti password?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
					if (FMain.konfir == JOptionPane.YES_OPTION){
						GantiPwd(UserName);
						FMain.konfir=0;
						FSettingUser.window.frame.setEnabled(true);
						window.frame.dispose();
						frame.dispose();
						window.frame = null;
						frame = null;
						window = null;
						System.gc();
					}
				}
				else
					JOptionPane.showMessageDialog(null, "Password tidak sama!!!");
				
			}
		});
		btnSimpan.setBounds(173, 64, 89, 23);
		frame.getContentPane().add(btnSimpan);
		
		JButton btnBatal = new JButton("Batal");
		btnBatal.setMnemonic(KeyEvent.VK_X);
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FSettingUser.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnBatal.setBounds(272, 64, 89, 23);
		frame.getContentPane().add(btnBatal);
	}
	
	@SuppressWarnings("deprecation")
	private void GantiPwd(String user){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_user set pass=password(?) where user = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtPwd1.getText());
			psQuery.setString(2, user);
			psQuery.execute();
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Password " + user + " berhasil diganti...");
			
		} catch (Exception eee) {
			System.out.println("error edit password: " + eee);
		}
	}
}
