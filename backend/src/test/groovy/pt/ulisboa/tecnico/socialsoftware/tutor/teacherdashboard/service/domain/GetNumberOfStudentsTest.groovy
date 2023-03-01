package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.BeanConfiguration
import pt.ulisboa.tecnico.socialsoftware.tutor.SpockTest
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher
import spock.lang.Unroll

@DataJpaTest
class GetNumberOfStudentsTest extends SpockTest { 
    // def teacher
    // def dashboard //teacher dashboard
    // def course 
    // def courseExecution

    def setup() {
        // createExternalCourseAndExecution()

        // teacher = new Teacher(USER_1_NAME, false)
        // userRepository.save(teacher)

        // course = new Course(COURSE_1_NAME, Course.Type.EXTERNAL)
        // courseRepository.save(course)

        // courseExecution = new CourseExecution(course, COURSE_1_ACRONYM, COURSE_1_ACADEMIC_TERM, Course.Type.EXTERNAL, LOCAL_DATE_TOMORROW)
        // courseExecutionRepository.save(courseExecution)

        // dashboard = new TeacherDashboard(courseExecution, teacher)
        // teacherDashboardRepository.save(dashboard)

        //studentsStats = new StudentsStats(dashboard, courseExecution)
        //studentsStatsRepository.save(studentsStats)
    }

    def "get total number of students when there is no students in the current course Execution"() {
        // when: 
        // studentsStats.update()
        // def result = studentsStats.GetNumStudents()

        // then: "the returned number of students is correct"
        // result == 0    

        given:
        expect: true 
    }

    def "get total number of students when there is only 1 student in the current course Execution"() {
        // given: "a new student"

        // when:
        // studentsStats.update()
        // def result = studentsStats.GetNumStudents()

        // then: "the returned number of students is correct"
        // result == 1

        given:
        expect: true

    }

    def "get total number of students when there are 3 students in the current course Execution"() {
        //    given: "a new student"
        //    student1 = new Student()
        //    student1.setID(student1) //assuming id is generated randomly

        //    and: "another student"
        //    student2 = new Student()
        //    student2.setID(student2)

        //    and: "another student"
        //    student3 = new Student()
        //    student3.setID()

        //    when:
        //    studentsStats.update()
        //    def result = studentsStats.GetNumStudents()

        //    then: "the returned number of students is correct"
        //    result == 3

        given:
        expect: true
    }


    @Unroll
    def "cannot get number of students with teacherDashboard=#teacherDashboardId"() {

        given:
        //attempt to get number of students with invalid teacherDashboard and raise exception

        expect: true
    }


    @Unroll
    def "cannot get number of students with courseExecution=#courseExecutionId"() {

        // when:
        // teacherDashboardService.getTeacherDashboard(courseExecutionId, authUserDto.getId())

        // then: "an exception is thrown"
        // def exception = thrown(TutorException)
        // exception.getErrorMessage() == ErrorMessage.COURSE_EXECUTION_NOT_FOUND

        // where:
        // courseExecutionId << [0, 100]


        given:
        //attempt to get number of students with invalid courseExecution and raise exception

        expect: true
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
