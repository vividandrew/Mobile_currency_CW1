/*  Starter project for Mobile Platform Development - 1st diet 25/26
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 Andrew Warnock
// Student ID           s2434620
// Programme of Study   BSc (Hons) Software Development (Year 4)
//

// UPDATE THE PACKAGE NAME to include your Student Identifier
package gcu.awarno300.currency;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    /*
    UI ELEMETS
     Section of variables reserved for UI Elements
    */
    private TextView rawDataDisplay;
    private String dateOfCurrency;
    private TextView lblDate;
    private EditText txtCode;
    private Button btnSearch;

    //Menu Elements
    private FloatingActionButton btnRefresh;
    private Button btnHome;

    //Private variables
    private boolean LoadedList;
    private String result;
    private final String url1="";
    private final String urlSource="https://www.fx-exchange.com/gbp/rss.xml";

    /*
    FRAGMENTS
     Section of variables for fragments
     */

    public static CurrencyExchangeManager cem;
    public Collection<CurrencyExchange> Library;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private Fragment fragCurrencyExchangeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        //rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay
        lblDate = findViewById(R.id.date);

        // Menu Elements
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);

        btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        txtCode = findViewById(R.id.txtCode);

        //Currency exchange manager
        cem = new CurrencyExchangeManager();

        if(savedInstanceState == null)
        {
            fm = getFragmentManager();
            startProgress();
        }
    }

    public void onClick(View aview)
    {
        /*if(aview == startButton)
        {
            startProgress();
        }*/
        if(aview == btnHome)
        {
            displayList();
        }
        if(aview == btnRefresh)
        {
            startProgress();
        }
        if(aview == btnSearch)
        {
            search(String.valueOf(txtCode.getText()));
        }

    }

    public void displayList()
    {
        ft = fm.beginTransaction();
        fragment_ListExchange list = new fragment_ListExchange();
        list.setLibrary(Library);

        ft.replace(R.id.fragmentMain, list);
        ft.commit();
    }

    /* DEBUG FOR EXCHANGE BAR
    public void displaybar()
    {
        ft = fm.beginTransaction();
        CurrencyExchange usd = cem.get("USD");
        Log.d("DisplayBar", cem.get("USD").getForeign());
        fragment_CurrencyExchangeBar frag = new fragment_CurrencyExchangeBar();
        frag.setCe(usd);

        ft.replace(R.id.fragmentMain, frag);
        ft.commit();
    }*/

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private final String url;
        public Task(String aurl){
            url = aurl;
        }
        @Override
        public void run(){
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.d("MyTask","in run");

            try
            {
                Log.d("MyTask","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null){
                    result = result + inputLine;
                }
                in.close();
            }
            catch (IOException ae) {
                Log.e("MyTask", "ioexception");
            }

            //Clean up any leading garbage characters
            int i = result.indexOf("<?"); //initial tag
            result = result.substring(i);

            //Clean up any trailing garbage at the end of the file
            i = result.indexOf("</rss>"); //final tag
            result = result.substring(0, i + 6);

            // Now that you have the xml data into result, you can parse it
            try {
                XmlPullParserFactory factory =
                        XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( new StringReader( result ) );

                //!TODO: YOUR PARSING HERE!!!

                while(xpp.getEventType() != XmlPullParser.END_DOCUMENT)
                {
                    if(xpp.getName() == null)
                    {
                        xpp.next();
                        continue;
                    }
                    if(xpp.getName().equalsIgnoreCase("lastbuilddate"))
                    {
                        dateOfCurrency = xpp.nextText();
                    }

                    if(xpp.getName().equals("item"))
                    {
                        String item ="";

                        while (!(xpp.next() == XmlPullParser.END_TAG && xpp.getName().equals("item")))
                        {
                            if(xpp.getEventType() == XmlPullParser.START_TAG)
                            {
                                String text = xpp.nextText();
                                item += text + ",";
                            }
                        }
                        CurrencyExchange ce = new CurrencyExchange(item);
                        cem.add(ce);
                    }
                    xpp.next();
                }



            } catch (XmlPullParserException e) {
                Log.e("Parsing","EXCEPTION" + e);
                //throw new RuntimeException(e);
            }catch (IOException e) {
                Log.e("Parsing","I/O EXCEPTION" + e);
                //throw new RuntimeException(e);
            }


            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !


            fragment_ListExchange list = new fragment_ListExchange();
            list.setLibrary(cem.getList());

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    ft = fm.beginTransaction();
                    ft.replace(R.id.fragmentMain, list);
                    ft.commit();
                    String tmp = "Content Aquired: " + dateOfCurrency;
                    lblDate.setText(tmp);
                    /*
                    Log.d("UI thread", "I am the UI thread");
                    rawDataDisplay.setText(result);*/
                }
            });
        }

    }


    private void search(String s)
    {
        new Thread(new Search(s.toUpperCase())).start();
    }
    private class Search implements Runnable
    {
        private final String search;
        public Search(String s)
        {
            this.search = s;
        }
        @Override
        public void run() {
            if(search.length() < 3)
            {
                Library = cem.getListOf(search);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ft = fm.beginTransaction();
                        fragment_ListExchange list = new fragment_ListExchange();
                        list.setLibrary(Library);

                        ft.replace(R.id.fragmentMain, list);
                        ft.commit();
                    }
                });
            }else if(search.length() == 3)
            {
                CurrencyExchange ce = cem.get(search);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ft = fm.beginTransaction();
                        Log.d("DisplayBar", cem.get("USD").getForeign());
                        fragment_CurrencyExchangeBar frag = new fragment_CurrencyExchangeBar();
                        frag.setCe(ce);

                        ft.replace(R.id.fragmentMain, frag);
                        ft.commit();
                    }
                });
            }
        }
    }

}