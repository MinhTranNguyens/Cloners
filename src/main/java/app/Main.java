package app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("======== KIỂM TRA QL SÁCH (Sử dụng Set) ========");
            QLSach qls = new QLSach();
            qls.nhap(sc);
            qls.nhap(sc);
            qls.inRa();

            System.out.println("\n======== KIỂM TRA NHÂN VIÊN GENERIC VÀ IO FILE ========");

            List<NhanVien<String>> nvStringList = new ArrayList<>();
            nvStringList.add(new NhanVien<>("S001", "Nguyen Van A"));
            nvStringList.add(new NhanVien<>("S002", "Tran Thi B"));

            List<NhanVien<Integer>> nvIntList = new ArrayList<>();
            nvIntList.add(new NhanVien<>(101, "Le Van C"));
            nvIntList.add(new NhanVien<>(102, "Pham Thi D"));

            String fileString = "NVString.txt";
            String fileInt = "NVInt.txt";

            IOFile.ghiFile(fileString, nvStringList);
            IOFile.ghiFile(fileInt, nvIntList);

            System.out.println("\n--- ĐỌC LẠI TỪ FILE VÀ IN RA ---");

            List<NhanVien<String>> docNvStringList = IOFile.docFile(fileString);
            System.out.println("Danh sách NV (Mã String):");
            docNvStringList.forEach(System.out::println);

            List<NhanVien<Integer>> docNvIntList = IOFile.docFile(fileInt);
            System.out.println("Danh sách NV (Mã Integer):");
            docNvIntList.forEach(System.out::println);
        }
    }
}

class Sach implements Serializable {
    private static final long serialVersionUID = 2L;

    private final String ma;
    private final String ten;
    private final String nhaXuatBan;
    private final double gia;
    private final int soBan;
    private final Date ngayXuatBan;

    public Sach(String ma, String ten, String nhaXuatBan, double gia, int soBan, Date ngayXuatBan) {
        this.ma = Objects.requireNonNull(ma, "Mã sách không được để trống");
        this.ten = Objects.requireNonNull(ten, "Tên sách không được để trống");
        this.nhaXuatBan = Objects.requireNonNull(nhaXuatBan, "Nhà xuất bản không được để trống");
        this.gia = gia;
        this.soBan = soBan;
        this.ngayXuatBan = Objects.requireNonNull(ngayXuatBan, "Ngày xuất bản không được để trống");
    }

    public String getMa() {
        return ma;
    }

    public String getTen() {
        return ten;
    }

    public String getNhaXuatBan() {
        return nhaXuatBan;
    }

    public double getGia() {
        return gia;
    }

    public int getSoBan() {
        return soBan;
    }

    public Date getNgayXuatBan() {
        return ngayXuatBan;
    }

    @Override
    public String toString() {
        return String.format(
                "Mã: %s | Tên: %s | NXB: %s | Giá: %.0f | Số Bản: %d | Ngày XB: %s",
                ma,
                ten,
                nhaXuatBan,
                gia,
                soBan,
                ngayXuatBan
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(ma);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Sach sach = (Sach) obj;
        return Objects.equals(ma, sach.ma);
    }
}

class NhanVien<T> implements Serializable {
    private static final long serialVersionUID = 3L;

    private final T ma;
    private final String ten;

    public NhanVien(T ma, String ten) {
        this.ma = ma;
        this.ten = ten;
    }

    public T getMa() {
        return ma;
    }

    public String getTen() {
        return ten;
    }

    @Override
    public String toString() {
        return "Nhân Viên [Mã: " + ma.getClass().getSimpleName() + " (" + ma + ") | Tên: " + ten + "]";
    }
}

final class IOFile {
    private IOFile() {
    }

    public static <T> void ghiFile(String tenFile, List<T> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tenFile))) {
            oos.writeObject(list);
            System.out.println("Ghi danh sách vào file " + tenFile + " thành công.");
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file " + tenFile + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> docFile(String tenFile) {
        List<T> list = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tenFile))) {
            list = (List<T>) ois.readObject();
            System.out.println("Đọc danh sách từ file " + tenFile + " thành công. Tổng: " + list.size());
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File " + tenFile + " không tồn tại. Trả về danh sách rỗng.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Lỗi khi đọc file " + tenFile + ": " + e.getMessage());
        }
        return list;
    }
}

class QLSach {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private final Set<Sach> danhSachSach = new HashSet<>();

    public void nhap(Scanner sc) {
        System.out.println("--- Nhập thông tin Sách mới ---");
        System.out.print("Mã sách: ");
        String ma = sc.nextLine();

        boolean tonTaiMa = danhSachSach.stream().anyMatch(s -> s.getMa().equals(ma));
        if (tonTaiMa) {
            System.out.println("Lỗi: Mã sách này đã tồn tại.");
            return;
        }

        System.out.print("Tên sách: ");
        String ten = sc.nextLine();
        System.out.print("Nhà xuất bản: ");
        String nxb = sc.nextLine();
        System.out.print("Giá: ");
        double gia = Double.parseDouble(sc.nextLine());
        System.out.print("Số bản: ");
        int soBan = Integer.parseInt(sc.nextLine());
        System.out.print("Ngày xuất bản (dd/MM/yyyy): ");
        Date ngayXB = docNgay(sc.nextLine());

        danhSachSach.add(new Sach(ma, ten, nxb, gia, soBan, ngayXB));
        System.out.println("Thêm sách thành công!");
    }

    public void inRa() {
        if (danhSachSach.isEmpty()) {
            System.out.println("Danh sách Sách rỗng.");
            return;
        }
        System.out.println("--- Danh sách Sách hiện có (Tổng: " + danhSachSach.size() + ") ---");
        danhSachSach.forEach(System.out::println);
    }

    public Set<Sach> getDanhSachSach() {
        return danhSachSach;
    }

    private Date docNgay(String input) {
        try {
            return DATE_FORMAT.parse(input);
        } catch (ParseException e) {
            System.out.println("Định dạng ngày không hợp lệ, sử dụng ngày hiện tại.");
            return new Date();
        }
    }
}
