package hello.itemservice;

import hello.itemservice.config.JdbcTemplateV1Config;
import hello.itemservice.config.JdbcTemplateV2Config;
import hello.itemservice.config.JdbcTemplateV3Config;
import hello.itemservice.config.MyBatisConfig;
import hello.itemservice.config.V2Config;
import hello.itemservice.repository.ItemRepository;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


@Slf4j
//@Import(MemoryConfig.class)
//@Import(JdbcTemplateV1Config.class)
//@Import(JdbcTemplateV2Config.class)
//@Import(JdbcTemplateV3Config.class)
//@Import(MyBatisConfig.class)
//@Import(JpaConfig.class)
//@Import(SpringDataJpaConfig.class)
@Import(V2Config.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
public class ItemServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ItemServiceApplication.class, args);
  }

  @Bean
  @Profile("local")
  public TestDataInit testDataInit(ItemRepository itemRepository) {
    return new TestDataInit(itemRepository);
  }

  /*
  @Bean
  @Profile("test")
  public DataSource dataSource() {
    log.info("메모리 데이터베이스 초기화");

    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"); //임베디드(메모리) 모드에서 H2 DB 동작
    dataSource.setUsername("sa");
    dataSource.setPassword("");
    return dataSource;
  }
  */
}
