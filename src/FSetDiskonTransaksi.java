import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FSetDiskonTransaksi {

	private JFrame frame;
	static FSetDiskonTransaksi window;
	private JTextField txtNamaBarang;
	private JTextField txtSatuan;
	private JTextField txtHarga;
	private JTextField txtDiskon;
	static String NoFaktur=null, IdBarang=null, NamaBarang=null, Satuan=null;
	static int Harga=0, Potongan=0, Qty=0;
	static float Diskon=0;
	private int potongan=0, selisih=0, Total=0;
	private JTextField txtDiscHarga;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FSetDiskonTransaksi();
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
	public FSetDiskonTransaksi() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
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
			@Override
			public void windowOpened(WindowEvent e) {
				txtNamaBarang.setText(NamaBarang);
				txtHarga.setText(FMain.FormatAngka(Harga));
				txtSatuan.setText(Satuan);
				txtDiskon.setText(String.valueOf(Diskon));
				txtDiskon.selectAll();
				txtDiskon.requestFocus();
				int potongan = (int) (float) (Integer.valueOf(txtHarga.getText().replace(",", ""))*(Float.valueOf(txtDiskon.getText())/100));
				txtDiscHarga.setText(FMain.FormatAngka(potongan));
			}
		});
		frame.setTitle("Set Diskon Data Transaksi");
		frame.setResizable(false);
		frame.setBounds(100, 100, 320, 178);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setBounds(10, 11, 83, 14);
		frame.getContentPane().add(lblNamaBarang);
		
		JLabel lblSatuan = new JLabel("Satuan");
		lblSatuan.setBounds(10, 36, 83, 14);
		frame.getContentPane().add(lblSatuan);
		
		JLabel lblHarga = new JLabel("Harga");
		lblHarga.setBounds(10, 61, 83, 14);
		frame.getContentPane().add(lblHarga);
		
		JLabel lblDiskon = new JLabel("Diskon");
		lblDiskon.setBounds(10, 86, 83, 14);
		frame.getContentPane().add(lblDiskon);
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.setEditable(false);
		txtNamaBarang.setBounds(103, 8, 203, 20);
		frame.getContentPane().add(txtNamaBarang);
		txtNamaBarang.setColumns(10);
		
		txtSatuan = new JTextField();
		txtSatuan.setEditable(false);
		txtSatuan.setBounds(103, 33, 203, 20);
		frame.getContentPane().add(txtSatuan);
		txtSatuan.setColumns(10);
		
		txtHarga = new JTextField();
		txtHarga.setEditable(false);
		txtHarga.setBounds(103, 58, 203, 20);
		frame.getContentPane().add(txtHarga);
		txtHarga.setColumns(10);
		
		txtDiskon = new JTextField();
		txtDiskon.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (txtDiskon.getText().equals(""))
					txtDiscHarga.setText("");
				else
					try {
						int potongan = (int) (float) (Integer.valueOf(txtHarga.getText().replace(",", ""))*(Float.valueOf(txtDiskon.getText())/100));
						txtDiscHarga.setText(FMain.FormatAngka(potongan));
					} catch (Exception e) {}
				
			}
		});
		txtDiskon.setBounds(103, 83, 40, 20);
		frame.getContentPane().add(txtDiskon);
		txtDiskon.setColumns(10);
