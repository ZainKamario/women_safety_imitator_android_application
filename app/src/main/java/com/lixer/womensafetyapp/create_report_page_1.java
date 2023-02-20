package com.lixer.womensafetyapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class create_report_page_1 extends Fragment implements View.OnClickListener{



    private View root_view;

    String tempSaveEditTextData = "";

    private ConstraintLayout option_1;
    private ConstraintLayout option_2;
    private ConstraintLayout option_3;
    private ConstraintLayout option_4;
    private ConstraintLayout option_5;
    private ConstraintLayout option_6;

    private ConstraintLayout next_btn;

    private ImageView option_img_1;
    private ImageView option_img_2;
    private ImageView option_img_3;
    private ImageView option_img_4;
    private ImageView option_img_5;
    private ImageView option_img_6;




    private EditText option_7_editText;


    private create_new_report_model report_model;


    public create_report_page_1(create_new_report_model create_new_report_model) {
        this.report_model= create_new_report_model;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root_view = inflater.inflate(R.layout.create_report_page_1, container, false);
        tempSaveEditTextData = "Harasment";

        option_1  = root_view.findViewById(R.id.option_1);
        option_2  = root_view.findViewById(R.id.option_2);
        option_3  = root_view.findViewById(R.id.option_3);
        option_4  = root_view.findViewById(R.id.option_4);
        option_5  = root_view.findViewById(R.id.option_5);
        option_6  = root_view.findViewById(R.id.option_6);

        next_btn  = root_view.findViewById(R.id.report_next_btn);

        option_img_1  = root_view.findViewById(R.id.option_1_img);
        option_img_2  = root_view.findViewById(R.id.option_2_img);
        option_img_3  = root_view.findViewById(R.id.option_3_img);
        option_img_4  = root_view.findViewById(R.id.option_4_img);
        option_img_5  = root_view.findViewById(R.id.option_5_img);
        option_img_6  = root_view.findViewById(R.id.option_6_img);



        option_7_editText = root_view.findViewById(R.id.option_7_edit);

        option_1.setOnClickListener(this);
        option_2.setOnClickListener(this);
        option_3.setOnClickListener(this);
        option_4.setOnClickListener(this);
        option_5.setOnClickListener(this);
        option_6.setOnClickListener(this);
        next_btn.setOnClickListener(this);







        return root_view;

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.option_1:
                option_img_1.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_checked));
                option_img_2.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_3.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_4.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_5.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_6.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));

                option_7_editText.setEnabled(false);
                option_7_editText.setText("");
                tempSaveEditTextData = "Harasment";

                break;
            case R.id.option_2:
                option_img_1.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_2.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_checked));
                option_img_3.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_4.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_5.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_6.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));

                option_7_editText.setEnabled(false);
                option_7_editText.setText("");
                tempSaveEditTextData = "Domestic Abuse";

                break;
            case R.id.option_3:
                option_img_1.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_2.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_3.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_checked));
                option_img_4.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_5.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_6.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));

                option_7_editText.setEnabled(false);
                option_7_editText.setText("");
                tempSaveEditTextData = "Commercial Sexual Exploitation";
                break;
            case R.id.option_4:
                option_img_1.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_2.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_3.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_4.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_checked));
                option_img_5.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_6.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));

                option_7_editText.setEnabled(false);
                option_7_editText.setText("");
                tempSaveEditTextData = "Stalking and Harassment";
                break;
            case R.id.option_5:
                option_img_1.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_2.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_3.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_4.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_5.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_checked));
                option_img_6.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));

                option_7_editText.setEnabled(false);
                option_7_editText.setText("");
                tempSaveEditTextData = "Report";

                break;
            case R.id.option_6:
                option_img_1.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_2.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_3.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_4.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_5.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_unchecked));
                option_img_6.setImageDrawable(root_view.getContext().getResources().getDrawable(R.drawable.ic_report_check_checked));

                option_7_editText.setEnabled(true);
                tempSaveEditTextData="";


                break;
            case R.id.report_next_btn:

                if(!tempSaveEditTextData.equals("")){
                    report_model.setReport_title(tempSaveEditTextData.toUpperCase());

                  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containor, new create_report_page_2(report_model)).commit();
                }
                else if (option_7_editText.isEnabled()){
                    if(!option_7_editText.getText().toString().equals("")){
                        report_model.setReport_title(option_7_editText.getText().toString().toUpperCase());
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.containor, new create_report_page_2(report_model)).commit();

                    }else{
                        Toast.makeText(root_view.getContext(), "Please Select Report Title First", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(root_view.getContext(), "Please Select Report Title First", Toast.LENGTH_SHORT).show();
                }


                break;

        }

    }
}