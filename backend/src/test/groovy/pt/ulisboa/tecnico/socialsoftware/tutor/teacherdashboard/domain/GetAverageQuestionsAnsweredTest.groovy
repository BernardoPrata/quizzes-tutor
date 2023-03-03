package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest

@DataJpaTest
class GetAverageQuestionsAnsweredTest extends SpockTest {

    def setup() {}

    def "get the average number of questions answered in a course execution with one student"() {}

    def "get the average questions answered in a courseExecution with no students"(){}

    def "get the average questions answered in a courseExecution with 2 students and no answers"(){}

    def "get the average questions answered in a courseExecution with 2 students answering different amounts of questions"(){}

    def "update the statistics of a dashboard"() {}

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}

}