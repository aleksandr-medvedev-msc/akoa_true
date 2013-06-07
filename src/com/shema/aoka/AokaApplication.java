package com.shema.aoka;

import android.app.Application;
import com.shema.aoka.M_DB;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 06.03.13
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public class AokaApplication extends Application {
    private HttpClient httpClient;
    private M_XmlParser xmlParser;
    private M_DB db;
    @Override
    public void onCreate()
    {
        super.onCreate();
        httpClient = createHttpClient();
        xmlParser = new M_XmlParser();
        M_DBFiller _filler = new M_DBFiller(this);
        db =_filler.db;
        //db.open();
    }
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        shutdownHttpClient();
        db.close();
    }
    @Override
    public void onTerminate()
    {
        super.onTerminate();
        shutdownHttpClient();
        db.close();
    }
    private HttpClient createHttpClient()
    {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params,"UTF-8");
        HttpProtocolParams.setUseExpectContinue(params,true);
        SchemeRegistry scheme = new SchemeRegistry();
        scheme.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));
        scheme.register(new Scheme("https", SSLSocketFactory.getSocketFactory(),443));
        ClientConnectionManager manager = new ThreadSafeClientConnManager(params,scheme);
        return new DefaultHttpClient(manager,params);
    }
    private void shutdownHttpClient()
    {
        if (httpClient!=null&&httpClient.getConnectionManager()!=null)
        {
            httpClient.getConnectionManager().shutdown();
        }
    }
    public HttpClient getHttpClient()
    {
        return httpClient;
    }
    public M_XmlParser getXmlParser()
    {
        return xmlParser;
    }
    public M_DB getDb()
    {
        return db;
    }
}
