package com.mycompany.atlantafx_gluon;

import atlantafx.base.util.BBCodeParser;
import javafx.scene.layout.VBox;

public class Html_dialog {

    public static VBox article() {
        var article = """
                [left][heading=1]JavaFX - Overview[/heading][/left]\

                [b]JavaFX[/b] is a Java library used to build Rich Internet Applications. \
                The applications written using this library can run consistently across multiple \
                platforms. The applications developed using JavaFX can run on various devices \
                such as Desktop Computers, Mobile Phones, TVs, Tablets, etc.

                To develop GUI Applications using [color=-color-accent-emphasis]Java programming language[/color], \
                the programmers rely on libraries such as [s]Advanced Windowing Toolkit[/s] and Swing. \
                After the advent of JavaFX, these Java programmers can now develop \
                [abbr='Graphical User Interface']GUI[/abbr] applications effectively with rich content.
                 
                [heading=3]Key Features[/heading]

                Following are some of the [i]important features[/i] of JavaFX:\
                [ul]
                [li][b]Written in Java[/b] − The JavaFX library is written in Java and is [u]available for \
                the languages that can be executed on a JVM[/u], which include − Java, \
                [url="https://groovy-lang.org/"]Groovy[/url] and JRuby.These JavaFX applications are also \
                platform independent.[/li]\
                [li][b]FXML[/b] − JavaFX features a language known as FXML, which is a HTML like declarative \
                markup language. The sole purpose of this language is to define a user interface.[/li]\
                [li][b]Scene Builder[/b] − JavaFX provides an application named Scene Builder. On integrating \
                this application in IDE’s such [as Eclipse and NetBeans], the users can access a drag \
                and drop design interface, which is used to develop [code]FXML[/code] applications (just like \
                Swing Drag & Drop and DreamWeaver Applications).[/li]\
                [li][b]Built-in UI controls[/b] − JavaFX library caters UI controls using which we can develop a \
                full-featured application.[/li]\
                [li][b]CSS like Styling[/b] − JavaFX provides a CSS like styling. By using this, you can improve \
                the design of your application with a simple knowledge of CSS.[/li]\
                [li][b]Canvas and Printing[/b] − JavaFX provides \
                [label style=-fx-background-color:-color-warning-muted]Canvas[/label], \
                an immediate mode style of rendering API. Within the package [font=monospace]javafx.scene.canvas[/font] \
                it holds a set of classes for canvas, using which we can draw directly within an area of the \
                JavaFX scene. JavaFX also provides classes for Printing purposes in the package javafx.print.[/li]\
                [li][b]Graphics pipeline[/b] − JavaFX supports graphics based on the hardware-accelerated graphics \
                pipeline known as Prism. When used with a supported Graphic Card or GPU it offers smooth \
                graphics. In case the system does not support graphic card then prism defaults to the \
                software rendering stack.[/li]\
                [/ul]\
                [hr/]\
                [right]Source: [url=https://www.tutorialspoint.com/javafx/javafx_overview.htm]tutorialspoint.com\
                [/url][/right]""";

        return BBCodeParser.createLayout(article);              // vraca centriran html text u VBox-u
        //       return new VBox(BBCodeParser.createFormattedText(article));  // html tekst u TextFlow controli
    }
}
