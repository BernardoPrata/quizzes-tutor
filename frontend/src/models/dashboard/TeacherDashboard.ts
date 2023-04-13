export default class TeacherDashboard {
  id!: number;
  numberOfStudents!: number;
  numberOfQuestions!: number[];

  uniqueQuestionsSolved!: number[];

  averageSolvedCorrectQuestions!: number[];

  executionYears!: number[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.numberOfStudents = jsonObj.numberOfStudents;
      this.numberOfQuestions = jsonObj.numberOfQuestions;
      this.uniqueQuestionsSolved = jsonObj.uniqueQuestionsSolved;
      this.averageSolvedCorrectQuestions = jsonObj.averageSolvedCorrectQuestions;
      this.executionYears = jsonObj.executionYears;
    }
  }
}
