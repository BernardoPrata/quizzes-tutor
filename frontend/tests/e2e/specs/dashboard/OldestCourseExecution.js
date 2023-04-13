describe('Statistics testing', () => {
  let date;

  const teacherUsername = 'demo-teacher';
  const teacherName = 'Demo Teacher';
  const teacherEmail = 'demo_teacher@mail.com';
  const teacherRole = 'TEACHER';
  beforeEach(() => {
    cy.deleteWeeklyScores();
    cy.deleteFailedAnswers();
    cy.deleteDifficultQuestions();
    cy.cleanTestCoursesByAcademicTerm();

    cy.deleteQuestionsAndAnswers();
    cy.request('http://localhost:8080/auth/demo/teacher')
        .as('loginResponse')
        .then((response) => {
          Cypress.env('token', response.body.token);
          return response;
        }); 
  });

  it('tests if the oldest course execution statistics data is currently presented in dashboard', () => {
    date = new Date();
    
    let year = 2020;
    let academicYear = `Year ${year}/${year + 1}`;
    cy.createCourseExecutionOnDemoCourse(`TEST2022- ${academicYear}`);
    cy.changeDemoTeacherCourseExecutionMatchingAcademicTerm(`TEST2022- ${academicYear}`);
    cy.changeDemoStudentCourseExecutionMatchingAcademicTerm(`TEST2022- ${academicYear}`);

    const createStudent = (name, year) => cy.createStudentAddToCourseExecution(`TEST- ${name}`, `TEST- ${year}`);
    createStudent('Student 1', 'Year 2020/2021');
    createStudent('Student 2', 'Year 2020/2021');
    createStudent('Student 3', 'Year 2020/2021');
   
    cy.demoTeacherLogin();
    for (let i =0; i < 6;i++){
      cy.createQuestion(`Question ${i}`, 'Question', 'Option', 'Option', 'ChooseThisWrong', 'Correct');
    }
         
    cy.addQuizz(1,`Quizz 1 - ${academicYear}`,`TEST- ${academicYear}`,    date.toUTCString());
    
    cy.addQuestionToQuizz( 'Question 0','Quizz 1 - Year 2020/2021',1);
    cy.addQuestionToQuizz( 'Question 1','Quizz 1 - Year 2020/2021',2);
    cy.addQuestionToQuizz( 'Question 2','Quizz 2 - Year 2020/2021',1);
    cy.addQuestionToQuizz( 'Question 3','Quizz 2 - Year 2020/2021',2);
    cy.addQuestionToQuizz( 'Question 4','Quizz 3 - Year 2020/2021',1);
    cy.addQuestionToQuizz( 'Question 5','Quizz 3 - Year 2020/2021',2);
    cy.logout();

    cy.demoStudentLogin();
    cy.selectCourseByTerm('TEST- Year 2020/2021');
    cy.solveQuizz('Quizz 1 - Year 2020/2021',2);
    cy.solveQuizz('Quizz 2 - Year 2020/2021',2);
    cy.solveQuizz('Quizz 3 - Year 2020/2021',2, 'ChooseThisWrong');

    cy.logout();

    cy.demoTeacherLogin();
    cy.selectCourseByTerm('TEST- Year 2020/2021');
    cy.get('[data-cy="dashboardMenuButton"]').click();
    cy.wait(2000);
    /*cy.checkStats('totalStudents',3);
    cy.checkStats('studentsWithMoreThan75PerCentCorrectAnswers',0);
    cy.checkStats('averageSolvedCorrectQuestions',2);
    cy.checkStats('studentsWithMoreThanThreeAnsweredQuizzes',1);

    cy.checkStats('totalQuizzes', 3);
    cy.checkStats('uniqueQuizzesSolved',3);
    cy.checkStats('averageSolvedQuizes',2);

    cy.checkStats('totalQuestions', 6);
    cy.checkStats('uniqueQuestionsSolved',6);*/

    cy.get('.bar-chart canvas').eq(0).scrollIntoView().wait(5000).screenshot("./QuizStatsGraph")
    //cy.compareImages('QuizStatsGraphExpected.png', 'QuizStatsGraph.png');

    cy.wait(5000);
    cy.logout();

  });

  afterEach(() => {
    cy.deleteWeeklyScores();
    cy.deleteFailedAnswers();
    cy.deleteDifficultQuestions();
    cy.deleteQuestionsAndAnswers();
    cy.cleanTestCoursesByAcademicTerm();
  });

});
