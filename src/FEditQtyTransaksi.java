import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class FEditQtyTransaksi {

	private JFrame frame;
	static FEditQtyTransaksi window;
	private JTextField txtNamaBarang;
	private JTextField txtQty;
	static String NoFaktur=null, IdBarang=null, NamaBarang=null, Satuan=null;
	static int Qty=0, HargaSatuan=0, TotalBayarLama=0;
	static float Diskon=0;
	private int potongan=0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FEditQtyTransaksi();
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
	public FEditQtyTransaksi() {
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
				txtNamaBarang.setText(NamaBarang);
				txtQty.setText(String.valueOf(Qty));
				txtQty.selectAll();
				txtQty.requestFocus();
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				FCetakFaktur.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Laporan.png"));
		frame.setTitle("Edit Qty Barang");
		frame.setResizable(false);
		frame.setBounds(100, 100, 350, 129);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setBounds(10, 11, 120, 14);
		frame.getContentPane().add(lblNamaBarang);
		
		JLabel lblHargaPerSatuan = new JLabel("Qty");
		lblHargaPerSatuan.setBounds(10, 36, 120, 14);
		frame.getContentPane().add(lblHargaPerSatuan);
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.setEditable(false);
		txtNamaBarang.setBounds(80, 8, 251, 20);
		frame.getContentPane().add(txtNamaBarang);
		txtNamaBarang.setColumns(10);
		
		txtQty = new JTextField();
		txtQty.setBounds(80, 33, 251, 20);
		frame.getContentPane().add(txtQty);
		txtQty.setColumns(10);
		
		JButton btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (Integer.valueOf(txtQty.getText()) > 0){
						if (FCetakFaktur.rdbtnTransaksiTunai.isSelected()){
							UpdateQtyTunai();
						}
						else{
							UpdateQtyKredit();
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
						JOptionPane.showMessageDialog(null, "Masukkan data qty yang benar!!!");
						txtQty.selectAll();
						txtQty.requestFocus();
					}
				} catch (Exception e) {
					System.out.println(e);
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
	
	private void UpdateQtyTunai(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_detail_transaksi set qty=?  where no_faktur = ? and id_barang = ? and satuan = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtQty.getText());
			psQuery.setString(2, NoFaktur);
			psQuery.setString(3, IdBarang);
			psQuery.setString(4, Satuan);
			psQuery.execute();
			
			int selisih = Integer.valueOf(txtQty.getText()) - Qty;
			potongan = (int) (float) ((HargaSatuan)*(Diskon / 100));
			int harga = selisih * (HargaSatuan-potongan);
			int Total = FMain.TotalBayarTransaksiTunai(NoFaktur) + harga;
			
			query = "update tb_transaksi set total_bayar=? where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setInt(1, Total);
			psQuery.setString(2, NoFaktur);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Data transaksi berhasil diubah...");
			FMain.UpdateStok(IdBarang, Satuan, Qty, Integer.valueOf(txtQty.getText()));
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Gagal");
			System.out.println("error simpan data: " + eee);
		}
	}
	
	private void UpdateQtyKredit(){
		if (FMain.TotalBayarTransaksiKredit(NoFaktur) != FMain.SisaHutang(NoFaktur)){
			JOptionPane.showMessageDialog(null, "Update qty tidak diperbolehkan!!!\nTransaksi Kredit ini sudah dibayarkan sebagian!!!");
		}
		else{
			try {
				String MyDriver = "com.mysql.jdbc.Driver";
				String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
				Class.forName(MyDriver);
				final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
				
				String query = "update tb_detail_transaksi_kredit set qty=?  where no_faktur = ? and id_barang = ? and satuan = ?";
				PreparedStatement psQuery = conn.prepareStatement(query);
				psQuery.setString(1, txtQty.getText());
				psQuery.setString(2, NoFaktur);
				psQuery.setString(3, IdBarang);
				psQuery.setString(4, Satuan);
				psQuery.execute();
				
				int selisih = Integer.valueOf(txtQty.getText()) - Qty;
				potongan = (int) (float) ((HargaSatuan)*(Diskon / 100));
				int harga = selisih * (HargaSatuan-potongan);
				int Total = FMain.TotalBayarTransaksiKredit(NoFaktur) + harga;
				
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
				
				JOptionPane.showMessageDialog(null, "Data transaksi kredit berhasil diubah...");
				FMain.UpdateStok(IdBarang, Satuan, Qty, Integer.valueOf(txtQty.getText()));
				
			} catch (Exception eee) {
				JOptionPane.showMessageDialog(null, "Edit Data Gagal");
				System.out.println("error simpan data: " + eee);
			}
		}
	}
	
}