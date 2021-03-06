<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="/common/header.jsp"%>

<div class="maincontent">
	<div class="maincontentinner">
		<form id="verify_form" method="post" onsubmit="return verify();"
			action="${ctx }/market/modifyOrderSubmit.do"
			enctype="multipart/form-data">
			<div class="row-fluid" style="min-height:300px;">
				<!--  如果是其它页面，这里是填充具体的内容。 -->
				<ul class="nav nav-tabs detail" id="tab">
					<li class="task-name">修改询单</li>
					<li><a href="#comment" data-toggle="tab">修改意见</a></li>
					<li><a href="#cad" data-toggle="tab">版型信息</a></li>
					<li><a href="#produce" data-toggle="tab">加工信息</a></li>
					<li><a href="#sample" data-toggle="tab">样衣信息</a></li>
					<li><a href="#material" data-toggle="tab">面辅信息</a></li>
					<li class="active"><a href="#basic" data-toggle="tab">基本信息</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="basic">
						<table
							class="table table-striped table-bordered table-hover detail">
							<tr>
								<td class="title">业务信息</td>
								<td class="title">业务编号</td>
								<td>${orderModel.orderId }</td>
								<td class="title">接单时间</td>
								<td>${fn:substring(orderModel.order.orderTime,0,10) }</td>
								<td class="title">接单业务员</td>
								<td>${orderModel.employee.employeeName }</td>
							</tr>
							<tr>
								<td class="title" rowspan="3">客户信息</td>
								<td class="title">客户编号</td>
								<td class="title">姓名</td>
								<td class="title">公司</td>
								<td class="title">传真</td>
								<td class="title">手机1</td>
								<td class="title">手机2</td>
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
								<td class="title">公司地址</td>
								<td colspan="5">${orderModel.order.customerCompanyAddress}</td>
							</tr>
							<tr>
								<td class="title" rowspan="6">款式信息</td>
								<td class="title">款式名称<span class="required">*</span></td>
								<td class="title">衣服类型<span class="required">*</span></td>
								<td class="title">款式性别<span class="required">*</span></td>
								<td class="title" colspan="2">款式季节<span class="required">*</span></td>
								<td class="title">订单来源<span class="required">*</span></td>
							</tr>
							<tr>
								<td><input type="text" class="span12" name="style_name"
									value="${orderModel.order.styleName }" /></td>
								<td><input type="text" class="span12" name="clothes_type"
									value="${orderModel.order.clothesType }" /></td>	
								<td><input type="radio" name="style_sex"
									${orderModel.order.styleSex eq '男'?'checked="checked"':'' }
									value="男" /> <span>男</span> <input type="radio"
									name="style_sex" value="女"
									${orderModel.order.styleSex eq '女'?'checked="checked"':'' } />
									<span>女</span> <input type="radio" name="style_sex" value="儿童"
									${orderModel.order.styleSex eq '儿童'?'checked="checked"':'' } />
									<span>儿童</span></td>
								<td colspan="2"><input type="radio" name="style_season"
									${orderModel.order.styleSeason eq '春夏'?'checked="checked"':'' }
									value="春夏" /> <span>春夏</span> <input type="radio"
									name="style_season" value="秋冬"
									${orderModel.order.styleSeason eq '秋冬'?'checked="checked"':'' } />
									<span>秋冬</span></td>
								<td><input type="text" class="span12" name="order_source" required="required"
									value="${orderModel.order.orderSource }" /> 
									<input type="hidden" name="ishaoduoyi" value="${orderModel.order.isHaoDuoYi }" />
								</td>
							</tr>
							<tr>
								<td class="title">面料类型</td>
								<td colspan="5"><input type="radio" name="fabric_type"
									${orderModel.order.fabricType eq '梭织'?'checked="checked"':'' }
									value="梭织" /> <span>梭织</span> <input type="radio"
									name="fabric_type" value="针织"
									${orderModel.order.fabricType eq '针织'?'checked="checked"':'' } />
									<span>针织</span> <input type="radio" name="fabric_type"
									value="编织"
									${orderModel.order.fabricType eq '编织'?'checked="checked"':'' } />
									<span>编织</span> <input type="radio" name="fabric_type"
									value="梭针混合"
									${orderModel.order.fabricType eq '梭针混合'?'checked="checked"':'' } />
									<span>梭针混合</span> <input type="radio" name="fabric_type"
									value="针编混合"
									${orderModel.order.fabricType eq '针编混合'?'checked="checked"':'' } />
									<span>针编混合</span> <input type="radio" name="fabric_type"
									value="梭编混合"
									${orderModel.order.fabricType eq '梭编混合'?'checked="checked"':'' } />
									<span>梭编混合</span></td>
							</tr>
							<tr>
								<td class="title">特殊工艺</td>
								<td colspan="5"><input type="checkbox"
									name="special_process" value="水洗"
									${fn:contains(orderModel.order.specialProcess,'水洗')?'checked="checked"':'' } />
									<span>水洗</span> <input type="checkbox" name="special_process"
									value="激光"
									${fn:contains(orderModel.order.specialProcess,'激光')?'checked="checked"':'' } />
									<span>激光</span> <input type="checkbox" name="special_process"
									value="压皱"
									${fn:contains(orderModel.order.specialProcess,'压皱')?'checked="checked"':'' } />
									<span>压皱</span> <input type="checkbox" name="special_process"
									value="其他"
									${fn:contains(orderModel.order.specialProcess,'其他')?'checked="checked"':'' } />
									<input type="text" name="other_special_process" class="span2"
									placeholder="其他" /></td>
							</tr>
							<tr>
								<td class="title">其他说明</td>
								<td colspan="5"><input type="checkbox"
									name="other_requirements" value="有主标"
									${fn:contains(orderModel.order.otherRequirements,'有主标')?'checked="checked"':'' } />
									有主标 <input type="checkbox" name="other_requirements"
									value="有吊牌"
									${fn:contains(orderModel.order.otherRequirements,'有吊牌')?'checked="checked"':'' } />
									有吊牌 <input type="checkbox" name="other_requirements"
									value="有水洗"
									${fn:contains(orderModel.order.otherRequirements,'有水洗')?'checked="checked"':'' } />
									有水洗 <input type="checkbox" name="other_requirements" value="其他"
									${fn:contains(orderModel.order.otherRequirements,'其他')?'checked="checked"':'' } />
									<input type="text" class="span2"
									name="other_other_requirements" placeholder="其他" /></td>
							</tr>
							<tr>
								<td class="title">参考链接</td>
								<td colspan="5"><input class="span12" type="url" name="reference_url" value="${orderModel.order.referenceUrl }"/></td>
							</tr>
						</table>
					</div>
					<div class="tab-pane" id="material">
						<table class="table table-striped table-bordered table-hover detail">
							<tr>
								<td class="span4 title">面料<input id="fabric_name" type="hidden"
									name="fabric_name" /> <input id="fabric_amount" type="hidden"
									name="fabric_amount" /></td>
								<td class="innertable span12"><table
										class="span12 table accessory_table">
										<tr>
											<td class="span5 title">面料名称</td>
											<td class="span5 title">面料克重</td>
											<td class="span3 title">操作</td>
										</tr>
										<tr class="addrow">
											<td><input class="span12" type="text" /></td>
											<td><input class="span12" type="text" /></td>
											<td><a>添加</a></td>
										</tr>
										<c:forEach var="fabricRow" items="${orderModel.fabrics }">
											<tr>
												<td class='fabric_name'>${fabricRow.fabricName }</td>
												<td class='fabric_amount'>${fabricRow.fabricAmount }</td>
												<td><a onclick="deleteRow(this,'fabric_table')">删除</a></td>
											</tr>
										</c:forEach>
									</table></td>
							</tr>
							<tr>
								<td class="span2 title">辅料<input id="accessory_name"
									type="hidden" name="accessory_name" /> <input
									id="accessory_query" type="hidden" name="accessory_query" /></td>
								<td class="innertable span12"><table
										class="span12 table accessory_table">
										<tr>
											<td class="span5 title">辅料名称</td>
											<td class="span5 title">辅料要求</td>
											<td class="span3 title">操作</td>
										</tr>
										<tr class="addrow">
											<td><input class="span12" type="text" /></td>
											<td><input class="span12" type="text" /></td>
											<td><a>添加</a></td>
										</tr>
										<c:forEach var="accessoryRow"
											items="${orderModel.accessorys }">
											<tr>
												<td class='accessory_name'>${accessoryRow.accessoryName }</td>
												<td class='accessory_query'>${accessoryRow.accessoryQuery }</td>
												<td><a onclick="deleteRow(this,'accessory_table')">删除</a></td>
											</tr>
										</c:forEach>
									</table></td>
							</tr>
						</table>
					</div>
					<div class="tab-pane" id="sample">
						<table
							class="table table-striped table-bordered table-hover detail">
							<tr>
								<td class="title" rowspan="2">客户样衣</td>
								<td class="title">提供样衣</td>
								<td class="title" colspan="2">邮寄时间</td>
								<td class="title">快递名称</td>
								<td class="title" colspan="2">快递单号</td>
							</tr>
							<tr>
								<td>
									<div style="display: none;">
										<input type="radio" name="has_posted_sample_clothes"
											${orderModel.order.hasPostedSampleClothes>0?'checked="checked"':'' }
											value="1" /> 是 <input type="radio"
											${orderModel.order.hasPostedSampleClothes==0?'checked="checked"':'' }
											name="has_posted_sample_clothes" value="0" /> 否
									</div>
									${orderModel.order.hasPostedSampleClothes>0?'是':'否'}
								</td>
								<td colspan="2"><input class="span6" type="date"
									name="in_post_sample_clothes_time"
									value="${fn:substring(orderModel.logistics.inPostSampleClothesTime,0,10) }"  readonly="readonly"/></td>
								<td><%-- <select name="in_post_sample_clothes_type"
									style="margin: 0px" >
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType!='顺丰'}">
											<option value="顺丰" >顺丰</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType=='顺丰'}">
											<option value="顺丰" selected="selected">顺丰</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType!='韵达'}">
											<option value="韵达">韵达</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType=='韵达'}">
											<option value="韵达" selected="selected">韵达</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType!='圆通'}">
											<option value="圆通">圆通</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType=='圆通'}">
											<option value="圆通" selected="selected">圆通</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType!='中通'}">
											<option value="中通">中通</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType=='中通'}">
											<option value="中通" selected="selected">中通</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType!='申通'}">
											<option value="申通">申通</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType=='申通'}">
											<option value="申通" selected="selected">申通</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType!='汇通'}">
											<option value="汇通">汇通</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType=='汇通'}">
											<option value="汇通" selected="selected">汇通</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType!='EMS'}">
											<option value="EMS">EMS</option>
										</c:if>
										<c:if
											test="${orderModel.logistics.inPostSampleClothesType=='EMS'}">
											<option value="EMS" selected="selected">EMS</option>
										</c:if>
								</select> --%>
									<input type="text" name="in_post_sample_clothes_type" value="${orderModel.logistics.inPostSampleClothesType}"  readonly="readonly"/>
								</td>
								<td colspan="2"><input class="span12" type="text"
									name="in_post_sample_clothes_number"
									value="${orderModel.logistics.inPostSampleClothesNumber }"   readonly="readonly"/></td>
							</tr>
							<tr>
								<td class="title" rowspan="5">生产样衣</td>
								<td class="title">制作样衣</td>
								<td class="title">邮寄人</td>
								<td class="title">手机</td>
								<td class="title" colspan="3">邮寄地址</td>
							</tr>
							<tr>
								<td>
								<div></div>
								<input type="radio" name="is_need_sample_clothes"
									${orderModel.order.isNeedSampleClothes==1?'checked="checked"':'' }
									value="1"  onchange="change()" /> 是 <input type="radio"
									${orderModel.order.isNeedSampleClothes==0?'checked="checked"':'' }
									name="is_need_sample_clothes" onchange="change()"  value="0" /> 否</td>
								<td><input class="span12" type="text"
									name="sample_clothes_name"
									value="${orderModel.logistics.sampleClothesName }" /></td>
								<td><input class="span12" type="text"
									name="sample_clothes_phone"
									value="${orderModel.logistics.sampleClothesPhone }" /></td>
								<td colspan="3"><input class="span12" type="text"
									name="sample_clothes_address"
									value="${orderModel.logistics.sampleClothesAddress }" /></td>
							</tr>
							<tr>
								<td class="title">其他备注</td>
								<td colspan="5"><input class="span12" type="text"
									name="sample_clothes_remark"
									value="${orderModel.logistics.sampleClothesRemark }" /></td>
							</tr>
							<tr>
								<td class="title" colspan="3">样衣图片</td>
								<td class="title" colspan="3">参考图片</td>
							</tr>
							<tr>
								<td colspan="3">
									<c:if test="${orderModel.order.sampleClothesPicture!=null}">
										<a class="fancybox" href="${orderModel.order.sampleClothesPicture}" title="样衣图片">
											<img src="${orderModel.order.sampleClothesPicture}"
												style="max-height: 300px;" alt="样衣图片"></img></a>
										<br/>
									</c:if>
									<input type="file" name="sample_clothes_picture" value="" />
								</td>
								<td colspan="3">
									<c:if test="${orderModel.order.referencePicture!=null}">
										<a class="fancybox" href="${orderModel.order.referencePicture}" title="参考图片">
											<img src="${orderModel.order.referencePicture}"
												style="max-height: 300px;" alt="参考图片"></img></a>
										<br/>
									</c:if>
									<input type="file" name="reference_picture" value="" />
								</td>
							</tr>
						</table>
					</div>
					<div class="tab-pane" id="produce">
						<table
							class="table table-striped table-bordered table-hover detail">
							<tr>
								<td class="title" rowspan="2">加工信息</td>
								<td class="span2 title" colspan="2">样衣总数<span class="required">*</span></td>
								<td class="span2 title" colspan="2">大货总数<span class="required">*</span></td>
								<td class="span2 title" colspan="2">最迟交货时间</td>
								<td class="span2 title" colspan="2">完工时间（天）</td>
							</tr>
							<tr>
								<td class="span2" colspan="2"><input class="span6"
									type="number" name="sample_amount" readonly="readonly"
									value="${orderModel.order.sampleAmount }" /></td>
								<td class="span2" colspan="2"><input class="span6"
									type="number" name="ask_amount"  readonly="readonly"
									value="${orderModel.order.askAmount }" /></td>
								<td class="span2" colspan="2"><input class="span8"
									type="date" name="ask_deliver_date"  required="required"
									value="${fn:substring(orderModel.order.askDeliverDate,0,10) }" /></td>
								<td class="span2" colspan="2"><input class="span4"
									type="number" name="ask_produce_period" required="required"
									value="${orderModel.order.askProducePeriod }" /></td>
							</tr>
						</table>
						<table
							class="table table-striped table-bordered table-hover detail">
							<tr>
								<td class="title">大货加工具体要求 <input id="produce_color" type="hidden"
									name="produce_color" /> <input id="produce_xs" type="hidden"
									name="produce_xs" /> <input id="produce_s" type="hidden"
									name="produce_s" /> <input id="produce_m" type="hidden"
									name="produce_m" /> <input id="produce_l" type="hidden"
									name="produce_l" /> <input id="produce_xl" type="hidden"
									name="produce_xl" /> <input id="produce_xxl" type="hidden"
									name="produce_xxl" /><input id="produce_j" type="hidden"
									name="produce_j" /></td>
								<td colspan="9" class="innertable">
									<table class="span12 table produce_table">
										<tr>
											<td class="span1 title">颜色</td>
											<td class="span1 title">XS</td>
											<td class="span1 title">S</td>
											<td class="span1 title">M</td>
											<td class="span1 title">L</td>
											<td class="span1 title">XL</td>
											<td class="span1 title">XXL</td>
											<td class="span1 title">均码</td>
											<td class="span1 title">操作</td>
										</tr>
										<tr class="addrow">
											<td><input type="text" class="span12" /></td>
											<td><input type="text" class="span12" value="0" /></td>
											<td><input type="text" class="span12" value="0" /></td>
											<td><input type="text" class="span12" value="0" /></td>
											<td><input type="text" class="span12" value="0" /></td>
											<td><input type="text" class="span12" value="0" /></td>
											<td><input type="text" class="span12" value="0" /></td>
											<td><input type="text" class="span12" value="0" /></td>
											<td><a>添加</a></td>
										</tr>
										<c:forEach var="produceRow" items="${orderModel.produce }">
											<tr>
												<td class='produce_color'>${produceRow.color }</td>
												<td class='produce_xs'>${produceRow.xs }</td>
												<td class='produce_s'>${produceRow.s }</td>
												<td class='produce_m'>${produceRow.m }</td>
												<td class='produce_l'>${produceRow.l }</td>
												<td class='produce_xl'>${produceRow.xl }</td>
												<td class='produce_xxl'>${produceRow.xxl }</td>
												<td class='produce_j'>${produceRow.j }</td>
												<td><a onclick="deleteRow(this,'produce_table')">删除</a></td>
											</tr>
										</c:forEach>
									</table>
								</td>
							</tr>
							<tr>
								<td class="title">样衣加工具体要求 <input id="sample_produce_color" type="hidden"
									name="sample_produce_color" /> <input id="sample_produce_xs"
									type="hidden" name="sample_produce_xs" /> <input
									id="sample_produce_s" type="hidden" name="sample_produce_s" />
									<input id="sample_produce_m" type="hidden"
									name="sample_produce_m" /> <input id="sample_produce_l"
									type="hidden" name="sample_produce_l" /> <input
									id="sample_produce_xl" type="hidden" name="sample_produce_xl" />
									<input id="sample_produce_xxl" type="hidden"
									name="sample_produce_xxl" /><input id="sample_produce_j" type="hidden"
									name="sample_produce_j" /></td>
								<td colspan="7" class="innertable">
									<table class="span12 table sample_produce_table" id="sampletable">
										<tr>
											<td class="span1 title">颜色</td>
											<td class="span1 title">XS</td>
											<td class="span1 title">S</td>
											<td class="span1 title">M</td>
											<td class="span1 title">L</td>
											<td class="span1 title">XL</td>
											<td class="span1 title">XXL</td>
											<td class="span1 title">均码</td>
											<td class="span1 title">操作</td>
										</tr>
										<tr class="addrow" >
											<td><input type="text" class="span12" /></td>
											<td><input type="text" class="span12"  value="0"/></td>
											<td><input type="text" class="span12"  value="0"/></td>
											<td><input type="text" class="span12"  value="0"/></td>
											<td><input type="text" class="span12"  value="0"/></td>
											<td><input type="text" class="span12"  value="0"/></td>
											<td><input type="text" class="span12"  value="0"/></td>
											<td><input type="text" class="span12"  value="0"/></td>
											<td><a>添加</a></td>
										</tr>
										<c:forEach var="produceRow" items="${orderModel.sample }">
											<tr>
												<td class='sample_produce_color'>${produceRow.color }</td>
												<td class='sample_produce_xs'>${produceRow.xs }</td>
												<td class='sample_produce_s'>${produceRow.s }</td>
												<td class='sample_produce_m'>${produceRow.m }</td>
												<td class='sample_produce_l'>${produceRow.l }</td>
												<td class='sample_produce_xl'>${produceRow.xl }</td>
												<td class='sample_produce_xxl'>${produceRow.xxl }</td>
												<td class='sample_produce_j'>${produceRow.j }</td>
												<td><a onclick="deleteRow(this,'sample_produce_table')">删除</a></td>
											</tr>
										</c:forEach>
									</table>
								</td>
							</tr>
						</table>
					</div>
					<div class="tab-pane" id="cad">
						<table
							class="table table-striped table-bordered table-hover detail">
							<tr>
								<td colspan="7" class="innertable">
									<table
										class="span12 table table-striped table-hover detail version_table">
										<tbody>
											<tr>
												<td class="title" colspan="11">版型数据信息 <input id="version_size"
													type="hidden" name="version_size" /> <input
													id="version_centerBackLength" type="hidden"
													name="version_centerBackLength" /> <input
													id="version_bust" type="hidden" name="version_bust" /> <input
													id="version_waistLine" type="hidden"
													name="version_waistLine" /> <input id="version_shoulder"
													type="hidden" name="version_shoulder" /> <input
													id="version_buttock" type="hidden" name="version_buttock" />
													<input id="version_hem" type="hidden" name="version_hem" />
													<input id="version_trousers" type="hidden"
													name="version_trousers" /> <input id="version_skirt"
													type="hidden" name="version_skirt" /> <input
													id="version_sleeves" type="hidden" name="version_sleeves" /></td>
											</tr>
											<tr>
												<td class="span1 title">尺寸表/部位</td>
												<td class="span1 title">后中长</td>
												<td class="span1 title">胸围</td>
												<td class="span1 title">腰围</td>
												<td class="span1 title">肩宽</td>
												<td class="span1 title">臀围</td>
												<td class="span1 title">下摆</td>
												<td class="span1 title">裤长</td>
												<td class="span1 title">裙长</td>
												<td class="span1 title">袖长</td>
												<td class="span1 title">操作</td>
											</tr>
											<tr class="addrow">
												<td><input type="text" class="span12" /></td>
												<td><input type="text" class="span12" /></td>
												<td><input type="text" class="span12" /></td>
												<td><input type="text" class="span12" /></td>
												<td><input type="text" class="span12" /></td>
												<td><input type="text" class="span12" /></td>
												<td><input type="text" class="span12" /></td>
												<td><input type="text" class="span12" /></td>
												<td><input type="text" class="span12" /></td>
												<td><input type="text" class="span12" /></td>
												<td><a>添加</a></td>
											</tr>
											<c:forEach var="versionRow" items="${orderModel.versions }">
												<tr>
													<td class='version_size'>${versionRow.size }</td>
													<td class='version_centerBackLength'>${versionRow.centerBackLength }</td>
													<td class='version_bust'>${versionRow.bust }</td>
													<td class='version_waistLine'>${versionRow.waistline }</td>
													<td class='version_shoulder'>${versionRow.shoulder }</td>
													<td class='version_buttock'>${versionRow.buttock }</td>
													<td class='version_hem'>${versionRow.hem }</td>
													<td class='version_trousers'>${versionRow.trousers }</td>
													<td class='version_skirt'>${versionRow.skirt }</td>
													<td class='version_sleeves'>${versionRow.sleeves }</td>
													<td><a onclick="deleteRow(this,'version_table')">删除</a></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</td>
							</tr>
						</table>
						<table
							class="table table-striped table-bordered table-hover detail">
							<tr>
								<td class="span1 title" rowspan="4">版型数据</td>
								<td class="span1 title">面料</td>
								<td class="span3"><textarea class="span12" name="cadFabric"
										style="resize:none" rows="2">${orderModel.designCad.cadFabric }</textarea></td>
								<td class="span1 title">包装</td>
								<td class="span3"><textarea class="span12" name="cadBox"
										style="resize:none" rows="2">${orderModel.designCad.cadBox }</textarea></td>
							</tr>
							<tr>
								<td class="span1 title">版型</td>
								<td class="span3"><textarea class="span12"
										name="cadVersionData" style="resize:none" rows="2">${orderModel.designCad.cadVersionData }</textarea></td>
								<td class="span1 title">装箱</td>
								<td class="span3"><textarea class="span12"
										name="cadPackage" style="resize:none" rows="2">${orderModel.designCad.cadPackage }</textarea></td>

							</tr>
							<tr>
								<td class="span1 title">工艺</td>
								<td class="span3"><textarea class="span12" name="cadTech"
										style="resize:none" rows="2">${orderModel.designCad.cadTech }</textarea></td>
								<td class="span1 title">其他</td>
								<td class="span3"><textarea class="span12" name="cadOther"
										style="resize:none" rows="2">${orderModel.designCad.cadOther }</textarea></td>
							</tr>
						</table>
					</div>
					<div class="tab-pane" id="comment">
						<table
							class="table table-striped table-bordered table-hover detail">
							<tr>
								<td class="title">设计部意见</td>
								<td colspan="5">${designComment }</td>
							</tr>
							<tr>
								<td class="title">采购部意见</td>
								<td colspan="5">${purchaseComment }</td>
							</tr>
							<tr>
								<td class="title">生产部意见</td>
								<td colspan="5">${produceComment }</td>
							</tr>
						</table>
					</div>
				</div>

				<input type="hidden" name="customerId"
					value="${orderModel.order.customerId}" /> <input type="hidden"
					name="id" value="${orderModel.order.orderId}" /> <input
					id="verify_val" type="hidden" name="editok" value="" /> <input
					type="hidden" name="task_id" value="${orderModel.task.id}" />

				<!--widgetcontent-->
				<!--row-fluid-->
			</div>
			<button id="disagree_detail" class="btn btn-danger btn-rounded">
					<i class="icon-remove icon-white"></i>删除
				</button>
			<div class="action" style="float: right;">
				<button id="agree_detail" class="btn btn-primary btn-rounded">
					<i class="icon-ok icon-white"></i>保存
				</button>
				
			</div>
		</form>

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
<link rel="stylesheet" href="${ctx}/css/fmc/detail.css">
<link rel="stylesheet" href="${ctx}/css/order/add_order.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/zoom_img/jquery.fancybox.css" media="screen" />
<script type="text/javascript" src="${ctx}/js/zoom_img/jquery.fancybox.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('.fancybox').fancybox();
});
</script>

<script type="text/javascript" src="${ctx}/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="${ctx}/js/order/add_order.js"></script>
<script type="text/javascript" src="${ctx }/js/custom.js"></script>
<%@include file="/common/footer.jsp"%>

