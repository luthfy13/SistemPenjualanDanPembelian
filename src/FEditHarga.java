import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class FEditHarga {

	private JFrame frame;
	static FEditHarga window;
	static String IdBarang = null;
	static String NamaBarang = null;
	static String Satuan = null;
	static String Harga = null;
	private JTextField txtIdBarang;
	private JTextField txtNamaBarang;
	private JTextField txtHargaLama;
	private JTextField txtHargaBaru;
	private JLabel Label1;
	private JLabel Label2;
	private NumberFormat numberFormatter = new DecimalFormat("#,##0");
	private int angka=0;
	private String formattedNumber=null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FEditHarga();
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
	public FEditHarga() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Edit Harga");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				txtIdBarang.setText(IdBarang);
				txtNamaBarang.setText(NamaBarang);
				
				
				angka = Integer.valueOf(Harga);
				formattedNumber = numberFormatter.format(angka);
				txtHargaLama.setText(formattedNumber);
				
				Label1.setText("/ " + Satuan);
				Label2.setText("/ " + Satuan);
				txtHargaBaru.requestFocus();
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
		frame.setBounds(100, 100, 300, 169);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		JLabel lblIdBarang = new JLabel("ID Barang");
		lblIdBarang.setBounds(10, 11, 78, 14);
		frame.getContentPane().add(lblIdBarang);
		
		JLabel lblNamaBarang = new JLabel("Nama Barang");
		lblNamaBarang.setBounds(10, 36, 78, 14);
		frame.getContentPane().add(lblNamaBarang);
		
		JLabel lblHargaLama = new JLabel("Harga Lama");
		lblHargaLama.setBounds(10, 61, 78, 14);
		frame.getContentPane().add(lblHargaLama);
		
		JLabel lblHargaBaru = new JLabel("Harga Baru");
		lblHargaBaru.setBounds(10, 86, 78, 14);
		frame.getContentPane().add(lblHargaBaru);
		
		txtIdBarang = new JTextField();
		txtIdBarang.setEditable(false);
		txtIdBarang.setBounds(98, 8, 185, 20);
		frame.getContentPane().add(txtIdBarang);
		txtIdBarang.setColumns(10);
		
		txtNamaBarang = new JTextField();
		txtNamaBarang.setEditable(false);
		txtNamaBarang.setBounds(98, 33, 185, 20);
		frame.getContentPane().add(txtNamaBarang);
		txtNamaBarang.setColumns(10);
		
		txtHargaLama = new JTextField();
		txtHargaLama.setEditable(false);
		txtHargaLama.setBounds(98, 58, 89, 20);
		frame.getContentPane().add(txtHargaLama);
		txtHargaLama.setColumns(10);
		
		txtHargaBaru = new JTextField();
		txtHargaBaru.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				if (! (arg0.getKeyCode() == KeyEvent.VK_LEFT || arg0.getKeyCode() == KeyEvent.VK_RIGHT)){
					try {
						txtHargaBaru.setText(FMain.FormatAngka(Integer.valueOf(txtHargaBaru.getText().replace(",", ""))));
					} catch (Exception er) {
						if (!txtHargaBaru.getText().equals(""))
							JOptionPane.showMessageDialog(null, "Batas maksimum = 2.147.483.647!!!");
						txtHargaBaru.setText("");
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
		txtHargaBaru.setBounds(98, 83, 89, 20);
		frame.getContentPane().add(txtHargaBaru);
		txtHargaBaru.setColumns(10);
		
		JButton btnSimpan = new JButton("Simpan");
		btnSimpan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FMain.konfir = JOptionPane.showConfirmDialog(null, "Apakah data yakin ingin mengubah harga\n" + 
						 txtIdBarang.getText() + " - " + txtNamaBarang.getText() +
						 "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (FMain.konfir == JOptionPane.YES_OPTION){
					UpdateHarga();
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
		btnSimpan.setBounds(10, 111, 89, 23);
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
		btnBatal.setBounds(108, 111, 89, 23);
		frame.getContentPane().add(btnBatal);
		
		Label1 = new JLabel("New label");
		Label1.setBounds(197, 61, 46, 14);
		frame.getContentPane().add(Label1);
		
		Label2 = new JLabel("New label");
		Label2.setBounds(197, 86, 46, 14);
		frame.getContentPane().add(Label2);
	}
	
	private void UpdateHarga(){
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			PreparedStatement psQuery = null;
			String query = "update tb_detail_barang set harga_jual = ? where id_barang = ? and satuan = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1, txtHargaBaru.getText().replace(",", ""));
			psQuery.setString(2, IdBarang);
			psQuery.setString(3, Satuan);
			psQuery.execute();
				
			psQuery.close();
			conn.close();
			JOptionPane.showMessageDialog(null, "Harga berhasil diubah...");
		}
		catch(Exception e){System.out.println("Ada error bro simpan ubah stok satuan asal" + e);}
	}
}
