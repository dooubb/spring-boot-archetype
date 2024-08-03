#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
@AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class FlywayConfig {

    @Value("${symbol_dollar}{flyway.db.url}")
    private String dbUrl;
    @Value("${symbol_dollar}{flyway.db.username}")
    private String dbUsername;
    @Value("${symbol_dollar}{flyway.db.password}")
    private String dbPassword;
    @Value("${symbol_dollar}{flyway.db.history.table.name}")
    private String dbHistoryTableName;
    @Value("${symbol_dollar}{spring.flyway.placeholder-replacement}")
    private boolean placeholaderReplacement;
    @Value("${symbol_dollar}{spring.flyway.out-of-order}")
    private boolean outOfOrder;
    @Value("${symbol_dollar}{spring.flyway.baselineOnMigrate}")
    private boolean baselineOnMigrate;

    @PostConstruct
    public void doMigrate() {

        Flyway db = Flyway.configure()
                .dataSource(dbUrl, dbUsername, dbPassword)
                .table(dbHistoryTableName)
                .placeholderReplacement(placeholaderReplacement)
                .outOfOrder(outOfOrder)
                .baselineOnMigrate(baselineOnMigrate)
                .load();
        db.migrate();

    }

}
