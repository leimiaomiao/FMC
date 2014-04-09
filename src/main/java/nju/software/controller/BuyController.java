package nju.software.controller;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nju.software.dataobject.Accessory;
import nju.software.dataobject.Account;
import nju.software.dataobject.Fabric;
import nju.software.dataobject.Logistics;
import nju.software.dataobject.Order;
import nju.software.model.OrderInfo;
import nju.software.model.OrderModel;
import nju.software.model.ProductModel;
import nju.software.service.BuyService;
import nju.software.service.OrderService;
import nju.software.util.JbpmAPIUtil;

import org.jbpm.task.query.TaskSummary;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BuyController {
	
	@Autowired
	private JbpmAPIUtil jbpmAPIUtil;
	@Autowired
	private OrderService orderService;
	@Autowired
	private BuyService buyService;
	

	
	//========================样衣采购===========================
	@RequestMapping(value = "/buy/purchaseSampleMaterialList.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseSampleMaterialList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		List<OrderInfo> list=buyService.getPurchaseSampleMaterialList();
		model.put("list", list);
		return "/buy/purchaseSampleMaterialList";
	}

	
	@RequestMapping(value = "/buy/purchaseSampleMaterialDetail.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseSampleMaterialDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String orderId=request.getParameter("orderId");
		OrderInfo orderInfo=buyService.getPurchaseSampleMaterialDetail(Integer.parseInt(orderId));
		model.addAttribute("orderInfo", orderInfo);
		return "/buy/purchaseSampleMaterialDetail";
	}
	
	
	@RequestMapping(value = "/buy/purchaseSampleMaterialSubmit.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseSampleMaterialSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String taskId=request.getParameter("task_id");
		String result=request.getParameter("purchaseerror");
		buyService.purchaseSampleMaterialSubmit(Long.parseLong(taskId), result);
		return "forward:/buy/purchaseSampleMaterialList.do";
	}
	

	//========================生产验证===========================
	@RequestMapping(value = "/buy/confirmPurchaseList.do")
	@Transactional(rollbackFor = Exception.class)
	public String confirmPurchaseList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		List<OrderInfo> list=buyService.getConfirmPurchaseList();
		model.put("list", list);
		return "/buy/confirmPurchaseList";
	}

	@RequestMapping(value = "/buy/confirmPurchaseDetail.do")
	@Transactional(rollbackFor = Exception.class)
	public String confirmPurchaseDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String orderId=request.getParameter("orderId");
		OrderInfo orderInfo=buyService.getConfirmPurchaseDetail(Integer.parseInt(orderId));
		model.addAttribute("orderInfo", orderInfo);
		return "/buy/confirmPurchaseDetail";
	}
	
	
	@RequestMapping(value = "/buy/confirmPurchaseSubmit.do")
	@Transactional(rollbackFor = Exception.class)
	public String confirmPurchaseSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String taskId=request.getParameter("task_id");
		String result=request.getParameter("purchaseerror");
		buyService.purchaseSampleMaterialSubmit(Long.parseLong(taskId), result);
		return "forward:/buy/confirmPurchaseList.do";
	}
	
	
	//========================生产采购===========================
	@RequestMapping(value = "/buy/purchaseMaterialList.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseMaterialList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		List<OrderInfo> list=buyService.getPurchaseMaterialList();
		model.put("list", list);
		return "/buy/purchaseMaterialList";
	}

	
	@RequestMapping(value = "/buy/purchaseMaterialDetail.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseMaterialDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String orderId=request.getParameter("orderId");
		OrderInfo orderInfo=buyService.getPurchaseMaterialDetail(Integer.parseInt(orderId));
		model.addAttribute("orderInfo", orderInfo);
		return "/buy/purchaseMaterialDetail";
	}
		

	@RequestMapping(value = "/buy/purchaseMaterialDetailSubmit.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseMaterialDetailSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String taskId=request.getParameter("task_id");
		String result=request.getParameter("purchaseerror");
		buyService.purchaseSampleMaterialSubmit(Long.parseLong(taskId), result);
		return "forward:/buy/purchaseMaterialList.do";
	}


	
	


	/**
	 * 采购验证跳转链接
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "buy/verify.do", method= RequestMethod.GET)
	@Transactional(rollbackFor = Exception.class)
	public String verify(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		
		System.out.println("buy verify ================ show task");
		List<OrderModel> orderList = new ArrayList<OrderModel>();
		Account account = (Account) request.getSession().getAttribute("cur_user");
//		String actorId = account.getUserRole();
		String actorId = "CAIGOUZHUGUAN";
		System.out.println("actorId: " + actorId);
		String taskName = "verification_purchased";
		orderList = orderService.getOrderByActorIdAndTaskname(actorId, taskName);
		if (orderList.isEmpty()) {
			System.out.println("no orderList ");
		}
		model.addAttribute("order_list", orderList);
		
		return "buy/verify";
	}
	
	/**
	 * 采购验证
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "buy/doVerify.do", method= RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String doVerify(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		System.out.println("buy verify ================");
		
		Account account = (Account) request.getSession().getAttribute("cur_user");
		boolean buyVal = Boolean.parseBoolean(request.getParameter("buyVal"));
		String s_orderId_request = (String) request.getParameter("orderId");
		int orderId_request = Integer.parseInt(s_orderId_request);
		String s_taskId = request.getParameter("taskId");
		long taskId = Long.parseLong(s_taskId);
		String s_processId = request.getParameter("pinId");
		long processId = Long.parseLong(s_processId);
		String comment = request.getParameter("suggestion");
		String taskName = "verification_purchased";
		buyService.verify(account, orderId_request, taskId, processId, buyVal, comment);
		
		return "redirect:/buy/verify.do";
	}
	
	/**
	 * 显示订单详细信息
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "buy/verifyDetail.do", method= RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String verifyDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		
		System.out.println("buy verify ================ show detail");
		OrderModel orderModel = null;
		Account account = (Account) request.getSession().getAttribute("cur_user");
//		String actorId = account.getUserRole();
		String s_orderId_request = (String) request.getParameter("id");
		int orderId_request = Integer.parseInt(s_orderId_request);
		String s_taskId = request.getParameter("task_id");
		long taskId = Long.parseLong(s_taskId);
		String s_processId = request.getParameter("process_id");
		long processId = Long.parseLong(s_processId);
		orderModel = orderService.getOrderDetail(orderId_request, taskId, processId);
		Logistics logistics = buyService.getLogisticsByOrderId(orderId_request);
		List<Fabric> fabricList = buyService.getFabricByOrderId(orderId_request);
		List<Accessory> accessoryList = buyService.getAccessoryByOrderId(orderId_request);
		model.addAttribute("orderModel", orderModel);
		model.addAttribute("logistics", logistics);
		model.addAttribute("fabric_list", fabricList);
		model.addAttribute("accessory_list", accessoryList);
		
		return "buy/verify_detail";
	}

	
	
	
	
	
	
	

	/**
	 * 成本核算跳转链接
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "buy/costAccounting.do", method= RequestMethod.GET)
	@Transactional(rollbackFor = Exception.class)
	public String costAccounting(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		
		System.out.println("cost Accounting ================ show task");
		List<OrderModel> orderList = new ArrayList<OrderModel>();
		Account account = (Account) request.getSession().getAttribute("cur_user");
//		String actorId = account.getUserRole();
		String actorId = "CAIGOUZHUGUAN";
		System.out.println("actorId: " + actorId);
		String taskName = "Purchasing_accounting";
		orderList = orderService.getOrderByActorIdAndTaskname(actorId, taskName);
		if (orderList.isEmpty()) {
			System.out.println("no orderList ");
		}
		model.addAttribute("order_list", orderList);
		
		
		return "buy/cost_accounting";
	}
	
	
	
	
	
	
	
	
	/**
	 * 显示成本核算详细信息
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "buy/costAccountingDetail.do", method= RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String costAccountingDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		
		System.out.println("buy costAccounting Detail ================ costAccountingDetail");
		OrderModel orderModel = null;
		Account account = (Account) request.getSession().getAttribute("cur_user");
//		String actorId = account.getUserRole();
		String s_orderId_request = (String) request.getParameter("id");
		int orderId_request = Integer.parseInt(s_orderId_request);
		String s_taskId = request.getParameter("task_id");
		long taskId = Long.parseLong(s_taskId);
		String s_processId = request.getParameter("process_id");
		long processId = Long.parseLong(s_processId);
		orderModel = orderService.getOrderDetail(orderId_request, taskId, processId);
		Logistics logistics = buyService.getLogisticsByOrderId(orderId_request);
		List<Fabric> fabricList = buyService.getFabricByOrderId(orderId_request);
		List<Accessory> accessoryList = buyService.getAccessoryByOrderId(orderId_request);
		model.addAttribute("orderModel", orderModel);
		model.addAttribute("logistics", logistics);
		model.addAttribute("fabric_list", fabricList);
		model.addAttribute("accessory_list", accessoryList);
		
		return "buy/costAccounting_detail";
	}
	
	
	
	
	/**
	 * 成本核算
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	
	
	
	//还未完成 unfinished
	@RequestMapping(value = "buy/doCostAccounting.do", method= RequestMethod.POST)
	@Transactional(rollbackFor = Exception.class)
	public String doCostAccounting(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		System.out.println("buy  cost Accounting================");
		
		Account account = (Account) request.getSession().getAttribute("cur_user");
		
//		boolean buyVal = Boolean.parseBoolean(request.getParameter("buyVal"));
		
		String s_orderId_request = (String) request.getParameter("orderId");
		int orderId_request = Integer.parseInt(s_orderId_request);
		String s_taskId = request.getParameter("taskId");
		long taskId = Long.parseLong(s_taskId);
		String s_processId = request.getParameter("pinId");
		long processId = Long.parseLong(s_processId);
		
		
		
		
		
		
		
//		
		String[] fabric_names=request.getParameterValues("fabricName");
		String[] tear_per_meters=request.getParameterValues("tear_per_meter");
		String[] cost_per_meters=request.getParameterValues("cost_per_meter");
		String[] fabric_prices=request.getParameterValues("fabric_price");
		
		
		
		String[] accessory_names=request.getParameterValues("accessoryName");
		
		String[] tear_per_piece=request.getParameterValues("tear_per_piece");
		String[] cost_per_piece=request.getParameterValues("cost_per_piece");
		String[] accessory_prices=request.getParameterValues("accessory_price");
		
		
//        buyService.updateAccessoryCost(orderId_request, taskId, processId, accessory_names, tear_per_piece, cost_per_piece, accessory_prices);
		
		buyService.costAccounting(account, orderId_request, taskId, processId, fabric_names, tear_per_meters,
				cost_per_meters,fabric_prices);
//		
//		String tear_per_meters=null;
//		StringBuilder fabric_names = new StringBuilder();
//		StringBuilder tear_per_meters = null;
//		StringBuilder cost_per_meters = null;
//		StringBuilder fabric_prices = null;
//		
		
//		for (int i=0;i<fabric_names_temp.length;i++)      
//		  {      
		
			
			
//			fabric_names.append(fabric_names_temp[i]);
//			fabric_names.append(",");
//			tear_per_meters.append(tear_per_meters_temp[i]);
//			tear_per_meters.append(",");
//			cost_per_meters.append(cost_per_meters_temp[i]);
//			cost_per_meters.append(",");
//			fabric_prices.append(fabric_prices_temp[i]);
//			fabric_prices.append(",");
//			
//			
//		  }      
//		
//		
//		System.out.println(fabric_names);
//		System.out.println(tear_per_meters);
	
		
		
		
		return "redirect:/buy/costAccounting.do";
	}
	
	
	
	
	
	
}
