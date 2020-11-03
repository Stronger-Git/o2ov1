package springboot.o2ov1.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil{
    public static boolean checkVerifyCode(HttpServletRequest request) {
        String verifyCodeExpected = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String verifyCodeActually = HttpServletRequestUtil.getString(request, "verifyCode");
        if (verifyCodeExpected != null && verifyCodeExpected.equalsIgnoreCase(verifyCodeActually)) {
            return true;
        } else {
            return false;
        }
    }
}
