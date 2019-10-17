import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.jdesktop.swingx.JXDatePicker;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class FTransaksi {
		
	JFrame frame;
	static FTransaksi window;
	private JTextField txtNoFaktur;
	private JTextField txtTanggal;
	private JTextField txtIdPelanggan;
	private JTextField txtNama;
	static JTextField txtCari;
	private JList<String> list1;
	private JScrollPane scrollPane1;
	private JComboBox<String> cmbCari;
	static JTable table;
	static DefaultTableModel model = new DefaultTableModel(){
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
	static JTextField txtTotalBayar;
	private JButton btnHapus;
	private JButton btnSimpan;
	private JLabel lblTanggalJatuhTempo;
	private JXDatePicker dpTglJatuhTempo;
	static int TotalBayar=0;
	//static String JenisTransaksi=null;
	//static boolean TransaksiTunai;
	static String JudulForm = null;
	private JComboBox<String> cmbSupir;
	private JLabel lblSupir;
	private JTextField txtCash;
	private JLabel lblNewLabel;
	static JComboBox<String> cmbTransaksi;
	static JComboBox<String> cmbSubTransaksi;
	private JFrame jf = new JFrame();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FTransaksi();
					window.frame.setVisible(true);
					window.frame.setExtendedState(window.frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FTransaksi() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Buy.png"));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 1024, 768);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				frame.setTitle(JudulForm);
//				if (TransaksiTunai==true){
//					lblTanggalJatuhTempo.setVisible(false);
//					dpTglJatuhTempo.setVisible(false);
//				}
//				else{
//					lblTanggalJatuhTempo.setVisible(true);
//					dpTglJatuhTempo.setVisible(true);
//				}
//				
//				if (JenisTransaksi.equals("eceran")){
//					lblSupir.setVisible(false);
//					cmbSupir.setVisible(false);
//				}
//				else{
//					lblSupir.setVisible(false); //permintaan goblok
//					cmbSupir.setVisible(false); //permintaan goblok juga
//					txtCash.setVisible(false);
//					lblNewLabel.setVisible(false);
//				}
				txtCash.setVisible(true);
				lblNewLabel.setVisible(true);
				TampilSupir();
				lblSupir.setVisible(false);
				cmbSupir.setVisible(false);
				AturTabel();
				cmbCari.setSelectedIndex(SetIndexCmbCari());
				if (UserSales()){
					cmbSubTransaksi.setSelectedIndex(2);
					cmbSubTransaksi.setEnabled(false);
					txtCash.setVisible(false);
					lblNewLabel.setVisible(false);
				}
				TransaksiBaru();
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				SimpanIndexCmbCari();
				for(int i = table.getRowCount()-1; i>=0; i--){
					model.removeRow(i);
				}
				txtNoFaktur = null;
				txtTanggal = null;
				txtIdPelanggan = null;
				txtNama = null;
				txtCari = null;
				list1 = null;
				scrollPane1 = null;
				cmbCari = null;
				table = null;
				//model =  null;
				txtTotalBayar = null;
				btnHapus = null;
				btnSimpan = null;
				lblTanggalJatuhTempo = null;
				dpTglJatuhTempo = null;
				TotalBayar=0;
				//JenisTransaksi=null;
				JudulForm = null;
				cmbSupir = null;
				lblSupir = null;
				txtCash = null;
				lblNewLabel = null;
				FMain.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		frame.getContentPane().setLayout(null);
		
		JLabel lblNoFaktur = new JLabel("No. Faktur");
		lblNoFaktur.setBounds(11, 15, 80, 14);
		frame.getContentPane().add(lblNoFaktur);
		
		JLabel lblTanggal = new JLabel("Tanggal");
		lblTanggal.setBounds(11, 40, 80, 14);
		frame.getContentPane().add(lblTanggal);
		
		JLabel lblIdPelanggan = new JLabel("ID Pelanggan");
		lblIdPelanggan.setBounds(11, 65, 80, 14);
		frame.getContentPane().add(lblIdPelanggan);
		
		JLabel lblNama = new JLabel("Nama");
		lblNama.setBounds(11, 90, 80, 14);
		frame.getContentPane().add(lblNama);
		
		txtNoFaktur = new JTextField();
		txtNoFaktur.setBounds(101, 12, 218, 20);
		txtNoFaktur.setEditable(false);
		txtNoFaktur.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtNoFaktur.setFont(new Font("Tahoma", txtNoFaktur.getFont().getStyle(), 14));
		txtNoFaktur.setBackground(new Color(230, 230, 250));
		frame.getContentPane().add(txtNoFaktur);
		txtNoFaktur.setColumns(10);
		
		txtTanggal = new JTextField();
		txtTanggal.setBounds(101, 37, 218, 20);
		txtTanggal.setEditable(false);
		txtTanggal.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtTanggal.setFont(new Font("Tahoma", txtNoFaktur.getFont().getStyle(), 14));
		txtTanggal.setBackground(new Color(230, 230, 250));
		frame.getContentPane().add(txtTanggal);
		txtTanggal.setColumns(10);
		
		txtIdPelanggan = new JTextField();
		txtIdPelanggan.setBounds(101, 62, 218, 20);
		txtIdPelanggan.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtIdPelanggan.setFont(new Font("Tahoma", txtNoFaktur.getFont().getStyle(), 14));
		txtIdPelanggan.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					CariDataPelanggan(txtIdPelanggan.getText());
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				char keyChar = e.getKeyChar();
			    if (Character.isLowerCase(keyChar)) {
			      e.setKeyChar(Character.toUpperCase(keyChar));
			    }
			}
		});
		txtIdPelanggan.setBackground(new Color(230, 230, 250));
		frame.getContentPane().add(txtIdPelanggan);
		txtIdPelanggan.setColumns(10);
		
		txtNama = new JTextField();
		txtNama.setBounds(101, 87, 218, 20);
		txtNama.setEditable(false);
		txtNama.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtNama.setFont(new Font("Tahoma", txtNoFaktur.getFont().getStyle(), 14));
		txtNama.setBackground(new Color(230, 230, 250));
		frame.getContentPane().add(txtNama);
		txtNama.setColumns(10);
		
		btnSimpan = new JButton("Simpan");
		btnSimpan.setBounds(816, 127, 91, 23);
		btnSimpan.setMnemonic(KeyEvent.VK_S);
		btnSimpan.setIcon(new ImageIcon(".\\icons\\Simpan.png"));
		btnSimpan.setForeground(Color.BLACK);
		btnSimpan.setFont(new Font("Tahoma", btnSimpan.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		btnSimpan.setBackground(Color.LIGHT_GRAY);
		btnSimpan.setBorder(null);
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (cmbSubTransaksi.getSelectedItem().toString().equals("Eceran")){
					SimpanEceran();
				}
				else{
					FMain.konfir = JOptionPane.showConfirmDialog(null, "Anda yakin ingin melanjutkan transaksi?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
					if (FMain.konfir == JOptionPane.YES_OPTION)
						SimpanData();
				}
			}
		});
		frame.getContentPane().add(btnSimpan);
		
		JButton btnTutup = new JButton("Tutup");
		btnTutup.setBounds(917, 127, 91, 23);
		btnTutup.setMnemonic(KeyEvent.VK_X);
		btnTutup.setIcon(new ImageIcon(".\\icons\\Tutup.png"));
		btnTutup.setForeground(Color.BLACK);
		btnTutup.setFont(new Font("Tahoma", btnSimpan.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		btnTutup.setBackground(Color.LIGHT_GRAY);
		btnTutup.setBorder(null);
		btnTutup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SimpanIndexCmbCari();
				for(int i = table.getRowCount()-1; i>=0; i--){
					model.removeRow(i);
				}
				txtNoFaktur = null;
				txtTanggal = null;
				txtIdPelanggan = null;
				txtNama = null;
				txtCari = null;
				list1 = null;
				scrollPane1 = null;
				cmbCari = null;
				table = null;
				//model =  null;
				txtTotalBayar = null;
				btnHapus = null;
				btnSimpan = null;
				lblTanggalJatuhTempo = null;
				dpTglJatuhTempo = null;
				TotalBayar=0;
				//JenisTransaksi=null;
				JudulForm = null;
				cmbSupir = null;
				lblSupir = null;
				txtCash = null;
				lblNewLabel = null;
				FMain.window.frame.setEnabled(true);
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		frame.getContentPane().add(btnTutup);
		
		cmbCari = new JComboBox<String>();
		cmbCari.setBounds(11, 128, 112, 22);
		cmbCari.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (!(txtIdPelanggan.getText().equals("")))
					txtCari.requestFocus();
			}
		});
		cmbCari.setModel(new DefaultComboBoxModel<String>(new String[] {"Kode Barang", "Nama Barang"}));
		cmbCari.setSelectedIndex(0);
		frame.getContentPane().add(cmbCari);
		
		txtCari = new JTextField();
		txtCari.setBounds(143, 128, 300, 20);
		txtCari.setFont(new Font("Tahoma", txtCari.getFont().getStyle(), 14));
		txtCari.setBackground(new Color(230, 230, 250));
		txtCari.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtCari.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_DOWN){
					list1.requestFocus();
					list1.setSelectedIndex(0);
				}
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					if (!txtCari.getText().equals("")){
						switch (cmbCari.getSelectedIndex()){
						case 0:
							SetBarangByTextField("id_barang");
							break;
						case 1:
							SetBarangByTextField("nama_barang");
							break;
						}
					}
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}
		});
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
			
			private void EventChange(){
				switch(cmbCari.getSelectedIndex()){
					case 0 :
						CariData("id_barang");
						break;
					case 1 :
						CariData("nama_barang");
						break;
				}
				
			}
			
		});
		frame.getContentPane().add(txtCari);
		txtCari.setColumns(10);
		
		scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(143, 148, 300, 140);
		scrollPane1.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPane1.setVisible(false);
		frame.getContentPane().add(scrollPane1);
		
		list1 = new JList<String>();
		list1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2 && !arg0.isConsumed()){
					arg0.consume();
					//statement dblclick di sini
					switch (cmbCari.getSelectedIndex()){
						case 0:
							SetBarang("id_barang");
							break;
						case 1:
							SetBarang("nama_barang");
							break;
					}
					txtCari.setText(list1.getSelectedValue().toString());
				}
			}
		});
		scrollPane1.setViewportView(list1);
		list1.setFont(new Font("Tahoma", txtCari.getFont().getStyle(), 14));
		list1.setBorder(new EmptyBorder(0, 0, 0, 0));
		list1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
					switch (cmbCari.getSelectedIndex()){
						case 0:
							SetBarang("id_barang");
							break;
						case 1:
							SetBarang("nama_barang");
							break;
					}
					txtCari.setText(list1.getSelectedValue().toString());
				}
				if (arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE){
					txtCari.requestFocus();
				}
			}
		});
		list1.setVisibleRowCount(3);
		list1.setValueIsAdjusting(true);
		list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 161, 998, 455);
		scrollPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setBackground(Color.WHITE);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getClickCount() == 2 && !arg0.isConsumed()){
					HapusBarisTabel();
				}
			}
		});
		table.setGridColor(Color.GRAY);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setBackground(Color.WHITE);
		table.setFont(new Font("Tahoma", table.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 18));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		
		JLabel lblTotalBayar = new JLabel("Total");
		lblTotalBayar.setBounds(754, 628, 56, 34);
		lblTotalBayar.setFont(new Font("Tahoma", lblTotalBayar.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 20));
		frame.getContentPane().add(lblTotalBayar);
		
		txtTotalBayar = new JTextField();
		txtTotalBayar.setBounds(820, 627, 188, 37);
		txtTotalBayar.setEditable(false);
		txtTotalBayar.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTotalBayar.setFont(new Font("Tahoma", txtTotalBayar.getFont().getStyle() & ~Font.ITALIC, 20));
		txtTotalBayar.setBackground(new Color(230, 230, 250));
		txtTotalBayar.setBorder(new EmptyBorder(0, 0, 0, 0));
		frame.getContentPane().add(txtTotalBayar);
		txtTotalBayar.setColumns(10);
		
		btnHapus = new JButton("Hapus Item");
		btnHapus.setBounds(11, 635, 112, 23);
		btnHapus.setIcon(new ImageIcon(".\\icons\\Hapus.png"));
		btnHapus.setForeground(Color.BLACK);
		btnHapus.setFont(new Font("Tahoma", btnSimpan.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 14));
		btnHapus.setBackground(Color.LIGHT_GRAY);
		btnHapus.setBorder(null);
		btnHapus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getRowCount() > 0)
					HapusBarisTabel();
				else
					JOptionPane.showMessageDialog(btnHapus, "Data barang belum ada!!!");
			}
		});
		frame.getContentPane().add(btnHapus);
		
		lblTanggalJatuhTempo = new JLabel("Tanggal Jatuh Tempo");
		lblTanggalJatuhTempo.setBounds(342, 15, 124, 14);
		lblTanggalJatuhTempo.setVisible(false);
		frame.getContentPane().add(lblTanggalJatuhTempo);
		
		dpTglJatuhTempo = new JXDatePicker();
		dpTglJatuhTempo.setBounds(475, 11, 145, 22);
		dpTglJatuhTempo.setVisible(false);
		dpTglJatuhTempo.setFormats(new String[] {"dd - MM - yyyy"});
		frame.getContentPane().add(dpTglJatuhTempo);
		
		lblSupir = new JLabel("Supir");
		lblSupir.setBounds(475, 133, 46, 14);
		frame.getContentPane().add(lblSupir);
		
		cmbSupir = new JComboBox<String>();
		cmbSupir.setBounds(510, 130, 180, 20);
		frame.getContentPane().add(cmbSupir);
		
		lblNewLabel = new JLabel("Cash");
		lblNewLabel.setBounds(754, 673, 46, 34);
		lblNewLabel.setFont(new Font("Tahoma", lblNewLabel.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 20));
		frame.getContentPane().add(lblNewLabel);
		
		txtCash = new JTextField();
		txtCash.setBounds(820, 675, 188, 37);
		txtCash.setFont(new Font("Tahoma", txtCash.getFont().getStyle() & ~Font.BOLD & ~Font.ITALIC, 20));
		txtCash.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (! (arg0.getKeyCode() == KeyEvent.VK_LEFT || arg0.getKeyCode() == KeyEvent.VK_RIGHT)){
					try {
						txtCash.setText(FMain.FormatAngka(Integer.valueOf(txtCash.getText().replace(",", ""))));
					} catch (Exception er) {
						if (!txtCash.getText().equals(""))
							JOptionPane.showMessageDialog(null, "Batas maksimum = 2.147.483.647!!!");
						txtCash.setText("");
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
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) SimpanEceran();
			}
		});
		txtCash.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCash.setColumns(10);
		txtCash.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtCash.setBackground(new Color(230, 230, 250));
		frame.getContentPane().add(txtCash);
		
		JPanel panel = new JPanel();
		panel.setBounds(863, 11, 145, 92);
		panel.setBackground(Color.WHITE);
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Jenis Transaksi", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		cmbTransaksi = new JComboBox<String>();
		cmbTransaksi.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				//TransaksiBaru();
				switch (cmbTransaksi.getSelectedItem().toString()){
					case "Tunai" :
						cmbSubTransaksi.removeAllItems();
						lblTanggalJatuhTempo.setVisible(false);
						dpTglJatuhTempo.setVisible(false);
						cmbSubTransaksi.addItem("Eceran");
						cmbSubTransaksi.addItem("Grosir");
						cmbSubTransaksi.addItem("Sales");
						break;
					case "Kredit":
						cmbSubTransaksi.removeAllItems();
						lblTanggalJatuhTempo.setVisible(true);
						dpTglJatuhTempo.setVisible(true);
						cmbSubTransaksi.addItem("Grosir");
						cmbSubTransaksi.addItem("Sales");
						break;
				}
			}
		});
		cmbTransaksi.setModel(new DefaultComboBoxModel<String>(new String[] {"Tunai", "Kredit"}));
		cmbTransaksi.setSelectedIndex(0);
		cmbTransaksi.setBounds(10, 23, 123, 20);
		panel.add(cmbTransaksi);
		
		cmbSubTransaksi = new JComboBox<String>();
		cmbSubTransaksi.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				try {
					switch (cmbSubTransaksi.getSelectedItem().toString()){
						case "Eceran":
							lblSupir.setVisible(false);
							cmbSupir.setVisible(false);
							txtCash.setVisible(true);
							lblNewLabel.setVisible(true);
							break;
						case "Grosir":
							lblSupir.setVisible(false); //permintaan goblok
							cmbSupir.setVisible(false); //permintaan goblok juga
							txtCash.setVisible(false);
							lblNewLabel.setVisible(false);
							break;
						case "Sales":
							lblSupir.setVisible(false); //permintaan goblok
							cmbSupir.setVisible(false); //permintaan goblok juga
							txtCash.setVisible(false);
							lblNewLabel.setVisible(false);
							break;
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
		cmbSubTransaksi.setModel(new DefaultComboBoxModel<String>(new String[] {"Eceran", "Grosir", "Sales"}));
		cmbSubTransaksi.setBounds(10, 54, 123, 20);
		panel.add(cmbSubTransaksi);
	}
	
	private void CariData(String NamaField){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			if (NamaField.equals("id_barang"))
				query = "SELECT id as a from tb_barang where id like ?";
			else if (NamaField.equals("nama_barang"))
				query = "SELECT nama as a from tb_barang where nama like ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,"%" + txtCari.getText() + "%");
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			String[] hasil = new String[rowcount];
			if (rowcount > 0 && !(txtCari.getText().equals(""))){
				scrollPane1.setVisible(true);
				int i=0;
				while (rs.next()) {
					hasil[i] = rs.getString("a");
					i++;
				}
				
			}
			else
				scrollPane1.setVisible(false);
			
			list1.setListData(hasil);
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cari datanya " + e);}
	}
	
	private String NoFakturTransaksiBaru(){
		String no_faktur = "";
//		DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
//		Date date = new Date();
//		if (cmbTransaksi.getSelectedItem().toString().equals("Tunai")){
//			no_faktur = "TU" + "/" + FMain.id_karyawan + "/" + 
//						dateFormat.format(date) + "/" +
//						(JmlRecordTransaksi()+1);
//			return no_faktur;
//		}
//		else{
//			no_faktur = "KR" + "/" + FMain.id_karyawan + "/" +
//						dateFormat.format(date) + "/" +
//						(JmlRecordTransaksi()+1);
//			return no_faktur;
//		}
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String x = null;
		int jml = JmlRecordTransaksi() + JmlRecordTransaksiKredit() + 1;
		if (jml<10) x = "000" + String.valueOf(jml);
		else if (jml<100) x = "00" + String.valueOf(jml);
		else if (jml<1000) x = "0" + String.valueOf(jml);
		else x = String.valueOf(jml);
		no_faktur = dateFormat.format(date) + x;
		while (NoFakturTunaiAda(no_faktur)==true || NoFakturKreditAda(no_faktur)==true){
			jml++;
			if (jml<10) x = "000" + String.valueOf(jml);
			else if (jml<100) x = "00" + String.valueOf(jml);
			else if (jml<1000) x = "0" + String.valueOf(jml);
			else x = String.valueOf(jml);
			no_faktur = dateFormat.format(date) + x;
		}
		x = null;
		return no_faktur;
	}
	
	private int JmlRecordTransaksi(){
		int jml=0;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			Statement st;
			
			query = "select count(no_faktur) as jml from tb_transaksi";
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next()) jml = rs.getInt("jml");
			
			st.close();
			rs.close();
			conn.close();
			query = null;
		}
		catch(Exception e){System.out.println("Ada error bro data transaksi " + e);}
		
		return jml;
	}
	
	private int JmlRecordTransaksiKredit(){
		int jml=0;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			Statement st;
			
			query = "select count(no_faktur) as jml from tb_transaksi_kredit";
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next()) jml = rs.getInt("jml");
			
			st.close();
			rs.close();
			conn.close();
			query = null;
			
		}
		catch(Exception e){System.out.println("Ada error bro data transaksi " + e);}
		return jml;
	}
	
	
	private void CariDataPelanggan(String id_pelanggan){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			
			query = "select * from tb_pelanggan where id = ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,id_pelanggan);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			
			if (rowcount > 0){
				while (rs.next()) {
					txtNama.setText(rs.getString("nama"));
				}
				txtCari.requestFocus();
				
			}
			else {
				JOptionPane.showMessageDialog(txtIdPelanggan, "ID Pelanggan tidak ditemukan!!!");
				txtIdPelanggan.requestFocus();
			}
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cari datanya pelanggan " + e);}
	}
	
	private void SetBarang(String NamaField){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);

			String query=null;
			ResultSet rs;

			if (NamaField.equals("id_barang"))
				query = "SELECT id,nama from tb_barang where id = ?";
			else if (NamaField.equals("nama_barang"))
				query = "SELECT id,nama from tb_barang where nama = ?";

			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,list1.getSelectedValue().toString());
			psQuery.execute();
			rs = psQuery.getResultSet();

			FTransaksiSetBarang.main(null);
			window.frame.setEnabled(false);

			while (rs.next()) {
				FTransaksiSetBarang.IdBarang = rs.getString("id");
				FTransaksiSetBarang.NamaBarang = rs.getString("nama");
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cari datanya barang " + e);}
	}
	
	private void SetBarangByTextField(String NamaField){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);

			String query = null;
			ResultSet rs;
			PreparedStatement psQuery;
			
			if (NamaField.equals("id_barang"))
				query = "SELECT id,nama from tb_barang where id = ?";
			else if (NamaField.equals("nama_barang"))
				query = "SELECT id,nama from tb_barang where nama = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1,txtCari.getText());
			psQuery.execute();
			rs = psQuery.getResultSet();

			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			if (rowcount > 0){
				FTransaksiSetBarang.main(null);
				window.frame.setEnabled(false);
				while (rs.next()) {
					FTransaksiSetBarang.IdBarang = rs.getString("id");
					FTransaksiSetBarang.NamaBarang = rs.getString("nama");
				}
			}
			else
				JOptionPane.showMessageDialog(txtCari, "Data barangtidak ditemukan!!!");
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cari datanya barang " + e);}
	}
	
	private void SimpanTabelDetailTransaksi(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			PreparedStatement psQuery=null;
			for (byte i=0; i<table.getRowCount(); i++){
				String query = "insert into tb_detail_transaksi values (?,?,?,?,?,?)";
				psQuery = conn.prepareStatement(query);
				psQuery.setString(1, txtNoFaktur.getText());
				psQuery.setString(2, table.getValueAt(i, 0).toString());
				psQuery.setString(3, table.getValueAt(i, 3).toString().replace(",", ""));
				psQuery.setString(4, table.getValueAt(i, 2).toString());
				psQuery.setString(5, table.getValueAt(i, 4).toString());
				psQuery.setString(6, table.getValueAt(i, 6).toString().replace("%", ""));
				
				psQuery.execute();
			}
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan detail transaksi " + e);}
	}
	
	private void SimpanTabelTransaksi(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String tgl = dateFormat.format(date);
			PreparedStatement psQuery=null;
			String query = "insert into tb_transaksi values (?,?,?,?,?,?,?)";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtNoFaktur.getText());
			psQuery.setString(2, txtIdPelanggan.getText());
			psQuery.setString(3, txtTotalBayar.getText().replace(",", ""));
			psQuery.setString(4, tgl);
			psQuery.setString(5, FMain.id_karyawan);
			psQuery.setString(6, cmbSubTransaksi.getSelectedItem().toString());
			psQuery.setString(7, NamaSupir(cmbSupir.getSelectedItem().toString()));
			psQuery.execute();
				
			psQuery.close();
			conn.close();
			JOptionPane.showMessageDialog(null, "Data Berhasil Tersimpan...");
		}
		catch(Exception e){System.out.println("Ada error bro simpan transaksi " + e);}
	}
	
	private byte SetIndexCmbCari(){
		byte jml=0;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			Statement st;
			
			query = "select FTransaksi_index_cari as jml from tb_info";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next()) jml = rs.getByte("jml");
			
			st.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro data transaksi " + e);}
		
		return jml;
	}
	
	private void SimpanIndexCmbCari(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			
			PreparedStatement psQuery=null;
			String query = "update tb_info set FTransaksi_index_cari = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, String.valueOf(cmbCari.getSelectedIndex()));
			psQuery.execute();
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan index CmbCari " + e);}
	}
	
