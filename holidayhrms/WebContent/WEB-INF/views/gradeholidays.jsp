<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="models.GradeHoliday" %>
<%@ page import="java.util.List" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List of Holidays</title>

    <style>
        
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        
        table {
            margin-top: 20px;
            border-collapse: collapse;
            width: 100%;
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
        }
        
        th, td {
            padding: 8px;
            text-align: left;
        }
        
        th {
            background-color: #f2f2f2;
            font-weight: bold;
            color: #333;
        }
        
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
 <h1>Job Grade Wise Holidays</h1><br>
    <table>
        <thead>
            <tr>
                <th>JobGradeID</th>
                <th>Total No of Holidays</th>
            </tr>
        </thead>
        <tbody>
            <%
            List<GradeHoliday> gradeholidays = (List<GradeHoliday>) request.getAttribute("gradeholidays");
            if (gradeholidays != null) {
                for (GradeHoliday gradeholiday : gradeholidays) { %>
                    <tr>
                        <td><%= gradeholiday.getJbgr_id() %></td>
                        <td><%= gradeholiday.getJbgr_totalnoh()%></td>
                    </tr>
                <% }
            } %>
        </tbody>
    </table>
</body>
</html>







<!-- <%@page import="models.HrmsJobGradeHoliday"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Job Grade Wise Holidays</title>
<style>
/* Add your custom CSS styles here */
body {
	font-family: Arial, sans-serif;
}

h1 {
	color: #333;
	text-align: center;
	margin-bottom: 30px;
}

table {
	margin-top: 20px;
	border-collapse: collapse;
	width: 100%;
	max-width: 800px;
	margin: 0 auto;
	background-color: #fff;
}

th, td {
	padding: 8px;
	text-align: left;
}

th {
	background-color: #f2f2f2;
	font-weight: bold;
	color: #333;
}

tr:nth-child(even) {
	background-color: #f9f9f9;
}

.button {
	display: inline-block;
	padding: 8px 12px;
	font-size: 14px;
	font-weight: bold;
	text-align: center;
	text-decoration: none;
	background-color: #4caf50;
	color: #fff;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.button.edit {
	background-color: #2196f3;
}

.modal {
	display: none;
	position: fixed;
	z-index: 1;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	overflow: auto;
	background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
	background-color: #fefefe;
	margin: 10% auto;
	padding: 20px;
	border: 1px solid #888;
	width: 80%;
	max-width: 600px;
}

.close {
	color: #aaa;
	float: right;
	font-size: 28px;
	font-weight: bold;
	cursor: pointer;
}

.close:hover, .close:focus {
	color: #000;
	text-decoration: none;
}

.form-row {
	margin-bottom: 16px;
}

.form-row label {
	display: block;
	margin-bottom: 4px;
	font-weight: bold;
	color: #333;
}

.form-row input[type="text"], .form-row input[type="number"] {
	padding: 6px 8px;
	font-size: 14px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

.form-row input[type="submit"] {
	padding: 8px 16px;
	font-size: 14px;
	font-weight: bold;
	text-align: center;
	text-decoration: none;
	background-color: #4caf50;
	color: #fff;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.button-container {
	text-align: center;
	margin-top: 20px;
}

.button.add {
	background-color: #ff9800;
	margin-left: auto;
	margin-right: auto;
	display: block;
}
</style>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    function openModal(jobGradeId) {
        $("#jobGradeId").val(jobGradeId);
        $("#editModal").css("display", "block");
    }

    function closeModal() {
        $("#editModal").css("display", "none");
    }

    function deleteHoliday(jobGradeId) {
        console.log("Deleting holiday with jobGradeId: " + jobGradeId);
        // Perform the delete operation
        // Here, you can make an AJAX request to delete the holiday using the jobGradeId

        // Reload the page
        location.reload();
    }
</script>
</head>
<body>
    <h1>Job Grade Wise Holidays</h1>
    <table>
        <tr>
            <th>Job Grade ID</th>
            <th>Total No of Holidays</th>

        </tr>

        <% 
            List<HrmsJobGradeHoliday> holidays = (List<HrmsJobGradeHoliday>) request.getAttribute("jobGradeHolidays");
            for (HrmsJobGradeHoliday holiday : holidays) {
        %>
        <tr>
            <td><%= holiday.getJobGradeId() %></td>
            <td><%= holiday.getHolidayDate() %></td>
            <td>
                <button class="button edit" onclick="openModal('<%= holiday.getJobGradeId() %>')">Edit</button>
            </td>
        </tr>
        <% } %>
    </table>

    <div class="button-container">
        <button class="button add" onclick="openModal('')">Add Holiday</button>
    </div>

    <div id="editModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal()">&times;</span>
            <h2><span id="modalTitle"></span></h2>
            <form id="editForm">
                <div class="form-row">
                    <label for="jobGradeId">Job Grade ID:</label>
                    <input type="text" id="jobGradeId" name="jobGradeId" readonly>
                </div>

                <div class="form-row">
                    <label for="holidayDate">Total No of Holidays:</label>
                    <input type="text" id="holidayDate" name="holidayDate">
                <div class="form-row">
                    <input type="submit" class="button" value="Save">
                </div>
            </form>
        </div>
    </div>
</body>
</html>
 -->