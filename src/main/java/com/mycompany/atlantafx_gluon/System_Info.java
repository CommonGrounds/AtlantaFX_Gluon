package com.mycompany.atlantafx_gluon;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.*;
import atlantafx.base.util.Animations;
import com.gluonhq.attach.device.DeviceService;
import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.connectivity.ConnectivityService;
import com.gluonhq.attach.util.Services;
import com.gluonhq.attach.storage.StorageService;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.converter.InputStreamInputConverter;
import com.gluonhq.connect.converter.JsonInputConverter;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.FileClient;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import com.gluonhq.connect.converter.JsonOutputConverter;
import com.gluonhq.connect.converter.OutputStreamOutputConverter;

import static com.mycompany.atlantafx_gluon.App.theme;

public class System_Info {

    public static Dimension2D dimension = new Dimension2D(800, 600);
    public static String platform = "Android";
    public  static Notification msg;

    public static String javaVersion() {
        return System.getProperty("java.version");
    }
    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }


    //------------------------- used for file connect ---------------------------
    static IntegerProperty theme_number = new SimpleIntegerProperty(0);;
    static GluonObservableObject<User> user;
    static FileClient fileClient;

    public static File ROOT_DIR;
    static {
        try{
            ROOT_DIR = Services.get(StorageService.class)
                    .flatMap(StorageService::getPrivateStorage)
                    .orElseThrow(() -> new RuntimeException("Error retrieving private storage"));
        } catch ( Exception e){                           // Ako pokrecemo sa mvn javafx:run
            String home = System.getProperty("user.home");
            ROOT_DIR = new File( home );         // gluon folder ne postoji ako ga ne kreiramo uz glunfx plugin - samo home as default
        }

    }


    public static void load_settings(){
        System.out.println("ROOT: " + ROOT_DIR);
        try {
            File userFile = new File(ROOT_DIR, "theme.json");
            if (!userFile.exists()) {
                try (FileWriter writer = new FileWriter(userFile)) {
                    writer.write("""
                            {"default_theme":1}
                            """);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // create a FileClient to the specified file
        fileClient = FileClient.create(new File(ROOT_DIR, "theme.json"));

        // create a JSON converter that converts a JSON object into a user object
        InputStreamInputConverter<User> converter = new JsonInputConverter<>(User.class);

        // retrieve an object from an ObjectDataReader created from the FileClient
        user = DataProvider.retrieveObject(fileClient.createObjectDataReader(converter));

        // when the object is initialized, bind its properties to the JavaFX UI controls
        user.initializedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                System.out.println("\ntheme: " + user.get().nameProperty().get());
                setTheme(user.get().nameProperty().get());
            }
        });
    }

    public static void save_settings(){

        System.out.println("theme_number: " + theme_number.get());
        user.get().setThemeNumber(theme_number.get());
        // create a JSON converter that converts the user object into a JSON object
        OutputStreamOutputConverter<User> outputConverter = new JsonOutputConverter<>(User.class);

        // store an object with an ObjectDataWriter created from the FileClient
        DataProvider.storeObject(user.get(), fileClient.createObjectDataWriter(outputConverter));
    }
    //-----------------------------------------------------------------------------

    //-------------------------------------------------------------------
    public static  void setTheme(int i) {
        switch (i) {
            case 0:
                theme = new PrimerLight();
                theme_number.set(0);
                break;
            case 1:
                theme = new PrimerDark();
                theme_number.set(1);
                break;
            case 2:
                theme = new Dracula();
                theme_number.set(2);
                break;
            case 3:
                theme = new CupertinoDark();
                theme_number.set(3);
                break;
            case 4:
                theme = new CupertinoLight();
                theme_number.set(4);
                break;
            case 5:
                theme = new NordLight();
                theme_number.set(5);
                break;
            case 6:
                theme = new NordDark();
                theme_number.set(6);
                break;
            default:
                theme = new CupertinoDark();
                theme_number.set(3);
        }
    }

    //------------------------------------------------------------------
    public static int get_platform(){
        AtomicInteger result = new AtomicInteger();
        DeviceService.create().map(deviceService -> {
                    platform = deviceService.getPlatform();
                    System.out.println("platform: " + platform);
                    result.set(1);
                    return result.get();
                })
                .orElseGet(() -> {
                    platform = "DESKTOP";
                    System.out.println("DeviceService nema - DESKTOP");
                    result.set(0);
                    return result.get();
                });

        return result.get();
    }




    //------------------------------------------------------------------
    public static void get_display(){
        DisplayService displayService;
        displayService = DisplayService.create().orElse(null);
        if (displayService == null) {
            System.out.println("WARNING: Unable to load Gluon Display Service");
        } else {
            dimension = displayService.getDefaultDimensions();
            System.out.println("Gluon Display Service created");
        }
    }

    public static boolean net_available(){
        AtomicBoolean connected = new AtomicBoolean(false);
        ConnectivityService.create().ifPresent(service -> {
            connected.set(service.isConnected());
//            System.out.println("Network connectivity available? " + String.valueOf(connected));
//            service.connectedProperty().addListener((obs, ov, nv) -> {
//            });
        });
        return connected.get();
    }


    // ----------------- MAIN SCREEN NOTIFICATION ----------------------
    public static Notification notification() {

        msg = new Notification(
                "",
                new FontIcon(Material2OutlinedAL.HELP_OUTLINE)
        );
        msg.getStyleClass().addAll(
                Styles.ELEVATED_1 , Styles.ACCENT                   // .message css je default
        );
        msg.setPrefHeight(20/*Region.USE_PREF_SIZE*/);
        msg.setMaxHeight(20/*Region.USE_PREF_SIZE*/);
        StackPane.setAlignment(msg, Pos.TOP_CENTER);              // Vazno
        msg.setVisible(false);

        return msg;
    }

    public static void show_notification(String message,String style){
        if(!message.isEmpty() && msg != null){
            msg.setMessage(message);
            msg.getStyleClass().remove(msg.getStyleClass().size()-1);
            msg.getStyleClass().add(style);
            msg.setVisible(true);
            var in = Animations.slideInDown(msg, Duration.millis(250));
            in.playFromStart();

            msg.setOnClose(e -> {
                var out = Animations.slideOutUp(msg, Duration.millis(250));
                out.setOnFinished(f -> msg.setVisible(false));
                out.playFromStart();
//            modalPane.hide();
            });
        }
    }


    //------------------------------------------------
    static long[] prevTicks = new long[CentralProcessor.TickType.values().length];
    public static double getCPU() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor cpu = hal.getProcessor();

        double cpuLoad = cpu.getSystemCpuLoadBetweenTicks( prevTicks ) * 100;
        prevTicks = cpu.getSystemCpuLoadTicks();
//        System.out.println("cpuLoad : " + cpuLoad);
        return cpuLoad;
    }

    //----------------------------------------------
    public static double getCPU_Android() {
        int cpuLoad = 0;

        try {
            String Result;
            Process p = Runtime.getRuntime().exec("top -n 1");
            java.io.BufferedReader br = new java.io.BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((Result = br.readLine()) !=null){
                // replace "com.example.fs" by your application
                if (Result.contains("com.mycompany")) {
//                    System.out.println(Result);
//                    System.out.println(Arrays.toString(Result.split(" ")));
                    String[] info = Result.trim().replaceAll(" +"," ").split(" ");
//                    System.out.println(info);
                    return Double.valueOf(info[9]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuLoad;
    }
}