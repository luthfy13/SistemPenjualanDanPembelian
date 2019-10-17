import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class FBayar {

	private JFrame frame;
	static FBayar window;
	private JTextField txtTgl;
	private JTextField txtJmlBayar;
	private JButton btnBayar;
	public static String NoFaktur,SisaHutang;
	private JComboBox<String> cmbSales;
	private JCheckBox chckbxSales;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FBayar();
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
	public FBayar() {
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
				DateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
				Date date = new Date();
				String tgl;
				tgl = dateFormat.format(date);
				txtTgl.setText(tgl);
				txtJmlBayar.setText(FMain.FormatAngka(Integer.valueOf(SisaHutang)));
				txtJmlBayar.selectAll();
				txtJmlBayar.requestFocus();
				TampilSales();
				cmbSales.setVisible(false);
			}
			@Override
			public void windowClosing(WindowEvent e) {
				FPembayaran.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 316, 163);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTglBayar = new JLabel("Tgl Bayar");
		lblTglBayar.setBounds(10, 11, 84, 14);
		frame.getContentPane().add(lblTglBayar);
		
		JLabel lblJumlahBayar = new JLabel("Jumlah Bayar");
		lblJumlahBayar.setBounds(10, 36, 84, 14);
		frame.getContentPane().add(lblJumlahBayar);
		
		txtTgl = new JTextField();
		txtTgl.setBackground(Color.WHITE);
		txtTgl.setEditable(false);
		txtTgl.setBounds(104, 8, 196, 20);
		frame.getContentPane().add(txtTgl);
		txtTgl.setColumns(10);
		
		txtJmlBayar = new JTextField();
		txtJmlBayar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (! (arg0.getKeyCode() == KeyEvent.VK_LEFT || arg0.getKeyCode() == KeyEvent.VK_RIGHT)){
					try {
						txtJmlBayar.setText(FMain.FormatAngka(Integer.valueOf(txtJmlBayar.getText().replace(",", ""))));
					} catch (Exception e) {
						if (!txtJmlBayar.getText().equals(""))
							JOptionPane.showMessageDialog(null, "Batas maksimum = 2.147.483.647!!!");
						txtJmlBayar.setText("");
					}
				}
				
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c = arg0.getKeyChar();
				if (!((c >= '0') && (c <= '9') ||
				   (c == KeyEvent.VK_BACK_SPACE) ||
				   (c == KeyEvent.VK_DELETE))) {
					arg0.consume();
				}
			}
		});
		txtJmlBayar.setBounds(104, 33, 196, 20);
		frame.getContentPane().add(txtJmlBayar);
		txtJmlBayar.setColumns(10);
		
		btnBayar = new JButton("Bayar");
		btnBayar.setBounds(114, 100, 89, 23);
		btnBayar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtJmlBayar.getText().equals("") || txtJmlBayar.getText().equals("0")){
					JOptionPane.showMessageDialog(null, "Jumlah bayar tidak boleh kosong!!!");
					txtJmlBayar.selectAll();
					txtJmlBayar.requestFocus();
				}
				else{
					if (Integer.valueOf(SisaHutang) >= Integer.valueOf(txtJmlBayar.getText().replace(",", ""))){
						BayarKredit();
						FPembayaran.window.frame.setEnabled(true);
						window.frame.dispose();
						frame.dispose();
					}
					else{
						JOptionPane.showMessageDialog(btnBayar, "Jumlah bayar harus\nlebih kecil atau sama dengan\nSisa hutang!!!");
						txtJmlBayar.selectAll();
						txtJmlBayar.requestFocus();
					}
				}
			}
		});
		frame.getContentPane().add(btnBayar);
		
		JButton btnBatal = new JButton("Batal");
		btnBatal.setMnemonic(KeyEvent.VK_X);
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FPembayaran.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnBatal.setBounds(211, 100, 89, 23);
		frame.getContentPane().add(btnBatal);
		
		chckbxSales = new JCheckBox("Sales");
		chckbxSales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxSales.isSelected()) cmbSales.setVisible(true);
				else cmbSales.setVisible(false);
			}
		});
		chckbxSales.setBounds(10, 57, 84, 23);
		frame.getContentPane().add(chckbxSales);
		
		cmbSales = new JComboBox<String>();
		cmbSales.setBounds(104, 58, 196, 20);
		frame.getContentPane().add(cmbSales);
	}
	
	private void BayarKredit(){
		SimpanTabelDetailHutang();
		UpdateTabelHutang();
		JOptionPane.showMessageDialog(btnBayar, "Data Berhasil Tersimpan...");
	}
	
	private void SimpanTabelDetailHutang(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String tgl;
			tgl = dateFormat.format(date);
			
			PreparedStatement psQuery=null;
			String query = "insert into tb_detail_hutang (no_faktur,tgl_bayar,jml_bayar,id_karyawan) values (?,?,?,?)";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.setString(2, tgl);
			psQuery.setString(3, txtJmlBayar.getText().replace(",", ""));
			psQuery.setString(4, IdKaryawan());
			psQuery.execute();
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan detail hutang" + e);}
	}
	
	private void UpdateTabelHutang(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			PreparedStatement psQuery=null;
			String query = "update tb_hutang set sisa_hutang = ? where no_faktur = ?";
			
			psQuery = conn.prepareStatement(query);
			psQuery.setInt(1, SisaHutang() - Integer.valueOf(txtJmlBayar.getText().replace(",", "")));
			psQuery.setString(2, NoFaktur);
			psQuery.execute();
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan tabel hutang" + e);}
	}
	
	private int SisaHutang(){
		int sisa=0;
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT sisa_hutang from tb_hutang where no_faktur = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,NoFaktur);
			psQuery.execute();
			rs = psQuery.getResultSet();
			while (rs.next()){
				sisa = rs.getInt("sisa_hutang");
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error tampil data: " + e);
		}
		return sisa;
	}
	
	private void TampilSales(){
		cmbSales.removeAllItems();
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query;
			Statement st;
			ResultSet rs;
			query = "select id, nama from tb_karyawan where bagian='sales'";
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next()){
				cmbSales.addItem(rs.getString("id") + " - " + rs.getString("nama"));
			}
			
			st.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro set diskon " + e);}
	}
	
	private String IdKaryawan(){
		if (chckbxSales.isSelected())
			return cmbSales.getSelectedItem().toString().substring(0, cmbSales.getSelectedItem().toString().indexOf(" "));
		else
			return FMain.id_karyawan;
	}
}
