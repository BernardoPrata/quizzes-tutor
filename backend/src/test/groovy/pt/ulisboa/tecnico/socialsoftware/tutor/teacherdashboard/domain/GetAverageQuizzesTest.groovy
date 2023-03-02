package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest

@DataJpaTest
class GetAverageQuizzesTest extends SpockTest {

    def "get the average number of quizzes per student with 0 solved quizzes" () {

        given:

        expect: true
    }


    def "get the average number of quizzes per student with 1 solved quiz"(){

        given:

        expect: true
    }


    def "get the average number of quizzes per student with multiple solved quizzes"(){

        given:

        expect: true
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}