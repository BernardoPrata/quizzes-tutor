package pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.teacherdashboard.domain.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class TeacherDashboardDto {
    private Integer id;

    private int[] numberOfStudents=new int[3], numberOfQuizzes=new int[3], numberOfQuestions=new int[3];
    private int[] numStudentsOver75perc=new int[3], uniqueQuizzesSolved=new int[3], uniqueQuestionsSolved=new int[3];
    private int[] numStudentsOver3quizes=new int[3]; private float[] averageSolvedQuizes=new float[3], averageSolvedCorrectQuestions=new float[3];

    public TeacherDashboardDto() {
    }

    public TeacherDashboardDto(TeacherDashboard teacherDashboard) {
        this.id = teacherDashboard.getId();

        List<StudentStats> studentsStats = teacherDashboard.getStudentsStats();
        List<QuizStats> quizStats = teacherDashboard.getQuizStats();
        List<QuestionStats> questionStats = teacherDashboard.getQuestionStats();

        for (int i = studentsStats.size() - 1, j = 0; i >= studentsStats.size() - 3 && i >= 0; i--, j++) {
            numberOfStudents[j] = studentsStats.get(i).getNumStudents();
            numStudentsOver75perc[j] = studentsStats.get(i).getNumStudentsWithMoreThan75PerCentCorrectAnswers();
            numStudentsOver3quizes[j] = studentsStats.get(i).getNumStudentsWithAtLeastThreeQuestionsAnswered();

            numberOfQuizzes[j] = quizStats.get(i).getNumQuizzes();
            uniqueQuizzesSolved[j] = quizStats.get(i).getTotalSolvedNumQuizzes();
            averageSolvedQuizes[j] = quizStats.get(i).getAverageSolvedNumQuizzes();

            numberOfQuestions[j] = questionStats.get(i).getNumAvailable();
            uniqueQuestionsSolved[j] = questionStats.get(i).getAnsweredQuestionUnique();
            averageSolvedCorrectQuestions[j] = questionStats.get(i).getAverageQuestionsAnswered();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public int[] getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int[] numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public int[] getNumberOfQuizzes() {
        return numberOfQuizzes;
    }

    public void setNumberOfQuizzes(int[] numberOfQuizzes) {
        this.numberOfQuizzes = numberOfQuizzes;
    }

    public int[] getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int[] numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public int[] getNumStudentsOver75perc() {
        return numStudentsOver75perc;
    }

    public void setNumStudentsOver75perc(int[] numStudentsOver75perc) {
        this.numStudentsOver75perc = numStudentsOver75perc;
    }

    public int[] getUniqueQuizzesSolved() {
        return uniqueQuizzesSolved;
    }

    public void setUniqueQuizzesSolved(int[] uniqueQuizzesSolved) {
        this.uniqueQuizzesSolved = uniqueQuizzesSolved;
    }

    public int[] getUniqueQuestionsSolved() {
        return uniqueQuestionsSolved;
    }

    public void setUniqueQuestionsSolved(int[] uniqueQuestionsSolved) {
        this.uniqueQuestionsSolved = uniqueQuestionsSolved;
    }

    public int[] getNumStudentsOver3quizes() {
        return numStudentsOver3quizes;
    }

    public void setNumStudentsOver3quizes(int[] numStudentsOver3quizes) {
        this.numStudentsOver3quizes = numStudentsOver3quizes;
    }

    public float[] getAverageSolvedQuizes() {
        return averageSolvedQuizes;
    }

    public void setAverageSolvedQuizes(float[] averageSolvedQuizes) {
        this.averageSolvedQuizes = averageSolvedQuizes;
    }

    public float[] getAverageSolvedCorrectQuestions() {
        return averageSolvedCorrectQuestions;
    }

    public void setAverageSolvedCorrectQuestions(float[] averageSolvedCorrectQuestions) {
        this.averageSolvedCorrectQuestions = averageSolvedCorrectQuestions;
    }

    @Override
    public String toString() {
        return "TeacherDashboardDto{" +
                "id=" + id +
                ", numberOfStudents=" + Arrays.toString(numberOfStudents) +
                ", numberOfQuizzes=" + Arrays.toString(numberOfQuizzes) +
                ", numberOfQuestions=" + Arrays.toString(numberOfQuestions) +
                ", numStudentsOver75perc=" + Arrays.toString(numStudentsOver75perc) +
                ", uniqueQuizzesSolved=" + Arrays.toString(uniqueQuizzesSolved) +
                ", uniqueQuestionsSolved=" + Arrays.toString(uniqueQuestionsSolved) +
                ", numStudentsOver3quizes=" + Arrays.toString(numStudentsOver3quizes) +
                ", averageSolvedQuizes=" + Arrays.toString(averageSolvedQuizes) +
                ", averageSolvedCorrectQuestions=" + Arrays.toString(averageSolvedCorrectQuestions) +
                '}';
    }

}
