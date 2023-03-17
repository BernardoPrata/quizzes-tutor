package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.dto.QuizDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler



import spock.lang.Unroll

@DataJpaTest
class UpdateAllTeacherDashboardsTest extends SpockTest {

    def course1
    def course2
    def courseExecution1
    def courseExecution2
    def teacher1
    def teacher2
    def dashboardDto1
    def dashboardDto2
    def quiz
    def student
    def question

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

        course1 = new Course(COURSE_1_NAME, Course.Type.TECNICO)
        courseRepository.save(course1)

        courseExecution1 = new CourseExecution(course1, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(courseExecution1)

        course2 = new Course(COURSE_2_NAME, Course.Type.TECNICO)
        courseRepository.save(course2)

        courseExecution2 = new CourseExecution(course2, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.TECNICO, LOCAL_DATE_TODAY)
        courseExecutionRepository.save(courseExecution2)

    }

    def "update allTeacherDashboards with no existing teachers"() {
        when: "dashboards are updated"
        teacherDashboardService.updateAllTeacherDashboards()

        then: "data is correct"
        userRepository.count() == 0L
        quizStatsRepository.count() == 0L
        questionStatsRepository.count() == 0L
        studentStatsRepository.count() == 0L
    }

    def "update allTeacherDashboards with one existing teacher and no new information"() {
        given: "a teacher"
        teacher1 = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher1)
        teacher1.addCourse(courseExecution1)

        and: "a dashboard"
        dashboardDto1 = teacherDashboardService.createTeacherDashboard(courseExecution1.getId(), teacher1.getId())

        when: "dashboards are updated"
        teacherDashboardService.updateAllTeacherDashboards()
        dashboardDto1 = teacherDashboardService.getTeacherDashboard(courseExecution1.getId(), teacher1.getId())

        then: "the statistics in the repository match the ones in the DTO"
        teacherDashboardRepository.count() == 1L
        quizStatsRepository.count() == 1L
        questionStatsRepository.count() == 1L
        studentStatsRepository.count() == 1L

        def quizStats = quizStatsRepository.findAll().get(0)
        def questionStats = questionStatsRepository.findAll().get(0)
        def studentStats = studentStatsRepository.findAll().get(0)

        def result1 = dashboardDto1.getNumberOfQuizzes().get(0)
        def result2 = dashboardDto1.getNumberOfQuestions().get(0)
        def result3 = dashboardDto1.getNumberOfStudents().get(0)

        result1 == 0
        result2 == 0
        result3 == 0

