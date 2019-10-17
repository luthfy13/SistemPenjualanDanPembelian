import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.awt.Toolkit;


public class FTambahStok {

	private JFrame frame;
	static FTambahStok window;
	private JTextField txtIdBarang;
	private JTextField txtNamaBarang;
	private JTextField txtCurStok;
	private JTextField txtTambahanStok;
	private JTextField txtStokBaru;
	static String IdBarang = null;
	static String NamaBarang = null;
	static String Satuan = null;
	static int Stok = 0;
	private JLabel Label1;
	private JLabel Label2;
	private JLabel Label3;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FTambahStok();
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
	public FTambahStok() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Barang.png"));
		frame.setTitle("Tambah Stok");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				txtIdBarang.setText(IdBarang);
				txtNamaBarang.setText(NamaBarang);
				txtCurStok.setText(String.valueOf(Stok));
				Label1.setText(Satuan);
				Label2.setText(Satuan);
				Label3.setText(Satuan);
				txtTambahanStok.requestFocus();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				FStokBarang.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 378, 195);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ID Barang");
		lblNewLabel.setBounds(10, 11, 157, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNamaBarang = new JLabel(" Nama Barang");
		lblNamaBarang.setBounds(10, 36, 157, 14);
		frame.getContentPane().add(lblNamaBarang);
		
		JLabel lblStokSaatIni = new JLabel("Stok Saat Ini");
		lblStokSaatIni.setBounds(10, 61, 157, 14);
		frame.getContentPane().add(lblStokSaatIni);
		
		JLabel lblStokYangAkan = new JLabel("Stok yang Akan Ditambahkan");
		lblStokYangAkan.setBounds(10, 86, 157, 14);
		frame.getContentPane().add(lblStokYangAkan);
		
		JLabel lblStokSetelahDitambahkan = new JLabel("Stok Setelah Ditambahkan");
		lblStokSetelahDitambahkan.setBounds(10, 111, 157, 14);
		frame.getContentPane().add(lblStokSetelahDitambahkan);
		
		txtIdBarang = new JTextField();
		txtIdBarang.setEditable(false);
		txtIdBarang.setBounds(177, 8, 183, 20);
		frame.getContentPane().add(txtIdBarang);
		txtIdBarang.setColumns(10);
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.setEditable(false);
		txtNamaBarang.setBounds(177, 33, 183, 20);
		frame.getContentPane().add(txtNamaBarang);
		txtNamaBarang.setColumns(10);
		
		txtCurStok = new JTextField();
		txtCurStok.setEditable(false);
		txtCurStok.setBounds(177, 58, 86, 20);
		frame.getContentPane().add(txtCurStok);
		txtCurStok.setColumns(10);
		
		txtTambahanStok = new JTextField();
		txtTambahanStok.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				 EventChange();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				 EventChange();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				 EventChange();
			}
			private void EventChange(){
				if (! txtTambahanStok.getText().equals("")){
					int StokTambahan,StokAkhir;
					StokTambahan = Integer.valueOf(txtTambahanStok.getText());
					StokAkhir = Stok + StokTambahan;
					txtStokBaru.setText(String.valueOf(StokAkhir));
				}
				else if (txtTambahanStok.getText().equals("")){
					txtStokBaru.setText("");
				}
			}
		});
		txtTambahanStok.setBounds(177, 83, 86, 20);
		frame.getContentPane().add(txtTambahanStok);
		txtTambahanStok.setColumns(10);
		
		txtStokBaru = new JTextField();
		txtStokBaru.setEditable(false);
		txtStokBaru.setBounds(177, 108, 86, 20);
		frame.getContentPane().add(txtStokBaru);
		txtStokBaru.setColumns(10);
		
		JButton btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UpdateStok();
				FStokBarang.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnSimpan.setBounds(10, 136, 89, 23);
		frame.getContentPane().add(btnSimpan);
		
		JButton btnBatal = new JButton("Batal");
		btnBatal.setMnemonic(KeyEvent.VK_X);
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FStokBarang.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnBatal.setBounds(109, 136, 89, 23);
		frame.getContentPane().add(btnBatal);
		
		Label1 = new JLabel("New label");
		Label1.setBounds(273, 61, 46, 14);
		frame.getContentPane().add(Label1);
		
		Label2 = new JLabel("New label");
		Label2.setBounds(273, 86, 46, 14);
		frame.getContentPane().add(Label2);
		
		Label3 = new JLabel("New label");
		Label3.setBounds(273, 111, 46, 14);
		frame.getContentPane().add(Label3);
	}
	
	private void UpdateStok(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			PreparedStatement psQuery = null;
			String query = "update tb_detail_barang set stok = ? where id_barang = ? and satuan = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setInt(1, Integer.valueOf(txtStokBaru.getText()));
			psQuery.setString(2, txtIdBarang.getText());
			psQuery.setString(3, Label1.getText());
			psQuery.execute();
				
			psQuery.close();
			conn.close();
			JOptionPane.showMessageDialog(null, "Data stok berhasil tersimpan...");
		}
		catch(Exception e){System.out.println("Ada error bro simpan ubah stok satuan asal" + e);}
	}
	
}
