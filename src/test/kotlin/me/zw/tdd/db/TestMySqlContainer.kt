package me.zw.tdd.db

import org.junit.jupiter.api.Order
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName
import javax.annotation.PreDestroy

@Component("testMySqlContainer")
@Order(value = Ordered.HIGHEST_PRECEDENCE)
class TestMySqlContainer : MySQLContainer<Nothing>() {

    companion object {
        @Container
        @JvmStatic
        val CONTAINER: MySQLContainer<*> =
            DockerImageName.parse("mysql/mysql-server:8.0.27")
                .asCompatibleSubstituteFor("mysql")
                .let { MySQLContainer<Nothing>(it) }
                .apply {
                    withDatabaseName(DATABASE_NAME)
                    withUsername(USERNAME)
                    withPassword(PASSWORD)
                    withEnv("MYSQL_USER", USERNAME)
                    withEnv("MYSQL_PASSWORD", PASSWORD)
                    withEnv("MYSQL_ROOT_PASSWORD", PASSWORD)
                    withInitScript("test-container-init-schema.sql")
                    start()
                }

        const val DATABASE_NAME = "tdd"
        const val USERNAME = "root"
        const val PASSWORD = "root"
    }

    @PreDestroy
    fun preDestroy() {
        CONTAINER.stop()
    }
}