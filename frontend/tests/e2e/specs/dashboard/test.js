describe('Statistics testing', () => {
  const teacherUsername = 'demo-teacher';
  const teacherName = 'Demo Teacher';
  const teacherEmail = 'demo_teacher@mail.com';
  const teacherRole = 'TEACHER';
  beforeEach(() => {
    cy.deleteQuestionsAndAnswers();
    cy.cleanTestCourses();
    cy.demoAdminLogin();
    cy.get('[data-cy="administrationMenuButton"]').click();
    cy.get('[data-cy="manageCoursesMenuButton"]').click({force: true});
  });

  it('creates two course executions and two quizzes for each one of them', () => {
    // Create "Year 2020/2021" course execution and a demo teacher
    cy.createCourseExecution('Demo Course', 'TEST- Year 2020/2021', 'Spring Semester');
    cy.addUserThroughForm('TEST- Year 2020/2021', teacherName, teacherUsername, teacherEmail, teacherRole);
    cy.closeUserCreationDialog();

    // Create "Year 2021/2022" course execution and a demo teacher
    cy.createCourseExecution('Demo Course', 'TEST- Year 2021/2022', 'Spring Semester');
    cy.addUserThroughForm('TEST- Year 2021/2022', teacherName, teacherUsername, teacherEmail, teacherRole);
    cy.closeUserCreationDialog();
    cy.logout()

    // Create two quizzes for "Year 2021/2022"
    cy.demoTeacherLogin();
    cy.changeCourse('TEST- Year 2020/2021');

    cy.createQuestion(
      'Question 1',
      'Question',
      'Option',
      'Option',
      'ChooseThisWrong',
      'Correct'
    );
    cy.createQuestion(
      'Question 2',
      'Question',
      'Option',
      'Option',
      'ChooseThisWrong',
      'Correct'
    );
    cy.createQuizzWith2Questions('Quiz 1 - Year 2020/2021', 'Question 1', 'Question 2');

    cy.changeCourse('TEST- Year 2021/2022');
    cy.createQuestion(
      'Question 3',
      'Question',
      'Option',
      'Option',
      'ChooseThisWrong',
      'Correct'
    );
    cy.createQuestion(
      'Question 4',
      'Question',
      'Option',
      'Option',
      'ChooseThisWrong',
      'Correct'
    );
    cy.createQuizzWith2Questions('Quiz 1 - Year 2021/2022', 'Question 3', 'Question 4');

    cy.logout();
  });

  afterEach(() => {
    cy.deleteQuestionsAndAnswers();
    cy.cleanTestCourses();
  });
});
