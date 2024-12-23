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
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

import com.gluonhq.emoji.Emoji;
import com.gluonhq.emoji.EmojiData;
import com.gluonhq.emoji.util.TextUtils;

import org.controlsfx.control.Rating;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Tile_Card_dialog {

    static List<Node> image_nodes;
    static List<Node> text_nodes;

    static Card card1;
    static TextFlow text_flow;
    public static final String orig_string = "Card Body with emoji mouse image and text - " +
            "koji bi trebao cat da se automatski formatira wave sa -fx-wrap-text: true; bicyclist u .css-u";
    static ImageView img = new ImageView(new Image(
            Objects.requireNonNull(App.class.getResource(App.ASSETS_DIR + "images/avatars/avatar1.png")).toExternalForm()));
    static ImageView img2 = new ImageView(new Image(
            Objects.requireNonNull(App.class.getResource(App.ASSETS_DIR + "images/avatars/avatar2.png")).toExternalForm()));

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
        tile.setDescription(quote); // IMPORTANT - Auto BBCode parser

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

        var text = BBCodeParser.createLayout( """
                [color=-color-success-emphasis]Body[/color] x[sup][small]2[/small][/sup] , H[sub][small]2[/small][/sub]O, [s]crossed[/s] \
                [u]under[/u] [size=small]size[/size] [email]dev@bbcode.org[/email] [code]code[/code] \
                [label style='-fx-background-color:-color-success-muted']Background[/label][indent]Indent[/indent][indent=3]Indent=3[/indent]\
                [hr/]\
                [hr=5/]\
                [left]left[/left][center]center[/center][right]right[/right]\
                """); // textFlow se vraca - [size=8] moze u px, [hr/] i right,center moze samo kao VBox ( createLayout )
        // table,quote br ( \n radi ) ne rade sa atlantaFX za sada
        // znak \ se dodaje na kraju reda da se izbegne dodatni razmak izmedju donjeg reda ili u istom redu da se doda ili smanji razmak izmadju 2 reci ako moramo da
        // formatiramo text u vise redova code-a
        text.setMaxWidth(260);
        tweetCard.setBody(text);

        var rating = new Rating(5,3);
        rating.setPartialRating(true);
//        rating.setScaleY(0.5);
//        rating.setScaleX(0.5);       // Podesavanje u .css-u
        rating.setOrientation(Orientation.HORIZONTAL);
        var btn_up = new Button(null, new FontIcon(Material2MZ.THUMB_UP));
        btn_up.getStyleClass().addAll(Styles.FLAT, Styles.SMALL,Styles.SUCCESS);
        var btn_down = new Button(null, new FontIcon(Material2MZ.THUMB_DOWN));
        btn_down.getStyleClass().addAll(Styles.FLAT, Styles.SMALL,Styles.DANGER);
        var lbl_up = new Label("5");
        var lbl_down = new Label("3");
        btn_up.setOnAction(evt -> {
            int f = Integer.parseInt(lbl_up.getText());
            int d = Integer.parseInt(lbl_down.getText());
            f++;
            int sum = d + f;
            lbl_up.setText(String.valueOf(f));
            rating.setRating((double)rating.getMax() / ((double)sum/(double)f));
        });
        btn_down.setOnAction(evt -> {
            int f = Integer.parseInt(lbl_up.getText());
            int d = Integer.parseInt(lbl_down.getText());
            d++;
            int sum = d + f;
            lbl_down.setText(String.valueOf(d));
            rating.setRating((double)rating.getMax() / ((double)sum/(double)f));
        });
        int f = Integer.parseInt(lbl_up.getText());
        int d = Integer.parseInt(lbl_down.getText());
        int sum = d + f;
        rating.setRating((double)rating.getMax() / ((double)sum/(double)f));

        var left_box = new HBox(
                btn_up,
                lbl_up,
                new Spacer(10),
                btn_down,
                lbl_down
        );
        left_box.setAlignment(Pos.CENTER_LEFT);
        left_box.setPrefWidth(System_Info.dimension.getWidth()/2);
        var rating_box = new HBox(rating);
        rating_box.setAlignment(Pos.CENTER_RIGHT);
        rating_box.setPrefWidth(System_Info.dimension.getWidth()/2);
//        rating_box.setMinHeight(left_box.getHeight()+16);

        var footer = new HBox(
                left_box,
                rating_box
        );
        footer.setAlignment(Pos.BASELINE_LEFT);
        tweetCard.setFooter(footer);
//        tweetCard.getStyleClass().add(Styles.DANGER);   // ne radi za card

        // ------------------------------------------
        var dialogCard = new Card();
        dialogCard.setHeader(new Tile(
                "Dialog in Card",
                "description - Support BBCode\n\n"
                        + """
                        [ul]
                        [li][color=yellow]first[/color][/li]
                        [li][color=red]second[/color][/li]
                        [li][color=green]third[/color][/li]
                       [/ul]
                       """
        ));
        dialogCard.setBody(new CheckBox("Body - Do not show it anymore"));

        var confirmBtn = new Button("Confirm");
        confirmBtn.setDefaultButton(true);
        confirmBtn.setPrefWidth(100);

        var cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(100);

        var dialogFooter = new HBox(20, confirmBtn, cancelBtn);
        dialogCard.setFooter(dialogFooter);

//-------------------------TILE IN CARD ----------------------------
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
//        HBox body_box = new HBox(create_emoji_image("\uD83D\uDC31"),text_flow);      // create single ImageView from emoji text
        var body_box = create_image_text_flow();
        card1.setBody(body_box);                                                       // emoji + TextFlow
//        card1.setBody(text_flow);                                                    // samo TextFlow

//---------------- CARD FOOTER -------------------
        var hbox = new HBox(10,
                new FontIcon(Material2AL.FAVORITE),
                new Label("861"),
                new Spacer(20),
                new FontIcon(Material2MZ.SHARE),
                new Label("92")
        );
        var footer2 = new VBox(hbox , new Separator());
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
                System.out.println("Search: " + exp.getMessage());
            }
        });
