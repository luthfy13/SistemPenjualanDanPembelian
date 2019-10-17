import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FMain {

	JFrame frame;
	static String LevelUser=null, Username=null,user=null,pass=null,host=null,id_karyawan=null;
	static FMain window;
	private JMenuBar menuBar;
	private JMenu mnUser;
	private JMenu mnSetting;
	private JMenu mnLaporan;
	private JMenuItem mntmPembayaran;
	//private KeyStroke keyStroke;
	static int konfir=0;
	private JMenuItem mntmPembelian;
	private JFrame jf = new JFrame();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FMain();
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
	public FMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JLabel lblBackGround = new JLabel("");
		lblBackGround.setIcon(new ImageIcon(".\\icons\\bg.jpg"));
		frame.getContentPane().add(lblBackGround, BorderLayout.CENTER);
		frame.setTitle("Sistem Informasi Penjualan");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\icons\\Toko.png"));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				BacaUser();
				mnUser.setText(Username);
				if (LevelUser.equals("admin"))
					LoginAdmin();
//				else if (LevelUser.equals("kasir_grosir"))
//					LoginGrosir();
//				else if (LevelUser.equals("kasir_eceran"))
//					LoginEceran();
//				else if (LevelUser.equals("sales"))
//					LoginSales();
				else if (LevelUser.equals("pos"))
					LoginPOS();
				else
					System.exit(0);
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				konfir = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menutup aplikasi?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (konfir == JOptionPane.YES_OPTION) System.exit(0);
			}
		});
		frame.setBounds(100, 100, 1366, 768);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnSetting = new JMenu("Setting");
		mnSetting.setIcon(new ImageIcon(".\\icons\\Setting.png"));
		menuBar.add(mnSetting);
		
		JMenuItem mntmUser = new JMenuItem("User");
		mntmUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FSettingUser.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnSetting.add(mntmUser);
		
		JMenuItem mntmPelanggan = new JMenuItem("Pelanggan");
		mntmPelanggan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FPelanggan.main(null);
				window.frame.setEnabled(false);
			}
		});
		
		JMenuItem mntmKaryawan = new JMenuItem("Karyawan");
		mntmKaryawan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FKaryawan.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnSetting.add(mntmKaryawan);
		mnSetting.add(mntmPelanggan);
		
		JMenu mnBarang = new JMenu("Barang");
		mnSetting.add(mnBarang);
		
		JMenuItem mntmStokBarang = new JMenuItem("Stok Barang");
		mnBarang.add(mntmStokBarang);
		
		JMenuItem mntmSatuanBarang = new JMenuItem("Satuan Barang");
		mntmSatuanBarang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FSatuan.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnBarang.add(mntmSatuanBarang);
		
		JMenuItem mntmSupplier = new JMenuItem("Supplier");
		mntmSupplier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FSupplier.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnBarang.add(mntmSupplier);
		
		JMenuItem mntmDiskon = new JMenuItem("Diskon");
		mnBarang.add(mntmDiskon);
		
		JMenuItem mntmProfilePemilik = new JMenuItem("Profile Pemilik");
		mntmProfilePemilik.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FProfilePemilik.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnSetting.add(mntmProfilePemilik);
		mntmDiskon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FSetDiskon.main(null);
				window.frame.setEnabled(false);
			}
		});
		mntmStokBarang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FStokBarang.main(null);
				window.frame.setEnabled(false);
			}
		});
		
		JMenu mnTransaksi = new JMenu("Transaksi");
		mnTransaksi.setIcon(new ImageIcon(".\\icons\\Sell.png"));
		menuBar.add(mnTransaksi);
		
		JMenuItem mntmPenjualan = new JMenuItem("Penjualan");
		mntmPenjualan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FTransaksi.main(null);
				FTransaksi.JudulForm = "Transaksi Penjualan Grosir Tunai";
				window.frame.setEnabled(false);
			}
		});
		mnTransaksi.add(mntmPenjualan);		
		mntmPembayaran = new JMenuItem("Pembayaran Piutang");
		mntmPembayaran.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FPembayaran.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnTransaksi.add(mntmPembayaran);
		
		mntmPembelian = new JMenuItem("Pembelian");
		mntmPembelian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FPembelian.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnTransaksi.add(mntmPembelian);
		
		mnLaporan = new JMenu("Laporan");
		mnLaporan.setIcon(new ImageIcon(".\\icons\\Report.png"));
		menuBar.add(mnLaporan);
		
		JMenu mnSeluruhTransaksiPenjualan = new JMenu("Seluruh Transaksi Penjualan");
		mnLaporan.add(mnSeluruhTransaksiPenjualan);
		
		JMenuItem mntmTahunan = new JMenuItem("Tahunan");
		mntmTahunan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FLapSeluruhPenjualanTahunan.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnSeluruhTransaksiPenjualan.add(mntmTahunan);
		
		JMenuItem mntmBulanan = new JMenuItem("Bulanan");
		mntmBulanan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FLapSeluruhPenjualanBulanan.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnSeluruhTransaksiPenjualan.add(mntmBulanan);
		
		JMenuItem mntmHarian = new JMenuItem("Harian");
		mntmHarian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FLapSeluruhPenjualanHarian.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnSeluruhTransaksiPenjualan.add(mntmHarian);
		
		JMenuItem mntmRangeWaktu = new JMenuItem("Range Waktu");
		mntmRangeWaktu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FLapSeluruhPenjualanRangeWaktu.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnSeluruhTransaksiPenjualan.add(mntmRangeWaktu);
		
		JMenu mnLaporanPerSales = new JMenu("Laporan per sales");
		mnLaporan.add(mnLaporanPerSales);
		
		JMenuItem mntmTahunan_1 = new JMenuItem("Tahunan");
		mntmTahunan_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FLapSalesTahunan.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnLaporanPerSales.add(mntmTahunan_1);
		
		JMenuItem mntmBulanan_1 = new JMenuItem("Bulanan");
		mntmBulanan_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FLapSalesBulanan.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnLaporanPerSales.add(mntmBulanan_1);
		
		JMenuItem mntmHarian_1 = new JMenuItem("Harian");
		mntmHarian_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FLapSalesHarian.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnLaporanPerSales.add(mntmHarian_1);
		
		JMenu mnTransaksiTunai = new JMenu("Transaksi Tunai");
		mnLaporan.add(mnTransaksiTunai);
		
		JMenuItem mntmLaporanTahunan = new JMenuItem("Laporan Tahunan");
		mntmLaporanTahunan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FReportTahunan.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnTransaksiTunai.add(mntmLaporanTahunan);
		
		JMenuItem mntmCetakLaporan = new JMenuItem("Laporan Bulanan");
		mntmCetakLaporan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FReport.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnTransaksiTunai.add(mntmCetakLaporan);
		
		
		JMenuItem mntmLaporanHarian = new JMenuItem("Laporan Harian");
		mntmLaporanHarian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FReportHarian.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnTransaksiTunai.add(mntmLaporanHarian);
		
		
		JMenu mnTransaksiKredit = new JMenu("Transaksi Kredit");
		mnLaporan.add(mnTransaksiKredit);
		
		JMenuItem mntmLaporanTahunan_1 = new JMenuItem("Laporan Tahunan");
		mntmLaporanTahunan_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FKreditTahunan.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnTransaksiKredit.add(mntmLaporanTahunan_1);
		
		JMenuItem mntmLaporanBulanan = new JMenuItem("Laporan Bulanan");
		mntmLaporanBulanan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FKreditBulanan.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnTransaksiKredit.add(mntmLaporanBulanan);
		
		JMenuItem mntmLaporanHarian_1 = new JMenuItem("Laporan Harian");
		mntmLaporanHarian_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FKreditHarian.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnTransaksiKredit.add(mntmLaporanHarian_1);
		
		JMenu mnBarangKeluar = new JMenu("Barang Keluar");
		mnLaporan.add(mnBarangKeluar);
		
		JMenuItem mntmPerHari = new JMenuItem("Per Hari");
		mntmPerHari.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FBarangPerHari.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnBarangKeluar.add(mntmPerHari);
		
		JMenuItem mntmPerBulan = new JMenuItem("Per Bulan");
		mntmPerBulan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FBarangPerBulan.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnBarangKeluar.add(mntmPerBulan);
		
		JMenuItem mntmPerTahun = new JMenuItem("Per Tahun");
		mntmPerTahun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FBarangPerTahun.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnBarangKeluar.add(mntmPerTahun);
		
		JMenuItem mntmCetakFaktur = new JMenuItem("Cetak Faktur");
		mntmCetakFaktur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FCetakFaktur.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnLaporan.add(mntmCetakFaktur);
		
		JMenuItem mntmDaftarTagihanHarian = new JMenuItem("Daftar Tagihan Harian");
		mntmDaftarTagihanHarian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FCetakDaftarTagihanHarian.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnLaporan.add(mntmDaftarTagihanHarian);
		
		JMenuItem mntmLaporanNilaiDan = new JMenuItem("Laporan Nilai dan Stok Barang");
		mntmLaporanNilaiDan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LaporanStokDanNilaiBarang();
			}
		});
		mnLaporan.add(mntmLaporanNilaiDan);
		
		JMenuItem mntmLaporanPembelian = new JMenuItem("Laporan Pembelian");
		mntmLaporanPembelian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FReportPembelian.main(null);
				window.frame.setEnabled(false);
			}
		});
		mnLaporan.add(mntmLaporanPembelian);
		
		mnUser = new JMenu("user");
		mnUser.setIcon(new ImageIcon(".\\icons\\User.png"));
		menuBar.add(mnUser);
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.setIcon(new ImageIcon(".\\icons\\Logout.png"));
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FLogin.main(null);
				FLogin.ReportInitialized = true;
				LevelUser=null; Username=null;user=null;pass=null;host=null;id_karyawan=null;
				menuBar  = null;
				mnUser  = null;
				mnSetting  = null;
				mnLaporan  = null;
				mntmPembayaran  = null;
				mntmPembelian  = null;
				window.frame.dispose();
				frame.dispose();
				window.frame = null;
				frame = null;
				window = null;
				System.gc();
			}
		});
		mnUser.add(mntmLogout);
		
		JMenuItem mntmKeluar = new JMenuItem("Tutup Aplikasi");
		mntmKeluar.setIcon(new ImageIcon(".\\icons\\Tutup.png"));
		mntmKeluar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				konfir = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menutup aplikasi?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
				if (konfir == JOptionPane.YES_OPTION) System.exit(0);
			}
		});
		mnUser.add(mntmKeluar);
	}
	
