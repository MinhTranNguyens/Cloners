package clonet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Simple console based product management program.
 */
public class BTVN1 {
    public static void main(String[] args) {
        QLSP q = new QLSP();
        String tenFile = "SP.in";
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("--------MENU---------\n"
                    + "1. Nhap san pham \n"
                    + "2. Hien thi danh sach san pham\n"
                    + "3. Luu danh sach san pham\n"
                    + "4. Doc danh sach san pham\n"
                    + "5. Tim theo ma\n"
                    + "6. Sua theo ma\n"
                    + "7. Xoa theo ma\n"
                    + "8. Xoa theo ten\n"
                    + "9. Tim theo ten\n"
                    + "10. Tim theo so luong\n"
                    + "11. Sap xep theo thanh tien\n"
                    + "12. Sap xep theo so luong\n"
                    + "13. Sap xep theo ma\n"
                    + "0. Thoat");
            System.out.println("-----------------------------");
            System.out.print("Moi chon: ");
            int chon = Integer.parseInt(sc.nextLine());
            switch (chon) {
                case 1 -> q.nhap(sc);
                case 2 -> q.hienthi();
                case 3 -> q.luuFile(tenFile);
                case 4 -> q.docFile(tenFile);
                case 5 -> {
                    System.out.print("Nhap ma san pham: ");
                    String ma = sc.nextLine();
                    q.timTheoMa(ma);
                }
                case 6 -> {
                    System.out.print("Nhap ma san pham: ");
                    String ma = sc.nextLine();
                    q.suaTheoMa(sc, ma);
                }
                case 7 -> {
                    System.out.print("Nhap ma san pham: ");
                    String ma = sc.nextLine();
                    q.xoaTheoMa(ma);
                }
                case 8 -> {
                    System.out.print("Nhap ten san pham: ");
                    String ten = sc.nextLine();
                    q.xoaTheoTen(ten);
                }
                case 9 -> {
                    System.out.print("Nhap ten san pham: ");
                    String ten = sc.nextLine();
                    q.timTheoTen(ten);
                }
                case 10 -> {
                    System.out.print("Nhap so luong tu: ");
                    int tu = Integer.parseInt(sc.nextLine());
                    System.out.print("Nhap so luong den: ");
                    int den = Integer.parseInt(sc.nextLine());
                    q.timTheoSoLuong(tu, den);
                }
                case 11 -> q.sapxepTheoThanhTien();
                case 12 -> q.sapxepTheoSoLuong();
                case 13 -> q.sapxepTheoMa();
                case 0 -> {
                    System.out.println("Tam biet!");
                    return;
                }
                default -> System.out.println("De nghi nguoi dung nhap 0->13");
            }
        }
    }
}

class QLSP implements IChucNang {
    private List<SanPham> a;

    public QLSP() {
        a = new ArrayList<>();
    }

