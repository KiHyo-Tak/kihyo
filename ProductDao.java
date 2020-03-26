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

import com.tj.Fruits.dto.ProductDto;

public class ProductDao {
	public static final int SUCCESS = 1;
	public static final int FAIL    = 0;
	
	private static ProductDao instance;
	
	public static ProductDao getInstance() {
		if(instance == null) {
			instance = new ProductDao();
		}
		return instance;
	}
	
	private ProductDao() {}
	
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
	
	// 상품 검색
	public ArrayList<ProductDto> productSearch(String pName, int startRow, int endRow) {
		ArrayList<ProductDto> products = new ArrayList<ProductDto>();
		Connection 			  conn     = null;
		PreparedStatement 	  pstmt    = null;
		ResultSet 			  rs 	   = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, P.* FROM " + 
					 " (SELECT * FROM PRODUCT ORDER BY pRDATE DESC) P)" + 
					 " WHERE pNAME LIKE '%'||?||'%' AND RN BETWEEN ? AND ?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pName);
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				pName 			 = rs.getString("pName");
				String pCode 	 = rs.getString("pCode");
				int    pStock 	 = rs.getInt("pStock");
				int    pPrice	 = rs.getInt("pPrice");
				int    pDiscount = rs.getInt("pDiscount");
				String pImgname  = rs.getString("pImgname");
				Date   pRdate 	 = rs.getDate("pRdate");
				int    pCategori = rs.getInt("pCategori");
				products.add(new ProductDto(pCode, pName, pStock, pPrice, pDiscount, pImgname, pRdate, pCategori));
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
		return products;
	}
	
