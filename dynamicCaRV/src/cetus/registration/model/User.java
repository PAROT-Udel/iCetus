/**
 * 
 */
package cetus.registration.model;

import java.io.File;
import java.util.Scanner;

/**
 * @author parinaz
 *
 */
public class User {

	private String inputCode;
	private String cetusOptionSet;
	private String cetusOutput;
//	file path
	private String cetusDebuggerReport;
	private String cetusAnalysisReport;
	private String fileOutput;
//	file content
	private String inputContent;
	private String cetusOutputContent;
	private String cetusPassesContent;
	private String cetusAnalysisConetent;
	private String experimentalSection; //for Cetus table
	private int FkId; //for Cetus table
	
	
	public int getFkId() {
		return FkId;
	}
	public void setFkId(int fkId) {
		FkId = fkId;
	}
	public String getExperimentalSection() {
		return experimentalSection;
	}
	public void setExperimentalSection(String experimentalSection) {
		this.experimentalSection = experimentalSection;
	}
	/**
	 * @return the inputContent
	 */
	public String getInputContent() {
		return inputContent;
	}
	/**
	 * @param inputContent the inputContent to set
	 */
	public void setInputContent(String inputContent) {
		this.inputContent = inputContent;
	}
	/**
	 * @return the cetusOutputContent
	 */
	public String getCetusOutputContent() {
		return cetusOutputContent;
	}
	/**
	 * @param cetusOutputContent the cetusOutputContent to set
	 */
	public void setCetusOutputContent(String cetusOutputContent) {
		this.cetusOutputContent = cetusOutputContent;
	}
	/**
	 * @return the cetusPassesContent
	 */
	public String getCetusPassesContent() {
		return cetusPassesContent;
	}
	/**
	 * @param cetusPassesContent the cetusPassesContent to set
	 */
	public void setCetusPassesContent(String cetusPassesContent) {
		this.cetusPassesContent = cetusPassesContent;
	}
	/**
	 * @return the cetusAnalysisConetent
	 */
	public String getCetusAnalysisConetent() {
		return cetusAnalysisConetent;
	}
	/**
	 * @param cetusAnalysisConetent the cetusAnalysisConetent to set
	 */
	public void setCetusAnalysisConetent(String cetusAnalysisConetent) {
		this.cetusAnalysisConetent = cetusAnalysisConetent;
	}
	/**
	 * @return the cetusAnalysisReport
	 */
	public String getCetusAnalysisReport() {
		return cetusAnalysisReport;
	}
	/**
	 * @param cetusAnalysisReport the cetusAnalysisReport to set
	 */
	public void setCetusAnalysisReport(String cetusAnalysisReport) {
		this.cetusAnalysisReport = cetusAnalysisReport;
	}

	/**
	 * @return the fileOutput
	 */
	public String getFileOutput(String path) {
		display(path);
		return fileOutput;
	}
	/**
	 * @param fileOutput the fileOutput to set
	 */
	public void setFileOutput(String fileOutputpath) {
		this.fileOutput = fileOutputpath;
	}
	/**
	 * @return the inputCode
	 */
	public String getInputCode() {
		return inputCode;
	}
	/**
	 * @param inputCode the inputCode to set
	 */
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}
	/**
	 * @return the cetusOptionSet
	 */
	public String getCetusOptionSet() {
		return cetusOptionSet;
	}
	/**
	 * @param cetusOptionSet the cetusOptionSet to set
	 */
	public void setCetusOptionSet(String cetusOptionSet) {
		this.cetusOptionSet = cetusOptionSet;
	}
	/**
	 * @return the cetusOutput
	 */
	public String getCetusOutput() {
		return cetusOutput;
	}
	/**
	 * @param cetusOutput the cetusOutput to set
	 */
	public void setCetusOutput(String cetusOutput) {
		this.cetusOutput = cetusOutput;
	}
	/**
	 * @return the cetusDebuggerReport
	 */
	public String getCetusDebuggerReport() {
		return cetusDebuggerReport;
	}
	/**
	 * @param cetusDebuggerReport the cetusDebuggerReport to set
	 */
	public void setCetusDebuggerReport(String cetusDebuggerReport) {
		this.cetusDebuggerReport = cetusDebuggerReport;
	}
	
	public void display(String path) {
        Scanner scan = null;
        String s2 = path;
        StringBuffer buffer;
        try {
            buffer = new StringBuffer();
 
            scan = new Scanner(new File(path), "UTF-8");
 
            String readdata = "";
            while (scan.hasNext() && (readdata = scan.nextLine()) != null) {
 
                buffer.append(readdata).append('\n');
            }
            this.fileOutput = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(scan != null) {
                scan.close();
                scan = null;
            }
        }
    }

}
