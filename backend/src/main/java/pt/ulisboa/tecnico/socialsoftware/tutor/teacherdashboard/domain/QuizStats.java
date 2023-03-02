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

    private int totalSolvedNumQuizzes;



    @OneToOne
    @JoinColumn(name = "course_execution_id", unique = true)
    private CourseExecution courseExecution;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    public QuizStats(TeacherDashboard teacherDashboard, CourseExecution courseExecution)
    {
        setCourseExecution(courseExecution);        
        setTeacherDashboard(teacherDashboard);

    }

    public void remove(){
        this.teacherDashboard.getQuizStats().remove(this);
        this.teacherDashboard=null;
        // Note: No need to remove from courseExecution given that it is a unidirectional association
    }

    public void update() {
        int calculation = courseExecution.getQuizzes().size();
        if (calculation < 0) {
            throw new TutorException(ErrorMessage.INVALID_QUIZZES_NUMBER);
        } else {
            setNumQuizzes(calculation);
        }


        int totalUniqueQuizzes = (int) courseExecution.getQuizzes().stream()
                .filter(quiz -> !quiz.getQuizAnswers().isEmpty())
                .count();

        if (totalUniqueQuizzes > 0)
            setTotalSolvedNumQuizzes(totalUniqueQuizzes);
        else setTotalSolvedNumQuizzes(0);
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

    public int getTotalSolvedNumQuizzes(){
        return totalSolvedNumQuizzes;
    }
    public void setTotalSolvedNumQuizzes(int totalSolvedNumQuizzes ){
            this.totalSolvedNumQuizzes = totalSolvedNumQuizzes;
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
                ", totalSolvedNumQuizzes" + totalSolvedNumQuizzes +
                '}';
    }
}