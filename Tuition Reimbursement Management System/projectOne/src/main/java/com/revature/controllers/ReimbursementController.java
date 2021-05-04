package com.revature.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.beans.Employee;
import com.revature.beans.Event;
import com.revature.beans.GradingFormat;
import com.revature.beans.Message;
import com.revature.beans.TuitionReimbursement;
import com.revature.beans.Upload;
import com.revature.factory.BeanFactory;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;
import com.revature.services.ReimbursementService;
import com.revature.services.ReimbursementServiceImpl;
import com.revature.utils.S3Util;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import software.amazon.awssdk.core.sync.RequestBody;

public class ReimbursementController {
	private static final Logger log = LogManager.getLogger(ReimbursementController.class);
	private static ReimbursementService rs = (ReimbursementService) BeanFactory.getFactory().get(ReimbursementService.class, ReimbursementServiceImpl.class);
	private static EmployeeService es = (EmployeeService) BeanFactory.getFactory().get(EmployeeService.class, EmployeeServiceImpl.class);

	public static void uploadFile(Context ctx){
		log.trace("Uploading files");
		
		Employee e;
		
		if(ctx.sessionAttribute("Employee") != null) {
			e = ctx.sessionAttribute("Employee");
		}
		else if(ctx.sessionAttribute("DirectSupervisor") != null) {
			e = ctx.sessionAttribute("DirectSupervisor");
		}
		else if(ctx.sessionAttribute("DepartmentHead") != null) {
			e = ctx.sessionAttribute("DepartmentHead");
		}
		else if(ctx.sessionAttribute("DirectHead") != null) {
			e = ctx.sessionAttribute("DirectHead");
		}
		else if(ctx.sessionAttribute("BenefitsCoordinator") != null) {
			e = ctx.sessionAttribute("BenefitsCoordinator");
		}
		else {
			ctx.status(401);
			log.warn("You aren't logged in to an appropriate account");
			return;
		}	
		List<Upload> uploads = new ArrayList<>();

		Random r = new Random();
		for(UploadedFile file : ctx.uploadedFiles()) {
			String key = e.getUsername() + "." + r.nextInt(Integer.MAX_VALUE) + file.getFilename();
			
			S3Util.getInstance().uploadToBucket(key, 
					RequestBody.fromInputStream(file.getContent(), file.getSize()));
			String url = S3Util.getInstance().getObjectUrl(key);
			
			Upload upload = new Upload();
			upload.setEmployeeID(e.getUsername());
			upload.setS3Key(key);
			upload.setFileURL(url);
			
			rs.addUpload(upload);
			
			uploads.add(upload);
		}

		
		ctx.json(uploads);
	}

	
	public static void getReimbursement(Context ctx) {
		log.trace("Getting reimbursement");
		int id = Integer.parseInt(ctx.pathParam("id"));
		TuitionReimbursement t = rs.getReimbursement(id);
		if(t == null) {
			log.warn("Cannot find reimbursement of id " + id);
			ctx.status(204);
			return;
		}
		Employee e;
		if(ctx.sessionAttribute("Employee") != null) {
			e = ctx.sessionAttribute("Employee");
			if(!e.getUsername().equals(t.getEmployee())) {
				ctx.status(401);
				log.warn("You aren't logged in to an appropriate account");
				return;
			}
		}
		else if(ctx.sessionAttribute("DirectSupervisor") != null) {
			e = ctx.sessionAttribute("DirectSupervisor");
			if(!e.getUsername().equals(t.getDirSu())) {
				ctx.status(401);
				log.warn("You aren't logged in to an appropriate account");
				return;
			}
		}
		else if(ctx.sessionAttribute("DepartmentHead") != null) {
			e = ctx.sessionAttribute("DepartmentHead");
		}
		else if(ctx.sessionAttribute("DirectHead") != null) {
			e = ctx.sessionAttribute("DirectHead");
			if(!e.getUsername().equals(t.getDirSu())) {
				ctx.status(401);
				log.warn("You aren't logged in to an appropriate account");
				return;
			}
		}
		else if(ctx.sessionAttribute("BenefitsCoordinator") != null) {
			e = ctx.sessionAttribute("BenefitsCoordinator");
		}
		else {
			ctx.status(401);
			log.warn("You aren't logged in to an appropriate account");
			return;
		}
			
		ctx.json(t);

	}
	
