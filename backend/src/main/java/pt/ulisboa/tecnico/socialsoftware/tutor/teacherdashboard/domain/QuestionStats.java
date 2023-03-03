package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;


@Entity
public class QuestionStats implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int numAvailable;

    @OneToOne
    @JoinColumn(name = "course_execution_id", unique = true)
    private CourseExecution courseExecution;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    public QuestionStats() {
    }

    public QuestionStats(CourseExecution courseExecution, TeacherDashboard teacherDashboard) {
        setCourseExecution(courseExecution);
        setTeacherDashboard(teacherDashboard);
        // setNumAvailable(courseExecution);
    }

    public void remove() {
        this.teacherDashboard.getQuestionStats().remove(this);
        this.teacherDashboard = null;
    }

    public void update() {
        int calculation = 0;
        for (Question question: courseExecution.getCourse().getQuestions()) {
            if (question.getStatus() == Question.Status.AVAILABLE) {
                calculation += 1;
            }
        }
        //int calculation = courseExecution.getNumberOfQuestions();
        // int calculation = courseExecution.getQuestionSubmissions().size();
        if (calculation < 0) throw new TutorException(ErrorMessage.INVALID_QUESTIONS_NUMBER);
        else setNumAvailable(calculation);
    }

    public Integer getId() { return id; }

    public int getNumAvailable() { return this.numAvailable; }

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }

    public CourseExecution getCourseExecution() { return courseExecution; }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public TeacherDashboard getTeacherDashboard() { return teacherDashboard; }

    public void setTeacherDashboard(TeacherDashboard teacherDashboard) {
        this.teacherDashboard = teacherDashboard;
        this.teacherDashboard.addQuestionStats(this);
    }

    public void accept(Visitor visitor) {
        // Only to generate XML
    }

    @Override
    public String toString() {
        return "QuestionStats{" +
                "id=" + id +
                ", numAvailable=" + numAvailable +
                ", courseExecution=" + courseExecution +
                ", teacherDashboard=" + teacherDashboard +
                '}';
    }
}