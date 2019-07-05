package com.lx.service;

import java.util.List;

import com.lx.dto.AppointExecution;
import com.lx.entity.Book;

public interface BookService {
	/**
	 * 鏌ヨ涓�鏈浘涔�
	 * 
	 * @param bookId
	 * @return
	 */
	Book getById(long bookId);

	/**
	 * 鏌ヨ鎵�鏈夊浘涔�
	 * 
	 * @return
	 */
	List<Book> getList();

	/**
	 * 棰勭害鍥句功
	 * 
	 * @param bookId
	 * @param studentId
	 * @return
	 */
	AppointExecution appoint(long bookId, long studentId);
	int deleteById(long bookId);
}
