package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class TeacherDashboard implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private CourseExecution courseExecution;

    @ManyToOne
    private Teacher teacher;

    @OneToMany(mappedBy = "teacherDashboard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentStats> studentsStats = new ArrayList<>();

    @OneToMany(mappedBy = "teacherDashboard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizStats> quizStats = new ArrayList<>();

    public TeacherDashboard() {
    }

    public TeacherDashboard(CourseExecution courseExecution, Teacher teacher) {
        setCourseExecution(courseExecution);
        setTeacher(teacher);
    }

    public void remove() {
        teacher.getDashboards().remove(this);
        teacher = null;
    }

    public void update(){
        for (QuizStats quizStat : quizStats) {
            quizStat.update();
        }

        studentsStats.forEach(StudentStats::update);
    }

    public Integer getId() {
        return id;
    }
    
    public CourseExecution getCourseExecution() {
        return courseExecution;
    }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        this.teacher.addDashboard(this);
    }

    public List<QuizStats> getQuizStats(){
        return this.quizStats;
    }

    public void addQuizStats(QuizStats newQuizStats){
        if (quizStats.stream()
                .anyMatch(quizStat -> quizStat.getCourseExecution().getId()
                        .equals(newQuizStats.getCourseExecution().getId()))) {
            throw new TutorException(ErrorMessage.DUPLICATE_QUIZ_STATS);
        }
        quizStats.add(newQuizStats);
    }

    public void addStudentStats(StudentStats newStudentStats){
        if (studentsStats.stream()
                .anyMatch(StudentStats -> StudentStats.getCourseExecution().getId()
                        .equals(newStudentStats.getCourseExecution().getId()))) {
            throw new TutorException(ErrorMessage.DUPLICATE_STUDENT_STATS);
        }
        studentsStats.add(newStudentStats);
    }

    public List<StudentStats> getStudentsStats() {
        return this.studentsStats;
    }

    public void accept(Visitor visitor) {
        // Only used for XML generation
    }

    @Override
    public String toString() {
        return "Dashboard{" +
                "id=" + id +
                ", courseExecution=" + courseExecution +
                ", teacher=" + teacher +
                ", quizStats=" + quizStats +
                ", studentsStats=" + studentsStats +
                '}';
    }


}
