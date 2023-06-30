<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Job Grades</title>
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

.button.delete {
	background-color: #f44336;
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

<script>
	
	function showAddModal() {
		var modal = document.getElementById("addModal");
		var modalContent = document.getElementById("addModalContent");

		// Display the modal
		modal.style.display = "block";

		// Close the modal when the close button is clicked
		var closeButton = document.getElementsByClassName("close")[0];
		closeButton.onclick = function() {
			modal.style.display = "none";
		}

		// Close the modal when the user clicks outside of it
		window.onclick = function(event) {
			if (event.target == modal) {
				modal.style.display = "none";
			}
		}
	}
</script>

<script src="https://code.jquery.com/jquery-3.7.0.min.js" integrity="sha256-2Pmvv0kuTBOenSvLm6bvfBSSHrUJ+3A7x6P5Ebd07/g=" crossorigin="anonymous"></script>

</head>
<body>
	<h1>Job Grades</h1>

		<%@ page import="java.util.List, java.util.ArrayList"%>
	<%@ page import="models.HrmsJobGrade"%>
	<table>
		<tr>
			<th>Job Grade ID</th>
			<th>Job Grade Name</th>
			<th>Job Grade Description</th>
		</tr>

		<%
		List<HrmsJobGrade> jobGrades = (List<HrmsJobGrade>) request.getAttribute("gradeInfo");

		for (HrmsJobGrade jobGrade : jobGrades) {
		%>
		<tr>
			<td><%=jobGrade.getId()%></td>
			<td><%=jobGrade.getName()%></td>
			<td><%=jobGrade.getDescription()%></td>
		</tr>
		<%
		}
		%>
	</table>

	<div class="button-container">
		<button class="button add" onclick="showAddModal()">Add Job Grade</button>
	</div>

	<div id="addModal" class="modal">
		<div class="modal-content">
			<span class="close">&times;</span>
			<h2>Add Job Grade</h2>
			<form id="jobGradeForm">
				<div class="form-row">
				<label for="Id">ID:</label>
					<input type="text" id="Id" name="jbgrId" required>
					<label for="name">Name:</label>
					<input type="text" id="name" name="jbgrName" required>
					<label for="desc">Description:</label>
					<input type="text" id="desc" name="jbgrDescription" required>
				</div>
					<input type="button" value="Add" onclick="addGrade()">
			</form>
		</div>
	</div>
	
	<script type="text/javascript">
	
	function addGrade(){
		var modal = $("#addModal");
		var modalContent = $("#addModalContent");
		var formData = $("#jobGradeForm");
		$.ajax({
			url:"addGrades",
			type:"POST",
			data:formData.serialize(),
			success:function (response){
				console.log(response);
				alert("added successfully.");
				location.reload();
			},
		    error:function (error){
		    	console.log(error);
		    }
		});
	}
	
	
	</script>
</body>
</html>
