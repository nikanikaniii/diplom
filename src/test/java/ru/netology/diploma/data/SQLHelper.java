package ru.netology.diploma.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    @SneakyThrows
    private static Connection getConn() {
        var url = System.getProperty("datasource.url", "jdbc:mysql://localhost:3306/app");
        var user = System.getProperty("datasource.username", "app");
        var password = System.getProperty("datasource.password", "pass");
        return DriverManager.getConnection(url, user, password);
    }

    @SneakyThrows
    public static String getLastPaymentStatus() {
        var codeSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
        try (var conn = getConn()) {
            return runner.query(conn, codeSQL, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static void cleanDatabase() {
        try (var conn = getConn()) {
            runner.execute(conn, "DELETE FROM credit_request_entity;");
            runner.execute(conn, "DELETE FROM order_entity;");
            runner.execute(conn, "DELETE FROM payment_entity;");
        }
    }
}