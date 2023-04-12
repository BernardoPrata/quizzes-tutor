export default class TeacherDashboard {
  id!: number;
  numberOfStudents!: number;

  numStudentsOver3quizes!: number[];

  numStudentsOver75perc!: number[];

  executionYears!: number[];

  constructor(jsonObj?: TeacherDashboard) {
    if (jsonObj) {
      this.id = jsonObj.id;
      this.numberOfStudents = jsonObj.numberOfStudents;
      this.numStudentsOver3quizes = jsonObj.numStudentsOver3quizes;
      this.numStudentsOver75perc = jsonObj.numStudentsOver75perc;
      this.executionYears = jsonObj.executionYears;
    }

    this.executionYears = [2021]
    this.numberOfStudents = 15
    this.numStudentsOver3quizes = [5]
    this.numStudentsOver75perc = [13]
  }
}
