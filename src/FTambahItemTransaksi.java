import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class FTambahItemTransaksi {

	private JFrame frame;
	static FTambahItemTransaksi window;
	private JTextField txtNoFaktur;
	private JTextField txtCari;
	private JTextField txtStok;
	private JTextField txtHarga;
	private JTextField txtQty;
	static String NoFaktur=null;
	private JScrollPane scrollPane;
	private JList<String> list;
	private JComboBox<String> cmbSatuan;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FTambahItemTransaksi();
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
	public FTambahItemTransaksi() {
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
				txtNoFaktur.setText(NoFaktur);
				txtCari.requestFocus();
			}
		});
		frame.setTitle("Tambah Item");
		frame.setResizable(false);
		frame.setBounds(100, 100, 397, 226);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setVisible(false);
		scrollPane.setBounds(131, 52, 249, 131);
		frame.getContentPane().add(scrollPane);
		
		list = new JList<String>();
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					txtCari.setText(list.getSelectedValue().toString());
					txtHarga.setText("");
					txtStok.setText("");
					TampilSatuan(IdBarangDariTxtCari());
					cmbSatuan.requestFocus();
				}
				if (arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE){
					txtCari.requestFocus();
				}
			}
		});
		scrollPane.setViewportView(list);
		
		JLabel lblNoFaktur = new JLabel("No. Faktur");
		lblNoFaktur.setBounds(10, 11, 111, 14);
		frame.getContentPane().add(lblNoFaktur);
		
		txtNoFaktur = new JTextField();
		txtNoFaktur.setEditable(false);
		txtNoFaktur.setBounds(131, 8, 249, 20);
		frame.getContentPane().add(txtNoFaktur);
		txtNoFaktur.setColumns(10);
		
		JLabel lblKodeNama = new JLabel("Kode / Nama Barang");
		lblKodeNama.setBounds(10, 36, 111, 14);
		frame.getContentPane().add(lblKodeNama);
		
		txtCari = new JTextField();
		txtCari.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				EventChange();
			}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				EventChange();
			}
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				EventChange();
			}

			public void EventChange(){
				CariData(txtCari.getText());
			}
		});
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_DOWN){
					list.requestFocus();
					list.setSelectedIndex(0);
				}
			}
		});
		txtCari.setBounds(131, 33, 249, 20);
		frame.getContentPane().add(txtCari);
		txtCari.setColumns(10);
		
		JLabel lblSatuan = new JLabel("Satuan");
		lblSatuan.setBounds(10, 61, 111, 14);
		frame.getContentPane().add(lblSatuan);
		
		cmbSatuan = new JComboBox<String>();
		cmbSatuan.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				try {
					TampilStokHarga(IdBarangDariTxtCari(), cmbSatuan.getSelectedItem().toString());
					txtQty.requestFocus();
				} catch (Exception e) {
					System.out.println("errorki tampil stok sama harga: " + e);
				}
			}
		});
		cmbSatuan.setBounds(131, 58, 249, 20);
		frame.getContentPane().add(cmbSatuan);
		
		JLabel lblSisaStok = new JLabel("Sisa Stok");
		lblSisaStok.setBounds(10, 86, 46, 14);
		frame.getContentPane().add(lblSisaStok);
		
		txtStok = new JTextField();
		txtStok.setEditable(false);
		txtStok.setBounds(131, 83, 249, 20);
		frame.getContentPane().add(txtStok);
		txtStok.setColumns(10);
		
		JLabel lblHargaSatuan = new JLabel("Harga Satuan");
		lblHargaSatuan.setBounds(10, 111, 111, 14);
		frame.getContentPane().add(lblHargaSatuan);
		
		txtHarga = new JTextField();
		txtHarga.setBounds(131, 108, 249, 20);
		frame.getContentPane().add(txtHarga);
		txtHarga.setColumns(10);
		
		JLabel lblQty = new JLabel("Qty");
		lblQty.setBounds(10, 136, 46, 14);
		frame.getContentPane().add(lblQty);
		
		txtQty = new JTextField();
		txtQty.setBounds(131, 133, 249, 20);
		frame.getContentPane().add(txtQty);
		txtQty.setColumns(10);
		
		JButton btnTambah = new JButton("Tambah");
		btnTambah.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (Integer.valueOf(txtQty.getText()) > 0 && !txtCari.getText().equals("")){
						if (FCetakFaktur.rdbtnTransaksiTunai.isSelected()){
							TambahItemTunai();
							FMain.UpdateStok(IdBarangDariTxtCari(), cmbSatuan.getSelectedItem().toString(), 0, Integer.valueOf(txtQty.getText()));
						}
						else{
							if (FMain.TotalBayarTransaksiKredit(NoFaktur) != FMain.SisaHutang(NoFaktur))
								JOptionPane.showMessageDialog(null, "Penambahan item tidak diperbolehkan!!!\nTransaksi Kredit ini sudah dibayarkan sebagian!!!");
							else{
								TambahItemKredit();
								FMain.UpdateStok(IdBarangDariTxtCari(), cmbSatuan.getSelectedItem().toString(), 0, Integer.valueOf(txtQty.getText()));
							}
						}
						
						FCetakFaktur.window.frame.setEnabled(true);
						window.frame.dispose();
						frame.dispose();
						window.frame = null;
						frame = null;
						window = null;
						System.gc();
					}
				} catch (Exception e) {}
			}
		});
		btnTambah.setBounds(192, 164, 89, 23);
		frame.getContentPane().add(btnTambah);
		
		JButton btnBatal = new JButton("Batal");
		btnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FCetakFaktur.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		btnBatal.setBounds(291, 164, 89, 23);
		frame.getContentPane().add(btnBatal);
	}
	
	private void CariData(String parameter){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT id, nama from tb_barang where id like ? or nama like ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,"%" + parameter + "%");
			psQuery.setString(2,"%" + parameter + "%");
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			
			String[] hasil = new String[rowcount];
			if (rowcount > 0 && !(txtCari.getText().equals(""))){
				scrollPane.setVisible(true);
				int i=0;
				while (rs.next()) {
					hasil[i] = rs.getString("id") + " - " + rs.getString("nama");
					i++;
				}
				
			}
			else
				scrollPane.setVisible(false);
			
			list.setListData(hasil);
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cari datanya " + e);}
	}
	
	private String IdBarangDariTxtCari(){
		return txtCari.getText().substring(0, txtCari.getText().indexOf(" "));
	}
	
	private void TampilSatuan(String IdBarang){
		cmbSatuan.removeAllItems();
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			
			query = "select satuan from tb_detail_barang where id_barang = ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, IdBarang);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			while (rs.next()) {
				cmbSatuan.addItem(rs.getString("satuan"));
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil satuan " + e);}
	}
	
	private void TampilStokHarga(String IdBarang, String Satuan){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);

			String query=null;
			ResultSet rs;

			query = "select harga_jual, stok from tb_detail_barang where id_barang=? and satuan=?";

			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, IdBarang);
			psQuery.setString(2, Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();

			while (rs.next()) {
				txtHarga.setText(FMain.FormatAngka(rs.getInt("harga_jual")));
				txtStok.setText(rs.getString("stok"));
			}

			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil harga " + e);}
	}
	
	private void TambahItemTunai(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			PreparedStatement psQuery=null;
			
			int Potongan=0,Subtotal=0;
			Potongan = (int) (float) (Integer.valueOf(txtHarga.getText().replace(",", ""))*(FMain.Diskon(IdBarangDariTxtCari(), cmbSatuan.getSelectedItem().toString()) / 100));
			Subtotal = Integer.valueOf(txtQty.getText()) * (Integer.valueOf(txtHarga.getText().replace(",", ""))-Potongan);
			
			query = "update tb_transaksi set total_bayar = ? where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setInt(1, FMain.TotalBayarTransaksiTunai(NoFaktur) + Subtotal);
			psQuery.setString(2, NoFaktur);
			psQuery.execute();
			
			query = "insert into tb_detail_transaksi values (?,?,?,?,?,?)";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.setString(2, IdBarangDariTxtCari());
			psQuery.setString(3, txtHarga.getText().replace(",", ""));
			psQuery.setString(4, cmbSatuan.getSelectedItem().toString());
			psQuery.setString(5, txtQty.getText());
			psQuery.setFloat(6, FMain.Diskon(IdBarangDariTxtCari(), cmbSatuan.getSelectedItem().toString()));
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Item untuk transaksi tunai berhasil ditambahkan...");
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil harga " + e);}
	}
	
	private void TambahItemKredit(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			PreparedStatement psQuery=null;
			
			int Potongan=0,Subtotal=0,total=0;
			Potongan = (int) (float) (Integer.valueOf(txtHarga.getText().replace(",", ""))*(FMain.Diskon(IdBarangDariTxtCari(), cmbSatuan.getSelectedItem().toString()) / 100));
			Subtotal = Integer.valueOf(txtQty.getText()) * (Integer.valueOf(txtHarga.getText().replace(",", ""))-Potongan);
			total = FMain.TotalBayarTransaksiKredit(NoFaktur) + Subtotal;
			
			query = "update tb_transaksi_kredit set total_bayar = ? where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setInt(1, total);
			psQuery.setString(2, NoFaktur);
			psQuery.execute();
			
			query = "insert into tb_detail_transaksi_kredit values (?,?,?,?,?,?)";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, NoFaktur);
			psQuery.setString(2, IdBarangDariTxtCari());
			psQuery.setString(3, txtHarga.getText().replace(",", ""));
			psQuery.setString(4, cmbSatuan.getSelectedItem().toString());
			psQuery.setString(5, txtQty.getText());
			psQuery.setFloat(6, FMain.Diskon(IdBarangDariTxtCari(), cmbSatuan.getSelectedItem().toString()));
			psQuery.execute();
			
			query = "update tb_hutang set sisa_hutang=? where no_faktur = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setInt(1, total);
			psQuery.setString(2, NoFaktur);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			JOptionPane.showMessageDialog(null, "Item untuk transaksi kredit berhasil ditambahkan...");
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil harga " + e);}
	}
	
}
