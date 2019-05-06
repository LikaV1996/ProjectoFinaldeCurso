/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import pt.solvit.probe.server.config.AppConfiguration;

@Component
public class UriBuilder {

    @Autowired
    private AppConfiguration appConfiguration;
    private static String BASE_PATH;

    @Autowired
    public void setFullUri() {
        try {
            String protocol = "http://";
            UriBuilder.BASE_PATH = protocol + getUri();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Error building full URI.");
        }
    }

    public static URI buildUri(String uriPart, Object... values) {
        return URI.create(buildUriStr(uriPart, values));
    }

    public static String buildUriStr(String uriPart, Object... values) {
        String uri = BASE_PATH + uriPart;
        String[] placeholders = getPlaceholders(new ArrayList<>(), uriPart);

        for (int i = 0; i < values.length; ++i) {
            uri = uri.replace(placeholders[i], values[i].toString());
        }
        return uri;
    }

    private static String[] getPlaceholders(List<String> result, String uriPart) {
        if (!uriPart.contains("{")) {
            return result.toArray(new String[0]);
        }

        int beginPlaceholderIndex = uriPart.indexOf("{");
        String placeholder = uriPart.substring(beginPlaceholderIndex, uriPart.indexOf("}") + 1);
        result.add(placeholder);

        return getPlaceholders(result, uriPart.substring(uriPart.indexOf("}") + 1));
    }

    private String getUri() throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        String port = ":" + appConfiguration.serverPort;

        return hostAddress + port;
    }
}