//	private void LoginGrosir(){
//		mnSetting.setEnabled(false);
//		mnLaporan.setEnabled(false);
//		mntmEceran.setEnabled(false);
//		mntmGrosir.setEnabled(true);
//		mntmGrosirKredit.setEnabled(true);
//		mntmPembayaran.setEnabled(false);
//		mntmSalesKredit.setEnabled(false);
//		mntmSalesTunai.setEnabled(false);
//		mntmPembelian.setEnabled(false);
//
//	}
	
//	private void LoginEceran(){
//		mnSetting.setEnabled(false);
//		mnLaporan.setEnabled(false);
//		mntmEceran.setEnabled(true);
//		mntmGrosir.setEnabled(false);
//		mntmGrosirKredit.setEnabled(false);
//		mntmPembayaran.setEnabled(false);
//		mntmSalesKredit.setEnabled(false);
//		mntmSalesTunai.setEnabled(false);
//		mntmPembelian.setEnabled(false);
//	}
	
	private void LoginAdmin(){
		mnSetting.setEnabled(true);
		mnLaporan.setEnabled(true);
		mntmPembelian.setEnabled(true);
	}
	
//	private void LoginSales(){
//		mnSetting.setEnabled(false);
//		mnLaporan.setEnabled(false);
//		mntmEceran.setEnabled(false);
//		mntmGrosir.setEnabled(false);
//		mntmGrosirKredit.setEnabled(false);
//		mntmPembayaran.setEnabled(false);
//		mntmSalesKredit.setEnabled(true);
//		mntmSalesTunai.setEnabled(true);
//		mntmPembelian.setEnabled(false);
//	}
	
	private void LoginPOS(){
		mnSetting.setEnabled(false);
		mnLaporan.setEnabled(false);
//		mntmPembayaran.setEnabled(false);
//		mntmPembelian.setEnabled(false);
	}
	
	private void BacaUser(){
		String fileName = "setserver";
		try {
            
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            user = bufferedReader.readLine();
            pass = bufferedReader.readLine();
            host = bufferedReader.readLine();
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
	}
	
	static String FormatAngka(int Angka){
		NumberFormat numberFormatter = new DecimalFormat("#,##0");
		return numberFormatter.format(Angka);
	}
	
	static int TotalBayarTransaksiTunai(String NoFaktur){
		int hasil=0;
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
						
			String query=null;
			ResultSet rs;
	
			query = "SELECT total_bayar from tb_transaksi where no_faktur = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,NoFaktur);
			psQuery.execute();
			rs = psQuery.getResultSet();
			while (rs.next()){
				hasil = rs.getInt("total_bayar");
			};	
			
			
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error dapat total bayar: " + e);
		}
		return hasil;
	}
	
	static int TotalBayarTransaksiKredit(String NoFaktur){
		int hasil=0;
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
						
			String query=null;
			ResultSet rs;
	
			query = "SELECT total_bayar from tb_transaksi_kredit where no_faktur = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,NoFaktur);
			psQuery.execute();
			rs = psQuery.getResultSet();
			while (rs.next()){
				hasil = rs.getInt("total_bayar");
			};	
			
			
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error dapat total bayar kredit: " + e);
		}
		return hasil;
	}
	
	static int SisaHutang(String NoFaktur){
		int hasil=0;
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
				hasil = rs.getInt("sisa_hutang");
			};	
			
			
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error dapat total bayar kredit: " + e);
		}
		return hasil;
	}
	
	static void UpdateStok(String IdBarang, String Satuan, int QtyLama, int QtyBaru){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			int Stok = StokBarang(IdBarang, Satuan) - (QtyBaru - QtyLama);
			
			String query = "update tb_detail_barang set stok=?  where id_barang = ? and satuan = ?";
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setInt(1, Stok);
			psQuery.setString(2, IdBarang);
			psQuery.setString(3, Satuan);
			psQuery.execute();
			
			psQuery.close();
			conn.close();
			
			TambahOtomatisStok(IdBarang, Satuan);
			
		} catch (Exception eee) {
			JOptionPane.showMessageDialog(null, "Edit Data Gagal");
			System.out.println("Update stok error: " + eee);
		}
	}
	
	private static void TambahOtomatisStok(String IdBarang, String Satuan){
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			PreparedStatement psQuery=null;
			int stok=0, isi=0, stok2=0;
			ResultSet rs;

			query = "SELECT stok from tb_detail_barang where id_barang = ? and satuan = ?";

			psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdBarang);
			psQuery.setString(2,Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			while (rs.next()){
				stok = rs.getInt("stok");
			};
			
			query = "SELECT isi,stok from tb_detail_barang where id_barang = ? and satuan_isi = ?";
			psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdBarang);
			psQuery.setString(2,Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			while (rs.next()){
				isi = rs.getInt("isi");
				stok2 = rs.getInt("stok");
			};
			
			if (stok2 > 0 && stok < isi){
				query = "update tb_detail_barang set stok = stok + ? where id_barang = ? and satuan = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setInt(1,isi);
				psQuery.setString(2,IdBarang);
				psQuery.setString(3,Satuan);
				psQuery.execute();
				
				query = "update tb_detail_barang set stok = stok - 1 where id_barang = ? and satuan_isi = ?";
				psQuery = conn.prepareStatement(query);
				psQuery.setString(1,IdBarang);
				psQuery.setString(2,Satuan);
				psQuery.execute();
			}
			
			psQuery.close();
			rs.close();
			conn.close();
			query=null;
			psQuery=null;
			rs=null;
			
		} catch (Exception eee) {
			System.out.println("Tambah otomatis stok error: " + eee);
		}
	}
	
	static int StokBarang(String IdBarang, String Satuan){
		int hasil=0;
		try {
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
						
			String query=null;
			ResultSet rs;
	
			query = "SELECT stok from tb_detail_barang where id_barang = ? and satuan = ?";
			
			
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdBarang);
			psQuery.setString(2,Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();
			while (rs.next()){
				hasil = rs.getInt("stok");
			};	
			psQuery.close();
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("error dapat stok barang: " + e);
		}
		return hasil;
	}
	
	static float Diskon(String IdBarang, String Satuan){
		float hasil=0;
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + FMain.host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, FMain.user, FMain.pass);
			
			String query=null;
			ResultSet rs;
	
			query = "SELECT diskon from tb_diskon where id_barang=? and satuan=?";
						
			PreparedStatement psQuery = conn.prepareStatement(query);
			psQuery.setString(1,IdBarang);
			psQuery.setString(2,Satuan);
			psQuery.execute();
			rs = psQuery.getResultSet();
			
			int rowcount = 0;
			if (rs.last()) {
			  rowcount = rs.getRow();
			  rs.beforeFirst(); 
			}
			
			if (rowcount > 0){
				while (rs.next()) {
					hasil = rs.getFloat("diskon");
				}				
			}
			else
				hasil=0;
			
			psQuery.close();
			rs.close();
			conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cari diskon " + e);}
		return hasil;
	}
	
	static String NamaUserLogin(String nama){
		return nama.substring(nama.indexOf(": ") + 2, nama.indexOf("(") - 1);
	}
	
	private void LaporanStokDanNilaiBarang(){
		window.frame.setEnabled(false);
		try{
			String MyDriver = "com.mysql.jdbc.Driver";
			String MyUrl    = "jdbc:mysql://" + host + "/db_penjualan";
			Class.forName(MyDriver);
			final Connection conn = DriverManager.getConnection(MyUrl, user, pass);
						
			String reportName = ".\\Laporan\\LapNilaiBarang.jasper";
	        
			JasperPrint jp = JasperFillManager.fillReport(reportName, null, conn);
        	JRViewer jv = new JRViewer(jp);
        	jf.getContentPane().add(jv);
        	jf.validate();
        	jf.setVisible(true);
        	jf.setExtendedState(jf.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        	jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        	WindowListener exitListener = new WindowAdapter() {
        	    @Override
        	    public void windowClosing(WindowEvent e) {
        	    	FMain.window.frame.setEnabled(true);
    				jf.dispose();
    				jf = null;
    				System.gc();
        	    }
        	};
        	jf.addWindowListener(exitListener);
        	conn.close();
			
		}
		catch(Exception e){System.out.println("Ada error bro cetak lap nilai barang" + e);}
		//FTransaksi.window.frame.setEnabled(true);
	}
}
