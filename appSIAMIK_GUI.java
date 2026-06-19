import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// =========================================================
//  APP SIAMIK - VERSI GUI (Swing)
// =========================================================
public class appSIAMIK_GUI extends JFrame {

    // ---------- DATA STORAGE ----------
    private ArrayList<cMhs> mhsList = new ArrayList<>();
    private ArrayList<cMatkul> mkList = new ArrayList<>();
    private ArrayList<cKRS> krsList = new ArrayList<>();
    private ArrayList<cDosenTetap> dosenList = new ArrayList<>();
    private ArrayList<cTendik> tendikList = new ArrayList<>();
    private cProdi prodi = new cProdi();

    // ---------- TABLE MODELS ----------
    private DefaultTableModel mhsModel, mkModel, krsModel, dosenModel, tendikModel;
    private JLabel prodiInfoLabel;

    public appSIAMIK_GUI() {
        setTitle("SIAMIK - Sistem Informasi Akademik");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Mahasiswa", buildMahasiswaPanel());
        tabs.addTab("Mata Kuliah", buildMatkulPanel());
        tabs.addTab("KRS", buildKRSPanel());
        tabs.addTab("Program Studi", buildProdiPanel());
        tabs.addTab("Pegawai", buildPegawaiPanel());

        add(tabs);
    }

