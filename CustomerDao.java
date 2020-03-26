package com.tj.Fruits.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.tj.Fruits.dto.CustomerDto;

public class CustomerDao {
	public static final int SUCCESS = 1;
	public static final int FAIL    = 0;
	
	private static CustomerDao instance;
	
	public static CustomerDao getInstance() {
		if(instance == null) {
			instance = new CustomerDao();
		}
		return instance;
	}
	
	private CustomerDao() {}
	
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
	public int idConfirm(String cId) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs	 = null;
		String sql = "SELECT * FROM CUSTOMER WHERE cID=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cId);
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
	
	// 회원가입
	public int customerJoin(CustomerDto dto) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "INSERT INTO CUSTOMER (cID, cPW, cNAME, cEMAIL, cTEL, cADDRESS, cGENDER, cBIRTH)" + 
					 " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getcId());
			pstmt.setString(2, dto.getcPw());
			pstmt.setString(3, dto.getcName());
			pstmt.setString(4, dto.getcEmail());
			pstmt.setString(5, dto.getcTel());
			pstmt.setString(6, dto.getcAddress());
			pstmt.setString(7, dto.getcGender());
			pstmt.setDate(8, dto.getcBirth());
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
	
	// 아이디 찾기
	public CustomerDto searchId(String cName, String cEmail) {
		CustomerDto 	  customer = null;
		Connection 		  conn 	   = null;
		PreparedStatement pstmt    = null;
		ResultSet 		  rs 	   = null;
		String sql = "SELECT * FROM CUSTOMER WHERE cNAME=? AND cEMAIL=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cName);
			pstmt.setString(2, cEmail);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String cId = rs.getString("cId");
				customer = new CustomerDto(cId, null, cName, cEmail, null, null, null, null, 0, 0, null);
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
		return customer;
	}
	
	// 비밀번호 찾기
	public CustomerDto searchPw(String cId, String cName) {
		CustomerDto 	  customer = null;
		Connection 		  conn 	   = null;
		PreparedStatement pstmt    = null;
		ResultSet 		  rs 	   = null;
		String sql = "SELECT * FROM CUSTOMER WHERE cID=? AND cNAME=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cId);
			pstmt.setString(2, cName);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String cPw = rs.getString("cPw");
				customer = new CustomerDto(cId, cPw, cName, null, null, null, null, null, 0, 0, null);
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
		return customer;
	}
	
	// 로그인
	public int customerLogin(String cId, String cPw) {
		int 			  result = FAIL;
		Connection 		  conn 	 = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs	 = null;
		String sql = "SELECT * FROM CUSTOMER WHERE cID=? AND cPW=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cId);
			pstmt.setString(2, cPw);
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
	
	// 아이디로 정보 가져오기
	public CustomerDto getCustomer(String cId) {
		CustomerDto 	  customer = null;
		Connection 		  conn     = null;
		PreparedStatement pstmt    = null;
		ResultSet 	  	  rs 	   = null;
		String sql = "SELECT * FROM CUSTOMER WHERE cID=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String cPw 	  	  = rs.getString("cPw");
				String cName 	  = rs.getString("cName");
				String cEmail 	  = rs.getString("cEmail");
				String cTel 	  = rs.getString("cTel");
				String cAddress   = rs.getString("cAddress");
				String cGender 	  = rs.getString("cGender");
				Date   cBirth 	  = rs.getDate("cBirth");
				int	   cTotamount = rs.getInt("cTotamount");
				int    cBonus 	  = rs.getInt("cBonus");
				String cGrade 	  = rs.getString("cGrade");
				customer = new CustomerDto(cId, cPw, cName, cEmail, cTel, cAddress, cGender, cBirth, cTotamount, cBonus, cGrade);
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
		return customer;
	}
	
	// 정보수정
	public int customerModify(CustomerDto dto) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "UPDATE CUSTOMER SET cPW=?, cNAME=?, cEMAIL=?, cTEL=?, cGENDER=?, cBIRTH=? WHERE cID=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getcPw());
			pstmt.setString(2, dto.getcName());
			pstmt.setString(3, dto.getcEmail());
			pstmt.setString(4, dto.getcTel());
			pstmt.setString(5, dto.getcGender());
			pstmt.setDate(6, dto.getcBirth());
			pstmt.setString(7, dto.getcId());
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
	
	// 회원탈퇴
	public int customerDelete(String cId, String cPw) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "DELETE CUSTOMER WHERE cID=? AND cPW=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cId);
			pstmt.setString(2, cPw);
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
	
	// 누적구매금액 증가
	public int totamountUp(String cId, int cost) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "UPDATE CUSTOMER SET cTOTAMOUNT=cTOTAMOUNT+?, cBONUS=cBONUS+(?*0.05)," + 
					" cGRADE=(SELECT G.cGRADE FROM CUSGRADE G, CUSTOMER C " + 
					" WHERE (SELECT cTOTAMOUNT+? FROM CUSTOMER WHERE cID=?)" + 
					" BETWEEN LOWGRADE AND HIGHGRADE AND cID=?) WHERE cID=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cost);
			pstmt.setInt(2, cost);
			pstmt.setInt(3, cost);
			pstmt.setString(4, cId);
			pstmt.setString(5, cId);
			pstmt.setString(6, cId);
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
	
	// 적립금 감소
	public void bonusDown(int cBonus, String cId) {
		Connection 		  conn  = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE CUSTOMER SET cBONUS=cBONUS-? WHERE cID=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cBonus);
			pstmt.setString(2, cId);
			pstmt.executeUpdate();
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
	}
	
