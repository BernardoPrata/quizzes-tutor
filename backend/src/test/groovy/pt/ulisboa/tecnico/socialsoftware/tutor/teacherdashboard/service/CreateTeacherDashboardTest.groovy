package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.TeacherDashboard
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.QuestionStatsRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.QuizStatsRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.repository.StudentStatsRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import spock.lang.Unroll

@DataJpaTest
class CreateTeacherDashboardTest extends SpockTest {
    def teacher
    def course

    def setup() {
        createExternalCourseAndExecution()

        teacher = new Teacher(USER_1_NAME, false)
        userRepository.save(teacher)

        course = new Course(COURSE_1_NAME, Course.Type.EXTERNAL)
        courseRepository.save(course)
    }

    def "create an empty dashboard"() {
        given: "a teacher in a course execution"
        teacher.addCourse(externalCourseExecution)

        when: "a dashboard is created"
        teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "an empty dashboard is created"
        teacherDashboardRepository.count() == 1L
        def result = teacherDashboardRepository.findAll().get(0)
        result.getId() != 0
        result.getCourseExecution().getId() == externalCourseExecution.getId()
        result.getTeacher().getId() == teacher.getId()

        and: "the teacher has a reference for the dashboard"
        teacher.getDashboards().size() == 1
        teacher.getDashboards().contains(result)
    }

    def "cannot create multiple dashboards for a teacher on a course execution"() {
        given: "a teacher in a course execution"
        teacher.addCourse(externalCourseExecution)

        and: "an empty dashboard for the teacher"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        when: "a second dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "there is only one dashboard"
        teacherDashboardRepository.count() == 1L

        and: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TEACHER_ALREADY_HAS_DASHBOARD
    }

    def "cannot create a dashboard for a user that does not belong to the course execution"() {
        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TEACHER_NO_COURSE_EXECUTION
    }

    @Unroll
    def "cannot create a dashboard with courseExecutionId=#courseExecutionId"() {
        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(courseExecutionId, teacher.getId())

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.COURSE_EXECUTION_NOT_FOUND

        where:
        courseExecutionId << [0, 100]
    }

    @Unroll
    def "cannot create a dashboard with teacherId=#teacherId"() {
        when: "a dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacherId)

        then: "an exception is thrown"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.USER_NOT_FOUND

        where:
        teacherId << [0, 100]
    }
    def "create multiple dashboards for same course execution"() {
        given: "two teachers in a course execution"
        def teacher2 = new Teacher(USER_2_NAME, false)
        userRepository.save(teacher2)
        teacher.addCourse(externalCourseExecution)
        teacher2.addCourse(externalCourseExecution)

        when: "an empty dashboard for the teacher is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "there is only one dashboard"
        teacherDashboardRepository.count() == 1L
        and: "there is only one quizStats"
        quizStatsRepository.count() == 1L

        when: "a second dashboard is created"
        teacherDashboardService.createTeacherDashboard(externalCourseExecution.getId(), teacher2.getId())

        then: "there are two dashboards"
        teacherDashboardRepository.count() == 2L

        and: "there are two quizStats"
        quizStatsRepository.count() == 2L

    }
    def "get statistics for a Course with only the current Course Execution"() {

        given: "a teacher in a course execution"
        teacher.addCourse(externalCourseExecution)

        when: "a dashboard is created"
        def tDashboardDto = teacherDashboardService.getTeacherDashboard(externalCourseExecution.getId(), teacher.getId())

        then: "there are only statistics for the current Course Execution"

        tDashboardDto.getNumberOfStudents().size() == 1
        tDashboardDto.getNumberOfQuizzes().size() == 1
        tDashboardDto.getNumberOfQuestions().size() == 1
        tDashboardDto.getNumStudentsOver75perc().size() == 1
        tDashboardDto.getUniqueQuizzesSolved().size() == 1
        tDashboardDto.getUniqueQuestionsSolved().size() == 1
        tDashboardDto.getNumStudentsOver3quizes().size() == 1
        tDashboardDto.getAverageSolvedQuizes().size() == 1
        tDashboardDto.getAverageSolvedCorrectQuestions().size() == 1

        and: "the statistics in the repository match the ones in the DTO"
        def questStats = questionStatsRepository.findAll().get(0)
        def quizStats = quizStatsRepository.findAll().get(0)
        def studentStats = studentStatsRepository.findAll().get(0)

        questionStatsRepository.count() == 1
        questStats.getNumAvailable() == tDashboardDto.getNumberOfQuestions().get(0)
        questStats.getAnsweredQuestionUnique() == tDashboardDto.getUniqueQuestionsSolved().get(0)
        questStats.getAverageQuestionsAnswered() == tDashboardDto.getAverageSolvedCorrectQuestions().get(0)

        quizStatsRepository.count() == 1
        quizStats.getNumQuizzes() == tDashboardDto.getNumberOfQuizzes().get(0)
        quizStats.getTotalSolvedNumQuizzes() == tDashboardDto.getUniqueQuizzesSolved().get(0)
        quizStats.getAverageSolvedNumQuizzes() == tDashboardDto.getAverageSolvedQuizes().get(0)

        studentStatsRepository.count() == 1
        studentStats.getNumStudents() == tDashboardDto.getNumberOfStudents().get(0)
        studentStats.getNumStudentsWithMoreThan75PerCentCorrectAnswers() == tDashboardDto.getNumStudentsOver75perc().get(0)
        studentStats.getNumStudentsWithAtLeastThreeQuestionsAnswered() == tDashboardDto.getNumStudentsOver3quizes().get(0)

    }

