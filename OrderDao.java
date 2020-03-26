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

import com.tj.Fruits.dto.OrderDto;

public class OrderDao {
	public static final int SUCCESS = 1;
	public static final int FAIL    = 0;
	
	private static OrderDao instance;
	
	public static OrderDao getInstance() {
		if(instance == null) {
			instance = new OrderDao();
		}
		return instance;
	}
	
	private OrderDao() {}
	
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
	
	// 상품 구매
	public int orderWrite(OrderDto dto) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "INSERT INTO ORDERS (oNO, cID, oNAME, oADDRESS, oTEL, pCODE, opNAME, oIMGNAME, QTY, COST)" + 
					 " VALUES (O_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getcId());
			pstmt.setString(2, dto.getoName());
			pstmt.setString(3, dto.getoAddress());
			pstmt.setString(4, dto.getoTel());
			pstmt.setString(5, dto.getpCode());
			pstmt.setString(6, dto.getOpName());
			pstmt.setString(7, dto.getoImgname());
			pstmt.setInt(8, dto.getQty());
			pstmt.setInt(9, dto.getCost());
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
	
	// 구매 취소
	public int orderDelete(int oNo) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "UPDATE ORDERS SET oDEILVERY=2 WHERE oNO=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oNo);
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
	
	// 배송 처리
	public int orderDeilvery(int oNo) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "UPDATE ORDERS SET oDEILVERY = 1 WHERE oNO=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oNo);
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
	
	// 아이디로 구매내역 가져오기
	public ArrayList<OrderDto> orderList(String cId) {
		ArrayList<OrderDto> orders = new ArrayList<OrderDto>();
		Connection 		    conn   = null;
		PreparedStatement 	pstmt  = null;
		ResultSet 		    rs 	   = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, O.* FROM (SELECT * FROM ORDERS ORDER BY oNO DESC) O)" + 
					 " WHERE cID=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int    oNo 	 	 = rs.getInt("oNo");
				String oName     = rs.getString("oName");
				String oAddress  = rs.getString("oAddress");
				String oTel 	 = rs.getString("oTel");
				Date   oDate 	 = rs.getDate("oDate");
				int    oDeilvery = rs.getInt("oDeilvery");
				String pCode 	 = rs.getString("pCode");
				String opName	 = rs.getString("opName");
				String oImgname	 = rs.getString("oImgname");
				int    qty		 = rs.getInt("qty");
				int	   cost      = rs.getInt("cost");
				orders.add(new OrderDto(oNo, cId, oName, oAddress, oTel, oDate, oDeilvery, pCode, opName, oImgname, qty, cost));
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
		return orders;
	}
	
	// 최신 구매내역 가져오기
	public int getTopoNo() {
		int 			  result = 0;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs 	 = null;
		String sql = "SELECT oNO FROM (SELECT ROWNUM RN, O.* FROM (SELECT * FROM ORDERS ORDER BY oNO DESC) O )" + 
					 " WHERE RN = 1";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt("oNo");
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
	
	public int orderProduct(String cId, String pCode) {
		int 			  result = 0;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs 	 = null;
		String sql = "SELECT COUNT(*) FROM ORDERS WHERE cID=? AND pCode=?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cId);
			pstmt.setString(2, pCode);
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
