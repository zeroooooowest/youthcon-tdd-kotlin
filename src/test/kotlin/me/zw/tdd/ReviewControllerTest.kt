package me.zw.tdd

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import me.zw.tdd.domain.Review
import me.zw.tdd.exception.ReviewNotFoundException
import me.zw.tdd.service.ReviewService
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put

@WebMvcTest
@TestConstructor(autowireMode = ALL)
class ReviewControllerTest(
    private val mockMvc: MockMvc,

    @MockkBean
    private val reviewService: ReviewService,
) : DescribeSpec({

    describe("GET /reviews/{id}") {

        context("존재하는 id로 요청하여 성공한다") {
            val id = 1L
            val content = "재밌어요"
            val phoneNumber = "010-1111-2222"
            every { reviewService.getById(id) } returns Review(id, content, phoneNumber)

            val perform = mockMvc.get("/reviews/${id}") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }

            it("HTTP 상태는 OK") {
                perform.andExpect {
                    status { isOk() }
                }
            }

            it("응답 json 바디 검증") {
                perform.andExpect {
                    jsonPath("id") { value(1) }
                    jsonPath("content") { value("재밌어요") }
                    jsonPath("phoneNumber") { value("010-1111-2222") }
                }
            }
        }

        context("존재하지 않는 id로 요청하여 실패한다") {
            val id = 1000L
            every { reviewService.getById(id) } throws ReviewNotFoundException("no review id: $id")

            val perform = mockMvc.get("/reviews/${id}") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }

            it("HTTP 상태는 Not Found") {
                perform.andExpect {
                    status { isNotFound() }
                }
            }
        }
    }


    describe("POST /reviews/{id}") {

        context("선물하기") {
            val id = 2L
            val content = "갓쿄잉최고"
            val phoneNumber = "010-2222-3333"

            every { reviewService.sendGift(id) } returns Review(id, content, phoneNumber, true)

            val perform = mockMvc.put("/reviews/$id")

            it("HTTP 상태는 OK") {
                perform.andExpect {
                    status { isOk() }
                }
            }

            it("응답 json 바디 검증") {
                perform.andExpect {
                    jsonPath("sent") { value(true) }
                }
            }

        }
    }
}) {
    override fun extensions() = listOf(SpringExtension)
}
