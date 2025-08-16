package org.rakuten.config;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class TenantConnectionProvider
        extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl<String> {  // Specify String as tenant identifier type

    @Autowired
    private DataSource dataSource;

    @Override
    protected DataSource selectAnyDataSource() {
        return dataSource;
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return dataSource;  // We use the same datasource but switch schema
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        // First get the connection from the underlying provider
        Connection connection = super.getConnection(tenantIdentifier);

        // Then switch to the tenant schema
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET SCHEMA '" + tenantIdentifier + "'");
        }

        return connection;
    }
}