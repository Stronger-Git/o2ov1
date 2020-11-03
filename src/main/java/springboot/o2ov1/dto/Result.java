package springboot.o2ov1.dto;
// 封装json对象 返回结果可以使用它代理Map<String, Object>
public class Result<T> {
    private boolean success;
    private T data;//成功返回时的数据
    private String errorMsg;
    private int errorCode;

    public Result() {
    }
    // 成功时的构造器
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
    // 失败时的构造器
    public Result(boolean success, int errorCode, String errorMsg) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
