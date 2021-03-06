<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/header.jsp"%>

<div class="maincontent">
	<div class="maincontentinner">
		<div class="row-fluid" style="min-height: 300px;">
			<!--  如果是其它页面，这里是填充具体的内容。 -->
			<ul class="nav nav-tabs detail" id="tab">
				<li class="task-name">毛衣样衣确认和工艺制作</li>
				<!-- 
 				<li><a href="#quote" data-toggle="tab">报价信息</a></li>
				<li><a href="#cad" data-toggle="tab">版型信息</a></li>
				<li><a href="#produce" data-toggle="tab">加工信息</a></li>
				<li><a href="#sample" data-toggle="tab">样衣信息</a></li>
				 -->

				<li class="active"><a href="#sweater" data-toggle="tab">毛衣样衣确认和工艺制作</a>
				</li>
				<li><a href="#sample" data-toggle="tab">样衣信息</a></li>
				<li><a href="#material" data-toggle="tab">面辅信息</a></li>
				<li><a href="#basic" data-toggle="tab">基本信息</a></li>
			</ul>

			<div class="tab-content">
				<div class="tab-pane" id="basic">
					<%@include file="/views/common/basic.jsp"%>
				</div>

				<div class="tab-pane" id="sample">
					<%@include file="/views/common/sample.jsp"%>
				</div>

				<div class="tab-pane" id="material">
					<%@include file="/views/common/material.jsp"%>
				</div>

				<div class="tab-pane active" id="sweater">

					<form method="post" 
						action="${ctx}/sweater/sweaterSampleAndCraftSubmit.do?taskId=${orderInfo.task.id}&orderId=${orderInfo.order.orderId}">
						<table
							class="table table-striped table-bordered table-hover detail">
							<tr>
								<td class="title" rowspan="4">款式信息</td>
								<td class="title">选择任务</td>
								<td colspan="5">
									<!-- 表示仓库里没有原料，需要去购买原料 ，需要打小样-->
									<c:if
										test="${orderInfo.order.buySweaterMaterialResult == true }">
									  <input type="radio" name="task_name" id="task_name1" value="打小样"  onchange="confim(1);"/><span>打小样</span>
									</c:if> 
									  <input type="radio" name="task_name" id="task_name2" value="制作工艺" onchange="confim(2);"><span>制作工艺</span>
									  <input type="radio" name="task_name" id="task_name3" value="制版打样" onchange="confim(3);"><span>制版打样</span> 
									  <input type="radio" name="task_name" id="task_name4" value="确认样衣" onchange="confim(4);"/><span>确认样衣</span>
								</td>
							</tr>
							<tr>
								<td class="title">完成时间</td>
								<td colspan="5"><input type="text" name="task_finish_date"
									required="required" id="input_day" readonly="readonly" /></td>
							</tr>
							<tr>
								<td class="title">负责人</td>
								<td colspan="5"><input type="text" name="person_in_charge"
									required="required" value="${employee_name}"  readonly="readonly"/></td>
							</tr>
							<tr>
								<td class="title">备注</td>
								<td colspan="5"><textarea class="span12"
										name="sweaterremark" placeholder="请输入备注"></textarea></td>
							</tr>

						</table>

						<table
							class="table table-striped table-bordered table-hover detail">
							<c:if test="${empty orderInfo.sweaterOperateRecord}">
								<tr>
									<td class="title" style="width: 22%; background: red;">操作记录</td>
									<td>无</td>
								</tr>
							</c:if>
							<c:if test="${!empty orderInfo.sweaterOperateRecord}">
								<tr>
									<td class="title" id="size"
										rowspan="${fn:length(orderInfo.sweaterOperateRecord)+1}"
										style="width: 22%; background: #ff0000;">操作记录</td>
									<td class="title">环节名称</td>
									<td class="title">完成时间</td>
									<td class="title">负责人</td>
									<td class="title">消息备注</td>
								</tr>
								<c:forEach var="operateRecord"
									items="${orderInfo.sweaterOperateRecord}" varStatus="status">
									<tr>
										<td><span id="taskName${status.index + 1}">${operateRecord.taskName}</span></td>
										<td>${operateRecord.operateTime}</td>
										<td>${operateRecord.operatePerson}</td>
										<td>${operateRecord.operateRemark}</td>
									</tr>
								</c:forEach>
							</c:if>
						</table>
						<!-- 表示仓库里有原料，不用去购买原料，直接到工艺制作 
			          <c:if test="${buySweaterMaterial == false }"></c:if>
			          -->

						<div class="action" style="float: right">
							<input id="save_this_send" class="btn btn-primary" type="submit" 
								value="保存记录" style="background-color: #1E90FF" />
						<input id="showSubmit" type="submit" value="完成毛衣样衣确认和工艺制作" class="btn btn-primary"/>
							<%-- <a id="showSubmit" href="${ctx}/sweater/sweaterSampleAndCraftSubmit.do?orderId=${orderInfo.order.orderId}&taskId=${orderInfo.task.id}"
								onclick="return confirmSendSampleSubmit();"
								class="btn btn-primary">完成毛衣样衣确认和工艺制作</a> --%>
							<!-- 隐藏标签，判断是否是最终的发货 -->
							<input id="is_final" type="hidden" name="isFinal" value="false" />
						</div>
					</form>
				</div>
			</div>
		</div>

		<button class="btn btn-primary" onclick="history.back();">返回</button>
	</div>


	<div class="footer">
		<div class="footer-left">
			<span>&copy; 2014. 江苏南通智造链有限公司.</span>
		</div>
	</div>

