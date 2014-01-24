package com.db;

public class Page {
	private long startPointer = 0;
	private int startRecordId = 0;
	private int endRecordId = 0;
	private long globalPageNo = 0;
	private int internalPageNo = 0;
	//private int frameNo = -1;
	
	private boolean isInMemory = false;

	public boolean isInMemory() {
		return isInMemory;
	}
	public void setInMemory(boolean isInMemory) {
		this.isInMemory = isInMemory;
	}
	public int getInternalPageNo() {
		return internalPageNo;
	}
	public void setInternalPageNo(int internalPageNo) {
		this.internalPageNo = internalPageNo;
	}
	public long getStartPointer() {
		return startPointer;
	}
	public void setStartPointer(long startPointer) {
		this.startPointer = startPointer;
	}
	public int getStartRecordId() {
		return startRecordId;
	}
	public void setStartRecordId(int startRecordId) {
		this.startRecordId = startRecordId;
	}
	public int getEndRecordId() {
		return endRecordId;
	}
	public void setEndRecordId(int endRecordId) {
		this.endRecordId = endRecordId;
	}
	public long getGlobalPageNo() {
		return globalPageNo;
	}
	public void setGlobalPageNo(long globalPageNo2) {
		this.globalPageNo = globalPageNo2;
	}
	/*public int getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(int frameNo) {
		this.frameNo = frameNo;
	}*/
	
	@Override
	public String toString(){
		return "GlobalPage#: " + globalPageNo + " Internal page ID : " + internalPageNo +"\tStart Record#: "+startRecordId+"\tEnd Record#: " + endRecordId + "\tStart Pointer: "+startPointer;
	}
	
}
