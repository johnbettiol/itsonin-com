<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>itsonin - Events in ${ioiContext.city}!</title>
</head>
<body>
	<h1>itsonin - Events in ${ioiContext.city}!</h1>
	<p>
	<table>
		<c:forEach items="${events}" var="event">
			<tr>
				<td>${event.title}</td>
				<td>${event.type}</td>
			</tr>
		</c:forEach>
	</table>
	</p>
</body>
</html>