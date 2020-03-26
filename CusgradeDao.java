package com.tj.Fruits.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.tj.Fruits.dto.CusgradeDto;

public class CusgradeDao {
	public static final int SUCCESS = 1;
	public static final int FAIL    = 0;
	
	private static CusgradeDao instance;
	
	public static CusgradeDao getInstance() {
		if(instance ==  null) {
			instance = new CusgradeDao();
		}
		return instance;
	}
	
	private CusgradeDao() {}
	
	private Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Context    ctx = new InitialContext();
			DataSource ds  = (DataSource)ctx.lookup("java:comp/env/jdbc/Oracle11g");
			conn = ds.getConnection();
		} catch (NamingException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	// 고객 등급 생성
	public int cusgradeWrite(CusgradeDto dto) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "INSERT INTO CUSGRADE VALUES (?, ?, ?)";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getcGrade());
			pstmt.setInt(2, dto.getLowGrade());
			pstmt.setInt(3, dto.getHighGrade());
			result = pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}
}
