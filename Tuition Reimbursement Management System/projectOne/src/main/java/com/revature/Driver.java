package com.revature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.controllers.EmployeeController;
import com.revature.controllers.ReimbursementController;
import com.revature.utils.CassandraUtil;

import io.javalin.Javalin;

public class Driver {
	private static final Logger log = LogManager.getLogger(Driver.class);

	
	public static void main(String[] args) {
		log.trace("Begin TRMS");
		employeeTable();
		reimbursementTable();
		messageTable();
		uploadTable();
		javalin();
	}

	public static void employeeTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS Employee (")
				.append("username text, password text, role int, formid list<int>, availablereimbursement double, primary key(username));");

		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	public static void reimbursementTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS Reimbursement (")
				.append("id int, employee text, directsupervisor text, departmenthead text, benco text, ")
				.append("date text, time text, location text, description text, cost double, gradingpercentage double, presentationrequired boolean, ")
				.append("event text, justification text, urgent boolean, approval int, pendingreimbursement double, exceedsfunds boolean, attachments list<text>, ")
				.append("primary key(id));");

		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	public static void messageTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS Message (")
				.append("messageid int, reimbursementid int, recipient text, sender text, reason text, primary key(recipient));");

		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	public static void uploadTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS Upload (")
				.append("employeeid text, s3key text, fileurl text, primary key(employeeid));");

		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	public static void javalin() {
		Javalin app = Javalin.create().start(8080);
		
		app.put("/employee/register", EmployeeController::register);

		//Employee
		app.post("/employee/login", EmployeeController::login);
		app.delete("/", EmployeeController::logout);
		
		//Reimbursements
		app.post("/reimbursements", ReimbursementController::createReimbursement);
		app.get("/reimbursements/:id", ReimbursementController::getReimbursement);
		app.put("/reimbursements/:id", ReimbursementController::approveReimbursement);
		
		//Messages
		app.get("/messages", ReimbursementController::getMessages);
		
		//Uploads
		app.put("/upload", ReimbursementController::uploadFile);
	}
	
}
