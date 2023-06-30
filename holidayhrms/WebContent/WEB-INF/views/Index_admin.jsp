<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Pennant HRMS</title>
    <link rel="stylesheet" href="style.css" />
 
    <style> 
    * {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Poppins", sans-serif;
}
.sidebar {
  position: fixed;
  height: 100%;
  width: 260px;
  background: #11101d;
  padding: 15px;
  z-index: 99;
}
.logo {
  font-size: 25px;
  padding: 0 15px;
}
.sidebar a {
  color: #fff;
  text-decoration: none;
}
.menu-content {
  position: relative;
  height: 100%;
  width: 100%;
  margin-top: 40px;
  overflow-y: scroll;
}
.menu-content::-webkit-scrollbar {
  overflow-y: scroll;
}
.menu-items {
  height: 100%;
  width: 100%;
  list-style: none;
  transition: all 0.4s ease;
}
.submenu-active .menu-items {
  transform: translateX(-56%);
}
.menu-title {
  color: #fff;
  font-size: 14px;
  padding: 15px 20px;
}
.item a,
.submenu-item {
  padding: 16px;
  display: inline-block;
  width: 100%;
  border-radius: 12px;
}
.item i {
  font-size: 12px;
}
.item a:hover,
.submenu-item:hover,
.submenu .menu-title:hover {
  background: rgba(255, 255, 255, 0.1);
}
.submenu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
  cursor: pointer;
}
.submenu {
  position: absolute;
  height: 100%;
  width: 100%;
  top: 0;
  right: calc(-100% - 26px);
  height: calc(100% + 100vh);
  background: #11101d;
  display: none;
}
.show-submenu ~ .submenu {
  display: block;
}
.submenu .menu-title {
  border-radius: 12px;
  cursor: pointer;
}
.submenu .menu-title i {
  margin-right: 10px;
}
.navbar,
.main {
  left: 260px;
  width: calc(100% - 260px);
  transition: all 0.5s ease;
  z-index: 1000;
}
.sidebar.close ~ .navbar,
.sidebar.close ~ .main {
  left: 0;
  width: 100%;
}
.navbar {
  position: relative;
  color: #fff;
  padding: 15px 20px;
  font-size: 25px;
  background: #4070f4;
  cursor: pointer;
}
.navbar #sidebar-close {
  cursor: pointer;
}
 .main {
      position: relative;
      display: flex;
      flex-direction: column;
      align-items: stretch;
     
      height: 100vh;
      padding: 20px;
      background: #e7f2fd;
      overflow: auto;
      max-width: 100%;
      max-height: 100%;
      top: 2px;
      right: 60px;
      
     
    }
    
    </style>
    <!-- Fontawesome CDN Link -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.9.1/dist/chart.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/remixicon@2.5.0/fonts/remixicon.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.1/css/all.min.css" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    
    
     <script>
    function getHolidays() {
    	  $.ajax({
    	    type: "GET",
    	    url: "holidaysupd",    	  
    	    success: function(response) {
    	      var containerDiv = $("#main");
    	      containerDiv.html(response);

    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	}

    function getCandidates() {
  	  $.ajax({
  	    type: "POST",
  	    url: "candidates",
  	    data: {},
  	    success: function(response) {
  	      var containerDiv = $("#main");
  	      containerDiv.html(response);
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	}

    
    
    function getemployees() {
  	  $.ajax({
  	    type: "POST",
  	    url: "employees",
  	    data: {},
  	    success: function(response) {
  	      var containerDiv = $("#main");
  	      containerDiv.html(response);
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	}
    
    function getcandidates() {
    	  $.ajax({
    	    type: "POST",
    	    url: "viewcandidates",
    	    data: {},
    	    success: function(response) {
    	      var containerDiv = $("#main");
    	      containerDiv.html(response);
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	}
    
    function referenceDoc() {
  	  $.ajax({
  	    type: "GET",
  	    url: "viewDocuments",
  	    success: function(response) {
  	      var containerDiv = $("#main");
  	      containerDiv.html(response);
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	}
    
    function inductiondoc() {
    	  $.ajax({
    	    type: "GET",
    	    url: "getform",
    	    success: function(response) {
    	      var containerDiv = $("#main");
    	      containerDiv.html(response);
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	}
    
    function getInductions() {
    	  $.ajax({
    	    type: "GET",
    	    url: "inductioninsert",
    	    data: {},
    	    success: function(response) {
    	      var containerDiv = $("#main");
    	      containerDiv.html(response);
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	}
    
    function viewInductions() {
  	  $.ajax({
  	    type: "POST",
  	    url: "inductionlist",
  	  
  	    success: function(response) {
  	      var containerDiv = $("#main");
  	      containerDiv.html(response);
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	}
    
    function getLeaveRequests() {
    	  $.ajax({
    	    type: "GET",
    	    url: "leaveRequests",
    	   
    	    success: function(response) {
    	      var containerDiv = $("#main");
    	      containerDiv.html(response);
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	}
    
    function getApprovedLeaves() {
  	  $.ajax({
  	    type: "GET",
  	    url: "AdminapprovedLeaves",
  	   
  	    success: function(response) {
  	      var containerDiv = $("#main");
  	      containerDiv.html(response);
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	} 
    
    function uploadAttendance() {
    	  $.ajax({
    	    type: "GET",
    	    url: "attendanceform",
    	   
    	    success: function(response) {
    	      var containerDiv = $("#main");
    	      containerDiv.html(response);
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	} 
    
    function viewAttendance() {
    	  $.ajax({
    	    type: "GET",
    	    url: "adminAttendance",
    	   
    	    success: function(response) {
    	      var containerDiv = $("#main");
    	      containerDiv.html(response);
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	} 
    
    function permissionsToBeApproved() {
  	  $.ajax({
  	    type: "POST",
  	    url: "adminviewpermissions",
  	   
  	    success: function(response) {
  	      var containerDiv = $("#main");
  	      containerDiv.html(response);
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	} 
  
    function generatePayroll() {
    	  $.ajax({
    	    type: "GET",
    	    url: "getemppayroll",
    	   
    	    success: function(response) {
    	      var containerDiv = $("#main");
    	      containerDiv.html(response);
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	} 
    
    function issueOffer() {
  	  $.ajax({
  	    type: "GET",
  	    url: "issue",
  	   
  	    success: function(response) {
  	      var containerDiv = $("#main");
  	      containerDiv.html(response);
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	} 
    
    function providedOffers() {
    	  $.ajax({
    	    type: "GET",
    	    url: "provided",
    	   
    	    success: function(response) {
    	      var containerDiv = $("#main");
    	      containerDiv.html(response);
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	} 
    
    function getLeavesByGrade() {
  	  $.ajax({
  	    type: "GET",
  	    url: "getJobGradeWiseLeaves",
  	   
  	    success: function(response) {
  	      var containerDiv = $("#main");
  	      containerDiv.html(response);
  	    },
  	    error: function() {
  	      alert("Error occurred. Please try again later.");
  	    }
  	  });
  	} 
    
    function getHolidaysByGrade() {
    	  $.ajax({
    	    type: "GET",
    	    url: "getgradewiseholidays",
    	   
    	    success: function(response) {
    	      var containerDiv = $("#main");
    	      containerDiv.html(response);
    	    },
    	    error: function() {
    	      alert("Error occurred. Please try again later.");
    	    }
    	  });
    	} 
    
    
    
    
    function logout(){
    	$.ajax({
    		url:"logout",
    		type:"GET",
    		success:function(response){
    			window.location.href='./';
    		},
    		error:function(error){
    			console.log(error);
    		}
    		
    	});
    }
    
    
    function getJobGrades(){
    	 $.ajax({
     	    type: "GET",
     	    url: "getJobGradeList",
     	   
     	    success: function(response) {
     	      var containerDiv = $("#main");
     	      containerDiv.html(response);
     	    },
     	    error: function() {
     	      alert("Error occurred. Please try again later.");
     	    }
     	  });
    }
    
    </script>
    
  

    
  </head>
  <body>
    <nav class="sidebar">
      <a href="#" class="logo">ADMIN</a>

      <div class="menu-content">
        <ul class="menu-items">
          <div class="menu-title"><i class="ri-dashboard-line"></i>
            DashBoard
          </div>

          <li class="item">
            <a href="#"><i class="ri-bar-chart-line"></i> Profile</a>
          </li>
          
 <li class="item">
            <a onclick = "getcandidates();"><i class="ri-bar-chart-line"></i> Candidates</a>
          </li>

          <li class="item">
            <div class="submenu-item">
              <span><i class="ri-handbag-line"></i> Offer Letters</span>
              <i class="fa-solid fa-chevron-right"></i>
            </div>

            <ul class="menu-items submenu">
              <div class="menu-title">
                <i class="fa-solid fa-chevron-left"></i>
                Back to Offer Letters
              </div>
             
              <li class="item">
              <a href="javascript:void(0)" onclick="providedOffers();"><i class="ri-calendar-line"></i> Provided</a>
              </li>
              
               <li class="item">
                <a href="javascript:void(0)"  onclick="issueOffer();"><i class="ri-file-list-line"></i> Issue</a>
              </li>
              
            </ul>
          </li>
          
<li class="item">
            <div class="submenu-item">
              <span><i class="ri-handbag-line"></i> Induction</span>
              <i class="fa-solid fa-chevron-right"></i>
            </div>

            <ul class="menu-items submenu">
              <div class="menu-title">
                <i class="fa-solid fa-chevron-left"></i>
                Back to Induction
              </div>
             
              <li class="item">
              <a href="javascript:void(0)" onclick="getInductions();"><i class="ri-calendar-line"></i> Insert</a>
              </li>
              
               <li class="item">
                <a href="javascript:void(0)" onclick="viewInductions();"><i class="ri-file-list-line"></i> View</a>
              </li>
              
            </ul>
          </li>
          
          <li class="item">
            <a onclick = "getemployees();"><i class="ri-bar-chart-line"></i> Employees</a>
          </li>

          
           
          
          <li class="item">
            <div class="submenu-item">
              <span><i class="ri-user-line"></i> Master Data</span>
              <i class="fa-solid fa-chevron-right"></i>
            </div>

            <ul class="menu-items submenu">
              <div class="menu-title">
                <i class="fa-solid fa-chevron-left"></i>
                Back to Master Data
              </div>
              <li class="item">
                <a onclick="getHolidays();"><i class="ri-file-list-line"></i> Holidays</a>
              </li>
               <li class="item">
                <a onclick="getJobGrades();"><i class="ri-file-list-line"></i> Job Grades</a>
              </li>
              <li class="item">
                <a onclick="getHolidaysByGrade();"><i class="ri-file-list-line"></i> Grade Wise Holidays</a>
              </li>
              <li class="item">
                <a onclick="getLeavesByGrade();"><i class="ri-check-double-line"></i>Grade Wise Leaves</a>
              </li>
            </ul>
          </li>
          
           <li class="item">
            <div class="submenu-item">
              <span><i class="ri-user-line"></i> ALMS</span>
              <i class="fa-solid fa-chevron-right"></i>
            </div>

            <ul class="menu-items submenu">
              <div class="menu-title">
                <i class="fa-solid fa-chevron-left"></i>
                Back to ALMS
              </div>
              <li class="item">
                <a onclick="uploadAttendance();"><i class="ri-file-list-line"></i> Upload Attendance</a>
              </li>
               <li class="item">
                <a onclick="viewAttendance();"><i class="ri-file-list-line"></i> View Attendance</a>
              </li>
              <li class="item">
              <li class="item">
                <a onclick="getLeaveRequests();"><i class="ri-check-double-line"></i> LeaveRequests</a>
              </li>
              <li class="item">
                <a onclick="getApprovedLeaves();"><i class="ri-check-double-line"></i> Approved Leaves</a>
              </li>
               <li class="item">
                <a onclick="permissionsToBeApproved();"><i class="ri-check-double-line"></i> Permissions</a>
              </li>
            </ul>
          </li>
          
          <li class="item">
            <div class="submenu-item">
              <span><i class="ri-user-line"></i> Documents</span>
              <i class="fa-solid fa-chevron-right"></i>
            </div>

            <ul class="menu-items submenu">
              <div class="menu-title">
                <i class="fa-solid fa-chevron-left"></i>
                Back to Documents
              </div>
              <li class="item">
                <a onclick="referenceDoc();"><i class="ri-check-double-line"></i> Employee Reference</a>
              </li>
               <li class="item">
                <a onclick="inductiondoc();"><i class="ri-check-double-line"></i> Induction Document</a>
              </li>
            </ul>
          </li>
          
          <li class="item">
            <a onclick="generatePayroll();"><span class="icon"><i class="ri-booklet-line"></i></span> Payrolls</a>
          </li>
          <li class="item">
            <a onclick="logout()"><span class="icon"><i class="ri-logout-box-r-line"></i></span> Logout</a>
          </li>
        </ul>
      </div>
    </nav>

    <nav class="navbar">
      <i class="fa-solid fa-bars" id="sidebar-close"></i>
    </nav>

<div class="main" id="main">
   <!--  <main class="main" id="main"  height="600px" width="600px">
      <h1>Welcome Admin to Pennant Technologies....</h1>
    </main> -->
    
</div>

   
  </body>
       <script>
       const sidebar = document.querySelector(".sidebar");
       const sidebarClose = document.querySelector("#sidebar-close");
       const menu = document.querySelector(".menu-content");
       const menuItems = document.querySelectorAll(".submenu-item");
       const subMenuTitles = document.querySelectorAll(".submenu .menu-title");

       sidebarClose.addEventListener("click", () => sidebar.classList.toggle("close"));

       menuItems.forEach((item, index) => {
         item.addEventListener("click", () => {
           menu.classList.add("submenu-active");
           item.classList.add("show-submenu");
           menuItems.forEach((item2, index2) => {
             if (index !== index2) {
               item2.classList.remove("show-submenu");
             }
           });
         });
       });

       subMenuTitles.forEach((title) => {
         title.addEventListener("click", () => {
           menu.classList.remove("submenu-active");
         });
       });

</script>

 
</html>

