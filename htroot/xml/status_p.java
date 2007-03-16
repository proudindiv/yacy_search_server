// /xml.queues/status_p.java
// -------------------------------
// part of the yacy
//
// (C) 2006 Alexander Schier
// last major change: 03.11.2006
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software Foundation, Inc., 
// 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
//
// Using this software in any meaning (reading, learning, copying, compiling,
// running) means that you agree that the Author(s) is (are) not responsible
// for cost, loss of data or any harm that may be caused directly or indirectly
// by usage of this softare or this documentation. The usage of this software
// is on your own risk. The installation and usage (starting/running) of this
// software may allow other people or application to access your computer and
// any attached devices and is highly dependent on the configuration of the
// software which must be done by the user of the software; the author(s) is
// (are) also not responsible for proper configuration and usage of the
// software, even if provoked by documentation provided together with
// the software.
//
// Any changes to this file according to the GPL as documented in the file
// gpl.txt aside this file in the shipment you received can be done to the
// lines that follows this copyright notice here, but changes must not be
// done inside the copyright notive above. A re-distribution must contain
// the intact and unchanged copyright notice.
// Contributions and changes to the program code must be marked as such.

package xml;
import de.anomic.http.httpHeader;
import de.anomic.plasma.plasmaSwitchboard;
import de.anomic.server.serverObjects;
import de.anomic.server.serverSwitch;
import de.anomic.server.serverMemory;
import de.anomic.yacy.yacyCore;
import de.anomic.yacy.yacySeed;
import de.anomic.http.httpdByteCountInputStream;
import de.anomic.http.httpdByteCountOutputStream;

public class status_p {
    
    
    public static serverObjects respond(httpHeader header, serverObjects post, serverSwitch env) {
        // return variable that accumulates replacements
        plasmaSwitchboard switchboard = (plasmaSwitchboard) env;
        serverObjects prop = new serverObjects();
        prop.put("rejected", 0);
        yacyCore.peerActions.updateMySeed();
        prop.put("ppm", yacyCore.seedDB.mySeed.get(yacySeed.ISPEED, "unknown"));
        prop.put("qpm", yacyCore.seedDB.mySeed.get(yacySeed.RSPEED, "unknown"));
        prop.put("wordCacheSize", switchboard.wordIndex.dhtOutCacheSize() + switchboard.wordIndex.dhtInCacheSize());
        prop.put("wordCacheWSize", switchboard.wordIndex.dhtOutCacheSize());
        prop.put("wordCacheKSize", switchboard.wordIndex.dhtInCacheSize());
        prop.put("wordCacheMaxCount", switchboard.getConfig("wordCacheMaxCount", "10000"));

		//
		// memory usage and system attributes
		final Runtime rt = Runtime.getRuntime();
        prop.put("freeMemory", rt.freeMemory());
        prop.put("totalMemory", rt.totalMemory());
        prop.put("maxMemory", serverMemory.max);
        prop.put("processors", rt.availableProcessors());

		// proxy traffic
		prop.put("trafficIn", httpdByteCountInputStream.getGlobalCount());
		prop.put("trafficProxy", httpdByteCountOutputStream.getAccountCount("PROXY"));
		prop.put("trafficCrawler", httpdByteCountInputStream.getAccountCount("CRAWLER"));

        // return rewrite properties
        return prop;
    }
    
}