//	void SetDiskon(){
//		try{
//			String MyDriver = "com.mysql.jdbc.Driver";
//			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
//			Class.forName(MyDriver);
//			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
//			
//			String query=null;
//			ResultSet rs;
//			Statement st;
//			
//			query = "select * from tb_diskon";
//			st = conn.createStatement();
//			rs = st.executeQuery(query);
//			
//			while (rs.next()){
//				diskon = rs.getFloat("diskon");
//				StatusDiskon = rs.getString("status");
//			}
//			
//			st.close();
//			rs.close();
//			conn.close();
//			
//		}
//		catch(Exception e){System.out.println("Ada error bro set diskon " + e);}
//		if (StatusDiskon.equals("Aktif"))
//			lblDiskon.setText("Diskon: " + String.valueOf(diskon) + "%");
//		else if (StatusDiskon.equals("Non Aktif"))
//			lblDiskon.setText("Diskon: 0%");
//	}
	
	private void HapusBarisTabel(){
		int a = Integer.valueOf(txtTotalBayar.getText().replace(",", ""));
		int b = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 7).toString().replace(",", ""));
		TotalBayar =  a-b ;
		txtTotalBayar.setText(FMain.FormatAngka(TotalBayar));
		model.removeRow(table.getSelectedRow());
	}

	private void TransaksiBaru(){
		
		txtNoFaktur.setText(NoFakturTransaksiBaru());
		DateFormat dateFormat = new SimpleDateFormat("dd MMMMM yyyy");
		Date date = new Date();
		txtTanggal.setText(dateFormat.format(date));
		txtNama.setText("");
		txtCari.setText("");
		txtTotalBayar.setText("0");
		txtIdPelanggan.setText("");
		txtCash.setText("0");
		for(int i = table.getRowCount()-1; i>=0; i--){
			model.removeRow(i);
		}
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		cal.add(GregorianCalendar.MONTH, 1);
		dpTglJatuhTempo.setDate(cal.getTime());
		
		txtIdPelanggan.requestFocus();
		date=null;
		cal=null;
	}
	
	private void AturTabel(){
		
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		String[] columnNames = {"Kode Barang","Nama Barang", "Harga", "Qty", "Total", "Satuan" };
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
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setPreferredWidth(200);
		table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(5).setPreferredWidth(75);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
	}

	private void UbahStok(){
		short i=0;
		for (i=0; i<table.getRowCount(); i++){
			FMain.UpdateStok(table.getValueAt(i, 0).toString(), table.getValueAt(i, 2).toString(), 0, Integer.valueOf(table.getValueAt(i, 4).toString()));
		}
	}
	
	
	private void SimpanTransaksiTunai(){
		SimpanTabelDetailTransaksi();
		SimpanTabelTransaksi();
		if (cmbSubTransaksi.getSelectedItem().toString().equals("Eceran")){
			CetakStruk();
		}
	}
	
	private void SimpanTabelTransaksiKredit(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String tgl;
			PreparedStatement psQuery=null;
			String query = "insert into tb_transaksi_kredit values (?,?,?,?,?,?,?,?)";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtNoFaktur.getText());
			psQuery.setString(2, txtIdPelanggan.getText());
			psQuery.setString(3, txtTotalBayar.getText().replace(",", ""));
			
			tgl = dateFormat.format(date);
			psQuery.setString(4, tgl);
			psQuery.setString(5, FMain.id_karyawan);
			
			tgl = dateFormat.format(dpTglJatuhTempo.getDate());
			psQuery.setString(6, tgl);
			psQuery.setString(7, cmbSubTransaksi.getSelectedItem().toString());
			psQuery.setString(8, NamaSupir(cmbSupir.getSelectedItem().toString()));
			psQuery.execute();
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan transaksi kredit" + e);}
	}
	
	private void SimpanTabelDetailTransaksiKredit(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			PreparedStatement psQuery=null;
			for (byte i=0; i<table.getRowCount(); i++){
				String query = "insert into tb_detail_transaksi_kredit values (?,?,?,?,?,?)";
				psQuery = conn.prepareStatement(query);
				psQuery.setString(1, txtNoFaktur.getText());
				psQuery.setString(2, table.getValueAt(i, 0).toString());
				psQuery.setString(3, table.getValueAt(i, 3).toString().replace(",", ""));
				psQuery.setString(4, table.getValueAt(i, 2).toString());
				psQuery.setString(5, table.getValueAt(i, 4).toString());
				psQuery.setString(6, table.getValueAt(i, 6).toString().replace("%", ""));
				
				psQuery.execute();
			}
				
			psQuery.close();
			conn.close();
		}
		catch(Exception e){System.out.println("Ada error bro simpan detail transaksi kredit: " + e);}
	}
	
	private void SimpanTabelHutang(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			PreparedStatement psQuery=null;
			String query = "insert into tb_hutang values (?,?)";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtNoFaktur.getText());
			psQuery.setString(2, txtTotalBayar.getText().replace(",", ""));
			psQuery.execute();
				
			psQuery.close();
			conn.close();
			JOptionPane.showMessageDialog(null, "Data Kredit Berhasil Tersimpan...");
		}
		catch(Exception e){System.out.println("Ada error bro simpan tabel hutang" + e);}
	}
	
	private void SimpanTransaksiKredit(){
		SimpanTabelDetailTransaksiKredit();
		SimpanTabelTransaksiKredit();
		SimpanTabelHutang();
	}
	
