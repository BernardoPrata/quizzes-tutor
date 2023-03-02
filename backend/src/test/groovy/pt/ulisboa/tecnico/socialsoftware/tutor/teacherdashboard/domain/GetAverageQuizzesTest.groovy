package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats

@DataJpaTest
class GetAverageQuizzesTest extends SpockTest {

    def teacher
    def dashboard
    def course
    def courseExecution

    def student1
    def student2
    def student3

    def quiz1
    def quiz2
    def quiz3
    def quiz4
    def quiz5
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

        quizStats = new QuizStats(dashboard, courseExecution)
        quizStatsRepository.save(quizStats)

        student1 = new Student(USER_1_NAME, false)
        student2 = new Student(USER_2_NAME, false)
        student3 = new Student(USER_3_NAME, false)

        quiz1 = new Quiz()
        quiz2 = new Quiz()
        quiz3 = new Quiz()
        quiz4 = new Quiz()
        quiz5 = new Quiz()

        courseExecution.addUser(student1)
        courseExecution.addUser(student2)
        courseExecution.addUser(student3)

        courseExecution.addQuiz(quiz1)
        courseExecution.addQuiz(quiz2)
        courseExecution.addQuiz(quiz3)
        courseExecution.addQuiz(quiz4)
    }


    def "get the average number of quizzes per student with 0 solved quizzes" () {

        when: "the average number of quizzes solved per student is calculated"
        dashboard.update()
        def result = quizStats.getAverageSolvedNumQuizzes()

        then: "the returned average number of quizzes is 0"
        result == 0
    }


    def "get the average number of quizzes per student with 1 solved quiz"(){

        given: "a student who has solved one quiz"

        def qa1 = new QuizAnswer(student1, quiz1)

        when: "the average number of quizzes solved per student is calculated"
        dashboard.update()
        def result = quizStats.getAverageSolvedNumQuizzes()

        // There are three students in the course execution
        then: "the returned average number of quizzes is 1/3"
        result == (float) 0.33333334
    }


    def "get the average number of quizzes per student with multiple solved quizzes"(){

        given: "three students who have solved 1, 3 and 5 quizzes, respectively"

        def qa1 = new QuizAnswer(student1, quiz1)

        def qa2 = new QuizAnswer(student2, quiz1)
        def qa3 = new QuizAnswer(student2, quiz2)
        def qa4 = new QuizAnswer(student2, quiz3)

        def qa5 = new QuizAnswer(student3, quiz1)
        def qa6 = new QuizAnswer(student3, quiz2)
        def qa7 = new QuizAnswer(student3, quiz3)
        def qa8 = new QuizAnswer(student3, quiz4)
        def qa9 = new QuizAnswer(student3, quiz5)

        when: "the average number of quizzes solved per student is calculated"
        dashboard.update()
        def result = quizStats.getAverageSolvedNumQuizzes()

        then: "the returned average number of quizzes is 3"
        result == 3
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}