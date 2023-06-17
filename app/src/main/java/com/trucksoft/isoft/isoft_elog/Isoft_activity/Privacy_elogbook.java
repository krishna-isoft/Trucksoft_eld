package com.trucksoft.isoft.isoft_elog.Isoft_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.isoft.trucksoft_elog.Loginactivitynew;
import com.isoft.trucksoft_elog.Multiused.Constant;
import com.isoft.trucksoft_elog.Multiused.Preference;
import com.trucksoft.isoft.isoft_elog.R;

public class Privacy_elogbook extends AppCompatActivity {
    private String val="  * When you change status OFF DUTY, SLEEP, ON DUTY"+ " \n \n" +

            "  * When you the system automatically changes status to 'DRIVING'\n" +
            " \n" + "  * When taking breaks"+" \n"
            +" \n" + "  * When moving one state to another to help you keep up with the current state rules. \n"
            +" \n" + "  * When performing a Vehicle inspection report (DVIR) (Pre and Post-Trip) \n"
            +" \n" +"  * If you are uploading an image to report a fault in your truck while doing DVIR\n"
            +" \n" +"  * When Certifying your log.";



    String styledText = "Please take a moment to review below the key points of our <u><font color='blue'><a href=\"https://trucksoft.com/ELD/privacy_policy\">Privacy Policy </a></font></u> and <u><font color='blue'><a href=\"https://trucksoft.com/ELD/terms_conditions\">Terms of Service</a></font></u> ";

    String styledText2 = "By clicking agree & continue you agree to our <u><font color='blue'><a href=\"https://trucksoft.com/ELD/privacy_policy\">Privacy Policy </a></font></u> and <u><font color='blue'><a href=\"https://trucksoft.com/ELD/terms_conditions\">Terms of Service.</a></font></u> ";

    private TextView tlocsub;
    private TextView ttopview;

    private TextView tbottomsub;
    private TextView taccept;

    Preference pref;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_dialog);
        context=this;
        pref=Preference.getInstance(context);
        tlocsub=findViewById(R.id.loc_sub);
        ttopview=findViewById(R.id.t_topview);

        tbottomsub=findViewById(R.id.tbottom_sub);
        taccept=findViewById(R.id.taccept);
        tlocsub.setText(""+val);


        tbottomsub.setText(Html.fromHtml(styledText2), TextView.BufferType.SPANNABLE);
        tbottomsub.setMovementMethod(LinkMovementMethod.getInstance());
         ttopview.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        ttopview.setMovementMethod(LinkMovementMethod.getInstance());
        taccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.putString(Constant.PRIVACY_KEY_ISOFTOFFICE,"1");
                finish();
                Intent mIntent = new Intent(
                        Privacy_elogbook.this,
                        Loginactivitynew.class);
                startActivity(mIntent);
            }
        });
    }
}
