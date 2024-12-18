package com.mycompany.atlantafx_gluon;

import atlantafx.base.controls.*;
import atlantafx.base.theme.*;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;

import static javafx.scene.control.Alert.AlertType;
import static javafx.scene.control.ButtonBar.ButtonData;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.feather.Feather;
//import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;
import org.kordamp.ikonli.javafx.StackedFontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.kordamp.ikonli.Ikon;
import net.datafaker.Faker;

import java.util.Optional;
import java.util.Random;

import javafx.beans.binding.Bindings;

import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.Objects;

import javafx.util.Duration;

//import com.sun.javafx.scene.control.skin.FXVK;

public class App extends Application {

    static Theme theme;

    Scene scene;
    static final boolean SHOW_KEYBOARD = true; // true za scene.addStyle da bi android imao soft keyboard
    public static final String ASSETS_DIR = "/assets/";
    Stage stage_copy;
    StackPane stackPane;
    public static ModalPane modalPane;
    Button dangerBtn;
    public static final StringProperty toolbar_taxt = new SimpleStringProperty("Neki tool-tip");
    public static final StringProperty memory_taxt = new SimpleStringProperty("Mem Usage");
    public static final StringProperty label_taxt = new SimpleStringProperty("Theme: ");
    Tooltip basicTtp = new Tooltip(toolbar_taxt.get());
    Label mem_label, cpu_label;
    long total_time;
    int count;
    public static final StringProperty fps = new SimpleStringProperty("FPS: 0");

    static final String APP_ICON_PATH = Objects.requireNonNull(
            App.class.getResource(ASSETS_DIR + "icons/app-icon.png")
    ).toExternalForm();

    static final String CPU_IMAGE_PATH = Objects.requireNonNull(
            App.class.getResource(ASSETS_DIR + "icons/icons8-microchip-50.png")
    ).toExternalForm();


    //------------------------------------------------------
    public void init() throws Exception {
//        System.setProperty("javafx.animation.pulse","30");
//        System.out.println("Method init()");
        System_Info.load_settings();
        try{
            while(System_Info.user.getValue() == null){ System.out.print('-'); }
            System.out.println("\nUser obj: " + System_Info.user.getValue());
            System_Info.setTheme(System_Info.user.get().getThemeNumber());
        }catch(Exception e){
            e.printStackTrace();
            System_Info.setTheme(1);
        }

//        System_Info.setTheme(3);
        super.init();
    }


    //------------------------------------------------------
    @Override
    public void start(Stage stage) {
        System.setProperty("com.sun.javafx.isEmbedded", "true");
        System.setProperty("com.sun.javafx.virtualKeyboard", "javafx");
        String javaVersion = System_Info.javaVersion();
        String javafxVersion = System_Info.javafxVersion();
        System.out.println("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        stage_copy = stage;

        toolbar_taxt.addListener((observable, oldValue, newValue) -> {
            //  in javaFX only the FX thread can modify the UI elements
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("old: " + oldValue + "\nnew: " + newValue);
                    basicTtp.setText(toolbar_taxt.get());
                    dangerBtn.setTooltip(basicTtp);
                }
            });
        });


        memory_taxt.addListener((observable, oldValue, newValue) -> {
            //  in javaFX only the FX thread can modify the UI elements
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    mem_label.setText(memory_taxt.get());
                }
            });
        });

        // set AtlantaFX stylesheet
//        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        if (!SHOW_KEYBOARD) {
//            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet()); // TODO - mobile softkeybord error ( -37.81818 )
            Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());
        }

        System_Info.get_display();

        modalPane = new ModalPane();
        modalPane.displayProperty().addListener((obs, old, val) -> {
            if (!val) {
                modalPane.setAlignment(Pos.CENTER);
                modalPane.usePredefinedTransitionFactories(null);
//                modalPane.usePredefinedTransitionFactories(null,Duration.seconds(2),Duration.seconds(2));
            }
        });
        modalPane.setId("modalPane");

        stackPane = new StackPane(modalPane, createWelcomePane(stage), System_Info.notification()); // notification create and add to root stack pane
        Label lbl = new Label("---");
        lbl.getStyleClass().addAll("fps_lbl");
