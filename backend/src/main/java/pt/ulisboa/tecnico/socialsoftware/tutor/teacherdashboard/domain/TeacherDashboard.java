package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.domain.Teacher;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;


@Entity
public class TeacherDashboard implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private CourseExecution courseExecution;

    @ManyToOne
    private Teacher teacher;

    public TeacherDashboard() {
    }

    // baseed on knowledge from studentDashBoard
    @OneToMany(mappedBy = "teacherDashboard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizStats> quizStats = new ArrayList<>();

    public TeacherDashboard(CourseExecution courseExecution, Teacher teacher) {
        setCourseExecution(courseExecution);
        setTeacher(teacher);
    }

    public void remove() {
        teacher.getDashboards().remove(this);
        teacher = null;
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

    public void update(){
        for (QuizStats quizStat : quizStats) {
            quizStat.update();
        }
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
                '}';
    }


}
