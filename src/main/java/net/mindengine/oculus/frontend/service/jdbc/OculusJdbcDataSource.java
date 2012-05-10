package net.mindengine.oculus.frontend.service.jdbc;

import net.mindengine.oculus.frontend.config.Config;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class OculusJdbcDataSource extends DriverManagerDataSource {

    public OculusJdbcDataSource(String driverClassName, Config config) {
        setDriverClassName(driverClassName);
        setUrl("jdbc:mysql://" + config.getDbHost() + ":" + config.getDbPort() + "/" + config.getDbScheme());
        setUsername(config.getDbUsername());
        setPassword(config.getDbPassword());
    }
}
