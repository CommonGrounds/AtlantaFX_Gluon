package com.mycompany.atlantafx_gluon;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import atlantafx.base.util.BBCodeParser;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import java.util.Objects;

public class Tile_Card_dialog {

    static Card card1;
    static TextFlow text_flow;
    public static final String orig_string = "Card Body with some text \uD83D\uDC31 - " +
            "koji bi trebao da se automatski formatira sa -fx-wrap-text: true; u .css-u";

    @SuppressWarnings("never used")
    public static Tile tile_example() {
        var url = "https://wikipedia.org/wiki/The_Elder_Scrolls_III:_Morrowind";
        var quote = "Ovo je description sektor"/*FAKER.elderScrolls().quote()*/
                + " \n[url=" + url + "]Learn more[/url]";

        /*
                 Title
     Graphic  description    Action
         */
//        var tile = new Tile(FAKER.name().fullName(), quote);
        var tile = new Tile();
        tile.setTitle("Tile primer");
//        tile.getStyleClass().add(Styles.LARGE);   // ne radi za ceo Tile samo za pojedinacne el.
//        tile.setBorder(Border.stroke(Paint.valueOf(String.valueOf(Color.BLUE))));
        tile.setDescription(quote);

        var img = new ImageView(new Image(
                Objects.requireNonNull(App.class.getResource(App.ASSETS_DIR + "images/avatars/avatar1.png")).toExternalForm()));
        var img2 = new ImageView(new Image(
                Objects.requireNonNull(App.class.getResource(App.ASSETS_DIR + "images/avatars/avatar2.png")).toExternalForm()));
        img.setFitWidth(64);
        img.setFitHeight(64);
        tile.setGraphic(img);

        var tile_btn = new Button(null, new FontIcon(Feather.REFRESH_CW/*Material2OutlinedAL.DELETE*/));
        tile_btn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.FLAT);
        tile.setAction(tile_btn);
//        tile.setActionHandler(tile_btn::fire);  // Akcija za ceo tile - ovde klik na tile pokrece button action
        // kada ima vise akcija za ist tile
        tile.addEventFilter(ActionEvent.ACTION, e -> {
            if (e.getTarget() instanceof Hyperlink link) {
//                BrowseEvent.fire((String) link.getUserData());
                System.out.println(link.getUserData().toString());
                if(System_Info.net_available()){
                    Browser.launch(link.getUserData().toString());
//                        if(Chart_samples.timeline != null) Chart_samples.timeline.stop(); // zaustaviti animaciju kad otvaramo drugi program
                }else{
                    if(System_Info.platform.equals("DESKTOP")){      // Zato sto na destop-u attach ConnectivityService uvek daje false
                        Browser.launch(link.getUserData().toString());
//                        if(Chart_samples.timeline != null) Chart_samples.timeline.stop();
                    }else{
                        App.modalPane.hide(true);  // 1. mora modal hide
                        if(Chart_samples.timeline != null) Chart_samples.timeline.stop();
                        System_Info.show_notification("Net is not available",Styles.DANGER); // pa onda notifikacija na main screen
                        System.out.println("Net is not available");
                    }
                }
            }
            if (e.getTarget() instanceof Button btn) {
                btn.getStyleClass().add(Styles.SUCCESS);
                tile.setGraphic(img2);
//                tile.setBorder(Border.stroke(Paint.valueOf("#ccf5d2")));
//                tile.setBackground(new Background(new BackgroundFill(Color.rgb(23, 48, 27, 1), null, null)));  // morala bi ovako da se postavi style za card i tile
                System.out.println(e.getEventType().getName());
            }
            e.consume();
        });
/*
        // kada ne treba filter ( samo jedna akcija )
        tile_btn.setOnAction(evt -> {
                tile.setGraphic(img2);
                System.out.println(evt.getEventType().getName());
        });
 */
        return tile;
    }


    //---------------------------------------------------------
    public static VBox card_example() {
        /*
        header
        subheader
        body
        footer
         */
        var tweetCard = new Card();
        tweetCard.setMinWidth(300);
        tweetCard.setMaxWidth(400);

        var title = new Label("Card Header");
        title.getStyleClass().add(Styles.TITLE_4);
        tweetCard.setHeader(title);

        var text = BBCodeParser.createFormattedText("[color=-color-success-emphasis]Body[/color] JavaFX is a Java library used to build " +
                "Rich Internet Applications."); // textFlow se vraca
        text.setMaxWidth(260);
        tweetCard.setBody(text);

        var footer = new HBox(10,
                new FontIcon(Material2MZ.THUMB_UP),
                new Label("861"),
                new Spacer(10),
                new FontIcon(Material2MZ.THUMB_DOWN),
                new Label("92")
        );
        footer.setAlignment(Pos.CENTER_LEFT);
        tweetCard.setFooter(footer);
//        tweetCard.getStyleClass().add(Styles.DANGER);   // ne radi za card

        // ------------------------------------------
        var dialogCard = new Card();
        dialogCard.setHeader(new Tile(
                "Dialog in Card",
                " description - Are you sure. "
                        + "You can access this file for 7 days in your trash."
        ));
        dialogCard.setBody(new CheckBox("Body - Do not show it anymore"));

        var confirmBtn = new Button("Confirm");
        confirmBtn.setDefaultButton(true);
        confirmBtn.setPrefWidth(100);

        var cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(100);

        var dialogFooter = new HBox(20, confirmBtn, cancelBtn);
        dialogCard.setFooter(dialogFooter);

//-----------------------------------------------------
        card1 = new Card();
        //------------ CARD HEADER - TILE -------------
        var avatar1 = new ImageView(new Image(
                Objects.requireNonNull(App.class.getResource(App.ASSETS_DIR + "images/avatars/avatar3.png")).toExternalForm()));
//        card1.getStyleClass().add(Styles.ELEVATED_2);
        var btn1 = new MenuButton();
        btn1.getStyleClass().addAll(Styles.FLAT, Tweaks.NO_ARROW);    // bez strelice - mora i deo u .css-u
        btn1.setGraphic(new FontIcon(Feather.MORE_VERTICAL));
        var m1 = new MenuItem("Menu 1");
        m1.setOnAction(event -> {
            System.out.println("Menu 1 selected via Lambda: " + event.getEventType());
        });
        var m2 = new MenuItem("Menu 2");
        m2.setOnAction(event -> {
            System.out.println("Menu 2 selected via Lambda");
        });
        btn1.getItems().setAll(m1, m2);
        var header1 = new Tile(
                "Tile in Card ",
                "Tile description in card header",
                avatar1
        );
        header1.setAction(btn1); // kada tile ima samo jednu action ne treba event filter
//---------------- END TILE ------------------
        card1.setHeader(header1);
//------------- CARD SUBHEADER -----------------------
        var tf2 = new CustomTextField();
        tf2.setPromptText("card subheader");
//        tf2.insertText(0,"🐱");
        var btn = new Button("", new FontIcon(Material2MZ.SEARCH));
        btn.getStyleClass().addAll(Styles.BUTTON_ICON/*,Styles.SMALL*/);
        var group = new InputGroup(btn, tf2);
//        tf2.setLeft(new FontIcon(Material2MZ.SEARCH));
//        tf2.setRight(new FontIcon(Feather.X));
//        tf2.getProperties().put("vkType", "text");
//        tf2.getStyleClass().clear();
//        tf2.setStyle(null);
        card1.setSubHeader(group);
//        FXVK.init(tf2);
//        FXVK.attach(tf2);
//------------------- CARD BODY ---------------------
        Text txt = new Text(orig_string);
        text_flow = BBCodeParser.createFormattedText(txt.getText());
        text_flow.setMaxWidth(330); // text se prelama auto na ovu sirinu sa -fx-wrap-text: true; u css-u ( za TextFlow )
        card1.setBody(text_flow);

//---------------- CARD FOOTER -------------------
        var hbox = new HBox(10,
                new FontIcon(Material2AL.FAVORITE),
                new Label("861"),
                new Spacer(20),
                new FontIcon(Material2MZ.SHARE),
                new Label("92")
        );
        var footer2 = new VBox(new Separator(), hbox);
        footer2.setAlignment(Pos.CENTER_LEFT);
        card1.setFooter(footer2);

        // subheader button deluje na card body text
        btn.setOnAction(e -> {
            try{
                int i = orig_string.indexOf(tf2.getText());
                System.out.println("Pocetak: " + i + ", kraj: " + (i + tf2.getText().length()));
//            txt = txt.getText().replaceAll(txt.getText().substring(i),"[color=-color-success-emphasis]")
                String s = orig_string;
                String[] str = new String[10];
                str[0] = s.substring(0, i);
                str[1] = s.substring(i, i + tf2.getText().length());
                str[2] = s.substring(i + tf2.getText().length());
                s = str[0] + "[color=-color-success-emphasis]" + str[1] + "[/color]" + str[2];
                txt.setText(s);
                text_flow = BBCodeParser.createFormattedText(txt.getText());
                text_flow.setMaxWidth(330); // obavezno pre setBody
                card1.setBody(text_flow);
            }catch(Exception exp){
                System.out.println("Search: : " + exp.getMessage());
            }
        });
//-----------------END CARD --------------------
        return new VBox(20, tweetCard, dialogCard, card1);
    }
}
