package com.tj.Fruits.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.tj.Fruits.dto.AdminDto;

public class AdminDao {
	public static final int SUCCESS = 1;
	public static final int FAIL    = 0;
	
	private static AdminDao instance;

	public static AdminDao getInstance() {
		if(instance == null) {
			instance = new AdminDao();
		}
		return instance;
	}
	
	private AdminDao() {}
	
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
	
	// 아이디 중복확인
	public int adminIdConfirm(String aId) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs     = null;
		String sql = "SELECT * FROM ADMIN WHERE aID=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = FAIL;
			} else {
				result = SUCCESS;
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
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
	
	// 관리자 계정생성
	public int adminJoin(AdminDto dto) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "INSERT INTO ADMIN VALUES (?, ?, ?)";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getaId());
			pstmt.setString(2, dto.getaPw());
			pstmt.setString(3, dto.getaName());
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
	
	// 관리자 로그인
	public int adminLogin(String aId, String aPw) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs = null;
		String sql = "SELECT * FROM ADMIN WHERE aID=? AND aPW=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aId);
			pstmt.setString(2, aPw);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = SUCCESS;
			} else {
				result = FAIL;
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
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
	
	// 관리자 정보 가져오기
	public AdminDto getAdmin(String aId) {
		AdminDto   		  admin = null;
		Connection 		  conn  = null;
		PreparedStatement pstmt = null;
		ResultSet 		  rs    = null;
		String sql = "SELECT * FROM ADMIN WHERE aID=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String aPw   = rs.getString("aPw");
				String aName = rs.getString("aName");
				admin = new AdminDto(aId, aPw, aName);
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
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
		return admin;
	}
	
	// 관리자 계정삭제
	public int adminDelete(String aId, String aPw) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "DELETE FROM ADMIN WHERE aID=? AND aPW=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, aId);
			pstmt.setString(2, aPw);
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
