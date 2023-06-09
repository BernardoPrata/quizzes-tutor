package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.DomainEntity;
import pt.ulisboa.tecnico.socialsoftware.tutor.impexp.domain.Visitor;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;

import javax.persistence.*;



@Entity
public class StudentStats implements DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int numStudents;
    private int numStudentsWithMoreThan75PerCentCorrectAnswers;
    private int numStudentsWithAtLeastThreeQuestionsAnswered;
    @ManyToOne
    @JoinColumn(name = "course_execution_id")
    private CourseExecution courseExecution;

    @ManyToOne
    private TeacherDashboard teacherDashboard;

    public StudentStats() {
    }

    public StudentStats(CourseExecution courseExecution, TeacherDashboard teacherDashboard) {
        setCourseExecution(courseExecution);
        setTeacherDashboard(teacherDashboard);
    }

    public void remove() {
        this.teacherDashboard.getStudentsStats().remove(this);
        this.teacherDashboard = null;
    }

    public void update(){
        int calculation = courseExecution.getStudents().size();
        if (calculation < 0) throw new TutorException(ErrorMessage.INVALID_STUDENT_STATS);
        else setNumStudents(calculation);

        calculation = 0;
        for (var student : courseExecution.getStudents()) {
            int numberOfCorrectAnswers = 0;
            int numberOfAnsweredQuestions = 0;

            for (var quizAnswer : student.getQuizAnswers()) {
                numberOfCorrectAnswers += quizAnswer.getNumberOfCorrectAnswers();
                numberOfAnsweredQuestions += quizAnswer.getNumberOfAnsweredQuestions();
            }

            if (numberOfCorrectAnswers > 0.75 * numberOfAnsweredQuestions) {
                calculation++;
            }
        }

        if (calculation < 0) throw new TutorException(ErrorMessage.INVALID_STUDENT_STATS);
        else setNumStudentsWithMoreThan75PerCentCorrectAnswers(calculation);

        calculation = 0;
        for (var student : courseExecution.getStudents()) {
            int numberOfAnsweredQuestions = 0;

            for (var quizAnswer : student.getQuizAnswers()) {
                numberOfAnsweredQuestions += quizAnswer.getNumberOfAnsweredQuestions();
            }

            if (numberOfAnsweredQuestions >= 3) {
                calculation++;
            }
        }

        if (calculation < 0) throw new TutorException(ErrorMessage.INVALID_STUDENT_STATS);
        else setNumStudentsWithAtLeastThreeQuestionsAnswered(calculation);
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

    public TeacherDashboard getTeacherDashboard() {
        return teacherDashboard;
    }

    public void setTeacherDashboard(TeacherDashboard teacherDashboard) {
        this.teacherDashboard = teacherDashboard;
        if (teacherDashboard != null) {
            this.teacherDashboard.addStudentStats(this);
        }
    }

    public int getNumStudents() {
        return numStudents;
    }

    public void setNumStudents(int numStudents) {
        this.numStudents = numStudents;
    }

    public int getNumStudentsWithMoreThan75PerCentCorrectAnswers() {
        return numStudentsWithMoreThan75PerCentCorrectAnswers;
    }

    public void setNumStudentsWithMoreThan75PerCentCorrectAnswers(int numStudentsWithMoreThan75PerCentCorrectAnswers) {
        this.numStudentsWithMoreThan75PerCentCorrectAnswers = numStudentsWithMoreThan75PerCentCorrectAnswers;
    }

    public int getNumStudentsWithAtLeastThreeQuestionsAnswered() {
        return numStudentsWithAtLeastThreeQuestionsAnswered;
    }

    public void setNumStudentsWithAtLeastThreeQuestionsAnswered(int numStudentsWithAtLeastThreeQuestionsAnswered) {
        this.numStudentsWithAtLeastThreeQuestionsAnswered = numStudentsWithAtLeastThreeQuestionsAnswered;
    }

    public void accept(Visitor visitor) {
        // Only used for XML generation
    }

    @Override
    public String toString() {
        return "StudentStats{" +
                "id=" + id +
                ", courseExecution=" + courseExecution +
                ", teacherDashboard=" + teacherDashboard +
                ", numStudents=" + numStudents +
                ", numStudentsWithMoreThan75PerCentCorrectAnswers=" + numStudentsWithMoreThan75PerCentCorrectAnswers +
                ", numStudentsWithAtLeastThreeQuestionsAnswered=" + numStudentsWithAtLeastThreeQuestionsAnswered +
                '}';
    }

}
