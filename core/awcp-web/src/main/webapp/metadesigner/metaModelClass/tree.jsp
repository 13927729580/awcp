<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
	<link rel="stylesheet" href="../../base/resources/treeView/jquery.treeview.css">
	<link rel="stylesheet" href="../../base/resources/treeView/screen.css">
	<script type="text/javascript" src="../../base/resources/treeView/jquery.js"></script>
	<script type="text/javascript" src="../../base/resources/treeView/jquery.cookie.js"></script>
	<script type="text/javascript" src="../../base/resources/treeView/jquery.treeview.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#browser").treeview({
				toggle: function() {
					console.log("%s was toggled.", $(this).find(">span").text());
				}
			});
			
			$("#add").click(function() {
				var branches = $("<li><span class='folder'>New Sublist</span><ul>" + 
					"<li><span class='file'>Item1</span></li>" + 
					"<li><span class='file'>Item2</span></li></ul></li>").appendTo("#browser");
				$("#browser").treeview({
					add: branches
				});
			});
		});
	</script>
</head>
<body>
	<div id="main">
		<ul id="browser" class="filetree treeview-famfamfam">
		<li><span class="folder">Folder 1</span>
			<ul>
				<li><span class="folder">Item 1.1</span>
					<ul>
						<li><span class="file">Item 1.1.1</span></li>
					</ul>
				</li>
				<li><span class="folder">Folder 2</span>
					<ul>
						<li><span class="folder">Subfolder 2.1</span>
							<ul id="folder21">
								<li><span class="file">File 2.1.1</span></li>
								<li><span class="file">File 2.1.2</span></li>
							</ul>
						</li>
						<li><span class="folder">Subfolder 2.2</span>
							<ul>
								<li><span class="file">File 2.2.1</span></li>
								<li><span class="file">File 2.2.2</span></li>
							</ul>
						</li>
					</ul>
				</li>
				<li class="closed"><span class="folder">Folder 3 (closed at start)</span>
					<ul>
						<li><span class="file">File 3.1</span></li>
					</ul>
				</li>
				<li><span class="file">File 4</span></li>
			</ul>
		</li>
	</ul>
	
	<button id="add">Add!</button>
	</div>
</body>
</html>