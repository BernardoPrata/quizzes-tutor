<template>
  <div class="container">
    <h2>Statistics for this course execution</h2>
    <div v-if="teacherDashboard != null" class="stats-container">
      <div class="items">
        <div ref="totalQuestions" class="icon-wrapper" data-cy="totalQuestions">
          <animated-number :number="teacherDashboard.numberOfQuestions[0]" />
        </div>
        <div class="project-name">
          <p>Number of Questions</p>
        </div>

        <div ref="uniqueQuestionsSolved" class="icon-wrapper" data-cy="uniqueQuestionsSolved">
          <animated-number :number="teacherDashboard.uniqueQuestionsSolved[0]" />
        </div>
        <div class="project-name">
          <p>Number of Questions Solved (Unique)</p>
        </div>

        <div ref="averageSolvedCorrectQuestions" class="icon-wrapper" data-cy="averageSolvedCorrectQuestions">
          <animated-number :number="teacherDashboard.averageSolvedCorrectQuestions[0]" />
        </div>
        <div class="project-name">
          <p>Number of Questions Correctly Solved (Unique, Average Per Student)</p>
        </div>
      </div>
    </div>

    <h2>Comparison with previous course executions</h2>

    <div v-if="teacherDashboard != null" class="stats container">
      <!-- Div to display the stats about questions -->
      <div class="bar-chart">
        <bar-chart :data="questionsData" />
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
import { Bar } from 'vue-chartjs';


ChartJS.register(
    Title,
    Tooltip,
    Legend,
    BarElement,
    CategoryScale,
    LinearScale
);


@Component({
  components: { AnimatedNumber, Bar },
})

export default class TeacherStatsView extends Vue {
  @Prop() readonly dashboardId!: number;
  teacherDashboard: TeacherDashboard | null = null;
  questionsData: Object = {};
  options: Object = {};

  async created() {
    await this.$store.dispatch('loading');
    try {
      this.teacherDashboard = await RemoteServices.getTeacherDashboard();
      this.questionsData = this.extractQuestionsData(this.teacherDashboard);
    } catch (error) {
      await this.$store.dispatch('error', error);
    }
    await this.$store.dispatch('clearLoading');
  }


  extractQuestionsData(tDb: TeacherDashboard) {
    let questionsData: { labels: object, datasets: object } = {
      labels: [],
      datasets: [],
    };

    questionsData.labels = [
      tDb?.executionYears[2] ? tDb?.executionYears[2] : ' ',
      tDb?.executionYears[1] ? tDb?.executionYears[1] : ' ',
      tDb?.executionYears[0] ? tDb?.executionYears[0] + ' (current)' : 'current',
    ];

    questionsData.datasets = [
      {
        label: 'Questions: Total Available',
        backgroundColor: '',
        data: [
          tDb?.numberOfQuestions[2],
          tDb?.numberOfQuestions[1],
          tDb?.numberOfQuestions[0],
        ],
      },
      {
        label: 'Questions: Solved (Unique)',
        backgroundColor: '',
        data: [
          tDb?.uniqueQuestionsSolved[2],
          tDb?.uniqueQuestionsSolved[1],
          tDb?.uniqueQuestionsSolved[0],
        ],
      },
      {
        label: 'Questions: Correctly Solved (Unique, Average Per Student)',
        backgroundColor: '',
        data: [
          tDb?.averageSolvedCorrectQuestions[2],
          tDb?.averageSolvedCorrectQuestions[1],
          tDb?.averageSolvedCorrectQuestions[0],
        ],
      },
    ];
    return questionsData;
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
