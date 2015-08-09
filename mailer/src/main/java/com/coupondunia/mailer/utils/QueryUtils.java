package com.coupondunia.mailer.utils;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.coupondunia.mailer.base.Application;
import com.coupondunia.mailer.base.DbConnectionPool;
import com.coupondunia.mailer.beans.EmailQueue;
import com.coupondunia.mailer.constants.Constants;

/**
 * QueryUtils provide Apis for Database query related task.
 * @author neeraj 
 * neeraj.nayal.2008@gmail.com
 */
public class QueryUtils extends Object {

	/**
	 * retrieve the email record from database with status = Pending or (Fail
	 * with retry < max_retry )
	 * 
	 * @return Arraylist of Email Records
	 * @throws Exception
	 */
	public static ArrayList<EmailQueue> getEmailRecords() throws Exception {

		int slotSize = Constants.DEFAULT_SLOT_SIZE;
		int maxRetry = Constants.DEFAULT_DB_RETRY;
		try {
			slotSize = Application.getInstance().getAppConfig().getDbSlotSize();
			maxRetry = Application.getInstance().getAppConfig().getMaxRetry();
		} catch (Exception ex) {
			System.out.println("[Error] unable to get email record from db");
			throw ex;
		}

		Connection conn = DbConnectionPool.getInstance().getConnection();
		ArrayList<EmailQueue> result = new ArrayList<EmailQueue>();

		StringBuilder selectSql = new StringBuilder();
		StringBuilder updateSql = new StringBuilder();

		selectSql = selectSql.append("Select ")
				.append(Constants.EMAIL_ID + ",")
				.append(Constants.EMAIL_FROM + ",")
				.append(Constants.EMAIL_TO + ",")
				.append(Constants.EMAIL_SUBJECT + ",")
				.append(Constants.EMAIL_BODY + ",")
				.append(Constants.EMAIL_STATUS + ",")
				.append(Constants.EMAIL_ATTEMPT)
				.append(" from " + Constants.TBL_EMAIL_QUEUE)
				.append(" where status = '" + Constants.STATUS_PENDING + "'")
				.append(" Or ( attempt < " + maxRetry + " And")
				.append(" status = '" + Constants.STATUS_FAIL + "')")
				.append(" order by id desc limit " + slotSize);

		updateSql = updateSql.append("UPDATE EmailQueue SET status = '")
				.append(Constants.STATUS_LOCK + "' ")
				.append("WHERE status in ('" + Constants.STATUS_PENDING + "',")
				.append("'" + Constants.STATUS_FAIL + "')and id = ?");

		if (conn != null) {
			try {
				result = fetchAndLockRecords(conn, selectSql.toString(),
						updateSql.toString());
			} catch (Exception e) {

			} finally {
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					System.out
							.println("[Warning] Unable to close Database resource.");
				}
			}
		}
		return result;
	}

	/**
	 * Fetch record from database and update it to Lock. at a time Only one
	 * thread from one JVM can enter in this method.
	 * 
	 * @param conn
	 *            - database connection
	 * @param selectSql
	 *            - SQL query to select record
	 * @param updateSql
	 *            - SQL query to update record
	 * @return return selected record.
	 * @throws Exception
	 */
	private static synchronized ArrayList<EmailQueue> fetchAndLockRecords(
			Connection conn, String selectSql, String updateSql)
			throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement preparedStatement = null;
		ArrayList<EmailQueue> result = new ArrayList<EmailQueue>();
		ArrayList<EmailQueue> tmp = new ArrayList<EmailQueue>();
		int commitSize = Application.getInstance().getAppConfig()
				.getCommitSize();

		try {

			stmt = conn.createStatement();
			rs = stmt.executeQuery(selectSql);
			preparedStatement = conn.prepareStatement(updateSql);
			int count = 0;
			while (rs.next()) {

				count++;
				int id = Integer.parseInt(rs.getString(Constants.EMAIL_ID));
				preparedStatement.setInt(1, id);
				preparedStatement.addBatch();

				// create EmailQueue record and add in temporary arraylist
				EmailQueue eq = new EmailQueue();
				eq.setEmailId(Long.parseLong(rs.getString(Constants.EMAIL_ID)));
				eq.setFromEmail(rs.getString(Constants.EMAIL_FROM));
				eq.setToEmail(rs.getString(Constants.EMAIL_TO));
				eq.setSubject(rs.getString(Constants.EMAIL_SUBJECT));
				eq.setBody(rs.getString(Constants.EMAIL_BODY));
				eq.setStatus(rs.getString(Constants.EMAIL_STATUS));
				eq.setAttempt(Integer.parseInt(rs
						.getString(Constants.EMAIL_ATTEMPT)));
				tmp.add(eq);

				// if count  is equal to the commit size. execute the batch
				// with auto commit = true
				if (count % commitSize == 0) {
					try {
						int status[] = preparedStatement.executeBatch();
						for (int inner = 0; inner < status.length; inner++) {
							// if particular prepare statement successful to
							// change status = lock
							// add the record in result set to send mail.
							if (status[inner] == 1)
								result.add(tmp.get(inner));
						}

					} catch (BatchUpdateException e) {
						/*
						 * When two different JVM try to update same record,
						 * exception will occur for one of them. do not include record
						 * for that JVM.
						 * if exception do not include in result set
						 */

						int[] updateCounts = e.getUpdateCounts();
						for (int inner = 0; inner < updateCounts.length; inner++) {
							if (updateCounts[inner] > 0) {
								// Successfully executed; the number represents
								// number of affected rows
								result.add(tmp.get(inner));
							} else if (updateCounts[inner] == Statement.EXECUTE_FAILED) {
								// Failed to execute - record updated by some
								// other process
							}

						}

					} finally {
						tmp.clear();
						preparedStatement.clearBatch();
					}

				}
			}

			// if selected record are less than commit size
			try {
				int status[] = preparedStatement.executeBatch();
				for (int inner = 0; inner < status.length; inner++) {
					// if particular prepare statement successful to change
					// status = lock
					// add the record in result set to send mail.
					if (status[inner] == 1)
						result.add(tmp.get(inner));
				}

			} catch (BatchUpdateException e) {
				/*
				 * if exception do not include in result set
				 */
				int[] updateCounts = e.getUpdateCounts();
				for (int inner = 0; inner < updateCounts.length; inner++) {
					if (updateCounts[inner] > 0) {
						// Successfully executed; the number represents
						// number of affected rows
						result.add(tmp.get(inner));
					} else if (updateCounts[inner] == Statement.EXECUTE_FAILED) {
						// Failed to execute - record updated by some
						// other process
					}
				}
				
			} finally {
				tmp.clear();
				preparedStatement.clearBatch();
			}

		} catch (Exception e) {
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (Exception e) {
				System.out
						.println("[Warning] Unable to close Database resource.");
			}
		}
		QueryUtils.class.notify();
		return result;
	}

	/**
	 * after sending mail, record should be updated. status could be Success or
	 * Fail. depending on the mail send result.
	 * 
	 * @param email
	 *            - email record to be updated.
	 * @param status
	 *            - status to be set in email record
	 * @throws Exception
	 */
	public static void updateStatusOfRecord(ArrayList<EmailQueue> emails)
			throws Exception {

		int commitSize = Application.getInstance().getAppConfig()
				.getCommitSize();
		Connection conn = null;
		StringBuilder sb = new StringBuilder();
		sb = sb.append("UPDATE EmailQueue SET status = ? ,")
				.append(" process_time = ? ,").append(" attempt = ? ")
				.append(" WHERE status ='" + Constants.STATUS_LOCK + "'")
				.append(" and id = ?");
		String updateSql = sb.toString();
		PreparedStatement preparedStatement = null;
		try {
			// connection with auto commit false
			conn = DbConnectionPool.getInstance().getConnection(false);
			preparedStatement = conn.prepareStatement(updateSql);
			conn.setSavepoint();
			for (int idx = 0; idx < emails.size(); idx++) {
				EmailQueue eq = emails.get(idx);
				preparedStatement.setString(1, eq.getStatus());
				preparedStatement.setTimestamp(2, eq.getSendTime());
				preparedStatement.setInt(3, eq.getAttempt());
				preparedStatement.setLong(4, eq.getEmailId());
				preparedStatement.addBatch();

				if (idx % commitSize == 0) {
					try {
						preparedStatement.executeBatch();
						conn.commit();
						preparedStatement.clearBatch();
					} catch (BatchUpdateException e) {
						conn.rollback();
						// this block will execute only if mail is sent/fail but status
						// is not updated in database.Ideally it should not be happend.
						// but can occur , if database connection is reset or not available.
						System.out
								.println("[Error] Mail sent for some email id but unable to update status in database");
					}
				}
			}

			// if email size is less than commit size
			try {
				preparedStatement.executeBatch();
				conn.commit();
				preparedStatement.clearBatch();
			} catch (BatchUpdateException e) {
				conn.rollback();
				// this block will execute only if mail is sent/fail but status
				// is not updated in database.Ideally it should not be happend.
				// but can occur , if database connection is reset or not
				// available.
				System.out
						.println("[Error] Mail sent for some email id but unable to update status in database");
			}

		} catch (Exception e) {
			System.out.println("[Error] Unable to update Database.");
			throw e;
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out
						.println("[Warning] Unable to close Database resource.");
			}
		}
	}

}
