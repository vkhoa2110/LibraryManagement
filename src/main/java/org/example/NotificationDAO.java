package org.example;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationDAO {

    // Lấy tất cả các thông báo từ các nguồn và ghép chúng lại
    public static List<NotificationItem> getAllNotifications() {
        List<NotificationItem> notifications = new ArrayList<>();
        String userId = interfaces.userId();

        // 1. Thông báo mượn sách
        String borrowQuery = """
                SELECT b.title, br.borrow_date
                FROM borrow br
                JOIN books b ON br.book_id = b.id
                WHERE br.user_id = ?
                """;

        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement borrowStmt = connection.prepareStatement(borrowQuery)) {

            borrowStmt.setString(1, userId);
            ResultSet rs = borrowStmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String borrowDate = rs.getString("borrow_date");
                String message = String.format("Bạn đã mượn sách: \"%s\", vào ngày: %s", title, borrowDate);
                Date timestamp = parseTimestamp(borrowDate);
                notifications.add(new NotificationItem(message, timestamp));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong getBorrowingNotifications: " + e.getMessage());
            e.printStackTrace();
        }

        // 2. Thông báo yêu cầu trợ giúp
        String helpQuery = """
                SELECT title, request_date
                FROM help_request
                WHERE user_id = ?
                """;

        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement helpStmt = connection.prepareStatement(helpQuery)) {

            helpStmt.setString(1, userId);
            ResultSet rs = helpStmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String requestDate = rs.getString("request_date");
                String message = String.format("Bạn đã gửi một yêu cầu trợ giúp: \"%s\", vào ngày: %s", title, requestDate);
                Date timestamp = parseTimestamp(requestDate);
                notifications.add(new NotificationItem(message, timestamp));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong getHelpRequestNotifications: " + e.getMessage());
            e.printStackTrace();
        }

        // 3. Thông báo đánh giá sách
        String reviewQuery = """
                SELECT b.title, br.rating, br.review_date
                FROM book_reviews br
                JOIN books b ON br.book_id = b.id
                WHERE br.user_id = ?
                """;

        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement reviewStmt = connection.prepareStatement(reviewQuery)) {

            reviewStmt.setString(1, userId);
            ResultSet rs = reviewStmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                double rating = rs.getDouble("rating");
                String reviewDate = rs.getString("review_date");
                String message = String.format("Bạn đã đánh giá sách: \"%s\" %.1f sao, vào ngày: %s", title, rating, reviewDate);
                Date timestamp = parseTimestamp(reviewDate);
                notifications.add(new NotificationItem(message, timestamp));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong getBookReviewNotifications: " + e.getMessage());
            e.printStackTrace();
        }

        // 4. Thông báo hệ thống
        String systemQuery = """
                SELECT title, content, created_at
                FROM system_notifications
                """;

        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement systemStmt = connection.prepareStatement(systemQuery)) {

            ResultSet rs = systemStmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String content = rs.getString("content");
                String createdAt = rs.getString("created_at");
                String message = String.format("Hệ thống: %s - %s", title, content);
                Date timestamp = parseTimestamp(createdAt);
                notifications.add(new NotificationItem(message, timestamp));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong getSystemNotifications: " + e.getMessage());
            e.printStackTrace();
        }

        return notifications;
    }

    // Chèn một thông báo mới vào hệ thống (hệ thống thông báo)
    public static boolean insertNotification(String title, String content) {
        String query = """
                INSERT INTO system_notifications (title, content, created_at)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.connectToLibrary();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, content);
            String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            preparedStatement.setString(3, currentTimestamp);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi SQL trong insertNotification: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Chuyển chuỗi thời gian thành đối tượng Date
    private static Date parseTimestamp(String dateTime) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (dateTime.contains(":")) {
                return dateTimeFormat.parse(dateTime);
            } else {
                return dateOnlyFormat.parse(dateTime + " 12:00:00");
            }
        } catch (Exception e) {
            System.err.println("Không thể parse thời gian: " + dateTime);
            e.printStackTrace();
            return new Date();
        }
    }
}
