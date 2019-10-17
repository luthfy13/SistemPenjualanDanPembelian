import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class FTambahUser {

	private JFrame frame;
	static FTambahUser window;
	private JTextField txtNama;
	private JTextField txtUsername;
	private JPasswordField txtPwd;
	private JPasswordField txtConfirmPwd;
	private JComboBox<String> cmbBagian;
	static String IdKaryawan=null, NamaKaryawan=null;
	private String Bagian=null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FTambahUser();
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
	public FTambahUser() {
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
				cmbBagian.removeAllItems();
				cmbBagian.addItem("-PILIH-");
				cmbBagian.addItem("Pos");
				cmbBagian.addItem("Admin");
				txtNama.setText(NamaKaryawan);
				txtUsername.requestFocus();
			}
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
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\AddUser.png"));
		frame.setTitle("Tambah User");
		frame.setResizable(false);
		frame.setBounds(100, 100, 341, 210);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNama = new JLabel("Nama");
		lblNama.setBounds(10, 11, 115, 14);
		frame.getContentPane().add(lblNama);
		
		JLabel lblBagian = new JLabel("Bagian / Level User");
		lblBagian.setBounds(10, 36, 115, 14);
		frame.getContentPane().add(lblBagian);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(10, 61, 115, 14);
		frame.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 86, 115, 14);
		frame.getContentPane().add(lblPassword);
		
		JLabel lblKonfirmasiPassword = new JLabel("Konfirmasi Password");
		lblKonfirmasiPassword.setBounds(10, 111, 115, 14);
		frame.getContentPane().add(lblKonfirmasiPassword);
		
		txtNama = new JTextField();
		txtNama.setEditable(false);
		txtNama.setBounds(135, 8, 185, 20);
		frame.getContentPane().add(txtNama);
		txtNama.setColumns(10);
		
		cmbBagian = new JComboBox<String>();
		cmbBagian.setBounds(135, 33, 185, 20);
		frame.getContentPane().add(cmbBagian);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(135, 58, 185, 20);
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPwd = new JPasswordField();
		txtPwd.setBounds(135, 83, 185, 20);
		frame.getContentPane().add(txtPwd);
		
		txtConfirmPwd = new JPasswordField();
		txtConfirmPwd.setBounds(135, 108, 185, 20);
		frame.getContentPane().add(txtConfirmPwd);
		
		JButton btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if (cmbBagian.getSelectedIndex() != 0){
					if (txtPwd.getText().equals(txtConfirmPwd.getText())){
						FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin menyimpan data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
						if (FMain.konfir == JOptionPane.YES_OPTION){
							SimpanData();
							UpdateDataKaryawan();
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
						JOptionPane.showMessageDialog(null, "Password tidak sama!\nPeriksa kembali password Anda!");
				}
				else
					JOptionPane.showMessageDialog(null, "Level user belum dipilih!!!");
			}
		});
		btnSimpan.setBounds(135, 139, 89, 23);
		frame.getContentPane().add(btnSimpan);
		
		JButton btnBatal = new JButton("Batal");
		btnBatal.setMnemonic(KeyEvent.VK_X);
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FSettingUser.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnBatal.setBounds(231, 139, 89, 23);
		frame.getContentPane().add(btnBatal);
	}
	
	@SuppressWarnings("deprecation")
	private void SimpanData(){
		switch(cmbBagian.getSelectedIndex()){
			case 1 : 
				Bagian = "pos";
				break;
			case 2 : 
				Bagian = "admin";
				break;
		}
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "insert into tb_user values (?,password(?),?)";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtUsername.getText());
			psQuery.setString(2, txtPwd.getText());
			psQuery.setString(3, Bagian);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			Bagian = null;
			JOptionPane.showMessageDialog(null, "Data user berhasil tersimpan");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Simpan Data Gagal");
			System.out.println("error simpan data user: " + eee);
		}
	}
	
	private void UpdateDataKaryawan(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_karyawan set username = ? where id = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtUsername.getText());
			psQuery.setString(2, IdKaryawan);
			psQuery.execute();
			psQuery.close();
			conn.close();
			
		} catch (Exception eee) {
			System.out.println("error update data karyawan: " + eee);
		}
	}
}
