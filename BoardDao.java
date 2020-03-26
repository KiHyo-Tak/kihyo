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

import com.tj.Fruits.dto.BoardDto;

public class BoardDao {
	public static final int SUCCESS = 1;
	public static final int FAIL    = 0;
	
	private static BoardDao instance;
	
	public static BoardDao getInstance() {
		if(instance == null) {
			instance = new BoardDao();
		}
		return instance;
	}
	
	private BoardDao() {}
	
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
	
	// 후기 목록 리스트
	public ArrayList<BoardDto> boardList(String pCode, int startRow, int endRow) {
		ArrayList<BoardDto> boards = new ArrayList<BoardDto>();
		Connection 			conn   = null;
		PreparedStatement 	pstmt  = null;
		ResultSet 			rs 	   = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, B.* FROM (" + 
					 " SELECT * FROM BOARD ORDER BY bGROUP DESC, bSTEP) B) " + 
					 " WHERE pCODE=? AND RN BETWEEN ? AND ?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pCode);
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				int    bNo 		= rs.getInt("bNo");
				String cId 		= rs.getString("cId");
				String bTitle 	= rs.getString("bTitle");
				String bContent = rs.getString("bContent");
				String bFile 	= rs.getString("bFile");
				Date   bDate 	= rs.getDate("bDate");
				int    bHit 	= rs.getInt("bHit");
				int    bGroup 	= rs.getInt("bGroup");
				int    bStep 	= rs.getInt("bStep");
				int    bIndent 	= rs.getInt("bIndent");
				String aId 		= rs.getString("aId");
				String cName 	= rs.getString("cName");
				String aName 	= rs.getString("aName");
				boards.add(new BoardDto(bNo, cId, bTitle, bContent, bFile, bDate, bHit, bGroup, bStep, bIndent, aId, cName, aName, pCode));
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
		return boards;
	}
	
	// 후기 작성
	public int boardWrite(BoardDto dto) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "INSERT INTO BOARD (bNO, cID, bTITLE, bCONTENT, bFILE, bGROUP, bSTEP, bINDENT, cNAME, pCODE)" + 
					 " VALUES (BOARD_SEQ.NEXTVAL, ?, ?, ?, ?, BOARD_SEQ.CURRVAL, 0, 0, ?, ?)";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getcId());
			pstmt.setString(2, dto.getbTitle());
			pstmt.setString(3, dto.getbContent());
			pstmt.setString(4, dto.getbFile());
			pstmt.setString(5, dto.getcName());
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
	
	// 글 총 개수
	public int boardTotCnt(String pCode) {
		int 			  result = 0;
		Connection 		  conn 	 = null;
		PreparedStatement pstmt  = null;
		ResultSet 		  rs 	 = null;
		String sql = "SELECT COUNT(*) FROM BOARD WHERE pCODE=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pCode);
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
	
	// 글 조회수 올리기
	public void hitUp(int bNo) {
		Connection 		  conn  = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE BOARD SET bHIT=bHIT+1 WHERE bNO=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bNo);
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
	
	// 글 불러오기
	public BoardDto getBoard(int bNo) {
		hitUp(bNo);
		BoardDto 		  board = null;
		Connection 		  conn  = null;
		PreparedStatement pstmt = null;
		ResultSet 		  rs    = null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, B.* FROM (" + 
					 " SELECT * FROM BOARD ORDER BY bGROUP DESC, bSTEP) B)" + 
					 " WHERE bNO=?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bNo);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String cId 		= rs.getString("cId");
				String bTitle 	= rs.getString("bTitle");
				String bContent = rs.getString("bContent");
				String bFile 	= rs.getString("bFile");
				Date   bDate 	= rs.getDate("bDate");
				int    bHit 	= rs.getInt("bHit");
				int    bGroup 	= rs.getInt("bGroup");
				int    bStep 	= rs.getInt("bStep");
				int    bIndent 	= rs.getInt("bIndent");
				String aId 		= rs.getString("aId");
				String cName 	= rs.getString("cName");
				String aName 	= rs.getString("aName");
				String pCode 	= rs.getString("pCode");
				board = new BoardDto(bNo, cId, bTitle, bContent, bFile, bDate, bHit, bGroup, bStep, bIndent, aId, cName, aName, pCode);
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
		return board;
	}
	
	// 수정, 답글 전 불러오기
	public BoardDto getModify_ReplyBoard(int bNo) {
		BoardDto 		  board = null;
		Connection 		  conn 	= null;
		PreparedStatement pstmt = null;
		ResultSet 		  rs 	= null;
		String sql = "SELECT * FROM (SELECT ROWNUM RN, B.* FROM (" + 
					 " SELECT * FROM BOARD ORDER BY bGROUP DESC, bSTEP) B)" + 
					 " WHERE bNO=?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bNo);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String cId 		= rs.getString("cId");
				String bTitle 	= rs.getString("bTitle");
				String bContent = rs.getString("bContent");
				String bFile 	= rs.getString("bFile");
				Date   bDate 	= rs.getDate("bDate");
				int    bHit 	= rs.getInt("bHit");
				int    bGroup 	= rs.getInt("bGroup");
				int    bStep 	= rs.getInt("bStep");
				int    bIndent 	= rs.getInt("bIndent");
				String aId 		= rs.getString("aId");
				String cName 	= rs.getString("cName");
				String aName 	= rs.getString("aName");
				String pCode 	= rs.getString("pCode");
				board = new BoardDto(bNo, cId, bTitle, bContent, bFile, bDate, bHit, bGroup, bStep, bIndent, aId, cName, aName, pCode);
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
		return board;
	}
	
	// 글 수정
	public int boardModify(BoardDto dto) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "UPDATE BOARD SET bTITLE=?, bCONTENT=?, bFILE=? WHERE bNO=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getbTitle());
			pstmt.setString(2, dto.getbContent());
			pstmt.setString(3, dto.getbFile());
			pstmt.setInt(4, dto.getbNo());
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
	
	// 글 삭제
	public int boardDelete(int bNo) {
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "DELETE BOARD WHERE bNO=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bNo);
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
	
	// 답변 전 stepA
	public void stepA(int bGroup) {
		Connection 		  conn  = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE BOARD SET bSTEP=bSTEP+1 WHERE bSTEP>0 AND bGROUP=?";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bGroup);
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
	
	// 답글달기
	public int boardReply(BoardDto dto) {
		stepA(dto.getbGroup());
		int 			  result = FAIL;
		Connection 		  conn   = null;
		PreparedStatement pstmt  = null;
		String sql = "INSERT INTO BOARD (bNO, bTITLE, bCONTENT, bFILE, bGROUP, bSTEP, bINDENT, aID, aNAME, pCODE)" + 
					 " VALUES (BOARD_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			conn  = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getbTitle());
			pstmt.setString(2, dto.getbContent());
			pstmt.setString(3, dto.getbFile());
			pstmt.setInt(4, dto.getbGroup());
			pstmt.setInt(5, dto.getbStep()+1);
			pstmt.setInt(6, dto.getbIndent()+1);
			pstmt.setString(7, dto.getaId());
			pstmt.setString(8, dto.getaName());
			pstmt.setString(9, dto.getpCode());
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