//        lbl.setMaxWidth(Double.MAX_VALUE);
//        lbl.setAlignment(Pos.BOTTOM_RIGHT);  // Ako koristimo VBox umesto Pane
        lbl.textProperty().bind(fps);
        Pane root = new Pane(stackPane,lbl) {
            @Override
            protected void layoutChildren() {
                Insets insets = getInsets();
                stackPane.setLayoutX(insets.getLeft()); stackPane.setLayoutY(insets.getTop());
                stackPane.setPrefWidth(insets.getLeft() + getWidth());
                stackPane.setPrefHeight(insets.getTop() + getHeight());
                lbl.setLayoutX(insets.getLeft()); lbl.setLayoutY(insets.getTop()+getHeight() - lbl.getHeight());
                super.layoutChildren();
            }
        };
        VBox.setVgrow(stackPane,Priority.ALWAYS);
        scene = new Scene(root, System_Info.dimension.getWidth(), System_Info.dimension.getHeight());
        System.out.println("w: " + System_Info.dimension.getWidth() + ",h: " + System_Info.dimension.getHeight());
//        scene.getStylesheets().add(ASSETS_DIR + "index.css"); // moze za native image ali ne moze za javafx:run
        if (SHOW_KEYBOARD) {
            // IMPORTANT - ovako nema custom ikone na custom text kontroli
//            scene.getStylesheets().add(new PrimerDark().getUserAgentStylesheet());  // 1. dodajemo glavnu theme a dole index posle
//            scene.getStylesheets().add(new CupertinoLight().getUserAgentStylesheet());
            scene.getStylesheets().add(theme.getUserAgentStylesheet());
        }
        scene.getStylesheets().add("index.css"); // prebaciti index.css u resource root folder kao i fonts folder

        stage.setScene(scene);
        stage.setTitle("AtlantaFX");
        stage.getIcons().add(new Image(APP_ICON_PATH));
        stage.setOnCloseRequest(t -> Platform.exit());
        Platform.setImplicitExit(false);
        //       stage.setMaxWidth(1280);
        //       stage.setMaxHeight(900);

        scene.addPreLayoutPulseListener(new Runnable() {
            @Override
            public void run() {
//                System.out.println("PreLayoutPulseListener: " + System.currentTimeMillis());
                count++;
                if(System.currentTimeMillis() >= total_time + 1000){
//                    System.out.println("FPS: " + count);
                    fps.set("FPS: " + count);
                    count = 0;
                    total_time = System.currentTimeMillis();
                }
            }
        });

        Platform.runLater(() -> {
            stage.show();
//            stage.requestFocus();
            mem_usage_timer();
        });
    }



    //--------------------------------------------------------------------
    private VBox createWelcomePane(Stage stage) {
        var root = new VBox();
        root.getStyleClass().add("welcome");
        root.setSpacing(10);

        //----------------------------------------------------
        var menu_label = new Label(theme.getName());
        menu_label.setAlignment(Pos.CENTER);
        var chb1 = new MenuButton("", menu_label);
        chb1.getStyleClass().addAll(Styles.ELEVATED_1, Tweaks.NO_ARROW);
        chb1.setPopupSide(Side.BOTTOM);
        chb1.setPrefWidth(170);
//        chb1.setAlignment(Pos.CENTER);
        var m1 = new MenuItem("CupertinoDark");
        m1.setOnAction(event -> {
            System_Info.setTheme(3);
            set_theme(stage);
        });
        var m2 = new MenuItem("CupertinoLight");
        m2.setOnAction(event -> {
            System_Info.setTheme(4);
            set_theme(stage);
        });
        var m3 = new MenuItem("PrimerDark");
        m3.setOnAction(event -> {
            System_Info.setTheme(1);
            set_theme(stage);
        });
        var m4 = new MenuItem("PrimerLight");
        m4.setOnAction(event -> {
            System_Info.setTheme(0);
            set_theme(stage);
        });
        var m5 = new MenuItem("Dracula");
        m5.setOnAction(event -> {
            System_Info.setTheme(2);
            set_theme(stage);
        });
        var m6 = new MenuItem("NordLight");
        m6.setOnAction(event -> {
            System_Info.setTheme(5);
            set_theme(stage);
        });
        var m7 = new MenuItem("NordDark");
        m7.setOnAction(event -> {
            System_Info.setTheme(6);
            set_theme(stage);
        });
        chb1.getItems().setAll(m1, m2, m3, m4, m5, m6, m7);

        //---------------------------------------------------
        var barToggle = new ToggleButton("Start", new FontIcon(Feather.DOWNLOAD));
        barToggle.setOnAction(a -> {
                    // notifikacija na main screen
                    if (barToggle.isSelected()) System_Info.show_notification("CPU Intensive process", Styles.WARNING);
                }
        );
        barToggle.textProperty().bind(Bindings.createStringBinding(
                () -> barToggle.isSelected() ? "Stop" : "Start",
                barToggle.selectedProperty()
        ));
        barToggle.getStyleClass().add(Styles.SMALL);
        var bar = new ProgressBar(0);
        bar.getStyleClass().add(Styles.SMALL);
        bar.progressProperty().bind(Bindings.createDoubleBinding(
                () -> barToggle.isSelected() ? -1d : 0d,
                barToggle.selectedProperty()
        ));


        basicTtp.setHideDelay(Duration.seconds(3));

        //---------------------------------------------
        var leftDialog_1 = create_left_dialog();
        var menuBtn = new Button(null, new FontIcon(Feather.MENU));
        menuBtn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.FLAT, Styles.LARGE
        );
        menuBtn.setOnAction(evt -> {
            modalPane.setAlignment(Pos.TOP_LEFT);
            modalPane.usePredefinedTransitionFactories(Side.LEFT);
            modalPane.show(leftDialog_1);
        });

        //---------------------------------------------
        var htmlDialog = make_html_dialog();
        var htmlBtn = new Button(null, new FontIcon(Feather.INFO));
        htmlBtn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.FLAT, Styles.LARGE
        );
        htmlBtn.setOnAction(evt -> {
            modalPane.setAlignment(Pos.TOP_LEFT);
            modalPane.usePredefinedTransitionFactories(Side.LEFT);
            modalPane.show(htmlDialog);
        });
        //---------------------------------------------
        var settingsDialog = make_settings_dialog();
        var settingsBtn = new Button(null, new FontIcon(Feather.CALENDAR));
        settingsBtn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.FLAT, Styles.LARGE
        );
        settingsBtn.setOnAction(evt -> {
            modalPane.setAlignment(Pos.TOP_RIGHT);
            modalPane.usePredefinedTransitionFactories(Side.RIGHT);
            modalPane.show(settingsDialog);
        });
        //---------------------------------------------
        var settingsDialog_2 = make_settings_dialog_2();
        var settingsBtn_2 = new Button(null, new FontIcon(Feather.MONITOR));
        settingsBtn_2.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.FLAT, Styles.LARGE
        );
        settingsBtn_2.setOnAction(evt -> {
            modalPane.setAlignment(Pos.TOP_RIGHT);
            modalPane.usePredefinedTransitionFactories(Side.RIGHT);
            modalPane.show(settingsDialog_2);
        });
        //--------------------------------------------------
        var card_Btn = new Button(null, stackingExample());
        card_Btn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.FLAT, Styles.LARGE
        );
        card_Btn.setOnAction(evt -> {
            modalPane.setAlignment(Pos.TOP_CENTER);
            modalPane.usePredefinedTransitionFactories(Side.TOP);
            modalPane.show(tile_card_dialog());
        });
        //--------------------------------------------------
        //######### ne koristim je ovde - samo primer ###########
        var theme_lbl = new Label();
        theme_lbl.setAlignment(Pos.CENTER);
        theme_lbl.textProperty().bind(label_taxt);  // bind Label sa StringProperty
        label_taxt.set("Theme: " + theme.getName());
        //######################################################
        dangerBtn = new Button("Charts", new FontIcon(Feather.BAR_CHART_2));
        dangerBtn.getStyleClass().add(Styles.DANGER);
        dangerBtn.setContentDisplay(ContentDisplay.RIGHT);
        dangerBtn.setMnemonicParsing(true);
        dangerBtn.setTooltip(basicTtp);
        dangerBtn.setOnAction(evt -> {
//            System.out.println("Action");
            modalPane.setAlignment(Pos.BOTTOM_CENTER);
            modalPane.usePredefinedTransitionFactories(Side.BOTTOM);
            modalPane.show(chart_dialog());
        });

        int platform_result = System_Info.get_platform();
        if (platform_result > 0) {
            if (System_Info.platform.equals("Android")) {
                toolbar_taxt.set("Android tool-tip");
            } else {
                toolbar_taxt.set("IOS tool-tip");
            }
        } else {
            Faker FAKER = new Faker();
            try {
                String str = FAKER.chuckNorris().fact();  // uvek na engleskom
//                    toolbar_taxt.set(new Faker(new Locale("hr", "HR")).name().fullName());
                toolbar_taxt.set(str);
            } catch (Exception e) {
                toolbar_taxt.set("chuckNorris.fact() is null");
            }
        }

        var hTickSlider = createTickSlider();
        hTickSlider.getStyleClass().add(Styles.SMALL);
        hTickSlider.setSkin(new ProgressSliderSkin(hTickSlider));

        FontIcon icon = new FontIcon(MaterialDesignS.SCHOOL);
        icon.getStyleClass().add("shool");

