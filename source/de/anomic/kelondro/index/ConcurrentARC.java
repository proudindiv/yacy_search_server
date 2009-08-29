// ConcurrentARC.java
// a Simple Adaptive Replacement Cache
// (C) 2009 by Michael Peter Christen; mc@yacy.net, Frankfurt a. M., Germany
// first published 17.04.2009 on http://yacy.net
//
// $LastChangedDate: 2006-04-02 22:40:07 +0200 (So, 02 Apr 2006) $
// $LastChangedRevision: 1986 $
// $LastChangedBy: orbiter $
//
// LICENSE
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
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package de.anomic.kelondro.index;

/**
 * This is a simple cache using two generations of hashtables to store the content with a LFU strategy.
 * The Algorithm is described in a slightly more complex version as Adaptive Replacement Cache, "ARC".
 * For details see http://www.almaden.ibm.com/cs/people/dmodha/ARC.pdf
 * or http://en.wikipedia.org/wiki/Adaptive_Replacement_Cache
 * This version omits the ghost entry handling which is described in ARC, and keeps both cache levels
 * at the same size.
 */

public class ConcurrentARC<K, V> implements ARC<K, V> {

    protected int cacheSize;
    private int mask;
    private ARC<K, V> arc[];
    
    @SuppressWarnings("unchecked")
	public ConcurrentARC(final int cacheSize, int partitions) {
    	this.mask = 1;
    	while (this.mask < partitions) this.mask = this.mask * 2;
    	this.arc = new SimpleARC[mask];
    	for (int i = 0; i < this.arc.length; i++) this.arc[i] = new SimpleARC<K, V>(cacheSize / this.mask);
    	this.mask -= 1;
    }
    
    /**
     * put a value to the cache.
     * @param s
     * @param v
     */
    public void put(K s, V v) {
    	this.arc[s.hashCode() & mask].put(s, v);
    }
    
    /**
     * get a value from the cache.
     * @param s
     * @return the value
     */
    public V get(K s) {
    	return this.arc[s.hashCode() & mask].get(s);
    }
    
    /**
     * check if the map contains the key
     * @param s
     * @return
     */
    public boolean containsKey(K s) {
    	return this.arc[s.hashCode() & mask].containsKey(s);
    }
    
    /**
     * remove an entry from the cache
     * @param s
     * @return the old value
     */
    public V remove(K s) {
    	return this.arc[s.hashCode() & mask].remove(s);
    }
    
    /**
     * clear the cache
     */
    public void clear() {
    	for (ARC<K, V> a: this.arc) a.clear();
    }
}
