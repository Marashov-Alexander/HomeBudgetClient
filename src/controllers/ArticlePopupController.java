package controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Article;

import java.util.function.Consumer;

public class ArticlePopupController {

    @FXML
    private TextField articleNameTB;

    @FXML
    private Button articleSaveBtn;

    private Parent articleContent;
    private Scene articleScene = null;

    public void setContent(final Parent articleContent) {
        this.articleContent = articleContent;
    }

    public void show(final String title, final Article article, final Consumer<Article> onSave) {
        final Stage myDialog = new Stage();
        myDialog.initModality(Modality.APPLICATION_MODAL);
        myDialog.setTitle(title);
        if (article != null) {
            articleNameTB.setText(article.getName());
        } else {
            articleNameTB.setText("");
        }

        articleNameTB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");

        if (articleScene == null) {
            articleScene = new Scene(articleContent);

        }
        myDialog.setScene(articleScene);

        articleSaveBtn.setOnAction(arg0 -> {
            final String articleName = articleNameTB.getText();
            if (articleName.isEmpty()) {
                System.out.println(articleNameTB.getStyle());
                articleNameTB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(240, 17, 99, 0.8), 10, 0, 0, 0);");
                return;
            }
            Article updatedArticle = article;
            if (updatedArticle == null) {
                updatedArticle = new Article(null, articleName);
            } else {
                updatedArticle.name = articleName;
            }
            myDialog.close();
            articleScene.disposePeer();
            onSave.accept(updatedArticle);
        });
        myDialog.setResizable(false);
        myDialog.show();
    }
}
