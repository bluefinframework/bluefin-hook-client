package cn.saymagic.bluefinhook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class BluefinHookApplication {

    private static final String TEMPLATE_PATH = System.getenv("BLUEFIN_TEMPLATE_PATH");

    private static final Map<String, Object> configDefault = new HashMap<String, Object>() {{
        put("server.address", getLocalHostIP());
        put("server.port", getLocalHostPort());
        put("mail.template.path", TEMPLATE_PATH);
    }};

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BluefinHookApplication.class);
        application.setDefaultProperties(configDefault);
        application.run(args);
    }

    public static String getLocalHostIP() {
        String ip;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (Exception ex) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    public static String getLocalHostPort() {
        return "2557";
    }
}
