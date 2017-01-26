package pl.greywarden.openr.gui.scenes;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import pl.greywarden.openr.gui.IconManager;

import java.util.ArrayList;
import java.util.List;

import static pl.greywarden.openr.i18n.I18nManager.getString;

public class HelpWindow extends Stage {

    private List<HelpTopicWrapper> topicWrappers;

    public HelpWindow() {
        super();
        VBox layout = new VBox(5);
        super.setScene(new Scene(layout));
        super.setTitle(getString("help-window-title"));

        topicWrappers = new ArrayList<>();
        topicWrappers.add(new HelpTopicWrapper("home"));
        topicWrappers.add(new HelpTopicWrapper("main-view"));
        topicWrappers.add(new HelpTopicWrapper("creating-entry"));
        topicWrappers.add(new HelpTopicWrapper("grep"));
        topicWrappers.add(new HelpTopicWrapper("find"));
        topicWrappers.add(new HelpTopicWrapper("known-issues"));

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        WebHistory history = webEngine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        Button goBack = new Button();
        Button goForward = new Button();
        Button goHome = new Button();
        Button exit = new Button();
        ToolBar toolBar = new ToolBar();

        goBack.disableProperty().bind(history.currentIndexProperty().isEqualTo(0));
        goForward.disableProperty().bind(history.currentIndexProperty().greaterThanOrEqualTo(entries.size() - 1));

        goBack.setOnAction(event -> {
            history.go(-1);
            webEngine.load(entries.get(history.getCurrentIndex()).getUrl());
        });
        goForward.setOnAction(event -> {
            history.go(1);
            webEngine.load(entries.get(history.getCurrentIndex()).getUrl());
        });

        goHome.setOnAction(event -> webEngine.load(topicWrappers.get(0).getUrlToContent()));
        exit.setOnAction(event -> super.close());

        goHome.setGraphic(IconManager.getIcon("home"));
        goBack.setGraphic(IconManager.getIcon("back"));
        goForward.setGraphic(IconManager.getIcon("forward"));
        exit.setGraphic(IconManager.getIcon("exit"));

        toolBar.getItems().addAll(goBack, goForward, goHome, new Separator(), exit);

        HBox wrapper = new HBox(3);
        wrapper.setPadding(new Insets(5));
        ListView<HelpTopicWrapper> topics = new ListView<>();
        topics.setCellFactory(param -> new ListCell<HelpTopicWrapper>() {
            @Override
            protected void updateItem(HelpTopicWrapper item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item.getTopic());
                }
            }
        });
        topics.minWidthProperty().setValue(300);
        topics.getItems().setAll(topicWrappers);
        topics.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        topics.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                webEngine.load(topics.getSelectionModel().getSelectedItem().getUrlToContent());
            }
        });

        HelpContent helpContent = new HelpContent(webView);
        BorderPane helpContentPane = new BorderPane(helpContent);
        helpContentPane.setStyle("-fx-border-color: -fx-text-box-border");
        HBox.setHgrow(helpContentPane, Priority.ALWAYS);

        wrapper.getChildren().addAll(topics, helpContentPane);

        VBox.setVgrow(wrapper, Priority.ALWAYS);
        layout.getChildren().addAll(toolBar, wrapper);
        helpContentPane.requestFocus();
        setMaximized(true);
        centerOnScreen();
        show();
    }

    private class HelpContent extends Region {
        private final WebView webView;

        private HelpContent(WebView webView) {
            this.webView = webView;
            getStyleClass().add("browser");
            getChildren().add(webView);

        }

        @Override
        protected void layoutChildren() {
            double w = getWidth();
            double h = getHeight();
            layoutInArea(webView, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
        }

        @Override
        protected double computePrefWidth(double height) {
            return 750;
        }

        @Override
        protected double computePrefHeight(double width) {
            return 500;
        }
    }

}
