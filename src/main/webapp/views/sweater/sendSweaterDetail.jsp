<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/header.jsp"%>

<div class="maincontent">
	<div class="maincontentinner">
		<div class="row-fluid" style="min-height: 300px;">
			<!--  如果是其它页面，这里是填充具体的内容。 -->
			<ul class="nav nav-tabs detail" id="tab">
				<li class="task-name">毛衣外发</li>
				<li class="active"><a href="#produceList" data-toggle="tab">毛衣外发</a></li>
				<li><a href="#quote" data-toggle="tab">报价信息</a></li>
				<li><a href="#cad" data-toggle="tab">版型信息</a></li>
				<li><a href="#produce" data-toggle="tab">加工信息</a></li>
				<li><a href="#sample" data-toggle="tab">样衣信息</a></li>
				<li><a href="#material" data-toggle="tab">面辅信息</a></li>
				<li><a href="#basic" data-toggle="tab">基本信息</a></li>
			</ul>

			<div class="tab-content">
				<div class="tab-pane" id="basic">
					<%@include file="/views/common/basic.jsp"%>
				</div>
				<div class="tab-pane" id="material">
					<%@include file="/views/common/material.jsp"%>
				</div>
				<div class="tab-pane" id="sample">
					<%@include file="/views/common/sample.jsp"%>
				</div>
				<div class="tab-pane" id="produce">
					<%@include file="/views/common/produce.jsp"%>
				</div>
				<div class="tab-pane" id="cad">
					<%@include file="/views/common/cad.jsp"%>
				</div>
				<div class="tab-pane" id="quote">
					<%@include file="/views/common/quote.jsp"%>
				</div>
				<div class="tab-pane active" id="produceList">
					<form method="post" action="${ctx}/sweater/sendSweaterSubmit.do"
						onsubmit="return check()">
						<table
							class="table table-striped table-bordered table-hover detail">
							<tr>
								<td class="title" rowspan="${fn:length(orderInfo.produce)+1}">计划生产数量</td>
								<td class="title">颜色</td>
								<td class="title">XS</td>
								<td class="title">S</td>
								<td class="title">M</td>
								<td class="title">L</td>
								<td class="title">XL</td>
								<td class="title">XXL</td>
								<td class="title">均码</td>
							</tr>
							<c:forEach var="produce" items="${orderInfo.produce}">
								<tr>
									<td>${produce.color}</td>
									<td>${produce.xs}</td>
									<td>${produce.s}</td>
									<td>${produce.m}</td>
									<td>${produce.l}</td>
									<td>${produce.xl}</td>
									<td>${produce.xxl}</td>
									<td>${produce.j}</td>
								</tr>
							</c:forEach>
							<tr>
								<td class="title"><span class="required">*</span>加工方</td>
								<td colspan="8"><input class="span14" id="processing_side"
									name="processing_side" type="text" /></td>
							</tr>
							<tr>
								<td colspan="4"><span>外发时间 ：</span><input type="text" name="sendTime" id="input_day" readonly="readonly">
								</td>
								<td colspan="5"><span>负责人：</span><input type="text" readonly="readonly"
									name="Purchase_director" value="${employee_name}"></td>
							</tr>
						</table>
						<button class="btn btn-primary" onclick="history.back();">返回</button>
						<div class="action" style="float: right">
							<input type="hidden" name="orderId"
								value="${orderInfo.order.orderId }" /> <input type="hidden"
								name="taskId" value="${orderInfo.taskId }" /> <input
								id="verify_val" type="hidden" name="result" value="" /> <input
								id="agree_detail" type="submit" value="开始外发加工"
								class="btn btn-primary btn-rounded">
						</div>
						<br>
						<c:forEach var="produce" items="${orderInfo.produce}">
								<tr>
									<td><input class="span12 produce_color" type="hidden"
										value="${produce.color}"  name="produce_color"/></td>
									<td><input class="span12 produce_xs" type="hidden" 
										value="${produce.xs}"  name="produce_xs"/></td>
									<td><input class="span12 produce_s" type="hidden" 
										value="${produce.s}"  name="produce_s" /></td>
									<td><input class="span12 produce_m" type="hidden" 
										value="${produce.m}"  name="produce_m"/></td>
									<td><input class="span12 produce_l" type="hidden"
										value="${produce.l}"  name="produce_l"/></td>
									<td><input class="span12 produce_xl" type="hidden" 
										value="${produce.xl}"  name="produce_xl"/></td>
									<td><input class="span12 produce_xxl" type="hidden"
									    value="${produce.xxl}"  name="produce_xxl"/></td>
									<td><input class="span12 produce_j" type="hidden"
									    value="${produce.j}"  name="produce_j"/></td>
								</tr>
							</c:forEach>
					</form>

				</div>
			</div>
		</div>
		<!--row-fluid-->

		<input type="hidden" id="result" />

		<div class="footer">
			<div class="footer-left">
				<span>&copy; 2014. 江苏南通智造链有限公司.</span>
			</div>
		</div>
		<!--footer-->

	</div>
	<!--maincontentinner-->
</div>
<!--maincontent-->


<%@include file="/common/js_file.jsp"%>
<%@include file="/common/js_form_file.jsp"%>
<link rel="stylesheet" href="${ctx}/css/fmc/table.css">
<script type="text/javascript" src="${ctx}/js/fmc/table.js"></script>
<link rel="stylesheet" href="${ctx}/css/fmc/detail.css">
<link rel="stylesheet" href="${ctx}/css/order/add_order.css">
<script type="text/javascript" src="${ctx}/js/order/add_produce.js"></script>
<script type="text/javascript" src="${ctx }/js/custom.js"></script>
<script type="text/javascript" src="${ctx}/js/order/add_order.js"></script>
<script>
	jQuery(document).ready(function() {
		//确认完成大货生产
		jQuery("#agree_detail").click(function() {
			//加工方不能为空
			/* var processingSide = jQuery("input[name='processing_side']").val();
			if(processingSide == "" || processingSide == null){
				alert("请填写加工方信息");
				return;
			}
			
			if(confirm("确认完成大货外发生产？")){
				jQuery("#verify_val").val("true");
				getProduce();
				jQuery("#produce_detail_form").submit();
			} */
			$("#result").val(1);
		});
		//大货生产失败
		jQuery("#disagree_detail").click(function() {
			/* if(confirm("确认大货生产失败？")){
				jQuery("#verify_val").val("false");
				getProduce();
				jQuery("#produce_detail_form").submit();
			} */
			$("#result").val(0);
		});
	});

	function check() {
		if ($("#result").val() == 1) {
			var processingSide = jQuery("input[name='processing_side']").val();
			if (processingSide == "" || processingSide == null) {
				alert("请填写加工方信息");
				return false;
			}
			if (confirm("确认完成大货外发生产？")) {
				jQuery("#verify_val").val("true");
				getProduce();
			} else {
				return false;
			}
		} else {
			if (confirm("确认大货生产失败？")) {
				jQuery("#verify_val").val("false");
				getProduce();
			} else {
				return false;
			}
		}
	}
</script>
<%@include file="/common/footer.jsp"%>


