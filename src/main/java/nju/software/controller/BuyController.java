package nju.software.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nju.software.dataobject.Account;
import nju.software.dataobject.Employee;
import nju.software.dataobject.SearchInfo;
import nju.software.service.BuyService;
import nju.software.service.EmployeeService;
import nju.software.util.DateUtil;
import nju.software.util.ListUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BuyController {

	// ===========================采购验证=================================
	@RequestMapping(value = "/buy/verifyPurchaseList.do")
	@Transactional(rollbackFor = Exception.class)
	public String verifyPurchaseList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		List<Map<String, Object>> list = buyService.getVerifyPurchaseList();
		list = ListUtil.reserveList(list);
		model.put("list", list);
		model.addAttribute("taskName", "采购验证");
		model.addAttribute("url", "/buy/verifyPurchaseDetail.do");
		model.addAttribute("searchurl", "/buy/verifyPurchaseListSearch.do");

		return "/buy/verifyPurchaseList";
	}
	@Autowired
	private EmployeeService employeeService;
	// ===========================采购验证搜索订单=================================
	@RequestMapping(value = "/buy/verifyPurchaseListSearch.do")
	@Transactional(rollbackFor = Exception.class)
	public String verifyPurchaseListSearch(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String ordernumber = request.getParameter("ordernumber");
		String customername = request.getParameter("customername");
		String stylename = request.getParameter("stylename");
		String employeename = request.getParameter("employeename");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		//将用户输入的employeeName转化为employeeId,因为order表中没有employeeName属性
		List<Employee> employees = employeeService.getEmployeeByName(employeename);
		Integer[] employeeIds = new Integer[employees.size()];
		for(int i=0;i<employeeIds.length;i++){
			employeeIds[i] = employees.get(i).getEmployeeId();
		}
		List<Map<String, Object>> list = buyService.getSearchVerifyPurchaseList(ordernumber,customername,stylename,startdate,enddate,employeeIds);
		list = ListUtil.reserveList(list);
		model.put("list", list);
		model.addAttribute("taskName", "采购验证");
		model.addAttribute("url", "/buy/verifyPurchaseDetail.do");
		model.addAttribute("searchurl", "/buy/verifyPurchaseListSearch.do");

		model.addAttribute("info", new SearchInfo(ordernumber, customername, stylename, employeename, startdate, enddate));//将查询条件传回页面  hcj
		return "/buy/verifyPurchaseList";
	}
	
	@RequestMapping(value = "/buy/verifyPurchaseDetail.do")
	@Transactional(rollbackFor = Exception.class)
	public String verifyPurchaseDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String orderId = request.getParameter("orderId");
		Map<String, Object> orderInfo = buyService
				.getVerifyPurchaseDetail(Integer.parseInt(orderId));
		model.addAttribute("orderInfo", orderInfo);
		return "/buy/verifyPurchaseDetail";
	}

	@RequestMapping(value = "/buy/verifyPurchaseSubmit.do")
	@Transactional(rollbackFor = Exception.class)
	public String verifyPurchaseSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String taskId = request.getParameter("taskId");
		String comment = request.getParameter("suggestion");
		boolean result = Boolean.parseBoolean(request.getParameter("result"));
		buyService.verifyPurchaseSubmit(taskId, result, comment);
		return "forward:/buy/verifyPurchaseList.do";
	}

	// ===========================采购成本核算=================================
	@RequestMapping(value = "/buy/computePurchaseCostList.do")
	@Transactional(rollbackFor = Exception.class)
	public String computePurchaseCostList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		List<Map<String, Object>> list = buyService
				.getComputePurchaseCostList();
		list = ListUtil.reserveList(list);
		/*if (list.size() == 0) {
			jbpmTest.completeVerify("1", true);
			list = buyService.getComputePurchaseCostList();
		}*/
		model.addAttribute("list", list);
		model.addAttribute("taskName", "采购成本验证并核算");
		model.addAttribute("url", "/buy/computePurchaseCostDetail.do");
		model.addAttribute("searchurl", "/buy/computePurchaseCostListSearch.do");

		return "/buy/computePurchaseCostList";
	}

	// ===========================采购成本核算=================================
	@RequestMapping(value = "/buy/computePurchaseCostListSearch.do")
	@Transactional(rollbackFor = Exception.class)
	public String computePurchaseCostListSearch(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String ordernumber = request.getParameter("ordernumber");
		String customername = request.getParameter("customername");
		String stylename = request.getParameter("stylename");
		String employeename = request.getParameter("employeename");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		//将用户输入的employeeName转化为employeeId,因为order表中没有employeeName属性
		List<Employee> employees = employeeService.getEmployeeByName(employeename);
		Integer[] employeeIds = new Integer[employees.size()];
		for(int i=0;i<employeeIds.length;i++){
			employeeIds[i] = employees.get(i).getEmployeeId();
		}
		List<Map<String, Object>> list = buyService
				.getSearchComputePurchaseCostList(ordernumber,customername,stylename,startdate,enddate,employeeIds);
		list = ListUtil.reserveList(list);
		/*if (list.size() == 0) {
			jbpmTest.completeVerify("1", true);
			list = buyService.getComputePurchaseCostList();
		}*/
		model.addAttribute("list", list);
		model.addAttribute("taskName", "采购成本核算");
		model.addAttribute("url", "/buy/computePurchaseCostDetail.do");
		model.addAttribute("searchurl", "/buy/computePurchaseCostListSearch.do");

		model.addAttribute("info", new SearchInfo(ordernumber, customername, stylename, employeename, startdate, enddate));//将查询条件传回页面  hcj
		return "/buy/computePurchaseCostList";
	}
	
	@RequestMapping(value = "/buy/computePurchaseCostDetail.do")
	@Transactional(rollbackFor = Exception.class)
	public String computePurchaseCostDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String orderId = request.getParameter("orderId");
		Map<String, Object> orderInfo = buyService
				.getComputePurchaseCostDetail(Integer.parseInt(orderId));
		model.addAttribute("orderInfo", orderInfo);
		return "/buy/computePurchaseCostDetail";
	}

	@RequestMapping(value = "/buy/computePurchaseCostSubmit.do")
	@Transactional(rollbackFor = Exception.class)
	public String computePurchaseCostSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		Integer orderId = Integer.parseInt(request.getParameter("orderId"));
		String taskId = request.getParameter("taskId");

		boolean result = Boolean.parseBoolean(request.getParameter("result"));// 是否拒绝采购
		String comment = request.getParameter("suggestion"); // 采购拒绝意见
		//如果拒绝，则不进行采购成本的核算，直接返回
		if (result == false) {
			buyService.verifyPurchaseSubmit(taskId, result, comment);
			return "forward:/buy/computePurchaseCostList.do";
		}

		// 面料数据
		String fabric_name = request.getParameter("fabric_name");
		String fabric_amount = request.getParameter("fabric_amount");
		String tear_per_meter = request.getParameter("tear_per_meter");
		String cost_per_meter = request.getParameter("cost_per_meter");
		String fabric_names_temp[] = fabric_name.split(",");
		String fabric_amounts_temp[] = fabric_amount.split(",");
		String tear_per_meters_temp[] = tear_per_meter.split(",");
		String cost_per_meters_temp[] = cost_per_meter.split(",");

		List<String> fabric_names_list = new ArrayList<String>();
		List<String> fabric_amounts_list = new ArrayList<String>();
		List<String> cost_per_meters_list = new ArrayList<String>();
		List<String> tear_per_meters_list = new ArrayList<String>();

		for (int i = 0; i < fabric_names_temp.length; i++) {

			if (fabric_names_temp[i].equals(""))
				continue;
			fabric_names_list.add(fabric_names_temp[i]);
			fabric_amounts_list.add(fabric_amounts_temp[i]);
			tear_per_meters_list.add(tear_per_meters_temp[i]);
			cost_per_meters_list.add(cost_per_meters_temp[i]);
		}

		int size = fabric_names_list.size();
		String[] fabric_names = (String[]) fabric_names_list
				.toArray(new String[size]);
		String[] fabric_amounts = (String[]) fabric_amounts_list
				.toArray(new String[size]);
		String[] tear_per_meters = (String[]) tear_per_meters_list
				.toArray(new String[size]);
		String[] cost_per_meters = (String[]) cost_per_meters_list
				.toArray(new String[size]);

		// 辅料数据
		String accessory_name = request.getParameter("accessory_name");
		String accessory_query = request.getParameter("accessory_query");
		String tear_per_pieces = request.getParameter("tear_per_piece");
		String cost_per_pieces = request.getParameter("cost_per_piece");

		String accessory_names_temp[] = accessory_name.split(",");
		String accessory_querys_temp[] = accessory_query.split(",");
		String tear_per_piece_temp[] = tear_per_pieces.split(",");
		String cost_per_piece_temp[] = cost_per_pieces.split(",");

		List<String> accessory_names_list = new ArrayList<String>();
		List<String> accessory_querys_list = new ArrayList<String>();
		List<String> tear_per_piece_list = new ArrayList<String>();
		List<String> cost_per_piece_list = new ArrayList<String>();
		for (int i = 0; i < accessory_names_temp.length; i++) {

			if (accessory_names_temp[i].equals(""))
				continue;
			accessory_names_list.add(accessory_names_temp[i]);
			accessory_querys_list.add(accessory_querys_temp[i]);
			tear_per_piece_list.add(tear_per_piece_temp[i]);
			cost_per_piece_list.add(cost_per_piece_temp[i]);
		}

		int accessory_size = accessory_names_list.size();
		String[] accessory_names = (String[]) accessory_names_list
				.toArray(new String[accessory_size]);
		String[] accessory_querys = (String[]) accessory_querys_list
				.toArray(new String[accessory_size]);
		String[] tear_per_piece = (String[]) tear_per_piece_list
				.toArray(new String[accessory_size]);
		String[] cost_per_piece = (String[]) cost_per_piece_list
				.toArray(new String[accessory_size]);

		// String[] fabric_names = request.getParameterValues("fabricName");
		// String[] tear_per_meters =
		// request.getParameterValues("tear_per_meter");
		// String[] cost_per_meters =
		// request.getParameterValues("cost_per_meter");

		// String[] fabric_prices = request.getParameterValues("fabric_price");

		// String[] accessory_names =
		// request.getParameterValues("accessoryName");
		// String[] tear_per_piece =
		// request.getParameterValues("tear_per_piece");
		// String[] cost_per_piece =
		// request.getParameterValues("cost_per_piece");
		// String[] accessory_prices = request
		// .getParameterValues("accessory_price");

		
		//采购报价提交，默认采购验证通过
		buyService.computePurchaseCostSubmit(orderId, taskId, result, comment,
				fabric_names, fabric_amounts, tear_per_meters, cost_per_meters,
				accessory_names, accessory_querys, tear_per_piece,
				cost_per_piece);

		return "forward:/buy/computePurchaseCostList.do";

	}

	// ========================样衣原料采购===========================
	@RequestMapping(value = "/buy/purchaseSampleMaterialList.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseSampleMaterialList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		List<Map<String, Object>> list = buyService
				.getPurchaseSampleMaterialList();
		list = ListUtil.reserveList(list);
		model.put("list", list);
		model.addAttribute("taskName", "样衣原料采购");
		model.addAttribute("url", "/buy/purchaseSampleMaterialDetail.do");
		model.addAttribute("searchurl", "/buy/purchaseSampleMaterialListSearch.do");

		return "/buy/purchaseSampleMaterialList";
	}

	@RequestMapping(value = "/buy/purchaseSampleMaterialListSearch.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseSampleMaterialListSearch(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String ordernumber = request.getParameter("ordernumber");
		String customername = request.getParameter("customername");
		String stylename = request.getParameter("stylename");
		String employeename = request.getParameter("employeename");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		//将用户输入的employeeName转化为employeeId,因为order表中没有employeeName属性
		List<Employee> employees = employeeService.getEmployeeByName(employeename);
		Integer[] employeeIds = new Integer[employees.size()];
		for(int i=0;i<employeeIds.length;i++){
			employeeIds[i] = employees.get(i).getEmployeeId();
		}
		List<Map<String, Object>> list = buyService
				.getSearchPurchaseSampleMaterialList(ordernumber,customername,stylename,startdate,enddate,employeeIds);
		list = ListUtil.reserveList(list);
		model.put("list", list);
		model.addAttribute("taskName", "样衣原料采购");
		model.addAttribute("url", "/buy/purchaseSampleMaterialDetail.do");
		model.addAttribute("searchurl", "/buy/purchaseSampleMaterialListSearch.do");

		model.addAttribute("info", new SearchInfo(ordernumber, customername, stylename, employeename, startdate, enddate));//将查询条件传回页面  hcj
		return "/buy/purchaseSampleMaterialList";
	}
	
	@RequestMapping(value = "/buy/purchaseSampleMaterialDetail.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseSampleMaterialDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Integer orderId = Integer.parseInt(request.getParameter("orderId"));
		Map<String, Object> orderInfo = buyService
				.getPurchaseSampleMaterialDetail(orderId);
		model.addAttribute("orderInfoArraySize", orderInfo.size());
		model.addAttribute("orderInfo", orderInfo);
		return "/buy/purchaseSampleMaterialDetail";
	}

	@RequestMapping(value = "/buy/purchaseSampleMaterialSubmit.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseSampleMaterialSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String orderId = request.getParameter("orderId");
		String taskId = request.getParameter("taskId");
		String processId = request.getParameter("processId");
		boolean result = request.getParameter("result").equals("1");
		String samplepurName = request.getParameter("samplepurName");
		Timestamp samplepurDate = getTime(request.getParameter("samplepurDate")) ;
		String samplesupplierName = request.getParameter("samplesupplierName");
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("cur_user");		 
		boolean needCraft = buyService.isNeedCraft(processId, "needCraft");
		
		System.out.println("need craft 是这个值："+needCraft);
//		String needCraft = 
//				(String)jbpmAPIUtil.getVariable(
//				jbpmAPIUtil.getTask(account.getAccountId()+"", long_taskId),
//				DesignServiceImpl.RESULT_NEED_CRAFT);
//		boolean needcraft = false;
//		if(needCraft!=null){			
//			 needcraft = (needCraft.equals("true"))?true:false;
//		}
		
//		buyService.purchaseSampleMaterialSubmit(Long.parseLong(taskId), result);
		buyService.purchaseSampleMaterialSubmit(taskId, result, needCraft,orderId,samplepurName,samplepurDate,samplesupplierName);

		return "forward:/buy/purchaseSampleMaterialList.do";
	}

	// ========================生产验证===========================
	@RequestMapping(value = "/buy/confirmPurchaseList.do")
	@Transactional(rollbackFor = Exception.class)
	public String confirmPurchaseList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		List<Map<String, Object>> list = buyService.getConfirmPurchaseList();
		model.put("list", list);
		list = ListUtil.reserveList(list);
		model.addAttribute("taskName", "确认生产原料");
		model.addAttribute("url", "/buy/confirmPurchaseDetail.do");
		model.addAttribute("searchurl", "/buy/confirmPurchaseListSearch.do");

		return "/buy/confirmPurchaseList";
	}

	@RequestMapping(value = "/buy/confirmPurchaseListSearch.do")
	@Transactional(rollbackFor = Exception.class)
	public String confirmPurchaseListSearch(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String ordernumber = request.getParameter("ordernumber");
		String customername = request.getParameter("customername");
		String stylename = request.getParameter("stylename");
		String employeename = request.getParameter("employeename");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		//将用户输入的employeeName转化为employeeId,因为order表中没有employeeName属性
		List<Employee> employees = employeeService.getEmployeeByName(employeename);
		Integer[] employeeIds = new Integer[employees.size()];
		for(int i=0;i<employeeIds.length;i++){
			employeeIds[i] = employees.get(i).getEmployeeId();
		}
		List<Map<String, Object>> list = buyService.getSearchConfirmPurchaseList(ordernumber,customername,stylename,startdate,enddate,employeeIds);
		list = ListUtil.reserveList(list);
		model.put("list", list);
		model.addAttribute("taskName", "确认生产原料");
		model.addAttribute("url", "/buy/confirmPurchaseDetail.do");
		model.addAttribute("searchurl", "/buy/confirmPurchaseListSearch.do");
		model.addAttribute("info", new SearchInfo(ordernumber, customername, stylename, employeename, startdate, enddate));//将查询条件传回页面  hcj
		return "/buy/confirmPurchaseList";
	}

	
	@RequestMapping(value = "/buy/confirmPurchaseDetail.do")
	@Transactional(rollbackFor = Exception.class)
	public String confirmPurchaseDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String orderId = request.getParameter("orderId");
		Map<String, Object> orderInfo = buyService
				.getConfirmPurchaseDetail(Integer.parseInt(orderId));
		model.addAttribute("orderInfo", orderInfo);
		return "/buy/confirmPurchaseDetail";
	}

	@RequestMapping(value = "/buy/confirmPurchaseSubmit.do")
	@Transactional(rollbackFor = Exception.class)
	public String confirmPurchaseSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String taskId = request.getParameter("taskId");
		Boolean result = request.getParameter("result").equals("1");
		buyService.confirmPurchaseSubmit(taskId, result);
		return "forward:/buy/confirmPurchaseList.do";
	}
	
	public String purchaseSampleMaterialSubmit1(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String orderId = request.getParameter("orderId");
		String taskId = request.getParameter("taskId");
		String processId = request.getParameter("processId");
		boolean result = request.getParameter("result").equals("1");
		String samplepurName = request.getParameter("samplepurName");
		Timestamp samplepurDate = getTime(request.getParameter("samplepurDate")) ;
		String samplesupplierName = request.getParameter("samplesupplierName");
		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("cur_user");		 
		boolean needCraft =  buyService.isNeedCraft(processId, "needCraft");
		
		System.out.println("need craft 是这个值："+needCraft);
//		String needCraft = 
//				(String)jbpmAPIUtil.getVariable(
//				jbpmAPIUtil.getTask(account.getAccountId()+"", long_taskId),
//				DesignServiceImpl.RESULT_NEED_CRAFT);
//		boolean needcraft = false;
//		if(needCraft!=null){			
//			 needcraft = (needCraft.equals("true"))?true:false;
//		}
//		buyService.purchaseSampleMaterialSubmit(Long.parseLong(taskId), result);
		buyService.purchaseSampleMaterialSubmit(taskId, result, needCraft,orderId,samplepurName,samplepurDate,samplesupplierName);

		return "forward:/buy/purchaseSampleMaterialList.do";
	}
	
	
	// ========================生产采购===========================
	@RequestMapping(value = "/buy/purchaseMaterialList.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseMaterialList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		List<Map<String, Object>> list = buyService.getPurchaseMaterialList();
		list = ListUtil.reserveList(list);
		model.put("list", list);
		model.addAttribute("taskName", "大货面料采购");
		model.addAttribute("url", "/buy/purchaseMaterialDetail.do");
		model.addAttribute("searchurl", "/buy/purchaseMaterialListSearch.do");

		return "/buy/purchaseMaterialList";
	}

	@RequestMapping(value = "/buy/purchaseMaterialListSearch.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseMaterialListSearch(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String ordernumber = request.getParameter("ordernumber");
		String customername = request.getParameter("customername");
		String stylename = request.getParameter("stylename");
		String employeename = request.getParameter("employeename");
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		//将用户输入的employeeName转化为employeeId,因为order表中没有employeeName属性
		List<Employee> employees = employeeService.getEmployeeByName(employeename);
		Integer[] employeeIds = new Integer[employees.size()];
		for(int i=0;i<employeeIds.length;i++){
			employeeIds[i] = employees.get(i).getEmployeeId();
		}
		List<Map<String, Object>> list = buyService.getSearchPurchaseMaterialList(ordernumber,customername,stylename,startdate,enddate,employeeIds);
		list = ListUtil.reserveList(list);
		model.put("list", list);
		model.addAttribute("taskName", "生产采购");
		model.addAttribute("url", "/buy/purchaseMaterialDetail.do");
		model.addAttribute("searchurl", "/buy/purchaseMaterialListSearch.do");

		model.addAttribute("info", new SearchInfo(ordernumber, customername, stylename, employeename, startdate, enddate));//将查询条件传回页面  hcj
		return "/buy/purchaseMaterialList";
	}
	
	@RequestMapping(value = "/buy/purchaseMaterialDetail.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseMaterialDetail(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String orderId = request.getParameter("orderId");
		Map<String, Object> orderInfo = buyService
				.getPurchaseMaterialDetail(Integer.parseInt(orderId));
		model.addAttribute("orderInfo", orderInfo);
		return "/buy/purchaseMaterialDetail";
	}

	@RequestMapping(value = "/buy/purchaseMaterialSubmit.do")
	@Transactional(rollbackFor = Exception.class)
	public String purchaseMaterialSubmit(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String orderId = request.getParameter("orderId");
		String masspurName = request.getParameter("masspurName");
		Timestamp masspurDate = getTime(request.getParameter("masspurDate")) ;
		String masssupplierName = request.getParameter("masssupplierName");
		String taskId = request.getParameter("taskId");
		Boolean result = request.getParameter("result").equals("1");
		buyService.purchaseMaterialSubmit(taskId, result,orderId,masspurName,masspurDate,masssupplierName);
		return "forward:/buy/purchaseMaterialList.do";
	}
	
 		// ========================毛衣原料采购===========================
		@RequestMapping(value = "/buy/purchaseSweaterMaterialList.do")
		@Transactional(rollbackFor = Exception.class)
		public String purchaseSweaterMaterialList(HttpServletRequest request,
				HttpServletResponse response, ModelMap model) {
			List<Map<String, Object>> list = buyService.purchaseSweaterMaterialList();
			list = ListUtil.reserveList(list);
			model.put("list", list);
			model.addAttribute("taskName", "毛衣原料采购");
			model.addAttribute("url", "/buy/purchaseSweaterMaterialDetail.do");
			model.addAttribute("searchurl", "/buy/purchaseSweaterMaterialListSearch.do");

			return "/buy/purchaseSweaterMaterialList";
		}
		
		// ========================毛衣原料采购订单搜索===========================		
		@RequestMapping(value = "/buy/purchaseSweaterMaterialListSearch.do")
		@Transactional(rollbackFor = Exception.class)
		public String purchaseSweaterMaterialListSearch(HttpServletRequest request,
				HttpServletResponse response, ModelMap model) {
			String ordernumber = request.getParameter("ordernumber");
			String customername = request.getParameter("customername");
			String stylename = request.getParameter("stylename");
			String employeename = request.getParameter("employeename");
			String startdate = request.getParameter("startdate");
			String enddate = request.getParameter("enddate");
			//将用户输入的employeeName转化为employeeId,因为order表中没有employeeName属性
			List<Employee> employees = employeeService.getEmployeeByName(employeename);
			Integer[] employeeIds = new Integer[employees.size()];
			for(int i=0;i<employeeIds.length;i++){
				employeeIds[i] = employees.get(i).getEmployeeId();
			}
			List<Map<String, Object>> list = buyService.getSearchPurchaseSweaterMaterialList(ordernumber,customername,stylename,startdate,enddate,employeeIds);
			list = ListUtil.reserveList(list);
			model.put("list", list);
			model.addAttribute("taskName", "毛衣原料采购订单搜索");
			model.addAttribute("url", "/buy/purchaseSweaterMaterialDetail.do");
			model.addAttribute("searchurl", "/buy/purchaseSweaterMaterialListSearch.do");
			return "/buy/purchaseSweaterMaterialList";
		}
		
		@RequestMapping(value = "/buy/purchaseSweaterMaterialDetail.do")
		@Transactional(rollbackFor = Exception.class)
		public String purchaseSweaterMaterialDetail(HttpServletRequest request,
				HttpServletResponse response, ModelMap model) {
			String orderId = request.getParameter("orderId");
			Map<String, Object> orderInfo = buyService
					.getPurchaseSweaterMaterialDetail(Integer.parseInt(orderId));
			//从session中取默认责任人
			HttpSession session = request.getSession();
			Account account = (Account) session.getAttribute("cur_user");
			model.addAttribute("employee_name", account.getUserName());
			model.addAttribute("orderInfo", orderInfo);
			return "/buy/purchaseSweaterMaterialDetail";
		}

		@RequestMapping(value = "/buy/purchaseSweaterMaterialSubmit.do", method = RequestMethod.POST)
		@Transactional(rollbackFor = Exception.class)
		public String purchaseSweaterMaterialSubmit(HttpServletRequest request,
				HttpServletResponse response, ModelMap model) {
			String orderId = request.getParameter("orderId");
			String taskId =  request.getParameter("taskId");
			String choose = request.getParameter("task_name");
			String head = request.getParameter("Purchase_director");
			String supplier = request.getParameter("supplier");
			String Purchase_time = request.getParameter("Purchase_time");
			String type = request.getParameter("Wool_type");
			String weight = request.getParameter("Wool_weight");
			String total_price = request.getParameter("total_price");
			boolean buySweaterMaterialResult = false;
			if("有库存".equals(choose)){
				buySweaterMaterialResult = true;
			}
 			buyService.purchaseSweaterMaterialSubmit(taskId,orderId,total_price,weight,type,Purchase_time,supplier,head,buySweaterMaterialResult);
			return "forward:/buy/purchaseSweaterMaterialList.do";
		}
	
	public static Timestamp getTime(String time) {
		if(time.equals("")) return null;
		Date outDate = DateUtil.parse(time, DateUtil.haveSecondFormat);
		return new Timestamp(outDate.getTime());
	}
	
		/*	 
		* @Title: printProcurementOrder 
		* @Description: TODO:打印大货补货单
		* @param @param request
		* @param @param response
		* @param @param model
		* @param @return    设定文件 
		* @return String    返回类型 
		* @throws 
		
		@RequestMapping(value = "/buy/printProcurementOrder.do")
		@Transactional(rollbackFor = Exception.class)
		public String printProcurementOrder(HttpServletRequest request,
				HttpServletResponse response, ModelMap model) {
			Integer orderId=Integer.parseInt(request.getParameter("orderId"));
			Map<String,Object>orderInfo=buyService.getPrintProcurementOrderDetail(orderId);
			model.addAttribute("orderInfo", orderInfo);
			return "/finance/printProcurementOrder";
		}
		//获取样衣裁剪单信息
		@RequestMapping(value = "/buy/printProcurementSampleOrder.do")
		@Transactional(rollbackFor = Exception.class)
		public String printProcurementSampleOrder(HttpServletRequest request,
				HttpServletResponse response, ModelMap model) {
			Integer orderId=Integer.parseInt(request.getParameter("orderId"));
			Map<String,Object>orderInfo=buyService.getPrintProcurementOrderDetail(orderId);
			model.addAttribute("orderInfo", orderInfo);
			return "/finance/printProcurementSampleOrder";
		}
		
		*/
	@Autowired
	private BuyService buyService;
}