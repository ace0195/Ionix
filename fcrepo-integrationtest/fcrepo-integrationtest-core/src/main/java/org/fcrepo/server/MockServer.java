package org.fcrepo.server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.fcrepo.server.errors.ModuleInitializationException;
import org.fcrepo.server.errors.ServerInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MockServer extends Server {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(MockServer.class);

    public MockServer() throws ServerInitializationException, ModuleInitializationException{
        this(new HashMap<String,String>(), getServerDir());
    }
    public MockServer(Map<String,String> serverParams, File homeDir) throws ServerInitializationException, ModuleInitializationException {
        super(serverParams, homeDir);
    }
    private static File getServerDir(){
        File s = new File(System.getProperty("java.io.tmpdir"));
        File f = new File(s,"server");
        f.mkdir();
        return s;
    }
}
