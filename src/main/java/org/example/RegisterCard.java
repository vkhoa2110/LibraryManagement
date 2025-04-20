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

public class RegisterCard {
    private LibraryManagement library;
    private Stage primaryStage;

    public RegisterCard(LibraryManagement library) {
        this.library = library;
        this.primaryStage = library.getPrimaryStage(); 
    }

    // Phương thức xác định giao diện nào sẽ được hiển thị (đăng ký thẻ hoặc thay đổi loại thẻ)
    public Node showRegisterCardInterface() {
        double containerWidth = 600; 
        double containerHeight = 450; 

        VBox mainContainer = createMainContainer(containerWidth, containerHeight);

       
        Label registerLabel = new Label("Đăng Ký Thẻ Thư Viện");
        registerLabel.setFont(new Font("Arial", 24));
        registerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        
        String userId = interfaces.userId(); 
        String cardType = null;

        // Truy vấn để kiểm tra người dùng đã có thẻ hay chưa
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String query = "SELECT card_type FROM card WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                cardType = resultSet.getString("card_type");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
        }

        // Nếu người dùng đã có thẻ, hiển thị giao diện thay đổi loại thẻ
        if (cardType != null) {
            return showCardTypeChangeInterface(userId, cardType);
        } else {
            return showCardRegistrationInterface(userId);
        }
    }

   
    private VBox createMainContainer(double width, double height) {
        VBox mainContainer = new VBox(25); 
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.CENTER); 
        mainContainer.setPrefSize(width, height);
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffffff, #e6f2ff); " +
                "-fx-border-radius: 20px; -fx-background-radius: 20px; " +
                "-fx-border-color: #b0c4de; -fx-border-width: 2px;");
        mainContainer.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.25)));
        return mainContainer;
    }

    // Giao diện đăng ký thẻ với 4 loại thẻ khác nhau
    private Node showCardRegistrationInterface(String userId) {
        double containerWidth = 600;
        double containerHeight = 450;

        VBox mainContainer = createMainContainer(containerWidth, containerHeight);

        Label registerLabel = new Label("Đăng Ký Thẻ Thư Viện");
        registerLabel.setFont(new Font("Arial", 24));
        registerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        Label cardTypeLabel = new Label("Chọn Loại Thẻ:");
        cardTypeLabel.setFont(Font.font("Arial", 14));
        cardTypeLabel.setStyle("-fx-font-weight: bold;");

        Label instructionLabel = new Label("Vui lòng chọn loại thẻ bạn muốn đăng ký:");
        instructionLabel.setFont(Font.font("Arial", 14));
        instructionLabel.setStyle("-fx-font-weight: bold;");

        GridPane cardOptions = new GridPane();
        cardOptions.setHgap(20); 
        cardOptions.setVgap(20);
        cardOptions.setAlignment(Pos.CENTER);

        Button cardButtonRegular = createCardTypeButton(userId, "Thẻ Thường", "#00BFFF");
        Button cardButtonPlus = createCardTypeButton(userId, "Thẻ Plus", "#32CD32");
        Button cardButtonPremium = createCardTypeButton(userId, "Thẻ Premium", "#FFD700");
        Button cardButtonVIP = createCardTypeButton(userId, "Thẻ VIP", "#8A2BE2");

        // Thêm các nút loại thẻ vào GridPane
        cardOptions.add(cardButtonRegular, 0, 0);
        cardOptions.add(cardButtonPlus, 1, 0);
        cardOptions.add(cardButtonPremium, 0, 1);
        cardOptions.add(cardButtonVIP, 1, 1);

        // Thêm các phần tử vào container chính
        mainContainer.getChildren().addAll(
                registerLabel,
                instructionLabel,
                cardOptions
        );

        StackPane wrapper = new StackPane();
        wrapper.setPrefSize(containerWidth, containerHeight);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.getChildren().add(mainContainer);

        return wrapper;
    }

    // Tạo nút cho mỗi loại thẻ
    private Button createCardTypeButton(String userId, String cardType, String color) {
        Button cardButton = new Button(cardType);
        cardButton.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10px;");
        cardButton.setPrefWidth(140);
        cardButton.setEffect(new DropShadow(5, Color.GRAY));
        
        cardButton.setOnMouseEntered(e -> cardButton.setStyle("-fx-background-color: #FF6347; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10px;"));
        cardButton.setOnMouseExited(e -> cardButton.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10px;"));

        cardButton.setOnAction(e -> {
            boolean success = registerNewCard(userId, cardType);  // Đăng ký thẻ mới
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Thành Công", "Đăng ký thẻ thành công!.Vui lòng chờ 1 ngày để được duyệt");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đăng ký thẻ. Vui lòng thử lại sau.");
            }
        });

        return cardButton;
    }

    // Đăng ký thẻ mới trong cơ sở dữ liệu
    private boolean registerNewCard(String userId, String cardType) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String query = "INSERT INTO card (user_id, card_type, status) VALUES (?, ?, 'Chờ duyệt')";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, cardType);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
            return false;
        }
    }

    // Phương thức hiển thị cảnh báo
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Thay đổi loại thẻ trong cơ sở dữ liệu
    private boolean changeCardType(String userId, String newCardType) {
        try (Connection connection = DatabaseConnection.connectToLibrary()) {
            String query = "UPDATE card SET card_type = ?, status = 'Chờ duyệt' WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newCardType);
            preparedStatement.setString(2, userId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
            return false;
        }
    }

    // Giao diện thay đổi loại thẻ
    private Node showCardTypeChangeInterface(String userId, String cardType) {
        double containerWidth = 600;
        double containerHeight = 450;

        VBox mainContainer = createMainContainer(containerWidth, containerHeight);

        Label registerLabel = new Label("Thay Đổi Loại Thẻ");
        registerLabel.setFont(new Font("Arial", 24));
        registerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2a2a2a;");

        Label cardTypeLabel = new Label("Loại Thẻ Hiện Tại: " + cardType);
        cardTypeLabel.setFont(Font.font("Arial", 14));
        cardTypeLabel.setStyle("-fx-font-weight: bold;");

        // Các nút thay đổi loại thẻ
        Button changeButtonRegular = createCardTypeButton(userId, "Thẻ Thường", "#00BFFF");
        Button changeButtonPlus = createCardTypeButton(userId, "Thẻ Plus", "#32CD32");
        Button changeButtonPremium = createCardTypeButton(userId, "Thẻ Premium", "#FFD700");
        Button changeButtonVIP = createCardTypeButton(userId, "Thẻ VIP", "#8A2BE2");

        GridPane cardOptions = new GridPane();
        cardOptions.setHgap(20);
        cardOptions.setVgap(20);
        cardOptions.setAlignment(Pos.CENTER);

        cardOptions.add(changeButtonRegular, 0, 0);
        cardOptions.add(changeButtonPlus, 1, 0);
        cardOptions.add(changeButtonPremium, 0, 1);
        cardOptions.add(changeButtonVIP, 1, 1);

        mainContainer.getChildren().addAll(
                registerLabel,
                cardTypeLabel,
                cardOptions
        );
        
        StackPane wrapper = new StackPane();
        wrapper.setPrefSize(containerWidth, containerHeight);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.getChildren().add(mainContainer);

        return wrapper;
    }
}
