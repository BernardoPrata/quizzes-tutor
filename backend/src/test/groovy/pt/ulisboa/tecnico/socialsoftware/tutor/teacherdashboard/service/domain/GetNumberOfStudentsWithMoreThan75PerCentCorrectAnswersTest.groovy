package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import spock.lang.Unroll

@DataJpaTest
class GetNumberOfStudentsWithMoreThan75PerCentCorrectAnswersTest extends SpockTest { 
    // def teacher_dashboard
    // def courseExecution

    def setup() {
    }

    def "get total number of students with more than 75 per cent correct answers when there is no students in the current course Execution"() {
        given:
        expect: true 
    }

    def "get total number of students with more than 75 per cent correct answers when there is only 1 student in the current course Execution"() {
        given:
        expect: true
    }

    def "get total number of students with more than 75 per cent correct answers when there are 3 students in the current course Execution"() {
        given:
        expect: true
    }

    @Unroll
    def "cannot get number of students with teacherDashboard=#teacherDashboardId"() {
        given:
        //attempt to get number of students with invalid teacherDashboard and raise exception

        expect: true
    }

    @Unroll
    def "cannot get number of students with courseExecution=#courseExecutionId"() {
        given:
        //attempt to get number of students with invalid courseExecution and raise exception

        expect: true
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
