describe('Statistics testing', () => {
  const teacherUsername = 'demo-teacher';
  const teacherName = 'Demo Teacher';
  const teacherEmail = 'demo_teacher@mail.com';
  const teacherRole = 'TEACHER';
  beforeEach(() => {
    cy.deleteQuestionsAndAnswers();
    cy.cleanTestCourses();
  });


  it('creates two course executions and two quizzes for each one of them', () => {
    // Create demo teacher
    cy.demoTeacherLogin();
    cy.logout();

    // Create course executions
    for (let year = 2020; year <= 2021; year++) {
      const academicYear = `Year ${year}/${year + 1}`;

      // Create course execution and demo teacher
      cy.createCourseExecutionOnDemoCourse(`TEST- ${academicYear}`);
      cy.changeDemoTeacherCourseExecutionMatchingAcademicTerm(`TEST- ${academicYear}`);
    }
    // Create quizzes
    cy.demoTeacherLogin();
    let j=0;
    for (let year = 2020; year <= 2021; year++) {
      const academicYear = `Year ${year}/${year + 1}`;
      cy.selectCourseByTerm(`TEST- ${academicYear}`);
      cy.createQuestion(`Question ${j}`, 'Question', 'Option', 'Option', 'ChooseThisWrong', 'Correct');
      cy.createQuestion(`Question ${j+1}`, 'Question', 'Option', 'Option', 'ChooseThisWrong', 'Correct');
      cy.createQuizzWith2Questions(`Quiz ${j} - ${academicYear}`, `Question ${j}`, `Question ${j+1}`);
      j+=2;

    }
    //cy.selectCourseByTerm('TEST- Year 2022/2023');
    //cy.get('[data-cy="dashboardMenuButton"]').click();
    // TODO: add everything prior to do before each test
    // TODO: check current dashboard and compare current year presented and graphs

    // TODO: delete beforeEach and afterEach only needs to delete created quizzes and course (not all)
    cy.logout();
  });
  //after each
  afterEach(() => {
    cy.deleteQuestionsAndAnswers();
    cy.cleanTestCourses();
  });

});