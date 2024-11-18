package com.mycompany.atlantafx_gluon;

import atlantafx.base.controls.MaskTextField;
import atlantafx.base.controls.RingProgressIndicator;
import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import atlantafx.base.util.IntegerStringConverter;
import javafx.concurrent.Task;
import javafx.geometry.HorizontalDirection;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.datafaker.Faker;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.IntStream;

public class Left_dialog {

    static Faker FAKER = new Faker();
    static MaskTextField timeField;
    static InputGroup group;

    //------------------------------------------------------------------
    public static ToggleSwitch toggle_switch() {
        var successToggle = new ToggleSwitch("Enabled");
        successToggle.selectedProperty().addListener((obs, old, val) -> {
                    successToggle.setText(val ? "Enabled" : "Disabled");
                    successToggle.pseudoClassStateChanged(Styles.STATE_SUCCESS, val);
                }
        );
        successToggle.setSelected(true);
        successToggle.pseudoClassStateChanged(Styles.STATE_SUCCESS, true);

        var dangerToggle = new ToggleSwitch("Disabled");
        dangerToggle.selectedProperty().addListener((obs, old, val) -> {
                    dangerToggle.setText(val ? "Enabled" : "Disabled");
                    dangerToggle.pseudoClassStateChanged(Styles.STATE_DANGER, val);
                }
        );
        dangerToggle.setLabelPosition(HorizontalDirection.RIGHT);  // po defaultu je labela desno
        dangerToggle.setSelected(false);

        return dangerToggle;  // or successToggle
    }



    //------------------------------------------------------------------
    public static InputGroup input_group() {
//        String[] s = {"qdsf", "asfd", "pc5w", "4ljk", "Al5b", "u&@W"};
        var leftTfd = new TextField();
//        Faker FAKER = new Faker();
//        Random random = new Random();
        leftTfd.setText(/*s[random.nextInt(6)]*/FAKER.internet().password()); // TODO Reflection (-Pandroid) za datafaker samo ovde radi ???
        HBox.setHgrow(leftTfd, Priority.ALWAYS);

        var rightBtn = new Button(
                "", new FontIcon(Feather.REFRESH_CW)
        );
        rightBtn.getStyleClass().addAll(Styles.BUTTON_ICON);
        rightBtn.setOnAction(
                e -> leftTfd.setText(/*s[random.nextInt(6)]*/FAKER.internet().password())
        );
        var group = new InputGroup(leftTfd, rightBtn);
        group.setMaxWidth(140);

        return group;
    }



    //------------------------------------------------------------------
    // using wildcard ?
    public static Spinner<?> spinner() {
        var sp4 = new Spinner<Integer>(1, 10, 1);
        IntegerStringConverter.createFor(sp4);
        sp4.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        sp4.setPrefWidth(120);
        sp4.setEditable(true);
        sp4.getProperties().put("vkType", "numeric"); // ovo samo kada je fx virtual keyboard , ne za android soft keyboard

        return sp4;
    }


    //-------------------------------------------------------------------------
    // Menu button on left modal pane
    public static MenuButton menu_button() {
        var accentSplitBtn = new MenuButton("");   // SplitMenuButton ne radi dobro pa mora ovako
        accentSplitBtn.getItems().setAll(createItems(5));
        accentSplitBtn.setGraphic(new FontIcon(Feather.MENU));
        accentSplitBtn.getStyleClass().addAll(
                Styles.BUTTON_OUTLINED, Styles.SUCCESS, Tweaks.NO_ARROW   // bez strelice - mora i deo u .css-u
        );
        return accentSplitBtn;
    }


    @SuppressWarnings("SameParameterValue")
    private static  MenuItem[] createItems(int count) {
//        Faker FAKER = new Faker();
        return IntStream.range(0, count)
                .mapToObj(i -> new MenuItem("Item: " + i /*FAKER.babylon5().character()*/))
                .toArray(MenuItem[]::new);

    }



    //------------------------------------------------------
    public static RingProgressIndicator create_progress() {
        var reverseInd = new RingProgressIndicator(0.25, true); // reverse progress
        reverseInd.setMinSize(100, 100);

        var reverseLabel = new Label("25%");
        reverseLabel.getStyleClass().add(Styles.TITLE_4);

        var reverseBtn = new Button(null, new FontIcon(Feather.PLAY));
        reverseBtn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.FLAT
        );
        reverseBtn.disableProperty().bind(
                reverseInd.progressProperty().greaterThan(0.25)
        );
        reverseBtn.setOnAction(evt1 -> {
            var task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    int max = 100;
                    for (int i = 25; i <= max; i++) {
                        Thread.sleep(100);
                        updateProgress(i, max);
                        updateMessage(i + "%");
                    }
                    return null;
                }
            };

            // reset properties, so we can start a new task
            task.setOnSucceeded(evt2 -> {
                reverseInd.progressProperty().unbind();
                reverseLabel.textProperty().unbind();
                reverseInd.setProgress(0.25);
                reverseLabel.setText("25%");
            });

            reverseInd.progressProperty().bind(task.progressProperty());
            reverseLabel.textProperty().bind(task.messageProperty());

            new Thread(task).start();
        });

        var reverseGraphic = new VBox(10, reverseLabel, reverseBtn);
        reverseGraphic.setAlignment(Pos.CENTER);
        reverseInd.setGraphic(reverseGraphic);

        return reverseInd;
    }





    //------------------------------------------------------------------
    public static InputGroup create_MaskTextField() {

        if(timeField == null){ // Sanity check
//            System.out.println("In create_MaskTextField()");
            var timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            timeField = new MaskTextField("29:59");
            timeField.setText(
                    LocalTime.now(ZoneId.systemDefault()).format(timeFormatter)
            );
//        timeField.setLeft(new FontIcon(Material2OutlinedMZ.TIMER));                // ne radi sa keyboard = true
//        timeField.setPrefWidth(100);
            timeField.setMaxWidth(100);
            // TODO - Zameniti listener sa bind
            timeField.textProperty().addListener((obs, old, val) -> {
                if (val != null) {
                    try {
                        LocalTime.parse(val, timeFormatter);
                        timeField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                    } catch (DateTimeParseException e) {
                        timeField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
                    }
                }
            });

            var lbl = new Label("",new FontIcon(Material2OutlinedMZ.TIMER));
            HBox.setHgrow(lbl, Priority.NEVER);
            group = new InputGroup(lbl, timeField);
            group.setMaxWidth(120);
            group.setAlignment(Pos.CENTER);
        }
        return group;
    }
}
