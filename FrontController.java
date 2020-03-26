package com.tj.Fruits.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tj.Fruits.service.AdminCustomerService;
import com.tj.Fruits.service.AdminDeleteService;
import com.tj.Fruits.service.AdminIdConfirm;
import com.tj.Fruits.service.AdminJoinService;
import com.tj.Fruits.service.AdminLoginService;
import com.tj.Fruits.service.AdminProductService;
import com.tj.Fruits.service.AdminPwConfirm;
import com.tj.Fruits.service.BoardContentService;
import com.tj.Fruits.service.BoardDeleteService;
import com.tj.Fruits.service.BoardListService;
import com.tj.Fruits.service.BoardModifyService;
import com.tj.Fruits.service.BoardModifyViewService;
import com.tj.Fruits.service.BoardReplyService;
import com.tj.Fruits.service.BoardReplyViewService;
import com.tj.Fruits.service.BoardWriteService;
import com.tj.Fruits.service.BoardWriteViewService;
import com.tj.Fruits.service.CustomerDeleteService;
import com.tj.Fruits.service.CustomerModifyService;
import com.tj.Fruits.service.CustomerViewService;
import com.tj.Fruits.service.IdConfirm;
import com.tj.Fruits.service.JoinService;
import com.tj.Fruits.service.LoginService;
import com.tj.Fruits.service.LogoutService;
import com.tj.Fruits.service.MainProductListService;
import com.tj.Fruits.service.NoticeContentService;
import com.tj.Fruits.service.NoticeDelete;
import com.tj.Fruits.service.NoticeListService;
import com.tj.Fruits.service.NoticeModifyService;
import com.tj.Fruits.service.NoticeModifyViewService;
import com.tj.Fruits.service.NoticeWriteService;
import com.tj.Fruits.service.OrderDeleteService;
import com.tj.Fruits.service.OrderListService;
import com.tj.Fruits.service.OrderService;
import com.tj.Fruits.service.OrderViewService;
import com.tj.Fruits.service.PcodeConfirm;
import com.tj.Fruits.service.ProductCategoriListService;
import com.tj.Fruits.service.ProductContentService;
import com.tj.Fruits.service.ProductDeleteService;
import com.tj.Fruits.service.ProductDiscountListService;
import com.tj.Fruits.service.ProductListService;
import com.tj.Fruits.service.ProductModifyService;
import com.tj.Fruits.service.ProductModifyViewService;
import com.tj.Fruits.service.ProductSearchService;
import com.tj.Fruits.service.ProductWriteService;
import com.tj.Fruits.service.PwConfirm;
import com.tj.Fruits.service.SearchIdService;
import com.tj.Fruits.service.SearchPwService;
import com.tj.Fruits.service.Service;

