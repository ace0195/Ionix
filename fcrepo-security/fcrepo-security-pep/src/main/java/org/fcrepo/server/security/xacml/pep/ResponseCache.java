
package org.fcrepo.server.security.xacml.pep;

/**
 * @author nishen@melcoe.mq.edu.au
 */
public interface ResponseCache {

    /**
     * Adds an item to the cache.
     * 
     * @param request
     *        the request to hash and add.
     * @param response
     *        the resposne to add
     */
    public void addCacheItem(String request, String response);
    
    public void setTTL(long ttl);

    /**
     * @param request
     *        the request to hash and retrieve
     * @return the response corresponding to the request hash
     */
    public String getCacheItem(String request);

    /**
     * Invalidates the cache.
     */
    public void invalidate();
}