    // =====================================================
    //  TAB 1: MAHASISWA
    // =====================================================
    private JPanel buildMahasiswaPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mhsModel = new DefaultTableModel(new String[]{"NPM", "Nama", "IPK"}, 0);
        JTable table = new JTable(mhsModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField tfNPM = new JTextField();
        JTextField tfNama = new JTextField();
        JTextField tfIPK = new JTextField();

        form.add(new JLabel("NPM:"));
        form.add(tfNPM);
        form.add(new JLabel("Nama:"));
        form.add(tfNama);
        form.add(new JLabel("IPK:"));
        form.add(tfIPK);

        JPanel btnPanel = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update IPK");
        JButton btnHapus = new JButton("Hapus");
        btnPanel.add(btnTambah);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.CENTER);
        south.add(btnPanel, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        // Tambah Mahasiswa
        btnTambah.addActionListener(e -> {
            String npm = tfNPM.getText().trim();
            String nama = tfNama.getText().trim();
            String ipkStr = tfIPK.getText().trim();

            if (npm.isEmpty() || nama.isEmpty()) {
                JOptionPane.showMessageDialog(this, "NPM dan Nama wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (cMhs m : mhsList) {
                if (m.getNPM().equalsIgnoreCase(npm)) {
                    JOptionPane.showMessageDialog(this, "NPM Sudah Ada!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            double ipk = 0.0;
            try {
                if (!ipkStr.isEmpty()) ipk = Double.parseDouble(ipkStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "IPK harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cMhs m = new cMhs(npm, nama);
            m.setIPK(ipk);
            mhsList.add(m);
            mhsModel.addRow(new Object[]{npm, nama, ipk});
            tfNPM.setText(""); tfNama.setText(""); tfIPK.setText("");
            JOptionPane.showMessageDialog(this, "Penambahan Sukses...");
        });

        // Update IPK
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data mahasiswa pada tabel!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String ipkStr = tfIPK.getText().trim();
            if (ipkStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Masukkan IPK baru pada field IPK!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                double ipk = Double.parseDouble(ipkStr);
                mhsList.get(row).setIPK(ipk);
                mhsModel.setValueAt(ipk, row, 2);
                JOptionPane.showMessageDialog(this, "Update Sukses...");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "IPK harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Hapus
        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data mahasiswa pada tabel!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mhsList.remove(row);
                mhsModel.removeRow(row);
                JOptionPane.showMessageDialog(this, "Penghapusan Berhasil...");
            }
        });

        return panel;
    }

    // =====================================================
    //  TAB 2: MATA KULIAH
    // =====================================================
    private JPanel buildMatkulPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mkModel = new DefaultTableModel(new String[]{"Kode MK", "Nama MK", "SKS"}, 0);
        JTable table = new JTable(mkModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField tfKode = new JTextField();
        JTextField tfNama = new JTextField();
        JTextField tfSKS = new JTextField();

        form.add(new JLabel("Kode MK:"));
        form.add(tfKode);
        form.add(new JLabel("Nama MK:"));
        form.add(tfNama);
        form.add(new JLabel("SKS:"));
        form.add(tfSKS);

        JPanel btnPanel = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnHapus = new JButton("Hapus");
        btnPanel.add(btnTambah);
        btnPanel.add(btnHapus);

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.CENTER);
        south.add(btnPanel, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        btnTambah.addActionListener(e -> {
            String kode = tfKode.getText().trim();
            String nama = tfNama.getText().trim();
            String sksStr = tfSKS.getText().trim();

            if (kode.isEmpty() || nama.isEmpty() || sksStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (cMatkul m : mkList) {
                if (m.getKodeMK().equalsIgnoreCase(kode)) {
                    JOptionPane.showMessageDialog(this, "Kode MK Sudah Ada!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            int sks;
            try {
                sks = Integer.parseInt(sksStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "SKS harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cMatkul m = new cMatkul(kode, nama, sks);
            mkList.add(m);
            mkModel.addRow(new Object[]{kode, nama, sks});
            tfKode.setText(""); tfNama.setText(""); tfSKS.setText("");
            JOptionPane.showMessageDialog(this, "Penambahan Sukses...");
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data mata kuliah pada tabel!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mkList.remove(row);
                mkModel.removeRow(row);
                JOptionPane.showMessageDialog(this, "Penghapusan Berhasil...");
            }
        });

        return panel;
    }

    // =====================================================
    //  TAB 3: KRS
    // =====================================================
    private JPanel buildKRSPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        krsModel = new DefaultTableModel(new String[]{"Mahasiswa", "Semester", "Jml MK", "Total SKS"}, 0);
        JTable table = new JTable(krsModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        JComboBox<String> cbMhs = new JComboBox<>();
        JComboBox<String> cbMatkul = new JComboBox<>();
        JTextField tfSemester = new JTextField();
        JTextArea taDetail = new JTextArea(6, 30);
        taDetail.setEditable(false);

        form.add(new JLabel("Mahasiswa:"));
        form.add(cbMhs);
        form.add(new JLabel("Semester:"));
        form.add(tfSemester);
        form.add(new JLabel("Mata Kuliah:"));
        form.add(cbMatkul);

        JPanel btnPanel = new JPanel();
        JButton btnBuatKRS = new JButton("Buat KRS Baru");
        JButton btnTambahMK = new JButton("Tambah MK ke KRS Terpilih");
        JButton btnHapusMK = new JButton("Hapus MK dari KRS Terpilih");
        JButton btnLihat = new JButton("Lihat Detail KRS Terpilih");
        btnPanel.add(btnBuatKRS);
        btnPanel.add(btnTambahMK);
        btnPanel.add(btnHapusMK);
        btnPanel.add(btnLihat);

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.NORTH);
        south.add(btnPanel, BorderLayout.CENTER);
        south.add(new JScrollPane(taDetail), BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        // refresh combo box ketika tab dibuka
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshComboMhs(cbMhs);
                refreshComboMatkul(cbMatkul);
            }
        });

        btnBuatKRS.addActionListener(e -> {
            int idxMhs = cbMhs.getSelectedIndex();
            String semester = tfSemester.getText().trim();
            if (idxMhs < 0 || mhsList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data mahasiswa kosong, tambahkan mahasiswa dulu!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (semester.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semester wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cKRS krs = new cKRS();
            krs.setMhs(mhsList.get(idxMhs));
            krs.setSemester(semester);
            krsList.add(krs);
            krsModel.addRow(new Object[]{mhsList.get(idxMhs).getNama(), semester, 0, 0});
            JOptionPane.showMessageDialog(this, "KRS Baru Dibuat...");
        });

        btnTambahMK.addActionListener(e -> {
            int row = table.getSelectedRow();
            int idxMK = cbMatkul.getSelectedIndex();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih KRS pada tabel!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (idxMK < 0 || mkList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data mata kuliah kosong, tambahkan matkul dulu!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cKRS krs = krsList.get(row);
            cMatkul mk = mkList.get(idxMK);
            int sebelum = krs.getJMK();
            krs.tambahMatkul(mk);
            if (krs.getJMK() > sebelum) {
                krsModel.setValueAt(krs.getJMK(), row, 2);
                krsModel.setValueAt(krs.getTotalSks(), row, 3);
                JOptionPane.showMessageDialog(this, "Tambah MK sukses...");
            } else {
                JOptionPane.showMessageDialog(this, "Total SKS melebihi batas (20)!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnHapusMK.addActionListener(e -> {
            int row = table.getSelectedRow();
            int idxMK = cbMatkul.getSelectedIndex();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih KRS pada tabel!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (idxMK < 0 || mkList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data mata kuliah kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cKRS krs = krsList.get(row);
            cMatkul mk = mkList.get(idxMK);
            krs.hapusMatkul(mk.getKodeMK());
            krsModel.setValueAt(krs.getJMK(), row, 2);
            krsModel.setValueAt(krs.getTotalSks(), row, 3);
        });

        btnLihat.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih KRS pada tabel!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            cKRS krs = krsList.get(row);
            taDetail.setText(krs.ToString());
        });

        return panel;
    }

    private void refreshComboMhs(JComboBox<String> cb) {
        cb.removeAllItems();
        for (cMhs m : mhsList) {
            cb.addItem(m.getNPM() + " - " + m.getNama());
        }
    }

    private void refreshComboMatkul(JComboBox<String> cb) {
        cb.removeAllItems();
        for (cMatkul m : mkList) {
            cb.addItem(m.getKodeMK() + " - " + m.getNamaMK());
        }
    }

    // =====================================================
    //  TAB 4: PROGRAM STUDI
    // =====================================================
    private JPanel buildProdiPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField tfKode = new JTextField(prodi.getKodeProdi());
        JTextField tfNama = new JTextField(prodi.getNamaProdi());
        JTextField tfAkre = new JTextField(prodi.getAkreditasi());

        form.add(new JLabel("Kode Prodi:"));
        form.add(tfKode);
        form.add(new JLabel("Nama Prodi:"));
        form.add(tfNama);
        form.add(new JLabel("Akreditasi:"));
        form.add(tfAkre);

        JButton btnSimpan = new JButton("Simpan");
        prodiInfoLabel = new JLabel("Info: " + prodi.ToString());

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(btnSimpan, BorderLayout.SOUTH);

        panel.add(north, BorderLayout.NORTH);
        panel.add(prodiInfoLabel, BorderLayout.SOUTH);

        btnSimpan.addActionListener(e -> {
            String kode = tfKode.getText().trim();
            String nama = tfNama.getText().trim();
            String akre = tfAkre.getText().trim();
            if (kode.isEmpty() || nama.isEmpty() || akre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            prodi.setKodeProdi(kode);
            prodi.setNamaProdi(nama);
            prodi.setAkreditasi(akre);
            prodiInfoLabel.setText("Info: " + prodi.ToString());
            JOptionPane.showMessageDialog(this, "Data Program Studi Disimpan...");
        });

        return panel;
    }

    // =====================================================
    //  TAB 5: PEGAWAI (DOSEN & TENDIK)
    // =====================================================
    private JPanel buildPegawaiPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTabbedPane subTabs = new JTabbedPane();
        subTabs.addTab("Dosen", buildDosenPanel());
        subTabs.addTab("Tendik", buildTendikPanel());

        panel.add(subTabs, BorderLayout.CENTER);
        return panel;
    }

    // ---- SUB TAB DOSEN ----
    private JPanel buildDosenPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dosenModel = new DefaultTableModel(new String[]{"ID", "Nama", "Jabatan", "NIDN"}, 0);
        JTable table = new JTable(dosenModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField tfID = new JTextField();
        JTextField tfNama = new JTextField();
        JTextField tfJabatan = new JTextField();
        JTextField tfNIDN = new JTextField();

        form.add(new JLabel("ID:"));
        form.add(tfID);
        form.add(new JLabel("Nama:"));
        form.add(tfNama);
        form.add(new JLabel("Jabatan:"));
        form.add(tfJabatan);
        form.add(new JLabel("NIDN:"));
        form.add(tfNIDN);

        JPanel btnPanel = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update Jabatan");
        JButton btnHapus = new JButton("Hapus");
        btnPanel.add(btnTambah);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.CENTER);
        south.add(btnPanel, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        btnTambah.addActionListener(e -> {
            String id = tfID.getText().trim();
            String nama = tfNama.getText().trim();
            String jabatan = tfJabatan.getText().trim();
            String nidn = tfNIDN.getText().trim();

            if (id.isEmpty() || nama.isEmpty() || jabatan.isEmpty() || nidn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (cDosenTetap d : dosenList) {
                if (d.getID().equalsIgnoreCase(id)) {
                    JOptionPane.showMessageDialog(this, "ID Sudah Ada!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            cDosenTetap d = new cDosenTetap(id, nama, jabatan, nidn);
            dosenList.add(d);
            dosenModel.addRow(new Object[]{id, nama, jabatan, nidn});
            tfID.setText(""); tfNama.setText(""); tfJabatan.setText(""); tfNIDN.setText("");
            JOptionPane.showMessageDialog(this, "Penambahan Sukses...");
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data dosen pada tabel!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String jabatanBaru = tfJabatan.getText().trim();
            if (jabatanBaru.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Masukkan jabatan baru pada field Jabatan!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            dosenList.get(row).setJabatan(jabatanBaru);
            dosenModel.setValueAt(jabatanBaru, row, 2);
            JOptionPane.showMessageDialog(this, "Update Sukses...");
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data dosen pada tabel!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dosenList.remove(row);
                dosenModel.removeRow(row);
                JOptionPane.showMessageDialog(this, "Penghapusan Berhasil...");
            }
        });

        return panel;
    }

    // ---- SUB TAB TENDIK ----
    private JPanel buildTendikPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tendikModel = new DefaultTableModel(new String[]{"ID", "Nama", "Bidang"}, 0);
        JTable table = new JTable(tendikModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField tfID = new JTextField();
        JTextField tfNama = new JTextField();
        JTextField tfBidang = new JTextField();

        form.add(new JLabel("ID:"));
        form.add(tfID);
        form.add(new JLabel("Nama:"));
        form.add(tfNama);
        form.add(new JLabel("Bidang:"));
        form.add(tfBidang);

        JPanel btnPanel = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update Bidang");
        JButton btnHapus = new JButton("Hapus");
        btnPanel.add(btnTambah);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.CENTER);
        south.add(btnPanel, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        btnTambah.addActionListener(e -> {
            String id = tfID.getText().trim();
            String nama = tfNama.getText().trim();
            String bidang = tfBidang.getText().trim();

            if (id.isEmpty() || nama.isEmpty() || bidang.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (cTendik t : tendikList) {
                if (t.getID().equalsIgnoreCase(id)) {
                    JOptionPane.showMessageDialog(this, "ID Sudah Ada!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            cTendik t = new cTendik(id, nama, bidang);
            tendikList.add(t);
            tendikModel.addRow(new Object[]{id, nama, bidang});
            tfID.setText(""); tfNama.setText(""); tfBidang.setText("");
            JOptionPane.showMessageDialog(this, "Penambahan Sukses...");
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data tendik pada tabel!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String bidangBaru = tfBidang.getText().trim();
            if (bidangBaru.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Masukkan bidang baru pada field Bidang!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            tendikList.get(row).setBidang(bidangBaru);
            tendikModel.setValueAt(bidangBaru, row, 2);
            JOptionPane.showMessageDialog(this, "Update Sukses...");
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Pilih data tendik pada tabel!", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tendikList.remove(row);
                tendikModel.removeRow(row);
                JOptionPane.showMessageDialog(this, "Penghapusan Berhasil...");
            }
        });

        return panel;
    }

    // =====================================================
    //  MAIN
    // =====================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new appSIAMIK_GUI().setVisible(true);
        });
    }
}

// ==================== CLASS CDOSENTETAP ====================
class cDosenTetap {
    private String ID;
    private String nama;
    private String jabatan;
    private String NIDN;

    public cDosenTetap(String id, String nm, String jbt, String nidn) {
        ID = id;
        nama = nm;
        jabatan = jbt;
        NIDN = nidn;
    }

    public String getID() { return ID; }
    public String getNama() { return nama; }
    public String getJabatan() { return jabatan; }
    public String getNIDN() { return NIDN; }
    public void setJabatan(String jbt) { jabatan = jbt; }

    public void info() {
        System.out.println("ID: " + ID + ", Nama: " + nama + ", Jabatan: " + jabatan + ", NIDN: " + NIDN);
    }
}

// ==================== CLASS CTENDIK ====================
class cTendik {
    private String ID;
    private String nama;
    private String bidang;

    public cTendik(String id, String nm, String bdg) {
        ID = id;
        nama = nm;
        bidang = bdg;
    }

    public String getID() { return ID; }
    public String getNama() { return nama; }
    public String getBidang() { return bidang; }
    public void setBidang(String bdg) { bidang = bdg; }

    public void info() {
        System.out.println("ID: " + ID + ", Nama: " + nama + ", Bidang: " + bidang);
    }
}

// ==================== CLASS CMHS ====================
class cMhs {
    private String nama;
    private String NPM;
    private double IPK;

    cMhs() {}

    cMhs(String np, String nm) {
        NPM = np;
        nama = nm;
        IPK = 0.0;
    }

    public void setNPM(String n) { NPM = n; }
    public void setNama(String n) { nama = n; }
    public void setIPK(double i) { IPK = i; }
    public String getNPM() { return NPM; }
    public String getNama() { return nama; }
    public double getIPK() { return IPK; }

    public String ToString() {
        return NPM + "\t" + nama + "\t" + IPK;
    }
}

// ==================== CLASS CMATKUL ====================
class cMatkul {
    private String kodeMK;
    private String namaMK;
    private int SKS;

    cMatkul() {}

    cMatkul(String kd, String nm, int s) {
        kodeMK = kd;
        namaMK = nm;
        SKS = s;
    }

    public void setKodeMK(String k) { kodeMK = k; }
    public void setNamaMK(String k) { namaMK = k; }
    public void setSKS(int k) { SKS = k; }
    public String getKodeMK() { return kodeMK; }
    public String getNamaMK() { return namaMK; }
    public int getSKS() { return SKS; }

    public String ToString() {
        return kodeMK + "\t" + namaMK + "\t" + SKS + " SKS";
    }
}

// ==================== CLASS CKRS ====================
class cKRS {
    private cMhs mhs;
    private cMatkul mk[];
    private int jmk;
    private String semester;
    private int totalSKS;

    cKRS() {
        mhs = null;
        mk = new cMatkul[10];
        jmk = 0;
        totalSKS = 0;
    }

    public void tambahMatkul(cMatkul m) {
        if ((totalSKS + m.getSKS()) <= 20 && jmk < mk.length) {
            mk[jmk] = m;
            jmk++;
            totalSKS = totalSKS + m.getSKS();
        }
    }

    public void hapusMatkul(String kd) {
        for (int i = 0; i < jmk; i++) {
            if (kd.equalsIgnoreCase(mk[i].getKodeMK())) {
                totalSKS = totalSKS - mk[i].getSKS();
                for (int j = i; j < jmk - 1; j++) {
                    mk[j] = mk[j + 1];
                }
                mk[jmk - 1] = null;
                jmk--;
                break;
            }
        }
    }

    public void setMhs(cMhs m) { mhs = m; }
    public void setMatkul(cMatkul[] m) { mk = m; }
    public void setSemester(String s) { semester = s; }
    public int getJMK() { return jmk; }
    public int getTotalSks() { return totalSKS; }
    public cMhs getMhs() { return mhs; }
    public cMatkul[] getMatkul() { return mk; }
    public String getSemester() { return semester; }

    public String ToString() {
        String temp = "KRS Semester " + semester + " - ";
        temp = temp + mhs.getNama();
        temp = temp + "\tSKS : " + totalSKS;
        temp = temp + "\nNo.\tKode\t\tNama MK\t\t\tSKS\n";
        for (int i = 0; i < jmk; i++) {
            temp = temp + (i + 1) + ".\t";
            temp = temp + mk[i].getKodeMK() + "\t\t";
            temp = temp + mk[i].getNamaMK() + "\t\t\t";
            temp = temp + mk[i].getSKS() + "\n";
        }
        return temp;
    }
}

// ==================== CLASS CPRODI ====================
class cProdi {
    private String kodeProdi;
    private String namaProdi;
    private String akreditasi;
    private cMhs mhs;

    cProdi() {
        kodeProdi = "XYZ";
        namaProdi = "ABC";
        akreditasi = "N/A";
        mhs = null;
    }

    cProdi(String kd, String nm, String ak) {
        kodeProdi = kd;
        namaProdi = nm;
        akreditasi = ak;
        mhs = null;
    }

    public void setMhs(cMhs m) { mhs = m; }
    public cMhs getMhs() { return mhs; }
    public void deleteMhs() { mhs = null; }
    public void setKodeProdi(String k) { kodeProdi = k; }
    public void setNamaProdi(String k) { namaProdi = k; }
    public void setAkreditasi(String k) { akreditasi = k; }
    public String getNamaProdi() { return namaProdi; }
    public String getAkreditasi() { return akreditasi; }
    public String getKodeProdi() { return kodeProdi; }

    public String ToString() {
        return kodeProdi + "\t" + namaProdi + "\t" + akreditasi;
    }
}