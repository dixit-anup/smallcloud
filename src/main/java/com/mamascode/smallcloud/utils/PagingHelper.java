package com.mamascode.smallcloud.utils;

public class PagingHelper {
	///////////// fields
	private int startPage;
	private int endPage;
	private int totalPage;
	private int curPage;
	private int numberOfPagesDisplyed;
	
	///////////// constructor
	public PagingHelper(int totalPage, int curPage, int numberOfPagesDisplyed) {
		this.totalPage = totalPage;
		this.curPage = curPage;
		this.numberOfPagesDisplyed = numberOfPagesDisplyed;
		
		// calculate a start page and a end page
		calculateStartAndEndPage();
	}
	
	///////////// getters and setters
	public int getStartPage() {
		return startPage;
	}
	
	public int getEndPage() {
		return endPage;
	}
	
	public int getNumberOfPagesDisplyed() {
		return numberOfPagesDisplyed;
	}
	
	public void setNumberOfPagesDisplyed(int numberOfPagesDisplyed) {
		this.numberOfPagesDisplyed = numberOfPagesDisplyed;
	}
	
	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	///////////// internal calculation methods
	/* calculateStartAndEndPage: calculate a start page and a end page */
	private void calculateStartAndEndPage() {
		int startBlock = (curPage - 1) / numberOfPagesDisplyed + 1;
		startPage = (startBlock - 1) * numberOfPagesDisplyed + 1;
		endPage = startPage + numberOfPagesDisplyed - 1;
		
		if(endPage > totalPage)
			endPage = totalPage;
		
		if(curPage > totalPage)
			curPage = totalPage;
	}
}
