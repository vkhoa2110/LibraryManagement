package org.example;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import java.io.*;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;
public class UserDAO {
    private boolean Available;
    private Connection connection;
    private PreparedStatement statement;
    private AtomicBoolean check;
    public UserDAO() {
        check = new AtomicBoolean(false);
    }

    //thêm
    public void addUser(User user) {
        String sql = "INSERT INTO users (user_id, full_name, date_of_birth, address, " +
                "phone_number, email, username, password_hash, membership_id, " +
                "join_date, membership_status, total_books_borrowed, total_books_returned, role, " +
                "card_registration_date, expiry_date, account_status, avatar, gender, department, class_name)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getFullName());
            setDate(pstmt,3, user.getDateOfBirth());
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getPhoneNumber());
            setString(pstmt, 6, user.getEmail());
            pstmt.setString(7, user.getUsername());
            pstmt.setString(8, user.getPasswordHash());
            setString(pstmt,9,user.getMembershipId());
            setDate(pstmt,10, user.getJoinDate());
            pstmt.setString(11, user.getMembershipStatus());
            pstmt.setInt(12, user.getTotalBooksBorrowed());
            pstmt.setInt(13, user.gettotalBooksReturned());
            pstmt.setString(14, user.getRole());
            setDate(pstmt,15, user.getCardRegistrationDate());
            setDate(pstmt,16, user.getExpiryDate());
            pstmt.setString(17, user.getAccountStatus());
            pstmt.setBytes(18, user.getAvatars());
            pstmt.setString(19, user.getGender());
            pstmt.setString(20, user.getDepartment());
            pstmt.setString(21, user.getClassName());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                Noti.showSuccessMessage("Người đã được thêm thành công");
                System.out.println("Người dùng đã được thêm thành công!");
            } else {
                Noti.showFailureMessage("Lỗi: không thể thêm Người dùng");
            }
        } catch (SQLException e) {
            Noti.showFailureMessage("Lỗi sql: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //sửa
    public void updateUser(User user) {
        // Câu lệnh SQL với tất cả các cột cần cập nhật
        String sql = "UPDATE users SET full_name = ?, date_of_birth = ?, address = ?, " +
                "phone_number = ?, email = ?, username = ?, password_hash = ?, " +
                "membership_id = ?, join_date = ?, membership_status = ?, " +
                "total_books_borrowed = ?, total_books_returned = ?, role = ?, " +
                "expiry_date = ?, card_registration_date = ?, account_status = ?, " +
                "gender = ?, department = ?, class_name = ? " +
                "WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gán giá trị cho các tham số trong câu lệnh SQL
            pstmt.setString(1, user.getFullName());
            pstmt.setDate(2, user.getDateOfBirth() != null ? java.sql.Date.valueOf(user.getDateOfBirth()) : null);
            pstmt.setString(3, user.getAddress());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getUsername());
            pstmt.setString(7, user.getPasswordHash());
            pstmt.setString(8, user.getMembershipId());
            pstmt.setDate(9, user.getJoinDate() != null ? java.sql.Date.valueOf(user.getJoinDate()) : null);
            pstmt.setString(10, user.getMembershipStatus());
            pstmt.setInt(11, user.getTotalBooksBorrowed() != null ? user.getTotalBooksBorrowed() : 0);
            pstmt.setInt(12, user.gettotalBooksReturned() != null ? user.gettotalBooksReturned() : 0);
            pstmt.setString(13, user.getRole());
            pstmt.setDate(14, user.getExpiryDate() != null ? java.sql.Date.valueOf(user.getExpiryDate()) : null);
            pstmt.setDate(15, user.getCardRegistrationDate() != null ? java.sql.Date.valueOf(user.getCardRegistrationDate()) : null);
            pstmt.setString(16, user.getAccountStatus());
            pstmt.setString(17, user.getGender());
            pstmt.setString(18, user.getDepartment());
            pstmt.setString(19, user.getClassName());
            pstmt.setString(20, user.getUserId()); // `userId` dùng để xác định người dùng cần cập nhật

            // Thực thi câu lệnh SQL
            int rowsUpdated = pstmt.executeUpdate();

            // Kiểm tra số dòng bị ảnh hưởng
            if (rowsUpdated > 0) {
                Noti.showSuccessMessage("Cập nhật thông tin người dùng thành công!");
                System.out.println("Cập nhật thông tin người dùng thành công!");
            } else {
                Noti.showFailureMessage("Không tìm thấy người dùng để cập nhật.");
                System.out.println("Không tìm thấy người dùng để cập nhật.");
            }

        } catch (SQLException e) {
            // In lỗi chi tiết
            System.err.println("Lỗi khi cập nhật thông tin người dùng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //xoá
    public void deleteUser(String userId) {
    String sql = "DELETE FROM users WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.connectToLibrary();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, userId);
        pstmt.executeUpdate(); // Thực thi câu lệnh SQL

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
// lay 1 gia tri
    public User output1Value(String id) {
        User user = new User();
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1,id);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                user.setUserId(resultSet.getString("user_id"));
                user.setFullName(resultSet.getString("full_name"));
                user.setDateOfBirth(getDate(resultSet,"date_of_birth"));
                user.setAddress(resultSet.getString("address"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setEmail(resultSet.getString("email"));
                user.setUsername(resultSet.getString("username"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setMembershipId(resultSet.getString("membership_id"));
                user.setJoinDate(getDate(resultSet,"join_date"));
                user.setMembershipStatus(resultSet.getString("membership_status"));
                user.setTotalBooksBorrowed(resultSet.getInt("total_books_borrowed"));
                user.settotalBooksReturned(resultSet.getInt("total_books_returned"));
                user.setRole(resultSet.getString("role"));
                user.setCardRegistrationDate(getDate(resultSet,"card_registration_date"));
                user.setExpiryDate(getDate(resultSet,"expiry_date"));
                user.setAccountStatus(resultSet.getString("account_status"));
                user.setGender(resultSet.getString("gender"));
                user.setDepartment(resultSet.getString("department"));
                user.setClassName(resultSet.getString("class_name"));
                try {
                    if (resultSet.getBytes("avatar")!=null)
                        user.setAvatar(new Image(new ByteArrayInputStream(resultSet.getBytes("qr_code"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
        return user;
    }

    public static String gender(String userId) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT gender FROM users WHERE user_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userId);
                ResultSet rs = statement.executeQuery();
                rs.next();
                return rs.getString(1);
            }
            } catch (SQLException e) {
            System.out.println("loi khi set gioi tinh: "+e.getMessage());
            return "other";
        }
    }
    // tim nguoi dung
    public ResultSet findUsers(User user) {
        try {
            connection = DatabaseConnection.connectToLibrary();

            // Khởi tạo câu lệnh SQL cơ bản
            StringBuilder sql = new StringBuilder("SELECT * FROM users");

            // List để lưu trữ các tham số cho PreparedStatement
            List<Object> parameters = new ArrayList<>();

            // Kiểm tra và thêm các điều kiện vào câu lệnh SQL
            if (!user.getUserId().isEmpty()) {
                sql.append(" WHERE user_id = ?");
                parameters.add(user.getUserId());
            }
            if (!user.getFullName().isEmpty()) {
                // Nếu đã có điều kiện WHERE, thêm AND, ngược lại, thêm WHERE
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" full_name = ?");
                parameters.add(user.getFullName());
            }
            if (user.getJoinDate() != null) {
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" join_date = ?");
                parameters.add(user.getJoinDate());
            }
            if (!user.getMembershipStatus().isEmpty()) {
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" membership_status = ?");
                parameters.add(user.getMembershipStatus());
            }
            if (user.getTotalBooksBorrowed() != null) {
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" total_books_borrowed = ?");
                parameters.add(user.getTotalBooksBorrowed());
            }
            if (user.gettotalBooksReturned() != null) {
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" total_books_returned = ?");
                parameters.add(user.gettotalBooksReturned());
            }
            if (!user.getRole().isEmpty()) {
                sql.append(sql.indexOf("WHERE") == -1 ? " WHERE" : " AND").append(" role = ?");
                parameters.add(user.getRole());
            }

            // Nếu không có điều kiện nào, câu lệnh SQL sẽ chỉ là "SELECT * FROM users"
            if (parameters.isEmpty()) {
                sql = new StringBuilder("SELECT * FROM users");
            }

            // Tạo prepared statement với câu lệnh SQL đã được xây dựng
            statement = connection.prepareStatement(sql.toString());

            // Gán các tham số vào PreparedStatement
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            // Thực thi truy vấn và trả về kết quả
            ResultSet resultSet = statement.executeQuery();
            return resultSet;

        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm người dùng: " + e.getMessage());
        }
        return null;
    }

    // kiểm tra acc có tn tại ko
    public static boolean checkAccount(String username, String password) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);

                // Thực thi truy vấn và kiểm tra kết quả
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {  // Nếu có kết quả trả về, tài khoản tồn tại
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\fptshop\\Documents\\SQL Server Management Studio\\library (1)\\user_id.txt"))) {
                            writer.write(rs.getString("user_id"));
                        } catch (IOException e) {
                            System.out.println("Lỗi khi ghi vào file: " + e.getMessage());
                        }
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
        return false;  // Nếu không có kết quả, tài khoản không tồn tại
    }
//họ tên
    public static String accountName(String user_id) {
        String sql = "SELECT full_name FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user_id);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("full_name");
                } else {
                    System.out.println("Không tìm thấy người dùng với user_id: " + user_id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm tên người dùng: " + e.getMessage());
        }
        return ""; // Trả về chuỗi rỗng nếu không tìm thấy người dùng
    }
// thêm luợt mượn
public static void updateBorrowed(String user_id, int borrowed) {
    String sql = "UPDATE users SET total_books_borrowed = ? WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.connectToLibrary();

         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1,borrowed);
        pstmt.setString(2, user_id); // Sử dụng userId để xác định người dùng cần sửa

        if(pstmt.executeUpdate()>0) // Thực thi câu lệnh SQL
        System.out.println("Update lượt mượn thành công");
        else
        System.out.println("Update lượt mượn không thành công");
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Update lượt mượn không thành công" + e.getMessage());
    }
}
// thêm luojt trả
public static void updateReturned(String user_id, int returned) {
    String sql = "UPDATE users SET total_books_returned = ? WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.connectToLibrary();

         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1,returned);
        pstmt.setString(2, user_id); // Sử dụng userId để xác định người dùng cần sửa

        if(pstmt.executeUpdate()>0) // Thực thi câu lệnh SQL
            System.out.println("Update lượt trả thành công");
        else
            System.out.println("Update lượt trả không thành công");
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Update lượt trả không thành công" + e.getMessage());
    }
}
//lấy chức vụ
    public static String getRole(String userId) {
        String sql = "SELECT role FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getString("role");

        } catch (SQLException e) {
            System.out.println("loi khi lay chuc vu" + e.getMessage());
            return "";
        }
    }

    public static ObservableList<TopUser> dataTopUser() {
        ObservableList<TopUser> data =  FXCollections.observableArrayList();

        String sql = "SELECT b.user_id, u.full_name, COUNT(b.user_id) AS count\n" +
                "FROM borrow b\n" +
                "JOIN users u ON b.user_id = u.user_id\n" +
                "GROUP BY b.user_id, u.full_name\n" +
                "ORDER BY count DESC\n" +
                "LIMIT 0, 100;";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                data.add(new TopUser(rs.getString("user_id"),
                        rs.getString("full_name"), rs.getInt("count")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public AtomicBoolean isCheck() {return check;}
    public void setCheck(AtomicBoolean check) {this.check=check;}

    public LocalDate getDate(ResultSet resultSet, String columnName) throws SQLException {
        java.sql.Date sqlDate = resultSet.getDate(columnName);
        return resultSet.wasNull() ? null : sqlDate.toLocalDate();
    }
    public static void setDate(PreparedStatement pstmt, int parameterIndex, LocalDate date) throws SQLException {
        if (date == null) {
            pstmt.setNull(parameterIndex, java.sql.Types.DATE); // Đẩy lên SQL là null
        } else {
            pstmt.setDate(parameterIndex, Date.valueOf(date)); // Đẩy ngày lên SQL
        }
    }

    public static int totalUsers() {
        String sql = "select count(*) as total_users from users\n" +
                "where role != 'admin'";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet resultSet = pstmt.executeQuery();
                resultSet.next();
                return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }


    public static boolean registerAccount(String username, String password, String email, String phone) {
        String sqlCheck = "SELECT COUNT(*) FROM users WHERE username = ?";
        String sqlInsert = "INSERT INTO users (user_id, username, password_hash, role, email, phone_number) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {

            // Kiểm tra xem tên đăng nhập đã tồn tại chưa
            checkStmt.setString(1, username);
            ResultSet resultSet = checkStmt.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                return false;
            }

           
            String passwordPrefix = password.length() >= 3 ? password.substring(0, 3) : password;
            int userId = getAsciiProduct(passwordPrefix); 

            // Hash mật khẩu trước khi lưu
            String hashedPassword = hashPassword(password);

            // Nếu tên đăng nhập chưa tồn tại, tiếp tục thêm vào cơ sở dữ liệu
            try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                insertStmt.setInt(1, userId);  
                insertStmt.setString(2, username);
                insertStmt.setString(3, hashedPassword);
                insertStmt.setString(4, "member"); 
                insertStmt.setString(5, email);
                insertStmt.setString(6, phone);

                int rowsInserted = insertStmt.executeUpdate();
                return rowsInserted > 0; 
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }

    private static int getAsciiProduct(String str) {
        int product = 1;
        for (int i = 0; i < str.length(); i++) {
            product *= (int) str.charAt(i); 
        }
        return product;
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkForgotPassword(String username, String email, String phone) {
        String sqlCheck = "SELECT COUNT(*) FROM users WHERE username = ? AND email = ? AND phone_number = ?";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {

            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            checkStmt.setString(3, phone);

            ResultSet resultSet = checkStmt.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            return count > 0;  
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean resetPassword(String username, String email, String phone, String newPassword) {
        if (!checkForgotPassword(username, email, phone)) {
            return false;
        }

        String sqlUpdate = "UPDATE users SET password_hash = ? WHERE username = ?";

        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {

            String hashedPassword = hashPassword(newPassword);
            updateStmt.setString(1, hashedPassword);
            updateStmt.setString(2, username);

            int rowsUpdated = updateStmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static int getAsciiSum(String str) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += (int) str.charAt(i);
        }
        return sum;
    }
 
    public static boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE username = ?";
        try (Connection conn = DatabaseConnection.connectToLibrary();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void setString(PreparedStatement pstmt, int parameterIndex, String string) throws SQLException {
        if (string.isEmpty()) {
            pstmt.setNull(parameterIndex, Types.DISTINCT); 
        } else {
                pstmt.setString(parameterIndex, string);
        }
    }

    public void closeDatabase() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {

        }
    }

}