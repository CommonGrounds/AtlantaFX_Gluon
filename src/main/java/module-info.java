module com.mycompany.atlantafx_gluon {
    requires javafx.controls;
    exports com.mycompany.atlantafx_gluon;
    requires atlantafx.base;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.kordamp.ikonli.feather;
        requires org.kordamp.ikonli.core;
    // add icon pack modules
    requires org.kordamp.ikonli.fontawesome5;
    requires net.datafaker;
    requires com.gluonhq.attach.device;
    requires com.gluonhq.attach.display;
    requires org.kordamp.ikonli.material2;
    requires com.gluonhq.attach.browser;
    requires java.desktop;
    requires com.gluonhq.attach.connectivity;
    requires java.management;
    requires com.github.oshi;
    requires com.gluonhq.attach.storage;
    requires com.gluonhq.attach.util;
    requires com.gluonhq.connect;
    requires com.sun.jna;
}
