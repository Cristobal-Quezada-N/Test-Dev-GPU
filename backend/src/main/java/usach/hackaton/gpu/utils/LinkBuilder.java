package usach.hackaton.gpu.utils;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LinkBuilder {
    private final URI baseAppUrl;

    public LinkBuilder(@Value("${app.url}") String hostAppUrl) {
        if (!hostAppUrl.endsWith("/")) {
            hostAppUrl = hostAppUrl + "/";
        }

        this.baseAppUrl = URI.create(hostAppUrl + "api/auth/");
    }

    private URI build(String path, String token) {
        return baseAppUrl.resolve(path + token);
    }

    public String buildActivation(String token) {
        return build("activate?token=", token).toString();
    }
}