	public static void getMessages(Context ctx) {
		log.trace("Getting messages");
		
		Employee e;
		
		if(ctx.sessionAttribute("Employee") != null) {
			e = ctx.sessionAttribute("Employee");
		}
		else if(ctx.sessionAttribute("DirectSupervisor") != null) {
			e = ctx.sessionAttribute("DirectSupervisor");
		}
		else if(ctx.sessionAttribute("DepartmentHead") != null) {
			e = ctx.sessionAttribute("DepartmentHead");
		}
		else if(ctx.sessionAttribute("DirectHead") != null) {
			e = ctx.sessionAttribute("DirectHead");
		}
		else if(ctx.sessionAttribute("BenefitsCoordinator") != null) {
			e = ctx.sessionAttribute("BenefitsCoordinator");
		}
		else {
			ctx.status(401);
			log.warn("You aren't logged in to an appropriate account");
			return;
		}
		List<Message> m = rs.getMessages(e.getUsername());
		if(m == null) {
			m = new ArrayList<Message>();
		}
		ctx.json(m);
	}
	
	public static void approveReimbursement(Context ctx) {
		log.trace("Begin approval process");
		if((ctx.sessionAttribute("DirectSupervisor") != null) 
				|| (ctx.sessionAttribute("DepartmentHead") != null) 
				|| (ctx.sessionAttribute("DirectHead") != null) 
				|| (ctx.sessionAttribute("BenefitsCoordinator") != null)) {
			int id = Integer.parseInt(ctx.pathParam("id"));
			TuitionReimbursement t = rs.getReimbursement(id);
			if(t == null) {
				log.warn("Cannot find reimbursement of id " + id);
				ctx.status(204);
				return;
			}
			switch(t.getApprovalLevel()) {
				case 1: 
					directSupervisorApprove(ctx, t, id);
					break;
				case 2:
					departmentHeadApprove(ctx, t, id);
					break;
				case 3:
					benCoApprove(ctx, t, id);
					break;
				default:
					log.warn("Already approved");
					ctx.status(204);
					return;
			}
		}
		else {
			ctx.status(403);
			log.warn("You aren't logged in to an appropriate account");
			return;
		}
		

	}
	
	private static void benCoApprove(Context ctx, TuitionReimbursement t, int id) {
		log.trace("Benefits Coordinator begins approval process");
		Employee e;
		if(ctx.sessionAttribute("BenefitsCoordinator") != null) {
			e = ctx.sessionAttribute("BenefitsCoordinator");
		}
		else {
			ctx.status(403);
			log.warn("You aren't logged in to an appropriate account");
			return;
		}
		String approve = ctx.formParam("approve");
		
		if(t.getApprovalLevel() > 3) {
			log.warn("This reimbursement has already been approved");
			return;
		}
		if(approve.equals("yes")) {
			String largerAmount = ctx.formParam("largerAmount");
			t.setApprovalLevel(4);
			t.setBenCo(e.getUsername()); 
			if(!largerAmount.isEmpty()) {
				String reason = ctx.formParam("reason");
				rs.sendMessage(new Message(rs.nextMessageId(), id, t.getEmployee(), e.getUsername(), reason)); // Send reason for larger amount
				t.setPendingReimbursement(Double.parseDouble(largerAmount));
			}
			rs.sendMessage(new Message(rs.nextMessageId(), id, t.getEmployee(), e.getUsername(), "Please send me a copy of your grade/presentation for final approval")); // Notify to send grade/presentation
			rs.updateReimbursement(t);
		}
		else if(approve.equals("no")) {
			String reason = ctx.formParam("reason");
			rs.sendMessage(new Message(rs.nextMessageId(), id, t.getEmployee(), e.getUsername(), reason)); // Send reason for rejection

		}
		else if(approve.equals("employeeInfo")) {
			String reason = ctx.formParam("reason");
			rs.sendMessage(new Message(rs.nextMessageId(), id, t.getEmployee(), e.getUsername(), reason)); // Send request for info from employee
		}
		else if(approve.equals("directSupervisorInfo")) {
			String reason = ctx.formParam("reason");
			rs.sendMessage(new Message(rs.nextMessageId(), id, t.getDirSu(), e.getUsername(), reason)); // Send request for info from direct supervisor
		}		
		else if(approve.equals("departmentHeadInfo")) {
			String reason = ctx.formParam("reason");
			rs.sendMessage(new Message(rs.nextMessageId(), id, t.getDepHe(), e.getUsername(), reason)); // Send request for info from direct supervisor
		}	
		ctx.json(t);

	}

