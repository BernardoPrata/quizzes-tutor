package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.utils.DateHandler
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Student
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Option

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.MultipleChoiceQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer

import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuestionAnswer

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.QuestionStats

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*

import spock.lang.Unroll

@DataJpaTest
class GetAverageQuestionsAnsweredTest extends SpockTest {

    def teacher
    def dashboard
    def course

    def courseExecution

    def questionStats

    def student1
    def student2

    def quizAnswer

    def questionAnswer

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
    }

    def "get the average number of questions answered in a course execution with one student"() {

        given: "a student"
        student1 = new Student(USER_1_NAME, false)
        student1.addCourse(courseExecution)

        and: "the student answers 2 questions"
        create_and_answer_quiz(courseExecution, student1, true)
        create_and_answer_quiz(courseExecution, student1, true)

        when: "the statistics are updated"
        dashboard.update()
        def result = questionStats.getAverageQuestionsAnswered()

        then: "the returned average number of questions answered is correct"
        result == 2
        result == dashboard.getQuestionStats().get(0).getAverageQuestionsAnswered()
    }

    def "get the average questions answered in a courseExecution with no students"(){

        when: "the statistics are updated"
        dashboard.update()
        def result = questionStats.getAverageQuestionsAnswered()

        then: "the returned average number of questions answered is correct"
        result == 0
        result == dashboard.getQuestionStats().get(0).getAverageQuestionsAnswered()
    }

    def "get the average questions answered in a courseExecution with two students and no answers"(){

        given: "two students"
        student1 = new Student(USER_1_NAME, false)
        student2 = new Student(USER_2_NAME, false)
        student1.addCourse(courseExecution)
        student2.addCourse(courseExecution)

        when: "the statistics are updated"
        dashboard.update()
        def result = questionStats.getAverageQuestionsAnswered()

        then: "the returned average number of questions is correct"
        result == 0
        result == dashboard.getQuestionStats().get(0).getAverageQuestionsAnswered()
    }

    def "get the average questions answered in a courseExecution with 2 students answering different amounts of questions"(){
        given: "two students"
        student1 = new Student(USER_1_NAME, false)
        student2 = new Student(USER_2_NAME, false)
        student1.addCourse(courseExecution)
        student2.addCourse(courseExecution)

        and: "student1 answers two questions"
        create_and_answer_quiz(courseExecution, student1, true)
        create_and_answer_quiz(courseExecution, student1, true)

        and: "student2 answers one question"
        create_and_answer_quiz(courseExecution, student2, true)

        when: "the statistics are updated"
        dashboard.update()
        def result = questionStats.getAverageQuestionsAnswered()

        then: "the returned average number of questions answered is correct"
        result == 1.5
        result == dashboard.getQuestionStats().get(0).getAverageQuestionsAnswered()
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}

}