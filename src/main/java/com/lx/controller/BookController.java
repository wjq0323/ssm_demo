package com.lx.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lx.dto.AppointExecution;
import com.lx.dto.Result;
import com.lx.emuns.AppointStateEnum;
import com.lx.entity.Book;
import com.lx.exception.NoNumberException;
import com.lx.exception.RepeatAppointException;
import com.lx.service.BookService;

@Controller
@RequestMapping("/book") // url:/妯″潡/璧勬簮/{id}/缁嗗垎 /seckill/list
public class BookController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	private String list(Model model) {
		List<Book> list = bookService.getList();
		model.addAttribute("list", list);
		// list.jsp + model = ModelAndView
		return "list";// WEB-INF/jsp/"list".jsp
	}

	@RequestMapping(value = "/{bookId}/detail", method = RequestMethod.GET)
	private String detail(@PathVariable("bookId") Long bookId, Model model) {
		if (bookId == null) {
			return "redirect:/book/list";
		}
		Book book = bookService.getById(bookId);
		if (book == null) {
			return "forward:/book/list";
		}
		model.addAttribute("book", book);
		return "detail";
	}

	//ajax json
	@RequestMapping(value = "/{bookId}/appoint", produces = {
			"application/json; charset=utf-8" })
	@ResponseBody
	private Result<AppointExecution> appoint(@PathVariable("bookId") Long bookId, @RequestParam("studentId") Long studentId) {
		if (studentId == null || studentId.equals("")) {
			return new Result<>(false, "瀛﹀彿涓嶈兘涓虹┖");
		}
		//AppointExecution execution = bookService.appoint(bookId, studentId);//閿欒鍐欐硶锛屼笉鑳界粺涓�杩斿洖锛岃澶勭悊寮傚父锛堝け璐ワ級鎯呭喌
		AppointExecution execution = null;
		try {
			execution = bookService.appoint(bookId, studentId);
		} catch (NoNumberException e1) {
			execution = new AppointExecution(bookId, AppointStateEnum.NO_NUMBER);
		} catch (RepeatAppointException e2) {
			execution = new AppointExecution(bookId, AppointStateEnum.REPEAT_APPOINT);
		} catch (Exception e) {
			execution = new AppointExecution(bookId, AppointStateEnum.INNER_ERROR);
		}
		return new Result<AppointExecution>(true, execution);
	}

	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	private int delete(Long bookId) {
		int count = bookService.deleteById(bookId);
		// list.jsp + model = ModelAndView
		// WEB-INF/jsp/"list".jsp
		return count;
	}
}
