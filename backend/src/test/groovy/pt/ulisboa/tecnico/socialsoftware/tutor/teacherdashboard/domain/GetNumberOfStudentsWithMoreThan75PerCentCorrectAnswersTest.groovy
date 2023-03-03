package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.AnswerDetails
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.StudentStats
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import spock.lang.Unroll

// fixme: this is a workaround for a bug in the test framework
import pt.ulisboa.tecnico.socialsoftware.tutor.studentdashboard.service.FailedAnswersSpockTest

@DataJpaTest
class GetNumberOfStudentsWithMoreThan75PerCentCorrectAnswersTest extends SpockTest { 
    def teacher
    def course
    def courseExecution
    def dashboard
    def studentStats

    def student1
    def student2
    def student3

    def create_and_answer_quiz(courseExecution, student, correct){
        def question = new Question()
        def questionDetails = new MultipleChoiceQuestion()
        question.setQuestionDetails(questionDetails)

        def option = new Option()
        option.setCorrect(true)
        option.setSequence(0)
        option.setQuestionDetails(questionDetails)

        def optionKO = new Option()
        optionKO.setCorrect(false)
        optionKO.setSequence(1)
        optionKO.setQuestionDetails(questionDetails)

        def quiz = new Quiz()
        quiz.setType(Quiz.QuizType.PROPOSED.toString())
        quiz.setCourseExecution(courseExecution)

        def quizQuestion = new QuizQuestion(quiz, question, 0)

        def date = DateHandler.now()
        def quizAnswer = new QuizAnswer()
        quizAnswer.setCompleted(true)
        quizAnswer.setCreationDate(date)
        quizAnswer.setAnswerDate(date)
        quizAnswer.setStudent(student)
        quizAnswer.setQuiz(quiz)

        def questionAnswer = new QuestionAnswer()
        questionAnswer.setTimeTaken(1)
        questionAnswer.setQuizAnswer(quizAnswer)
        questionAnswer.setQuizQuestion(quizQuestion)

        def correctOption = quizQuestion.getQuestion().getQuestionDetails().getCorrectOption()
        
        if (correct) {
            def answerDetailsCorrect = new MultipleChoiceAnswer(questionAnswer, correctOption)
            questionAnswer.setAnswerDetails(answerDetailsCorrect)
        } else {
            def incorrectOption = quizQuestion.getQuestion().getQuestionDetails().getOptions().stream().filter(optionX -> optionX != correctOption).findAny().orElse(null)
            def answerDetailsWrong = new MultipleChoiceAnswer(questionAnswer, incorrectOption)
            questionAnswer.setAnswerDetails(answerDetailsWrong)
        }

        return questionAnswer
    }

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        course = new Course(COURSE_1_NAME, Course.Type.EXTERNAL)
        courseRepository.save(course)

        courseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(courseExecution)

        dashboard = new TeacherDashboard(courseExecution, teacher)
        teacherDashboardRepository.save(dashboard)

        studentStats = new StudentStats(courseExecution, dashboard)
        studentStatsRepository.save(studentStats)

