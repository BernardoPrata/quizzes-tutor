package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

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

        for (int i = studentsStats.size() - 1; i >= studentsStats.size() - 3 && i >= 0; i--) {
            try {
                executionYears.add(studentsStats.get(i).getCourseExecution().getYear());
            } catch (IllegalStateException e) {
                //false
            }



            numberOfStudents.add( studentsStats.get(i).getNumStudents());
            numStudentsOver75perc.add(studentsStats.get(i).getNumStudentsWithMoreThan75PerCentCorrectAnswers());
            numStudentsOver3quizes.add(studentsStats.get(i).getNumStudentsWithAtLeastThreeQuestionsAnswered());

            numberOfQuizzes.add(quizStats.get(i).getNumQuizzes());
            uniqueQuizzesSolved.add( quizStats.get(i).getTotalSolvedNumQuizzes());
            averageSolvedQuizes.add( quizStats.get(i).getAverageSolvedNumQuizzes());

            numberOfQuestions.add(questionStats.get(i).getNumAvailable());
            uniqueQuestionsSolved.add( questionStats.get(i).getAnsweredQuestionUnique());
            averageSolvedCorrectQuestions.add(questionStats.get(i).getAverageQuestionsAnswered());
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

    public void setExecutionYears(List<Integer> executionYears) {
        this.executionYears = executionYears;
    }

    public List<Integer> getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(List<Integer> numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public List<Integer> getNumberOfQuizzes() {
        return numberOfQuizzes;
    }

    public void setNumberOfQuizzes(List<Integer> numberOfQuizzes) {
        this.numberOfQuizzes = numberOfQuizzes;
    }

    public List<Integer> getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(List<Integer> numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public List<Integer> getNumStudentsOver75perc() {
        return numStudentsOver75perc;
    }

    public void setNumStudentsOver75perc(List<Integer> numStudentsOver75perc) {
        this.numStudentsOver75perc = numStudentsOver75perc;
    }

    public List<Integer> getUniqueQuizzesSolved() {
        return uniqueQuizzesSolved;
    }

    public void setUniqueQuizzesSolved(List<Integer> uniqueQuizzesSolved) {
        this.uniqueQuizzesSolved = uniqueQuizzesSolved;
    }

    public List<Integer> getUniqueQuestionsSolved() {
        return uniqueQuestionsSolved;
    }

    public void setUniqueQuestionsSolved(List<Integer> uniqueQuestionsSolved) {
        this.uniqueQuestionsSolved = uniqueQuestionsSolved;
    }

    public List<Integer> getNumStudentsOver3quizes() {
        return numStudentsOver3quizes;
    }

    public void setNumStudentsOver3quizes(List<Integer> numStudentsOver3quizes) {
        this.numStudentsOver3quizes = numStudentsOver3quizes;
    }

    public List<Float> getAverageSolvedQuizes() {
        return averageSolvedQuizes;
    }

    public void setAverageSolvedQuizes(List<Float> averageSolvedQuizes) {
        this.averageSolvedQuizes = averageSolvedQuizes;
    }

    public List<Float> getAverageSolvedCorrectQuestions() {
        return averageSolvedCorrectQuestions;
    }

    public void setAverageSolvedCorrectQuestions(List<Float> averageSolvedCorrectQuestions) {
        this.averageSolvedCorrectQuestions = averageSolvedCorrectQuestions;
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
