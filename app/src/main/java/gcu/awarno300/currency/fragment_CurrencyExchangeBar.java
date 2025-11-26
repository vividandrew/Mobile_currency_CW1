package gcu.awarno300.currency;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class fragment_CurrencyExchangeBar extends Fragment implements View.OnClickListener {

    private CurrencyExchange ce;
    private Button btn;

    public fragment_CurrencyExchangeBar() {
        // Required empty public constructor
    }

    public void setCe(CurrencyExchange input)
    {
        this.ce = input;
    }
    public static fragment_CurrencyExchangeBar newInstance(CurrencyExchange input) {
        fragment_CurrencyExchangeBar fragment = new fragment_CurrencyExchangeBar();
        fragment.setCe(input);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment__currency_exchange_bar,container, false);
        TextView Category = v.findViewById(R.id.Category);
        TextView Foreign = v.findViewById(R.id.Foreign);

        // Set flags for conversion rates
        ImageView CategoryFlag = v.findViewById(R.id.Categoryflag);
        ImageView ForeignFlag = v.findViewById(R.id.ForeignFlag);

        int resid = getActivity().getResources().getIdentifier(ce.getCategory().toLowerCase(),"drawable", getActivity().getPackageName());

        if(resid != 0)
            CategoryFlag.setImageResource(resid);
        else
            CategoryFlag.setImageResource(R.drawable.gbp);

        resid = getActivity().getResources().getIdentifier(ce.getForeign().toLowerCase(),"drawable", getActivity().getPackageName());

        if(resid != 0)
            ForeignFlag.setImageResource(resid);
        else
            ForeignFlag.setImageResource(R.drawable.gbp);

        btn = v.findViewById(R.id.button);

        String tmp = ce.getCategory() + "\n1";
        Category.setText(tmp);

        tmp = ce.getForeign() + "\n" + ce.getRate();
        Foreign.setText(tmp);


        tmp = "Convert to " + ce.getForeign();
        btn.setText(tmp);
        btn.setOnClickListener(this);

        if(ce.getRate() > 10)
        {
            Category.setTextColor(Color.rgb(0,0.5f,0));
            Foreign.setTextColor(Color.rgb(0.5f,0,0));
        } else if (ce.getRate() > .9) {
            Category.setTextColor(Color.rgb(0.5f,0.5f,0.0f));
            Foreign.setTextColor(Color.rgb(0.5f,0.5f,0.0f));
        }else{
            Category.setTextColor(Color.rgb(0.5f,0,0));
            Foreign.setTextColor(Color.rgb(0,0.5f,0));
        }

        Log.d("Fragment", ce.getForeign() + " bar created");

        return v;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment__currency_exchange_bar, container, false);
    }


    @Override
    public void onClick(View v) {
        if(v == btn)
        {
            FragmentManager fm = getActivity().getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            fragment_conversion frag = new fragment_conversion();
            frag.setCurrency(ce);

            ft.replace(R.id.fragmentMain, frag);
            ft.commit();
            Log.d("Debug-aw", "Fragment created and set");
        }
    }
}