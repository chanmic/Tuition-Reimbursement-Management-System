package com.revature.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.beans.Employee;
import com.revature.factory.BeanFactory;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;

import io.javalin.http.Context;

public class EmployeeController {
	private static final Logger log = LogManager.getLogger(EmployeeController.class);

	private static EmployeeService es = (EmployeeService) BeanFactory.getFactory().get(EmployeeService.class, EmployeeServiceImpl.class);
	
	public static void register(Context ctx) {
		log.trace("Attempting to register");
		if((ctx.sessionAttribute("Employee") != null) 
				|| (ctx.sessionAttribute("DirectSupervisor") != null) 
				|| (ctx.sessionAttribute("DepartmentHead") != null) 
				|| (ctx.sessionAttribute("DirectHead") != null)
				|| (ctx.sessionAttribute("BenefitsCoordinator") != null)) {
			ctx.status(204);
			log.trace("Employee currently logged in");
			return;
		}
		Employee employee = ctx.bodyAsClass(Employee.class);
		employee.setAvailableReimbursement(1000); // Set available reimbursement to 1000 in the beginning
		if (es.addEmployee(employee)) {
			ctx.json(employee);
		} else {
			ctx.status(409);
		}
	}

	public static void login(Context ctx) {
		log.trace("Attempting to login");
		if((ctx.sessionAttribute("Employee") != null) 
				|| (ctx.sessionAttribute("DirectSupervisor") != null) 
				|| (ctx.sessionAttribute("DepartmentHead") != null) 
				|| (ctx.sessionAttribute("DirectHead") != null)
				|| (ctx.sessionAttribute("BenefitsCoordinator") != null)) {
			ctx.status(204);
			log.trace("Employee already logged in");
			return;
		}
		String username = ctx.formParam("username");
		String password = ctx.formParam("password");
		Employee employee = es.getEmployee(username);
		if(employee == null) {
			ctx.status(401);
			log.warn("Unable to find employee " + username);
			return;
		}
		else {
			if(employee.getPassword().equals(password)) {
				switch(employee.getRole()) {
					case 1:
						ctx.sessionAttribute("Employee", employee);
						break;
					case 2:
						ctx.sessionAttribute("DirectSupervisor", employee);
						break;
					case 3:
						ctx.sessionAttribute("DepartmentHead", employee);
						break;
					case 4:
						ctx.sessionAttribute("DirectHead", employee);
						break;
					case 5:
						ctx.sessionAttribute("BenefitsCoordinator", employee);
						break;
					default:
						log.warn("For some reason, this user doesn't have a defined role");
						return;
				}
				ctx.json(employee);
				log.trace("Successful login");
			}
			else {
				log.trace("Invalid password");
				ctx.status(204);
			}
		}
	}
	
	
	public static void logout(Context ctx) {
		ctx.req.getSession().invalidate();
		log.trace("Successful logout");
	}
}