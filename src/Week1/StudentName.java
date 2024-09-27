package Week1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class StudentName {

    /**
     * Hàm kiểm tra thông tin đăng nhập từ file account.txt.
     * Hàm return về true nếu thông tin đăng nhập hợp lệ
     */
    public static void checkNameFromStudentID(String studentId) {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\fptshop\\Documents\\name.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(" ");
                
                    String fileStudentId = credentials[0];
                    String firstName = credentials[1];
                    String midName = credentials[2];
                    String lastName = credentials[3];
                    if (fileStudentId.equals(studentId)) {
                    	Main.name = firstName + " " + midName + " " + lastName;
                    //	System.out.println("Đã truy nhập");
                    
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Lỗi đọc file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