    @Override
    public void nhap(Scanner sc) {
        System.out.print("So luong san pham: ");
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            System.out.print("Ten: ");
            String ten = sc.nextLine();
            System.out.print("Gia: ");
            double gia = Double.parseDouble(sc.nextLine());
            System.out.print("So luong: ");
            int sl = Integer.parseInt(sc.nextLine());
            a.add(new SanPham(ten, gia, sl));
        }
    }

    @Override
    public void hienthi() {
        if (a.isEmpty()) {
            System.out.println("Danh sach san pham rong");
            return;
        }
        System.out.printf("%-5s %-15s %-15s %-12s %-10s\n", "Ma", "Ten", "Gia", "So luong", "Thanh tien");
        System.out.println("---------------------------------------------------------------");
        a.forEach(x -> System.out.printf("%-5s %-15s %-15.2f %-12d %-10.2f\n",
                x.getMa(), x.getTen(), x.getGia(), x.getSoluong(), x.getThanhtien()));
        System.out.println("---------------------------------------------------------------");
        System.out.println("Tong: " + a.size());
    }

    @Override
    public void luuFile(String tenFile) {
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(tenFile))) {
            o.writeObject(a);
            System.out.println("Luu file thanh cong");
        } catch (Exception e) {
            System.out.println("Khong the luu file: " + e.getMessage());
        }
    }

    @Override
    public void docFile(String tenFile) {
        try (ObjectInputStream o = new ObjectInputStream(new FileInputStream(tenFile))) {
            Object data = o.readObject();
            if (data instanceof List<?>) {
                List<?> list = (List<?>) data;
                List<SanPham> tmp = new ArrayList<>();
                for (Object item : list) {
                    if (item instanceof SanPham sp) {
                        tmp.add(sp);
                    }
                }
                a = tmp;
                System.out.println("Doc file thanh cong");
            } else {
                System.out.println("File khong dung dinh dang");
            }
        } catch (Exception e) {
            System.out.println("Khong the doc file: " + e.getMessage());
        }
    }

    private int getViTri(String ma) {
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).getMa().equalsIgnoreCase(ma)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void timTheoMa(String ma) {
        int p = getViTri(ma);
        if (p == -1) {
            System.out.println("Khong thay!");
        } else {
            System.out.println(a.get(p));
        }
    }

    @Override
    public void suaTheoMa(Scanner sc, String ma) {
        int p = getViTri(ma);
        if (p == -1) {
            System.out.println("Khong thay!");
        } else {
            SanPham x = a.get(p);
            System.out.print("Ten moi: ");
            x.setTen(sc.nextLine());
            System.out.print("Gia moi: ");
            x.setGia(Double.parseDouble(sc.nextLine()));
            System.out.print("So luong moi: ");
            x.setSoluong(Integer.parseInt(sc.nextLine()));
            System.out.println("Sua thanh cong");
        }
    }

    @Override
    public void xoaTheoMa(String ma) {
        Iterator<SanPham> i = a.iterator();
        int c = 0;
        while (i.hasNext()) {
            SanPham sp = i.next();
            if (sp.getMa().equalsIgnoreCase(ma)) {
                i.remove();
                c++;
            }
        }
        if (c == 0) {
            System.out.println("Khong thay");
        } else {
            System.out.println("Xoa " + c + " san pham");
        }
    }

    @Override
    public void xoaTheoTen(String ten) {
        Iterator<SanPham> i = a.iterator();
        int c = 0;
        while (i.hasNext()) {
            SanPham sp = i.next();
            if (sp.getTen().toLowerCase().contains(ten.toLowerCase())) {
                i.remove();
                c++;
            }
        }
        if (c == 0) {
            System.out.println("Khong thay");
        } else {
            System.out.println("Xoa " + c + " san pham");
        }
    }

    @Override
    public void timTheoTen(String ten) {
        int c = 0;
        for (SanPham i : a) {
            if (i.getTen().toLowerCase().contains(ten.toLowerCase())) {
                System.out.println(i);
                c++;
            }
        }
        if (c == 0) {
            System.out.println("Khong thay");
        } else {
            System.out.println("Tong " + c + " san pham");
        }
    }

    @Override
    public void timTheoSoLuong(int tu, int den) {
        int c = 0;
        for (SanPham i : a) {
            if (i.getSoluong() >= tu && i.getSoluong() <= den) {
                System.out.println(i);
                c++;
            }
        }
        if (c == 0) {
            System.out.println("Khong thay");
        } else {
            System.out.println("Tong " + c + " san pham");
        }
    }

    @Override
    public void sapxepTheoThanhTien() {
        a.sort(Comparator.comparingDouble(SanPham::getThanhtien).reversed());
    }

    @Override
    public void sapxepTheoSoLuong() {
        a.sort(Comparator.comparingInt(SanPham::getSoluong));
    }

    @Override
    public void sapxepTheoMa() {
        Collections.sort(a);
    }
}

interface IChucNang {
    void nhap(Scanner sc);

    void hienthi();

    void luuFile(String tenFile);

    void docFile(String tenFile);

    void timTheoMa(String ma);

    void suaTheoMa(Scanner sc, String ma);

    void xoaTheoMa(String ma);

    void xoaTheoTen(String ten);

    void timTheoTen(String ten);

    void timTheoSoLuong(int tu, int den);

    void sapxepTheoThanhTien();

    void sapxepTheoSoLuong();

    void sapxepTheoMa();
}

class SanPham implements Serializable, Comparable<SanPham> {
    private static final long serialVersionUID = 1L;
    private static int sMa = 1;

    private String ma;
    private String ten;
    private double gia;
    private int soluong;

    public SanPham() {
    }

    public SanPham(String ten, double gia, int soluong) {
        this.ma = String.format("%03d", sMa++);
        this.ten = ten;
        this.gia = gia;
        this.soluong = soluong;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public double getThanhtien() {
        return gia * soluong;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    @Override
    public int compareTo(SanPham o) {
        return this.getMa().compareToIgnoreCase(o.getMa());
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f %d %.2f", ma, ten, gia, soluong, getThanhtien());
    }
}
