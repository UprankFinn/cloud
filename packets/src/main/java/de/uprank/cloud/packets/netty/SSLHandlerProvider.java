package de.uprank.cloud.packets.netty;

import io.netty.handler.ssl.SslHandler;
import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class SSLHandlerProvider {

    private static final Logger logger = Logger.getLogger(SSLHandlerProvider.class);

    private static final String PROTOCOL = "TLS";
    private static final String ALGORITHM_SUN_X509 = "SunX509";
    private static final String ALGORITHM = "ssl.KeyManagerFactory.algorithm";
    private static final String KEYSTORE = "ssl/venuria.jks";
    private static final String KEYSTORE_TYPE = "JKS";
    private static final String KEYSTORE_PASSWORD = "GqgSoIQSzmmcwZCdyLizMDUiykPfvFtM";
    private static final String CERT_PASSWORD = "picnQSfMaRkRwiHKhhGVBHpfAdcMxWwN";
    private static SSLContext serverSSLContext = null;

    public static SslHandler getSSLHandler() {
        SSLEngine sslEngine = null;
        if (serverSSLContext == null) {
            logger.error("Server SSL context is null");
            System.exit(-1);
        } else {
            sslEngine = serverSSLContext.createSSLEngine();
            sslEngine.setUseClientMode(false);
            sslEngine.setNeedClientAuth(false);

        }
        return new SslHandler(sslEngine);
    }

    public static void initSSLContext() {

        logger.info("Initiating SSL context");
        String algorithm = Security.getProperty(ALGORITHM);
        if (algorithm == null) {
            algorithm = ALGORITHM_SUN_X509;
        }
        KeyStore ks = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("ssl/venuria.de.jks");
            ks = KeyStore.getInstance(KEYSTORE_TYPE);
            ks.load(inputStream, KEYSTORE_PASSWORD.toCharArray());
        } catch (IOException e) {
            logger.error("Cannot load the keystore file", e);
        } catch (CertificateException e) {
            logger.error("Cannot get the certificate", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Somthing wrong with the SSL algorithm", e);
        } catch (KeyStoreException e) {
            logger.error("Cannot initialize keystore", e);
        }
        try {

            // Set up key manager factory to use our key store
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(ks, CERT_PASSWORD.toCharArray());
            KeyManager[] keyManagers = kmf.getKeyManagers();
            // Setting trust store null since we don't need a CA certificate or Mutual Authentication
            TrustManager[] trustManagers = null;

            serverSSLContext = SSLContext.getInstance(PROTOCOL);
            serverSSLContext.init(keyManagers, trustManagers, null);


        } catch (Exception e) {
            logger.error("Failed to initialize the server-side SSLContext", e);
        }


    }

}
