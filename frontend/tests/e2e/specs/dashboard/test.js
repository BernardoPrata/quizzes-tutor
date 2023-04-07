describe('Statistics testing', () => {
  const teacherUsername = 'demo-teacher';
  const teacherName = 'Demo Teacher';
  const teacherEmail = 'demo_teacher@mail.com';
  const teacherRole = 'TEACHER';
  beforeEach(() => {
    cy.deleteQuestionsAndAnswers();
    cy.request('http://localhost:8080/auth/demo/teacher')
        .as('loginResponse')
        .then((response) => {
          Cypress.env('token', response.body.token);
          return response;
        });
  });


  it('creates two course executions and two quizzes for each one of them', () => {
    // Create demo teacher
    // Login as demo teacher


    // Create course executions
    for (let year = 2020; year <= 2022; year++) {
      const academicYear = `Year ${year}/${year + 1}`;

      // Create course execution and demo teacher
      cy.createCourseExecutionOnDemoCourse(`TEST- ${academicYear}`);
      cy.changeDemoTeacherCourseExecutionMatchingAcademicTerm(`TEST- ${academicYear}`);
    }
    // Create quizzes
    cy.demoTeacherLogin();
    let j=0;
    for (let year = 2020; year <= 2022; year++) {
      const academicYear = `Year ${year}/${year + 1}`;
      cy.selectCourseByTerm(`TEST- ${academicYear}`);
      cy.createQuestion(`Question ${j}`, 'Question', 'Option', 'Option', 'ChooseThisWrong', 'Correct');
      cy.createQuestion(`Question ${j+1}`, 'Question', 'Option', 'Option', 'ChooseThisWrong', 'Correct');

      cy.createQuizzWith2Questions(`Quiz 1 - ${academicYear}`, `Question ${j}`, `Question ${j+1}`);
      if (j>=2) {
        cy.createOtherQuizzWith2Questions(`Quiz 2 - ${academicYear}`, `Question ${j-2}`, `Question ${j-1}`);
      }
      if (j>=4) {
        cy.createOtherQuizzWith2Questions(`Quiz 3 - ${academicYear}`, `Question ${j-4}`, `Question ${j-3}`);
        }
      j+=2;

    }



    //cy.selectCourseByTerm('TEST- Year 2022/2023');
    //cy.get('[data-cy="dashboardMenuButton"]').click();
    // TODO: add everything prior to do before each test
    // TODO: check current dashboard and compare current year presented and graphs

    // TODO: delete beforeEach and afterEach only needs to delete created quizzes and course (not all)

    // TODO: in commands, collapse createQuizzWith2Questions and createOtherQuizzWith2Questions into one function
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