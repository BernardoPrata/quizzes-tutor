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
    </div>

    <h2>Comparison with previous course executions</h2>

    <div v-if="teacherDashboard != null" class="stats-container">
      <!-- Div to display the students statistics about students -->
      <div class="bar-chart">
        <bar-chart :data="studentsData" /> 
      </div>
    </div>
  </div>
</template>  //npm i -D @types/chart.js

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import RemoteServices from '@/services/RemoteServices';
import AnimatedNumber from '@/components/AnimatedNumber.vue';
import BarChart from '@/components/charts/BarChart.vue';
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
import {types} from 'sass';
import String = types.String;

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

@Component({
  components: { BarChart, AnimatedNumber, Bar },
})

export default class TeacherStatsView extends Vue {
  @Prop() readonly dashboardId!: number;
  teacherDashboard: TeacherDashboard | null = null;
  studentsData: Object = {};
  options: Object = {};


  async created() {
    await this.$store.dispatch('loading');
    try {
      this.teacherDashboard = await RemoteServices.getTeacherDashboard();
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }
  
  extractStudentsData(tDb: TeacherDashboard) { 
    let studentsData: { labels: object; datasets: object } = {
      labels: [],
      datasets: [],
    };
  
    studentsData.labels = [
      tDb?.executionYears[2] ? tDb?.executionYears[2] : ' ',
      tDb?.executionYears[1] ? tDb?.executionYears[1] : ' ',
      tDb?.executionYears[0] ? tDb.executionYears[0] + ' (current)' : 'current',
    ];
    studentsData.datasets = [
      {
        label: 'Total Number of Students',
        backgroundColor: '#b14434',
        data: [
          tDb?.numberOfStudents[2],
          tDb?.numberOfStudents[1],
          tDb?.numberOfStudents[0],
        ],
      },
      {
        label: 'Students who Solved >= 75% of Questions',
        backgroundColor: '#437eb4',
        data: [
          tDb?.numStudentsOver75perc[2],
          tDb?.numStudentsOver75perc[1],
          tDb?.numStudentsOver75perc[0],
        ], 
      },
      {
        label: 'Students who Solved >= 3 Quizzes',
        backgroundColor: '#58b99d',
        data: [
          tDb?.numStudentsOver3quizes[2],
          tDb?.numStudentsOver3quizes[1],
          tDb?.numStudentsOver3quizes[0],
        ],
      },
    ];
    return studentsData;
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
    background-color: rgba(255, 255, 255, 0.90);
    height: 400px;
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
  font-size: 24px;
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
