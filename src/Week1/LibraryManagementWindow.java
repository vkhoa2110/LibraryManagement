package Week1;

import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JList;
import java.awt.Cursor;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.DebugGraphics;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LibraryManagementWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public LibraryManagementWindow() {
    	StudentName.checkNameFromStudentID(Main.currentID);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setTitle("Quản lý thư viện");

        contentPane = new JPanel();
        contentPane.setFont(new Font("Arial", Font.PLAIN, 15));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Title 
        JLabel lblWelcome = new JLabel("Chào mừng đến hệ thống Quản lý Thư viện!");
        lblWelcome.setBounds(146, 62, 500, 50);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        contentPane.add(lblWelcome);

        // nút đăng xuất
        JButton btnLogout = new JButton("Đăng Xuất");
        btnLogout.setBounds(650, 20, 120, 40);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 16));
        contentPane.add(btnLogout);
        
        JLabel lblNewLabel = new JLabel("Xin chào " + Main.name );
        lblNewLabel.setBounds(57, 21, 201, 28);
        lblNewLabel.setForeground(new Color(255, 0, 0));
        lblNewLabel.setBackground(new Color(0, 255, 255));
        lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 15));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblNewLabel);
        ImageIcon img = new ImageIcon("library.jpg");
        
        JList list = new JList();
        list.setBounds(143, 229, 1, 1);
        contentPane.add(list);
        
        JList list_2 = new JList();
        list_2.setBounds(143, 229, 1, 1);
        contentPane.add(list_2);
        
        JButton addDocument = new JButton("Thêm sách");
        addDocument.setBounds(71, 219, 120, 40);
        contentPane.add(addDocument);
        
        JButton removeDocument = new JButton("Xóa sách");
        removeDocument.setBounds(71, 277, 120, 40);
        contentPane.add(removeDocument);
        
        JButton updateDocument = new JButton("Cập nhật sách");
        updateDocument.setBounds(71, 342, 120, 40);
        contentPane.add(updateDocument);
        
        JButton findDocument = new JButton("Tìm kiếm");
        findDocument.setBounds(71, 407, 120, 40);
        contentPane.add(findDocument);
        
        JButton addUsers = new JButton("Thêm người dùng");
        addUsers.setBounds(551, 407, 120, 40);
        contentPane.add(addUsers);
        
        JButton borrowDocument = new JButton("Mượn sách");
        borrowDocument.setBounds(551, 219, 120, 40);
        contentPane.add(borrowDocument);
        
        JButton returnDocument = new JButton("Trả sách");
        returnDocument.setBounds(551, 277, 120, 40);
        contentPane.add(returnDocument);
        
        JButton displayUserInfor = new JButton("Sửa thông tin");
        displayUserInfor.setBounds(551, 342, 120, 40);
        contentPane.add(displayUserInfor);
        
        JPanel panel = new JPanel();
        panel.setBounds(54, 206, 684, 263);
        contentPane.add(panel);
        
        JLabel lblImage = new JLabel();
        panel.add(lblImage);
        lblImage.setIcon(img);

        // Nhấn nút đăng xuất
        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                // Chương trình quay lại màn hình đăng nhập khi bấm nút đăng xuất
                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setVisible(true);
            }
        });

    }
	private static void addPopup(Component component, final JPopupMenu popup) {
	}
}
