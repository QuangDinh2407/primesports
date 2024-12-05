function renderLineChart(chartId, chartData) {
    var ctx = document.getElementById(chartId).getContext('2d');

    var values = chartData.values;
    var labels = chartData.labels;

    var maxValue = Math.max(...values);
    var stepSize = 1000;
    var maxY = Math.ceil(maxValue / stepSize) * stepSize;
    const labelChart = null;
    var data = {
        labels: labels,
        datasets: [{
            label: "Doanh thu theo tháng",
            data: values,
            backgroundColor: 'rgba(26, 115, 232, 0.18)',
            borderColor: '#1F3BB3',
            borderWidth: 1.5,
            fill: true,
            pointBorderWidth: 1,
            pointRadius: 4,
            pointHoverRadius: 2,
            pointBackgroundColor: '#1F3BB3',
            pointBorderColor: '#fff',
        }]
    };

    var options = {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
            yAxes: [{
                ticks: {
                    stepSize: stepSize,
                    min: 0,
                    max: maxY,
                    fontColor: "#6B778C",
                    fontSize: 14,
                    autoSkip: true,
                },
                gridLines: {
                    color: '#F0F0F0'
                },
            }],
            xAxes: [{
                gridLines: {
                    display: false
                },
                ticks: {
                    fontColor: "#6B778C"
                }
            }]
        },
        plugins: {
            legend: {
                display: false
            },
            tooltip: {
                backgroundColor: 'rgba(31, 59, 179, 1)',
            }
        }
    };

    new Chart(ctx, {
        type: 'line',
        data: data,
        options: options
    });
}