//        cpu_label = new Label("CPU: 0",new ImageView(new Image(CPU_IMAGE_PATH)));
        cpu_label = new Label("CPU: 0", new FontIcon(Material2OutlinedAL.FLASH_ON));
        cpu_label.setAlignment(Pos.CENTER);
        mem_label = new Label(memory_taxt.get(), new FontIcon(Material2OutlinedMZ.MEMORY));
        mem_label.setAlignment(Pos.CENTER);

        HBox icon_box = new HBox(menuBtn, htmlBtn, card_Btn, settingsBtn, settingsBtn_2);
        icon_box.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                bar,
                barToggle,
                hTickSlider,
                dangerBtn,
                icon,
                chb1/*theme_lbl*/,
                cpu_label,
                mem_label,
                icon_box,
                custom_combo(),
                show_dialog() /*,
                pageInfoAlignmentExample()
                */
        );
        root.setAlignment(Pos.CENTER);

        return root;
    }


    //------------------------------------------
    private void set_theme(Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System_Info.save_settings();
                    stage.close();
                    System.gc();
                    start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //----------------------------------------------------
    private Slider createTickSlider() {
        var slider = new Slider(1, 5, 3);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setBlockIncrement(1);
        slider.setMinorTickCount(5);
        slider.setSnapToTicks(true);
        return slider;
    }


    //------------------------------------------------------------------
    private StackedFontIcon stackingExample() {
        var dataClass1 = """
                .stacked-ikonli-font-icon > .outer-icon {
                    -fx-icon-size: 48px;
                    -fx-icon-color: -color-danger-emphasis;
                }
                .stacked-ikonli-font-icon > .inner-icon {
                    -fx-icon-size: 24px;
                }
                """;


        //snippet_2:start
        var outerIcon1 = new FontIcon(Material2OutlinedAL.BLOCK);
        outerIcon1.getStyleClass().add("outer-icon");

        var innerIcon1 = new FontIcon(Material2MZ.PHOTO_CAMERA);
        innerIcon1.getStyleClass().add("inner-icon");

        var stackIcon1 = new StackedFontIcon();
        stackIcon1.getChildren().addAll(innerIcon1, outerIcon1);
        // .stacked-ikonli-font-icon > .outer-icon {
        //   -fx-icon-size: 48px;
        //   -fx-icon-color: -color-danger-emphasis;
        // }
        // .stacked-ikonli-font-icon > .inner-icon {
        //   -fx-icon-size: 24px;
        // }
        stackIcon1.getStylesheets().add(Styles.toDataURI(dataClass1));
        return stackIcon1;
    }


    //------------------------------------------------------------------
    // Pagination je vise upotrebljiva za Desktop
    private HBox pageInfoAlignmentExample() {
        var pg = new Pagination(25, 0);
        pg.setMaxPageIndicatorCount(5);

        // Note this. It's another ancient #javafx-bug:
        // https://bugs.openjdk.org/browse/JDK-8088711
        // Pagination doesn't calculate its size correctly.
        // To work around this issue, you should adjust the pagination
        // width to include page info label manually.
        pg.setPrefWidth(1200);

        pg.setPageFactory(index -> {
            var label = new Label("Page #" + (index + 1));
            label.setStyle("-fx-font-size: 1em;");
            return new BorderPane(label);
        });

        var topRadio = new RadioButton("Top");
        topRadio.setUserData("-fx-page-information-alignment:top;");

        var rightRadio = new RadioButton("Right");
        rightRadio.setUserData("-fx-page-information-alignment:right;");

        var bottomRadio = new RadioButton("Bottom");
        bottomRadio.setUserData("-fx-page-information-alignment:bottom;");
        bottomRadio.setSelected(true);

        var leftRadio = new RadioButton("Left");
        leftRadio.setUserData("-fx-page-information-alignment:left;");

        var sideGroup = new ToggleGroup();
        sideGroup.getToggles().setAll(
                topRadio, rightRadio, bottomRadio, leftRadio
        );
        sideGroup.selectedToggleProperty().addListener(
                (obs, old, val) -> pg.setStyle(String.valueOf(val.getUserData()))
        );

        var toggleBox = new VBox(
                10, topRadio, rightRadio, bottomRadio, leftRadio
        );

        var box = new HBox(10, pg, toggleBox);
        box.setAlignment(Pos.CENTER);

        return box;
    }


    //------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
    private ComboBox custom_combo() {
        record Badge(String text, Ikon icon) {
        }

        class BadgeCell extends ListCell<Badge> {

            @Override
            protected void updateItem(Badge badge, boolean isEmpty) {
                super.updateItem(badge, isEmpty);

                if (isEmpty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(new FontIcon(badge.icon()));
                    setText(badge.text());
                }
            }
        }

//        Faker FAKER = new Faker();
        var items = IntStream.range(0, 5).boxed()
                .map(i -> new Badge(/*FAKER.hipster().word()*/ "Item: " + i, randomIcon()))
                .collect(Collectors.toCollection(
                        FXCollections::observableArrayList
                ));

        var cmb = new ComboBox<Badge>(items);
        cmb.setPrefWidth(160);
//        cmb.setPlaceholder(new Label("Loading...")); // kada cekamo podatke
        cmb.setButtonCell(new BadgeCell());
        cmb.setCellFactory(c -> new BadgeCell());
        cmb.getSelectionModel().selectFirst();

        return cmb;
    }


    private Ikon randomIcon() {
        /*
        for (Feather c : Feather. values())
            System. out. println(c.getCode());
        // 57344 - 57629
 */
        Random rnd = new Random();
        return Feather.values()[rnd.nextInt(Feather.values().length)];
    }


    //------------------------------------------------------------------
    // PRIKAZ PROIZVOLJAN - IZBEGAVATI ZA MOBILE - KORISTITI CARD ( DIALOG_CARD ) UMESTO
    private VBox show_dialog() {
        VBox box = new VBox();

        var button = new Button(
                "Dialog", new FontIcon(Feather.CHECK_SQUARE)
        );
        button.setOnAction(e -> {
            var alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null/*"Header"*/);    // null ako ne zelimo da ima header
            alert.setContentText("Content");
//            alert.setWidth(dimension.getWidth() * 0.9);

            ButtonType yesBtn = new ButtonType("Yes", ButtonData.YES);
            ButtonType noBtn = new ButtonType("No", ButtonData.NO);
            ButtonType cancelBtn = new ButtonType(
                    "Cancel", ButtonData.CANCEL_CLOSE
            );

            alert.getButtonTypes().setAll(yesBtn, noBtn, cancelBtn);
            alert.initOwner(stage_copy);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get().getButtonData() == ButtonData.YES) {
                String style_danger = Styles.DANGER;
                System.out.println("OK Pressed");
                button.getStyleClass().removeAll(style_danger);  // uklanjamo sve danger style ako ga ima
                button.getStyleClass().add(Styles.SUCCESS);
            } else {
                String style_success = Styles.SUCCESS;
                System.out.println("OK not Pressed: " + result.get());
                button.getStyleClass().removeAll(style_success); // uklanjamo sve sucess style ako ga ima
                button.getStyleClass().add(Styles.DANGER);
            }
        });
        box.getChildren().add(button);
        box.setAlignment(Pos.CENTER);
        return box;
    }


    //---------------------------------------------------------
    private VBox tile_card_dialog() {
        var card_dialog = new VBox();
        card_dialog.setPrefWidth(System_Info.dimension.getWidth());

//        leftDialog.setHeight(-1);
        var back_btn = new Button(null, new FontIcon(Feather.ARROW_UP));
        back_btn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.LARGE
        );
        back_btn.setOnAction(evt -> {
            modalPane.hide(true);
        });
        card_dialog.getChildren().add(back_btn);//.getDialogPane().setContent(lbl);
        card_dialog.setPadding(new Insets(10, 10, 20, 10));
        card_dialog.setSpacing(20);
        var scrolling_box = new VBox(Tile_Card_dialog.tile_example(), Tile_Card_dialog.card_example());
        scrolling_box.setPadding(new Insets(0, 0, 40, 0));
        scrolling_box.setSpacing(20);
        var gridScroll = new ScrollPane(scrolling_box);            // IMPORTANT - Use ScrollPane za nov VBox ispod button
        gridScroll.setFitToWidth(true);
