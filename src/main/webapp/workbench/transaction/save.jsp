<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

	Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
	Set<String> set =  pMap.keySet();
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
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

	<script>

		var json = {
			<%
				for (String key : set){
					String value = pMap.get(key);
			%>
				"<%=key%>" : <%=value%>,
			<%
				};
			%>
		};
		$(function () {
			//为保存按钮添加事件 执行添加操作
			$("#saveBtn").on("click",function () {
				//发出传统请求 提交表单
				$("#tranForm").submit();
			})

			//为阶段的下拉框,绑定选中的下拉框事件,根据选中的阶段填写可能性
			$("#create-state").on("change",function () {
				//获取选中的阶段
				var stage = $("#create-state").val();

				/**
				 * 传统取json值可以json.key的方式
				 * 但是在这里的stage是一个动态的 必须使用json[stage]的方式
				 */
				var possibility = json[stage];
				$("#create-possibility").val(possibility);
			})

			//自动补全
			$("#create-customerId").typeahead({
				source: function (query, process) {
					$.get(
							"tran/getCustomerName.do",
							{
								"name" : query
							},
							function (data) {
								//alert(data);
								process(data);
							},
							"json"
					);
				},
				delay: 500
			});
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
			alert($.trim($("#cname").val()))
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
						    <input type="text" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询" id="aname">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead >
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="tranModelList">

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
	<div class="modal fade" id="searchContactModal" role="dialog" >
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
						    <input type="text" class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询" id="cname">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead id="">
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="tranContactModelList">

						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary"  id="submitContactBtn">提交</button>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveBtn" >保存</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form action="tran/save.do" id="tranForm" class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionOwner" name="owner">
					<option></option>
					<c:forEach items="${userList}" var="u">
						<option value="${u.id}" ${user.id eq u.id ? "selected" : ""} >${u.name}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-money" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-money" name="money">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-transactionName" name="name">
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="date" class="form-control" id="create-expectedClosingDate" name="expectedDate" >
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-customerId" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-customerId" name="customerName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-state" name="stage">
			  	<option></option>
				  <c:forEach items="${applicationScope.stage}" var="s">
					  <option value="${s.value}">${s.text}</option>
				  </c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;" id="type">
				<select class="form-control" id="create-type" name="type">
				  <option></option>
				  <option>已有业务</option>
				  <option>新业务</option>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource" name="source">
				  <option></option>
					<c:forEach items="${applicationScope.source}" var="s">
						<option value="${s.value}">${s.text}</option>
					</c:forEach>

				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="openSearchModel" data-toggle="modal" data-target="#findMarketActivity"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="activityName" readonly>
				<input type="hidden" id="create-activityId" name="activityId">
			</div>
		</div>
		
		<div class="form-group">
			<label for="contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" id="openContactModel" data-toggle="modal" data-target="#findContacts"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="contactsName" readonly>
				<input type="hidden" id="create-contactsId"  name="contactsId"/>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe" name="description"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary" name="contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="date" class="form-control" id="create-nextContactTime" name="nextContactTime">
			</div>
		</div>
		
	</form>
</body>
</html>