package springboot.o2ov1.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {
    public static int getInt(HttpServletRequest request, String key) {
        try {
            return Integer.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }
    public static long getLong(HttpServletRequest request, String key) {
        try {
            return Long.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getString(HttpServletRequest request, String key) {
        try {
            String result = request.getParameter(key);
            if (result != null) {
                result = result.trim();
            }
            if ("".equals(result)) {
                result = null;
            }
            return result;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static boolean getBoolean(HttpServletRequest request, String key) {
        String status = request.getParameter(key);
        if (status != null)
            return Boolean.parseBoolean(status);
        else
            return false;
    }

}
