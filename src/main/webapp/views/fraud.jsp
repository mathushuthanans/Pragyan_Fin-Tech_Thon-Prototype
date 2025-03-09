<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Fraud Data Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    
    <!-- Include External CSS -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css">

    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
        }
        /* Container for the entire dashboard */
        .dashboard-container {
            display: flex;
            height: 100vh; /* Full viewport height */
            justify-content: space-between;
            padding: 20px;
        }
        /* Chart container (Left) */
        #chartContainer {
            width: 60%;
            height: 60vh; /* 60% of viewport height */
            padding: 20px;
            border: 1px solid #ccc;
            background: white;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        /* Metrics container (Right) */
        #metricsContainer {
            width: 35%;
            height: 60vh;
            padding: 20px;
            border: 1px solid #ccc;
            background: #f9f9f9;
            overflow-y: auto; /* Prevents overflow issues */
        }
        h2 {
            margin-top: 0;
        }
        .metric {
            margin-bottom: 10px;
        }
        /* Ensure canvas scales properly */
        #fraudChart {
            width: 100% !important;
            height: 100% !important;
        }
    </style>
</head>
<body>
    
    <h1 style="text-align: center;">Scam Aware: App to detect Fraud Transactions</h1>    <!-- Include Header -->
    <%@ include file="header.jsp" %>


    <div class="dashboard-container">
        <!-- Left: Time-Series Graph -->
        <div id="chartContainer">
            <h2>Fraud Data - Time Series</h2>
            <canvas id="fraudChart"></canvas>
        </div>

        <!-- Right: Accuracy Metrics & Interpretability -->
        <div id="metricsContainer">
            <h2>Accuracy Metrics</h2>
            <div class="metric"><strong>Precision:</strong> <span>${metrics.precision}</span></div>
            <div class="metric"><strong>Recall:</strong> <span>${metrics.recall}</span></div>

            <h2>Model Interpretability</h2>
            <div class="metric"><strong>True Positives:</strong> <span>${interpretability.truePositive}</span></div>
            <div class="metric"><strong>False Positives:</strong> <span>${interpretability.falsePositive}</span></div>
            <div class="metric"><strong>False Negatives:</strong> <span>${interpretability.falseNegative}</span></div>
            <div class="metric"><strong>True Negatives:</strong> <span>${interpretability.trueNegative}</span></div>
            <div class="metric"><strong>Description:</strong> <p>${interpretability.description}</p></div>
        </div>
    </div>

    <script>
        // Prepare Data Arrays from JSP
        var labels = [];
        var fraudCounts = [];
        <c:forEach var="entry" items="${fraudData}">
            labels.push("${entry.time}");
            fraudCounts.push(${entry.count});
        </c:forEach>

        // Initialize Chart.js Line Graph
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
                    tension: 0.3
                }]
            },
            options: {
                responsive: true,
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
