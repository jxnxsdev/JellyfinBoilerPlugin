package de.jxnxsdev.jellyfin;

import de.jxnxsdev.jellyfin.responses.AuthenticateByNameResponse;

public class Headers {
    public static class AuthenticateByNameHeaders {
        public static final String x_application = "boiler/1.0.0";
        public static final String accept_charset = "UTF-8, *";
        public static final String accept_encoding = "gzip";
        public static final String user_agent = "Boiler/1.0.0";
        public static final String content_type = "application/json";
        public static final String accept = "application/json";
        public static final String x_emby_authorization = "MediaBrowser Client=boiler, Device=boiler, DeviceId=boiler-1.0.0, Version=1.0.0";
    }


    public static class UserViewsHeaders {
        public static final String user_agent = "Boiler/1.0.0";
        public static final String accept = "application/json";
        public static String create_x_emby_authorization(AuthenticateByNameResponse authenticateByNameResponse) {
            return "MediaBrowser Client=boiler, Device=boiler, DeviceId=boiler-1.0.0, Version=1.0.0, Token=" + authenticateByNameResponse.accessToken;
        }
    }
}
