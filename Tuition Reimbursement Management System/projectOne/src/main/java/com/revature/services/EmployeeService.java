package com.revature.services;

import com.revature.beans.Employee;

public interface EmployeeService {
	Employee getEmployee(String username);
	boolean addEmployee(Employee e);
	void updateEmployee(Employee e);
}
