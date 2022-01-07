package me.zw.tdd.db

import me.zw.tdd.db.TestMySqlContainer.Companion.CONTAINER
import me.zw.tdd.db.TestMySqlContainer.Companion.DATABASE_NAME
import me.zw.tdd.db.TestMySqlContainer.Companion.PASSWORD
import me.zw.tdd.db.TestMySqlContainer.Companion.USERNAME
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.testcontainers.containers.MySQLContainer
import javax.sql.DataSource

@Configuration
class TestDataSourceConfiguration {

    @DependsOn("testMySqlContainer")
    @Bean
    fun dataSource(): DataSource = DataSourceBuilder.create()
        .driverClassName("com.mysql.cj.jdbc.Driver")
        .url("jdbc:mysql://localhost:${CONTAINER.getMappedPort(MySQLContainer.MYSQL_PORT)}/$DATABASE_NAME")
        .username(USERNAME)
        .password(PASSWORD)
        .build()

}