package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;
import java.lang.AssertionError;
import java.lang.IllegalArgumentException;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class TeacherDashboardDto {
    private Integer id;

    private List<Integer> executionYears = new ArrayList<>();
    private List<Integer> numberOfStudents = new ArrayList<>();
    private List<Integer> numberOfQuizzes = new ArrayList<>();
    private List<Integer> numberOfQuestions = new ArrayList<>();
    private List<Integer> numStudentsOver75perc = new ArrayList<>();
    private List<Integer> uniqueQuizzesSolved = new ArrayList<>();
    private List<Integer> uniqueQuestionsSolved = new ArrayList<>();
    private List<Integer> numStudentsOver3quizes = new ArrayList<>();
    private List<Float> averageSolvedQuizes = new ArrayList<>();
    private List<Float> averageSolvedCorrectQuestions = new ArrayList<>();

    public TeacherDashboardDto() {
    }
    
    public TeacherDashboardDto(TeacherDashboard teacherDashboard) {
        this.id = teacherDashboard.getId();

        List<StudentStats> studentsStats = teacherDashboard.getStudentsStats();
        List<QuizStats> quizStats = teacherDashboard.getQuizStats();
        List<QuestionStats> questionStats = teacherDashboard.getQuestionStats();

        for (int i = 0; i < studentsStats.size() && i < 3; i++) {
            try {
                addExecutionYear(studentsStats.get(i).getCourseExecution().getYear());
            } catch (IllegalStateException e) {
                //false
            }
            addNumberOfStudents( studentsStats.get(i).getNumStudents());
            addNumStudentsOver75perc(studentsStats.get(i).getNumStudentsWithMoreThan75PerCentCorrectAnswers());
            addNumStudentsOver3quizes(studentsStats.get(i).getNumStudentsWithAtLeastThreeQuestionsAnswered());

            addNumberOfQuizzes(quizStats.get(i).getNumQuizzes());
            addUniqueQuizzesSolved( quizStats.get(i).getTotalSolvedNumQuizzes());
            addAverageSolvedQuizes( quizStats.get(i).getAverageSolvedNumQuizzes());

            addNumberOfQuestions(questionStats.get(i).getNumAvailable());
            addUniqueQuestionsSolved(questionStats.get(i).getAnsweredQuestionUnique());
            addAverageSolvedCorrectQuestions(questionStats.get(i).getAverageQuestionsAnswered());

        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getExecutionYears() {
        return executionYears;
    }

    public void addExecutionYear(Integer year) {
        this.executionYears.add(year);
    }

    public List<Integer> getNumberOfStudents() {
        return numberOfStudents;
    }

    public void addNumberOfStudents(Integer numberOfStudents) {
        this.numberOfStudents.add(numberOfStudents);
    }

    public List<Integer> getNumberOfQuizzes() {
        return numberOfQuizzes;
    }

    public void addNumberOfQuizzes(Integer numberOfQuizzes) {
        this.numberOfQuizzes.add(numberOfQuizzes);
    }

    public List<Integer> getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void addNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions.add(numberOfQuestions);
    }

    public List<Integer> getNumStudentsOver75perc() {
        return numStudentsOver75perc;
    }

    public void addNumStudentsOver75perc(Integer numStudentsOver75perc) {
        this.numStudentsOver75perc.add(numStudentsOver75perc);
    }

    public List<Integer> getUniqueQuizzesSolved() {
        return uniqueQuizzesSolved;
    }

    public void addUniqueQuizzesSolved(Integer uniqueQuizzesSolved) {
        this.uniqueQuizzesSolved.add(uniqueQuizzesSolved);
    }

    public List<Integer> getUniqueQuestionsSolved() {
        return uniqueQuestionsSolved;
    }

    public void addUniqueQuestionsSolved(Integer uniqueQuestionsSolved) {
        this.uniqueQuestionsSolved.add(uniqueQuestionsSolved);
    }

    public List<Integer> getNumStudentsOver3quizes() {
        return numStudentsOver3quizes;
    }

    public void addNumStudentsOver3quizes(Integer numStudentsOver3quizes) {
        this.numStudentsOver3quizes.add(numStudentsOver3quizes);
    }

    public List<Float> getAverageSolvedQuizes() {
        return averageSolvedQuizes;
    }

    public void addAverageSolvedQuizes(Float averageSolvedQuizes) {
        this.averageSolvedQuizes.add(averageSolvedQuizes);
    }

    public List<Float> getAverageSolvedCorrectQuestions() {
        return averageSolvedCorrectQuestions;
    }

    public void addAverageSolvedCorrectQuestions(Float averageSolvedCorrectQuestions) {
        this.averageSolvedCorrectQuestions.add(averageSolvedCorrectQuestions);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "TeacherDashboardDto{" +
                "id=" + id +
                ", executionYears=" + executionYears +
                ", numberOfStudents=" + numberOfStudents +
                ", numberOfQuizzes=" + numberOfQuizzes +
                ", numberOfQuestions=" + numberOfQuestions +
                ", numStudentsOver75perc=" + numStudentsOver75perc +
                ", uniqueQuizzesSolved=" + uniqueQuizzesSolved +
                ", uniqueQuestionsSolved=" + uniqueQuestionsSolved +
                ", numStudentsOver3quizes=" + numStudentsOver3quizes +
                ", averageSolvedQuizes=" + averageSolvedQuizes +
                ", averageSolvedCorrectQuestions=" + averageSolvedCorrectQuestions +
                '}';
    }
}
