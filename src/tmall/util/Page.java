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
	
	/*判断是否存在前一页，如果start == 0，即不存在前页，反之即存在前一页*/
	public boolean isHasPreviouse() {
		/*if(start == 0) {
			return false;
		}
		return true;*/
		
		return start == 0 ? false: true;
	}
	/*判断是否存在后一页，如start == getLast(),即不存在后一页，反之存在后一页*/
	public boolean isHasNext() {
		if(start == getLast()) {
			return false;
		}
		return true;
	}
	/*获取最后一页开始的ID*/
	public int getLast() {
		int last;
		
		if(0 == total % count) {
			last = total - count ;
		}else {
			last = total - total % count;
		}
		
		/*if(last < 0) {
			return 0;
		}
		return last;*/
		
		return last < 0 ? 0:last;
	}
	/*获取总页数*/
	public int getTotalPage() {
		int totalPage = 0;
		if(0 == total % count) {
			totalPage = total / count;
		}else {
			totalPage = total % count + 1;
		}
		
		return totalPage < 0 ? 0:totalPage;
		
	}
	
	
}
