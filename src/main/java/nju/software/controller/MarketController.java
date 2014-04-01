package nju.software.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Map;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import nju.software.dataobject.Accessory;
import nju.software.dataobject.Account;
import nju.software.dataobject.Customer;
import nju.software.dataobject.Fabric;
import nju.software.dataobject.Logistics;
import nju.software.dataobject.Money;
import nju.software.dataobject.Order;
import nju.software.dataobject.Product;
import nju.software.dataobject.Quote;
import nju.software.model.OrderInfo;
import nju.software.model.OrderModel;
import nju.software.model.QuoteConfirmTaskSummary;
import nju.software.model.QuoteModel;
import nju.software.service.BuyService;
import nju.software.service.CustomerService;
import nju.software.service.MarketService;
import nju.software.service.OrderService;
import nju.software.service.QuoteService;
import nju.software.util.DateUtil;
import nju.software.util.FileOperateUtil;
import nju.software.util.JbpmAPIUtil;
import nju.software.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.drools.runtime.process.WorkflowProcessInstance;
import org.jbpm.task.query.TaskSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class MarketController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private BuyService buyService;
	@Autowired
	private QuoteService quoteService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private JbpmAPIUtil jbpmAPIUtil;

	// test precondition
	@RequestMapping(value = "market/precondition.do", method = RequestMethod.GET)
	@Transactional(rollbackFor = Exception.class)
	public String precondition(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		List<TaskSummary> task1 = jbpmAPIUtil.getAssignedTasksByTaskname(
				"CAIGOUZHUGUAN", "Purchasing_accounting");

		List<TaskSummary> task2 = jbpmAPIUtil.getAssignedTasksByTaskname(
				"SHEJIZHUGUAN", "design_accounting");
		List<TaskSummary> task3 = jbpmAPIUtil.getAssignedTasksByTaskname(
				"SHENGCHANZHUGUAN", "business_accounting");
		try {
			for (TaskSummary s1 : task1) {

				jbpmAPIUtil.completeTask(s1.getId(), null, "CAIGOUZHUGUAN");
			}
			for (TaskSummary s1 : task2) {

				jbpmAPIUtil.completeTask(s1.getId(), null, "SHEJIZHUGUAN");
			}
			for (TaskSummary s1 : task3) {

				jbpmAPIUtil.completeTask(s1.getId(), null, "SHENGCHANZHUGUAN");
			}
			System.out.println("precodition satisfied");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return null;
	}

	@Autowired
	private MarketService marketService;

	// 专员修改报价
	@RequestMapping(value = "market/modifyOrderSum.do", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String modifyOrderSum(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		String innerPrice = request.getParameter("inner_price");
		String outerPrice = request.getParameter("outer_price");
		String orderId = request.getParameter("order_id");
		String s_taskId = request.getParameter("taskId");
		String s_processId = request.getParameter("processId");

		try {
			float inner = Float.parseFloat(innerPrice);
			float outer = Float.parseFloat(outerPrice);
			int id = Integer.parseInt(orderId);
			long taskId = Long.parseLong(s_taskId);
			long processId = Long.parseLong(s_processId);
			boolean success = quoteService.updateQuote(inner, outer, id,
					taskId, processId, "SHICHANGZHUANYUAN");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/market/quoteConfirmList.do";
	}

	//专员修改报价
	@RequestMapping(value = "market/quoteModifyList.do", method = RequestMethod.GET)
	@Transactional(rollbackFor = Exception.class)
	public String modifyOrderSumDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		
		String orderId=request.getParameter("id");
		String s_processId=request.getParameter("pid");
		int id=Integer.parseInt(orderId);
		long processId=Long.parseLong(s_processId);
		QuoteModel quoteModel = orderService.getQuoteByOrderAndPro("SHICHANGZHUANYUAN", "edit_quoteorder", id, processId);
		model.addAttribute("quoteModel", quoteModel);
		return "market/modify_quote_order";
	}

	// 顾客下单的列表页面
	@RequestMapping(value = "market/customerOrder.do", method = RequestMethod.GET)
	@Transactional(rollbackFor = Exception.class)
	public String customerOrder(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Map params = new HashMap();
		params.put("page", 1);
		params.put("number_per_page", 100);
		List list = customerService.listCustomer(params);
		model.addAttribute("customer_list", list.get(0));
		return "market/customer_order";
	}

	// 专员合并报价
	@RequestMapping(value = "market/computerOrderSum.do", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String computerOrderSum(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Map params = new HashMap();
		params.put("page", 1);
		params.put("number_per_page", 100);

		String innerPrice = request.getParameter("inner_price");
		String outerPrice = request.getParameter("outer_price");
		String orderId = request.getParameter("order_id");
		String s_taskId = request.getParameter("taskId");
		String s_processId = request.getParameter("processId");

		try {
			float inner = Float.parseFloat(innerPrice);
			float outer = Float.parseFloat(outerPrice);
			int id = Integer.parseInt(orderId);
			long taskId = Long.parseLong(s_taskId);
			long processId = Long.parseLong(s_processId);
			boolean success = quoteService.updateQuote(inner, outer, id,
					taskId, processId, "SHICHANGZHUANYUAN");

			return "redirect:/market/computerOrderSumList.do";
		} catch (Exception e) {

		}
		return "redirect:/market/computerOrderSumList.do";
	}

	// 专员合并报价List
	@RequestMapping(value = "market/computerOrderSumList.do", method = RequestMethod.GET)
	@Transactional(rollbackFor = Exception.class)
	public String computerOrderSumList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		/*
		 * Map params = new HashMap(); params.put("page", 1);
		 * params.put("number_per_page", 100); List list =
		 * customerService.listCustomer(params);
		 * model.addAttribute("customer_list", list.get(0));
		 */
		String actor = "SHICHANGZHUANYUAN";
		String taskName = "quote";
		List<QuoteModel> quoteModelList = orderService.getQuoteByActorAndTask(
				actor, taskName);

		model.addAttribute("quote_list", quoteModelList);
		return "market/quote_order_list";
	}

	// 主管审核报价
	@RequestMapping(value = "market/checkOrderSum.do", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String checkOrderSum(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		/*
		 * Map params = new HashMap(); params.put("page", 1);
		 * params.put("number_per_page", 100); List list =
		 * customerService.listCustomer(params);
		 * model.addAttribute("customer_list", list.get(0));
		 */
		String innerPrice = request.getParameter("inner_price");
		String outerPrice = request.getParameter("outer_price");
		String orderId = request.getParameter("order_id");
		String s_taskId = request.getParameter("taskId");
		String s_processId = request.getParameter("processId");

		try {
			float inner = Float.parseFloat(innerPrice);
			float outer = Float.parseFloat(outerPrice);
			int id = Integer.parseInt(orderId);
			long taskId = Long.parseLong(s_taskId);
			long processId = Long.parseLong(s_processId);
			boolean success = quoteService.updateQuote(inner, outer, id,
					taskId, processId, "SHICHANGZHUGUAN");
			return "redirect:/market/checkOrderSumList.do";
		} catch (Exception e) {

		}
		return "redirect:/market/checkOrderSumList.do";

	}

	// 主管审核报价List
	@RequestMapping(value = "market/checkOrderSumList.do", method = RequestMethod.GET)
	@Transactional(rollbackFor = Exception.class)
	public String checkOrderSumList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		/*
		 * Map params = new HashMap(); params.put("page", 1);
		 * params.put("number_per_page", 100); List list =
		 * customerService.listCustomer(params);
		 */
		String actor = "SHICHANGZHUGUAN";
		String taskName = "check_quote";
		List<QuoteModel> quoteModelList = orderService.getQuoteByActorAndTask(
				actor, taskName);

		model.addAttribute("quote_list", quoteModelList);
		return "market/check_quote_order_list";

	}

	// 修改询单的列表
	@RequestMapping(value = "market/sampleOrderList.do", method = RequestMethod.GET)
	@Transactional(rollbackFor = Exception.class)
	public String orderList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		// Map params = new HashMap();
		// params.put("page", 1);
		// params.put("number_per_page", 100);
		List<OrderModel> orderModelList = orderService
				.getOrderByActorIdAndTaskname("SHICHANGZHUANYUAN", "edit_order");
		// Logistics logistics =
		// buyService.getLogisticsByOrderId(orderId_request);
		// List<Fabric> fabricList =
		// buyService.getFabricByOrderId(orderId_request);
		// List<Accessory> accessoryList =
		// buyService.getAccessoryByOrderId(orderId_request);
		model.put("order_list", orderModelList);
		return "market/sample_order_list";
	}

	// 询单的修改界面
	@RequestMapping(value = "market/modify.do", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String modifyOrder(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		String s_id = request.getParameter("id");
		String s_task_id = request.getParameter("task_id");
		String s_process_id = request.getParameter("process_id");
		String modify = request.getParameter("modify");
		try {
			int id = Integer.parseInt(s_id);
			long task_id = Long.parseLong(s_task_id);
			long process_id = Long.parseLong(s_process_id);
			if (Integer.parseInt(modify) == 1) {

				// 修改
				OrderModel orderModel = orderService.getOrderDetail(
						Integer.parseInt(s_id), Long.parseLong(s_task_id),
						Long.parseLong(s_process_id));
				Logistics logistics = buyService.getLogisticsByOrderId(Integer
						.parseInt(s_id));
				List<Fabric> fabricList = buyService.getFabricByOrderId(Integer
						.parseInt(s_id));
				List<Accessory> accessoryList = buyService
						.getAccessoryByOrderId(Integer.parseInt(s_process_id));
				model.addAttribute("orderModel", orderModel);
				model.addAttribute("logistics", logistics);
				model.addAttribute("fabric_list", fabricList);
				model.addAttribute("accessory_list", accessoryList);

				WorkflowProcessInstance process = (WorkflowProcessInstance) jbpmAPIUtil
						.getKsession().getProcessInstance(
								Long.parseLong(s_process_id));
				String buyComment = process.getVariable("buyComment")
						.toString();
				String designComment = process.getVariable("designComment")
						.toString();
				// String
				// productComment=process.getVariable("productComment").toString();
				model.addAttribute("buyComment", buyComment);
				model.addAttribute("designComment", designComment);
				// model.addAttribute("productComment",productComment);
				return "market/modify_order";
			} else {

				// 删除
				WorkflowProcessInstance process = (WorkflowProcessInstance) jbpmAPIUtil
						.getKsession().getProcessInstance(
								Long.parseLong(s_process_id));
				String buyComment = process.getVariable("buyComment")
						.toString();
				String designComment = process.getVariable("designComment")
						.toString();
				// String
				// productComment=process.getVariable("productComment").toString();
				orderService.verify(id, task_id, process_id, false, buyComment,
						designComment, null);
				return "redirect:/market/sampleOrderList";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/market/sampleOrderList";
	}

	// 询单的修改界面
	@RequestMapping(value = "market/doModify.do", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String doModifyOrder(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		String s_id = request.getParameter("id");
		String s_task_id = request.getParameter("task_id");
		String s_process_id = request.getParameter("process_id");
		// 保存修改该的order数据，accessory，fabric，logistics
		// 订单数据

		Integer employeeId = 6;
		String orderState = "A";
		Timestamp orderTime = new Timestamp(new Date().getTime());

		String styleName = request.getParameter("style_name");
		String fabricType = request.getParameter("fabric_type");
		String styleSex = request.getParameter("style_sex");
		String styleSeason = request.getParameter("style_season");
		String specialProcess = StringUtils.join(
				request.getParameterValues("special_process"), "|");
		String otherRequirements = StringUtils.join(
				request.getParameterValues("other_requirements"), "|");
		Calendar calendar = Calendar.getInstance();

		Integer askAmount = Integer
				.parseInt(request.getParameter("ask_amount"));
		String askProducePeriod = request.getParameter("ask_produce_period");
		Timestamp askDeliverDate = getTime(request
				.getParameter("ask_deliver_date"));
		String askCodeNumber = request.getParameter("ask_code_number");
		Short hasPostedSampleClothes = Short.parseShort(request
				.getParameter("has_posted_sample_clothes"));
		Short isNeedSampleClothes = Short.parseShort(request
				.getParameter("is_need_sample_clothes"));
		String orderSource = request.getParameter("order_source");

		// 面料数据
		String fabric_names = request.getParameter("fabric_name");
		String fabric_amounts = request.getParameter("fabric_amount");
		String fabric_name[] = fabric_names.split(",");
		String fabric_amount[] = fabric_amounts.split(",");
		List<Fabric> fabrics = new ArrayList<Fabric>();
		for (int i = 0; i < fabric_name.length; i++) {
			fabrics.add(new Fabric(0, fabric_name[i], fabric_amount[i]));
		}

		// 辅料数据
		String accessory_names = request.getParameter("accessory_name");
		String accessory_querys = request.getParameter("accessory_query");
		String accessory_name[] = accessory_names.split(",");
		String accessory_query[] = accessory_querys.split(",");
		List<Accessory> accessorys = new ArrayList<Accessory>();
		for (int i = 0; i < fabric_name.length; i++) {
			accessorys.add(new Accessory(0, accessory_name[i],
					accessory_query[i]));
		}

		// 物流数据
		Logistics logistics = new Logistics();
		String in_post_sample_clothes_time = request
				.getParameter("in_post_sample_clothes_time");
		String in_post_sample_clothes_type = request
				.getParameter("in_post_sample_clothes_type");
		String in_post_sample_clothes_number = request
				.getParameter("in_post_sample_clothes_number");

		logistics
				.setInPostSampleClothesTime(getTime(in_post_sample_clothes_time));
		logistics.setInPostSampleClothesType(in_post_sample_clothes_type);
		logistics.setInPostSampleClothesNumber(in_post_sample_clothes_number);

		String sample_clothes_time = request
				.getParameter("sample_clothes_time");
		String sample_clothes_type = request
				.getParameter("sample_clothes_type");
		String sample_clothes_number = request
				.getParameter("sample_clothes_number");
		String sample_clothes_name = request
				.getParameter("sample_clothes_name");
		String sample_clothes_phone = request
				.getParameter("sample_clothes_phone");
		String sample_clothes_address = request
				.getParameter("sample_clothes_address");
		String sample_clothes_remark = request
				.getParameter("sample_clothes_remark");

		logistics.setSampleClothesTime(getTime(sample_clothes_time));
		logistics.setSampleClothesType(sample_clothes_type);
		logistics.setSampleClothesNumber(sample_clothes_number);
		logistics.setSampleClothesName(sample_clothes_name);
		logistics.setSampleClothesPhone(sample_clothes_phone);
		logistics.setSampleClothesAddress(sample_clothes_address);
		logistics.setSampleClothesRemark(sample_clothes_remark);

		// Order
		Order order = orderService.findByOrderId(s_id);
		order.setEmployeeId(employeeId);
		// order.setCustomerId(customerId);
		order.setOrderState(orderState);
		order.setOrderTime(orderTime);
		// order.setCustomerName(customerName);
		// order.setCustomerCompany(customerCompany);
		// order.setCustomerCompanyFax(customerCompanyFax);
		// order.setCustomerPhone1(customerPhone1);
		// order.setCustomerPhone2(customerPhone2);
		// order.setCustomerCompanyAddress(customerCompanyAddress);
		order.setStyleName(styleName);
		order.setFabricType(fabricType);
		order.setStyleSex(styleSex);
		order.setStyleSeason(styleSeason);
		order.setSpecialProcess(specialProcess);
		order.setOtherRequirements(otherRequirements);
		// order.setSampleClothesPicture(sampleClothesPicture);
		// order.setReferencePicture(referencePicture);
		order.setAskAmount(askAmount);
		order.setAskProducePeriod(askProducePeriod);
		order.setAskDeliverDate(askDeliverDate);
		order.setAskCodeNumber(askCodeNumber);
		order.setHasPostedSampleClothes(hasPostedSampleClothes);
		order.setIsNeedSampleClothes(isNeedSampleClothes);
		order.setOrderSource(orderSource);

		orderService.modifyOrder(order, fabrics, accessorys, logistics);
		// 推进流程
		int id = Integer.parseInt(s_id);
		long task_id = Long.parseLong(s_task_id);
		long process_id = Long.parseLong(s_process_id);
		WorkflowProcessInstance process = (WorkflowProcessInstance) jbpmAPIUtil
				.getKsession().getProcessInstance(Long.parseLong(s_process_id));
		String buyComment = process.getVariable("buyComment").toString();
		String designComment = process.getVariable("designComment").toString();
		// String
		// productComment=process.getVariable("productComment").toString();
		orderService.verify(id, task_id, process_id, true, buyComment,
				designComment, null);
		return "market/sample_order_list";
	}

	// 顾客下单的表单页面
	@RequestMapping(value = "market/add.do", method = RequestMethod.GET)
	@Transactional(rollbackFor = Exception.class)
	public String addEmployee(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String id = request.getParameter("cid");
		Customer customer = customerService.findByCustomerId(Integer
				.parseInt(id));
		model.addAttribute("customer", customer);
		HttpSession session = request.getSession();
		session.setAttribute("sample_clothes_picture", null);
		session.setAttribute("reference_picture", null);
		return "market/add_order";
	}

	// 提交表单的页面
	@RequestMapping(value = "market/addMarketOrder.do", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String addMarketOrder(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		// 订单数据
		Integer customerId = Integer.parseInt(request
				.getParameter("customerId"));
		Customer customer = customerService.findByCustomerId(customerId);
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("cur_user");
		Integer employeeId = account.getUserId();
		String orderState = "A";
		Timestamp orderTime = new Timestamp(new Date().getTime());
		String customerName = customer.getCustomerName();
		String customerCompany = customer.getCompanyName();
		String customerCompanyFax = customer.getCompanyFax();
		String customerPhone1 = customer.getContactPhone1();
		String customerPhone2 = customer.getContactPhone2();
		String customerCompanyAddress = customer.getCompanyAddress();
		String styleName = request.getParameter("style_name");
		String fabricType = request.getParameter("fabric_type");
		String styleSex = request.getParameter("style_sex");
		String styleSeason = request.getParameter("style_season");
		String specialProcess = StringUtils.join(
				request.getParameterValues("special_process"), "|");
		String otherRequirements = StringUtils.join(
				request.getParameterValues("other_requirements"), "|");
		Calendar calendar = Calendar.getInstance();

		String sampleClothesPicture = (String) session
				.getAttribute("sample_clothes_picture");
		String referencePicture = (String) session
				.getAttribute("reference_picture");
		// session.setAttribute("reference_picture", null);
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:ss");
		// String sampleClothesPicture=sdf.format(calendar.getTime());
		// String referencePicture=sdf.format(calendar.getTime());
		// FileOperateUtil.Upload(request, "sample_clothes_picture",
		// sdf.format(calendar.getTime()), "sample_clothes_picture");
		// FileOperateUtil.Upload(request, "reference_picture",
		// sdf.format(calendar.getTime()), "reference_picture");
		// System.out.println(session.getAttribute("sample_clothes_picture"));

		Integer askAmount = Integer
				.parseInt(request.getParameter("ask_amount"));
		String askProducePeriod = request.getParameter("ask_produce_period");
		Timestamp askDeliverDate = getTime(request
				.getParameter("ask_deliver_date"));
		String askCodeNumber = request.getParameter("ask_code_number");
		Short hasPostedSampleClothes = Short.parseShort(request
				.getParameter("has_posted_sample_clothes"));
		Short isNeedSampleClothes = Short.parseShort(request
				.getParameter("is_need_sample_clothes"));
		String orderSource = request.getParameter("order_source");

		// 面料数据
		String fabric_names = request.getParameter("fabric_name");
		String fabric_amounts = request.getParameter("fabric_amount");
		String fabric_name[] = fabric_names.split(",");
		String fabric_amount[] = fabric_amounts.split(",");
		List<Fabric> fabrics = new ArrayList<Fabric>();
		for (int i = 0; i < fabric_name.length; i++) {
			fabrics.add(new Fabric(0, fabric_name[i], fabric_amount[i]));
		}

		// 辅料数据
		String accessory_names = request.getParameter("accessory_name");
		String accessory_querys = request.getParameter("accessory_query");
		String accessory_name[] = accessory_names.split(",");
		String accessory_query[] = accessory_querys.split(",");
		List<Accessory> accessorys = new ArrayList<Accessory>();
		for (int i = 0; i < fabric_name.length; i++) {
			accessorys.add(new Accessory(0, accessory_name[i],
					accessory_query[i]));
		}

		// 物流数据
		Logistics logistics = new Logistics();
		String in_post_sample_clothes_time = request
				.getParameter("in_post_sample_clothes_time");
		String in_post_sample_clothes_type = request
				.getParameter("in_post_sample_clothes_type");
		String in_post_sample_clothes_number = request
				.getParameter("in_post_sample_clothes_number");

		logistics
				.setInPostSampleClothesTime(getTime(in_post_sample_clothes_time));
		logistics.setInPostSampleClothesType(in_post_sample_clothes_type);
		logistics.setInPostSampleClothesNumber(in_post_sample_clothes_number);

		String sample_clothes_time = request
				.getParameter("sample_clothes_time");
		String sample_clothes_type = request
				.getParameter("sample_clothes_type");
		String sample_clothes_number = request
				.getParameter("sample_clothes_number");
		String sample_clothes_name = request
				.getParameter("sample_clothes_name");
		String sample_clothes_phone = request
				.getParameter("sample_clothes_phone");
		String sample_clothes_address = request
				.getParameter("sample_clothes_address");
		String sample_clothes_remark = request
				.getParameter("sample_clothes_remark");

		logistics.setSampleClothesTime(getTime(sample_clothes_time));
		logistics.setSampleClothesType(sample_clothes_type);
		logistics.setSampleClothesNumber(sample_clothes_number);
		logistics.setSampleClothesName(sample_clothes_name);
		logistics.setSampleClothesPhone(sample_clothes_phone);
		logistics.setSampleClothesAddress(sample_clothes_address);
		logistics.setSampleClothesRemark(sample_clothes_remark);

		// Order
		Order order = new Order();
		order.setEmployeeId(employeeId);
		order.setCustomerId(customerId);
		order.setOrderState(orderState);
		order.setOrderTime(orderTime);
		order.setCustomerName(customerName);
		order.setCustomerCompany(customerCompany);
		order.setCustomerCompanyFax(customerCompanyFax);
		order.setCustomerPhone1(customerPhone1);
		order.setCustomerPhone2(customerPhone2);
		order.setCustomerCompanyAddress(customerCompanyAddress);
		order.setStyleName(styleName);
		order.setFabricType(fabricType);
		order.setStyleSex(styleSex);
		order.setStyleSeason(styleSeason);
		order.setSpecialProcess(specialProcess);
		order.setOtherRequirements(otherRequirements);
		order.setSampleClothesPicture(sampleClothesPicture);
		order.setReferencePicture(referencePicture);
		order.setAskAmount(askAmount);
		order.setAskProducePeriod(askProducePeriod);
		order.setAskDeliverDate(askDeliverDate);
		order.setAskCodeNumber(askCodeNumber);
		order.setHasPostedSampleClothes(hasPostedSampleClothes);
		order.setIsNeedSampleClothes(isNeedSampleClothes);
		order.setOrderSource(orderSource);

		orderService.addOrder(order, fabrics, accessorys, logistics);

		return "redirect:/market/customerOrder.do";
	}

	public static Timestamp getTime(String time) {
		Date outDate = DateUtil.parse(time, DateUtil.newFormat);
		return new Timestamp(outDate.getTime());
	}

	/**
	 * @author 莫其凡 :报价商定列表
	 */
	@RequestMapping(value = "market/quoteConfirmList.do")
	@Transactional(rollbackFor = Exception.class)
	public String quoteConfirmList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("cur_user");
		List<QuoteConfirmTaskSummary> tasks = marketService
				.getQuoteConfirmTaskSummaryList(account.getUserId());
		model.addAttribute("tasks", tasks);
		return "/market/quoteConfirmList";
	}

	/**
	 * @author 莫其凡 :提交报价商定结果
	 */
	@RequestMapping(value = "market/quoteConfirm.do")
	@Transactional(rollbackFor = Exception.class)
	public String quoteConfirm(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		String result = request.getParameter("result");
		String taskId = request.getParameter("taskId");
		marketService.completeQuoteConfirmTaskSummary(Long.parseLong(taskId),
				result);
		// 2=修改报价
		if (result.equals("2")) {
			return "redirect:/market/quoteModifyList.do";
		} else {
			return "redirect:/market/quoteConfirmList.do";
		}
	}

	/**
	 * @author 莫其凡 :提交报价商定结果
	 * 
	 */
	@RequestMapping(value = "market/uploadFile.do")
	@Transactional(rollbackFor = Exception.class)
	public void uploadFile(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		Account account = (Account) request.getSession().getAttribute(
				"cur_user");
		String dir = request.getSession().getServletContext()
				.getRealPath("/upload/temp/" + account.getUserId());
		String title = request.getParameter("title");
		File save = FileOperateUtil.Upload(request, dir, title, title);
		String result_json = "";
		if (save == null) {
			result_json = "fail";
		} else {
			result_json = "success";
			request.getSession().setAttribute(title, save.getAbsolutePath());
		}
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("result_json", result_json);
		sendJson(response, jsonobj);
	}

	public void sendJson(HttpServletResponse response, JSONObject jsonobj) {
		try {
			PrintWriter out = response.getWriter();
			out.print(jsonobj);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 确认合同加工单跳转链接
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "market/confirmProduct.do", method = RequestMethod.GET)
	@Transactional(rollbackFor = Exception.class)
	public String confirmSample(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		System.out.println("product confirm ================ show task");
		List<OrderModel> orderList = new ArrayList<OrderModel>();
		Account account = (Account) request.getSession().getAttribute(
				"cur_user");
		// String actorId = account.getUserRole();
		String actorId = "SHICHANGZHUANYUAN";
		System.out.println("actorId: " + actorId);
		String taskName = "comfirm_worksheet";
		orderList = orderService
				.getOrderByActorIdAndTaskname(actorId, taskName);
		if (orderList.isEmpty()) {
			System.out.println("no orderList ");
		}
		model.addAttribute("order_list", orderList);

		return "market/confirm_product";
	}

	/**
	 * 确认合同加工单
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "market/doConfirmProduct.do", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String doConfirmSample(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		System.out.println("product confirm ================");

		Account account = (Account) request.getSession().getAttribute(
				"cur_user");
		String s_orderId_request = (String) request.getParameter("orderId");
		int orderId_request = Integer.parseInt(s_orderId_request);
		String s_taskId = request.getParameter("taskId");
		long taskId = Long.parseLong(s_taskId);
		String s_processId = request.getParameter("processId");
		long processId = Long.parseLong(s_processId);
		String productAskAmount = request.getParameter("product_amount");
		String productColor = request.getParameter("product_color");
		String productStyle = request.getParameter("product_style");
		boolean comfirmworksheet = true;

		if ((productAskAmount != null) && (productColor != null)
				&& (productStyle != null)) {
			List<Product> productList = marketService.getProduct(
					orderId_request, productAskAmount, productColor,
					productStyle);
			marketService.confirmProduct(account, orderId_request, taskId,
					processId, comfirmworksheet, productList);
		}

		return "redirect:/market/confirmProdyct.do";
	}

	/**
	 * 确认合同加工单详情
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "market/confirmProductDetail.do", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String confirmSampleDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		System.out.println("product corfirm ================ show detail");
		OrderModel orderModel = null;
		Account account = (Account) request.getSession().getAttribute(
				"cur_user");
		// String actorId = account.getUserRole();
		String s_orderId_request = (String) request.getParameter("id");
		int orderId_request = Integer.parseInt(s_orderId_request);
		String s_taskId = request.getParameter("task_id");
		long taskId = Long.parseLong(s_taskId);
		String s_processId = request.getParameter("process_id");
		long processId = Long.parseLong(s_processId);
		orderModel = orderService.getOrderDetail(orderId_request, taskId,
				processId);
		model.addAttribute("orderModel", orderModel);

		return "market/confirm_product_detail";
	}

	/**
	 * 取消订单
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "market/cancelProduct.do", method = RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String cancelSample(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		System.out.println("cancel product ===============");
		Account account = (Account) request.getSession().getAttribute(
				"cur_user");
		String s_orderId_request = (String) request.getParameter("id");
		int orderId_request = Integer.parseInt(s_orderId_request);
		String s_taskId = request.getParameter("task_id");
		long taskId = Long.parseLong(s_taskId);
		String s_processId = request.getParameter("process_id");
		long processId = Long.parseLong(s_processId);
		boolean comfirmworksheet = false;
		marketService.confirmProduct(account, orderId_request, taskId,
				processId, comfirmworksheet, null);

		return "redirect:/market/confirmProduct.do";
	}

	// =================================以下为莫其凡的内容============================================
	@RequestMapping(value = "market/signContractList.do")
	@Transactional(rollbackFor = Exception.class)
	public String signContractList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Account account = (Account) request.getSession().getAttribute(
				"cur_user");
		List<OrderInfo> tasks = marketService.getOrderInfoList(account
				.getUserId());
		model.addAttribute("tasks", tasks);
		return "/market/signContractList";
	}

	@RequestMapping(value = "market/signContract.do")
	@Transactional(rollbackFor = Exception.class)
	public String signContract(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String orderId = request.getParameter("orderId");
		String taskId = request.getParameter("taskId");
		OrderInfo orderInfo = marketService.getOrderInfo(
				Integer.parseInt(orderId), Long.parseLong(taskId));
		model.addAttribute("task", orderInfo);
		return "/market/signContract";
	}

	@RequestMapping(value = "market/signContractSubmit.do")
	@Transactional(rollbackFor = Exception.class)
	public String signContractSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		String discount = request.getParameter("discount");
		String orderId = request.getParameter("orderId");
		String taskId = request.getParameter("taskId");

		marketService.completeSignContract(Integer.parseInt(orderId),
				Double.parseDouble(discount), Long.parseLong(taskId));

		return "redirect:/market/signContractList";
	}

	// =================================莫其凡的内容到此结束===========================================
}
