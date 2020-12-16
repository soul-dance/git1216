
<%
    //准备当前阶段
    Tran t = (Tran)request.getAttribute("tran");
    String currentStage = t.getStage();
    //准备当前阶段可能性
    String possibilty  = map.get(currentStage);

    //判断当前阶段

    if("0".equals(possibilty)){
        //如果当前阶段可能性为0 说明前7个一定是黑圈 后两个 一个红叉一个黑叉
        for (int i = 0; i < stageList.size(); i++) {
            DicValue dv = stageList.get(i);
            //取出每一个遍历出来的阶段
            String listStage = dv.getValue();
            //取出每一个可能性
            String listPossibilty = map.get(listStage);
            //如果遍历出来的可能性是0 说明就是后两个  一个是红叉 一个是黑叉
            if ("0".equals(listPossibilty)){
                //如果是当前阶段
                 if(listStage.equals(currentStage)){
                    //红叉
                 }else{
                   //黑叉
                 }
                //如果遍历出来的阶段可能性不是0 就是黑圈
            }else{
                //黑圈
            }
        }
    //如果不为0 前7个可能是绿圈 绿标 黑圈 后两个一定是黑叉
    }else{
        //当前阶段下标
        int index = 0;
        for (int i = 0; i < stageList.size(); i++) {
            DicValue dv = stageList.get(i);
            //取出每一个遍历出来的阶段
            String stage = dv.getValue();
            if (stage.equals(currentStage)){
                //当前阶段
                index = i;
                break;
            }
        }
        for (int i = 0; i < stageList.size(); i++) {
            DicValue dv = stageList.get(i);
            //取出每一个遍历出来的阶段
            String listStage = dv.getValue();
            //取出每一个可能性
            String listPossibilty = map.get(listStage);

            //如果取出来的可能性为0 说明是后两个阶段
            if ("0".equals(possibilty)){

                //黑叉
                //如果遍历出来的可能性不为0
            }else{
                //如果是当前阶段
                if (i == index){

                    //绿色标记

                    //如果小于当前阶段
                 }else if(i  < index){

                    //绿圈

                    //如果大于当前阶段
                 }else{
                    //黑圈

                 }
             }
        }

 }

%>
<script>
    //当前阶段
    var currentStage = stage;
    //当前阶段可能性
    var possibilty = $("#possibilty").html();

    //当前阶段的下标
    var index = index1;

    //前面正常阶段 和 后面丢失阶段分界点
    var point = "<%=point%>";

    //如果当前阶段的可能性为0 前7个都是黑圈 只需要 去判断后两个是一个红一个黑
    if (possibilty == "0"){
        //遍历前7个
        for (var i = 0; i < point;i++){
            //============前7个肯定是黑圈

        }
        //遍历后两个
        for (var i = point; i <"<%=stageList.size()%>";i++){
            // 如果是当前阶段 红叉
            if (i == index){
                //红叉
                //如果不是当前阶段
            }else{
                //黑叉
            }
        }
        //如果当前阶段的可能性不为0 后两个肯定是黑叉 只需要判断前7个绿圈 绿标 和 黑圈
    }else{
        //遍历前7个
        for (var i = 0; i < point;i++){
            //如果是当前阶段
            if(i == index){
                //绿标
                //如果是小于当前阶段
            }else if(i < index){
                //绿圈
                //如果是大于当前阶段
            }else{
                //黑拳
            }
        }
        //遍历后两个
        for (var i = point; i <"<%=stageList.size()%>";i++){
            //黑叉

        }
    }
</script>