</div>

<%@include file="/common/js_file.jsp"%>
<%@include file="/common/js_form_file.jsp"%>
<link rel="stylesheet" href="${ctx}/css/fmc/table.css">
<link rel="stylesheet" href="${ctx}/css/order/add_order.css">
<script type="text/javascript" src="${ctx}/js/fmc/table.js"></script>
<link rel="stylesheet" href="${ctx}/css/fmc/detail.css">
<link rel="stylesheet" href="${ctx}/views/buy/buy.css">
<script type="text/javascript" src="${ctx}/views/buy/cost.js"></script>
<script type="text/javascript" src="${ctx}/js/order/add_order.js"></script>
<script type="text/javascript" src="${ctx }/js/custom.js"></script>
<script type="text/javascript">
	var  result = ${orderInfo.order.buySweaterMaterialResult};
	var length =null;
	var taskName =null;
	var checked =null;
	var disabled =null;
	var NotEmpty =${!empty orderInfo.sweaterOperateRecord};
	var rowLength = $("#size").attr("rowspan") - 1;	
	var taskName = $("#"+"taskName"+rowLength+"").text();
	if(NotEmpty == false){
		if(result){
			$("#task_name1").attr("checked", "checked");
		}else{
			$("#task_name2").attr("checked", "checked");
		}
	}else if("打小样" == taskName){
		length = 1;
	}else if("制作工艺" == taskName){
		length = 2;
	}else if("制版打样" == taskName){
		length = 3;
	}else if("确认样衣" == taskName){
		length = 3;
	}
	for(var i=0;i<length+1;i++){
		disabled = $("#"+"task_name"+i+"").attr("disabled","disabled");
		checked = $("#"+"task_name"+(length+1)+"").attr("checked", "checked");
	}
	if ("制版打样" == taskName) {
			$("#showSubmit").show();
			$("#save_this_send").hide();
			
	} else if("确认样衣" == taskName) {
			$("#showSubmit").hide();
			$("#save_this_send").hide();
	}else{
			$("#showSubmit").hide();
			$("#save_this_send").show();
		
}
	
function confim(Show){
	if(Show == 4){
		$("#showSubmit").show();
		$("#save_this_send").hide();
	}else{
		$("#showSubmit").hide();
		$("#save_this_send").show();
	}

}	
</script>
<script type="text/javascript">
//禁止按键F5
document.onkeydown = function (e) {
	var ev = window.event || e;
	var code = ev.keyCode || ev.which;
	if (code == 116) {
	ev.keyCode ? ev.keyCode = 0 : ev.which = 0;
	cancelBubble = true;
	return false;
	}
}

//禁止鼠标右键菜单
document.oncontextmenu = function(e){
         return false;
}
</script>
<%@include file="/common/footer.jsp"%>