@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		actionDo(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		actionDo(request, response);
	}

	private void actionDo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		String path = request.getContextPath();
		String command = uri.substring(path.length());
		String viewPage = null;
		Service service = null;
		if(command.equals("/main.do")) {
			// 메인 화면 : MainProductListService.java
			service = new MainProductListService();
			service.execute(request, response);
			viewPage = "main/main.jsp";
		} else if(command.equals("/adminLoginView.do")) {
			// 관리자 로그인 화면
			viewPage = "admin/login.jsp";
		} else if(command.equals("/adminLogin.do")) {
			// 관리자 로그인 처리 : AdminLoginService.java
			service = new AdminLoginService();
			service.execute(request, response);
			viewPage = "adminMain.do";
		} else if(command.equals("/adminMain.do")) {
			// 관리자 메인화면 : AdminCustomerService.java
			service = new AdminCustomerService();
			service.execute(request, response);
			viewPage = "admin/main.jsp";
		} else if(command.equals("/adminIdConfirm.do")) {
			// 관리자 아이디 중복 확인 : AdminIdConfirm.java
			service = new AdminIdConfirm();
			service.execute(request, response);
			viewPage = "admin/idConfirm.jsp";
		} else if(command.equals("/adminPwConfirm.do")) {
			// 관리자 비밀번호 확인 : AdminPwConfirm.java
			service = new AdminPwConfirm();
			service.execute(request, response);
			viewPage = "admin/pwConfirm.jsp";
		} else if(command.equals("/adminJoin.do")) {
			// 관리자 계정 생성 처리 : AdminJoinService.java
			service = new AdminJoinService();
			service.execute(request, response);
			viewPage = "adminMain.do";
		} else if(command.equals("/adminDelete.do")) {
			// 관리자 계정 탈퇴 처리 : AdminDeleteService.java
			service = new AdminDeleteService();
			service.execute(request, response);
			viewPage = "main.do";
		} else if(command.equals("/loginView.do")) {
			// 로그인 화면
			viewPage = "customer/login.jsp";
		} else if(command.equals("/login.do")) {
			// 로그인 처리 : LoginService.java
			service = new LoginService();
			service.execute(request, response);
			viewPage = "main.do";
		} else if(command.equals("/idConfirm.do")) {
			// 아이디 중복확인 : IdConfirm.java
			service = new IdConfirm();
			service.execute(request, response);
			viewPage = "customer/idConfirm.jsp";
		} else if(command.equals("/pwConfirm.do")) {
			// 비밀번호 확인 : PwConfirm.java
			service = new PwConfirm();
			service.execute(request, response);
			viewPage = "customer/pwConfirm.jsp";
		} else if(command.equals("/join.do")) {
			// 회원가입 처리 : JoinService.java
			service = new JoinService();
			service.execute(request, response);
			viewPage = "loginView.do";
		} else if(command.equals("/searchIdPw.do")) {
			// 아이디/비밀번호 찾기 화면
			viewPage = "customer/searchIdPw.jsp";
		} else if(command.equals("/searchId.do")) {
			// 아이디 찾기 처리 : SearchIdService.java
			service = new SearchIdService();
			service.execute(request, response);
			viewPage = "customer/searchId.jsp";
		} else if(command.equals("/searchPw.do")) {
			// 비밀번호 찾기 처리 : SearchPwService.java
			service = new SearchPwService();
			service.execute(request, response);
			viewPage = "customer/searchPw.jsp";
		} else if(command.equals("/customerDeleteView.do")) {
			// 회원 탈퇴 화면
			viewPage = "customer/customerDelete.jsp";
		} else if(command.equals("/customerDelete.do")) {
			// 회원탈퇴 처리 : CustomerDeleteService.java
			service = new CustomerDeleteService();
			service.execute(request, response);
			viewPage = "main.do";
		} else if(command.equals("/logout.do")) {
			// 로그아웃 처리 : LogoutService.java
			service = new LogoutService();
			service.execute(request, response);
			viewPage = "main.do";
		} else if(command.equals("/mypage.do")) {
			// 마이페이지 화면
			viewPage = "orderList.do";
		} else if(command.equals("/customerView.do")) {
			// 정보수정전 불러오기 : CustomerViewService.java
			service = new CustomerViewService();
			service.execute(request, response);
			viewPage = "customer/customerView.jsp";
		} else if(command.equals("/customerModify.do")) {
			// 정보수정 페이지 : CustomerModifyService.java
			service = new CustomerModifyService();
			service.execute(request, response);
			viewPage = "mypage.do";
		} else if(command.equals("/pCodeConfirm.do")) {
			// 상품코드 중복 확인 : PcodeConfirm.java
			service = new PcodeConfirm();
			service.execute(request, response);
			viewPage = "product/pCodeConfirm.jsp";
		} else if(command.equals("/productWrite.do")) {
			// 상품 추가 처리 : ProductWriteService.java
			service = new ProductWriteService();
			service.execute(request, response);
			viewPage = "productControll.do";
		} else if(command.equals("/productContent.do")) {
			// 상품 상세보기 : ProductContentService.java
			service = new ProductContentService();
			service.execute(request, response);
			viewPage = "product/productContent.jsp";
		} else if(command.equals("/productModifyView.do")) {
			// 상품 정보수정 페이지 : ProductModifyViewService.java
			service = new ProductModifyViewService();
			service.execute(request, response);
			viewPage = "product/productModify.jsp";
		} else if(command.equals("/productModify.do")) {
			// 상품 정보수정 처리 : ProductModifyService.java
			service = new ProductModifyService();
			service.execute(request, response);
			viewPage = "productContent.do";
		} else if(command.equals("/productDelete.do")) {
			// 상품 삭제 : ProductDeleteService.java
			service = new ProductDeleteService();
			service.execute(request, response);
			viewPage = "adminMain.do";
		} else if(command.equals("/productList.do")) {
			// 상품 목록 보기 : ProductListService.java
			service = new ProductListService();
			service.execute(request, response);
			viewPage = "product/productList.jsp";
		} else if(command.equals("/productDiscountList.do")) {
			// 할인상품 목록 : ProductDiscountListService.java
			service = new ProductDiscountListService();
			service.execute(request, response);
			viewPage = "product/productDiscountList.jsp";
		} else if(command.equals("/productCategoriList.do")) {
			// 카테고리별 상품목록 : ProductCategoriListService.java
			service = new ProductCategoriListService();
			service.execute(request, response);
			viewPage = "product/productCategoriList.jsp";
		} else if(command.equals("/productSearch.do")) {
			// 상품 검색 : ProductSearchService.java
			service = new ProductSearchService();
			service.execute(request, response);
			viewPage = "product/productSearchList.jsp";
		} else if(command.equals("/productControll.do")) {
			// 관리자 상품 관리 화면 : AdminProductService.java
			service = new AdminProductService();
			service.execute(request, response);
			viewPage = "admin/product.jsp";
		} else if(command.equals("/noticeList.do")) {
			// 공지사항 리스트 화면 : NoticeListService.java
			service = new NoticeListService();
			service.execute(request, response);
			viewPage = "notice/noticeList.jsp";
		} else if(command.equals("/noticeWriteView.do")) {
			// 공지사항 작성 화면
			viewPage = "notice/noticeWrite.jsp";
		} else if(command.equals("/noticeWrite.do")) {
			// 공지사항 작성 처리 : NoticeWriteService.java
			service = new NoticeWriteService();
			service.execute(request, response);
			viewPage = "noticeList.do";
		} else if(command.equals("/noticeModifyView.do")) {
			// 공지사항 글 수정 페이지 : NoticeModifyViewService.java
			service = new NoticeModifyViewService();
			service.execute(request, response);
			viewPage = "notice/noticeModify.jsp";
		} else if(command.equals("/noticeModify.do")) {
			// 공지사항 글 수정 처리 : NoticeModifyService.java
			service = new NoticeModifyService();
			service.execute(request, response);
			viewPage = "noticeList.do";
		} else if(command.equals("/noticeContent.do")) {
			// 공지 글 상세보기 : NoticeContentService.java
			service = new NoticeContentService();
			service.execute(request, response);
			viewPage = "notice/noticeContent.jsp";
		} else if(command.equals("/noticeDelete.do")) {
			// 공지 삭제 처리 : NoticeDelete.java
			service = new NoticeDelete();
			service.execute(request, response);
			viewPage = "noticeList.do";
		} else if(command.equals("/boardList.do")) {
			// 후기글 목록 화면 : BoardListService.java
			service = new BoardListService();
			service.execute(request, response);
			viewPage = "board/boardList.jsp";
		} else if(command.equals("/boardContent.do")) {
			// 후기 상세보기 : BoardContentService.java
			service = new BoardContentService();
			service.execute(request, response);
			viewPage = "board/boardContent.jsp";
		} else if(command.equals("/boardModifyView.do")) {
			// 후기 수정 화면 : BoardModifyViewService.java
			service = new BoardModifyViewService();
			service.execute(request, response);
			viewPage = "board/boardModify.jsp";
		} else if(command.equals("/boardModify.do")) {
			// 후기 수정 처리 : BoardModifyService.java
			service = new BoardModifyService();
			service.execute(request, response);
			viewPage = "productContent.do";
		} else if(command.equals("/boardWriteView.do")) {
			// 후기 글 작성 화면 : BoardWriteViewService.java
			service = new BoardWriteViewService();
			service.execute(request, response);
			viewPage = "board/boardWrite.jsp";
		} else if(command.equals("/boardWrite.do")) {
			// 후기 글 작성 처리 : BoardWriteService.java
			service = new BoardWriteService();
			service.execute(request, response);
			viewPage = "productContent.do";
		} else if(command.equals("/boardDelete.do")) {
			// 후기 삭제 처리 : BoardDeleteService.java
			service = new BoardDeleteService();
			service.execute(request, response);
			viewPage = "productContent.do";
		} else if(command.equals("/boardReplyView.do")) {
			// 후기 답변 화면 : BoardReplyViewService.java
			service = new BoardReplyViewService();
			service.execute(request, response);
			viewPage ="board/boardReply.jsp";
		} else if(command.equals("/boardReply.do")) {
			// 후기 답변 처리 : BoardReplyService.java
			service = new BoardReplyService();
			service.execute(request, response);
			viewPage = "productContent.do";
		} else if(command.equals("/orderList.do")) {
			// 주문목록 화면 : OrderListService.java
			service = new OrderListService();
			service.execute(request, response);
			viewPage = "customer/myPage.jsp";
		} else if(command.equals("/orderView.do")) {
			// 구매 페이지 : OrderViewService.java
			service = new OrderViewService();
			service.execute(request, response);
			viewPage = "order/orderView.jsp";
		} else if(command.equals("/order.do")) {
			// 구매 처리 : OrderService.java
			service = new OrderService();
			service.execute(request, response);
			viewPage = "main.do";
		} else if(command.equals("/orderDelete.do")) {
			// 주문 취소 : OrderDeleteService.java
			service = new OrderDeleteService();
			service.execute(request, response);
			viewPage = "orderList.do";
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
		dispatcher.forward(request, response);
	}

}
