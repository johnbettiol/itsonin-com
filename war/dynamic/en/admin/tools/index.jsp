<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>itsonin - tools page</title>
</head>
<body>
<h1>IOI Admin Console</h1>
<p>Welcome!</p>
<p>Device Info: ${ioiContext.device.level}</p>
<p>Command:  ${ioiContext.command}</p>
<c:if test="${not empty message}"><p>Message:  ${message}</p></c:if>
	<table>
		<thead>
			<tr>
				<th colspan="2">Device Actions</th>
			</tr>
		</thead>
		<tbody>	
			<tr>
				<td>Change Type to NORMAL account</td>
				<td><a href="UpdateAccount?level=NORMAL">process</a></td>
			</tr>
			<tr>
				<td>Change Type to ADMIN account</td>
				<td><a href="UpdateAccount?level=ADMIN">process</a></td>
			</tr>
			<tr>
				<td>Change Type to SUPER account</td>
				<td><a href="UpdateAccount?level=SUPER">process</a></td>
			</tr>
		</tbody>
	</table>
		<table>
		<thead>
			<tr>
				<th colspan="2">Import Actions</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<form method="POST" action="ImportData" enctype="application/x-www-form-urlencoded">
				<td>Import Default Data</td>
				<td>
						<input type="hidden" name="type" value="default" />
				</td>
				<td>
						<input type="submit" value="Process"/>
				</td>
				</form>
			</tr>
			<tr>
				<form method="POST" action="ImportData" enctype="application/x-www-form-urlencoded">
				<td>Import From File</td>
				<td>
						<input type="hidden" name="type" value="upload" />
						<input type="file" name="importFile"/>
				</td>
				<td>
						<input type="submit" value="Process"/>
				</td>
				</form>
			</tr>
			<tr>
				<form method="POST" action="ImportData">
				<td>Import From URL</td>
				<td>
					<input type="hidden" name="type" value="download" />
					<input type="text" name="url"/>
				</td>
				<td>
					<input type="submit" value="Process"/>
				</td>
				</form>
			</tr>
			<tr>
				<form method="POST" action="ImportData">
				<td>Import From clipboard</td>
				<td>
					<input type="hidden" name="type" value="text" />
					<textarea name="data"></textarea>
				</td>
				<td>
					<input type="submit" value="Process"/>
				</td>
				</form>
			</tr>
		</tbody>
	</table>
</body>
</html>