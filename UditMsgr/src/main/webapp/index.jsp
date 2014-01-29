<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Messenger!</title>
		
		<script type='text/javascript'>
			var height = 430;                      
			var width = 400;                       
			var top = 20;                          
			var left = 20;                         
			
			if(document.location.search == '')  {
				newwin=window.open("showLoginPage.htm", name, "fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=no,resizable=no,directories=no,location=no,width=" + width + ",height=" + height + ",left=" + left + ",top=" + top);
				this.focus();
				self.opener = this;
				self.close();
			}
		</script>
	</head>
	<body>
		Launching...
	</body>
</html>