	// 누적구매금액 감소
	public int totamountDown(String cId, int cost) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "UPDATE CUSTOMER SET cTOTAMOUNT=cTOTAMOUNT-?, cBONUS=cBONUS-(?*0.05)," + 
					 " cGRADE=(SELECT G.cGRADE FROM CUSGRADE G, CUSTOMER C " + 
					 " WHERE (SELECT cTOTAMOUNT-? FROM CUSTOMER WHERE cID=?)" + 
					 " BETWEEN LOWGRADE AND HIGHGRADE AND cID=?) WHERE cID=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cost);
			pstmt.setInt(2, cost);
			pstmt.setInt(3, cost);
			pstmt.setString(4, cId);
			pstmt.setString(5, cId);
			pstmt.setString(6, cId);
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
	
	// 회원 리스트
	public ArrayList<CustomerDto> customerList(int startRow, int endRow) {
		ArrayList<CustomerDto> customers = new ArrayList<CustomerDto>();
		Connection 			   conn 	 = null;
		PreparedStatement 	   pstmt 	 = null;
		ResultSet 			   rs 		 = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, C.* FROM " + 
					 " (SELECT * FROM CUSTOMER ORDER BY cNAME) C)" + 
					 " WHERE RN BETWEEN ? AND ?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String cId 		  = rs.getString("cId");
				String cPw 		  = rs.getString("cPw");
				String cName 	  = rs.getString("cName");
				String cEmail 	  = rs.getString("cEmail");
				String cTel 	  = rs.getString("cTel");
				String cAddress   = rs.getString("cAddress");
				String cGender    = rs.getString("cGender");
				Date   cBirth 	  = rs.getDate("cBirth");
				int    cTotamount = rs.getInt("cTotamount");
				int    cBonus 	  = rs.getInt("cBonus");
				String cGrade 	  = rs.getString("cGrade");
				customers.add(new CustomerDto(cId, cPw, cName, cEmail, cTel, cAddress, cGender, cBirth, cTotamount, cBonus, cGrade));
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
		return customers;
	}
	
	// 등급별 회원 리스트
	public ArrayList<CustomerDto> customerGradeList(int startRow, int endRow, String cGrade) {
		ArrayList<CustomerDto> customers = new ArrayList<CustomerDto>();
		Connection 			   conn 	 = null;
		PreparedStatement 	   pstmt 	 = null;
		ResultSet 			   rs 		 = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, C.* FROM " + 
					 " (SELECT * FROM CUSTOMER) C)" + 
					 " WHERE cGRADE=? AND RN BETWEEN ? AND ?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cGrade);
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String cId 		  = rs.getString("cId");
				String cPw 		  = rs.getString("cPw");
				String cName 	  = rs.getString("cName");
				String cEmail 	  = rs.getString("cEmail");
				String cTel 	  = rs.getString("cTel");
				String cAddress   = rs.getString("cAddress");
				String cGender 	  = rs.getString("cGender");
				Date   cBirth 	  = rs.getDate("cBirth");
				int    cTotamount = rs.getInt("cTotamount");
				int    cBonus 	  = rs.getInt("cBonus");
				customers.add(new CustomerDto(cId, cPw, cName, cEmail, cTel, cAddress, cGender, cBirth, cTotamount, cBonus, cGrade));
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
		return customers;
	}
	
	// 총 회원수 가져오기
	public int customerTotCnt() {
		int 			  result = 0;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs 	 = null;
		String sql = "SELECT COUNT(*) FROM CUSTOMER";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
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
}
