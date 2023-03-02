package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats

import spock.lang.Unroll

@DataJpaTest
class GetTotalSolvedUniqueQuizzes extends SpockTest {

    def setup() {
    }
    def  "get the total number of unique solved quizzes by students with 0 solved quizzes"(){

        given:

        expect: true
    }

    def  "get the total number of unique solved quizzes by students with 3 solved quizzes"() {

        given:

        expect: true

    }

    def  "get the total number of unique solved quizzes if students does all quizzes"() {

        given:

        expect: true

    }

    def  "get the total number of unique solved quizzes if every student does same quizzes"() {

        given:

        expect: true

    }


    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}