	// 상품 리스트
	public ArrayList<ProductDto> productList(int startRow, int endRow) {
		ArrayList<ProductDto> products = new ArrayList<ProductDto>();
		Connection 			  conn	   = null;
		PreparedStatement 	  pstmt    = null;
		ResultSet 			  rs 	   = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, P.* FROM (SELECT * FROM PRODUCT " + 
					 " ORDER BY pRDATE DESC) P) WHERE RN BETWEEN ? AND ?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String pCode 	 = rs.getString("pCode");
				String pName 	 = rs.getString("pName");
				int    pStock 	 = rs.getInt("pStock");
				int    pPrice	 = rs.getInt("pPrice");
				int    pDiscount = rs.getInt("pDiscount");
				String pImgname  = rs.getString("pImgname");
				Date   pRdate 	 = rs.getDate("pRdate");
				int	   pCategori = rs.getInt("pCategori");
				products.add(new ProductDto(pCode, pName, pStock, pPrice, pDiscount, pImgname, pRdate, pCategori));
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
		return products;
	}
	
	// 할인율 있는 상품 리스트
	public ArrayList<ProductDto> productDiscountList(int startRow, int endRow) {
		ArrayList<ProductDto> products = new ArrayList<ProductDto>();
		Connection 			  conn 	   = null;
		PreparedStatement 	  pstmt    = null;
		ResultSet 			  rs 	   = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, P.* FROM (SELECT * FROM PRODUCT WHERE pDISCOUNT > 0" + 
					 " ORDER BY pDISCOUNT DESC) P) WHERE pDISCOUNT > 0 AND RN BETWEEN ? AND ?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String pCode 	 = rs.getString("pCode");
				String pName 	 = rs.getString("pName");
				int    pStock 	 = rs.getInt("pStock");
				int    pPrice	 = rs.getInt("pPrice");
				int    pDiscount = rs.getInt("pDiscount");
				String pImgname  = rs.getString("pImgname");
				Date   pRdate 	 = rs.getDate("pRdate");
				int	   pCategori = rs.getInt("pCategori");
				products.add(new ProductDto(pCode, pName, pStock, pPrice, pDiscount, pImgname, pRdate, pCategori));
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
		return products;
	}
	
	// 카테고리별 상품 리스트
	public ArrayList<ProductDto> productCategoriList(int pCategori, int startRow, int endRow) {
		ArrayList<ProductDto> products = new ArrayList<ProductDto>();
		Connection 			  conn 	   = null;
		PreparedStatement 	  pstmt    = null;
		ResultSet 			  rs 	   = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, P.* FROM (SELECT * FROM PRODUCT WHERE pCATEGORI=? " + 
					 " ORDER BY pRDATE DESC) P) WHERE pCATEGORI=? AND RN BETWEEN ? AND ?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pCategori);
			pstmt.setInt(2, pCategori);
			pstmt.setInt(3, startRow);
			pstmt.setInt(4, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String pCode 	 = rs.getString("pCode");
				String pName 	 = rs.getString("pName");
				int    pStock 	 = rs.getInt("pStock");
				int    pPrice	 = rs.getInt("pPrice");
				int    pDiscount = rs.getInt("pDiscount");
				String pImgname  = rs.getString("pImgname");
				Date   pRdate 	 = rs.getDate("pRdate");
				products.add(new ProductDto(pCode, pName, pStock, pPrice, pDiscount, pImgname, pRdate, pCategori));
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
		return products;
	}
	
	// 메인화면 신상품 4개
	public ArrayList<ProductDto> productMainList() {
		ArrayList<ProductDto> products = new ArrayList<ProductDto>();
		Connection 			  conn 	   = null;
		PreparedStatement 	  pstmt    = null;
		ResultSet 			  rs 	   = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, P.* FROM (SELECT * FROM PRODUCT " + 
					 " ORDER BY pRDATE DESC) P) WHERE RN BETWEEN 1 AND 4";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String pCode 	 = rs.getString("pCode");
				String pName 	 = rs.getString("pName");
				int    pStock 	 = rs.getInt("pStock");
				int    pPrice	 = rs.getInt("pPrice");
				int    pDiscount = rs.getInt("pDiscount");
				String pImgname  = rs.getString("pImgname");
				Date   pRdate 	 = rs.getDate("pRdate");
				int	   pCategori = rs.getInt("pCategori");
				products.add(new ProductDto(pCode, pName, pStock, pPrice, pDiscount, pImgname, pRdate, pCategori));
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
		return products;
	}
	
	// 메인화면 할인상품 4개
	public ArrayList<ProductDto> productMainDiscountList() {
		ArrayList<ProductDto> products = new ArrayList<ProductDto>();
		Connection 			  conn 	   = null;
		PreparedStatement 	  pstmt    = null;
		ResultSet 			  rs 	   = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, P.* FROM (SELECT * FROM PRODUCT " + 
					 " ORDER BY pDISCOUNT DESC) P) WHERE pDISCOUNT > 0 AND RN BETWEEN 1 AND 4";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String pCode 	 = rs.getString("pCode");
				String pName 	 = rs.getString("pName");
				int    pStock 	 = rs.getInt("pStock");
				int    pPrice	 = rs.getInt("pPrice");
				int    pDiscount = rs.getInt("pDiscount");
				String pImgname  = rs.getString("pImgname");
				Date   pRdate 	 = rs.getDate("pRdate");
				int	   pCategori = rs.getInt("pCategori");
				products.add(new ProductDto(pCode, pName, pStock, pPrice, pDiscount, pImgname, pRdate, pCategori));
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
		return products;
	}
	
	// 상품코드로 상세보기
	public ProductDto getProduct(String pCode) {
		ProductDto 		  product = null;
		Connection 		  conn 	  = null;
		PreparedStatement pstmt   = null;
		ResultSet 		  rs 	  = null;
		String sql = "SELECT * FROM PRODUCT WHERE pCODE=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pCode);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String pName 	 = rs.getString("pName");
				int    pStock 	 = rs.getInt("pStock");
				int    pPrice	 = rs.getInt("pPrice");
				int    pDiscount = rs.getInt("pDiscount");
				String pImgname  = rs.getString("pImgname");
				Date   pRdate 	 = rs.getDate("pRdate");
				int	   pCategori = rs.getInt("pCategori");
				product = new ProductDto(pCode, pName, pStock, pPrice, pDiscount, pImgname, pRdate, pCategori);
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
		return product;
	}
	
	// 상품코드 중복확인
	public int pCodeConfirm(String pCode) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs 	 = null;
		String sql = "SELECT * FROM PRODUCT WHERE pCODE=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pCode);
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
	
	// 상품 추가
	public int productWrite(ProductDto dto) {
		int 			  result = FAIL;
		Connection 		  conn  = null;
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO PRODUCT (pCODE, pNAME, pSTOCK, pPRICE, pDISCOUNT, pIMGNAME, pCategori)" + 
					 " VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getpCode());
			pstmt.setString(2, dto.getpName());
			pstmt.setInt(3, dto.getpStock());
			pstmt.setInt(4, dto.getpPrice());
			pstmt.setInt(5, dto.getpDiscount());
			pstmt.setString(6, dto.getpImgname());
			pstmt.setInt(7, dto.getpCategori());
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
	
	// 상품 수정
	public int productModify(ProductDto dto) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "UPDATE PRODUCT SET pNAME=?, pSTOCK=?, pPRICE=?, pDISCOUNT=?, pIMGNAME=? WHERE pCODE=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getpName());
			pstmt.setInt(2, dto.getpStock());
			pstmt.setInt(3, dto.getpPrice());
			pstmt.setInt(4, dto.getpDiscount());
			pstmt.setString(5, dto.getpImgname());
			pstmt.setString(6, dto.getpCode());
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
	
	// 상품 삭제
	public int productDelete(String pCode) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "DELETE PRODUCT WHERE pCODE=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pCode);
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
	
	// 상품 재고감소
	public int productStockDown(int qty, String pCode) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "UPDATE PRODUCT SET pSTOCK=pSTOCK-? WHERE pCODE=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qty);
			pstmt.setString(2, pCode);
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
	
	// 상품 재고증가
	public int productStockUp(int qty, String pCode) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "UPDATE PRODUCT SET pSTOCK=pSTOCK+? WHERE pCODE=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qty);
			pstmt.setString(2, pCode);
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
	
	// 주문번호로 상품코드 가져오기
	public String getpCode(int oNo) {
		String 			  pCode = null;
		Connection 		  conn  = null;
		PreparedStatement pstmt = null;
		ResultSet 		  rs    = null;
		String sql = "SELECT pCODE FROM ORDERDETAIL WHERE oNO=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oNo);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				pCode = rs.getString("pCode");
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
				if(rs != null) {
					rs.close();
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return pCode;
	}
	
	// 상품 총 개수
	public int productTotCnt() {
		int 			  result = 0;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs 	 = null;
		String sql = "SELECT COUNT(*) FROM PRODUCT";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs    = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
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
	
	// 검색 상품 이름 총 개수
	public int productSearchCnt(String pName) {
		int 			  result = 0;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs     = null;
		String sql = "SELECT COUNT(*) FROM PRODUCT WHERE pNAME LIKE '%'||?||'%'";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pName);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
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

	// 할인 상품 총 개수
	public int productDiscountCnt() {
		int 			  result = 0;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs     = null;
		String sql = "SELECT COUNT(*) FROM PRODUCT WHERE pDISCOUNT > 0";
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
	
	// 카테고리별 상품 총 개수
	public int productCategoriCnt(int pCategori) {
		int 			  result = 0;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs 	 = null;
		String sql = "SELECT COUNT(*) FROM PRODUCT WHERE pCATEGORI=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, pCategori);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
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
