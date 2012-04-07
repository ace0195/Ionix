/* The contents of this file are subject to the license and copyright terms
 * detailed in the license directory at the root of the source tree (also
 * available online at http://fedora-commons.org/license/).
 */
package org.fcrepo.server.storage.types;

import java.io.InputStream;

import org.fcrepo.server.Context;
import org.fcrepo.server.Server;
import org.fcrepo.server.errors.InitializationException;
import org.fcrepo.server.errors.ServerException;
import org.fcrepo.server.errors.StreamIOException;
import org.fcrepo.server.storage.ContentManagerParams;
import org.fcrepo.server.storage.ExternalContentManager;




/**
 * Referenced Content.
 *
 * @author Chris Wilper
 * @version $Id$
 */
public class DatastreamReferencedContent
        extends Datastream {

    private ExternalContentManager m_ecm;

    public DatastreamReferencedContent(Server server) throws InitializationException {
        super(server);
    }

    public DatastreamReferencedContent() throws ServerException {
        this(Datastream.getStaticServer());
    }

    @Override
    public Datastream copy() {
        try{
            DatastreamReferencedContent ds = new DatastreamReferencedContent(m_server);
            copy(ds);
            return ds;
        } catch (InitializationException ie) {
            throw new RuntimeException(ie.getMessage(),ie);
        }
    }

    /**
     * Gets the external content manager which is used for the retrieval of
     * content.
     *
     * @return an instance of <code>ExternalContentManager</code>
     * @throws Exception is thrown in case the server is not able to find the module.
     */
    private ExternalContentManager getExternalContentManager()
            throws Exception {
        if (m_ecm == null) {
            m_ecm = (ExternalContentManager) m_server.getModule("org.fcrepo.server.storage.ExternalContentManager");
        }
        return m_ecm;
    }

    /**
     * Gets an InputStream to the content of this externally-referenced
     * datastream.
     *
     * <p>The DSLocation of this datastream must be non-null before invoking
     * this method.
     *
     * <p>If successful, the DSMIME type is automatically set based on the web
     * server's response header. If the web server doesn't send a valid
     * Content-type: header, as a last resort, the content-type is guessed by
     * using a map of common extensions to mime-types.
     *
     * <p>If the content-length header is present in the response, DSSize will
     * be set accordingly.
     *
     * @see org.fcrepo.server.storage.types.Datastream#getContentStream()
     */
    @Override
    public InputStream getContentStream(Context context) throws StreamIOException {
        try {
            ContentManagerParams params = new ContentManagerParams(DSLocation);
            if (context != null ) {
                params.setContext(context);
            }
            MIMETypedStream stream = getExternalContentManager()
                    .getExternalContent(params);
            DSSize = getContentLength(stream);
            return stream.getStream();
        } catch (Exception ex) {
            throw new StreamIOException("Error getting content stream", ex);
        }
    }

    /**
     * Returns the length of the content of this stream.
     * @param stream the MIMETypedStream
     * @return length the length of the content
     */
    public long getContentLength(MIMETypedStream stream) {
        long length = 0;
        if (stream.header != null) {
            for (int i = 0; i < stream.header.length; i++) {
                if (stream.header[i].name != null
                        && !stream.header[i].name.equalsIgnoreCase("")
                        && stream.header[i].name.equalsIgnoreCase("content-length")) {
                    length = Long.parseLong(stream.header[i].value);
                    break;
                }
            }
        }
        return length;
    }
}
