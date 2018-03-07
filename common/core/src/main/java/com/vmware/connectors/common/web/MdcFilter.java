/*
 * Copyright © 2017 VMware, Inc. All Rights Reserved.
 * SPDX-License-Identifier: BSD-2-Clause
 */

package com.vmware.connectors.common.web;

/**
 * Created by Rob Worsnop on 9/6/17.
 */
public class MdcFilter {
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        //NOPMD
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
//        try {
//            String requestId = HttpServletRequest.class.cast(servletRequest).getHeader("X-Request-Id");
//            if (requestId != null) {
//                MDC.put("requestId", requestId);
//            }
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication != null && authentication.getPrincipal() != null) {
//                MDC.put("principal", authentication.getPrincipal().toString());
//            }
//            chain.doFilter(servletRequest, servletResponse);
//        } finally {
//            MDC.clear();
//        }
//    }
//
//    @Override
//    public void destroy() {
//        //NOPMD
//    }
}