//		txtDiskon.getDocument().addDocumentListener(new DocumentListener() {
//			
//			@Override
//			public void removeUpdate(DocumentEvent arg0) {
//				EventChange();
//			}
//			
//			@Override
//			public void insertUpdate(DocumentEvent arg0) {
//				EventChange();
//			}
//			
//			@Override
//			public void changedUpdate(DocumentEvent arg0) {
//				EventChange();
//			}
//			
//			private void EventChange(){
//				try {
//					int potongan = (int) (float) (Integer.valueOf(txtHarga.getText().replace(",", ""))*(Float.valueOf(txtDiskon.getText())/100));
//					txtDiscHarga.setText(String.valueOf(potongan));
//				} catch (Exception e) {}
//			}
//		});
		
		JLabel label = new JLabel("%");
		label.setBounds(147, 86, 11, 14);
		frame.getContentPane().add(label);
		
		JButton btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (Float.valueOf(txtDiskon.getText()) >= 0){
						if (FCetakFaktur.rdbtnTransaksiTunai.isSelected()){
							UpdateDiskonTunai();
						}
						else{
							UpdateDiskonKredit();
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
				} catch (Exception e1) {System.out.println("error simpan data: " + e1);}
			}
		});
		btnSimpan.setBounds(223, 114, 83, 23);
		frame.getContentPane().add(btnSimpan);
		
		txtDiscHarga = new JTextField();
		txtDiscHarga.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				if (! (arg0.getKeyCode() == KeyEvent.VK_LEFT || arg0.getKeyCode() == KeyEvent.VK_RIGHT)){
					try {
						txtDiscHarga.setText(FMain.FormatAngka(Integer.valueOf(txtDiscHarga.getText().replace(",", ""))));
					} catch (Exception er) {
						if (!txtDiscHarga.getText().equals(""))
							JOptionPane.showMessageDialog(null, "Batas maksimum = 2.147.483.647!!!");
						txtDiscHarga.setText("");
					}
				}
				
				if (txtDiscHarga.getText().equals(""))
					txtDiskon.setText("");
				else
					try {
						float persen = ((float)Integer.valueOf(txtDiscHarga.getText().replace(",", ""))/(float)Integer.valueOf(txtHarga.getText().replace(",", ""))*100);
						//(int) (float) (Integer.valueOf(txtHarga.getText().replace(",", ""))*(Float.valueOf(txtDiskon.getText())/100));
						txtDiskon.setText(String.valueOf(persen));
					} catch (Exception e) {}
			}
		});
		txtDiscHarga.setBounds(185, 83, 121, 20);
		frame.getContentPane().add(txtDiscHarga);
		txtDiscHarga.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(170, 86, 16, 14);
		frame.getContentPane().add(separator);
	}
	
	private void UpdateDiskonTunai(){
		try {
			if (Diskon > 0){
				UpdateDiskonTunaiX();
			}
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_detail_transaksi set diskon=? where no_faktur = ? and id_barang = ? and satuan = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtDiskon.getText());
			psQuery.setString(2, NoFaktur);
			psQuery.setString(3, IdBarang);
			psQuery.setString(4, Satuan);
			psQuery.execute();
			
			//potongan = (int) (float) (Harga*(Diskon/100));
			//Harga = Harga - potongan;
			potongan = (int) (float) (Integer.valueOf(txtHarga.getText().replace(",", ""))*(Float.valueOf(txtDiskon.getText())/100));
			selisih = ((Integer.valueOf(txtHarga.getText().replace(",", ""))-potongan)*Qty) - (Integer.valueOf(txtHarga.getText().replace(",", ""))*Qty);
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
			System.out.println("error simpan data TUNAI: " + eee);
		}
	}
	
	private void UpdateDiskonTunaiX(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query = "update tb_detail_transaksi set diskon=? where no_faktur = ? and id_barang = ? and satuan = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtDiskon.getText());
			psQuery.setString(2, NoFaktur);
			psQuery.setString(3, IdBarang);
			psQuery.setString(4, Satuan);
			psQuery.execute();
			
			potongan = (int) (float) (Harga*(Diskon/100));
			Harga = Harga - potongan;
			potongan = (int) (float) (Harga*(Float.valueOf("0")/100));
			selisih = ((Integer.valueOf(txtHarga.getText().replace(",", ""))-potongan)*Qty) - (Harga*Qty);
			Total = FMain.TotalBayarTransaksiTunai(NoFaktur) + selisih;

			
			query = "update tb_transaksi set total_bayar=? where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setInt(1, Total);
			psQuery.setString(2, NoFaktur);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			//JOptionPane.showMessageDialog(null, "Data transaksi X berhasil diubah...");
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Gagal");
			System.out.println("error simpan data TUNAI: " + eee);
		}
	}
	
	private void UpdateDiskonKredit(){
		if (Diskon > 0){
			UpdateDiskonKreditX();
		}
		
		if (FMain.TotalBayarTransaksiKredit(NoFaktur) != FMain.SisaHutang(NoFaktur)){
			JOptionPane.showMessageDialog(null, "Edit harga tidak diperbolehkan!!!\nTransaksi Kredit ini sudah dibayarkan sebagian!!!");
		}
		else{
			try {
				String MyDriver = "com.mysql.jdbc.Driver";
				String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
				Class.forName(MyDriver);
				final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
				
				String query = "update tb_detail_transaksi_kredit set diskon=? where no_faktur = ? and id_barang = ? and satuan = ?";
				PreparedStatement psQuery = conn.prepareStatement(query);
				psQuery.setFloat(1, Float.valueOf(txtDiskon.getText()));
				psQuery.setString(2, NoFaktur);
				psQuery.setString(3, IdBarang);
				psQuery.setString(4, Satuan);
				psQuery.execute();
				
				potongan = (int) (float) (Integer.valueOf(txtHarga.getText().replace(",", ""))*(Float.valueOf(txtDiskon.getText())/100));
				selisih = ((Integer.valueOf(txtHarga.getText().replace(",", ""))-potongan)*Qty) - (Integer.valueOf(txtHarga.getText().replace(",", ""))*Qty);
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
				
				JOptionPane.showMessageDialog(null, "Data transaksi kredit berhasil diubah...");
				
			} catch (Exception eee) {
				JOptionPane.showMessageDialog(null, "Edit Data Gagal");
				System.out.println("error simpan data KREDIT: " + eee);
			}
		}
	}
	
	private void UpdateDiskonKreditX(){
		if (FMain.TotalBayarTransaksiKredit(NoFaktur) != FMain.SisaHutang(NoFaktur)){
			JOptionPane.showMessageDialog(null, "Edit harga tidak diperbolehkan!!!\nTransaksi Kredit ini sudah dibayarkan sebagian!!!");
		}
		else{
			try {
				String MyDriver = "com.mysql.jdbc.Driver";
				String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
				Class.forName(MyDriver);
				final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
				
				String query = "update tb_detail_transaksi_kredit set diskon=? where no_faktur = ? and id_barang = ? and satuan = ?";
				PreparedStatement psQuery = conn.prepareStatement(query);
				psQuery.setFloat(1, Float.valueOf(txtDiskon.getText()));
				psQuery.setString(2, NoFaktur);
				psQuery.setString(3, IdBarang);
				psQuery.setString(4, Satuan);
				psQuery.execute();
				
				potongan = (int) (float) (Harga*(Diskon/100));
				Harga = Harga - potongan;
				potongan = (int) (float) (Harga*(Float.valueOf("0")/100));
				selisih = ((Integer.valueOf(txtHarga.getText().replace(",", ""))-potongan)*Qty) - (Harga*Qty);
				Total = FMain.TotalBayarTransaksiKredit(NoFaktur) + selisih;

				
				query = "update tb_transaksi_kredit set total_bayar=? where no_faktur = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setInt(1, Total);
				psQuery.setString(2, NoFaktur);
				psQuery.execute();
				
				psQuery.close();
				conn.close();
				
				//JOptionPane.showMessageDialog(null, "Data transaksi kredit berhasil diubah...");
				
			} catch (Exception eee) {
				JOptionPane.showMessageDialog(null, "Edit Data Gagal");
				System.out.println("error simpan data KREDIT: " + eee);
			}
		}
	}
}
