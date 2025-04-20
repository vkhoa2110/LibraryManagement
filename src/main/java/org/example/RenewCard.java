package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RenewCard {
    private LibraryManagement library;
    private Stage primaryStage;
    private LocalDate currentExpiryDate; // Biến instance để lưu trữ ngày hết hạn

    public RenewCard(LibraryManagement library, Stage primaryStage) {
        this.library = library;
        this.primaryStage = primaryStage;
        this.currentExpiryDate = LocalDate.now(); // Khởi tạo giá trị mặc định
    }

    public Node showRenewCardInterface() {
        double containerWidth = 500; // Chiều rộng vùng nội dung
        double containerHeight = 400; // Chiều cao vùng nội dung

        // Main container với hiệu ứng đổ bóng và bo góc
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.CENTER); // Căn giữa tất cả các thành phần bên trong
        mainContainer.setPrefSize(containerWidth, containerHeight);
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffffff, #e6f2ff); " +
                "-fx-border-radius: 20px; -fx-background-radius: 20px; " +
                "-fx-border-color: #b0c4de; -fx-border-width: 2px;");
        mainContainer.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.25)));

        // Tiêu đề với icon (nếu có)
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER);
        // Thêm icon nếu có (Đảm bảo đường dẫn đúng hoặc bỏ qua nếu không có)
        // ImageView icon = new ImageView(new Image("file:path_to_icon.png"));
        // icon.setFitWidth(40);
        // icon.setFitHeight(40);
        Label renewLabel = new Label("Gia Hạn Thẻ Thư Viện");
        renewLabel.setFont(new Font("Arial", 24));
        renewLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2a2a2a;");
        // titleBox.getChildren().addAll(icon, renewLabel); // Nếu có icon
        titleBox.getChildren().add(renewLabel); // Nếu không có icon

        // Lấy thông tin người dùng từ cơ sở dữ liệu
        String userId = interfaces.userId(); // Lấy ID người dùng từ giao diện
        String userName = "Không xác định";
        String cardType = "Không xác định";

        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String query = "SELECT users.full_name, card.card_type, card.expiry_date " +
                    "FROM card " +
                    "INNER JOIN users ON card.user_id = users.user_id " +
                    "WHERE users.user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userName = resultSet.getString("full_name");
                cardType = resultSet.getString("card_type");
                if (resultSet.getDate("expiry_date") != null) {
                    currentExpiryDate = resultSet.getDate("expiry_date").toLocalDate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
        }

        // Chuyển đổi loại thẻ
        cardType = mapCardType(cardType);

        // Tạo GridPane để sắp xếp thông tin người dùng
        GridPane userInfoGrid = new GridPane();
        userInfoGrid.setHgap(20);
        userInfoGrid.setVgap(15);
        userInfoGrid.setAlignment(Pos.CENTER);

        // Nhãn
        Label userIdLabel = new Label("ID Người Dùng:");
        userIdLabel.setFont(Font.font("Arial", 14));
        userIdLabel.setStyle("-fx-font-weight: bold;");

        Label userNameLabel = new Label("Tên Người Dùng:");
        userNameLabel.setFont(Font.font("Arial", 14));
        userNameLabel.setStyle("-fx-font-weight: bold;");

        Label cardTypeLabel = new Label("Loại Thẻ Hiện Tại:");
        cardTypeLabel.setFont(Font.font("Arial", 14));
        cardTypeLabel.setStyle("-fx-font-weight: bold;");

        Label expiryDateLabel = new Label("Ngày Hết Hạn Dự Kiến:");
        expiryDateLabel.setFont(Font.font("Arial", 14));
        expiryDateLabel.setStyle("-fx-font-weight: bold;");

        // Giá trị
        Label userIdValue = new Label(userId);
        userIdValue.setFont(Font.font("Arial", 14));
        userIdValue.setTextFill(Color.DARKBLUE);

        Label userNameValue = new Label(userName);
        userNameValue.setFont(Font.font("Arial", 14));
        userNameValue.setTextFill(Color.DARKBLUE);

        Label cardTypeValue = new Label(cardType);
        cardTypeValue.setFont(Font.font("Arial", 14));
        cardTypeValue.setTextFill(Color.DARKBLUE);

        Label expiryDateValue = new Label(currentExpiryDate.plusMonths(1).toString());
        expiryDateValue.setFont(Font.font("Arial", 14));
        expiryDateValue.setTextFill(Color.DARKBLUE);

        // Thêm vào GridPane
        userInfoGrid.add(userIdLabel, 0, 0);
        userInfoGrid.add(userIdValue, 1, 0);
        userInfoGrid.add(userNameLabel, 0, 1);
        userInfoGrid.add(userNameValue, 1, 1);
        userInfoGrid.add(cardTypeLabel, 0, 2);
        userInfoGrid.add(cardTypeValue, 1, 2);
        userInfoGrid.add(expiryDateLabel, 0, 3);
        userInfoGrid.add(expiryDateValue, 1, 3);

        // Label và ComboBox để chọn thời gian gia hạn thêm
        Label extendLabel = new Label("Chọn thời gian gia hạn thêm:");
        extendLabel.setFont(Font.font("Arial", 14));
        extendLabel.setStyle("-fx-font-weight: bold;");

        ComboBox<String> extendComboBox = new ComboBox<>();
        extendComboBox.getItems().addAll("1 tháng", "3 tháng", "6 tháng");
        extendComboBox.setMaxWidth(200);
        extendComboBox.setStyle("-fx-font-size: 14px;");
        extendComboBox.setValue("1 tháng");

        // Nút Gia Hạn Thẻ
        Button renewButton = new Button("Gia Hạn Thẻ");
        renewButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10px;");
        renewButton.setPrefWidth(150);
        renewButton.setEffect(new DropShadow(5, Color.GRAY));

        // Hiệu ứng khi hover vào nút
        renewButton.setOnMouseEntered(e -> renewButton.setStyle("-fx-background-color: #218838; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10px;"));
        renewButton.setOnMouseExited(e -> renewButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10px;"));

        // HBox để chứa nút
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().add(renewButton);

        // Sự kiện khi nhấn nút Gia Hạn Thẻ
        renewButton.setOnAction(e -> {
            int monthsToExtend = parseMonths(extendComboBox.getValue());
            boolean success = renewUserCard(userId, monthsToExtend);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành Công");
                alert.setHeaderText(null);
                alert.setContentText("Gia hạn thẻ thành công cho " + monthsToExtend + " tháng!");
                alert.showAndWait();
                // Cập nhật lại ngày hết hạn
                currentExpiryDate = currentExpiryDate.plusMonths(monthsToExtend);
                expiryDateValue.setText(currentExpiryDate.toString());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Không thể gia hạn thẻ. Vui lòng kiểm tra lại thông tin.");
                alert.showAndWait();
            }
        });

        // Thêm các thành phần vào mainContainer
        mainContainer.getChildren().addAll(
                titleBox,
                userInfoGrid,
                extendLabel,
                extendComboBox,
                buttonBox
        );

        // Wrapper để căn giữa mainContainer
        StackPane wrapper = new StackPane();
        wrapper.setPrefSize(containerWidth, containerHeight);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.getChildren().add(mainContainer);

        return wrapper;
    }

    private String mapCardType(String cardType) {
        if (cardType == null || cardType.isBlank()) return "Không xác định";
        switch (cardType.trim()) {
            case "Thẻ Thường":
                return "Thẻ Thường";
            case "Thẻ VIP":
                return "Thẻ VIP";
            case "Thẻ Plus":
                return "Thẻ Plus";
            default:
                return "Không xác định";
        }
    }

    private boolean renewUserCard(String userId, int monthsToExtend) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            connection.setAutoCommit(false);

            LocalDate fetchedExpiryDate = currentExpiryDate; // Sử dụng biến instance đã cập nhật

            // Truy vấn ngày hết hạn hiện tại từ cơ sở dữ liệu
            try (PreparedStatement queryStmt = connection.prepareStatement(
                    "SELECT expiry_date FROM card WHERE user_id = ?")) {
                queryStmt.setString(1, userId);
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next() && rs.getDate("expiry_date") != null) {
                    fetchedExpiryDate = rs.getDate("expiry_date").toLocalDate();
                }
            }

            LocalDate newExpiryDate = fetchedExpiryDate.plusMonths(monthsToExtend);

            // Cập nhật ngày hết hạn mới vào cơ sở dữ liệu
            try (PreparedStatement updateStmt = connection.prepareStatement(
                    "UPDATE card SET expiry_date = ? WHERE user_id = ?")) {
                updateStmt.setDate(1, java.sql.Date.valueOf(newExpiryDate));
                updateStmt.setString(2, userId);
                updateStmt.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi gia hạn thẻ: " + e.getMessage());
        }
        return false;
    }

    private int parseMonths(String monthsString) {
        try {
            return Integer.parseInt(monthsString.split(" ")[0]);
        } catch (Exception e) {
            return 1; // Mặc định 1 tháng nếu không phân tích được
        }
    }
}
