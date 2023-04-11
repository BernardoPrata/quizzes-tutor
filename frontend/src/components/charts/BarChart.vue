<template>
    <Bar :chartData="data" :chartOptions="options" />
  </template>
  
  <script lang="ts">
  import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
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
    components: { Bar },
  })
  export default class BarChart extends Vue {
    @Prop({ default: {} }) data!: Object;
    options: Object = {
      responsive: true,
      maintainAspectRatio: false,
    };
  }
  </script>
  
  <style scoped lang="scss" />
  