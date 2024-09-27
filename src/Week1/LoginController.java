package Week1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class LoginController {

    /**
     * Hàm kiểm tra thông tin đăng nhập từ file account.txt.
     * Hàm return về true nếu thông tin đăng nhập hợp lệ
     */
    public boolean checkLoginFromFile(String studentId, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\fptshop\\Documents\\account.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(" ");
                if (credentials.length == 2) {
                    String fileStudentId = credentials[0];
                    String filePassword = credentials[1];
                    if (fileStudentId.equals(studentId) && filePassword.equals(password)) {
                    	Main.currentID = studentId;
                        return true; 
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Lỗi đọc file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    
    }
}
