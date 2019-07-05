<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js">
</script>
<title>Insert title here</title>
</head>
<body>
	<a href="book/1000/appoint?studentId=12345678910">
		嗯嗯
	</a>
	<p>名称: <input type="text" id="test" value="菜鸟教程"></p>
	<button>删除</button>
	<a onclick="deleteBtn()">删除</a>
</body>
<script type="text/javascript">
$(document).ready(function(){
	  $("button").click(function(){
	  var bookId =  $("#test").val();
	  $.ajax({
		  url:'http://localhost:8080/ssm_demo/book/delete',
		  data:{
			  bookId:bookId
		  }
	  });
	  });
	});
</script>
</html>