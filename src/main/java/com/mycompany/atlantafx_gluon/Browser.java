package com.mycompany.atlantafx_gluon;

import com.gluonhq.attach.browser.BrowserService;

import java.io.IOException;
import java.net.URISyntaxException;

public class Browser {

    public static void launch(String url){
        BrowserService.create().ifPresent(service -> {
            try {
                service.launchExternalBrowser(url);
            } catch (URISyntaxException | IOException e) {
                System.out.println("Browser Exception");
                throw new RuntimeException(e);
            }
        });
    }
}
