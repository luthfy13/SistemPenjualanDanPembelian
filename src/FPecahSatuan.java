import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FPecahSatuan {

	JFrame frame;
	static FPecahSatuan window;
	private JTextField txtIdBarang;
	private JTextField txtNamaBarang;
	private JTextField txtJmlSatuan;
	private JLabel lblSatuan;
	static String IdBarang, NamaBarang;
	private JTextField txtHargaJual;
	private JLabel lblSatuanIsi;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FPecahSatuan();
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
	public FPecahSatuan() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Pecah Satuan Barang");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				txtIdBarang.setText(IdBarang);
				txtNamaBarang.setText(NamaBarang);
				lblSatuan.setText(FStokBarang.tblDetailBarang.getValueAt(FStokBarang.tblDetailBarang.getSelectedRow(), 0).toString());
				lblSatuanIsi.setText("/ " + AmbilSatuanIsi(IdBarang, lblSatuan.getText()));
				String SatuanAsal = FStokBarang.tblDetailBarang.getValueAt(FStokBarang.tblDetailBarang.getSelectedRow(), 0).toString();
				String Pesan = "<html> Data satuan isi belum ada, <br> Pilih tombol <b> Edit Satuan </b> pada Tabel Detail Barang! </html>";
				JLabel label1 = new JLabel(Pesan);
		        label1.setFont(new Font("Arial", Font.PLAIN, 14));
				if (AmbilSatuanIsi(IdBarang, SatuanAsal).equals("-")){
					JOptionPane.showMessageDialog(null, label1);
					FStokBarang.window.frame.setEnabled(true);
					window.frame.dispose();
					frame.dispose();
					window.frame = null;
					frame = null;
					window = null;
					System.gc();
				}
				
				if(SatuanSudahAda(IdBarang, AmbilSatuanIsi(IdBarang, SatuanAsal)) == true){
					try {
						String MyDriver = "com.mysql.jdbc.Driver";
						String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
						Class.forName(MyDriver);
						final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
												
						String query=null;
						ResultSet rs;

						query = "SELECT harga_jual from tb_detail_barang where id_barang = ? and satuan = ?";

						PreparedStatement psQuery = conn.prepareStatement(query);
						psQuery.setString(1,IdBarang);
						psQuery.setString(2, AmbilSatuanIsi(IdBarang, lblSatuan.getText()));
						psQuery.execute();
						rs = psQuery.getResultSet();
						
						while (rs.next()){
							txtHargaJual.setText(FMain.FormatAngka(rs.getInt("harga_jual")));
						};	
						
						psQuery.close();
						rs.close();
						conn.close();
					}
					catch (Exception e) {
						System.out.println(e);
					}
					txtHargaJual.setEditable(false);
				}
				else
					txtHargaJual.setText("0");
				txtJmlSatuan.requestFocus();
				label1 = null;
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
		frame.setBounds(100, 100, 302, 193);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ID Barang");
		lblNewLabel.setBounds(10, 11, 100, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setBounds(10, 36, 100, 14);
		frame.getContentPane().add(lblNamaBarang);
		
		JLabel lblJumlahSatuan = new JLabel("Jumlah satuan");
		lblJumlahSatuan.setBounds(10, 61, 100, 14);
		frame.getContentPane().add(lblJumlahSatuan);
		
		txtIdBarang = new JTextField();
		txtIdBarang.setEditable(false);
		txtIdBarang.setBounds(103, 8, 183, 20);
		frame.getContentPane().add(txtIdBarang);
		txtIdBarang.setColumns(10);
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.setEditable(false);
		txtNamaBarang.setBounds(103, 33, 183, 20);
		frame.getContentPane().add(txtNamaBarang);
		txtNamaBarang.setColumns(10);
		
		txtJmlSatuan = new JTextField();
		txtJmlSatuan.addKeyListener(new KeyAdapter() {
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
		txtJmlSatuan.setBounds(103, 58, 53, 20);
		frame.getContentPane().add(txtJmlSatuan);
		txtJmlSatuan.setColumns(10);
		
		lblSatuan = new JLabel("satuan");
		lblSatuan.setBounds(166, 61, 46, 14);
		frame.getContentPane().add(lblSatuan);
		
		JButton btnPecahSatuan = new JButton("Pecah Satuan");
		btnPecahSatuan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String teks;
				String SatuanAsal;
				SatuanAsal = FStokBarang.tblDetailBarang.getValueAt(FStokBarang.tblDetailBarang.getSelectedRow(), 0).toString();
				int HasilPecah;
				HasilPecah = AmbilIsiSatuanIsi(IdBarang, SatuanAsal) * Integer.valueOf(txtJmlSatuan.getText());
				teks = "Apakah Anda ingin memecah " + txtJmlSatuan.getText() + " " + SatuanAsal +
						"\nmenjadi " + String.valueOf(HasilPecah) + " " + AmbilSatuanIsi(IdBarang, SatuanAsal) + "?";
				int konfir;
				konfir = JOptionPane.showConfirmDialog(null, teks, "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (konfir == JOptionPane.YES_OPTION){
					UpdateStokSatuanAsal(IdBarang, SatuanAsal);
					SimpanStokSatuanBaru();
					FStokBarang.window.frame.setEnabled(true);
					window.frame.dispose();
					frame.dispose();
					window.frame = null;
					frame = null;
					window = null;
					System.gc();
				}	
			}
		});
		btnPecahSatuan.setBounds(10, 131, 111, 23);
		frame.getContentPane().add(btnPecahSatuan);
		
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
		btnBatal.setBounds(131, 131, 111, 23);
		frame.getContentPane().add(btnBatal);
		
		JLabel lblHargaJual = new JLabel("Harga Jual");
		lblHargaJual.setBounds(10, 86, 100, 14);
		frame.getContentPane().add(lblHargaJual);
		
		txtHargaJual = new JTextField();
		txtHargaJual.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
		          if (!((c >= '0') && (c <= '9') ||
		             (c == KeyEvent.VK_BACK_SPACE) ||
		             (c == KeyEvent.VK_DELETE))) {
		            e.consume();
		          }
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (! txtHargaJual.getText().equals("")){
					txtHargaJual.setText(txtHargaJual.getText().replace(",", ""));
					NumberFormat numberFormatter = new DecimalFormat("#,##0");
					int angka = Integer.valueOf(txtHargaJual.getText());
					String formattedNumber = numberFormatter.format(angka);
					txtHargaJual.setText(formattedNumber);
				}
			}
		});
		txtHargaJual.setBounds(103, 83, 100, 20);
		frame.getContentPane().add(txtHargaJual);
		txtHargaJual.setColumns(10);
		
		lblSatuanIsi = new JLabel("New label");
		lblSatuanIsi.setBounds(213, 86, 46, 14);
		frame.getContentPane().add(lblSatuanIsi);
	}
	
	private int AmbilIsiSatuanIsi(String Id, String Satuan){
		int isi=0;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;

			query = "SELECT isi from tb_detail_barang where id_barang = ? and satuan = ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,Id);
			psQuery.setString(2,Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();

			while (rs.next()) isi = rs.getInt("isi");
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro ambil isi satuan " + e);}
		return isi;
	}
	
	private String AmbilSatuanIsi(String Id, String Satuan){
		String SatuanIsi="";
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;

			query = "SELECT satuan_isi from tb_detail_barang where id_barang = ? and satuan = ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,Id);
			psQuery.setString(2,Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();

			while (rs.next()) SatuanIsi = rs.getString("satuan_isi");
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro ambil satuan_isi " + e);}
		return SatuanIsi;
	}
	
	boolean SatuanSudahAda(String Id, String Satuan){
		byte jmldata=0;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;

			query = "SELECT * from tb_detail_barang where id_barang = ? and satuan = ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,Id);
			psQuery.setString(2,Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();
		
			while (rs.next()) jmldata++;
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro ambil satuan_isi " + e);}
		if (jmldata == 0)
			return false;
		else
			return true;
	}
	
	private void UpdateStokSatuanAsal(String Id, String Satuan){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			int Sisa = 0;
			String JmlStok = FStokBarang.tblDetailBarang.getValueAt(FStokBarang.tblDetailBarang.getSelectedRow(), 1).toString();
			PreparedStatement psQuery = null;
			Sisa = Integer.valueOf(JmlStok) - Integer.valueOf(txtJmlSatuan.getText());
			String query = "update tb_detail_barang set stok = ? where id_barang = ? and satuan = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setInt(1, Sisa);
			psQuery.setString(2, Id);
			psQuery.setString(3, Satuan);
			psQuery.execute();
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan ubah stok satuan asal" + e);}
	}
	
	int AmbilStokSatuan(String Id, String Satuan){
		int Stok=0;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;

			query = "SELECT stok from tb_detail_barang where id_barang = ? and satuan = ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,Id);
			psQuery.setString(2,Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();

			while (rs.next()) Stok = rs.getInt("stok");
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro ambil isi satuan " + e);}
		return Stok;
	}
	
	private void SimpanStokSatuanBaru(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			PreparedStatement psQuery=null;
			String SatuanAsal = FStokBarang.tblDetailBarang.getValueAt(FStokBarang.tblDetailBarang.getSelectedRow(), 0).toString();
			int HasilPecah = AmbilIsiSatuanIsi(IdBarang, SatuanAsal) * Integer.valueOf(txtJmlSatuan.getText());
			if(SatuanSudahAda(IdBarang, AmbilSatuanIsi(IdBarang, SatuanAsal)) == false){
				String query = "insert into tb_detail_barang values (?,?,?,?,?,?)";
				psQuery = conn.prepareStatement(query);
				psQuery.setString(1, IdBarang);
				psQuery.setString(2, AmbilSatuanIsi(IdBarang, SatuanAsal));
				psQuery.setInt(3, HasilPecah);
				psQuery.setString(4, txtHargaJual.getText().replace(",", ""));
				psQuery.setString(5, "0");
				psQuery.setString(6, "-");
			}
			else{
				int Sisa = AmbilStokSatuan(IdBarang, AmbilSatuanIsi(IdBarang, SatuanAsal)) + HasilPecah;
				String query = "update tb_detail_barang set stok = ? where id_barang = ? and satuan = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setInt(1, Sisa);
				psQuery.setString(2, IdBarang);
				psQuery.setString(3, AmbilSatuanIsi(IdBarang, SatuanAsal));
			}
			
			
			psQuery.execute();
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan stok satuan baru " + e);}
	}
}
