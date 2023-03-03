package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats
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
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler

import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*

@DataJpaTest
class GetUniqueAnsweredQuestionsTest extends SpockTest {

    def teacher
    def student1
    def student2
    def student3
    def course
    def courseExecution
    def dashboard
    def questionStats
    def quizQuestion

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

        questionStats = new QuestionStats(courseExecution, dashboard)
        questionStatsRepository.save(questionStats)

        student1 = new Student(USER_1_NAME, false)
        student2 = new Student(USER_2_NAME, false)
        student3 = new Student(USER_3_NAME, false)

        student1.addCourse(courseExecution)
        student2.addCourse(courseExecution)
        student3.addCourse(courseExecution)
    }

    def "get number of unique questions answered for a course Execution with no questions answered"() {
        when:
        questionStats.update()
        def result = questionStats.getAnsweredQuestionUnique()

        then: "the returned number of quizzes is correct"
        result == 0
        result == dashboard.getQuestionStats().get(0).getAnsweredQuestionUnique()
    }

    def "get number of unique questions answered for a course Execution with 1 question answered"() {

        given: "student correctly answers question of quiz"
        create_and_answer_quiz(courseExecution, student1, true)

        when: "a question is answered"
        dashboard.update()
        def result = questionStats.getAnsweredQuestionUnique()


        then: "1 unique question has been answered"
        result == dashboard.getQuestionStats().get(0).getAnsweredQuestionUnique()
        result == 1

    }

    def "get number of unique questions answered for a course Execution with 3 question answered"() {

        given: "student correctly answers question of quiz"
        create_and_answer_quiz(courseExecution, student1, true)
        create_and_answer_quiz(courseExecution, student2, true)
        create_and_answer_quiz(courseExecution, student3, true)

        when: "the dashboard is updated"
        dashboard.update()
        def result = questionStats.getAnsweredQuestionUnique()

        then: "1 unique question has been answered"
        result == dashboard.getQuestionStats().get(0).getAnsweredQuestionUnique()
        result == 3
    }

    def "update the statistics of a dashboard"() {

        given: "student correctly answers question of quiz"
        create_and_answer_quiz(courseExecution, student1, true)

        and: "the stats are updated and obtained"
        dashboard.update()
        def result1 = questionStats.getAnsweredQuestionUnique()

        and: "another student correctly answers question of quiz"
        create_and_answer_quiz(courseExecution, student1, true)

        and: "the stats are updated and obtained"
        dashboard.update()
        def result2 = questionStats.getAnsweredQuestionUnique()

        expect:
        result2 == dashboard.getQuestionStats().get(0).getAnsweredQuestionUnique()
        result2 == result1 + 1
        result1 == dashboard.getQuestionStats().get(0).getAnsweredQuestionUnique() - 1
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}

}