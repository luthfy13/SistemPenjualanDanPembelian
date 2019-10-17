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
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ListSelectionModel;

public class FSetDiskon {

	private JFrame frame;
	static FSetDiskon window;
	private JTextField txtCari;
	private JTable table;
	private JTextField txtDiskon;
	private JScrollPane spBarang;
	private JList<String> listBarang;
	private JComboBox<String> cmbSatuan;
	private JLabel lblPersen;
	private JLabel lblHarga;
	private JTextField txtHarga;
	private JTextField txtDiscHarga;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FSetDiskon();
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
	public FSetDiskon() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Set Diskon Barang");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				RefreshTabel();
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
		frame.setResizable(false);
		frame.setBounds(100, 100, 507, 479);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JButton btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtCari.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Kode atau Nama Barang\ntidak boleh kosong!!!");
				}
				else if (txtDiskon.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Diskon tidak boleh kosong!!!");
				}
				else {
					SimpanDiskon();
					RefreshTabel();
					txtDiskon.setText("");
					txtCari.setText("");
					txtCari.requestFocus();
				}
			}
		});
		
		spBarang = new JScrollPane();
		spBarang.setVisible(false);
		spBarang.setBounds(111, 33, 280, 90);
		frame.getContentPane().add(spBarang);
		
		listBarang = new JList<String>();
		listBarang.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					txtCari.setText(listBarang.getSelectedValue().toString());
					txtHarga.setText("");
					TampilSatuan(IdBarangDariTxtCari());
					cmbSatuan.requestFocus();
				}
				if (arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE){
					txtCari.requestFocus();
				}
			}
		});
		spBarang.setViewportView(listBarang);
		btnSimpan.setBounds(401, 13, 91, 23);
		frame.getContentPane().add(btnSimpan);
		
		JButton btnBatal = new JButton("Tutup");
		btnBatal.setMnemonic(KeyEvent.VK_X);
		btnBatal.addActionListener(new ActionListener() {
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
		btnBatal.setBounds(401, 41, 91, 23);
		frame.getContentPane().add(btnBatal);
		
		txtCari = new JTextField();
		txtCari.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtCari.selectAll();
			}
		});
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_DOWN){
					listBarang.requestFocus();
					listBarang.setSelectedIndex(0);
				}
			}
		});
		
		txtCari.getDocument().addDocumentListener(new DocumentListener() {
			
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
			public void EventChange(){
				CariData(txtCari.getText());
			}
		});
		txtCari.setBounds(111, 14, 280, 20);
		frame.getContentPane().add(txtCari);
		txtCari.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 167, 482, 267);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Jumlah Diskon", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(10, 75, 183, 81);
		frame.getContentPane().add(panel_1);
		
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
		txtDiskon.setBounds(66, 17, 53, 20);
		panel_1.add(txtDiskon);
		txtDiskon.setColumns(10);
		
		lblPersen = new JLabel("%");
		lblPersen.setBounds(129, 20, 16, 14);
		panel_1.add(lblPersen);
		
		JLabel lblDiskon = new JLabel("Persen");
		lblDiskon.setBounds(10, 20, 46, 14);
		panel_1.add(lblDiskon);
		
		JLabel lblHarga_1 = new JLabel("Harga");
		lblHarga_1.setBounds(10, 45, 46, 14);
		panel_1.add(lblHarga_1);
		
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
		txtDiscHarga.setBounds(66, 42, 79, 20);
		panel_1.add(txtDiscHarga);
		txtDiscHarga.setColumns(10);

		JLabel lblKodenama = new JLabel("Kode/Nama Brg");
		lblKodenama.setBounds(10, 17, 91, 14);
		frame.getContentPane().add(lblKodenama);

		JButton btnHapus = new JButton("Hapus");
		btnHapus.setText("<html><center>"+"Hapus"+"<br>"+"Diskon"+"</center></html>");
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (FMain.konfir == JOptionPane.YES_OPTION){
					if (table.getSelectedRow() > -1){
						String id = table.getValueAt(table.getSelectedRow(), 0).toString();
						String satuan = table.getValueAt(table.getSelectedRow(), 2).toString();
						HapusItemDiskon(id, satuan);
					}
					RefreshTabel();
				}
			}
		});
		btnHapus.setBounds(401, 75, 90, 49);
		frame.getContentPane().add(btnHapus);
		
		JLabel lblSatuan = new JLabel("Satuan");
		lblSatuan.setBounds(10, 45, 46, 14);
		frame.getContentPane().add(lblSatuan);
		
		cmbSatuan = new JComboBox<String>();
		cmbSatuan.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (cmbSatuan.getItemCount() > 0)
					TampilHarga(IdBarangDariTxtCari(), cmbSatuan.getSelectedItem().toString());
			}
		});
		cmbSatuan.setBounds(111, 42, 82, 20);
		frame.getContentPane().add(cmbSatuan);
		
		lblHarga = new JLabel("Harga");
		lblHarga.setBounds(238, 45, 46, 14);
		frame.getContentPane().add(lblHarga);
		
		txtHarga = new JTextField();
		txtHarga.setEditable(false);
		txtHarga.setBounds(279, 42, 112, 20);
		frame.getContentPane().add(txtHarga);
		txtHarga.setColumns(10);
	}
	
	private void SimpanDiskon(){
		
//		switch (cmbDiskon.getSelectedIndex()){
//			case 0 :
//				disc = (float) (
//						Integer.valueOf(txtHarga.getText().replace(",", ""))*(Float.valueOf(txtDiskon.getText()) / 100)
//						);
//				Diskon = (int) disc;
//				break;
//			case 1 :
//				Diskon = Integer.valueOf(txtDiskon.getText());
//				break;
//		}

		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			
			PreparedStatement ps=null;
			String query = "insert into tb_diskon values (?,?,?)";
			ps = conn.prepareStatement(query);
			
			ps.setString(1, IdBarangDariTxtCari());
			ps.setString(2, cmbSatuan.getSelectedItem().toString());
			ps.setFloat(3, Float.valueOf(txtDiskon.getText()));
			ps.execute();
				
			ps.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan diskon " + e);}
	}
	
	private void RefreshTabel(){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			String[] columnNames = {
					"ID Barang",
					"Nama Barang",
					"Satuan",
					"Harga Jual",
					"Diskon",
					"Harga Setelah Diskon"
			};
			DefaultTableModel model = new DefaultTableModel(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
				       //all cells false
				       return false;
				}
			};
			model.setColumnIdentifiers(columnNames);
			
			String query;
			Statement st;
			ResultSet rs;
			query = "SELECT " +
					"	tb_diskon.id_barang, " +
					"	tb_barang.nama, " +
					"	tb_diskon.satuan, " +
					"	tb_detail_barang.harga_jual, " +
					"	tb_diskon.diskon, " +
					"	( " +
					"		tb_detail_barang.harga_jual - ((tb_diskon.diskon/100)*tb_detail_barang.harga_jual) " +
					"	) AS harga_diskon " +
					"FROM " +
					"	tb_diskon " +
					"INNER JOIN tb_barang ON tb_diskon.id_barang = tb_barang.id " +
					"INNER JOIN tb_detail_barang ON tb_diskon.id_barang = tb_detail_barang.id_barang " +
					"AND tb_diskon.satuan = tb_detail_barang.satuan";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()){
				model.addRow(new Object[] {
						rs.getString("id_barang"),
						rs.getString("nama"),
						rs.getString("satuan"),
						FMain.FormatAngka(rs.getInt("harga_jual")),
						rs.getString("diskon") + "%",
						FMain.FormatAngka(rs.getInt("harga_diskon"))
				});
			};

			table.setModel(model);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getColumnModel().getColumn(0).setPreferredWidth(70);
			table.getColumnModel().getColumn(1).setPreferredWidth(120);
			table.getColumnModel().getColumn(2).setPreferredWidth(50);
			table.getColumnModel().getColumn(3).setPreferredWidth(70);
			table.getColumnModel().getColumn(4).setPreferredWidth(50);
			table.getColumnModel().getColumn(5).setPreferredWidth(120);
			
		} catch (Exception e) {
			System.out.println(e);
		}
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
				spBarang.setVisible(true);
				int i=0;
				while (rs.next()) {
					hasil[i] = rs.getString("id") + " - " + rs.getString("nama");
					i++;
				}
				
			}
			else
				spBarang.setVisible(false);
			
			listBarang.setListData(hasil);
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cari datanya " + e);}
	}
	
	private void HapusItemDiskon(String IdBarang, String Satuan){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			
			PreparedStatement psInsertHslPrep=null;
			String qInsertHslPrep = "delete from tb_diskon where id_barang=? and satuan=?";
			psInsertHslPrep = conn.prepareStatement(qInsertHslPrep);
			psInsertHslPrep.setString(1, IdBarang);
			psInsertHslPrep.setString(2, Satuan);
			psInsertHslPrep.execute();
				
			psInsertHslPrep.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro hapus item diskon " + e);}
	}
		
	private String IdBarangDariTxtCari(){
		return txtCari.getText().substring(0, txtCari.getText().indexOf(" "));
	}
	
	private void TampilSatuan(String IdBarang){
		cmbSatuan.removeAllItems();
		cmbSatuan.addItem("-PILIH-");
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
	
	private void TampilHarga(String IdBarang, String Satuan){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);

			String query=null;
			ResultSet rs;

			query = "select harga_jual from tb_detail_barang where id_barang=? and satuan=?";

			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, IdBarang);
			psQuery.setString(2, Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();

			while (rs.next()) {
				txtHarga.setText(FMain.FormatAngka(rs.getInt("harga_jual")));
			}

			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil harga " + e);}
	}
}