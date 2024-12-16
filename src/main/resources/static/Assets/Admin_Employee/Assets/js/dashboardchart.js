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


function renderColumnChart(chartId, chartData) {
    var ctx = document.getElementById(chartId).getContext('2d');

    var values = chartData.values_sales;
    var labels = chartData.labels_sales;
    var maxValue = Math.max(...values);
    var stepSize = 10;
    var maxY = Math.ceil(maxValue / stepSize) * stepSize;
    var data = {
        labels: labels,
        datasets: [{
            label: 'Tình trạng đơn hàng',
            data: values,
            backgroundColor: [
                'rgba(75, 192, 192, 0.2)',
                'rgba(255, 99, 132, 0.2)',
                'rgba(255, 206, 86, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(153, 102, 255, 0.2)',
                'rgba(255, 159, 64, 0.2)'
            ],
            borderColor: [
                'rgba(75, 192, 192, 1)',
                'rgba(255,99,132,1)',
                'rgba(255, 206, 86, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 1,
            fill: false
        }]
    };

    var options = {
        scales: {
            yAxes: [{
                ticks: {
                    stepSize: stepSize,
                    min: 0,
                    max: maxY,
                    fontColor: "#6B778C",
                    fontSize: 14,
                    autoSkip: true,
                }
            }],
            xAxes: [{
                gridLines: {
                    display: false
                },
                ticks: {
                    fontColor: "#6B778C"
                },
                barPercentage: 0.8, // Giảm kích thước của cột (0.0 - 1.0)
                categoryPercentage: 0.6 // Giảm khoảng cách giữa các cột (0.0 - 1.0)
            }]
        },
        legend: {
            display: false
        },
        elements: {
            point: {
                radius: 0
            }
        }

    };

    new Chart(ctx, {
        type: 'bar',
        data: data,
        options: options
    });
}

function renderTopSellingProduct(chartId, chartData) {
    var ctx = document.getElementById(chartId).getContext('2d');

    var values = chartData.values_product;
    var labels = chartData.labels_product.map(label =>
        label.length > 15 ? label.slice(0, 15) + '...' : label
    );
    var maxValue = Math.max(...values);
    var stepSize = 10;
    var maxY = maxValue;
    var data = {
        labels: labels,
        datasets: [{
            data: values,
            backgroundColor: [
                'rgba(75, 192, 192, 1)',
                'rgba(255,99,132,1)',
                'rgba(255, 206, 86, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)'
            ],
            borderColor: [
                'rgba(75, 192, 192, 1)',
                'rgba(255,99,132,1)',
                'rgba(255, 206, 86, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(153, 102, 255, 1)',
                'rgba(255, 159, 64, 1)'
            ],
            borderWidth: 1,
            fill: false
        }]
    };

    var options = {
        scales: {
            yAxes: [{
                ticks: {
                    stepSize: 1,
                    min: 0,
                    max: maxY,
                    fontColor: "#6B778C",
                    fontSize: 14,
                    autoSkip: true,
                }
            }],
            xAxes: [{
                gridLines: {
                    display: false
                },
                ticks: {
                    fontColor: "#6B778C"
                },
                barPercentage: 0.8, // Giảm kích thước của cột (0.0 - 1.0)
                categoryPercentage: 0.6 // Giảm khoảng cách giữa các cột (0.0 - 1.0)
            }]
        },
        legend: {
            display: false
        },
        elements: {
            point: {
                radius: 0
            }
        }

    };

    new Chart(ctx, {
        type: 'bar',
        data: data,
        options: options
    });
}