	private static void directSupervisorApprove(Context ctx, TuitionReimbursement t, int id) {
		log.trace("Direct Supervisor begins approval process");
		Employee e;
		if(ctx.sessionAttribute("DirectSupervisor") != null) {
			e = ctx.sessionAttribute("DirectSupervisor");
		}
		else if(ctx.sessionAttribute("DirectHead") != null) {
			e = ctx.sessionAttribute("DirectHead");
		}
		else {
			ctx.status(403);
			log.warn("You aren't logged in to an appropriate account");
			return;
		}
		String approve = ctx.formParam("approve");

		if(t.getApprovalLevel() > 1) {
			log.warn("This reimbursement has already been approved by a direct supervisor");
			return;
		}
		if(approve.equals("yes")) {
			if(e.getRole() == 2) {
				t.setApprovalLevel(2);
			}
			else if(e.getRole() == 4) {
				t.setApprovalLevel(3);
				t.setDepHe(e.getUsername()); // Since this user is both direct supervisor and department head, save this name as department head and skip
			}
			rs.updateReimbursement(t);
		}
		else if(approve.equals("no")) {
			String reason = ctx.formParam("reason");
			rs.sendMessage(new Message(rs.nextMessageId(), id, t.getEmployee(), e.getUsername(), reason)); // Send reason for rejection
		}
		else if(approve.equals("info")) {
			String reason = ctx.formParam("reason");
			rs.sendMessage(new Message(rs.nextMessageId(), id, t.getEmployee(), e.getUsername(), reason)); // Send request for info
		}
		ctx.json(t);
	}
	
	private static void departmentHeadApprove(Context ctx, TuitionReimbursement t, int id) {
		log.trace("Department Head begins approval process");
		Employee e;
		if(ctx.sessionAttribute("DepartmentHead") != null) {
			e = ctx.sessionAttribute("DepartmentHead");
		}
		else {
			ctx.status(403);
			log.warn("You aren't logged in to an appropriate account");
			return;
		}
		String approve = ctx.formParam("approve");

		if(t.getApprovalLevel() > 2) {
			log.warn("This reimbursement has already been approved by a department head");
			return;
		}
		if(approve.equals("yes")) {
			t.setApprovalLevel(3);
			t.setDepHe(e.getUsername()); 
			rs.updateReimbursement(t);
		}
		else if(approve.equals("no")) {
			String reason = ctx.formParam("reason");
			rs.sendMessage(new Message(rs.nextMessageId(), id, t.getEmployee(), e.getUsername(), reason)); // Send reason for rejection

		}
		else if(approve.equals("employeeInfo")) {
			String reason = ctx.formParam("reason");
			rs.sendMessage(new Message(rs.nextMessageId(), id, t.getEmployee(), e.getUsername(), reason)); // Send request for info from employee
		}
		else if(approve.equals("directSupervisorInfo")) {
			String reason = ctx.formParam("reason");
			rs.sendMessage(new Message(rs.nextMessageId(), id, t.getDirSu(), e.getUsername(), reason)); // Send request for info from direct supervisor
		}
		ctx.json(t);

	}
	
