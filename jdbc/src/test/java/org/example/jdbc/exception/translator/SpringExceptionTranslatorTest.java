package org.example.jdbc.exception.translator;

import static org.assertj.core.api.Assertions.*;
import static org.example.jdbc.connection.ConnectionConst.PASSWORD;
import static org.example.jdbc.connection.ConnectionConst.URL;
import static org.example.jdbc.connection.ConnectionConst.USERNAME;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

@Slf4j
public class SpringExceptionTranslatorTest {

  DataSource dataSource;

  @BeforeEach
  void init() {
    dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
  }

  @Test
  void sqlExceptionErrorCode() {
    String sql = "select bad grammar";

    try {
      Connection con = dataSource.getConnection();
      PreparedStatement pstmt = con.prepareStatement(sql);
      pstmt.executeQuery();
    } catch (SQLException e) {
      int errorCode = e.getErrorCode();
      assertThat(errorCode).isEqualTo(42122);
      log.info("errorCode={}", errorCode);
      log.info("error", e);
    }
  }

  @Test
  void exceptionTranslator() {
    String sql = "select bad grammar";

    try {
      Connection con = dataSource.getConnection();
      PreparedStatement pstmt = con.prepareStatement(sql);
      pstmt.executeQuery();
    } catch (SQLException e) {
      SQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
      DataAccessException resultEx = exTranslator.translate("select", sql, e);
      log.info("resultEx", resultEx);
      assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
    }
  }
}
