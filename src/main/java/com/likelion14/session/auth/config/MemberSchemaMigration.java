package com.likelion14.session.auth.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class MemberSchemaMigration implements ApplicationRunner {

    private final DataSource dataSource;

    public MemberSchemaMigration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        alterEmailColumnToNullable();
    }

    private void alterEmailColumnToNullable() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String databaseProductName = connection.getMetaData().getDatabaseProductName();
            String sql;
            
            if (databaseProductName.equalsIgnoreCase("H2")) {
                sql = "ALTER TABLE members ALTER COLUMN email VARCHAR(255) NULL";
            } else {
                // MySQL, MariaDB
                sql = "ALTER TABLE members MODIFY COLUMN email VARCHAR(255) NULL";
            }
            
            try {
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                // Ignore if table or column doesn't exist yet (Hibernate will create it)
                // or if the syntax is still slightly off for the specific version
            }
        }
    }
}
