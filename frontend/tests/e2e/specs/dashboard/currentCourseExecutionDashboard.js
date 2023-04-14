describe('Statistics testing', () => {
  let date;

  beforeEach(() => {
      cy.deleteWeeklyScores();
      cy.deleteFailedAnswers();
      cy.deleteDifficultQuestions();
      cy.deleteQuestionsAndAnswers();
      cy.cleanTestCoursesByAcademicTerm();
      cy.deleteTestUsers();
      cy.request('http://localhost:8080/auth/demo/teacher')
        .as('loginResponse')
        .then((response) => {
          Cypress.env('token', response.body.token);
          return response;
        });
      cy.demoStudentLogin();
      cy.logout();

  });


  it('tests if current course executions statistics data is currently presented in dashboard', () => {
    date = new Date();

    for (let year = 2020; year <=2022; year++) {
      const academicYear = `Year ${year}/${year + 1}`;
      cy.createCourseExecutionOnDemoCourse(`TESTS2022- ${academicYear}`);
      cy.changeDemoTeacherCourseExecutionMatchingAcademicTerm(`TESTS2022- ${academicYear}`);
      cy.changeDemoStudentCourseExecutionMatchingAcademicTerm(`TESTS2022- ${academicYear}`);
    }
    const createStudent = (name, year) => cy.createStudentAddToCourseExecution(`TESTS2022- ${name}`, `TESTS2022- ${year}`);
    createStudent('Student 1', 'Year 2022/2023');
    createStudent('Student 2', 'Year 2022/2023');
    createStudent('Student 3', 'Year 2021/2022');
    createStudent('Student 4', 'Year 2021/2022');
    createStudent('Student 5', 'Year 2021/2022');
    createStudent('Student 6', 'Year 2020/2021');
    createStudent('Student 7', 'Year 2020/2021');
    createStudent('Student 8', 'Year 2020/2021');


    cy.demoTeacherLogin();
    for (let i =0; i < 6;i++){
      cy.createQuestion(`Question ${i}`, 'Question', 'Option', 'Option', 'ChooseThisWrong', 'Correct');
    }

    let j=0;
    for (let year = 2020; year <= 2022; year++) {
        const academicYear = `Year ${year}/${year + 1}`;
        cy.addQuizz(1,`Quizz 1 - ${academicYear}`,`TESTS2022- ${academicYear}`,    date.toUTCString());
        if (j > 0)
            cy.addQuizz(2,`Quizz 2 - ${academicYear}`,`TESTS2022- ${academicYear}`,    date.toUTCString());
        if (j > 1)
            cy.addQuizz(3,`Quizz 3 - ${academicYear}`,`TESTS2022- ${academicYear}`,    date.toUTCString());
        j++;
    }

    cy.addQuestionToQuizz( 'Question 0','Quizz 1 - Year 2022/2023',1);
    cy.addQuestionToQuizz( 'Question 1','Quizz 1 - Year 2022/2023',2);
    cy.addQuestionToQuizz( 'Question 2','Quizz 2 - Year 2022/2023',1);
    cy.addQuestionToQuizz( 'Question 3','Quizz 2 - Year 2022/2023',2);
    cy.addQuestionToQuizz( 'Question 4','Quizz 3 - Year 2022/2023',1);
    cy.addQuestionToQuizz( 'Question 5','Quizz 3 - Year 2022/2023',2);
    cy.addQuestionToQuizz( 'Question 0','Quizz 1 - Year 2021/2022',1);
    cy.addQuestionToQuizz( 'Question 1','Quizz 1 - Year 2021/2022',2);
    cy.addQuestionToQuizz( 'Question 2','Quizz 2 - Year 2021/2022',1);
    cy.addQuestionToQuizz( 'Question 0','Quizz 1 - Year 2020/2021',1);
    cy.addQuestionToQuizz( 'Question 1','Quizz 1 - Year 2020/2021',2);
    cy.logout();

    cy.demoStudentLogin();
    cy.selectCourseByTerm('TESTS2022- Year 2022/2023');
    cy.solveQuizz('Quizz 1 - Year 2022/2023',2);
    cy.solveQuizz('Quizz 2 - Year 2022/2023',2);
    cy.solveQuizz('Quizz 3 - Year 2022/2023',2, 'ChooseThisWrong');
    cy.selectCourseByTerm('TESTS2022- Year 2021/2022');
    cy.solveQuizz('Quizz 1 - Year 2021/2022',2);
    cy.selectCourseByTerm('TESTS2022- Year 2020/2021');
    cy.solveQuizz('Quizz 1 - Year 2020/2021',2);
    cy.logout();

    cy.demoTeacherLogin();
    cy.selectCourseByTerm('TESTS2022- Year 2022/2023');
    cy.get('[data-cy="dashboardMenuButton"]').click();
    cy.wait(2000);
    cy.checkStats('totalStudents',3);
    cy.checkStats('studentsWithMoreThan75PerCentCorrectAnswers',0);
    cy.checkStats('studentsWithMoreThanThreeAnsweredQuizzes',1);

    cy.checkStats('totalQuizzes', 3);
    cy.checkStats('uniqueQuizzesSolved',3);
    cy.checkStats('averageSolvedQuizes',2);

    cy.checkStats('totalQuestions', 6);
    cy.checkStats('uniqueQuestionsSolved',6);
    cy.checkStats('averageSolvedCorrectQuestions',2);

    cy.get('.bar-chart canvas').eq(0).scrollIntoView().wait(5000).screenshot("QuizStatsGraph")
        .then(() => {
          cy.compareImages('QuizStatsGraphExpected.png', 'QuizStatsGraph.png');
        })
    cy.get('.bar-chart canvas').eq(1).scrollIntoView().wait(5000).screenshot("StudentStatsGraph").then(() => {
        cy.compareImages('StudentStatsGraphExpected.png', 'StudentStatsGraph.png');
    })

    cy.get('.bar-chart canvas').eq(2).scrollIntoView().wait(5000).screenshot("QuestionStatsGraph")
        .then(() => {
            cy.compareImages('QuestionStatsGraphExpected.png', 'QuestionStatsGraph.png');

        })

    cy.logout();

  });


  afterEach(() => {
    cy.deleteWeeklyScores();
    cy.deleteFailedAnswers();
    cy.deleteDifficultQuestions();
    cy.deleteQuestionsAndAnswers();
    cy.cleanTestCoursesByAcademicTerm();
    cy.deleteTestUsers();
  });

});