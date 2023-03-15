package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import spock.lang.Unroll

@DataJpaTest
class RemoveTeacherDashboardTest extends SpockTest {

    def teacher

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)
    }

    def createTeacherDashboard() {
        def dashboard = new TeacherDashboard(externalCourseExecution, teacher)
        teacherDashboardRepository.save(dashboard)
        return dashboard
    }

    def createQuizStats(dashboard) {
        def quizStats = new QuizStats(externalCourseExecution, dashboard)
        quizStatsRepository.save(quizStats)
        return quizStats
    } 
     def createStudentStats(dashboard) {
        def studentstats = new StudentStats(externalCourseExecution, dashboard)
        studentStatsRepository.save(studentstats)
        return studentstats
    }
     def createQuestionStats(dashboard) {
        def questionstats = new QuestionStats(externalCourseExecution, dashboard)
        questionStatsRepository.save(questionstats)
        return questionstats
    }

    def "remove a dashboard"() {
        given: "a dashboard"
        def dashboard = createTeacherDashboard()

        when: "the user removes the dashboard"
        teacherDashboardService.removeTeacherDashboard(dashboard.getId())

        then: "the dashboard is removed"
        teacherDashboardRepository.findAll().size() == 0L
        teacher.getDashboards().size() == 0
    }

    def "cannot remove a dashboard twice"() {
        given: "a removed dashboard"
        def dashboard = createTeacherDashboard()
        teacherDashboardService.removeTeacherDashboard(dashboard.getId())

        when: "the dashboard is removed for the second time"
        teacherDashboardService.removeTeacherDashboard(dashboard.getId())

        then: "an exception is thrown"        
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.DASHBOARD_NOT_FOUND
    }

    @Unroll
    def "cannot remove a dashboard that doesn't exist with the dashboardId=#dashboardId"() {
        when: "an incorrect dashboard id is removed"
        teacherDashboardService.removeTeacherDashboard(dashboardId)

        then: "an exception is thrown"        
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.DASHBOARD_NOT_FOUND

        where:
        dashboardId << [null, 10, -1]
    }

    def "remove a dashboard with stats associated"(){
        given: "a dashboard with stats"
        def dashboard = createTeacherDashboard()
        def quizStats = createQuizStats(dashboard)
        def studentStats = createStudentStats(dashboard)
        def questionStats = createQuestionStats(dashboard)

        when: "the dashboard is removed"
        teacherDashboardService.removeTeacherDashboard(dashboard.getId())

        then: "stats are dissociated from the dashboard but still exist in the database"
        quizStatsRepository.findAll().size() == 1L
        quizStats.getTeacherDashboard() == null

        studentStatsRepository.findAll().size() == 1L
        studentStats.getTeacherDashboard() == null

        questionStatsRepository.findAll().size() == 1L
        questionStats.getTeacherDashboard() == null
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
