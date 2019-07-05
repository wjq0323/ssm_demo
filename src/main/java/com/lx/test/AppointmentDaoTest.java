package com.lx.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.lx.dao.AppointmentDao;


public class AppointmentDaoTest extends BaseTest{
	@Autowired
	private AppointmentDao appointmentDao;

	@Test
	public void testInsertAppointment() throws Exception {
		long bookId = 1000;
		long studentId = 12345678910L;
		int insert = appointmentDao.insertAppointment(bookId, studentId);
		System.out.println("insert=" + insert);
	}

}
