package io.jenkins.plugins.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;

public class ProxyPass {
    public ProxyPass( String proxyHost, int proxyPort, final String userid, final String password, String url ) {

        try {
            /* Create a HttpURLConnection Object and set the properties */
            URL u = new URL( url );
            Proxy proxy = new Proxy( Proxy.Type.HTTP, new InetSocketAddress( proxyHost, proxyPort ) );
            HttpURLConnection uc = (HttpURLConnection) u.openConnection( proxy );

            Authenticator.setDefault( new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    if (getRequestorType().equals( RequestorType.PROXY )) {
                        return new PasswordAuthentication( userid, password.toCharArray() );
                    }
                    return super.getPasswordAuthentication();
                }
            } );
            uc.connect();
            /* Print the content of the url to the console. */
            showContent( uc );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showContent( HttpURLConnection uc ) throws IOException {
        InputStream i = uc.getInputStream();
        char c;
        InputStreamReader isr = new InputStreamReader( i );
        BufferedReader br = new BufferedReader( isr );
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println( line );
        }
    }

}