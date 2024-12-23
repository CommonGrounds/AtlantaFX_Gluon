package com.mycompany.atlantafx_gluon;

import atlantafx.base.controls.Calendar;
import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import com.gluonhq.emoji.Emoji;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import com.gluonhq.emoji.Emoji;
import com.gluonhq.emoji.EmojiData;
import com.mycompany.atlantafx_gluon.popup.EmojiPopup;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Settings_dialog {

    static Font font;

    static {
        try {
//            font = Font.loadFont(App.class.getResource("popup/fonts/TwitterColorEmoji-SVGinOT.ttf").toExternalForm(), 16);
            font = Font.loadFont(Settings_dialog.class.getResource("/fonts/OpenSansEmoji.ttf").toExternalForm(), 20);
            System.out.println("Loaded Font: " + font.getName());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    //------------------------------------------------------------------
    public static Calendar calendarExample() {
        final var style = """
                -fx-border-width: 0 0 0.5 0;
                -fx-border-color: -color-border-default;""";


        class Clock extends VBox {

            static final DateTimeFormatter DATE_FORMATTER =
                    DateTimeFormatter.ofPattern("EEEE, LLLL dd, yyyy");
            static final DateTimeFormatter TIME_FORMATTER =
                    DateTimeFormatter.ofPattern("HH:mm:ss");

            public Clock() {
                var clockLbl = new Label(TIME_FORMATTER.format(
                        LocalTime.now(ZoneId.systemDefault()))
                );
                clockLbl.getStyleClass().add(Styles.TITLE_4);

                var dateLbl = new Label(DATE_FORMATTER.format(
                        LocalDate.now(ZoneId.systemDefault()))
                );

                // -fx-border-width: 0 0 0.5 0;
                // -fx-border-color: -color-border-default;
                setStyle(style);
                setSpacing(10);
                getChildren().setAll(clockLbl, dateLbl);

                var t = new Timeline(new KeyFrame(
                        Duration.seconds(1),
                        e -> {
                            var time = LocalTime.now(ZoneId.systemDefault());
                            clockLbl.setText(TIME_FORMATTER.format(time));
                        }
                ));
                t.setCycleCount(Animation.INDEFINITE);
                t.playFromStart();
            }
        }

        final LocalDate TODAY = LocalDate.now(ZoneId.systemDefault());
        var cal = new Calendar(TODAY);
        cal.setStyle("-fx-font-size: 12pt;");
        cal.setTopNode(new Clock());
        cal.setShowWeekNumbers(true);

        return cal;
    }


    //------------------------------------------------------------------
    // TODO ne radi za android sa show keyboard = true
    public static PasswordTextField passwordSample() {
        //snippet_3:start
        var tf = new PasswordTextField("qwerty");
        tf.setMinWidth(250);
        tf.setMaxWidth(250);
//        tf.setPrefWidth(250);

        var icon = new FontIcon(Feather.EYE_OFF);
        icon.setCursor(Cursor.HAND);
        icon.setOnMouseClicked(e -> {
            icon.setIconCode(tf.getRevealPassword()
                    ? Feather.EYE_OFF : Feather.EYE
            );
            tf.setRevealPassword(!tf.getRevealPassword());
        });
        icon.setOnTouchPressed(e -> {
            icon.setIconCode(tf.getRevealPassword()
                    ? Feather.EYE_OFF : Feather.EYE
            );
            tf.setRevealPassword(!tf.getRevealPassword());
        });
        tf.setRight(icon);

        return tf;
    }


    //------------------------------------------------------------------
    // Alternativno resenje za Android
    public static InputGroup passwordSample_2() {
        // text field to show password as unmasked
        final TextField textField = new TextField("qwerty");
        // Set initial state
        textField.setManaged(false);
        textField.setVisible(false);

        // Actual password field
        final PasswordField passwordField = new PasswordField();
        var icon = new FontIcon(Feather.EYE_OFF);

        // Bind properties. Toggle textField and passwordField
        // visibility and managability properties mutually when checkbox's state is changed.
        // Because we want to display only one component (textField or passwordField)
        // on the scene at a time.
        textField.managedProperty().bind(icon.iconCodeProperty().isEqualTo( Feather.EYE));
        textField.visibleProperty().bind(icon.iconCodeProperty().isEqualTo( Feather.EYE));

        passwordField.managedProperty().bind(icon.iconCodeProperty().isEqualTo( Feather.EYE_OFF));
        passwordField.visibleProperty().bind(icon.iconCodeProperty().isEqualTo( Feather.EYE_OFF));

        // Bind the textField and passwordField text values bidirectionally.
        passwordField.textProperty().bindBidirectional(textField.textProperty());

        var hbox = new HBox(textField, passwordField);
        hbox.setMinWidth(200);
        hbox.setMaxWidth(200);

        var rightBtn = new Button(
                "", icon
        );
        rightBtn.setCursor(Cursor.HAND);
        rightBtn.getStyleClass().addAll(Styles.BUTTON_ICON);
        rightBtn.setOnAction(e -> {
                    icon.setIconCode(icon.getIconCode() == Feather.EYE ? Feather.EYE_OFF : Feather.EYE);
                }
        );
        var group = new InputGroup(hbox, rightBtn);
        HBox.setHgrow(hbox, Priority.ALWAYS);
//        group.setMaxWidth(250);
        group.setAlignment(Pos.CENTER);

        return group;
    }

    //------------------------------------------
    public static VBox Text_Area(){
//        var txt_area = new TextArea("Testing 1 2 3 😷 Chess:♕♔ Faces:😀😃😄");
        // 🍄🐵🐒🐴🐺🍇🍈🍉🍊🍋🍌🍍🎃🎆🎇🌋🗻👓👞👠👢⛑🔔🔄🔃🏁🚩🎌
        var txt_area = new TextArea("Emoji 123 \uD83C\uDF44\uD83D\uDC35\uD83D\uDC12\uD83D\uDC34\uD83D\uDC3A\uD83C\uDF47\uD83C\uDF48\uD83C\uDF49\uD83C\uDF4A" +
                "\uD83C\uDF4B\uD83C\uDF4C\uD83C\uDF4D\uD83C\uDF83\uD83C\uDF86\uD83C\uDF87\uD83C\uDF0B\uD83D\uDDFB\uD83D\uDC53\uD83D\uDC5E\uD83D\uDC60\uD83D\uDC62⛑" +
                "\uD83D\uDD14\uD83D\uDD04\uD83D\uDD03\uD83C\uDFC1\uD83D\uDEA9\uD83C\uDF8C");
        txt_area.setFont(font);
//        System.out.println(txt_area.getStyleClass());
        txt_area.getStyleClass().clear();     // Ne radi dobro sa atlantaFX pa mora 1. clean svih style
//        txt_area.pseudoClassStateChanged(Styles.STATE_ACCENT,true);
        txt_area.getStyleClass().addAll("text-input", "text-area");    // pa onda add standard text-area styles
//       txt_area.lookup(".content").setStyle("-fx-background-color: green;");     // ovako direktno bez dodavanja icega u css ( mora gornji red )
//       txt_area.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18));
        txt_area.setPromptText("promt text");
        txt_area.setWrapText(true);
        txt_area.setMaxSize(300,150);
        txt_area.setMinSize(300,150);
        txt_area.textProperty().addListener((observable, oldValue, newValue) -> {
            //  in javaFX only the FX thread can modify the UI elements
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
//                    System.out.println("old: " + oldValue + "\nnew: " + newValue);
                }
            });
        });

        Button emojiButton = new Button(null, new FontIcon(Feather.SMILE));
        emojiButton.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.BUTTON_ICON);
        emojiButton.getStyleClass().add("emoji-button");
        emojiButton.setOnAction(e -> {
            EmojiPopup emojiPopup = new EmojiPopup();
            emojiPopup.setOnAction(ev -> {
                Emoji emoji = (Emoji) ev.getSource();
//                editor.getActionFactory().insertEmoji(emoji).execute(new ActionEvent());
                txt_area.insertText(txt_area.getLength(),emoji.character());
                System.out.println("U+" + emoji.getUnified());
            });
            emojiPopup.show(emojiButton);
        });
        var box = new VBox(10,txt_area,emojiButton);
        box.setAlignment(Pos.CENTER);

        return box;
    }
}
