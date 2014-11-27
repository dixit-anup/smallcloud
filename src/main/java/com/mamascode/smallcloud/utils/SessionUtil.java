package com.mamascode.smallcloud.utils;

import javax.servlet.http.HttpSession;

public class SessionUtil {
	//////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	// constants
	
	public final static String loginStatusAttr = "login";
	public final static String loginUserNameAttr = "loginUserName";
	public final static String loginUserNoAttr = "loginUserNo";
	
	public final static String loginSuccess = "loginSuccess";
	public final static String loginFailed = "loginFailed";
	public final static String logoutStatus = "logoutStatus";
	
	//////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	// static methods
	
	// isLoginStatus
	public static boolean isLoginStatus(HttpSession session) {
		if(session != null && session.getAttribute(loginStatusAttr) != null && 
				session.getAttribute(loginStatusAttr).equals(loginSuccess))
			return true;
		
		return false;
	}
	
	//////////////////////////////////////////////////////////////////
		
	// setLogin
	public static void setLogin(HttpSession session, String userName) {
		session.setAttribute(loginStatusAttr, loginSuccess);
		session.setAttribute(loginUserNameAttr, userName);
	}
	
	// setLogin
	public static void setLogin(HttpSession session, int userNo) {
		session.setAttribute(loginStatusAttr, loginSuccess);
		session.setAttribute(loginUserNoAttr, userNo);
	}
	
	// setLogout
	public static void setLogout(HttpSession session) {
		session.removeAttribute(loginUserNameAttr);
		session.removeAttribute(loginUserNoAttr);
	}
	
	//////////////////////////////////////////////////////////////////
	
	// getLoginUserName
	public static String getLoginUserName(HttpSession session) {
		if(isLoginStatus(session))
			return (String) session.getAttribute(loginUserNameAttr);
		
		return "";
	}
	
	// isValidUser
	public static boolean isValidUser(HttpSession session, String userName) {
		boolean checkValidUser = false;
		
		if(SessionUtil.isLoginStatus(session)) {
			String loginUserName = getLoginUserName(session);
			
			if(userName != null && userName.equals(loginUserName)) {
				checkValidUser = true;
			}	
		}
		
		return checkValidUser;
	}
}
