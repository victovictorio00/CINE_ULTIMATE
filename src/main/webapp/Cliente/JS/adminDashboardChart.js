(function () {
    'use strict';

    console.log('📊 Inicializando gráfico de ventas del dashboard...');

    // Obtener datos de configuración
    const config = window.adminDashboardData;

    // Validar que existan los datos
    if (!config || !config.ventasMensuales) {
        console.error('❌ Error: No se encontraron datos de ventas');
        return;
    }

    // Obtener el canvas del gráfico
    const canvas = document.getElementById('ventasChart');
    if (!canvas) {
        console.error('❌ Error: Canvas del gráfico no encontrado');
        return;
    }

    console.log('📈 Datos de ventas mensuales:', config.ventasMensuales);

    // Crear el contexto 2D
    const ctx = canvas.getContext('2d');

    // Crear el gráfico de barras
    const ventasChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: config.meses,
            datasets: [{
                label: 'Ventas Mensuales (S/)',
                data: config.ventasMensuales,
                backgroundColor: 'rgba(13, 110, 253, 0.8)',
                borderColor: '#0d6efd',
                borderWidth: 2,
                hoverBackgroundColor: '#084298'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return 'S/ ' + value.toFixed(2);
                        }
                    }
                },
                x: {}
            },
            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: true,
                    text: 'Ventas Mensuales - 2025',
                    color: '#084298',
                    font: {
                        size: 18,
                        weight: 'bold'
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return 'Ventas: S/ ' + context.parsed.y.toFixed(2);
                        }
                    }
                }
            }
        }
    });
})();
