package cetus.registration.model;

import java.io.File;
import java.util.Scanner;

public class Carvreplay {
	private int replayid;
	private int userid;
	//file path
	private String replayfilepath;
	//file content
	private String replayfilecontent;
	private String replayexpsection;
	private String replayexecutionresults;

	  
    
    
	public Carvreplay(int replayid, int userid, String replayfilepath, String replayfilecontent,
			String replayexpsection, String replayexecutionresults) {
		super();
		this.replayid = replayid;
		this.userid = userid;
		this.replayfilepath = replayfilepath;
		this.replayfilecontent = replayfilecontent;
		this.replayexpsection = replayexpsection;
		this.replayexecutionresults = replayexecutionresults;
	}



	public Carvreplay() {
		// TODO Auto-generated constructor stub
	}



	/**
	 * @return the replayid
	 */
	public int getReplayid() {
		return replayid;
	}

	/**
	 * @param replayid the replayid to set
	 */
	public void setReplayid(int replayid) {
		this.replayid = replayid;
	}
	/**
	 * @return the userid
	 */
	public int getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(int userid) {
		this.userid = userid;
	}
	/**
	 * @return the replayfilepath
	 */
	public String getReplayfilepath() {
		return replayfilepath;
	}
	/**
	 * @param replayfilepath the replayfilepath to set
	 */
	public void setReplayfilepath(String replayfilepath) {
		this.replayfilepath = replayfilepath;
	}
	/**
	 * @return the replayfilecontent
	 */
	public String getReplayfilecontent() {
		return replayfilecontent;
	}
	/**
	 * @param replayfilecontent the replayfilecontent to set
	 */
	public void setReplayfilecontent(String replayfilecontent) {
		this.replayfilecontent = replayfilecontent;
	}
	/**
	 * @return the replayexpsection
	 */
	public String getReplayexpsection() {
		return replayexpsection;
	}
	/**
	 * @param replayexpsection the replayexpsection to set
	 */
	public void setReplayexpsection(String replayexpsection) {
		this.replayexpsection = replayexpsection;
	}
	/**
	 * @return the replayexecutionresults
	 */
	public String getReplayexecutionresults() {
		return replayexecutionresults;
	}
	/**
	 * @param replayexecutionresults the replayexecutionresults to set
	 */
	public void setReplayexecutionresults(String replayexecutionresults) {
		this.replayexecutionresults = replayexecutionresults;
	}



}
