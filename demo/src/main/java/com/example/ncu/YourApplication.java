package com.example.ncu;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;



@SpringBootApplication
public class YourApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        testDatabaseConnection();
    }

    private void testDatabaseConnection() {
        try {
            String sql = "SELECT * FROM member";
            java.util.List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            for (Map<String, Object> row : rows) {
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    String column = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(column + ": " + value);
                }
                System.out.println("--------------------------------------");
            }
        } catch (Exception e) {
            System.err.println("Failed to test database connection: " + e.getMessage());
        }
    }

}
