import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Toolkit;

public class FTransaksiSetBarang {

	private JFrame frame;
	static FTransaksiSetBarang window;
	private JTextField txtIdBarang;
	private JTextField txtNamaBarang;
	private JTextField txtStok;
	private JTextField txtHarga;
	private JLabel lblJumlah;
	private JTextField txtJml;
	private JComboBox<String> cmbSatuan;
	static String IdBarang=null,NamaBarang=null;
	static short Stok;
	private int Harga=0,Potongan=0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FTransaksiSetBarang();
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
	public FTransaksiSetBarang() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Buy.png"));
		frame.setTitle("Pilih Barang");
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				
				//cmbSatuan.requestFocus();
			}
			@Override
			public void windowOpened(WindowEvent arg0) {
				if (FMain.LevelUser.equals("admin"))
					txtHarga.setEditable(true);
				txtIdBarang.setText(IdBarang);
				txtNamaBarang.setText(NamaBarang);
				TampilSatuan(IdBarang);
				TampilHargaStok(IdBarang, cmbSatuan.getSelectedItem().toString());
			}
			@Override
			public void windowClosing(WindowEvent e) {
				FTransaksi.window.frame.setEnabled(true);
				FTransaksi.txtCari.setText("");
				FTransaksi.txtCari.requestFocus();
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		frame.setBounds(100, 100, 423, 215);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblKodeBarang = new JLabel("ID Barang");
		lblKodeBarang.setBounds(26, 24, 92, 14);
		frame.getContentPane().add(lblKodeBarang);
		
		JLabel lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setBounds(26, 49, 92, 14);
		frame.getContentPane().add(lblNamaBarang);
		
		JLabel lblSatuan = new JLabel("Satuan");
		lblSatuan.setBounds(26, 74, 46, 14);
		frame.getContentPane().add(lblSatuan);
		
		JLabel lblSisaStok = new JLabel("Sisa Stok");
		lblSisaStok.setBounds(26, 99, 92, 14);
		frame.getContentPane().add(lblSisaStok);
		
		JLabel lblHarga = new JLabel("Harga Satuan");
		lblHarga.setBounds(26, 124, 92, 14);
		frame.getContentPane().add(lblHarga);
		
		lblJumlah = new JLabel("Qty");
		lblJumlah.setBounds(26, 149, 46, 14);
		frame.getContentPane().add(lblJumlah);
		
		txtIdBarang = new JTextField();
		txtIdBarang.setEditable(false);
		txtIdBarang.setBounds(106, 21, 202, 20);
		frame.getContentPane().add(txtIdBarang);
		txtIdBarang.setColumns(10);
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.setEditable(false);
		txtNamaBarang.setColumns(10);
		txtNamaBarang.setBounds(106, 46, 202, 20);
		frame.getContentPane().add(txtNamaBarang);
		
		cmbSatuan = new JComboBox<String>();
		cmbSatuan.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				TampilHargaStok(txtIdBarang.getText(), cmbSatuan.getSelectedItem().toString());
				txtJml.requestFocus();
			}
		});
		cmbSatuan.setBounds(106, 71, 202, 20);
		frame.getContentPane().add(cmbSatuan);
		
		txtStok = new JTextField();
		txtStok.setEditable(false);
		txtStok.setColumns(10);
		txtStok.setBounds(106, 96, 202, 20);
		frame.getContentPane().add(txtStok);
		
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
		});
		txtHarga.setEditable(false);
		txtHarga.setColumns(10);
		txtHarga.setBounds(106, 121, 202, 20);
		frame.getContentPane().add(txtHarga);
		
		txtJml = new JTextField();
		txtJml.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					boolean ada=false;
					if (FTransaksi.table.getRowCount() > 0){
						for (byte i=0; i<=FTransaksi.table.getRowCount()-1; i++){
							if (FTransaksi.table.getValueAt(i, 0).toString().equals(txtIdBarang.getText()))
									ada = true;
						}
					}
					try {
						int jml,stok;
						jml = Integer.valueOf(txtJml.getText());
						stok = Integer.valueOf(txtStok.getText());
						if (jml > stok){
							JOptionPane.showMessageDialog(txtJml, "Stok tidak cukup!");
							txtJml.setText(String.valueOf(stok));
						}
						else if (jml <= stok){
							if (ada == false){
								FTransaksi.window.frame.setEnabled(true);
								SetListTransaksi();
								FTransaksi.txtCari.setText("");
								FTransaksi.txtCari.requestFocus();
								window.frame.dispose();
							}
							else if (ada == true){
								JOptionPane.showMessageDialog(null, txtNamaBarang.getText() + " sudah ada pada daftar item!!!");
								FTransaksi.window.frame.setEnabled(true);
								FTransaksi.txtCari.setText("");
								FTransaksi.txtCari.requestFocus();
								window.frame.dispose();
								frame.dispose();
							}
							
						}
					} catch (Exception e) {
						System.out.println(e);;
					}
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
		          if (!((c >= '0') && (c <= '9') ||
		             (c == KeyEvent.VK_BACK_SPACE) ||
		             (c == KeyEvent.VK_DELETE))) {
		            e.consume();
		          }
			}
		});
		txtJml.setBounds(106, 146, 202, 20);
		frame.getContentPane().add(txtJml);
		txtJml.setColumns(10);
		
		JButton BtnOk = new JButton("OK");
		BtnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean ada=false;
				if (FTransaksi.table.getRowCount() > 0){
					for (byte i=0; i<=FTransaksi.table.getRowCount()-1; i++){
						if (FTransaksi.table.getValueAt(i, 0).toString().equals(txtIdBarang.getText()))
								ada = true;
					}
				}
				try {
					int jml,stok;
					jml = Integer.valueOf(txtJml.getText());
					stok = Integer.valueOf(txtStok.getText());
					if (jml > stok){
						JOptionPane.showMessageDialog(txtJml, "Stok tidak cukup!");
						txtJml.setText(String.valueOf(stok));
					}
					else if (jml <= stok){
						if (ada == false){
							FTransaksi.window.frame.setEnabled(true);
							SetListTransaksi();
							FTransaksi.txtCari.setText("");
							FTransaksi.txtCari.requestFocus();
							window.frame.dispose();
							frame.dispose();
							window.frame = null;
							frame = null;
							window = null;
							System.gc();
						}
						else if (ada == true){
							JOptionPane.showMessageDialog(null, txtNamaBarang.getText() + " sudah ada pada daftar item!!!");
							FTransaksi.window.frame.setEnabled(true);
							FTransaksi.txtCari.setText("");
							FTransaksi.txtCari.requestFocus();
							window.frame.dispose();
							frame.dispose();
							frame.dispose();
							window.frame = null;
							frame = null;
							window = null;
							System.gc();
						}
						
					}
				} catch (Exception e) {
					System.out.println(e);;
				}				
			}
		});
		BtnOk.setBounds(318, 20, 91, 23);
		frame.getContentPane().add(BtnOk);
		
		JButton BtnBatal = new JButton("BATAL");
		BtnBatal.setMnemonic(KeyEvent.VK_X);
		BtnBatal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FTransaksi.window.frame.setEnabled(true);
				FTransaksi.txtCari.setText("");
				FTransaksi.txtCari.requestFocus();
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		BtnBatal.setBounds(318, 49, 91, 23);
		frame.getContentPane().add(BtnBatal);
	}
	
	private void SetListTransaksi(){
//		if (Diskon(txtIdBarang.getText(), cmbSatuan.getSelectedItem().toString()) != 0){
//			Harga = Integer.valueOf(txtHarga.getText()) - Diskon(txtIdBarang.getText(), cmbSatuan.getSelectedItem().toString());
//		}
//		else{
//			Harga = Integer.valueOf(txtHarga.getText());
//		}
		Potongan = (int) (float) (Integer.valueOf(txtHarga.getText().replace(",", ""))*(FMain.Diskon(txtIdBarang.getText(), cmbSatuan.getSelectedItem().toString()) / 100));
		Harga = Integer.valueOf(txtHarga.getText()) - Potongan;
		Harga = Harga * Integer.valueOf(txtJml.getText());
		
		String[] columnNames = {"Kode Barang","Nama Barang", "Satuan", "Harga", "Qty", "Sub Total", "Diskon", "Final Sub Total" };
		FTransaksi.model.setColumnIdentifiers(columnNames);
		FTransaksi.model.addRow(new Object[] {
				IdBarang,
				NamaBarang,
				cmbSatuan.getSelectedItem().toString(),
				FMain.FormatAngka(Integer.valueOf(txtHarga.getText())),
				txtJml.getText(),
				FMain.FormatAngka(Integer.valueOf(txtHarga.getText())*Integer.valueOf(txtJml.getText())),
				FMain.Diskon(txtIdBarang.getText(), cmbSatuan.getSelectedItem().toString()) + "%",
				FMain.FormatAngka(Harga)
		});
		
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		FTransaksi.table.setModel(FTransaksi.model);
		FTransaksi.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		FTransaksi.table.getColumnModel().getColumn(0).setPreferredWidth(140); //id
		FTransaksi.table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		FTransaksi.table.getColumnModel().getColumn(1).setPreferredWidth(250); //nama
		FTransaksi.table.getColumnModel().getColumn(2).setPreferredWidth(70); //satuan
		FTransaksi.table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		FTransaksi.table.getColumnModel().getColumn(3).setPreferredWidth(100); //harga
		FTransaksi.table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		FTransaksi.table.getColumnModel().getColumn(4).setPreferredWidth(50); //qty
		FTransaksi.table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		FTransaksi.table.getColumnModel().getColumn(5).setPreferredWidth(120); //sub total
		FTransaksi.table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		FTransaksi.table.getColumnModel().getColumn(6).setPreferredWidth(80); // diskon
		FTransaksi.table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		FTransaksi.table.getColumnModel().getColumn(7).setPreferredWidth(150); //final sub total
		FTransaksi.table.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
		
		FTransaksi.table.setRowHeight(25);
		
		FTransaksi.TotalBayar = Integer.valueOf(FTransaksi.txtTotalBayar.getText().replace(",", "")) + Harga;
		FTransaksi.txtTotalBayar.setText(FMain.FormatAngka(FTransaksi.TotalBayar));
		
		rightRenderer = null;
		centerRenderer = null;
		
	}
	
	private void TampilSatuan(String Id){
		cmbSatuan.removeAllItems();
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			
			if (FTransaksi.cmbSubTransaksi.getSelectedItem().toString().equals("Eceran"))
				query = "select satuan from tb_detail_barang where id_barang= ? order by harga_jual";
			else if (FTransaksi.cmbSubTransaksi.getSelectedItem().toString().equals("Grosir") || FTransaksi.cmbSubTransaksi.getSelectedItem().toString().equals("Sales"))
				query = "select satuan from tb_detail_barang where id_barang= ? order by harga_jual desc";
			else
				query = "select satuan from tb_detail_barang where id_barang= ? order by harga_jual";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, Id);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			while (rs.next()){
				cmbSatuan.addItem(rs.getString("satuan"));
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil satuan " + e);}
	}
	
	private void TampilHargaStok(String Id, String Satuan){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			
			query = "select harga_jual, stok from tb_detail_barang where id_barang= ? and satuan = ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, Id);
			psQuery.setString(2, Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			while (rs.next()){
				txtHarga.setText(rs.getString("harga_jual"));
				txtStok.setText(rs.getString("stok"));
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil harga satuan " + e);}
	}
	
}
