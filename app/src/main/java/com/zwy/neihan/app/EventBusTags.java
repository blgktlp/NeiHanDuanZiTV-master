package com.zwy.neihan.app;

/**
 * Created by jess on 8/30/16 16:39
 * Contact with jess.yan.effort@gmail.com
 */
public interface EventBusTags {
    /*取二维码/条形码扫描结果*/
    public static final String SCAN_QR_CODE_RESULT = "scan_qr_code_result";
    /*首页的刷新按钮被点击时发出事件 子tab接收到判断页面在前端展示时刷新*/
    public static final String HOME_TAB_REFRESH = "home_tab_refresh";
}
