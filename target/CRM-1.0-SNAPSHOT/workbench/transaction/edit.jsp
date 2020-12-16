<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>

<script>
	$(function () {
		$("#quxiaoBtn").on("click",function () {
			window.location.href="workbench/transaction/index.jsp";
		})

		//为放大镜图标绑定事件
		$("#openSearchModel").on("click",function () {
			$("#searchActivityModal").modal("show")
			//展示市场活动列表
			showActivity()
		})
		//为放大镜图标绑定事件 打开联系人
		$("#openContactModel").on("click",function () {
			$("#searchContactModal").modal("show")
			//展示联系人活动列表
			showContact();
		})

		//为搜索框绑定事件，展示市场活动列表
		$("#aname").keydown(function (event) {
			if (event.keyCode == 13){
				//展示市场活动列表
				showActivity();
				return false;
			}
		})

		//为搜索框绑定事件，展示联系人活动列表
		$("#cname").keydown(function (event) {
			if (event.keyCode == 13){
				//展示市场活动列表
				showContact();
				return false;
			}
		})

		//为提交市场活动按钮绑定事件,填充市场活动源 获取 市场名字和id
		$("#submitActivityBtn").on("click",function () {
			alert("点击了")
			//取得选中的id
			var $xz = $("input[name=tran]:checked");
			var id = $xz.val();
			//取得选择市场活动的名字
			var name = $("#"+id).html();
			alert(name)
			alert(id)
			//将以上信息填写到交易表单市场活动源中
			$("#activityName").val(name)
			$("#create-activityId").val(id)

			//关闭模态窗口
			$("#searchActivityModal").modal("hide")

		})

		//为提交联系人活动按钮绑定事件,填充联系人名称 获取 联系人名字和id
		$("#submitContactBtn").on("click",function () {
			//取得选中的id
			var $xz = $("input[name=contacts]:checked");
			var id = $xz.val();
			//取得选择市场活动的名字
			var name = $("#"+id).html();
			alert(name)
			alert(id)
			//将以上信息填写到交易表单市场活动源中
			$("#contactsName").val(name)
			$("#create-contactsId").val(id)

			//关闭模态窗口
			$("#searchContactModal").modal("hide")

		})

		//修改按钮
		$("#updateBtn").on("click",function () {
			$.ajax({
				url : "tran/updateTran.do",
				data : {
					"id":"${tran.id}",
					"owner":"${user.id}",
					"money":$.trim($("#edit-money").val()),
					"name":$.trim($("#edit-name").val()),
					"expectedDate":$.trim($("#edit-expectedClosingDate").val()),
					//客户名称
					"customerId":$.trim($("#edit-customerId").val()),
					"stage":$.trim($("#edit-stage").val()),
					"type":$.trim($("#edit-type").val()),
					"source":$.trim($("#edit-source").val()),
					"activityId":$.trim($("#edit-activityId").val()),
					"contactsId":$.trim($("#edit-contactsId").val()),
					"description":$.trim($("#edit-description").val()),
					"contactSummary":$.trim($("#edit-contactSummary").val()),
					"nextContactTime":$.trim($("#edit-nextContactTime").val())

				},
				type:"post",
				dataType : "json",
				success:function (data) {
					console.log(data)
					if(data>0){
						alert("修改成功")
						window.location.href="workbench/transaction/index.jsp";
					}else {
						alert("修改失败")
						window.location.href="workbench/transaction/edit.jsp";

					}
				}
			})
		})
	})

	//展示市场活动模态窗口
	function showActivity() {
		$.ajax({
			url : "clue/getConvertModelActivityList.do",
			data : {
				"name":$.trim($("#aname").val())
			},
			type:"get",
			dataType : "json",
			success:function (data) {
				console.log(data)
				var html = "";
				$.each(data,function (i,n) {
					html += '<tr>',
							html += '<td><input type="radio" name="tran" value="'+n.id+'"/></td>',
							html += '<td id="'+n.id+'">'+n.name+'</td>',
							html += '<td>'+n.startDate+'</td>',
							html += '<td>'+n.endDate+'</td>',
							html += '<td>'+n.owner+'</td>',
							html += '</tr>'
				})
				$("#tranModelList").html(html)

			}
		})
	}
	//展示联系人模态窗口
	function showContact(){
		// alert($.trim($("#cname").val()))
		$.ajax({
			url : "tran/getConvertModelContactList.do",
			data : {
				"name":$.trim($("#cname").val())
			},
			type:"get",
			dataType : "json",
			success:function (data) {
				console.log(data)
				var html = "";
				$.each(data,function (i,n) {
					html += '<tr>',
							html += '<td><input type="radio" name="contacts" value="'+n.id+'"/></td>',
							html += '<td id="'+n.id+'">'+n.owner+'</td>',
							html += '<td>'+n.email+'</td>',
							html += '<td>'+n.mphone+'</td>',
							html += '</tr>'
				})
				$("#tranContactModelList").html(html)
			}
		})
	}
</script>
</head>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="searchActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="aname" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable4" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="tranModelList">
						<%--	<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary"  id="submitActivityBtn">提交</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="searchContactModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="cname" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="tranContactModelList">
<%--							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button"  class="btn btn-default" data-dismiss="modal" >取消</button>
					<button type="button" class="btn btn-primary"  id="submitContactBtn">提交</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>更新交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
			<button type="button" class="btn btn-default" id="quxiaoBtn">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="edit-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="edit-transactionOwner">
					<option></option>
					<c:forEach items="${userList}" var="u">
						<option value="${u.id}" ${user.id eq u.id ? "selected" : ""} >${u.name}</option>
					</c:forEach>
				</select>
			</div>
			<label for="edit-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-money" value="${tran.money}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-name" value="${tran.name}">
			</div>
			<label for="edit-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="date" class="form-control" id="edit-expectedClosingDate" value="${tran.expectedDate}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control"  value="${customerName}" placeholder="支持自动补全，输入客户不存在则新建">
				<input type="hidden"  id="edit-customerId"  value="${tran.customerId}">
			</div>
			<label for="edit-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="edit-stage">
				  <option></option>
				  <c:forEach items="${applicationScope.stage}" var="s">
					  <option value="${s.value}" ${tran.stage eq s.text ? "selected" : "" } >${s.text}</option>
				  </c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="edit-type">
				  <option></option>
				  <option>已有业务</option>
				  <option selected>新业务</option>
				</select>
			</div>
			<label for="edit-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="edit-possibility" value="90">
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="edit-source">
					<option></option>
					<c:forEach items="${applicationScope.source}" var="s">
						<option value="${s.value}" ${tran.source eq s.text ? "selected" : "" } >${s.text}</option>
					</c:forEach>
				</select>
			</div>
			<label for="edit-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="openSearchModel" data-toggle="modal" data-target="#findMarketActivity"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="activityName" value="${activityName}">
				<input type="hidden" id="edit-activityId" value="${tran.customerId}" >
			</div>
		</div>
		
		<div class="form-group">
			<label for="edit-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" id="openContactModel" data-toggle="modal" data-target="#findContacts"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="contactsName" value="${ContactName}">
				<input type="hidden"  id="edit-contactsId" value="${tran.contactsId}" >
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="edit-description" >${tran.description}</textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="edit-contactSummary">${tran.contactSummary}</textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="date" class="form-control" id="edit-nextContactTime" value="${tran.nextContactTime}">
			</div>
		</div>
		
	</form>
</body>
</html>