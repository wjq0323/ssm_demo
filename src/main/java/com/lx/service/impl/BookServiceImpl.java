package com.lx.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lx.dao.AppointmentDao;
import com.lx.dao.BookDao;
import com.lx.dto.AppointExecution;
import com.lx.emuns.AppointStateEnum;
import com.lx.entity.Appointment;
import com.lx.entity.Book;
import com.lx.exception.AppointException;
import com.lx.exception.NoNumberException;
import com.lx.exception.RepeatAppointException;
import com.lx.service.BookService;
@Service
public class BookServiceImpl implements BookService{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// 娉ㄥ叆Service渚濊禆
	@Autowired
	private BookDao bookDao;

	@Autowired
	private AppointmentDao appointmentDao;


	@Override
	public Book getById(long bookId) {
		return bookDao.queryById(bookId);
	}

	@Override
	public List<Book> getList() {
		return bookDao.queryAll(0, 1000);
	}

	@Override
	@Transactional
	/**
	 * 浣跨敤娉ㄨВ鎺у埗浜嬪姟鏂规硶鐨勪紭鐐癸細 1.寮�鍙戝洟闃熻揪鎴愪竴鑷寸害瀹氾紝鏄庣‘鏍囨敞浜嬪姟鏂规硶鐨勭紪绋嬮鏍�
	 * 2.淇濊瘉浜嬪姟鏂规硶鐨勬墽琛屾椂闂村敖鍙兘鐭紝涓嶈绌挎彃鍏朵粬缃戠粶鎿嶄綔锛孯PC/HTTP璇锋眰鎴栬�呭墺绂诲埌浜嬪姟鏂规硶澶栭儴
	 * 3.涓嶆槸鎵�鏈夌殑鏂规硶閮介渶瑕佷簨鍔★紝濡傚彧鏈変竴鏉′慨鏀规搷浣滐紝鍙鎿嶄綔涓嶉渶瑕佷簨鍔℃帶鍒�
	 */
	public AppointExecution appoint(long bookId, long studentId) {
		try {
			// 鍑忓簱瀛�
			int update = bookDao.reduceNumber(bookId);
			if (update <= 0) {// 搴撳瓨涓嶈冻
				//return new AppointExecution(bookId, AppointStateEnum.NO_NUMBER);//閿欒鍐欐硶				
				throw new NoNumberException("no number");
			} else {
				// 鎵ц棰勭害鎿嶄綔
				int insert = appointmentDao.insertAppointment(bookId, studentId);
				if (insert <= 0) {// 閲嶅棰勭害
					//return new AppointExecution(bookId, AppointStateEnum.REPEAT_APPOINT);//閿欒鍐欐硶
					throw new RepeatAppointException("repeat appoint");
				} else {// 棰勭害鎴愬姛
					Appointment appointment = (Appointment) appointmentDao.queryByKeyWithBook(bookId, studentId);
					return new AppointExecution(bookId, AppointStateEnum.SUCCESS, appointment);
				}
			}
		// 瑕佸厛浜巆atch Exception寮傚父鍓嶅厛catch浣忓啀鎶涘嚭锛屼笉鐒惰嚜瀹氫箟鐨勫紓甯镐篃浼氳杞崲涓篈ppointException锛屽鑷存帶鍒跺眰鏃犳硶鍏蜂綋璇嗗埆鏄摢涓紓甯�
		} catch (NoNumberException e1) {
			throw e1;
		} catch (RepeatAppointException e2) {
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 鎵�鏈夌紪璇戞湡寮傚父杞崲涓鸿繍琛屾湡寮傚父
			//return new AppointExecution(bookId, AppointStateEnum.INNER_ERROR);//閿欒鍐欐硶
			throw new AppointException("appoint inner error:" + e.getMessage());
		}
	}

	@Override
	public int deleteById(long bookId) {
		int count = 0;
		count += bookDao.deleteById(bookId);
		return count;
	}
}
