<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/header.jsp"%>

<div class="maincontent">
	<div class="maincontentinner">
		<div class="row-fluid" style="min-height:300px;">
			<!--  如果是其它页面，这里是填充具体的内容。 -->
			<div class="widget">
				<h4 class="widgettitle">修改询单</h4>
				<div class="widgetcontent">

					<form id="verify_form" method="post" 
						action="${ctx }/market/modifyOrderSubmit.do">
						<table class="table table-striped table-bordered table-hover">
							<tr>
								<td>业务信息</td>
								<td>业务编号</td>
								<td><input class="span12" type="text" value="" readonly /></td>
								<td>接单时间</td>
								<td>${fn:substring(orderModel.order.orderTime,0,10) }</td>
								<td>接单业务员</td>
								<td>${orderModel.employee.nickName }</td>
							</tr>
							<tr>
								<td rowspan="3">客户信息</td>
								<td>客户编号</td>
								<td>姓名</td>
								<td>公司</td>
								<td>传真</td>
								<td>手机1</td>
								<td>手机2</td>
							</tr>
							<tr>
								<td>${orderModel.order.customerId }</td>
								<td>${orderModel.order.customerName }</td>
								<td>${orderModel.order.customerCompany }</td>
								<td>${orderModel.order.customerCompanyFax}</td>
								<td>${orderModel.order.customerPhone1}</td>
								<td>${orderModel.order.customerPhone2}</td>
							</tr>
							<tr>
								<td>公司地址</td>
								<td colspan="5">${orderModel.order.customerCompanyAddress}</td>
							</tr>
							<tr>
								<td rowspan="6">款式信息</td>
								<td><label>款式名称<span class="required">*</span></label></td>
								<td colspan="2">款式性别<span class="required">*</span></td>
								<td colspan="2">款式季节<span class="required">*</span></td>
								<td>订单来源<span class="required">*</span></td>
							</tr>
							<tr>
								<td><input type="text" class="span12" name="style_name" value="${orderModel.order.styleName }" /></td>
								<td colspan="2">
									<input type="radio" name="style_sex" value="男" ${orderModel.order.styleSex eq '男'?'checked="checked"':'' } /> <span>男</span> 
									<input type="radio" name="style_sex" value="女" ${orderModel.order.styleSex eq '女'?'checked="checked"':'' } /> <span>女</span> 
									<input type="radio" name="style_sex" value="儿童" ${orderModel.order.styleSex eq '儿童'?'checked="checked"':'' } /> <span>儿童</span>
								</td>
								<td colspan="2">
									<input type="radio" name="style_season" checked="checked" value="春夏" ${orderModel.order.styleSeason eq 'CHUNXIA'?'checked="checked"':'' } /> <span>春夏</span> 
									<input type="radio" name="style_season" value="秋冬" ${orderModel.order.styleSeason eq 'QIUDONG'?'checked="checked"':'' } /> <span>秋冬</span>
								</td>
								<td><input type="text" class="span12" name="order_source" value="${orderModel.order.orderSource }" /></td>
							</tr>
							<tr>
								<td>面料类型</td>
								<td colspan="5"><input type="radio" name="fabric_type" ${orderModel.order.fabricType eq 'SUOZHI'?'checked="checked"':'' }
									value="梭织" /> <span>梭织</span> <input
									type="radio" name="fabric_type" value="针织" ${orderModel.order.fabricType eq 'ZHENZHI'?'checked="checked"':'' } /> <span>针织</span> <input
									type="radio" name="fabric_type" value="编织" ${orderModel.order.fabricType eq 'BIANZHI'?'checked="checked"':'' } /> <span>编织</span>
									<input type="radio" name="fabric_type" value="梭针混合" ${orderModel.order.fabricType eq 'SUOZHENHUNHE'?'checked="checked"':'' } /> <span>梭针混合</span>
									<input type="radio" name="fabric_type" value="针编混合" ${orderModel.order.fabricType eq 'ZHENBIANHUNHE'?'checked="checked"':'' } /> <span>阵编混合</span>
									<input type="radio" name="fabric_type" value="梭编混合" ${orderModel.order.fabricType eq 'SUOBIANHUNHE'?'checked="checked"':'' } /> <span>梭编混合</span></td>
							</tr>
							<tr>
								<td>特殊工艺</td>
								<td colspan="5"><input type="checkbox"
									name="special_process" value="水洗" ${fn:contains(orderModel.order.specialProcess,'SHUIXI')?'checked="checked"':'' } /> <span>水洗</span> <input
									type="checkbox" name="special_process" value="激光" ${fn:contains(orderModel.order.specialProcess,'JIGUANG')?'checked="checked"':'' } /> <span>激光</span>
									<input type="checkbox" name="special_process" value="压皱" ${fn:contains(orderModel.order.specialProcess,'YAZHOU')?'checked="checked"':'' } /> <span>压皱</span>
									<input type="checkbox" name="special_process" value="其他" ${fn:contains(orderModel.order.specialProcess,'其他')?'checked="checked"':'' } /> <input
									type="text" name="other_special_process" class="span2"
									value="其他" /></td>
							</tr>
							<tr>
								<td>其他说明</td>
								<td colspan="5"><input type="checkbox"
									name="other_requirements" value="有主标" ${fn:contains(orderModel.order.otherRequirements,'ZHUBIAO')?'checked="checked"':'' } /> 有主标 <input
									type="checkbox" name="other_requirements" value="有吊牌" ${fn:contains(orderModel.order.otherRequirements,'DIAOPAI')?'checked="checked"':'' } /> 有吊牌 <input
									type="checkbox" name="other_requirements" value="有水洗" ${fn:contains(orderModel.order.otherRequirements,'SHUIXI')?'checked="checked"':'' } /> 有水洗 <input
									type="checkbox" name="other_requirements" value="其他" ${fn:contains(orderModel.order.otherRequirements,'其他')?'checked="checked"':'' }/> <input
									type="text" class="span2" name="other_other_requirements"
									value="其他" /></td>
							</tr>
							<tr>
								<td>参考链接</td>
								<td colspan="5"><input class="span12" type="url" /></td>
							</tr>
							<tr>
								<td rowspan="2">加工信息</td>
								<td>加工件数<span class="required">*</span></td>
								<td colspan="2">最迟交货时间</td>
								<td colspan="2">完工时间（天）</td>
								<td>码数</td>
							</tr>
							<tr>
								<td><input class="span6" type="number" name="ask_amount" value="${orderModel.order.askAmount }" /></td>
								<td colspan="2"><input class="span6" type="date"
									name="ask_deliver_date" value="${fn:substring(orderModel.order.askDeliverDate,0,10) }" /></td>
								<td colspan="2"><input class="span3" type="number"
									name="ask_produce_period" value="${orderModel.order.askProducePeriod }" /></td>
								<td><select class="span6" name="ask_code_number">
										<option value="XS" ${orderModel.order.askCodeNumber eq 'XS'?'selected':'' } >XS</option>
										<option value="S" ${orderModel.order.askCodeNumber eq 'S'?'selected':'' } >S</option>
										<option value="L" ${orderModel.order.askCodeNumber eq 'L'?'selected':'' } >L</option>
										<option value="XL" ${orderModel.order.askCodeNumber eq 'XL'?'selected':'' } >XL</option>
										<option value="XXL" ${orderModel.order.askCodeNumber eq 'XXL'?'selected':'' } >XXL</option>
								</select></td>
							</tr>
							<tr>
								<td>面料<input id="fabric_name" type="hidden" name="fabric_name" />
									<input id="fabric_amount" type="hidden" name="fabric_amount" /></td>
								<td colspan="6" class="innertable"><table
										class="span12 table fabric_table">
										<tr>
											<td>面料名称</td>
											<td>面料克重</td>
											<td>操作</td>
										</tr>
										<tr class="addrow">
											<td><input class="span12" type="text" /></td>
											<td><input class="span12" type="text" /></td>
											<td><a>添加</a></td>
										</tr>
										<c:forEach var="fabricRow" items="${orderModel.fabrics }" >
											<tr>
												<td class='span12 fabric_name'>${fabricRow.fabricName }</td>
												<td class='span12 fabric_amount'>${fabricRow.fabricAmount }</td>
												<td class='span12'><a onclick="deleteRow(this,'fabric_table')">删除</a></td>
											</tr>
										</c:forEach>
									</table></td>
							</tr>
							<tr>
								<td>辅料<input id="accessory_name" type="hidden" name="accessory_name" />
									<input id="accessory_query" type="hidden" name="accessory_query" /></td>
								<td colspan="6" class="innertable"><table 
										class="span12 table accessory_table">
										<tr>
											<td>辅料名称</td>
											<td>辅料要求</td>
											<td>操作</td>
										</tr>
										<tr class="addrow">
											<td><input class="span12" type="text" /></td>
											<td><input class="span12" type="text" /></td>
											<td><a>添加</a></td>
										</tr>
										<c:forEach var="accessoryRow" items="${orderModel.accessorys }" >
											<tr>
												<td class='span12 accessory_name'>${accessoryRow.accessoryName }</td>
												<td class='span12 accessory_query'>${accessoryRow.accessoryQuery }</td>
												<td class='span12'><a onclick="deleteRow(this,'accessory_table')">删除</a></td>
											</tr>
										</c:forEach>
									</table></td>
							</tr>
							<tr>
								<td rowspan="2">客户样衣</td>
								<td>提供样衣</td>
								<td colspan="2">邮寄时间</td>
								<td>快递名称</td>
								<td colspan="2">快递单号</td>
							</tr>
							<tr>
								<td><input type="radio" name="has_posted_sample_clothes" ${orderModel.order.hasPostedSampleClothes==1?'checked="checked"':'' }
									value="1" /> 是 <input type="radio" ${orderModel.order.hasPostedSampleClothes==0?'checked="checked"':'' }
									name="has_posted_sample_clothes" value="0" /> 否</td>
								<td colspan="2"><input class="span6" type="date"
									name="in_post_sample_clothes_time" value="${fn:substring(orderModel.logistics.inPostSampleClothesTime,0,10) }" /></td>
								<td><input class="span12" type="text"
									name="in_post_sample_clothes_type" value="${orderModel.logistics.inPostSampleClothesType }" /></td>
								<td colspan="2"><input class="span12" type="text"
									name="in_post_sample_clothes_number" value="${orderModel.logistics.inPostSampleClothesNumber }" /></td>
							</tr>
							<tr>
								<td colspan="7" class="innertable">
									<table class="span12 table produce_table">
										<tbody>
											<tr>
												<td colspan="8">大货加工具体要求
													<input id="produce_color" type="hidden" name="produce_color" />
													<input id="produce_xs" type="hidden" name="produce_xs" />
													<input id="produce_s" type="hidden" name="produce_s" />
													<input id="produce_m" type="hidden" name="produce_m" />
													<input id="produce_l" type="hidden" name="produce_l" />
													<input id="produce_xl" type="hidden" name="produce_xl" />
													<input id="produce_xxl" type="hidden" name="produce_xxl" /></td>
											</tr>
											<tr>
												<td>颜色</td>
												<td>XS</td>
												<td>S</td>
												<td>M</td>
												<td>L</td>
												<td>XL</td>
												<td>XXL</td>
												<td>操作</td>
											</tr>
											<tr class="addrow">
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><a>添加</a></td>
											</tr>
											<c:forEach var="produceRow" items="${orderModel.produces }" >
												<tr>
													<td class='span12 produce_color'>${produceRow.color }</td>
													<td class='span12 produce_xs'>${produceRow.xs }</td>
													<td class='span12 produce_s'>${produceRow.s }</td>
													<td class='span12 produce_m'>${produceRow.m }</td>
													<td class='span12 produce_l'>${produceRow.l }</td>
													<td class='span12 produce_xl'>${produceRow.xl }</td>
													<td class='span12 produce_xxl'>${produceRow.xxl }</td>
													<td class='span12'><a onclick="deleteRow(this,'produce_table')">删除</a></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="7" class="innertable">
									<table class="span12 table version_table">
										<tbody>
											<tr>
												<td colspan="11">版型数据信息
													<input id="version_size" type="hidden" name="version_size" />
													<input id="version_centerBackLength" type="hidden" name="version_centerBackLength" />
													<input id="version_bust" type="hidden" name="version_bust" />
													<input id="version_waistLine" type="hidden" name="version_waistLine" />
													<input id="version_shoulder" type="hidden" name="version_shoulder" />
													<input id="version_buttock" type="hidden" name="version_buttock" />
													<input id="version_hem" type="hidden" name="version_hem" />
													<input id="version_trousers" type="hidden" name="version_trousers" />
													<input id="version_skirt" type="hidden" name="version_skirt" />
													<input id="version_sleeves" type="hidden" name="version_sleeves" /></td>
											</tr>
											<tr>
												<td>尺寸表/部位 </td>
												<td>后中长</td>
												<td>胸围</td>
												<td>腰围</td>
												<td>肩宽</td>
												<td>臀围</td>
												<td>下摆</td>
												<td>裤长</td>
												<td>裙长</td>
												<td>袖长</td>
												<td>操作</td>
											</tr>
											<tr class="addrow">
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><input type="text" class="span12"/></td>
												<td><a>添加</a></td>
											</tr>
											<c:forEach var="versionRow" items="${orderModel.versions }" >
												<tr>
													<td class='span12 version_size'>${versionRow.size }</td>
													<td class='span12 version_centerBackLength'>${versionRow.centerBackLength }</td>
													<td class='span12 version_bust'>${versionRow.bust }</td>
													<td class='span12 version_waistLine'>${versionRow.waistline }</td>
													<td class='span12 version_shoulder'>${versionRow.shoulder }</td>
													<td class='span12 version_buttock'>${versionRow.buttock }</td>
													<td class='span12 version_hem'>${versionRow.hem }</td>
													<td class='span12 version_trousers'>${versionRow.trousers }</td>
													<td class='span12 version_skirt'>${versionRow.skirt }</td>
													<td class='span12 version_sleeves'>${versionRow.sleeves }</td>
													<td class='span12'><a onclick="deleteRow(this,'version_table')">删除</a></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="7">版型数据信息</td>
							</tr>
							<tr>
								<td>面料</td>
								<td colspan="3"><input type="text" class="span12"/></td>
								<td>包装</td>
								<td colspan="2"><input type="text" class="span12"/></td>
							</tr>
							<tr>
								<td>版型</td>
								<td colspan="3"><input type="text" class="span12"/></td>
								<td>装箱</td>
								<td colspan="2"><input type="text" class="span12"/></td>
							</tr>
							<tr>
								<td>工艺</td>
								<td colspan="6"><input type="text" class="span12"/></td>
							</tr>
							<tr>
								<td rowspan="5">生产样衣</td>
								<td>制作样衣</td>
								<td colspan="2">邮寄时间</td>
								<td>快递名称</td>
								<td colspan="2">快递单号</td>
							</tr>
							<tr>
								<td><input type="radio" name="is_need_sample_clothes" ${orderModel.order.isNeedSampleClothes==1?'checked="checked"':'' }
									value="1" /> 是 <input type="radio" ${orderModel.order.isNeedSampleClothes==0?'checked="checked"':'' }
									name="is_need_sample_clothes" value="0" /> 否</td>
								<td colspan="2"><input class="span6" type="date"
									name="sample_clothes_time" value="${fn:substring(orderModel.logistics.sampleClothesTime,0,10) }" /></td>
								<td><input class="span12" type="text"
									name="sample_clothes_type" value="${orderModel.logistics.sampleClothesType }" /></td>
								<td colspan="2"><input class="span12" type="text"
									name="sample_clothes_number" value="${orderModel.logistics.sampleClothesNumber }" /></td>
							</tr>
							<tr>
								<td>邮寄人</td>
								<td>手机</td>
								<td colspan="4">邮寄地址</td>
							</tr>
							<tr>
								<td><input class="span12" type="text"
									name="sample_clothes_name" value="${orderModel.logistics.sampleClothesName }" /></td>
								<td><input class="span12" type="text"
									name="sample_clothes_phone" value="${orderModel.logistics.sampleClothesPhone }" /></td>
								<td colspan="4"><input class="span12" type="text"
									name="sample_clothes_address" value="${orderModel.logistics.sampleClothesAddress }" /></td>
							</tr>
							<tr>
								<td>其他备注</td>
								<td colspan="5"><input class="span12" type="text"
									name="sample_clothes_remark" value="${orderModel.logistics.sampleClothesRemark }" /></td>
							</tr>
							<tr>
								<td>样衣信息</td>
								<td>样衣图片</td>
								<td colspan="2"><input type="file"
									name="sample_clothes_picture" value="${orderModel.order.sampleClothesPicture }" /></td>
								<td>参考图片</td>
								<td colspan="2"><input type="file" name="reference_picture" value="${orderModel.order.referencePicture }" /></td>
							</tr>
							<tr>
								<td>操作</td>
								<td colspan="3"><a id="agree_detail" class="btn btn-primary btn-rounded"><i class="icon-ok icon-white"></i>保存</a></td>
								<td colspan="3"><a id="disagree_detail" class="btn btn-danger btn-rounded"><i class="icon-remove icon-white"></i>删除</a></td>
							</tr>
						</table>
						<input type="hidden" name="customerId" value="${orderModel.order.customerId}" />
						<input type="hidden" name="id" value="${orderModel.order.orderId}" />
						<input id="verify_val" type="hidden" name="editok" value="" />
						<input type="hidden" name="task_id" value="${orderModel.task.id}" />
					</form>
				</div>
				<!--widgetcontent-->
			</div>

		</div>
		<!--row-fluid-->



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
<link rel="stylesheet" href="${ctx}/css/order/add_order.css">
<script type="text/javascript" src="${ctx}/js/order/add_order.js"></script>
<script type="text/javascript" src="${ctx }/js/custom.js"></script>
<%@include file="/common/footer.jsp"%>
