package me.zw.tdd

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.core.test.TestCase
import io.kotest.extensions.spring.SpringExtension
import io.restassured.RestAssured
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.kotlin.extensions.Given
import io.restassured.module.mockmvc.kotlin.extensions.Then
import io.restassured.module.mockmvc.kotlin.extensions.When
import org.hamcrest.Matchers.equalTo
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL
import org.springframework.test.web.servlet.MockMvc

/**
 * Acceptance test
 *
 * 1. 후기 조회하기 API
 * [ ] 후기의 ID를 요청값으로 받습니다
 * [ ] 요청 ID에 대한 후기를 찾아 응답값으로 내려줍니다. (200 OK)
 * [ ] 응답에는 반드시 본문과 연락처를 포함해야 합니다.
 * [ ] 후기가 존재하지 않는다면 에러를 응답해야 합니다. (404 Not Found)
 *
 * 2. 후기 작성자에게 선물하기 API
 * [ ] 후기의 ID를 요청 값으로 받습니다.
 * [ ] 선물은 후기당 한번만 요청할 수 있습니다.
 * [ ] 선물하기에 성공하면 후기의 현재 상태를 응답합니다. (200 OK)
 * [ ] 선물하기는 API 를 호출하여 수행합니다.
 *
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestConstructor(autowireMode = ALL)
class AcceptanceTest(
    @LocalServerPort
    private val port: Int,

    private val mockMvc: MockMvc,
) : FeatureSpec({

    feature("후기 조회 인수 조건") {

        scenario("성공하여 후기를 응답한다") {
            Given {
                accept(MediaType.APPLICATION_JSON)
            } When {
                get("/reviews/1")
            } Then {
                status(HttpStatus.OK)
                body(
                    "id", equalTo(1),
                    "content", equalTo("재밌어요"),
                    "phoneNumber", equalTo("010-1111-2222")
                )
            }
        }

        scenario("존재하지 않아 에러를 응답한다") {
            Given {
                accept(MediaType.APPLICATION_JSON)
            } When {
                get("/reviews/1000")
            } Then {
                status(HttpStatus.NOT_FOUND)
            }
        }

    }

    feature("선물하기 인수 조건") {

        scenario("성공한다") {
            Given {
                accept(MediaType.APPLICATION_JSON)
            } When {
                put("/reviews/1")
            } Then {
                status(HttpStatus.OK)
                body(
                    "isSent", equalTo(true)
                )
            }
        }

    }

}) {

    override fun beforeEach(testCase: TestCase) {
        RestAssured.port = port
        RestAssuredMockMvc.mockMvc(mockMvc)
        super.beforeEach(testCase)
    }

    override fun extensions() = listOf(SpringExtension)
}