        student1 = new Student(USER_1_NAME, false)
        student2 = new Student(USER_2_NAME, false)
        student3 = new Student(USER_3_NAME, false)
    }

    def "get total number of students with more than 75 per cent correct answers with 0 students that fit the criteria"() {
        when: "the statistics are updated"
        studentStats.update()
        def result = studentStats.getNumStudentsWithMoreThan75PerCentCorrectAnswers()

        then: "the returned number of students is correct"
        dashboard.getStudentsStats().size() == 1
        result == 0
        result == dashboard.getStudentsStats().get(0).getNumStudentsWithMoreThan75PerCentCorrectAnswers()
    }

    def "get total number of students with more than 75 per cent correct answers with 1 students that fits the criteria"() {
        given: "a new student with 100 per cent correct answers (1 quizzes, 1 correct)"
        student1.addCourse(courseExecution)
        def questionAnswer = create_and_answer_quiz(courseExecution, student1, true)

        when: "the statistics are updated"
        studentStats.update()
        def result = studentStats.getNumStudentsWithMoreThan75PerCentCorrectAnswers()

        then: "the returned number of students is correct"
        dashboard.getStudentsStats().size() == 1
        result == 1
        result == dashboard.getStudentsStats().get(0).getNumStudentsWithMoreThan75PerCentCorrectAnswers()
    }

    def "get total number of students with more than 75 per cent correct answers with 1 students that doesn't fit the criteria"() {
        given: "a new student with 50 per cent correct answers (2 quizzes, 1 correct)"
        student1.addCourse(courseExecution)
        def questionAnswer1 = create_and_answer_quiz(courseExecution, student1, true)
        def questionAnswer2 = create_and_answer_quiz(courseExecution, student1, false)

        when: "the statistics are updated"
        studentStats.update()
        def result = studentStats.getNumStudentsWithMoreThan75PerCentCorrectAnswers()

        then: "the returned number of students is correct"
        dashboard.getStudentsStats().size() == 1
        result == 0
        result == dashboard.getStudentsStats().get(0).getNumStudentsWithMoreThan75PerCentCorrectAnswers()
    }

    def "get total number of students with more than 75 per cent correct answers with 2 students, one that fits the criteria and another that doesn't fit the criteria"() {
        given: "2 new students with 0 per cent correct answers (2 quizzes, 0 correct)"
        student1.addCourse(courseExecution)
        student2.addCourse(courseExecution)
        def questionAnswer1 = create_and_answer_quiz(courseExecution, student1, true)
        def questionAnswer2 = create_and_answer_quiz(courseExecution, student2, false)

        when: "the statistics are updated"
        studentStats.update()
        def result = studentStats.getNumStudentsWithMoreThan75PerCentCorrectAnswers()

        then: "the returned number of students is correct"
        dashboard.getStudentsStats().size() == 1
        result == 1
        result == dashboard.getStudentsStats().get(0).getNumStudentsWithMoreThan75PerCentCorrectAnswers()
    }

    def "cannot add the same statistics twice"(){

        when: "the same statistics are added for the dashboard twice"
        dashboard.addStudentStats(studentStats)

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.DUPLICATE_STUDENT_STATS
    }

    def "update the statistics of a dashboard"(){
        given: "a new student"
        def student = new Student(USER_1_NAME, false)
        student.addCourse(courseExecution)

        when: "the dashboard isn't updated"
        def result = dashboard.getStudentsStats().get(0).getNumStudents()

        then: "the returned number of students isn't updated"
        result == 0

        when: "the dashboard is updated"
        dashboard.update()
        result = dashboard.getStudentsStats().get(0).getNumStudents()

        then: "the returned number of students is now updated"
        result == 1
    }

    def "create a StudentStats"(){
        given: "a new student"
        def student = new Student(USER_1_NAME, false)
        student.addCourse(courseExecution)
        userRepository.save(student)

        when: "the teacherDashboard is updated"
        dashboard.update()
        def result = dashboard.getStudentsStats()

        then: "the list studentStats contains only one element"
        result.size()==1

        when: ""
        def resultStudentStats = result.get(0)

        then: "the new studentStat is correctly persisted"
        studentStatsRepository.count() == 1L

        def studentStats = studentStatsRepository.findAll().get(0)
        studentStats.getId() != null
        studentStats.getTeacherDashboard().getId() == dashboard.getId()
        studentStats.getCourseExecution().getId() == courseExecution.getId()

        and: " and number of students is correct both in DB and in instance"
        resultStudentStats.getNumStudents()== 1
        studentStats.getNumStudents() == 1
    }

    def "get studentStats from two course Executions for same dashboard"(){
        given:
        def courseExecution2 = new CourseExecution(course, COURSE_2_ACRONYM, COURSE_2_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        assert courseExecution2 != null : "courseExecution2 is null"
        courseExecutionRepository.save(courseExecution2)

        dashboard.setCourseExecution(courseExecution2)
        def studentStats2 = new StudentStats(courseExecution2,dashboard)
        studentStatsRepository.save(studentStats2)

        when:
        dashboard.update()
        def result = dashboard.getStudentsStats()

        then:
        result.size() == 2
        studentStatsRepository.count() == 2L
        def studentStats = studentStatsRepository.findAll().get(1)
        studentStats.getId() != null
        studentStats.getTeacherDashboard().getId() == dashboard.getId()
        studentStats.getCourseExecution().getId() == courseExecution2.getId()
        studentStats.getNumStudents() == 0
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