//        gridScroll.setFitToHeight(true);
        card_dialog.getChildren().addAll(gridScroll);
        card_dialog.setAlignment(Pos.TOP_CENTER);
        if (theme.isDarkMode()) {
            card_dialog.setBackground(new Background(new BackgroundFill(Color.rgb(20, 20, 20, 1), null, null)));
        } else {
            card_dialog.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235, 1), null, null)));
        }

        return card_dialog;
    }


    //---------------------------------------------------------
    private VBox chart_dialog() {
        var chart_dialog = new VBox();
        chart_dialog.setPrefWidth(System_Info.dimension.getWidth());

//        leftDialog.setHeight(-1);
        var back_btn = new Button(null, new FontIcon(Feather.ARROW_DOWN));
        back_btn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.LARGE
        );
        back_btn.setOnAction(evt -> {
            modalPane.hide(true);
            Chart_samples.timeline.stop();
        });
        chart_dialog.getChildren().add(back_btn);//.getDialogPane().setContent(lbl);
        chart_dialog.setPadding(new Insets(10, 0, 20, 0));
        chart_dialog.setSpacing(20);
        var scrolling_box = new VBox(Tile_Card_dialog.tile_example(), Chart_samples.Updating_2(), Chart_samples.areaChart(),
                Chart_samples.stackedAreaChart(), Chart_samples.barChart(), Chart_samples.lineChart(), Chart_samples.bubbleChart(),
                Chart_samples.pieChart(), Chart_samples.scatterChart());
        scrolling_box.setPadding(new Insets(0, 0, 40, 0));
        scrolling_box.setSpacing(20);
        var gridScroll = new ScrollPane(scrolling_box);            // IMPORTANT - Use ScrollPane za nov unutrasnji VBox ispod button
        gridScroll.setFitToWidth(true);
