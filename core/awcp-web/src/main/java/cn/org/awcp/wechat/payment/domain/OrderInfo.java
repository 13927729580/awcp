package cn.org.awcp.wechat.payment.domain;

/**
 * 订单信息
 * @author Administrator
 *
 */
public class OrderInfo {
	private String outTradeNo;		//商品订单号
	private String body;			//商品描述	
	private String attach;			//附加数据
	private long totalFee; 			//标价金额(分)
	
	public OrderInfo(String outTradeNo, String body, String attach, long totalFee) {
		super();
		this.outTradeNo = outTradeNo;
		this.body = body;
		this.attach = attach;
		this.totalFee = totalFee;
	}

	public OrderInfo() {
		
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public long getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(long totalFee) {
		this.totalFee = totalFee;
	}

	@Override
	public String toString() {
		return "OrderInfo [outTradeNo=" + outTradeNo + ", body=" + body + ", attach=" + attach + ", totalFee="
				+ totalFee + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attach == null) ? 0 : attach.hashCode());
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((outTradeNo == null) ? 0 : outTradeNo.hashCode());
		result = prime * result + (int) (totalFee ^ (totalFee >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		OrderInfo other = (OrderInfo) obj;
		if (attach == null) {
			if (other.attach != null) {
                return false;
            }
		} else if (!attach.equals(other.attach)) {
            return false;
        }
		if (body == null) {
			if (other.body != null) {
                return false;
            }
		} else if (!body.equals(other.body)) {
            return false;
        }
		if (outTradeNo == null) {
			if (other.outTradeNo != null) {
                return false;
            }
		} else if (!outTradeNo.equals(other.outTradeNo)) {
            return false;
        }
		if (totalFee != other.totalFee) {
            return false;
        }
		return true;
	}
	
}
