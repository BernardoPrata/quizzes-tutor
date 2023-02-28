package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import javax.persistence.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import java.util.ArrayList;

@Entity
public class QuizStats implements DomainEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int numQuizzes;
    @OneToOne
    @JoinColumn(name = "course_execution_id", unique = true)
    // we will get a database error due to the unique constraint violation.
    private CourseExecution courseExecution;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    public QuizStats(TeacherDashboard teacherDashboard, CourseExecution courseExecution)
    {
        // ToDo: check if I really need to do this on creation,or only after first update
        // int numQuizzes = courseExecution.getQuizzes().size();
        //if (numQuizzes < 0)throw new TutorException(ErrorMessage.INVALID_QUIZZES_NUMBER);
        // else setNumQuizzes(numQuizzes);
        setTeacherDashboard(teacherDashboard);
        setCourseExecution(courseExecution);
    }

    public void remove(){
        this.teacherDashboard.getQuizStats().remove(this);
        this.teacherDashboard=null;
        // Note: No need to remove from courseExecution given that it is a unidirectional association
    }

    public void update(){
        int calculation = courseExecution.getQuizzes().size();
        if (calculation < 0) throw new TutorException(ErrorMessage.INVALID_QUIZZES_NUMBER);
        else setNumQuizzes(calculation);

    }

    public Integer getId() {
        return id;
    }

    public int getNumQuizzes() {
        return numQuizzes;
    }

    public void setNumQuizzes(int numQuizzes) {
        this.numQuizzes = numQuizzes;
    }

    public TeacherDashboard getTeacherDashboard() {
        return teacherDashboard;
    }

    public void setTeacherDashboard(TeacherDashboard teacherDashboard){
        this.teacherDashboard=teacherDashboard;
        this.teacherDashboard.addQuizStats(this);
    }

    public CourseExecution getCourseExecution() {
        return courseExecution;
    }

    public void setCourseExecution(CourseExecution courseExecution){
        this.courseExecution=courseExecution;
    }

    public void accept(Visitor visitor){
        //only to generate XML
    }
    @Override
    public String toString() {
        return "QuizStats{" +
                "id=" + id +
                ", courseExecution=" + courseExecution +
                ", teacherDashboard=" + teacherDashboard +
                ", numQuizzes=" + numQuizzes +
                '}';
    }
}