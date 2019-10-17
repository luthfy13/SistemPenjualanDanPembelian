import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.awt.Toolkit;

public class FEditHargaTransaksi {

	private JFrame frame;
	static FEditHargaTransaksi window;
	private JTextField txtNamaBarang;
	private JTextField txtHarga;
	static String NoFaktur=null, IdBarang=null, NamaBarang=null, satuan=null;
	static int Qty=0, harga_lama=0;
	private int selisih=0, Total=0, potongan=0;
	static float Diskon=0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FEditHargaTransaksi();
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
	public FEditHargaTransaksi() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Laporan.png"));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				txtNamaBarang.setText(NamaBarang);
				txtHarga.setText(FMain.FormatAngka(harga_lama));
				txtHarga.selectAll();
				txtHarga.requestFocus();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				FCetakFaktur.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		frame.setTitle("Edit Harga Barang");
		frame.setResizable(false);
		frame.setBounds(100, 100, 350, 129);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setBounds(10, 11, 120, 14);
		frame.getContentPane().add(lblNamaBarang);
		
		JLabel lblHargaPerSatuan = new JLabel("Harga per satuan");
		lblHargaPerSatuan.setBounds(10, 36, 120, 14);
		frame.getContentPane().add(lblHargaPerSatuan);
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.setEditable(false);
		txtNamaBarang.setBounds(140, 8, 191, 20);
		frame.getContentPane().add(txtNamaBarang);
		txtNamaBarang.setColumns(10);
		
		txtHarga = new JTextField();
		txtHarga.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c = arg0.getKeyChar();
				if (!((c >= '0') && (c <= '9') ||
				   (c == KeyEvent.VK_BACK_SPACE) ||
				   (c == KeyEvent.VK_DELETE))) {
					arg0.consume();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (! (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)){
					try {
						txtHarga.setText(FMain.FormatAngka(Integer.valueOf(txtHarga.getText().replace(",", ""))));
					} catch (Exception er) {
						if (!txtHarga.getText().equals(""))
							JOptionPane.showMessageDialog(null, "Batas maksimum = 2.147.483.647!!!");
						txtHarga.setText("");
					}
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					if (FCetakFaktur.rdbtnTransaksiTunai.isSelected()){
						UpdateHargaTunai();
					}
					else{
						UpdateHargaKredit();
					}
					FCetakFaktur.window.frame.setEnabled(true);
					window.frame.dispose();
					frame.dispose();
					window.frame = null;
					frame = null;
					window = null;
					System.gc();
				}
			}
		});
		txtHarga.setBounds(140, 33, 191, 20);
		frame.getContentPane().add(txtHarga);
		txtHarga.setColumns(10);
		
		JButton btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (Integer.valueOf(txtHarga.getText().replace(",", "")) > 0){
						if (FCetakFaktur.rdbtnTransaksiTunai.isSelected()){
							UpdateHargaTunai();
						}
						else{
							UpdateHargaKredit();
						}
						FCetakFaktur.window.frame.setEnabled(true);
						window.frame.dispose();
						frame.dispose();
						window.frame = null;
						frame = null;
						window = null;
						System.gc();
					}
					else{
						JOptionPane.showMessageDialog(null, "Masukkan data harga yang benar!!!");
						txtHarga.selectAll();
						txtHarga.requestFocus();
					}
				} catch (Exception e1) {
					System.out.println(e1);
				}
				
			}
		});
		btnSimpan.setBounds(140, 64, 89, 23);
		frame.getContentPane().add(btnSimpan);
		
		JButton btnBatal = new JButton("Batal");
		btnBatal.setMnemonic(KeyEvent.VK_X);
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FCetakFaktur.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnBatal.setBounds(242, 64, 89, 23);
		frame.getContentPane().add(btnBatal);
	}
	
	private void UpdateHargaTunai(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_detail_transaksi set harga_jual=? where no_faktur = ? and id_barang = ? and satuan = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtHarga.getText().replace(",", ""));
			psQuery.setString(2, NoFaktur);
			psQuery.setString(3, IdBarang);
			psQuery.setString(4, satuan);
			psQuery.execute();
			
			potongan = (int) (float) (harga_lama*(Diskon/100));
			harga_lama = harga_lama - potongan;
			potongan = (int) (float) (Integer.valueOf(txtHarga.getText().replace(",", ""))*(Diskon/100));
			selisih = ((Integer.valueOf(txtHarga.getText().replace(",", ""))-potongan)*Qty) - (harga_lama*Qty);
			Total = FMain.TotalBayarTransaksiTunai(NoFaktur) + selisih;

			
			query = "update tb_transaksi set total_bayar=? where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setInt(1, Total);
			psQuery.setString(2, NoFaktur);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Data transaksi berhasil diubah...");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private void UpdateHargaKredit(){
		if (FMain.TotalBayarTransaksiKredit(NoFaktur) != FMain.SisaHutang(NoFaktur)){
			JOptionPane.showMessageDialog(null, "Edit harga tidak diperbolehkan!!!\nTransaksi Kredit ini sudah dibayarkan sebagian!!!");
		}
		else{
			try {
				String MyDriver = "com.mysql.jdbc.Driver";
				String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
				Class.forName(MyDriver);
				final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
				
				String query = "update tb_detail_transaksi_kredit set harga_jual=? where no_faktur = ? and id_barang = ? and satuan = ?";
				PreparedStatement psQuery = conn.prepareStatement(query);
				psQuery.setString(1, txtHarga.getText().replace(",", ""));
				psQuery.setString(2, NoFaktur);
				psQuery.setString(3, IdBarang);
				psQuery.setString(4, satuan);
				psQuery.execute();
				
				potongan = (int) (float) (harga_lama*(Diskon/100));
				harga_lama = harga_lama - potongan;
				potongan = (int) (float) (Integer.valueOf(txtHarga.getText().replace(",", ""))*(Diskon/100));
				selisih = ((Integer.valueOf(txtHarga.getText().replace(",", ""))-potongan)*Qty) - (harga_lama*Qty);
				Total = FMain.TotalBayarTransaksiKredit(NoFaktur) + selisih;
				
				query = "update tb_transaksi_kredit set total_bayar=? where no_faktur = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setInt(1, Total);
				psQuery.setString(2, NoFaktur);
				psQuery.execute();
				
				query = "update tb_hutang set sisa_hutang=? where no_faktur = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setInt(1, Total);
				psQuery.setString(2, NoFaktur);
				psQuery.execute();
				
				psQuery.close();
				conn.close();
				
				JOptionPane.showMessageDialog(null, "Harga berhasil diubah...");
				
			} catch (Exception eee) {
				JOptionPane.showMessageDialog(null, "Edit Data kredit Gagal");
				System.out.println("error simpan data kredit: " + eee);
			}
		}
		
	}
	
}
