
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.coprocessor.BaseMasterObserver;
import org.apache.hadoop.hbase.coprocessor.MasterCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.ipc.TransactionalRegionInterface;
import org.apache.hadoop.hbase.master.HMaster;

public class MasterObserverEx extends BaseMasterObserver{
        
        private static final Log LOG = LogFactory.getLog(MasterObserverEx.class.getName());
        private HConnection connection;
                
        @Override
        public void preMove(ObserverContext<MasterCoprocessorEnvironment> ctx,
            HRegionInfo region, ServerName srcServer, ServerName destServer)
        throws IOException {
                LOG.trace("preMove -- ENTRY -- region: " + region.toString());
                 
                HRegionInterface hri = null;
                
                if (this.connection == null)
                   this.connection = HConnectionManager.getConnection(ctx.getEnvironment().getConfiguration());

                try {
                    hri = this.connection.getHRegionConnection(srcServer.getHostname(),
                                                               srcServer.getPort());                
                } catch (Exception e) {
                    LOG.error("Error occurred while obtaining HRegionInterface");
                }
  
		/* 
		// Now we can call an HRegion call to determine whether or not we want to bypass the move
                if(!hri.isMoveable(region.getRegionName())) {
                    LOG.debug("Unable to balance region, transactions present in region: " +
                    region.toString());
                    ctx.bypass();
                }
		*/
                boolean move = true;
		if(move) {
			LOG.trace("Found no reason to bypass move");
		}
		else 
			ctx.bypass();

                LOG.trace("preMove -- EXIT -- region: " + region.toString()); 
        }
}
