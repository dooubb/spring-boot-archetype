#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableEncryptableProperties
public class DoubaoServiceApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(DoubaoServiceApplication.class, args);
    }
}
