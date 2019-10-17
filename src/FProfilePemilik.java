import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FProfilePemilik {

	private JFrame frame;
	static FProfilePemilik window;
	private JTextField txtNama;
	private JTextField txtAlamat;
	private JTextField txtTelp;
	private JTextField txtNPWP;
	private JButton btnEdit, btnSimpan, btnBatal, btnTutup;
	private JTextField txtPimpinan;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FProfilePemilik();
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
	public FProfilePemilik() {
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
				txtNama.setEditable(false);
				txtAlamat.setEditable(false);
				txtTelp.setEditable(false);
				txtNPWP.setEditable(false);
				txtPimpinan.setEditable(false);
				btnEdit.setEnabled(true);
				btnBatal.setEnabled(false);
				btnSimpan.setEnabled(false);
				btnTutup.setEnabled(true);
				TampilData();
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
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Owner.png"));
		frame.setTitle("Profile Perusahaan / Toko");
		frame.setResizable(false);
		frame.setBounds(100, 100, 420, 215);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Nama Perusahaan / Toko");
		lblNewLabel.setBounds(10, 11, 149, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblAlamat = new JLabel("Alamat");
		lblAlamat.setBounds(10, 67, 46, 14);
		frame.getContentPane().add(lblAlamat);
		
		JLabel lblTelp = new JLabel("Telp");
		lblTelp.setBounds(10, 95, 46, 14);
		frame.getContentPane().add(lblTelp);
		
		JLabel lblNpwp = new JLabel("NPWP");
		lblNpwp.setBounds(10, 123, 46, 14);
		frame.getContentPane().add(lblNpwp);
		
		JLabel lblNamaPemilik = new JLabel("Nama Pemilik / Pimpinan");
		lblNamaPemilik.setBounds(10, 39, 128, 14);
		frame.getContentPane().add(lblNamaPemilik);
		
		txtNama = new JTextField();
		txtNama.setBounds(146, 8, 250, 20);
		frame.getContentPane().add(txtNama);
		txtNama.setColumns(10);
		
		txtAlamat = new JTextField();
		txtAlamat.setBounds(146, 64, 250, 20);
		frame.getContentPane().add(txtAlamat);
		txtAlamat.setColumns(10);
		
		txtTelp = new JTextField();
		txtTelp.setBounds(146, 92, 250, 20);
		frame.getContentPane().add(txtTelp);
		txtTelp.setColumns(10);
		
		txtNPWP = new JTextField();
		txtNPWP.setBounds(146, 120, 250, 20);
		frame.getContentPane().add(txtNPWP);
		txtNPWP.setColumns(10);
		
		btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtNama.setEditable(true);
				txtAlamat.setEditable(true);
				txtTelp.setEditable(true);
				txtNPWP.setEditable(true);
				txtPimpinan.setEditable(true);
				btnEdit.setEnabled(false);
				btnBatal.setEnabled(true);
				btnSimpan.setEnabled(true);
				btnTutup.setEnabled(false);
				txtNama.selectAll();
				txtNama.requestFocus();
			}
		});
		
		txtPimpinan = new JTextField();
		txtPimpinan.setBounds(146, 36, 250, 20);
		frame.getContentPane().add(txtPimpinan);
		txtPimpinan.setColumns(10);
		btnEdit.setBounds(10, 148, 89, 23);
		frame.getContentPane().add(btnEdit);
		
		btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditData();
				txtNama.setEditable(false);
				txtAlamat.setEditable(false);
				txtTelp.setEditable(false);
				txtNPWP.setEditable(false);
				btnEdit.setEnabled(true);
				btnBatal.setEnabled(false);
				btnSimpan.setEnabled(false);
				btnTutup.setEnabled(true);
				TampilData();
			}
		});
		btnSimpan.setBounds(109, 148, 89, 23);
		frame.getContentPane().add(btnSimpan);
		
		btnBatal = new JButton("Batal");
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtNama.setEditable(false);
				txtAlamat.setEditable(false);
				txtTelp.setEditable(false);
				txtNPWP.setEditable(false);
				txtPimpinan.setEditable(false);
				btnEdit.setEnabled(true);
				btnBatal.setEnabled(false);
				btnSimpan.setEnabled(false);
				btnTutup.setEnabled(true);
				TampilData();
			}
		});
		btnBatal.setBounds(208, 148, 89, 23);
		frame.getContentPane().add(btnBatal);
		
		btnTutup = new JButton("Tutup");
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
		btnTutup.setBounds(307, 148, 89, 23);
		frame.getContentPane().add(btnTutup);
	}
	
	private void TampilData(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query;
			Statement st;
			ResultSet rs;
			query = "select * from tb_properties";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				txtNama.setText(rs.getString("nama_toko"));
				txtAlamat.setText(rs.getString("alamat"));
				txtTelp.setText(rs.getString("telp"));
				txtNPWP.setText(rs.getString("npwp"));
				txtPimpinan.setText(rs.getString("nama_pimpinan"));
			};	
			
			st.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data properties: " + e);
		}
	}
	
	private void EditData(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_properties set nama_toko=?, alamat=?, telp=?, npwp=?, nama_pimpinan = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtNama.getText());
			psQuery.setString(2, txtAlamat.getText());
			psQuery.setString(3, txtTelp.getText());
			psQuery.setString(4, txtNPWP.getText());
			psQuery.setString(5, txtPimpinan.getText());
			psQuery.execute();
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Data berhasil diedit...");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data properties Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
}
