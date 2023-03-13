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
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler;

import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*

@DataJpaTest
class GetAvailableQuizzesTest extends SpockTest {

    def teacher
    def dashboard
    def course
    def courseExecution

    def quiz1
    def quiz2
    def quiz3
    def quizStats

    def setup() {

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        course = new Course(COURSE_1_NAME, Course.Type.EXTERNAL)
        courseRepository.save(course)

        courseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(courseExecution)

        dashboard = new TeacherDashboard(courseExecution, teacher)
        teacherDashboardRepository.save(dashboard)

        quizStats = new QuizStats(courseExecution, dashboard)
        quizStatsRepository.save(quizStats)
    }


    def "get number of quizzes associated with a course Execution with 0 quizzes" () {

        when:
        quizStats.update()
        def result = quizStats.getNumQuizzes()

        then: "the returned number of quizzes is correct"
        result == 0
        result == dashboard.getQuizStats().get(0).getNumQuizzes()
    }


    def "get number of quizzes associated with a course Execution with 1 quizz" () {

        given: "a new quiz submission"
        quiz1 = new Quiz()
        quiz1.setKey(1)
        quiz1.setTitle("Quiz 1")
        quiz1.setType(Quiz.QuizType.PROPOSED.toString())
        quiz1.setCourseExecution(courseExecution)
        quiz1.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz1)

        when: "the statistics are updated"
        quizStats.update()
        def result = quizStats.getNumQuizzes()

        then: "the returned number of quizzes is correct"
        result == 1
        result == dashboard.getQuizStats().get(0).getNumQuizzes()
    }


    def "get number of quizzes associated with a course Execution with 3 quizzes" () {

        given: "a new quiz submission"
        quiz1 = new Quiz()
        quiz1.setKey(1)
        quiz1.setTitle("Quiz 1")
        quiz1.setType(Quiz.QuizType.PROPOSED.toString())
        quiz1.setCourseExecution(courseExecution)
        quiz1.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz1)

        and: "another quiz submission"
        quiz2 = new Quiz()
        quiz2.setKey(2)
        quiz2.setTitle("Quiz 2")
        quiz2.setType(Quiz.QuizType.PROPOSED.toString())
        quiz2.setCourseExecution(courseExecution)
        quiz2.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz2)

        and: "another quiz submission"
        quiz3 = new Quiz()
        quiz3.setKey(3)
        quiz3.setTitle("Quiz 3")
        quiz3.setType(Quiz.QuizType.PROPOSED.toString())
        quiz3.setCourseExecution(courseExecution)
        quiz3.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz3)

        when: "the statistics are updated"
        quizStats.update()
        def result = quizStats.getNumQuizzes()
        def resultQuizStats = dashboard.getQuizStats()

        then: "the returned number of quizzes is correct"
        resultQuizStats.size()==1
        result == 3
        result == dashboard.getQuizStats().get(0).getNumQuizzes()
    }


    def "cannot add the same statistics twice"(){

        when: "the same statistics are added for the dashboard twice"
        dashboard.addQuizStats(quizStats)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.DUPLICATE_QUIZ_STATS
    }


    def "update the statistics of a dashboard"(){

        given: "a new quiz submission"
        quiz1 = new Quiz()
        quiz1.setKey(1)
        quiz1.setTitle("Quiz 1")
        quiz1.setType(Quiz.QuizType.PROPOSED.toString())
        quiz1.setCourseExecution(courseExecution)
        quiz1.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz1)

        when: "the dashboard isn't updated"
        def result = dashboard.getQuizStats().get(0).getNumQuizzes()

        then: "the returned number of quizzes isn't updated"
        result == 0

        when: "the dashboard is updated"
        dashboard.update()
        result = dashboard.getQuizStats().get(0).getNumQuizzes()

        then: "the returned number of quizzes is now updated"
        result == 1
    }



    def "create a QuizStats "(){

        given: "a new created quiz"
        quiz2 = new Quiz()
        quiz2.setKey(2)
        quiz2.setTitle("Quiz 2")
        quiz2.setType(Quiz.QuizType.PROPOSED.toString())
        quiz2.setCourseExecution(courseExecution)
        quiz2.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz2)

        when: "the teacherDashboard is updated"
        dashboard.update()
        def result = dashboard.getQuizStats()

        then: "the list quizStats contains only one element"
        result.size()==1

        when: ""
        def resultQuizStats = result.get(0)

        then: "the new QuizStat is correctly persisted"
        quizStatsRepository.count() == 1L

        def quizStats = quizStatsRepository.findAll().get(0)
        quizStats.getId() != null
        quizStats.getTeacherDashboard().getId() == dashboard.getId()
        quizStats.getCourseExecution().getId() == courseExecution.getId()

        and: " and number of quizzes is correct both in DB and in instance"
        resultQuizStats.getNumQuizzes()== 1
        quizStats.getNumQuizzes() == 1


    }

    def "get quizStats from two course Executions for same dashboard"(){
        given:
        def courseExecution2 = new CourseExecution(course, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        assert courseExecution2 != null : "courseExecution2 is null"
        courseExecutionRepository.save(courseExecution2)

        dashboard.setCourseExecution(courseExecution2)
        def quizStats2 = new QuizStats(courseExecution2, dashboard)
        quizStatsRepository.save(quizStats2)

        when:
        dashboard.update()
        def result = dashboard.getQuizStats()

        then:
        result.size() == 2
        quizStatsRepository.count() == 2L
        def quizStats = quizStatsRepository.findAll().get(1)
        quizStats.getId() != null
        quizStats.getTeacherDashboard().getId() == dashboard.getId()
        quizStats.getCourseExecution().getId() == courseExecution2.getId()
        quizStats.getNumQuizzes() == 0
    }
    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}