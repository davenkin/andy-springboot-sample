package davenkin.springboot.web.common.configuration;

import io.mongock.runner.springboot.EnableMongock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoManagedTypes;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.TransactionManager;

@Slf4j
@EnableMongock
@Configuration
public class MongoConfiguration {

    // Make all @Persistent(including @Document and @TypeAlias) annotated class to be managed by Mongo,
    // otherwise only @Document annotated classes will be considered as the default behavior
    @Bean
    MongoManagedTypes mongoManagedTypes(ApplicationContext applicationContext) throws ClassNotFoundException {
        return MongoManagedTypes.fromIterable(new EntityScanner(applicationContext).scan(Persistent.class));
    }

    @Bean
    public TransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTransactionManager(mongoDatabaseFactory);
    }

}
