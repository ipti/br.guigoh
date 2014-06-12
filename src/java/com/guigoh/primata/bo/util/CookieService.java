/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.primata.bo.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ipti004
 */
public class CookieService {

    private static HttpServletRequest request;
    private static HttpServletResponse response;

    public static String getCookie(String name) {
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();
        String value = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase(name)) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }
    
    public static String getCookieByRequest(String name, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String value = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().trim().equalsIgnoreCase(name)) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }

    public static void eraseCookie() {
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (!cookies[i].getName().equals("locale")) {
                    cookies[i].setValue("");
                    cookies[i].setPath("/");
                    cookies[i].setMaxAge(0);
                    cookies[i].setDomain(".guigoh.com");
                    response.addCookie(cookies[i]);
                }
            }
        }
    }

    public static void addCookie(String key, String value) {
        response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Cookie cookie = new Cookie(key, value);
        cookie.setDomain(".guigoh.com");
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
    }
}