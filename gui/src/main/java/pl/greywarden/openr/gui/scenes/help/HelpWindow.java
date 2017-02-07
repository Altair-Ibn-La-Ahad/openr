package pl.greywarden.openr.gui.scenes.help;

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
import pl.greywarden.openr.commons.IconManager;

import java.util.ArrayList;
import java.util.List;

import static pl.greywarden.openr.commons.I18nManager.getString;

public class HelpWindow extends Stage {

    private List<HelpTopicWrapper> topicWrappers;
    private WebView webView;
    private WebEngine webEngine;
    private WebHistory history;
    private ObservableList<WebHistory.Entry> historyEntries;
    private BorderPane helpContentPane;
    private final ListView<HelpTopicWrapper> helpTopics = createTopicsListView();
    private final HBox wrapper = new HBox(3);
    private final Button goBack = createBackButton();
    private final Button goForward = createForwardButton();
    private final Button goHome = createHomeButton();
    private final Button exit = createExitButton();
    private final ToolBar toolBar = new ToolBar();


    public HelpWindow() {
        super();
        super.setTitle(getString("help-window-title"));

        createTopicWrappers();
        createWebView();
        createToolBar();

        createHelpContent();
        createWindowLayoutWrapper();

        VBox layout = new VBox(5);
        layout.getChildren().addAll(toolBar, wrapper);
        super.setScene(new Scene(layout));
        helpContentPane.requestFocus();
        setMaximized(true);
        centerOnScreen();
        show();
    }

    private void createToolBar() {
        toolBar.getItems().addAll(goBack, goForward, goHome, new Separator(), exit);
    }

    private void createWindowLayoutWrapper() {
        wrapper.setPadding(new Insets(5));
        wrapper.getChildren().addAll(helpTopics, helpContentPane);
        VBox.setVgrow(wrapper, Priority.ALWAYS);
    }

    private void createHelpContent() {
        HelpContent helpContent = new HelpContent(webView);
        helpContentPane = new BorderPane(helpContent);
        helpContentPane.setStyle("-fx-border-color: -fx-text-box-border");
        HBox.setHgrow(helpContentPane, Priority.ALWAYS);
    }

    private ListView<HelpTopicWrapper> createTopicsListView() {
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
        return topics;
    }

    private Button createExitButton() {
        Button exit = new Button();
        exit.setOnAction(event -> super.close());
        exit.setGraphic(IconManager.getIcon("exit"));
        return exit;
    }

    private Button createHomeButton() {
        Button goHome = new Button();
        goHome.setOnAction(event -> webEngine.load(topicWrappers.get(0).getUrlToContent()));
        goHome.setGraphic(IconManager.getIcon("home"));
        return goHome;
    }

    private Button createForwardButton() {
        Button goForward = new Button();
        goForward.disableProperty().bind(history.currentIndexProperty().greaterThanOrEqualTo(historyEntries.size() - 1));
        goForward.setOnAction(event -> {
            history.go(1);
            webEngine.load(historyEntries.get(history.getCurrentIndex()).getUrl());
        });
        goForward.setGraphic(IconManager.getIcon("forward"));
        return goForward;
    }

    private Button createBackButton() {
        Button goBack = new Button();
        goBack.disableProperty().bind(history.currentIndexProperty().isEqualTo(0));
        goBack.setOnAction(event -> {
            history.go(-1);
            webEngine.load(historyEntries.get(history.getCurrentIndex()).getUrl());
        });
        goBack.setGraphic(IconManager.getIcon("back"));
        return goBack;
    }

    private void createWebView() {
        webView = new WebView();
        webEngine = webView.getEngine();
        history = webEngine.getHistory();
        historyEntries = history.getEntries();
    }

    private void createTopicWrappers() {
        topicWrappers = new ArrayList<>();
        topicWrappers.add(new HelpTopicWrapper("home"));
        topicWrappers.add(new HelpTopicWrapper("main-view"));
        topicWrappers.add(new HelpTopicWrapper("creating-entry"));
        topicWrappers.add(new HelpTopicWrapper("grep"));
        topicWrappers.add(new HelpTopicWrapper("find"));
        topicWrappers.add(new HelpTopicWrapper("known-issues"));
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