        result1 == quizStats.getNumQuizzes()
        result2 == questionStats.getNumAvailable()
        result3 == studentStats.getNumStudents()
    }

    def "update allTeacherDashboards with two existing teachers"() {
        given: "two teachers"
        teacher1 = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher1)
        teacher1.addCourse(courseExecution1)


        teacher2 = new Teacher(USER_2_NAME, false)
        userRepository.save(teacher2)
        teacher2.addCourse(courseExecution2)

        and: "one dashboard associated with each one"
        dashboardDto1 = teacherDashboardService.createTeacherDashboard(courseExecution1.getId(), teacher1.getId())
        dashboardDto2 = teacherDashboardService.createTeacherDashboard(courseExecution2.getId(), teacher2.getId())

        and: "components are added"
        addQuizzes(courseExecution1, 1)

        addQuestions(courseExecution1)
        addQuestions(courseExecution2)

        addStudents(courseExecution2)

        when: "dashboards are updated"
        teacherDashboardService.updateAllTeacherDashboards()
        dashboardDto1 = teacherDashboardService.getTeacherDashboard(courseExecution1.getId(), teacher1.getId())
        dashboardDto2 = teacherDashboardService.getTeacherDashboard(courseExecution2.getId(), teacher2.getId())


        then: "the statistics in the repository match the ones in the DTO"
        teacherDashboardRepository.count() == 2L
        quizStatsRepository.count() == 2L
        questionStatsRepository.count() == 2L
        studentStatsRepository.count() == 2L

        def quizStats1 = quizStatsRepository.findAll().get(0)
        def questionStats1 = questionStatsRepository.findAll().get(0)
        def studentStats1 = studentStatsRepository.findAll().get(0)

        def quizStats2 = quizStatsRepository.findAll().get(1)
        def questionStats2 = questionStatsRepository.findAll().get(1)
        def studentStats2 = studentStatsRepository.findAll().get(1)

        def result1 = dashboardDto1.getNumberOfQuizzes().get(0)
        def result2 = dashboardDto1.getNumberOfQuestions().get(0)
        def result3 = dashboardDto1.getNumberOfStudents().get(0)

        def result4 = dashboardDto2.getNumberOfQuizzes().get(0)
        def result5 = dashboardDto2.getNumberOfQuestions().get(0)
        def result6 = dashboardDto2.getNumberOfStudents().get(0)

        result1 == 1
        result2 == 1
        result3 == 0

        result4 == 0
        result5 == 1
        result6 == 1

        result1 == quizStats1.getNumQuizzes()
        result2 == questionStats1.getNumAvailable()
        result3 == studentStats1.getNumStudents()

        result4 == quizStats2.getNumQuizzes()
        result5 == questionStats2.getNumAvailable()
        result6 == studentStats2.getNumStudents()

    }

    def "update allTeacherDashboards with one existing teacher and two associated dashboards"() {
        given: "a teacher"
        teacher1 = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher1)
        teacher1.addCourse(courseExecution1)
        teacher1.addCourse(courseExecution2)

        and: "a dashboard"
        dashboardDto1 = teacherDashboardService.createTeacherDashboard(courseExecution1.getId(), teacher1.getId())

        and: "another dashboard"
        dashboardDto2 = teacherDashboardService.createTeacherDashboard(courseExecution2.getId(), teacher1.getId())

        and: "components are added"
        addQuizzes(courseExecution1, 1)
        addQuizzes(courseExecution2, 2)

        addQuestions(courseExecution1)
        addQuestions(courseExecution2)

        addStudents(courseExecution2)

        when: "dashboards are updated"
        teacherDashboardService.updateAllTeacherDashboards()
        dashboardDto1 = teacherDashboardService.getTeacherDashboard(courseExecution1.getId(), teacher1.getId())
        dashboardDto2 = teacherDashboardService.getTeacherDashboard(courseExecution2.getId(), teacher1.getId())

        then: "the statistics in the repository match the ones in the DTO"
        teacherDashboardRepository.count() == 2L
        quizStatsRepository.count() == 2L
        questionStatsRepository.count() == 2L
        studentStatsRepository.count() == 2L

        def quizStats1 = quizStatsRepository.findAll().get(0)
        def questionStats1 = questionStatsRepository.findAll().get(0)
        def studentStats1 = studentStatsRepository.findAll().get(0)

        def quizStats2 = quizStatsRepository.findAll().get(1)
        def questionStats2 = questionStatsRepository.findAll().get(1)
        def studentStats2 = studentStatsRepository.findAll().get(1)

        def result1 = dashboardDto1.getNumberOfQuizzes().get(0)
        def result2 = dashboardDto1.getNumberOfQuestions().get(0)
        def result3 = dashboardDto1.getNumberOfStudents().get(0)

        def result4 = dashboardDto2.getNumberOfQuizzes().get(0)
        def result5 = dashboardDto2.getNumberOfQuestions().get(0)
        def result6 = dashboardDto2.getNumberOfStudents().get(0)

        result1 == 1
        result2 == 1
        result3 == 0

        result4 == 1
        result5 == 1
        result6 == 1

        result1 == quizStats1.getNumQuizzes()
        result2 == questionStats1.getNumAvailable()
        result3 == studentStats1.getNumStudents()

        result4 == quizStats2.getNumQuizzes()
        result5 == questionStats2.getNumAvailable()
        result6 == studentStats2.getNumStudents()
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}