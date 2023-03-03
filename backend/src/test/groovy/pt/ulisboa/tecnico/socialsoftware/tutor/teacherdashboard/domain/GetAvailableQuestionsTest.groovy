package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest

@DataJpaTest
class GetAvailableQuestionsTest extends SpockTest {
    def setup() {}

    def "get number of available questions for a course Execution with no questions"() {}

    def "get number of available questions for a course Execution with 1 question"() {}

    def "get number of available questions for a course Execution with 3 questions"() {}

    def "cannot add the same statistics twice"() {}

    def "update the statistics of a dashboard"() {}

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}

}