	public static void createReimbursement(Context ctx) {
		log.trace("Creating reimbursement");
		
		Employee e;
		
		if(ctx.sessionAttribute("Employee") != null) {
			e = ctx.sessionAttribute("Employee");
		}
		else if(ctx.sessionAttribute("DirectSupervisor") != null) {
			e = ctx.sessionAttribute("DirectSupervisor");
		}
		else if(ctx.sessionAttribute("DepartmentHead") != null) {
			e = ctx.sessionAttribute("DepartmentHead");
		}
		else if(ctx.sessionAttribute("DirectHead") != null) {
			e = ctx.sessionAttribute("DirectHead");
		}
		else if(ctx.sessionAttribute("BenefitsCoordinator") != null) {
			e = ctx.sessionAttribute("BenefitsCoordinator");
		}
		else {
			ctx.status(401);
			log.warn("You aren't logged in to an appropriate account");
			return;
		}
		Integer id = rs.nextId();
		String name = e.getUsername();
		String dirSu = ctx.formParam("supervisor");
		if(es.getEmployee(dirSu) == null) {
			log.warn("Direct supervisor " + dirSu + " not found");
		}
		String date = ctx.formParam("date");
		String time = ctx.formParam("time");
		String location = ctx.formParam("location");
		String description = ctx.formParam("description");
		double cost = Double.parseDouble(ctx.formParam("cost"));
		boolean presentationRequired = false;
		if(ctx.formParam("presentationRequired").equals("yes")) {
			presentationRequired = true;
		}
		else if(ctx.formParam("presentationRequired").equals("no")) {
			presentationRequired = false;
		}
		else {
			log.warn("Presentation required field is wrong");
		}
		GradingFormat gradingFormat = new GradingFormat(Double.parseDouble(ctx.formParam("gradingPercentage")), presentationRequired);
		Event event = Event.valueOf(ctx.formParam("event"));
		String justification = ctx.formParam("justification");
		int approvalLevel = 1;
		boolean urgent = false;
		if(LocalDate.now().isAfter(LocalDate.parse(date).minusDays(14))) {
			// If today's date is after the day two weeks before the event date
			urgent = true;
		}
		double pendingReimbursement = cost * event.getPercent();
		double availableReimbursement = e.getAvailableReimbursement();
		if(pendingReimbursement > availableReimbursement) {
			// Pending reimbursement exceeds available reimbursement
			pendingReimbursement = availableReimbursement;
		}
		List<String> attachments = new ArrayList<String>(); 
		uploadFile(ctx);
		List<Upload> uploads = rs.getUploads(e);
		int uploadCount = 0;
		for(Upload u : uploads) {
			attachments.add(u.getFileURL());
			log.trace("added " + u.getFileURL());
			uploadCount++;
		}
		if(uploadCount > 0) {
			approvalLevel = 3;
		}
		
		TuitionReimbursement reimbursementForm = new TuitionReimbursement(id, name, dirSu, "NotYetViewed", "NotYetViewed", date, time, location, description, cost,
				gradingFormat, event, justification, approvalLevel, urgent, pendingReimbursement, false, attachments);
		if(rs.createReimbursement(reimbursementForm)) {
			// If it was a success, add the form id to employee and subtract from available reimbursement
			e.setAvailableReimbursement(availableReimbursement - pendingReimbursement);
			e.getFormId().add(id);
			es.updateEmployee(e);
			rs.sendMessage(new Message(rs.nextMessageId(), id, dirSu, name, "Reimbursement form from " + name + " is ready to view.")); // Send notification message to direct supervisor
			ctx.json(reimbursementForm);
		}

		return;
	}
}
