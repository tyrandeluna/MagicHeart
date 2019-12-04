package entidades;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {
	@Expose(serialize = true)
	@SerializedName("RequestId")
	private int RequestId;
	@Expose(serialize = true)
	@SerializedName("objRef")
	private String objRef;
	@Expose(serialize = true)
	@SerializedName("method")
	private String method;
	@Expose(serialize = true)
	@SerializedName("args")
	private String args;
	
	
	
	public int getRequestId() {
		return RequestId;
	}
	public void setRequestId(int requestId) {
		RequestId = requestId;
	}
	public String getObjRef() {
		return objRef;
	}
	public void setObjRef(String objRef) {
		this.objRef = objRef;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getArgs() {
		return args;
	}
	public void setArgs(String args) {
		this.args = args;
	}
	
	public String toString() {
		return "ReqID: "+this.RequestId+" /Obj: "+this.objRef+" /Metodo: "+this.method+" /Args: "+this.args;
	}
}
