import java.util.Collection;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.ipc.HMasterInterface;

/*
 *   Sample application to get Region information from the client side
 *   First steps were to get hbaseadmin and hmaster to get cluster information
 *   With the region server names, we can get online regions and use 
 *   HRegionInterface to communicate with region to gather information
 */
public class RSInfo {
  
	Configuration     config;
	HBaseAdmin        hbadmin;
	HMasterInterface  hmaster; 
	
	
	/*  Need logging?
	    void setupLog4j() {
           System.out.println("In setupLog4J");
           String confFile = System.getenv("MY_SQROOT")
               + "/logs/log4j.dtm.config";
            PropertyConfigurator.configure(confFile);
        }

	*/
	
	public RSInfo() {
		init();
	}
	
	public void init() {
		//setupLog4j();		
                //TODO: see if zk output can be suppressed, makes output cleaner
		config = HBaseConfiguration.create();	
		try {
			hbadmin = new HBaseAdmin(config);
			hmaster = hbadmin.getMaster();
		} catch(Exception e) {
			System.out.println("ERROR: Unable to obtain HBase accessors, Exiting");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public static void printHelp() {
		System.out.println("Commands to gather HBase Region information:");
		System.out.println("    -l       list RegionServers");
	}
	
	public void getRegionServers() {
		Collection<ServerName> sn = hmaster.getClusterStatus().getServers();
		for(ServerName name : sn) {
			System.out.println(name.toString());
		}
	}
	
	public static void main(String[] args) {
		
		if(args.length == 0) {
			RSInfo.printHelp();
			System.exit(0);
		}

		RSInfo ri = new RSInfo();
		
		if(args[0].equals("-l")) {
			ri.getRegionServers();
		}		
	}
}
