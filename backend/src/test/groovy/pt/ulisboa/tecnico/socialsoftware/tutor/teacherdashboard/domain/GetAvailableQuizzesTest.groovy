package pt.ulisboa.tecnico.socialsoftware.tutor.questionsubmission.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.auth.domain.AuthUser
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.questionsubmission.domain.QuestionSubmission
import pt.ulisboa.tecnico.socialsoftware.tutor.questionsubmission.domain.Review
import pt.ulisboa.tecnico.socialsoftware.tutor.questionsubmission.dto.ReviewDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*

@DataJpaTest
class GetAvailableQuizzesTest extends SpockTest {

    def setup() {
    }

    def "get number of quizzes associated with a course Execution with 0 quizzes" () {
        // courseExecution.getQuizzes().count() == QuizStats.getAvailableQuizzes() == 0
    }

    def "get number of quizzes associated with a course Execution with 1 quizz" () {
        // The course execution starts without quizzes
        // courseExecution.getQuizzes().count() == QuizStats.getAvailableQuizzes() == 0
        // adds a Quizz
        // courseExecution.getQuizzes().count() == QuizStats.getAvailableQuizzes() == 1
    }

    def "get number of quizzes associated with a course Execution with 3 quizzes" () {
        // The course execution starts without quizzes
        // courseExecution.getQuizzes().count() == QuizStats.getAvailableQuizzes() == 0
        // adds a Quizz
        // courseExecution.getQuizzes().count() == QuizStats.getAvailableQuizzes() == 1
        // adds a Quizz
        // courseExecution.getQuizzes().count() == QuizStats.getAvailableQuizzes() == 2
        // adds a Quizz
        // courseExecution.getQuizzes().count() == QuizStats.getAvailableQuizzes() == 3
    }

    def "create two statistics for the same execution"(){
        // throw(TutorException)
    }

    def "update the statistics of a dashboard"(){
        // dashboard = new ...
        // courseExecution = new ...
        // getQuizStats(dashboard, courseExecution)
        // dashboard.addDashbord(dashboard)
        // dashboard.addCourseExecution(courseExecution)
        // courseExecution.addQuiz() // n vezes
        // dashboard.getQuizStat().getNumQuizzes()
    }

}