//	private void TampilNamaSales(){
//		cmbNamaSales.removeAllItems();
//		cmbNamaSales.addItem("-PILIH SALES-");
//		try{
//			String MyDriver = "com.mysql.jdbc.Driver";
//			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
//			Class.forName(MyDriver);
//			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
//			
//			String query=null;
//			ResultSet rs;
//			Statement st;
//			
//			query = "select id, nama from tb_karyawan where bagian='sales'";
//			st = conn.createStatement();
//			rs = st.executeQuery(query);
//			
//			while (rs.next()){
//				cmbNamaSales.addItem(rs.getString("id") + " - " +rs.getString("nama"));
//			}
//			
//			st.close();
//			rs.close();
//			conn.close();
//			
//		}
//		catch(Exception e){System.out.println("Ada error bro set diskon " + e);}
//	}
	
	private void CetakStruk(){
		window.frame.setEnabled(false);
		
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			Map<String, Object> parametersMap = new HashMap<String, Object>();
			
			String reportName = "Laporan\\struk.jasper";
	        parametersMap.put(
	        		"paramNoFaktur",
	        		txtNoFaktur.getText()
	        );
	        parametersMap.put(
	        		"paramCash",
	        		Integer.valueOf(txtCash.getText().replace(",", ""))
	        );
	        
	        JasperPrint jp = JasperFillManager.fillReport(reportName, parametersMap, conn);
        	JRViewer jv = new JRViewer(jp);
        	
        	jf.getContentPane().add(jv);
        	jf.validate();
        	jf.setVisible(true);
        	jf.setExtendedState(jf.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        	jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        	WindowListener exitListener = new WindowAdapter() {
        	    @Override
        	    public void windowClosing(WindowEvent e) {
        	    	FTransaksi.window.frame.setEnabled(true);
    				jf.dispose();
    				jf = null;
    				System.gc();
        	    }
        	};
        	jf.addWindowListener(exitListener);
        	
        	
        	conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cetak struk" + e);}
	}
	
	private void TampilSupir(){
		cmbSupir.removeAllItems();
		cmbSupir.addItem("-PILIH SUPIR-");
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
			Statement st;
			
			query = "select * from tb_karyawan where bagian='supir'";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			while (rs.next()){
				cmbSupir.addItem(rs.getString("id") + " - " + rs.getString("nama"));
			}
			
			st.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro tampil supir " + e);}
	}
	
	private String NamaSupir(String Teks){
		if (cmbSupir.getSelectedIndex() == 0){
			return "-";
		}
		else{
			return Teks.substring(Teks.indexOf(" - ") + 3, Teks.length());
		}
		
	}
	
	private void SimpanData(){
		if (table.getRowCount() > 0){
			txtNoFaktur.setText(NoFakturTransaksiBaru());
			if (txtNama.getText().equals("")){
				txtIdPelanggan.setText("Umum");
				txtNama.setText("Umum");
			}
			UbahStok();
			if (cmbTransaksi.getSelectedItem().toString().equals("Tunai")) SimpanTransaksiTunai();
			else if (cmbTransaksi.getSelectedItem().toString().equals("Kredit")) SimpanTransaksiKredit();
			TransaksiBaru();
		}
		else
			JOptionPane.showMessageDialog(null, "Barang belum dipilih!!!");
	}
	
	private void SimpanEceran(){
		if (txtCash.getText().equals("")){
			JOptionPane.showMessageDialog(null, "Masukkan jumlah cash!!!");
			txtCash.requestFocus();
		}
		else{
			if (Integer.valueOf(txtCash.getText().replace(",", "")) < Integer.valueOf(txtTotalBayar.getText().replace(",", ""))){
				JOptionPane.showMessageDialog(null, "Masukkan jumlah cash yang benar!!!");
				txtCash.selectAll();
				txtCash.requestFocus();
			}
			else{
				String Pesan = "<html> " +
						"<font size='6'>Anda yakin ingin melanjutkan transaksi?</font><br> " +
						"<font color='blue' size='6'><b>"
						+ "Total Harga = Rp "
						+ txtTotalBayar.getText()
						+ "</font><br>"
						+ "<font color='red' size='7'>Kembalian = Rp "
						+ FMain.FormatAngka(Integer.valueOf(txtCash.getText().replace(",", "")) - Integer.valueOf(txtTotalBayar.getText().replace(",", "")))
						+ "</b></font> " +
						"</html>";
				JLabel lblPesan = new JLabel(Pesan);
				FMain.konfir = JOptionPane.showConfirmDialog(null, lblPesan, "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (FMain.konfir == JOptionPane.YES_OPTION)
					SimpanData();
				lblPesan = null;
			}
		}
	}
	
	private boolean UserSales(){
		int rowcount = 0;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;

			query = "select bagian from tb_karyawan where nama = ? and bagian = 'sales'";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, FMain.NamaUserLogin(FMain.Username));
			
			psQuery.execute();
			rs = psQuery.getResultSet();

			
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error " + e);}
		if (rowcount > 0) 
			return true;
		else 
			return false;
	}
	
	private boolean NoFakturTunaiAda(String KodeTransaksi){
		int rowcount = 0;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;

			query = "select no_faktur from tb_transaksi where no_faktur = ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, KodeTransaksi);
			
			psQuery.execute();
			rs = psQuery.getResultSet();

			
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error " + e);}
		if (rowcount > 0) 
			return true;
		else 
			return false;
	}
	
	private boolean NoFakturKreditAda(String KodeTransaksi){
		int rowcount = 0;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;

			query = "select no_faktur from tb_transaksi where no_faktur = ?";
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1, KodeTransaksi);
			
			psQuery.execute();
			rs = psQuery.getResultSet();

			
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error " + e);}
		if (rowcount > 0) 
			return true;
		else 
			return false;
	}
}
