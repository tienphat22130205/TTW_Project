package vn.edu.hcmuaf.fit.project_fruit.dao.auth;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;

public class FacebookAuthHelper {

    private static final String APP_ID = "YOUR_FACEBOOK_APP_ID"; // Thay thế bằng App ID của bạn
    private static final String APP_SECRET = "YOUR_FACEBOOK_APP_SECRET"; // Thay thế bằng App Secret của bạn

    public static User getUser(String accessToken) {
        try {
            FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.VERSION_19_0);
            return facebookClient.fetchObject("me", User.class, Parameter.with("fields", "id,name,email"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
