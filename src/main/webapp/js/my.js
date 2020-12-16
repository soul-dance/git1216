
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
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.jsp\';">'+n.name+'</a></td>',
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