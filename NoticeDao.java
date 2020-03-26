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

import com.tj.Fruits.dto.NoticeDto;

public class NoticeDao {
	public static final int SUCCESS = 1;
	public static final int FAIL    = 0;
	
	private static NoticeDao instance;
	
	public static NoticeDao getInstance() {
		if(instance == null) {
			instance = new NoticeDao();
		}
		return instance;
	}
	
	private NoticeDao() {}
	
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
	
	// 공지사항 작성
	public int noticeWrite(NoticeDto dto) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "INSERT INTO NOTICE (nNO, aID, nCATEGORI, nTITLE, nCONTENT, nFILE)" + 
					 " VALUES (NOTICE_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getaId());
			pstmt.setString(2, dto.getnCategori());
			pstmt.setString(3, dto.getnTitle());
			pstmt.setString(4, dto.getnContent());
			pstmt.setString(5, dto.getnFile());
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
	
	// 공지사항 수정
	public int noticeModify(NoticeDto dto) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "UPDATE NOTICE SET nCATEGORI=?, nTITLE=?, nCONTENT=?, nFILE=?" + 
					 " WHERE nNO=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getnCategori());
			pstmt.setString(2, dto.getnTitle());
			pstmt.setString(3, dto.getnContent());
			pstmt.setString(4, dto.getnFile());
			pstmt.setInt(5, dto.getnNo());
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
	
	// 공지사항 삭제
	public int noticeDelete(int nNo) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "DELETE NOTICE WHERE nNO=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nNo);
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
	
	// 공지사항 리스트
	public ArrayList<NoticeDto> noticeList(int startRow, int endRow) {
		ArrayList<NoticeDto> notices = new ArrayList<NoticeDto>();
		Connection 			 conn    = null;
		PreparedStatement 	 pstmt   = null;
		ResultSet 			 rs 	 = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, N.* FROM (" + 
					 " SELECT * FROM NOTICE ORDER  BY nDATE DESC) N)" + 
					 " WHERE RN BETWEEN ? AND ?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int	   nNo 		 = rs.getInt("nNo");
				String aId 		 = rs.getString("aId");
				String nCategori = rs.getString("nCategori");
				String nTitle 	 = rs.getString("nTitle");
				String nContent  = rs.getString("nContent");
				String nFile 	 = rs.getString("nFile");
				Date   nDate 	 = rs.getDate("nDate");
				int	   nHit 	 = rs.getInt("nHit");
				notices.add(new NoticeDto(nNo, aId, nCategori, nTitle, nContent, nFile, nDate, nHit));
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
		return notices;
	}
	
	// 공지글 조회수 증가
	public void hitUp(int nNo) {
		Connection 		  conn  = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE NOTICE SET nHIT=nHIT+1 WHERE nNO=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nNo);
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
	
	// 공지글 불러오기
	public NoticeDto getNotice(int nNo) {
		hitUp(nNo);
		NoticeDto 		  notice = null;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs     = null;
		String sql = "SELECT * FROM NOTICE WHERE nNO=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nNo);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String aId 		 = rs.getString("aId");
				String nCategori = rs.getString("nCategori");
				String nTitle 	 = rs.getString("nTitle");
				String nContent  = rs.getString("nContent");
				String nFile 	 = rs.getString("nFile");
				Date   nDate 	 = rs.getDate("nDate");
				int    nHit 	 = rs.getInt("nHit");
				notice = new NoticeDto(nNo, aId, nCategori, nTitle, nContent, nFile, nDate, nHit);
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
		return notice;
	}
	
	// 수정전 불러오기
	public NoticeDto getModifyNotice(int nNo) {
		NoticeDto 		  notice = null;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs     = null;
		String sql = "SELECT * FROM NOTICE WHERE nNO=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, nNo);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String aId 		 = rs.getString("aId");
				String nCategori = rs.getString("nCategori");
				String nTitle 	 = rs.getString("nTitle");
				String nContent  = rs.getString("nContent");
				String nFile 	 = rs.getString("nFile");
				Date   nDate 	 = rs.getDate("nDate");
				int    nHit 	 = rs.getInt("nHit");
				notice = new NoticeDto(nNo, aId, nCategori, nTitle, nContent, nFile, nDate, nHit);
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
		return notice;
	}
	
	// 공지글 총 개수
	public int noticeTotCnt() {
		int 		      result = 0;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs 	 = null;
		String sql = "SELECT COUNT(*) FROM NOTICE";
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
