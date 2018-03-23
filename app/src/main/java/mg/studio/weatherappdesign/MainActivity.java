package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat ;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.*;

public class MainActivity extends AppCompatActivity {

    int i;
    String xml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnClick(View view) {
        new DownloadUpdate().execute();
    }
    public void btnrefresh(View view){
        i=0;
        new DownloadUpdate().execute();
        Date day=new Date();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy ");
        ((TextView)findViewById(R.id.tv_date)).setText(df.format(day));
        SimpleDateFormat weekday = new SimpleDateFormat("EEEE");
        ((TextView)findViewById(R.id.week)).setText(weekday.format(day));

        weekday = new SimpleDateFormat("EEE");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);    //得到后一天
        day =calendar.getTime();

        ((TextView)findViewById(R.id.buttonmon)).setText(weekday.format(day));

        calendar.add(Calendar.DATE, +1);    //得到后二天
        day =calendar.getTime();

        ((TextView)findViewById(R.id.buttontue)).setText(weekday.format(day));

        calendar.add(Calendar.DATE, +1);    //得到后三天
        day =calendar.getTime();

        ((TextView)findViewById(R.id.buttonthu)).setText(weekday.format(day));

        calendar.add(Calendar.DATE, +1);    //得到后四天
        day =calendar.getTime();

        ((TextView)findViewById(R.id.buttonfri)).setText(weekday.format(day));


        calendar.add(Calendar.DATE, -4);

    }
    public void btnmonClick(View view) {
        i=1;
        new DownloadUpdate().execute();


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +1);
        Date day=calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy ");
        ((TextView)findViewById(R.id.tv_date)).setText(df.format(day));
        SimpleDateFormat weekday = new SimpleDateFormat("EEEE");
        ((TextView)findViewById(R.id.week)).setText(weekday.format(day));
        calendar.add(Calendar.DATE, -1);
    }
    public void btntueClick(View view) {
        i=2;
        new DownloadUpdate().execute();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +2);
        Date day=calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy ");
        ((TextView)findViewById(R.id.tv_date)).setText(df.format(day));
        SimpleDateFormat weekday = new SimpleDateFormat("EEEE");
        ((TextView)findViewById(R.id.week)).setText(weekday.format(day));
        calendar.add(Calendar.DATE, -2);
    }
    public void btnthuClick(View view) {
        i=3;
        new DownloadUpdate().execute();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +3);
        Date day=calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy ");
        ((TextView)findViewById(R.id.tv_date)).setText(df.format(day));
        SimpleDateFormat weekday = new SimpleDateFormat("EEEE");
        ((TextView)findViewById(R.id.week)).setText(weekday.format(day));
        calendar.add(Calendar.DATE, -3);
    }
    public void btnfriClick(View view) {
        i=4;
        new DownloadUpdate().execute();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, +4);
        Date day=calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy ");
        ((TextView)findViewById(R.id.tv_date)).setText(df.format(day));
        SimpleDateFormat weekday = new SimpleDateFormat("EEEE");
        ((TextView)findViewById(R.id.week)).setText(weekday.format(day));
        calendar.add(Calendar.DATE, -4);
    }

    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String city="101010";
            try {
                 city = URLEncoder.encode("重庆", "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String stringUrl = String.format("https://www.sojson.com/open/api/weather/xml.shtml?city=%s",city);
            HttpURLConnection urlConnection = null;
            BufferedReader reader;//缓存输入流

            try {
                if(xml==null||xml.equals("")) {
                    URL url = new URL(stringUrl);

                    // Create the request to get the information from the server, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();

                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer buffer = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Mainly needed for debugging
                        buffer.append(line + "\n");
                    }
                    xml=buffer.toString();
                }
                if(xml!=null&&!xml.equals(""))
                {

                    try{
                        Document doc = DocumentHelper.parseText(xml);

                        Element root =doc.getRootElement();
                        Element node=root.element("forecast");
                        List<Element> forecast=node.elements("weather");
                        String temp= forecast.get(i).element("high").getText();
                        temp=temp.trim();
                        String str="";
                        if(temp!=null&&!"".equals(temp)){
                            for(int j=0;j<temp.length();j++){
                                if(temp.charAt(j)>=48 && temp.charAt(j)<=57)
                                   str+=temp.charAt(j);
                            }
                        }
                        return str;

                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }

                //The temperature
                return null;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String temperature) {
            //Update the temperature displayed
            if(temperature==null){
                Toast.makeText(getApplicationContext(),"please connect internet",Toast.LENGTH_SHORT).show();
            }

            else
                ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);

        }
    }
}
