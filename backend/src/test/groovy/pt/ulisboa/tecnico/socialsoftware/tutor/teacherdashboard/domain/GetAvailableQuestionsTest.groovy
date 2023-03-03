package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.questionsubmission.domain.QuestionSubmission
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler;

import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*

@DataJpaTest
class GetAvailableQuestionsTest extends SpockTest {
    def teacher
    def dashboard
    def dashboard2
    def course
    def courseExecution
    def courseExecution2
    def question
    def questionStats
    def questionStats1


    def setup() {
        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        course = new Course(COURSE_1_NAME, Course.Type.EXTERNAL)
        courseRepository.save(course)

        courseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(courseExecution)

        courseExecution2 = new CourseExecution(course, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(courseExecution2)

        dashboard = new TeacherDashboard(courseExecution, teacher)
        teacherDashboardRepository.save(dashboard)

        dashboard2 = new TeacherDashboard(courseExecution, teacher)
        teacherDashboardRepository.save(dashboard2)

        questionStats = new QuestionStats(courseExecution, dashboard)
        questionStatsRepository.save(questionStats)

    }

    def "get number of available questions for a course Execution with no questions"() {
        when:
        questionStats.update()
        def result = questionStats.getNumAvailable()

        then: "the returned number of questions is correct"
        result == 0
        result == dashboard.getQuestionStats().get(0).getNumAvailable()
    }

    def "get number of available questions for a course Execution with 1 question"() {
        given: "a new question"
        def question = new Question()
        question.setKey(1)
        question.setTitle(QUESTION_1_TITLE)
        question.setContent(QUESTION_1_CONTENT)
        question.setCourse(course)
        question.setStatus(Question.Status.AVAILABLE)
        questionRepository.save(question)

        when: "the statistics are updated"
        questionStats.update()
        def result = questionStats.getNumAvailable()

        then: "the returned number of questions is correct"
        result == 1
        result == dashboard.getQuestionStats().get(0).getNumAvailable()
    }

    def "get number of available questions for a course Execution with 3 questions"() {
        given: "a new question"
        def question1 = new Question()
        question1.setKey(1)
        question1.setTitle(QUESTION_1_TITLE)
        question1.setContent(QUESTION_1_CONTENT)
        question1.setCourse(course)
        question1.setStatus(Question.Status.AVAILABLE)
        questionRepository.save(question1)

        and: "another question"
        def question2 = new Question()
        question2.setKey(2)
        question2.setTitle(QUESTION_2_TITLE)
        question2.setContent(QUESTION_2_CONTENT)
        question2.setCourse(course)
        question2.setStatus(Question.Status.AVAILABLE)
        questionRepository.save(question2)

        and: "another question"
        def question3 = new Question()
        question3.setKey(3)
        question3.setTitle(QUESTION_3_TITLE)
        question3.setContent(QUESTION_3_CONTENT)
        question3.setCourse(course)
        question3.setStatus(Question.Status.AVAILABLE)
        questionRepository.save(question3)

        when: "the statistics are updated"
        questionStats.update()
        def result = questionStats.getNumAvailable()

        then: "the returned number of questions is correct"
        result == 3
        result == dashboard.getQuestionStats().get(0).getNumAvailable()
    }

    def "cannot add the same statistics twice"() {
        when: "the same statistics are added for the dashboard twice"
        dashboard.addQuestionStats(questionStats)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.DUPLICATE_QUESTION_STATS

    }

    def "update the statistics of a dashboard"() {
        given: "a new question"
        def question1 = new Question()
        question1.setKey(1)
        question1.setTitle(QUESTION_1_TITLE)
        question1.setContent(QUESTION_1_CONTENT)
        question1.setCourse(course)
        question1.setStatus(Question.Status.AVAILABLE)
        questionRepository.save(question1)

        when: "the dashboard is not updated"
        def result = dashboard.getQuestionStats().get(0).getNumAvailable()

        then: "the returned number of questionStats is not updated"
        result == 0

        when: "the dashboard is updated"
        dashboard.update()
        result = dashboard.getQuestionStats().get(0).getNumAvailable()

        then: "the returned number of questionStats is now updated"
        result == 1
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}

}