//        gridScroll.setFitToHeight(true);
        chart_dialog.getChildren().add(gridScroll);
        chart_dialog.setAlignment(Pos.TOP_CENTER);
        if (theme.isDarkMode()) {
            chart_dialog.setBackground(new Background(new BackgroundFill(Color.rgb(20, 20, 20, 1), null, null)));
        } else {
            chart_dialog.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235, 1), null, null)));
        }

        return chart_dialog;
    }


    //------------------------------------------------------------------
    private VBox create_left_dialog() {
        var leftDialog_1 = new VBox();
        leftDialog_1.setMaxWidth(System_Info.dimension.getWidth() / 2);
        if (theme.isDarkMode()) {
            leftDialog_1.setBackground(new Background(new BackgroundFill(Color.rgb(20, 20, 20, 1), null, null)));
        } else {
            leftDialog_1.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235, 1), null, null)));
        }
        //        leftDialog_1.setHeight(-1);
        var btn = new Button("Close");
        btn.setOnAction(evt -> {
            modalPane.hide(true);
        });
        leftDialog_1.getChildren().add(Left_dialog.toggle_switch());
        leftDialog_1.getChildren().add(Left_dialog.input_group());
        leftDialog_1.getChildren().add(Left_dialog.menu_button());
        leftDialog_1.getChildren().add(Left_dialog.spinner());
        leftDialog_1.getChildren().add(Left_dialog.create_MaskTextField());
        leftDialog_1.getChildren().add(Left_dialog.create_progress());
        leftDialog_1.getChildren().add(btn);//.getDialogPane().setContent(lbl);
        leftDialog_1.setSpacing(20);
        leftDialog_1.setAlignment(Pos.CENTER);
        //       leftDialog_1.getChildren().setAll(new Label("Modal Pane"));

        return leftDialog_1;
    }


    //------------------------------------------------------------------
    private BorderPane make_settings_dialog() {
        var root = new BorderPane();
        root.setPrefWidth(System_Info.dimension.getWidth());

//        leftDialog.setHeight(-1);
        var back_btn = new Button(null, new FontIcon(Feather.ARROW_RIGHT));
        back_btn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.BUTTON_CIRCLE, Styles.LARGE
        );
        back_btn.setOnAction(evt -> {
            modalPane.hide(true);
        });

        var app_top_bar = new HBox(back_btn);
        app_top_bar.setPrefWidth(System_Info.dimension.getWidth());
        app_top_bar.setAlignment(Pos.CENTER);

        var vBox = new VBox(/*passwordSample()*/Settings_dialog.Text_Area(), Settings_dialog.passwordSample_2(), Settings_dialog.calendarExample());
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 0, 0, 0));
        vBox.setAlignment(Pos.CENTER);

        root.setTop(app_top_bar);
        BorderPane.setAlignment(app_top_bar, Pos.TOP_CENTER);
        root.setCenter(vBox);
        BorderPane.setAlignment(vBox, Pos.CENTER);
        root.setPadding(new Insets(10, 0, 0, 0)); // za ceo view - border pane
        if (theme.isDarkMode()) {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(20, 20, 20, 1), null, null)));
        } else {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235, 1), null, null)));
        }

        return root;
    }


    //------------------------------------------------------------------
    private BorderPane make_settings_dialog_2() {
        var root = new BorderPane();
        root.setPrefWidth(System_Info.dimension.getWidth());

//        leftDialog.setHeight(-1);
        var back_btn = new Button(null, new FontIcon(Feather.ARROW_RIGHT));
        back_btn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.BUTTON_CIRCLE, Styles.LARGE
        );
        back_btn.setOnAction(evt -> {
            modalPane.hide(true);
        });

        var app_top_bar = new HBox(back_btn);
        app_top_bar.setPrefWidth(System_Info.dimension.getWidth());
        app_top_bar.setAlignment(Pos.CENTER);

        var vBox = new VBox(Settings_dialog_2.Message_Example(), Settings_dialog_2.Notification_popup(null),
                Settings_dialog_2.Accordion_box(), Settings_dialog_2.gridScrollExample());
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);

        root.setTop(app_top_bar);
        BorderPane.setAlignment(app_top_bar, Pos.TOP_CENTER);
        root.setCenter(vBox);
        BorderPane.setAlignment(vBox, Pos.CENTER);
        root.setPadding(new Insets(10, 0, 0, 0));
        if (theme.isDarkMode()) {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(20, 20, 20, 1), null, null)));
        } else {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235, 1), null, null)));
        }

        return root;
    }


    //------------------------------------------------------------------
    private BorderPane make_html_dialog() {
        var root = new BorderPane();
        root.setPrefWidth(System_Info.dimension.getWidth());

//        leftDialog.setHeight(-1);
        var back_btn = new Button(null, new FontIcon(Feather.ARROW_LEFT));
        back_btn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.BUTTON_CIRCLE, Styles.LARGE
        );

        back_btn.setOnAction(evt -> {
            modalPane.hide(true);
        });

        var app_top_bar = new HBox(back_btn);
        app_top_bar.setPrefWidth(System_Info.dimension.getWidth());
        app_top_bar.setAlignment(Pos.CENTER);

        var vBox = Html_dialog.article();
        vBox.setStyle("-fx-font-size: 10pt;");
        vBox.setPadding(new Insets(10, 10, 0, 10));
        vBox.setAlignment(Pos.CENTER);

        root.setTop(app_top_bar);
        BorderPane.setAlignment(app_top_bar, Pos.TOP_CENTER);
        root.setCenter(vBox);
        BorderPane.setAlignment(vBox, Pos.CENTER);
        root.setPadding(new Insets(10, 0, 0, 0));
        if (theme.isDarkMode()) {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(20, 20, 20, 1), null, null)));
        } else {
            root.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235, 1), null, null)));
        }

        return root;
    }


    //------------------------------------------------------------------
    private long asMB(long bytes) {
        return bytes / 1024 / 1024;
    }


    private void mem_usage_timer() {
        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE); // repeat over and over again ( samo 1. ako se ne definise )
        var runtime = Runtime.getRuntime();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), evt -> {
            var cpus = runtime.availableProcessors();
            var max_mem = asMB(runtime.maxMemory());
            var total = asMB(runtime.totalMemory());
            var free_mem = asMB(runtime.freeMemory());
            // format 0-what to pad , 4 ako je ispod 4 cifre pad 0, .1 jedna decimala , %% dodati samo % znak
//            cpu_label.setText(String.format("CPU: %d ( %04.1f %% )",cpus, System_Info.getCPU()));   // leading zero
            if (System_Info.platform.equals("DESKTOP")) {
//                System.out.println("JNA Version::" + com.sun.jna.Native.VERSION);
                cpu_label.setText(String.format("CPU: %d ( %.1f %% )", cpus, System_Info.getCPU()));   // No leading zero - koristeci oshi
            } else {
// java.nio.file.AccessDeniedException: /sys/devices/system/cpu/memlat/c4_memlat/cpu4-cpu-l3-lat
//                cpu_label.setText(String.format("CPU: %d ( %.1f %% )", cpus, System_Info.getCPU()));         // koristeci oshi
//                cpu_label.setText(String.format("CPU: %d ( %.1f %% )", cpus, System_Info.getCPU_Android())); // koristeci top
                cpu_label.setText(String.format("CPU: %d", cpus));   // No leading zero                        // bez cpu load-a
            }
            memory_taxt.set("Memory usage: " + (total - free_mem) + " MB");
//            System.out.println("Max mem for java: " + max_mem);
            /*
            var mb = ManagementFactory.getOperatingSystemMXBean();
            var avarage_running_process = mb.getSystemLoadAverage();  // system wide processes
            System.out.println("running_process: " + avarage_running_process);
             */
//                timeline.stop();
        }));
        timeline.play();
    }


    //------------------------------------------------------------------
    public static void main(String[] args) {
        launch();
    }
}