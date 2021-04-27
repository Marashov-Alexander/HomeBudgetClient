package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Article;
import models.Balance;
import models.Operation;
import utils.ActionButtonTableCell;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WorkFormController {

    private ObservableList<Article> articlesData = FXCollections.observableArrayList();
    private ObservableList<Balance> balancesData = FXCollections.observableArrayList();
    private ObservableList<Operation> operationsData = FXCollections.observableArrayList();

    @FXML private Button createArticleBtn;
    @FXML private TabPane workTabPane;

    @FXML private TableView<Article> articlesTable;
    @FXML private TableColumn<Article, Integer> articleIdCol;
    @FXML private TableColumn<Article, String> articleNameCol;
    @FXML private TableColumn<Article, Button> updateArticleCol;
    @FXML private TableColumn<Article, Button> removeArticleCol;

    @FXML private TableView<Balance> balancesTable;
    @FXML private TableColumn<Balance, Integer> balanceIdCol;
    @FXML private TableColumn<Balance, String>  balanceCreateDateCol;
    @FXML private TableColumn<Balance, Integer> balanceDebitCol;
    @FXML private TableColumn<Balance, Integer> balanceCreditCol;
    @FXML private TableColumn<Balance, Integer> balanceAmountCol;
    @FXML private TableColumn<Balance, Integer> monthCol;
    @FXML private TableColumn<Balance, Integer> yearCol;
    @FXML private TableColumn<Balance, Button> removeBalanceCol;

    @FXML private TableView<Operation> operationsTable;
    @FXML private TableColumn<Operation, Integer> opsIdCol;
    @FXML private TableColumn<Operation, String>  opsArticleCol;
    @FXML private TableColumn<Operation, Integer> opsDebitCol;
    @FXML private TableColumn<Operation, Integer> opsCreditCol;
    @FXML private TableColumn<Operation, String> opsCreatedCol;
    @FXML private TableColumn<Operation, String> opsBalanceCol;
    @FXML private TableColumn<Operation, Button> updateOpsCol;
    @FXML private TableColumn<Operation, Button> removeOpsCol;

    private Supplier<List<Article>> articlesSupplier;
    private Supplier<List<Balance>> balancesSupplier;
    private Supplier<List<Operation>> operationsSupplier;

    private Consumer<Runnable> onCreateArticle;
    private BiConsumer<Runnable, Article> onUpdateArticle;
    private BiConsumer<Runnable, Article> onRemoveArticle;

    private Consumer<Runnable> onCreateOperation;
    private BiConsumer<Runnable, Operation> onUpdateOperation;
    private BiConsumer<Runnable, Operation> onRemoveOperation;

    private Consumer<Runnable> onCreateBalance;
    private BiConsumer<Runnable, Balance> onRemoveBalance;

    private Runnable onStatClicked;

    public void setListeners(
            Supplier<List<Operation>> operationsSupplier,
            Supplier<List<Article>> articlesSupplier,
            Supplier<List<Balance>> balancesSupplier,

            Consumer<Runnable> onCreateArticle,
            BiConsumer<Runnable, Article> onUpdateArticle,
            BiConsumer<Runnable, Article> onRemoveArticle,

            Consumer<Runnable> onCreateOperation,
            BiConsumer<Runnable, Operation> onUpdateOperation,
            BiConsumer<Runnable, Operation> onRemoveOperation,

            Consumer<Runnable> onCreateBalance,
            BiConsumer<Runnable, Balance> onRemoveBalance,
            Runnable onStatClicked
    ) {
        this.operationsSupplier = operationsSupplier;
        this.articlesSupplier = articlesSupplier;
        this.balancesSupplier = balancesSupplier;
        this.onCreateArticle = onCreateArticle;
        this.onUpdateArticle = onUpdateArticle;
        this.onRemoveArticle = onRemoveArticle;

        this.onCreateOperation = onCreateOperation;
        this.onUpdateOperation = onUpdateOperation;
        this.onRemoveOperation = onRemoveOperation;

        this.onCreateBalance = onCreateBalance;
        this.onRemoveBalance = onRemoveBalance;

        this.onStatClicked = onStatClicked;
    }



    @FXML
    private void initialize() {

        // устанавливаем тип и значение которое должно хранится в колонке
        articleIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        articleNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        updateArticleCol.setCellFactory(ActionButtonTableCell.forTableColumn("Update", (Article article) -> {
            System.out.println("UPDATE ARTICLE " + article.id);
            onUpdateArticle.accept(this::fetchArticles, article);
            return article;
        }));
        removeArticleCol.setCellFactory(ActionButtonTableCell.forTableColumn("Remove", (Article article) -> {
//            table.getItems().remove(p);
            System.out.println("REMOVE ARTICLE " + article.id);
            onRemoveArticle.accept(this::fetchArticles, article);
            return article;
        }));

        balanceIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        balanceCreateDateCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        balanceDebitCol.setCellValueFactory(new PropertyValueFactory<>("debit"));
        balanceCreditCol.setCellValueFactory(new PropertyValueFactory<>("credit"));
        balanceAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        removeBalanceCol.setCellFactory(ActionButtonTableCell.forTableColumn("Remove", (Balance balance) -> {
//            table.getItems().remove(p);
            System.out.println("REMOVE BALANCE " + balance.id);
            onRemoveBalance.accept(this::fetchBalances, balance);
            return balance;
        }));

        // устанавливаем тип и значение которое должно хранится в колонке
        opsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        opsArticleCol.setCellValueFactory(new PropertyValueFactory<>("articleName"));
        opsDebitCol.setCellValueFactory(new PropertyValueFactory<>("debit"));
        opsCreditCol.setCellValueFactory(new PropertyValueFactory<>("credit"));
        opsCreatedCol.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        opsBalanceCol.setCellValueFactory(new PropertyValueFactory<>("balancePeriod"));
        updateOpsCol.setCellFactory(ActionButtonTableCell.forTableColumn("Update", (Operation operation) -> {
            System.out.println("UPDATE OPERATION " + operation.id);
            onUpdateOperation.accept(this::fetchOperations, operation);
            return operation;
        }));
        removeOpsCol.setCellFactory(ActionButtonTableCell.forTableColumn("Remove", (Operation operation) -> {
            System.out.println("REMOVE OPERATION " + operation.id);
            onRemoveOperation.accept(this::fetchOperations, operation);
            return operation;
        }));
    }

    public void onTabSelected(Event event) {
        switch (((Tab) event.getSource()).getId()) {
            case "articlesTab" -> fetchArticles();
            case "balancesTab" -> fetchBalances();
            case "operationsTab" -> fetchOperations();
        }
    }

    public void fetchOperations() {
        if (operationsSupplier == null)
            return;
        operationsData.clear();
        operationsData.addAll(operationsSupplier.get());
        operationsTable.setItems(operationsData);
    }

    private void fetchBalances() {
        balancesData.clear();
        balancesData.addAll(balancesSupplier.get());
        balancesTable.setItems(balancesData);
    }

    public void onCreateArticleClicked(ActionEvent actionEvent) {
        onCreateArticle.accept(this::fetchArticles);
    }

    public void onCreateOperationClick(ActionEvent actionEvent) {
        onCreateOperation.accept(this::fetchOperations);
    }

    public void onCreateBalanceClicked(ActionEvent actionEvent) {
        onCreateBalance.accept(this::fetchBalances);
    }

    private void fetchArticles() {
        System.out.println("FETCH ARTICLES");
        articlesData.clear();
        articlesData.addAll(articlesSupplier.get());
        articlesTable.setItems(articlesData);
    }

    public void onAnalisisClicked1(ActionEvent actionEvent) {
        onStatClicked.run();
    }
}
