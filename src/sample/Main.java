package sample;

import controllers.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import database.Database;
import models.AnalyzeInfo;
import models.Article;
import models.Balance;
import models.StatObject;
import utils.ScreenManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private Database db;
    private ScreenManager screenManager;
    private Scene mainScene;

    private final String LOGIN_SCREEN = "LOGIN_SCREEN";
    private final String WORK_SCREEN = "WORK_SCREEN";
    private final String MESSAGE_POPUP = "POPUP";
    private final String ERROR_TITLE = "An error occurred";
    private final String OK_TITLE = "All right";

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginForm.fxml"));
        Parent root = loader.load();

        FXMLLoader popupLoader = new FXMLLoader(getClass().getResource("messagePopup.fxml"));
        Parent popupContent = popupLoader.load();
        MessagePopupController popupController = popupLoader.getController();
        popupController.setContent(popupContent);

        FXMLLoader articlePopupLoader = new FXMLLoader(getClass().getResource("articlePopup.fxml"));
        Parent articlePopupContent = articlePopupLoader.load();
        ArticlePopupController articlePopupController = articlePopupLoader.getController();
        articlePopupController.setContent(articlePopupContent);

        FXMLLoader operationPopupLoader = new FXMLLoader(getClass().getResource("operationPopup.fxml"));
        Parent operationPopupContent = operationPopupLoader.load();
        OperationPopupController operationsPopupController = operationPopupLoader.getController();
        operationsPopupController.setContent(operationPopupContent);

        FXMLLoader balancePopupLoader = new FXMLLoader(getClass().getResource("balancePopup.fxml"));
        Parent balancePopupContent = balancePopupLoader.load();
        BalancePopupController balancePopupController = balancePopupLoader.getController();
        balancePopupController.setContent(balancePopupContent);

        FXMLLoader chartPopupLoader = new FXMLLoader(getClass().getResource("percentageAnalysis.fxml"));
        Parent chartPopupContent = chartPopupLoader.load();
        ChartPopupController chartPopupController = chartPopupLoader.getController();
        chartPopupController.setContent(chartPopupContent);

        FXMLLoader confirmationPopupLoader = new FXMLLoader(getClass().getResource("confirmationPopup.fxml"));
        Parent confirmationPopupContent = confirmationPopupLoader.load();
        ConfirmationPopupController confirmationPopupController = confirmationPopupLoader.getController();
        confirmationPopupController.setContent(confirmationPopupContent);

        FXMLLoader workFormLoader = new FXMLLoader(getClass().getResource("workForm.fxml"));
        Parent workFormRoot = workFormLoader.load();
        WorkFormController workFormController = workFormLoader.getController();
        workFormController.setListeners(
                () -> {
                    try {
                        System.out.println("1");
                        return db.fetchOperations();
                    } catch (final SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                    System.out.println("2");
                    return new ArrayList<>();
                },
                () -> {
                    try {
                        return db.fetchArticles();
                    } catch (final SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                    return new ArrayList<>();
                },
                () -> {
                    try {
                        return db.fetchBalances();
                    } catch (final SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                    return new ArrayList<>();
                },
                (callback) -> articlePopupController.show("Create article", null, article -> {
                    try {
                        db.saveArticle(article);
                        callback.run();
                    } catch (SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                }),
                (callback, article) -> articlePopupController.show("Update article", article, updatedArticle -> {
                    try {
                        db.saveArticle(updatedArticle);
                        callback.run();
                    } catch (SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                }),
                (callback, article) -> confirmationPopupController.show("Confirmation", "Do you want to remove article \"%s\"?".formatted(article.name), () -> {}, () -> {
                    try {
                        db.removeArticle(article);
                        callback.run();
                    } catch (SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                }),


                (callback) -> {
                    try {
                        operationsPopupController.show("Create operation", null, operation -> {
                            try {
                                db.saveOperation(operation);
                                callback.run();
                            } catch (SQLException throwable) {
                                popupController.show(ERROR_TITLE, throwable.getMessage());
                            }
                        }, db.fetchArticles());
                    } catch (SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                },
                (callback, operation) -> {
                    try {
                        operationsPopupController.show("Update operation", operation, updatedOperation -> {
                            try {
                                db.saveOperation(updatedOperation);
                                callback.run();
                            } catch (SQLException throwable) {
                                popupController.show(ERROR_TITLE, throwable.getMessage());
                            }
                        }, db.fetchArticles());
                    } catch (SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                },
                (callback, operation) -> confirmationPopupController.show("Confirmation", "Do you want to remove operation \"%s\" from %s?".formatted(operation.articleName, operation.createDate), () -> {}, () -> {
                    try {
                        db.removeOperation(operation);
                        callback.run();
                    } catch (SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                }),

                (callback) -> balancePopupController.show("Create balance", balance -> {
                    try {
                        db.createBalance(balance);
                        callback.run();
                    } catch (SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                }),
                (Runnable callback, Balance balance) -> confirmationPopupController.show("Confirmation", "Do you want to remove balance for %d.%d created at %s?".formatted(balance.month, balance.year, balance.createDate), () -> {}, () -> {
                    try {
                        db.removeBalance(balance);
                        callback.run();
                    } catch (SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                }),
                () -> chartPopupController.show("Statistics", (AnalyzeInfo analyzeInfo) -> {
                    try {
                        List<StatObject> statObjects = db.fetchPercents(analyzeInfo);
                        analyzeInfo.callback.run();
                        return statObjects;
                    } catch (SQLException throwable) {
                        popupController.show(ERROR_TITLE, throwable.getMessage());
                    }
                    return new ArrayList<>();
                })
        );

        mainScene = new Scene(root, 600, 400);
        screenManager = new ScreenManager(mainScene, LOGIN_SCREEN);
        screenManager.addScreen(WORK_SCREEN, workFormRoot);
        screenManager.addScreen(MESSAGE_POPUP, popupContent);

        LoginController controller = loader.getController();
        controller.setListeners(strings -> {
            try {
                if (db.login(strings[0], strings[1])) {
                    popupController.show(OK_TITLE, "Connected successfully. You are welcome!", () -> {
                        workFormController.fetchOperations();
                        screenManager.activate(WORK_SCREEN);
                    });
                } else {
                    popupController.show(ERROR_TITLE, "Incorrect login or password");
                }
            } catch (SQLException throwable) {
                popupController.show(ERROR_TITLE, throwable.getMessage());
            }
        });

        primaryStage.setTitle("Home budget APP (Inactive)");
        primaryStage.setScene(mainScene);
        primaryStage.setWidth(930);
        primaryStage.setHeight(530);
        primaryStage.setResizable(false);
        primaryStage.show();

        db = new Database();
        try {
            db.connect("c##test", "MyPass");
            primaryStage.setTitle("Home budget APP");
        } catch (final SQLException throwable) {
            popupController.show(ERROR_TITLE, throwable.getMessage(), primaryStage::close);
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
