package gcu.awarno300.currency;

import android.os.Bundle;

import android.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class fragment_conversion extends Fragment {

    private CurrencyExchange ce;
    EditText txtCategory;
    public fragment_conversion() {
        // Required empty public constructor
    }

    public void setCurrency(CurrencyExchange input)
    {
        Log.d("Debug", "Set currency");
        this.ce = input;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("Debug","Set to retain instance");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("Debug", "Find View");
        View v = inflater.inflate(R.layout.fragment_conversion,container, false);
        Log.d("Debug", "View was found");

        TextView lblTitle = v.findViewById(R.id.lblTitle);
        TextView lblFooter = v.findViewById(R.id.lblConversion);

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

        String tmp = "Convert " + ce.getCategory() + " - " + ce.getForeign();
        lblTitle.setText(tmp);
        tmp = "1 - " + ce.getRate();
        lblFooter.setText(tmp);


        TextView lblCategory = v.findViewById(R.id.lblCategory);
        txtCategory = v.findViewById(R.id.txtCategory);

        lblCategory.setText(ce.getCategory());
        txtCategory.setText("");


        TextView lblForeign = v.findViewById(R.id.lblForeign);
        EditText txtForeign = v.findViewById(R.id.txtForeign);

        lblForeign.setText(ce.getForeign());
        txtForeign.setText("");


        Log.d("Debug", "Create Text watcher");


        //Set up text watcher to check for changes in input
        txtCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >0)
                {
                    convert(s.toString(),true);
                }
            }
        });

        txtForeign.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >0)
                {
                    convert(s.toString(),false);
                }
            }
        });


        return v;
    }

    private void convert(String input, boolean isCategory)
    {
        new Thread(new Task(input,isCategory,ce)).start();
    }

    private class Task implements Runnable {
        private final String input;
        private final boolean isCategory;
        private final CurrencyExchange ce;

        private float answer = 0.0f;
        public Task(String input, boolean isCategory, CurrencyExchange ce)
        {
            this.input = input;
            this.isCategory = isCategory;
            this.ce = ce;
        }

        @Override
        public void run() {
            float input = this.convert(this.input);
            EditText edit;
            if(isCategory)
            {
                answer = input * ce.getRate();
            }else{
                answer = input / ce.getRate();
            }


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(isCategory) //Category is the one making the request
                    {
                        //Check to make sure category has been selected so it doesn't create an infinite loop
                        if(txtCategory.hasFocus())
                        {
                            EditText e = getView().findViewById(R.id.txtForeign);
                            e.setText(String.valueOf(answer));
                        }
                    }else{ //Foreign is the one making the request
                        //Makes sure that only txtCategory has been selected so it doesn't create an infinite loop
                        if(!txtCategory.hasFocus())
                        {
                            EditText e = getView().findViewById(R.id.txtCategory);
                            e.setText(String.valueOf(answer));
                        }
                    }
                }
            });
        }

        public float convert(String s)
        {
            return Float.parseFloat(s);
        }
    }
}