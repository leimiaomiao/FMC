package nju.software.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nju.software.dao.impl.EmployeeDAO;
import nju.software.dao.impl.OperateRecordDAO;
import nju.software.dao.impl.OrderDAO;
import nju.software.dao.impl.ProduceDAO;
import nju.software.dataobject.OperateRecord;
import nju.software.dataobject.Order;
import nju.software.dataobject.Produce;
import nju.software.service.SweaterMakeService;
import nju.software.util.JbpmAPIUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sweaterMakeServiceImpl")
public class SweaterMakeServiceImpl implements SweaterMakeService {

	@Override
	public List<Map<String, Object>> getSweaterSampleAndCraftList() {
		return service.getOrderList(ACTOR_SWEATER_MANAGER, TASK_CONFIRM_SWEATER_SAMPLE_AND_CRAFT);
	}

	@Override
	public Map<String, Object> getSweaterSampleAndCraftDetail(int orderId) {
		
		Map<String, Object> model = service.getBasicOrderModelWithQuote(ACTOR_SWEATER_MANAGER,
				TASK_CONFIRM_SWEATER_SAMPLE_AND_CRAFT, orderId);
		model.put("sweaterOperateRecord", operateRecordDAO.findByOrderId(orderId));// 毛衣打小样制造工艺等操作步骤记录
//        model.put("buySweaterMaterial", orderDAO.findById(orderId).isBuySweaterMaterialResult());
	    return model;
	}

	@Override
	public List<Map<String, Object>> getSendSweaterList() {
		return service.getOrderList(ACTOR_SWEATER_MANAGER, TASK_SEND_SWEATER);

	}

	@Override
	public Map<String, Object> getSendSweaterDetail(int orderId) {
		return service.getBasicOrderModelWithQuote(ACTOR_SWEATER_MANAGER,
				TASK_SEND_SWEATER, orderId);
	}

