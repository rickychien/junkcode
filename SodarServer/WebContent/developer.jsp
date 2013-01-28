<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Sodar Developer</title>
</head>
<body>
	<h2>Sodar Developer</h2>
	<form action="getAPIKey" method="post">
		<table border="0">
			<tr>
				<td colspan="2" style="color: red">${api_key}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>*Name :</td>
				<td><input type="text" name="devp_name" maxlength="50" size="40" /></td>
			</tr>
			<tr>
				<td>*Email :</td>
				<td><input type="text" name="email" maxlength="100" size="40" /></td>
			</tr>
			<tr>
				<td>*App Name :</td>
				<td><input type="text" name="app_name" maxlength="50" size="40" /></td>
			</tr>
			<tr>
				<td>&nbsp;Company :</td>
				<td><input type="text" name="company" maxlength="50" size="40" /></td>
			</tr>
			<tr>
				<td>&nbsp;WebPage Url :</td>
				<td><input type="text" name="web_page" maxlength="100" size="40" /></td>
			</tr>
			<tr>
				<td>&nbsp;Description :</td>
				<td>
					<textarea name="description" cols="35" rows="5"
						onkeyup='value=value.substr(0,300);this.nextSibling.innerHTML=value.length+"/300";'
					></textarea><div>0/300</div>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center"><center>
						<input type="submit" value="Get API Key" />
					</center></td>
			</tr>
		</table>
	</form>
</body>
</html>