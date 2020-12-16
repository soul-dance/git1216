<%@ page import="com.ry.utils.UUIDUtil" %>
<%@ page import="com.ry.utils.DateTimeUtil" %>
<%@ page import="com.ry.settings.pojo.User" %>
<%@ page language="java" pageEncoding="UTF-8" %>
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
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


<script type="text/javascript">

	$(function(){
		//页面加载完毕后触发一个方法 获取市场活动列表
		pageList(1,2);

		//为创建按钮绑定事件 打开添加操作的模态窗口
		$("#addBtn").click(function () {
			//设置日历
			$(".time").datetimepicker({
				minView: "month",
				language:  'UTF-8',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			//走后台,目的是走后台取得用户信息列表
			$ .ajax({
				url :"myActivity/getUserList.do",
				type:"get",
				dataType : "json",
				success:function (data) {
					console.log(data)
					//alert(data[0].name);
					/*
						data取的是一个json形式的用户数组
					 */
					var html = "<option></option>";
					$.each(data,function (i,n) {
						html +="<option value='"+n.id+"'>"+n.name+"</option>";
					})
					//将下拉列表动态写入
					$("#create-marketActivityOwner").html(html)

					//取得当前登录用户的id
					var  uuid = "${user.id}";
					//将当前登录的用户,设置为下拉框默认的值
					$("#create-marketActivityOwner").val(uuid)

					//所有者处理完成后打开模态窗
					$("#createActivityModal").modal("show");
				}
			})
		})

		//添加市场活动操作
		$("#saveBtn").click(function () {
			//获取表单信息
			$.ajax({
				url : "myActivity/save.do",
				data : {
					"id":"<%=UUIDUtil.getUUID() %>",
					"owner":$.trim($("#create-marketActivityOwner").val()), //所有者
					"name":$.trim($("#create-marketActivityName").val()),	//名称
					"startDate":$.trim($("#create-startTime").val()),	//开始日期
					"endDate":$.trim($("#create-endTime").val()), //结束日期
					"cost":$.trim($("#create-cost").val()), //成本
					"description":$.trim($("#create-describe").val()), //介绍
					"createTime":"<%=DateTimeUtil.getSysTime()%>",
					"creatBy":"${user.name}"
				},
				type:"post",
				dataType : "json",
				success:function (data) {
					/*
						data就是添加成功或者失败
					 */
					if(data.success){
						//成功后 刷新市场活动信息列表 并且关闭窗口
						//刷新市场活动列表
						/*
							$("#activityPage").bs_pagination('getOption', 'currentPage')
							操作后停留在当前页
							$("#activityPage").bs_pagination('getOption', 'rowsPerPage')
							操作后维持已经设置好的每页展示的记录数
						*/

						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//清空添加操作模态窗口中的数据
						$("#activityAddForm")[0].reset();
						//关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");
					}else {
						alert("添加失败")
					}
				}
			})
		})
		/*
			点击查询按钮时
		 */
		$("#searchBtn").click(function(){
			$("#hidden-name").val($.trim($("#search-name").val()))
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-startDate").val($.trim($("#search-startDate").val()))
			$("#hidden-endDate").val($.trim($("#search-endDate").val()))
			pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

		});


		/*
			为全选的复选框绑定事件,触发全选操作
		 */
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked",this.checked)
		})
		/*
			动态生成的元素 只能用on来触发事件
			语法：
				$(需要绑定的有效外层元素).on("绑定事件触发的方式","需要绑定的jq对象",回调函数)
		 */
		$("#activityBody").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length)
		})

		//为删除按钮绑定事件 执行删除操作
		$("#deleteBtn").click(function () {
			//找到所有选中的复选框
			var $xz = $("input[name=xz]:checked")
			if($xz.length == 0){
				alert("请选择需要删除的记录")
			}else{
				if (confirm("确定要删除选中的记录吗？")){
					//肯定选了
					//拼接url 参数
					var param = "";
					for (var i=0;i<$xz.length;i++){
						param += "id="+$xz[i].value+"&";
					}
					param = param.substring(0,param.length-1)
					$.ajax({
						url : "myActivity/delete.do",
						data :param,
						type:"post",
						dataType : "json",
						success:function (data) {
							if(data.success){
								//删除成功后 刷新市场活动 回到首页
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

								alert("删除成功")
							}else {
								alert("删除失败")
							}
						}
					})
				}
			}
		})

		//为修改按钮绑定事件,打开修改的模态窗口
		$("#editBtn").click(function () {
			//获取选中的复选框
			var $xz = $("input[name=xz]:checked")
			if($xz.length==0){
				alert("请选择需要修改的记录")
			}else if($xz.length >1){
				alert("一次只能修改一条记录")
			}else{
				//获取复选框的值 就是当前记录的id
				var id = $xz.val();
				//获取用户信息列表 和一条市场活动
				$.ajax({
					url : "myActivity/getUserListAndActivity.do",
					data : {
						"id":id
					},
					type:"get",
					dataType : "json",
					success:function (data) {
						/*
							data 中是一个用户列表 和一个市场活动
						 */
						//处理下拉框
						var html = "";
						$.each(data.uList,function (i,n) {
							html += "<option value="+n.id+" >" +n.name + "</option>";
						})
						$("#edit-marketActivityOwner").html(html);

						//处理单条市场记录
						$("#edit-marketActivityName").val(data.a.name); //名称
						$("#edit-startDate").val(data.a.startDate); //开始日期
						$("#edit-endDate").val(data.a.endDate); //结束日期
						$("#edit-cost").val(data.a.cost); //成本
						$("#edit-description").val(data.a.description); //描述
						$("#edit-id").val(data.a.id);

						//打开修改操作的模态窗口
						$("#editActivityModal").modal("show")
					}
				})
			}
		})

		//为更新按钮绑定事件
		$("#updateBtn").on("click",function () {
			//获取表单信息
			$.ajax({
				url : "myActivity/updateActivity.do",
				data : {
					"id":$.trim($("#edit-id").val()), //市场活动id
					"owner":$.trim($("#edit-marketActivityOwner").val()), //用户id
					"name":$.trim($("#edit-marketActivityName").val()),	//名称
					"startDate":$.trim($("#edit-startDate").val()),	//开始日期
					"endDate":$.trim($("#edit-endDate").val()), //结束日期
					"cost":$.trim($("#edit-cost").val()), //成本
					"description":$.trim($("#edit-description").val()), //介绍
					"editTime":"<%=DateTimeUtil.getSysTime()%>",
					"editBy":"${user.name}"
				},
				type:"post",
				dataType : "json",
				success:function (data) {
					/*
						data就是添加成功或者失败
					 */
					if(data.success){
						alert("修改成功")
						//成功后 刷新市场活动信息列表 并且关闭窗口
						//刷新市场活动列表 修改后停留当前页
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//关闭添加修改的模态窗口
						$("#editActivityModal").modal("hide");
					}else {
						alert("修改市场活动失败")
					}
				}

			})
		})
	});
	/*
		pageNo:页码
	 	pageSize:每页的记录条数

	 	需要调用该方法的情况：
	 		(1)加载页面时
	 		(2)添加 修改 删除时
	 		(3)点击查询时
	 		(4)点击分页组件时
	 */
	function pageList(pageNo,pageSize) {
		//将全选复选框的选中状态取消
		$("#qx").prop("checked",false)
		//查询之前 获取到隐藏域的内容 给了 搜索框
		$("#search-name").val($.trim($("#hidden-name").val()))
		$("#search-owner").val($.trim($("#hidden-owner").val()))
		$("#search-startDate").val($.trim($("#hidden-startDate").val()))
		$("#search-endDate").val($.trim($("#hidden-endDate").val()))

		$.ajax({
			url : "myActivity/pageList.do",
			data : {
				"pageNo":pageNo,
				"pageSize":pageSize,
				"owner":$.trim($("#search-owner").val()), //所有者
				"name":$.trim($("#search-name").val()),	//名称
				"startDate":$.trim($("#search-startDate").val()),	//开始日期
				"endDate":$.trim($("#search-endDate").val()) //结束日期
			},
			type:"get",
			dataType : "json",
			success:function(data) {
				/*
					data需要查询出来的数据
					1.总记录数
					2.市场活动集合
				 */
				console.log(data)
				var html = "";
				$.each(data.dataList,function (i,n) {
					html += '<tr class="active">',
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>',
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'myActivity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>',
					html += '<td>'+n.owner+'</td>',
					html += '<td>'+n.startDate+'</td>',
					html += '<td>'+n.endDate+'</td>',
					html += '</tr>'
				})
				$("#activityBody").html(html);

				//总页数
				var totalPages = data.pageTotal % pageSize == 0 ? data.pageTotal / pageSize : parseInt(data.pageTotal/pageSize)+1;
				console.log(totalPages + "是总页数")
				//数据处理完成后 结合分页插件
				$("#activityPage").bs_pagination({

					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.pageTotal, // 总记录条数
					visiblePageLinks: 3, // 显示几个卡片
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					/*
						在点击分页组件时触发
					 */
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}

</script>
</head>
<body>

	<!--做模糊查询-->
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label ">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label time">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<!--动态生成-->
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" >
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表123</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon" >结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>

			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage"></div>


			</div>
			
		</div>
		
	</div>
</body>
</html>