	@Override
	public boolean sweaterSampleAndCraftSubmit(long taskId, boolean result,
			String orderId,OperateRecord oprec) {
		if(result==false){
			operateRecordDAO.save(oprec);
			return false;
		}else{			
			Map<String, Object> data = new HashMap<String, Object>();
			try {
				
				jbpmAPIUtil.completeTask(taskId, data, ACTOR_SWEATER_MANAGER);
				return true;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	public boolean finishAllSweaterTasks(Integer orderId,boolean hasSweaterMaterial){
		List<OperateRecord> operateRecords = operateRecordDAO.findByOrderId(orderId);
		String[] operateTasksName = new String[operateRecords.size()];
		for(int i =0;i<operateRecords.size();i++){
			String tsknameI = operateRecords.get(operateRecords.size()-1-i).getTaskName();
			switch (tsknameI)
			{
			case "打小样": 
				tsknameI = "A";
				break;
			case "制作工艺":
				tsknameI = "B";
				break;
			case "制版打样":
				tsknameI = "C";
				break;
			case "确认样衣":
				tsknameI = "D";
				
			}
			operateTasksName[i] = tsknameI;
		}
		Pattern pattern = null;
		if(hasSweaterMaterial){//不做打小样任务			
			pattern = Pattern.compile("D+C+B+");
		}else{
			pattern = Pattern.compile("D+C+B+A+");
		}
		String tasknames = operateTasksName.toString();
        Matcher matcher = pattern.matcher(tasknames);
        if(matcher.find(0)){
        	System.out.println("符合任务执行顺序");
        	return true;
        }else{
        	System.out.println("不符合任务执行顺序");
        	return false;
        }
	}
	@Override
	public boolean sendSweaterSubmit(long taskId, boolean result,List<Produce> produceList, Integer orderId) {
 		Order order = orderDAO.findById(orderId);
 		if (result) {
			for (int i = 0; i < produceList.size(); i++) {
				ProduceDAO.save(produceList.get(i));
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
 		try {
 			data.put(RESULT_SWEATER, result);
			jbpmAPIUtil.completeTask(taskId, data, ACTOR_SWEATER_MANAGER);
			if(result==false){//如果result的的值为false，即为大货生产失败，流程会异常终止，将orderState设置为1
				order.setOrderState("1");
				orderDAO.attachDirty(order);
			}
			return true;
		} catch (InterruptedException e) {
 			e.printStackTrace();
			return false;
			}
	}

	@Override
	public List<Map<String, Object>> getSearchSweaterSampleAndCraftList(
			String ordernumber, String customername, String stylename,
			String startdate, String enddate, Integer[] employeeIds) {
		return service.getSearchOrderList(ACTOR_SWEATER_MANAGER, ordernumber,  customername,
				stylename,startdate,  enddate,  employeeIds,
				 TASK_CONFIRM_SWEATER_SAMPLE_AND_CRAFT);
	}

	@Override
	public List<Map<String, Object>> getSearchSendSweaterList(
			String ordernumber, String customername, String stylename,
			String startdate, String enddate, Integer[] employeeIds) {
		return service.getSearchOrderList(ACTOR_SWEATER_MANAGER, ordernumber,  customername, 
				stylename, startdate,  enddate,  employeeIds,
				 TASK_SEND_SWEATER);
	}
	public final static String ACTOR_SWEATER_MANAGER = "SweaterMakeManager";
	public final static String TASK_CONFIRM_SWEATER_SAMPLE_AND_CRAFT = "confirmSweaterSampleAndCraft";
	public final static String TASK_SEND_SWEATER= "sendSweater";
	public final static String RESULT_SWEATER = "sweater";
	public final static String RESULT_PRODUCE = "produce";
	public final static String RESULT_PRODUCE_COMMENT = "produceComment";
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private OperateRecordDAO operateRecordDAO;
	@Autowired
	private ServiceUtil service;
	@Autowired
	private ProduceDAO ProduceDAO;
	@Autowired
	private JbpmAPIUtil jbpmAPIUtil;
	@Autowired
	private EmployeeDAO employeeDAO;
	@Autowired
	private MarketServiceImpl MarketService;
	
	
	@Override
	public List<Map<String, Object>> getOrders() {
		// TODO Auto-generated method stub
		List<Order> orders = orderDAO.getSweaterOrders();
 		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Order order : orders) {
			ArrayList<String>  orderProcessStateNames = MarketService.getProcessStateName(order.getOrderId());
			if(orderProcessStateNames.size()>0){
				order.setOrderProcessStateName(orderProcessStateNames.get(0));
			}else{
				order.setOrderProcessStateName("");
			}
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("order", order);
			model.put("employee", employeeDAO.findById(order.getEmployeeId()));
			model.put("orderId", service.getOrderId(order));
 			list.add(model);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getSerachOrders(String orderState) {
		// TODO Auto-generated method stub
		List<Order> orders = orderDAO.getSweaterOrders();
 		List<Map<String, Object>> list = new ArrayList<>();
		for (Order order : orders) {
			int oid = order.getOrderId();
			ArrayList<String>  orderProcessStateNames = MarketService.getProcessStateName(oid);
			List<OperateRecord> Operatelist= operateRecordDAO.findByIdQueryMax(oid);
			if(orderProcessStateNames.size()>0){
				if(orderState.equals(orderProcessStateNames.get(0))){
					order.setOrderProcessStateName(orderProcessStateNames.get(0));
				}else if(orderProcessStateNames.get(0).indexOf(orderState)!= -1){//因为样衣确认和制作模块是分开来的，所以单独查询
					
					if(Operatelist.size() >0){
						OperateRecord Operate = Operatelist.get(0);
						if(orderState.equals(Operate.getTaskName())){//判断此单子的最大状态，如果和查询的状态一样，则显示
							order.setOrderProcessStateName(orderState);
						}else{
							continue;
						}
					}else if(Operatelist.size() == 0 && order.isBuySweaterMaterialResult() == false && !("制作工艺").equals(orderState)){
						order.setOrderProcessStateName(orderState);
					}else{
						continue;
					}	
				}else if(Operatelist.size() == 0 && order.isBuySweaterMaterialResult() == true){
					order.setOrderProcessStateName(orderState);
				}else{
					continue;
				}
			}
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("order", order);
			model.put("employee", employeeDAO.findById(order.getEmployeeId()));
			model.put("orderId", service.getOrderId(order));
 			list.add(model);
		}
		return list;
	}

}
