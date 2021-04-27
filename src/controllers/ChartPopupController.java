package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.AnalyzeInfo;
import models.Article;
import models.StatObject;
import utils.Utils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChartPopupController {

    @FXML
    private ComboBox<String> typeCB;

    @FXML private DatePicker dateFromDP;
    @FXML private DatePicker dateToDP;

    @FXML private PieChart chartPie;

    @FXML
    private Button btnClose;

    private Parent articleContent;
    private Scene articleScene = null;

    public void setContent(final Parent articleContent) {
        this.articleContent = articleContent;
    }

    private Function<AnalyzeInfo, List<StatObject>> statFunction;

    public void show(final String title, final Function<AnalyzeInfo, List<StatObject>> statFunction) {
        this.statFunction = statFunction;
        final Stage myDialog = new Stage();
        myDialog.initModality(Modality.APPLICATION_MODAL);
        myDialog.setTitle(title);

        typeCB.setItems(FXCollections.observableArrayList(Utils.TYPES));
        typeCB.setValue(Utils.TYPES[0]);

        if (articleScene == null) {
            articleScene = new Scene(articleContent);
        }
        myDialog.setScene(articleScene);

        btnClose.setOnAction(arg0 -> {
            myDialog.close();
            articleScene.disposePeer();
        });
        myDialog.setResizable(false);
        myDialog.show();
    }


    public void onCloseClicked(ActionEvent actionEvent) {

    }

    public void onShowClicked(ActionEvent actionEvent) {
        chartPie.getData().clear();
        Date dateFrom = Date.valueOf(dateFromDP.getValue());
        Date dateTo = Date.valueOf(dateToDP.getValue());
        int type = Utils.typeNumber(typeCB.getValue());
        final AnalyzeInfo analyzeInfo = new AnalyzeInfo(() -> {}, dateFrom, dateTo, type);
        final List<StatObject> apply = statFunction.apply(analyzeInfo);
        final List<PieChart.Data> dataList = new ArrayList<>();
        for (StatObject statObject : apply) {
            dataList.add(new PieChart.Data(statObject.getArticleName(), statObject.getPercent()));
        }
        chartPie.setData(FXCollections.observableArrayList(dataList));
    }
}
