<template>
  <div class="container">
    <h2>Statistics for this course execution</h2>
    <div v-if="teacherDashboard != null" class="stats-container">
      <div class="items">
        <div ref="totalStudents" class="icon-wrapper">
          <animated-number :number="teacherDashboard.numberOfStudents" />
        </div>
        <div class="project-name">
          <p>Number of Students</p>
        </div>
      </div>
      <div class="items">
        <div ref="studentsWithMoreThan75PerCentCorrectAnswers" class="icon-wrapper">
          <animated-number :number="teacherDashboard.numStudentsOver75perc" />
        </div>
        <div class="project-name">
          <p>Number of Students who Solved >= 75% Questions</p>
        </div>
      </div>
      <div class="items">
        <div ref="studentsWithMoreThanThreeAnsweredQuizzes" class="icon-wrapper">
          <animated-number :number="teacherDashboard.numStudentsOver3quizes" />
        </div>
        <div class="project-name">
          <p>Number of Students who Solved >= 3 Quizzes</p>
        </div>
      </div>

      <!-- Div to display the number of quizzes -->
      <div class="items">
        <div ref="totalQuizzes" class="icon-wrapper">
          <animated-number :number="teacherDashboard.numberOfQuizzes[0]" />
        </div>
        <div class="project-name">
          <p>Number of Quizzes</p>
        </div>
      </div>

      <!-- Div to display the number of quizzes solved (unique) -->
      <div class="items">
        <div ref="uniqueQuizzesSolved" class="icon-wrapper">
          <animated-number :number="teacherDashboard.uniqueQuizzesSolved[0]" />
        </div>
        <div class="project-name">
          <p>Number of Quizzes Solved (Unique)</p>
        </div>
      </div>

      <!-- Div to display the number of average quizzes solved -->
      <div class="items">
        <div ref="averageSolvedQuizes" class="icon-wrapper">
          <animated-number :number="teacherDashboard.averageSolvedQuizes[0]" />
        </div>
        <div class="project-name">
          <p>Number of Quizzes Solved (Unique, Average per student)</p>
        </div>
      </div>
    </div>
    <h2>Comparison with previous course executions</h2>

    <div v-if="teacherDashboard != null" class="stats-container">
      <!-- Div to display the statistics about quizzes -->
      <div class="bar-chart">
        <Bar :chartData="quizzesData" :chartOptions="options" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import AnimatedNumber from '@/components/AnimatedNumber.vue';
import TeacherDashboard from '@/models/dashboard/TeacherDashboard';
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from 'chart.js';
import { Bar } from 'vue-chartjs/legacy';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

@Component({
  components: { AnimatedNumber, Bar },
})
export default class TeacherStatsView extends Vue {
  @Prop() readonly dashboardId!: number;
  teacherDashboard: TeacherDashboard | null = null;
  quizzesData: Object = {};
  options: Object = {};

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.teacherDashboard = await RemoteServices.getTeacherDashboard();
      this.options = {
        responsive: true,
        maintainAspectRatio: false,
      };
      this.quizzesData = {
        labels: [
          this.teacherDashboard?.executionYears[2]
            ? this.teacherDashboard?.executionYears[2]
            : ' ',
          this.teacherDashboard?.executionYears[1]
            ? this.teacherDashboard?.executionYears[1]
            : ' ',
          this.teacherDashboard?.executionYears[0] + ' (current)',
        ],
        datasets: [
          {
            label: 'Quizzes: Total Available',
            backgroundColor: '#b14434',
            data: [
              this.teacherDashboard?.numberOfQuizzes[2],
              this.teacherDashboard?.numberOfQuizzes[1],
              this.teacherDashboard?.numberOfQuizzes[0],
            ],
          },
          {
            label: 'Quizzes: Solved (Unique)',
            backgroundColor: '#437eb4',
            data: [
              this.teacherDashboard?.uniqueQuizzesSolved[2],
              this.teacherDashboard?.uniqueQuizzesSolved[1],
              this.teacherDashboard?.uniqueQuizzesSolved[0],
            ],
          },
          {
            label: 'Quizzes: Solved (Unique, Average per student)',
            backgroundColor: '#58b99d',
            data: [
              this.teacherDashboard?.averageSolvedQuizes[2],
              this.teacherDashboard?.averageSolvedQuizes[1],
              this.teacherDashboard?.averageSolvedQuizes[0],
            ],
          },
        ],
      };
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }
}
</script>

<style lang="scss" scoped>
.stats-container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  align-items: stretch;
  align-content: center;
  height: 100%;

  .items {
    background-color: rgba(255, 255, 255, 0.75);
    color: #1976d2;
    border-radius: 5px;
    flex-basis: 25%;
    margin: 20px;
    cursor: pointer;
    transition: all 0.6s;
  }

  .bar-chart {
    background-color: rgba(255, 255, 255, 0.9);
    height: 400px;
    width: 800px;
    margin: 30px;
  }
}

.icon-wrapper,
.project-name {
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-wrapper {
  font-size: 100px;
  transform: translateY(0px);
  transition: all 0.6s;
}

.icon-wrapper {
  align-self: end;
}

.project-name {
  align-self: start;
}

.project-name p {
  font-size: 20px;
  font-weight: bold;
  letter-spacing: 2px;
  transform: translateY(0px);
  transition: all 0.5s;
}

.items:hover {
  border: 3px solid black;

  & .project-name p {
    transform: translateY(-10px);
  }

  & .icon-wrapper i {
    transform: translateY(5px);
  }
}
</style>
