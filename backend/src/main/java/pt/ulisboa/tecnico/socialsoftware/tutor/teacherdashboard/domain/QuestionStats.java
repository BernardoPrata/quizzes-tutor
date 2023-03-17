package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.execution.domain.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
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

    private float averageQuestionsAnswered;

    private int answeredQuestionUnique;

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
        if (calculation < 0) throw new TutorException(ErrorMessage.INVALID_QUESTIONS_NUMBER);
        else setNumAvailable(calculation);

        int unique_questions = 0;
        Set<QuizQuestion> questionsSet = new HashSet<>();

        courseExecution.getQuizzes()
                .forEach(quiz -> quiz.getQuizQuestions()
                        .forEach(quizQuestion -> questionsSet.add(quizQuestion))
                );

        for (QuizQuestion question: questionsSet) {
            if (question.getQuestionAnswers().size() > 0) {
                unique_questions += 1;
            }
        }

        int numStudents = courseExecution.getStudents().size();
        float average = (numStudents > 0 && unique_questions > 0) ? (float)unique_questions / numStudents : 0;


        if (calculation < 0) throw new TutorException(ErrorMessage.INVALID_QUESTIONS_NUMBER);
        else setNumAvailable(calculation);

        if (average < 0) throw new TutorException(ErrorMessage.INVALID_AVERAGE_UNIQUE_QUESTIONS_ANSWERED);
        else setAverageQuestionsAnswered(average);

        if (unique_questions < 0) throw new TutorException(ErrorMessage.INVALID_NUMBER_OF_UNIQUE_ANSWERED_QUESTIONS);
        setAnsweredQuestionUnique(unique_questions);
    }

    public Integer getId() { return id; }

    public int getNumAvailable() { return this.numAvailable; }

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }

    public int getAnsweredQuestionUnique(){ return this.answeredQuestionUnique; }

    public float getAverageQuestionsAnswered() { return this.averageQuestionsAnswered; }

    public void setAverageQuestionsAnswered(float averageQuestionsAnswered) {
        this.averageQuestionsAnswered = averageQuestionsAnswered;
    }

    public void setAnsweredQuestionUnique(int answeredQuestionUnique) {
        this.answeredQuestionUnique = answeredQuestionUnique;
    }

    public CourseExecution getCourseExecution() { return courseExecution; }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
    }

    public TeacherDashboard getTeacherDashboard() { return teacherDashboard; }

    public void setTeacherDashboard(TeacherDashboard teacherDashboard) {
        this.teacherDashboard = teacherDashboard;
        if (teacherDashboard != null) {
            this.teacherDashboard.addQuestionStats(this);
        }
    }

    public void accept(Visitor visitor) {
        // Only to generate XML
    }

    @Override
    public String toString() {
        return "QuestionStats{" +
                "id=" + id +
                ", answeredQuestionUnique=" + answeredQuestionUnique +
                ", numAvailable=" + numAvailable +
                ", averageQuestionsAnswered=" + averageQuestionsAnswered +
                ", courseExecution=" + courseExecution +
                ", teacherDashboard=" + teacherDashboard +
                '}';
    }
}