package tmall.util;

public class Page {
	int start;
	int count;
	int total;
	String param;
	
	public Page(int start, int count) {
		this.start = start;
		this.count = count;
	}
	
	/*获取总页数*/
	public int getTotalPage() {
		int totalPage;
		if(0 == total % count) {
			totalPage = total / count;
		}else {
			totalPage = total / count + 1;
		}
		if(0 == totalPage) {
			totalPage = 1;
		}
		return totalPage;
	}

	/*获取最有一页开始的ID*/
	public int getLast() {
		int last;
		if(0 == total % count) {
			last = total - count;
		}else {
			last = total - total%count;
		}
		last = last<0?0:last;
		return last;
	}
	
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	
	
}
