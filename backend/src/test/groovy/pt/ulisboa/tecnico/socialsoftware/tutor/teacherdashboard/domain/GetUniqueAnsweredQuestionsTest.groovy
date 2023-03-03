package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest

@DataJpaTest
class GetUniqueAnsweredQuestionsTest extends SpockTest {

    def setup() {}

    def "get number of unique questions answered for a course Execution with no questions answered"() {}

    def "get number of unique questions answered for a course Execution with 1 question answered"() {}

    def "get number of unique questions answered for a course Execution with 3 question answered"() {}

    def "update the statistics of a dashboard"() {}

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}

}