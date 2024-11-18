package com.mycompany.atlantafx_gluon;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.chart.*;

import javafx.util.Duration;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class Chart_samples {

    static Faker FAKER = new Faker();
    static Timeline timeline;

    //---------------------------------------------------------
    @SuppressWarnings({"rawtypes"})
    public static LineChart Updating(){

        var x = new NumberAxis(0, 100, 10);
        x.setLabel("Hz");
        var y = new NumberAxis(-1.5,1.5,0.5);
        y.setLabel("Amp");

        LineChart<Number, Number> lc = new LineChart<>(x,y);
        lc.setAnimated(false);   // flickering on axis ako je default - true
        lc.setTitle("Live");
        lc.setMinHeight(300);
        lc.setMaxHeight(300);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Freq");
        lc.getData().add(series);

        new Thread(() -> {
            try {
                for (int i = 0; i < 100; i++) {
                    int finalI = i;
                    Platform.runLater(() -> series.getData().add(new XYChart.Data<>(finalI, Math.cos(Math.toRadians(10.0 * finalI)))));
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }).start();

        return lc;
    }


    //---------------------------------------------------------
    @SuppressWarnings({"rawtypes"})
    public static LineChart Updating_2(){
        AtomicInteger counter = new AtomicInteger();
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Freq");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amp");

        ArrayList <Double> unsortedArray =  new ArrayList<>();

        XYChart.Series<Number, Number> graph = new XYChart.Series<>();
        for (int i = 0; i < 37; i++) {
            unsortedArray.add(Math.cos(Math.toRadians(10.0 * i)));
            graph.getData().add(new XYChart.Data<>(i, unsortedArray.get(i)));
        }
        LineChart<Number, Number> lc = new LineChart<>(xAxis,yAxis);
        lc.setAnimated(false);        // flickering on axis ako je default - true
        lc.setCreateSymbols(false);   // hide dots
        lc.setTitle("Live");
        lc.setMinHeight(300);
        lc.setMaxHeight(300);
        graph.setName("Scope");
        lc.getData().add(graph);

        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(50), event -> {
                    unsortedArray.remove(0);
                    unsortedArray.add(Math.cos(Math.toRadians(10.0 * counter.getAndIncrement())));
                    if(counter.get() >= 37){
                        counter.set(0);
                    }
                    updateGraph(graph,unsortedArray);
    //                Platform.runLater(() -> graph.getData().add(new XYChart.Data<>(graph.getData().size(), Math.cos(Math.toRadians(10.0 * 10)))));
    //                graph.getData().remove(0);
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        return lc;
    }


    //------------------------------------------------------
    public static void updateGraph(XYChart.Series<Number, Number> graph, ArrayList <Double> updatedArray) {
        graph.getData().clear();
        for (int i = 0; i < updatedArray.size(); i++) {
            graph.getData().add(new XYChart.Data<>(i, updatedArray.get(i)));
        }
    }

    //---------------------------------------------------------
    @SuppressWarnings({"unchecked","rawtypes"})
    public static AreaChart areaChart() {
        var x = new NumberAxis(1, 9, 1);
        x.setLabel("Day");

        var y = new NumberAxis();
        y.setLabel("Temp");

        var april = new XYChart.Series<Number, Number>();
        april.setName("April");
        IntStream.range(1, 10).forEach(i -> april.getData().add(
                new XYChart.Data<>(i, FAKER.random().nextInt(15, 30))
        ));

        var may = new XYChart.Series<Number, Number>();
        may.setName("May");
        IntStream.range(1, 10).forEach(i -> may.getData().add(
                new XYChart.Data<>(i, FAKER.random().nextInt(15, 30))
        ));

        var chart = new AreaChart<>(x, y);
        chart.setTitle("Area");
        chart.setMinHeight(300);
        chart.setMaxHeight(300);
        chart.getData().addAll(april, may);

        return chart;
    }


    //-----------------------------------------------------
    @SuppressWarnings("unchecked")
    public static StackedAreaChart<?,?> stackedAreaChart() {
        //snippet_2:start
        var x = new NumberAxis(1, 9, 1);
        x.setLabel("Day");

        var y = new NumberAxis();
        y.setLabel("Temp");

        var april = new XYChart.Series<Number, Number>();
        april.setName("April");
        IntStream.range(1, 10).forEach(i -> april.getData().add(
                new XYChart.Data<>(i, FAKER.random().nextInt(15, 30))
        ));

        var may = new XYChart.Series<Number, Number>();
        may.setName("May");
        IntStream.range(1, 10).forEach(i -> may.getData().add(
                new XYChart.Data<>(i, FAKER.random().nextInt(15, 30))
        ));

        var chart = new StackedAreaChart<>(x, y);
        chart.setTitle("Stacked Area");
        chart.setMinHeight(300);
        chart.getData().addAll(april, may);

        return chart;
    }



    //---------------------------------------------------
    @SuppressWarnings("unchecked")
    public static BarChart<?,?> barChart() {
//        final var rnd = FAKER.random();
        Random rnd = new Random();
        final var countries = new ArrayList<String>(Arrays.stream(new String[]{"GBR", "SRB", "USA"}).toList());

        var x = new CategoryAxis();
        x.setLabel("Country");

        var y = new NumberAxis(0, 80, 10);
        y.setLabel("Value");

        var january = new XYChart.Series<String, Number>();
        january.setName("January");
        IntStream.range(0, countries.size()).forEach(i -> january.getData().add(
                new XYChart.Data<>(countries.get(i), rnd.nextInt(10, 80))
        ));

        var february = new XYChart.Series<String, Number>();
        february.setName("February");
        IntStream.range(0, countries.size()).forEach(i -> february.getData().add(
                new XYChart.Data<>(countries.get(i), rnd.nextInt(10, 80))
        ));

        var march = new XYChart.Series<String, Number>();
        march.setName("March");
        IntStream.range(0, countries.size()).forEach(i -> march.getData().add(
                new XYChart.Data<>(countries.get(i), rnd.nextInt(10, 80))
        ));

        var chart = new BarChart<>(x, y);
        chart.setTitle("Bar Chart");
        chart.setMinHeight(300);
        chart.getData().addAll(january, february, march);
/*
        var s = march.getData().get(1);   // march bar za zemlju broj 2
        var task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int max = 80;
                for (int i = 0; i <= max; i++) {
                    Thread.sleep(200);
                    s.setYValue(i);                   // y value za march bar za zemlju broj 2
                }
                return null;
            }
        };
        new Thread(task).start();
*/
        return chart;
    }



    @SuppressWarnings("unchecked")
    public static BubbleChart bubbleChart() {
        //snippet_5:start
//        final var rnd = FAKER.random();
        Random rnd = new Random();

        var x = new NumberAxis(1, 21, 4);
        x.setLabel("Week");

        var y = new NumberAxis(0, 80, 10);
        y.setLabel("Budget");

        var series1 = new XYChart.Series<Number, Number>();
        series1.setName("Cycling Jersey"/*FAKER.commerce().productName()*/);
        IntStream.range(1, 4).forEach(i -> series1.getData().add(
                new XYChart.Data<>(
                        rnd.nextInt(1, 21),
                        rnd.nextInt(10, 80),
                        rnd.nextDouble(1, 6)
                )
        ));

        var series2 = new XYChart.Series<Number, Number>();
        series2.setName("Cycling Shorts"/*FAKER.commerce().productName()*/);
        IntStream.range(1, 4).forEach(i -> series2.getData().add(
                new XYChart.Data<>(
                        rnd.nextInt(1, 21),
                        rnd.nextInt(10, 80),
                        rnd.nextDouble(1, 6)
                )
        ));

        var chart = new BubbleChart<>(x, y);
        chart.setTitle("Buble Chart");
        chart.setMinHeight(300);
        chart.getData().addAll(series1, series2);

        return chart;
    }

    @SuppressWarnings("unchecked")
    public static LineChart lineChart() {
        //snippet_6:start
//        final var rnd = FAKER.random();
        Random rnd = new Random();

        var x = new CategoryAxis();
        x.setLabel("Month");

        var y = new NumberAxis(0, 80, 10);
        y.setLabel("Value");

        var series1 = new XYChart.Series<String, Number>();
        series1.setName("NDAQ" /*FAKER.stock().nsdqSymbol()*/);
        IntStream.range(1, 6).forEach(i -> series1.getData().add(
                new XYChart.Data<>(
                        Month.of(i).getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        rnd.nextInt(10, 80)
                )
        ));

        var series2 = new XYChart.Series<String, Number>();
        series2.setName("DJI"/*FAKER.stock().nsdqSymbol()*/);
        IntStream.range(1, 6).forEach(i -> series2.getData().add(
                new XYChart.Data<>(
                        Month.of(i).getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        rnd.nextInt(10, 80)
                )
        ));

        var chart = new LineChart<>(x, y);
        chart.setTitle("Line Chart");
        chart.setMinHeight(300);
        chart.getData().addAll(series1, series2);

        return chart;
    }

    public static PieChart pieChart() {
        //snippet_7:start
//        final var rnd = FAKER.random();
        Random rnd = new Random();

        var chart = new PieChart();
        chart.getStylesheets().add("piechart.css");
        chart.getData().addAll(
                new PieChart.Data("Kiwi"/*FAKER.food().fruit()*/, rnd.nextInt(10, 30)),
                new PieChart.Data("Plum" /*FAKER.food().fruit()*/, rnd.nextInt(10, 30)),
                new PieChart.Data("Lime" /*FAKER.food().fruit()*/, rnd.nextInt(10, 30))
        );
        chart.setMinHeight(300);
        chart.setTitle("Pie Chart");

        Platform.runLater(() -> {
/*
            data.get(0).getNode().setStyle("-fx-pie-color: #563429");
            data.get(1).getNode().setStyle("-fx-pie-color: PLUM");
            data.get(2).getNode().setStyle("-fx-pie-color: LIME");
 */
        });

        return chart;
    }



    @SuppressWarnings("unchecked")
    public static ScatterChart scatterChart() {
        //snippet_8:start
//        final var rnd = FAKER.random();
        Random rnd = new Random();

        var x = new NumberAxis(0, 5, 1);
        x.setLabel("Age");

        var y = new NumberAxis(-20, 80, 20);
        y.setLabel("Returns");

        var series1 = new XYChart.Series<Number, Number>();
        series1.setName("Equities");
        IntStream.range(1, 6).forEach(i -> series1.getData().add(
                new XYChart.Data<>(
                        rnd.nextDouble(0, 5),
                        rnd.nextDouble(-20, 80)
                )
        ));

        var series2 = new XYChart.Series<Number, Number>();
        series2.setName("Mutual funds");
        IntStream.range(1, 6).forEach(i -> series2.getData().add(
                new XYChart.Data<>(
                        rnd.nextDouble(0, 5),
                        rnd.nextDouble(-20, 80)
                )
        ));

        var chart = new ScatterChart<>(x, y);
        chart.setTitle("Scatter Chart");
        chart.setMinHeight(300);
        chart.getData().addAll(series1, series2);

        return chart;
    }
}
