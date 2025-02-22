<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Fraud Data - Time Series</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>  <!-- Load Chart.js -->
    
    <style>
        /* Restrict chart to the top-left and set fixed size */
        #chartContainer {
            width: 60vw;  /* 60% of viewport width */
            height: 60vh; /* 60% of viewport height */
            position: absolute;
            top: 10px;
            left: 10px;
            border: 1px solid #ccc; /* Optional border */
            padding: 10px;
            background: white;
        }

        canvas {
            width: 100% !important;
            height: 100% !important;
        }
    </style>
</head>
<body>
    <h2>Fraud Data</h2>

    <!-- Time-Series Graph Container -->
    <div id="chartContainer">
        <canvas id="fraudChart"></canvas>
    </div>

    <script>
        // Prepare Data from JSP into JavaScript Arrays
        var labels = [];
        var fraudCounts = [];

        <c:forEach var="entry" items="${fraudData}">
            labels.push("${entry.time}");  // Add timestamps as labels
            fraudCounts.push(${entry.count}); // Add fraud count values
        </c:forEach>

        // Render Chart.js Graph
        var ctx = document.getElementById('fraudChart').getContext('2d');
        var fraudChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Fraud Count Over Time',
                    data: fraudCounts,
                    borderColor: 'rgba(255, 99, 132, 1)', 
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    borderWidth: 2,
                    tension: 0.3 // Smooth curve
                }]
            },
            options: {
                responsive: false,  // Prevent auto-resizing
                maintainAspectRatio: false, 
                scales: {
                    x: { title: { display: true, text: 'Time' } },
                    y: { title: { display: true, text: 'Fraud Count' }, beginAtZero: true }
                }
            }
        });
    </script>
</body>
</html>
