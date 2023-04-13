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

    this.executionYears = [2021, 2020, 2019];
    this.numberOfQuestions = [600, 500, 400];
    this.uniqueQuestionsSolved = [400, 250, 300];
    this.averageSolvedCorrectQuestions = [300, 150, 100];
  }
}
