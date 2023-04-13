export default class TeacherDashboard {
  id!: number;
  numberOfStudents!: number[];

  numStudentsOver3quizes!: number[];

  numStudentsOver75perc!: number[];

  numberOfQuizzes!: number[];

  uniqueQuizzesSolved!: number[];

  averageSolvedQuizes!: number[];

  numberOfQuestions!: number[];

  uniqueQuestionsSolved!: number[];

  averageSolvedCorrectQuestions!: number[];

  executionYears!: number[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.numberOfStudents = jsonObj.numberOfStudents;
      this.numStudentsOver3quizes = jsonObj.numStudentsOver3quizes;
      this.numStudentsOver75perc = jsonObj.numStudentsOver75perc;
      this.numberOfQuizzes = jsonObj.numberOfQuizzes;
      this.uniqueQuizzesSolved = jsonObj.uniqueQuizzesSolved;
      this.averageSolvedQuizes = jsonObj.averageSolvedQuizes;
      this.numberOfQuestions = jsonObj.numberOfQuestions;
      this.uniqueQuestionsSolved = jsonObj.uniqueQuestionsSolved;
      this.averageSolvedCorrectQuestions = jsonObj.averageSolvedCorrectQuestions;
      this.executionYears = jsonObj.executionYears;
    }
  }
}