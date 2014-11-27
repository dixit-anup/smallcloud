package com.mamascode.smallcloud.utils;

import java.util.ArrayList;
import java.util.List;

public class ListHelper<T> {
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	// fileds
	
	private int totalCount;
	private int curPageNumber;
	private int totalPageCount;
	private int objectPerPage;
	private int limit;
	private int offset;
	
	private List<T> list;
	
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	// constructors
	
	public ListHelper(int totalCount, 
			int curPageNumber, int objectPerPage) {
		this.totalCount = totalCount;
		this.curPageNumber = curPageNumber;
		this.objectPerPage = objectPerPage;
		this.limit = objectPerPage;
		
		calculateTotalPage();
		calculateOffset();
		
		list = new ArrayList<T>();
	}
	
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	// getters and setters

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getCurPageNumber() {
		return curPageNumber;
	}

	public void setCurPageNumber(int curPageNumber) {
		this.curPageNumber = curPageNumber;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public int getObjectPerPage() {
		return objectPerPage;
	}

	public void setObjectPerPage(int objectPerPage) {
		this.objectPerPage = objectPerPage;
	}
	
	public int getLimit() {
		return limit;
	}

	public boolean isEmpty() {
		return getTotalCount() == 0;
	}
	
	public int getOffset() {
		return offset;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}	
	
	////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////
	// calculation methods
	
	public void calculateTotalPage() {
		
		totalPageCount = 1;
		
		if(totalCount != 0) {
			totalPageCount = totalCount / objectPerPage;
			
			if(totalCount % objectPerPage != 0)
				totalPageCount++;
		}
		
		curPageNumber = (int) Validation.ProcrustesBed(curPageNumber, 1, totalPageCount);
	}
	
	public void calculateOffset() {
		offset = (curPageNumber - 1) * objectPerPage;
	}
}
