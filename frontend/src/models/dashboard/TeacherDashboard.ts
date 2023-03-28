export default class TeacherDashboard {
  id!: number;
  numberOfStudents!: number;

  numberOfQuizzes!: number[];

  uniqueQuizzesSolved!: number[];

  averageSolvedQuizes!: number[];
  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.numberOfStudents = jsonObj.numberOfStudents;
      this.numberOfQuizzes = jsonObj.numberOfQuizzes;
      this.uniqueQuizzesSolved = jsonObj.uniqueQuizzesSolved;
      this.averageSolvedQuizes = jsonObj.averageSolvedQuizes;
    }
  }
}
