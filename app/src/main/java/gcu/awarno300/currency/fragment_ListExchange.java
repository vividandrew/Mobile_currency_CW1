package gcu.awarno300.currency;

import android.app.FragmentTransaction;
import android.os.Bundle;

import android.app.Fragment;
import android.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_ListExchange#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_ListExchange extends Fragment {

    public fragment_ListExchange() {
        // Required empty public constructor
    }

    private Collection<CurrencyExchange> Library;


    private final fragment_CurrencyExchangeBar barManager = new fragment_CurrencyExchangeBar();

    public void setLibrary(Collection<CurrencyExchange> input)
    {
        if(Library == null)
        {
            Library = input;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment__list_exchange,container,false);

        if(savedInstanceState == null && Library != null)
        {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            for(CurrencyExchange ce : Library)
            {
                Fragment frag = fragment_CurrencyExchangeBar.newInstance(ce);
                ft.add(R.id.layout, frag);
            }
            ft.commit();

        }
        return v;
    }

    /*
    public void loadLibrary()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fmC.beginTransaction();

        fragment_CurrencyExchangeBar barManager = new fragment_CurrencyExchangeBar();

        for(CurrencyExchange ce : Library)
        {
            Fragment frag = barManager.newInstance(ce);
            ft.add(R.id.layout, frag);
        }
        ft.commit();
    }*/

/* Attempt Async
    public void loadList(View v)
    {
        new Thread(new Task(Library, v)).start();
    }

    private class Task implements Runnable
    {
        private Collection<CurrencyExchange> ceList;
        public Task(Collection<CurrencyExchange> input, View v)
        {
            this.ceList = input;
        }
        public void run()
        {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            fragment_CurrencyExchangeBar barManager = new fragment_CurrencyExchangeBar();

            for(CurrencyExchange ce : Library)
            {
                Fragment frag = barManager.newInstance(ce);
                ft.add(R.id.layout, frag);
            }
            ft.commit();
        }
    }*/
}
