package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler


import spock.lang.Unroll

@DataJpaTest
class UpdateTeacherDashboardTest extends SpockTest {

    def teacher
    def dashboard
    def dashboardDto
    def quiz
    def question
    def student

    def addQuizzes(courseExecution, key) {
        quiz = new Quiz()
        quiz.setKey(key)
        quiz.setTitle("Quiz")
        quiz.setType(Quiz.QuizType.PROPOSED.toString())
        quiz.setCourseExecution(courseExecution)
        quiz.setAvailableDate(DateHandler.now())
        quizRepository.save(quiz)
        courseExecution.addQuiz(quiz)
    }

    def addStudents(courseExecution) {
        student = new Student(USER_1_NAME, false)
        userRepository.save(student)
        student.addCourse(courseExecution)
        courseExecution.addUser(student)
    }

    def addQuestions(courseExecution) {
        question = new Question()
        question.setStatus(Question.Status.AVAILABLE)
        courseExecution.getCourse().addQuestion(question)
    }

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        teacher.addCourse(externalCourseExecution)
        dashboardDto = teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

    }

    def "update an empty dashboard with no information"() {
        when: "a dashboard is updated"
        teacherDashboardService.updateTeacherDashboard(teacherDashboardRepository.findAll().get(0).getId())

        then: "the statistics in the repository match the ones in the DTO (dashboard is still empty)"
        quizStatsRepository.count() == 1L
        questionStatsRepository.count() == 1L
        studentStatsRepository.count() == 1L

        def quizStats = quizStatsRepository.findAll().get(0)
        def questionStats = questionStatsRepository.findAll().get(0)
        def studentStats = studentStatsRepository.findAll().get(0)

        dashboardDto.getNumberOfQuizzes().size() == 1
        dashboardDto.getNumberOfQuestions().size() == 1
        dashboardDto.getNumberOfStudents().size() == 1

        def result1 = dashboardDto.getNumberOfQuizzes().get(0)
        def result2 = dashboardDto.getNumberOfQuestions().get(0)
        def result3 = dashboardDto.getNumberOfStudents().get(0)

        result1 == 0
        result2 == 0
        result3 == 0

        result1 == quizStats.getNumQuizzes()
        result2 == questionStats.getNumAvailable()
        result3 == studentStats.getNumStudents()
    }

    def "update dashboard with new information"() {
        given: "new components added"
        addQuizzes(externalCourseExecution, 1)
        addQuestions(externalCourseExecution)
        addStudents(externalCourseExecution)

        when: "dashboard is updated"
        teacherDashboardService.updateTeacherDashboard(teacherDashboardRepository.findAll().get(0).getId())
        dashboardDto = teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "the statistics in the repository match the ones in the DTO (dashboard is no longer empty)"
        quizStatsRepository.count() == 1L
        questionStatsRepository.count() == 1L
        studentStatsRepository.count() == 1L

        dashboardDto.getNumberOfQuizzes().get(0) == 1
        dashboardDto.getNumberOfQuestions().get(0) == 1
        teacherDashboardRepository.findAll().get(0).getStudentsStats().get(0).getNumStudents() == 1
        dashboardDto.getNumberOfStudents().get(0) == 1
    }

    def "cannot update a dashboard with dashboardID=#dashboardID"() {
        when: "dashboard is updated"
        teacherDashboardService.updateTeacherDashboard(dashboardID)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.DASHBOARD_NOT_FOUND

        where:
        dashboardID << [0, 100]
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}