    def "get statistics are correct with 1 previous Course Execution"() {

        given: "a teacher in a course execution"

        def previousCourseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_4_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(previousCourseExecution)

        teacher.addCourse(previousCourseExecution)

        when: "an empty dashboard for the teacher is created"
        teacherDashboardService.createTeacherDashboard(previousCourseExecution.getId(), teacher.getId())

        then: "there is only one dashboard"
        teacherDashboardRepository.count() == 1L

        and: "there are only one Stats for each type"
        quizStatsRepository.count() == 1L
        questionStatsRepository.count() == 1L
        studentStatsRepository.count() == 1L

        when: "a second dashboard is created"

        def currentCourseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_3_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(currentCourseExecution)

        teacher.addCourse(currentCourseExecution)

        def tDashboardDto = teacherDashboardService.createTeacherDashboard(currentCourseExecution.getId(), teacher.getId())

        then: "there is only one dashboard"
        teacherDashboardRepository.count() == 2L

        and: "there are two Stats for each type for the previous Course Execution and one for the current Course Execution"
        quizStatsRepository.count() == 3L
        questionStatsRepository.count() == 3L
        studentStatsRepository.count() == 3L

        then: "there are statistics for the current and previous Course Execution"

        tDashboardDto.getNumberOfQuizzes().size() == 2
        tDashboardDto.getNumberOfQuestions().size() == 2
        tDashboardDto.getNumStudentsOver75perc().size() == 2
        tDashboardDto.getUniqueQuizzesSolved().size() == 2
        tDashboardDto.getUniqueQuestionsSolved().size() == 2
        tDashboardDto.getNumStudentsOver3quizes().size() == 2
        tDashboardDto.getAverageSolvedQuizes().size() == 2
        tDashboardDto.getAverageSolvedCorrectQuestions().size() == 2

        tDashboardDto.getExecutionYears().size() == 2
        // [2018, 2017]
        tDashboardDto.getExecutionYears().get(0) == 2018
        tDashboardDto.getExecutionYears().get(1) == 2017

    }

    def "get statistics are correct with 3 previous Course Executions"() {
        given: "a teacher in 4 course executions"
        def courseExecution

        courseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_5_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(courseExecution)
        teacher.addCourse(courseExecution)

        courseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_4_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(courseExecution)
        teacher.addCourse(courseExecution)

        courseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_3_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(courseExecution)
        teacher.addCourse(courseExecution)

        def currentCourseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        courseExecutionRepository.save(currentCourseExecution)

        when: "a dashboard is created"
        teacher.addCourse(currentCourseExecution)
        def tDashboardDto = teacherDashboardService.createTeacherDashboard(currentCourseExecution.getId(), teacher.getId())

        then: "there is only one dashboard"
        teacherDashboardRepository.count() == 1L

        and: "there are three Stats for each Course Execution of the dashboard"
        // PS: new stats are created for each execution on the dashboard, even if already one exists
        quizStatsRepository.count() == 3L
        questionStatsRepository.count() == 3L
        studentStatsRepository.count() == 3L

        then: "there are statistics for the current and previous Course Execution"

        tDashboardDto.getNumberOfQuizzes().size() == 3
        tDashboardDto.getNumberOfQuestions().size() == 3
        tDashboardDto.getNumStudentsOver75perc().size() == 3
        tDashboardDto.getUniqueQuizzesSolved().size() == 3
        tDashboardDto.getUniqueQuestionsSolved().size() == 3
        tDashboardDto.getNumStudentsOver3quizes().size() == 3
        tDashboardDto.getAverageSolvedQuizes().size() == 3
        tDashboardDto.getAverageSolvedCorrectQuestions().size() == 3

        and: "the correct number of execution years are added"
        tDashboardDto.getExecutionYears().size() == 3

        // 2017
        tDashboardDto.getExecutionYears().get(0) == 2019
        //2018
        tDashboardDto.getExecutionYears().get(1) == 2018
        //2019
        tDashboardDto.getExecutionYears().get(2) == 2017
        //[2019, 2018, 2017
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
