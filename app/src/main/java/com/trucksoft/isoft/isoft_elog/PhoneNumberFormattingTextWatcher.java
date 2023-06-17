package com.trucksoft.isoft.isoft_elog;

import android.text.Editable;
import android.widget.EditText;

public class PhoneNumberFormattingTextWatcher extends android.telephony.PhoneNumberFormattingTextWatcher {
    private boolean backspacingFlag = false;
    private boolean editedFlag = false;
    private int cursorComplement;
    public EditText edt_mno;

    public PhoneNumberFormattingTextWatcher(EditText ET) {
        this.edt_mno = ET;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        cursorComplement = s.length()-edt_mno.getSelectionStart();
        if (count > after) {
            backspacingFlag = true;
        } else {
            backspacingFlag = false;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // nothing to do here =D
    }

    @Override
    public void afterTextChanged(Editable s) {
        String strings = s.toString();
        if (!editedFlag) {
            if (strings.length() == 3 && !backspacingFlag) {
                editedFlag = true;
                String ans = "(" + strings + ") ";
                edt_mno.setText(ans);
                edt_mno.setSelection(edt_mno.getText().length()-cursorComplement);
            }
            else if(strings.length() == 9 && !backspacingFlag) {
                editedFlag = true;
                String ans = strings+"-" ;
                edt_mno.setText(ans);
                edt_mno.setSelection(edt_mno.getText().length()-cursorComplement);

            }

        } else {
            editedFlag = false;
        }
    }

        }
