package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.user.repository.*
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuizStats

import spock.lang.Unroll

@DataJpaTest
class GetTotalSolvedUniqueQuizzes extends SpockTest {

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
        userRepository.save(student1)
        userRepository.save(student2)
        userRepository.save(student3)

        quiz1 = new Quiz()
        quiz2 = new Quiz()
        quiz3 = new Quiz()
        quiz4 = new Quiz()
        quiz5 = new Quiz()
        // the quiz table in DB will not be directly tested in this file,as it is already tested on issue #34 Extend Tests
        // initialize all quizzes to be able to store it in DB would take a lot of lines;
        // the same logic applies for quiz answers in the tests below, as the quizAnswer is already tested
        
        //quizRepository.save(quiz1)
        //quizRepository.save(quiz2)
        //quizRepository.save(quiz3)
        //quizRepository.save(quiz4)
        //quizRepository.save(quiz5)


        courseExecution.addUser(student1)
        courseExecution.addUser(student2)
        courseExecution.addUser(student3)

        courseExecution.addQuiz(quiz1)
        courseExecution.addQuiz(quiz2)
        courseExecution.addQuiz(quiz3)
        courseExecution.addQuiz(quiz4)
        courseExecution.addQuiz(quiz5)

    }
    def  "get the total number of unique solved quizzes by students with 0 solved quizzes"(){
        when: "dashboard is updated"
        dashboard.update()
        def result = quizStats.getTotalSolvedNumQuizzes()

        then: "the total  number of solved quizzes is  0"
        result==0

    }

    def  "get the total number of unique solved quizzes by students with 3 solved quizzes"() {
        given: "three new quiz answers"
        def qa1 = new QuizAnswer(student1, quiz1)
        def qa2 = new QuizAnswer(student2, quiz2)
        def qa3 = new QuizAnswer(student3, quiz4)
        when: "dashboard is updated"
        dashboard.update()
        def result = quizStats.getTotalSolvedNumQuizzes()

        then: "the total  number of solved quizzes is  3"
        result == 3

    }

    def  "get the total number of unique solved quizzes if students does all quizzes"() {
        given: "five new quiz answers"
        def qa1 = new QuizAnswer(student1, quiz1)
        def qa2 = new QuizAnswer(student2, quiz2)
        def qa3 = new QuizAnswer(student3, quiz4)
        def qa4 =  new QuizAnswer(student1,quiz3)
        def qa5 = new QuizAnswer(student1,quiz5)
        when: "dashboard is updated"
        dashboard.update()
        def result = quizStats.getTotalSolvedNumQuizzes()

        then: "the total  number of solved quizzes is  3"
        result == 5

    }

    def  "get the total number of unique solved quizzes if every student does same quizzes"() {
        given: "multiple quiz answers from different students"
        def qa11 = new QuizAnswer(student1, quiz1)
        def qa12 = new QuizAnswer(student1, quiz2)
        def qa13 = new QuizAnswer(student1, quiz3)
        def qa14 = new QuizAnswer(student1, quiz4)
        // answer quiz twice
        def qa15 = new QuizAnswer(student1, quiz1)

        def qa21 = new QuizAnswer(student2, quiz1)
        def qa22 = new QuizAnswer(student2, quiz2)
        def qa23 = new QuizAnswer(student2, quiz3)
        def qa24 = new QuizAnswer(student2, quiz4)
        //answer quiz twice
        def qa25 = new QuizAnswer(student2, quiz2)


        def qa31 = new QuizAnswer(student3, quiz1)
        def qa32 = new QuizAnswer(student3, quiz2)
        def qa33 = new QuizAnswer(student3, quiz3)
        def qa34 = new QuizAnswer(student3, quiz4)
        //answer quiz twice
        def qa35 = new QuizAnswer(student3, quiz3)

        when: "dashboard is updated"
        dashboard.update()
        def result = quizStats.getTotalSolvedNumQuizzes()

        then: "the total  number of unique solved quizzes is  4"
        result == 4

    }


    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}