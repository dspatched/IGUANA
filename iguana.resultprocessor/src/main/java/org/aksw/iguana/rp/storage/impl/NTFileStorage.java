/**
 * 
 */
package org.aksw.iguana.rp.storage.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Properties;

import org.aksw.iguana.rp.config.CONSTANTS;
import org.aksw.iguana.rp.storage.TripleBasedStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Will save results as NTriple File
 * 
 * @author f.conrads
 *
 */
public class NTFileStorage extends TripleBasedStorage {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NTFileStorage.class);
	
	private StringBuilder file;
	
	/**
	 * 
	 */
	public NTFileStorage() {
		Calendar now = Calendar.getInstance();
		
		this.file = new StringBuilder();
		file.append("results_").append(now.get(Calendar.DAY_OF_MONTH)).append("-")
			.append(now.get(Calendar.MONTH)).append("-")
			.append(now.get(Calendar.YEAR)).append("_")
			.append(now.get(Calendar.HOUR_OF_DAY)).append("-")
			.append(now.get(Calendar.MINUTE)).append(".nt");
	}

	public NTFileStorage(String fileName){
		this.file = new StringBuilder(fileName);
	}
	
	/* (non-Javadoc)
	 * @see org.aksw.iguana.rp.storage.Storage#commit()
	 */
	@Override
	public void commit() {
		try(PrintWriter pw = new PrintWriter(
				new FileOutputStream(file.toString(), true))){
			pw.println(blockUpdate);
			blockUpdate = new StringBuilder();
			pw.flush();
		} catch (FileNotFoundException e) {
			LOGGER.error("Could not commit to NTFileStorage.", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.aksw.iguana.rp.storage.Storage#getStorageInfo()
	 */
	@Override
	public Properties getStorageInfo() {
		File f = new File(file.toString());
		Properties ret = new Properties();
		ret.setProperty(CONSTANTS.STORAGE_FILE,f.getAbsolutePath());
		return ret;
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}

}
