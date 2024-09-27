package Week1;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class LoginWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtMSSinh;
    private JPasswordField pwdPassword;

    /**
     * Create the frame.
     */
    public LoginWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 787, 451);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
      
  
        // Chèn ảnh logo vào màn hình login  
        JLabel lblImage = new JLabel();
        lblImage.setBounds(500, 150, 200, 200); // Kích thước và vị trí của ảnh
        ImageIcon img = new ImageIcon("uet.png");
        lblImage.setIcon(img);  // Gán ảnh vào JLabel
        contentPane.add(lblImage); // Thêm JLabel vào panel chính


        JLabel lblTitle = new JLabel("Welcome to Library");
        lblTitle.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 30));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);  // Căn giữa theo chiều ngang
        lblTitle.setVerticalAlignment(SwingConstants.CENTER);    // Căn giữa theo chiều dọc
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(255, 204, 204));
        lblTitle.setBounds(131, 30, 533, 81);  // Kích thước và vị trí của ô
        contentPane.add(lblTitle);

        txtMSSinh = new JTextField();
        txtMSSinh.setHorizontalAlignment(SwingConstants.LEADING);
        txtMSSinh.setBackground(new Color(128, 255, 255));
        txtMSSinh.setFont(new Font("Arial", Font.PLAIN, 20));
        txtMSSinh.setText("Student ID");
        txtMSSinh.setBounds(157, 181, 244, 40);
        contentPane.add(txtMSSinh);
        txtMSSinh.setColumns(10);

        // Placeholder cho ô Student ID
        txtMSSinh.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtMSSinh.getText().equals("Student ID")) {
                    txtMSSinh.setText("");
                    txtMSSinh.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtMSSinh.getText().isEmpty()) {
                    txtMSSinh.setForeground(Color.GRAY);
                    txtMSSinh.setText("Student ID");
                }
            }
        });

        pwdPassword = new JPasswordField();
        pwdPassword.setBackground(new Color(192, 192, 192));
        pwdPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        pwdPassword.setForeground(Color.GRAY);
        pwdPassword.setToolTipText("Nhap Mat Khau");
        pwdPassword.setBounds(157, 254, 244, 40);
        pwdPassword.setEchoChar((char) 0); // Hiển thị mật khẩu như văn bản thường ban đầu
        pwdPassword.setText("Password");
        contentPane.add(pwdPassword);

        // Placeholder cho ô Password
        pwdPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(pwdPassword.getPassword()).equals("Password")) {
                    pwdPassword.setText("");
                    pwdPassword.setForeground(Color.BLACK);
                    pwdPassword.setEchoChar('●'); // Chuyển sang ký tự ẩn khi người dùng nhập
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(pwdPassword.getPassword()).isEmpty()) {
                    pwdPassword.setForeground(Color.GRAY);
                    pwdPassword.setText("Password");
                    pwdPassword.setEchoChar((char) 0); // Hiển thị lại mật khẩu như văn bản thường
                }
            }
        });

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(255, 255, 128)); // Màu nền ban đầu của nút
        btnLogin.setFont(new Font("Arial", Font.BOLD, 18));
        btnLogin.setBounds(161, 320, 120, 40);
        contentPane.add(btnLogin);

        JLabel lblMessage = new JLabel("");
        lblMessage.setFont(new Font("Arial", Font.PLAIN, 16));
        lblMessage.setBounds(161, 370, 300, 30);
        contentPane.add(lblMessage);

        // Đổi màu nền của nút khi chuột trỏ vào
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(Color.RED); // Đổi sang màu đỏ khi chuột trỏ vào
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(255, 255, 128)); // Quay lại màu ban đầu khi chuột rời khỏi
            }
        });

        // Sự kiện khi nhấn nút Login
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentId = txtMSSinh.getText().trim();
                String password = new String(pwdPassword.getPassword());

                // Kiểm tra thông tin đăng nhập từ file thông qua LoginController
                LoginController controller = new LoginController();
                if (controller.checkLoginFromFile(studentId, password)) {
                	Main.currentID = studentId;
                    JOptionPane.showMessageDialog(contentPane, "Đăng nhập thành công");
                    dispose(); // Đóng màn hình hiện tại
                    
                    LibraryManagementWindow libraryFrame = new LibraryManagementWindow();
                    libraryFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(contentPane, "Thông tin đăng nhập không đúng", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Đặt focus cho nút Login để không focus vào txtMSSinh
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                btnLogin.requestFocusInWindow();  // Đặt focus vào btnLogin ngay sau khi khởi động
            }
        });
    }
}