//-----------------END CARD --------------------
        return new VBox(20, tweetCard, dialogCard, card1);
    }


    //-----------------------------------
    // Vraca samo jedan ImageView from string ( vawe , cat , etc )
    private static Node create_emoji_image(String word){
        StringBuilder unicodeText = new StringBuilder();
        Optional<Emoji> optionalEmoji = EmojiData.emojiFromShortName(word);
        unicodeText.append(optionalEmoji.isPresent() ? optionalEmoji.get().character() : word);
        unicodeText.append(" ");
        List<Node> nodes = TextUtils.convertToTextAndImageNodes(unicodeText.toString());
        if(nodes.get(0) instanceof ImageView){
            System.out.println("We have emoji");
            return nodes.get(0);
        }else{
            System.out.println("No emoji");
            return null;
        }
    }


    private static String createUnicodeText(String nv) {
        StringBuilder unicodeText = new StringBuilder();
        String[] words = nv.split(" ");
        System.out.println("words size: " + words.length);
        for (String word : words) {
            Optional<Emoji> optionalEmoji = EmojiData.emojiFromShortName(word);
            unicodeText.append(optionalEmoji.isPresent() ? optionalEmoji.get().character() : word);
            unicodeText.append(" ");
        }
        return unicodeText.toString();
    }


    private static FlowPane create_image_text_flow(){
        String unicodeText = createUnicodeText(orig_string);
        List<Node> nodes = TextUtils.convertToTextAndImageNodes(unicodeText);

        for (int i =0;i<nodes.size();i++) {
            if(nodes.get(i) instanceof Text){
                TextFlow flow = new TextFlow((Text)nodes.get(i));
                nodes.remove(nodes.get(i));
                nodes.add(i,flow);
            }
        }

        FlowPane flowPane = new FlowPane();
        flowPane.setPrefWrapLength(330);
        flowPane.getChildren().setAll(nodes);

        return flowPane;
    }
}
