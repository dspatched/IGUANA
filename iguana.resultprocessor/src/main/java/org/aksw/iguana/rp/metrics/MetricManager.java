/**
 * 
 */
package org.aksw.iguana.rp.metrics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * The MetricManager will manage all {@link org.aksw.iguana.rp.metrics.Metric}
 * 
 * @author f.conrads
 *
 */
public class MetricManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MetricManager.class);
	
	private Set<Metric> metrics = new HashSet<Metric>();
	
	/**
	 * WIll add a metric to the manager
	 * @param metric
	 */
	public void addMetric(Metric metric){
		if(metric==null){
			return;
		}
		metrics.add(metric);
	}
	
	public Set<Metric> getMetrics(){
		return metrics;
	}
	
	/**
	 * Will add the meta Data to all metrics
	 * @param metaData
	 */
	public void addMetaData(Properties metaData){
		for(Metric m : metrics){
			m.setMetaData(metaData);
		}
	}
	/**
	 * This will message the received properties to all defined metrics.
	 * 
	 * @param p
	 */
	public void receiveData(Properties p){
		Set<Metric> remove = new HashSet<Metric>();
		for(Metric  m : metrics){
			try{
				m.receiveData(p);
			}catch(Exception e){
				LOGGER.warn("Could not use metric "+m.getShortName()+", Cause: "+e);
				remove.add(m);
			}
		}
		metrics.removeAll(remove);
	}
	
	@Override 
	public String toString(){
		StringBuilder ret =new StringBuilder();
			
		Iterator<Metric> it = metrics.iterator();
		for(int i=0;i<metrics.size()-1;i++){
			
			ret.append(it.next().getShortName()).append(", ");
		}
		ret.append(it.next().getShortName());
		return ret.toString();
	}
	
	/**
	 * Will close all metrics
	 */
	public void close(){
		Set<Metric> remove = new HashSet<Metric>();
		for(Metric m : metrics){
			try{
				m.close();
				m.getStorageManager().commit();
				
			}catch(Exception e){
				LOGGER.warn("Could not use metric "+m.getShortName()+", Cause: "+e);
				//remove.add(m);
				e.printStackTrace();
			}
		}
		metrics.removeAll(remove);
	}
	
}
