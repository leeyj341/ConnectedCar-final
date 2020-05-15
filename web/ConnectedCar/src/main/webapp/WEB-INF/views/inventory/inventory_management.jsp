<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html lang="en">
<head>
	<title>inventory_management</title>
	<link rel="stylesheet" type="text/css" href="/mycar/common/css/minjae/table.css">
	<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
	integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
	crossorigin="anonymous">
</head>

<body>
<div class="container">
	<div class="row">
		<h1>재고관리</h1>
	</div>
	<div class="row">
		<div class="col-sm-1"></div>
		<div class="col-sm-10">
			<div class="pull-right"> <button id="add parts" onclick="window.open('여기에 부품추가 컨트롤러 url쓰기', '_blank', 'width=300px,height=300px')">부품추가</button></div>
		</div>
		<!-- <div align="right" style="padding-right: 40px">
			<div><button id="add parts" onclick="window.open('여기에 부품추가 컨트롤러 url쓰기', '_blank', 'width=300px,height=300px')">부품추가</button></div>
		</div> -->
		<div class="col-sm-1"></div>
	</div>
	<!-- management area start -->
	<div class="row">
		<div class="col-sm-1"></div>
		<div id="management" class="col-sm-10"  style="padding-top: 10px; padding-left: 20px;">
			<table id="table_managementList" class="type01">
				<thead>
					<tr style="font-weight: bold;">
						<th scope="col">최근 입고일</th>
						<th scope="col">부품코드</th>
						<th scope="col">종류</th>
						<th scope="col">제조사</th>
						<th scope="col">모델이름</th>
						<th scope="col">개수</th>
						<th scope="col">가격</th>
						<th scope="col">관리</th>
					</tr>
				</thead>
				<tbody>
				<tr>
					<td>오늘</td>
					<td>AA001</td>
					<td>브레이크 패드</td>
					<td>현대</td>
					<td>그랜져</td>
					<td>5</td>
					<td>15000</td>
					<td><a onclick="window.open('manageDetail.do', '_blank', 'width=800px,height=500px')">관리</a></td>
				</tr>
					<%-- <%
						for (int i = 0; i < list.size(); i++) {
							BoardVO row = list.get(i);
					%>
					<tr>
					<td><%=row.getMember_id()%></td>
						<td><a href="/maeggiSeggi/board/read.do?askno=<%=row.getAskno()%>"><%=row.getAsk_title()%></a></td>
						<td><%=row.getAsk_regdate()%></td>
					</tr>
					<%
						}
					%> --%>
				</tbody>
			</table>
		</div>
		<div class="col-sm-1"></div>
	</div>
		<!-- management area END -->
</div>
</body>
</html>