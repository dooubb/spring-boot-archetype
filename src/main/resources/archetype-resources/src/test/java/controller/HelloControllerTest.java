#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;


import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Supplier;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"test"})
@AutoConfigureMockMvc
class HelloControllerTest {

    static GenericContainer<?> redis = new GenericContainer<>(
            DockerImageName.parse("redis:latest")
    )
            .withExposedPorts(6379);

    static MySQLContainer<?> mysql = new MySQLContainer<>(
            DockerImageName.parse("dockerhub.qingcloud.com/doubao/mysql:9.0.1").asCompatibleSubstituteFor("mysql")
    ).withAccessToHost(true)
            .withUsername("root")
            .withExposedPorts(3306)
            .waitingFor(Wait.forListeningPort())
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(
                            new PortBinding(Ports.Binding.bindPort(33064), new ExposedPort(3306))
                    )
            ));




    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) throws SQLException {

        redis.start();
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);

        mysql.start();
        Connection connection = DriverManager.getConnection(mysql.getJdbcUrl(), mysql.getUsername(), mysql.getPassword());
        Statement statement = connection.createStatement() ;
        String createPrimaryDatabaseSQL = "CREATE DATABASE doubao_primary_test";
        String createSlaveDatabaseSQL = "CREATE DATABASE doubao_slave_test";

        statement.executeUpdate(createPrimaryDatabaseSQL);
        statement.executeUpdate(createSlaveDatabaseSQL);

        Supplier<String> primary = () -> mysql.getJdbcUrl().replace("test", "doubao_primary_test");
        Supplier<String> slave = () -> mysql.getJdbcUrl().replace("test", "doubao_slave_test");
        registry.add("spring.datasource.dynamic.datasource.primary.url", primary::get);
        registry.add("spring.datasource.dynamic.datasource.primary.username", mysql::getUsername);
        registry.add("spring.datasource.dynamic.datasource.primary.password", mysql::getPassword);
        registry.add("spring.datasource.dynamic.datasource.slave.url", slave::get);
        registry.add("spring.datasource.dynamic.datasource.slave.username", mysql::getUsername);
        registry.add("spring.datasource.dynamic.datasource.slave.password", mysql::getPassword);
    }

    @Test
    void hello(){
        System.out.println("hello");
    }
}

