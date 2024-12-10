package com.mycompany.atlantafx_gluon;

import atlantafx.base.controls.Message;
import atlantafx.base.controls.Notification;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import net.datafaker.Faker;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class Settings_dialog_2 {

    private static final Tile fireSmokeTile = TileBuilder.create()
            .skinType(Tile.SkinType.FIRE_SMOKE)
            //        .prefSize(300, 300)
            .title("FireSmoke Tile")
            .text("CPU temp")
            .unit("\u00b0C")
            .threshold(40)       // triggers the fire and smoke effect
            .decimals(0)
            .animated(false)   // da bi odmah poceo sa animacijom postaviti na false
            .running(true)      // a running postaviti na true
            .value(41.1)
            .build();

    //------------------------------------------------------------------
    public static Accordion Accordion_box() {
        String[] s = {"Ipsa delectus quaerat nobis iste suscipit. Quidem explicabo beatae aspernatur fugiat. " +
                "Nostrum eos architecto voluptatem impedit quo atque ut.",
                "Sed vel voluptatum sunt expedita culpa. Praesentium harum labore quis. Voluptate nulla deleniti vel soluta. " +
                        "Asperiores eius magnam sint omnis.",
                "Excepturi officia aliquid atque animi aperiam et accusantium. Dolorem consequatur tenetur. Recusandae suscipit eum atque sit nihil labore."};

        AtomicInteger i = new AtomicInteger();
        Supplier<Node> gen = () -> {
            Faker FAKER = new Faker();
            var text = /* FAKER.lorem().paragraph() */ s[i.getAndIncrement()];
            if (i.get() > 2) i.set(0);
            var textFlow = new TextFlow(new Text(text));
            textFlow.setMinHeight(100);
            VBox.setVgrow(textFlow, Priority.ALWAYS);
            return new VBox(textFlow);
        };

        var tp1 = new TitledPane();
        tp1.setText("TitledPane 1");          // header
        var lbl = new Label("861");
        var btn = new Button(null, new FontIcon(Material2AL.FAVORITE));
        btn.getStyleClass().addAll(
                Styles.FLAT, Styles.SMALL
        );
        var share_btn = new Button(null, new FontIcon(Material2MZ.SHARE));
        share_btn.getStyleClass().addAll(
                Styles.FLAT, Styles.SMALL
        );
        var share_lbl = new Label("73");
        btn.setOnAction(evt -> {
            int f = Integer.parseInt(lbl.getText());
            lbl.setText(String.valueOf(++f));
            btn.getStyleClass().add(Styles.DANGER);
        });
        var footer = new HBox(10,
                btn,
                lbl,
                new Spacer(20),
                share_btn,
                share_lbl
        );
        var content = new VBox(25, gen.get(), footer);
        tp1.setContent(content);
        tp1.setCollapsible(true);              // default
//        tp1.setExpanded(false);   // default true ( najsigurnije je deinisati zeljeno stanje odmah )
        tp1.getStyleClass().add(Styles.DENSE);

        var tp2 = new TitledPane("TitledPane 2", gen.get());
        tp2.getStyleClass().add(Styles.DENSE);

        var tp3 = new TitledPane("TileFX TitledPane", fireSmokeTile);
        tp3.getStyleClass().add(Styles.DENSE);

        return new Accordion(tp1, tp2, tp3);
    }


    //------------------------------------------------------------------
    public static ScrollPane gridScrollExample() {
        var grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);
        grid.addColumn(0,
                createRegion(200, 75, "-color-success-emphasis"),
                createRegion(200, 75, "-color-danger-emphasis")
        );
        grid.addColumn(1,
                createRegion(200, 75, "-color-danger-emphasis"),
                createRegion(200, 75, "-color-success-emphasis")
        );

        var gridScroll = new ScrollPane(grid);
//        gridScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);    // bez vertical scroll
        gridScroll.setMaxHeight(100);
        gridScroll.setMaxWidth(300);

        return gridScroll;
    }

    private static Region createRegion(int width, int height, String bg) {
        var r = new Region();
        r.setMinSize(width, height);
        r.setPrefSize(width, height);
        r.setMaxSize(width, height);
        r.setStyle("-fx-background-color:" + bg + ";");
        return r;
    }


    //------------------------------------------------------------------
    public static VBox Message_Example() {
        VBox this_root = new VBox();

        var info = new Message(
                "Info",
                "Message example 0",
                new FontIcon(Material2OutlinedAL.HELP_OUTLINE)
        );
        info.getStyleClass().addAll(Styles.ACCENT, Styles.SMALL);       // .message css je default

        var success = new Message(
                "Success",
                "Message example 1",
                new FontIcon(Material2OutlinedAL.CHECK_CIRCLE_OUTLINE)
        );
        success.getStyleClass().addAll(Styles.SUCCESS, Styles.SMALL);       // .message css je default
        success.setOnClose(e -> Animations.flash(success).playFromStart());       // da bi imali close

        var warning = new Message(
                "Warning",
                "Message example 2",
                new FontIcon(Material2OutlinedMZ.OUTLINED_FLAG)
        );
        warning.getStyleClass().addAll(Styles.WARNING, Styles.SMALL);
        warning.setOnClose(e -> {
            var out = Animations.flash(warning);
            out.setOnFinished(f -> this_root.getChildren().remove(warning));        // actual close
            out.playFromStart();
        });

        var danger = new Message(
                "Danger",
                "Message example 3",
                new FontIcon(Material2OutlinedAL.ERROR_OUTLINE)
        );
        danger.getStyleClass().addAll(Styles.DANGER, Styles.SMALL);       // .message css je default
        danger.setOnClose(e -> Animations.flash(danger).playFromStart());

        this_root.getChildren().addAll(info, success, warning, danger);
//        root.getChildren().remove(warning);
        return this_root;
    }


    //------------------------------------------------------------------
    public static StackPane Notification_popup(StackPane high_root) {

        StackPane root;
        if (high_root != null) {
            root = high_root;
        } else {
            root = new StackPane();
            root.setMaxHeight(50);
            root.setMinHeight(50);
        }

        final var msg = new Notification(
                "Notification example",
                new FontIcon(Material2OutlinedAL.HELP_OUTLINE)
        );
        msg.getStyleClass().addAll(
                Styles.ACCENT, Styles.ELEVATED_1                    // .message css je default
        );
        msg.setPrefHeight(Region.USE_PREF_SIZE);
        msg.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(msg, Pos.TOP_CENTER);
//        StackPane.setMargin(msg, new Insets(10, 10, 0, 0));

        var btn = new Button("Notification");

        msg.setOnClose(e -> {
            var out = Animations.slideOutUp(msg, Duration.millis(250));
            out.setOnFinished(f -> root.getChildren().remove(msg));
            out.playFromStart();
//            modalPane.hide();
        });
        btn.setOnAction(e -> {
            var in = Animations.slideInDown(msg, Duration.millis(250));
            if (!root.getChildren().contains(msg)) {
                root.getChildren().add(msg);
            }
            in.playFromStart();
//            modalPane.setAlignment(Pos.TOP_CENTER);
//            modalPane.usePredefinedTransitionFactories(Side.TOP);
//            modalPane.show(msg);
        });
        root.getChildren().add(btn);

        return root;
    }
}
