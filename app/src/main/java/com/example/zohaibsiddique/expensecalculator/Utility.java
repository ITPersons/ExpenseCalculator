package com.example.zohaibsiddique.expensecalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class Utility {

    static String dateFormat(long date) {
        Date dateF = new Date(date);
        return new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(dateF);
    }

    static String currentTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.getTimeInMillis());
    }

    static String currentTimeInDateFormat() {
        Calendar c = Calendar.getInstance();
        c.getTimeInMillis();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(new StringBuilder().append(day).append("/").append(month+1).append("/").append(year));
    }

    static String simpleDateFormat(long timeInMills) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(new Date(timeInMills));
    }

    static long milliseconds(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            return timeInMilliseconds;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static void setIntentResultCode(Activity activity) {
        Intent intent = new Intent();
        activity.setResult(Activity.RESULT_OK, intent);
    }



    public static String getDate(String time) {
        Date mDate = null;
        String givenDateString = time;
            long timeInMilliseconds = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.SSS");
            try {
                mDate = sdf.parse(givenDateString);
                timeInMilliseconds = mDate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return String.valueOf(mDate);
}

    //Short Toast
    static void shortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    //Long Toast
//    static void longToast(Context context, String message) {
//        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//    }
//
//    static void alertDialog(Context context, String title, String message) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//        alertDialog.setTitle(title);
//        alertDialog.setCancelable(true);
//        alertDialog.setMessage(message);
//        alertDialog.create();
//        alertDialog.show();
//    }


//    static void customeSnackBar(Context context, View view, String message) {
//        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
//        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
//        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setVisibility(View.INVISIBLE);
//
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View view2 = layoutInflater.inflate(R.layout.add_expense, null);
//        layout.addView(view2);
//        snackbar.show();
//    }

    static void failSnackBar(View view, String message, Context context) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view1 = snackbar.getView();
        view1.setBackgroundColor(context.getResources().getColor(R.color.failure));
        TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    static void successSnackBar(View view, String message, Context context) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View view1 = snackbar.getView();
        view1.setBackgroundColor(context.getResources().getColor(R.color.success));
        TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }


//    static void startAnActivityWithBundle(Context context, Class<?> className, String key, String value) {
//        Intent intent = new Intent(context, className);
//        Bundle bundle = new Bundle();
//        bundle.putString(key, value);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
//    }
//
//    static void startActivityWithStringArrayListBundleValue(Context context, Class<?> className, String key, ArrayList<String> value) {
//        Intent intent = new Intent(context, className);
//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList(key, value);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
//    }
//
//    static void startAnActivityWithIntBundleValue(Context context, Class<?> className, String key, int value) {
//        Intent intent = new Intent(context, className);
//        Bundle bundle = new Bundle();
//        bundle.putInt(key, value);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
//    }

    static void startAnActivity(Context context, Class<?> className) {
        Intent intent = new Intent(context, className);
        context.startActivity(intent);
    }

    static void startAnActivityForResult(Activity activity, Context context, Class<?> className, int requestCode) {
        Intent intent = new Intent(context, className);
        activity.startActivityForResult(intent, requestCode);
    }

//    static void putValueToBundle(String key, String value) {
//        Bundle bundle = new Bundle();
//        bundle.putString(key, value);
//    }
//
//    static int getValueFromBundle(Intent intent, String key) {
//        Bundle extras = intent.getExtras();
//        int value = 0;
//        if (extras != null) {
//            value = extras.getInt(key);
//        }
//        return value;
//    }

//    static String getStringValueFromBundle(Intent intent, String key) {
//        Bundle extras = intent.getExtras();
//        String value = null;
//        if (extras != null) {
//            value = extras.getString(key);
//        }
//        return value;
//    }
//
//    static ArrayList<String> getStringArrayListValueFromBundle(Intent intent, String key) {
//        Bundle extras = intent.getExtras();
//        ArrayList<String> arrayList = new ArrayList<>();
//        if (arrayList != null) {
//            arrayList = extras.getStringArrayList(key);
//        }
//        return arrayList;
//    }
//
//    static void alertDialogWithThereArrayItems(final Context context, String directory, String resourceItem, String title, final Class<?> class1, final Class<?> class2, final Class<?> class3) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//        alertDialog.setTitle(title).setItems(Utility.getResourceId(context, directory, resourceItem), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                switch (i) {
//                    case 0:
//                        Utility.startAnActivity(context, class1);
//                        break;
//                    case 1:
//                        Utility.startAnActivity(context, class2);
//                        break;
//                    case 2:
//                        Utility.startAnActivity(context, class3);
//                }
//            }
//        });
//        alertDialog.setCancelable(true);
//        alertDialog.create();
//        alertDialog.show();
//    }

//    static void chooseDialogWithTwoItems(final Context context, String directory, String resourceItem, String title, final Class<?> class1, final Class<?> class2, final String key, final int value, final String key1, final int value1) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//        alertDialog.setTitle(title).setItems(Utility.getResourceId(context, directory, resourceItem), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                switch (i) {
//                    case 0:
//                        Utility.startAnActivityWithIntBundleValue(context, class1, key, value);
//                        break;
//                    case 1:
//                        Utility.startAnActivityWithIntBundleValue(context, class2, key1, value1);
//                        break;
//                }
//            }
//        });
//        alertDialog.setCancelable(true);
//        alertDialog.create();
//        alertDialog.show();
//    }
//
//    static void chooseDialogWithOneItem(final Context context, String directory, String resourceItem, String title, final Class<?> class1, final int value, final String key) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//        alertDialog.setTitle(title).setItems(Utility.getResourceId(context, directory, resourceItem), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                switch (i) {
//                    case 0:
//                        Utility.startAnActivityWithIntBundleValue(context, class1, key, value);
//                        break;
//                }
//            }
//        });
//        alertDialog.setCancelable(true);
//        alertDialog.create();
//        alertDialog.show();
//    }




    static int getResourceId(Context context, String directory, String resourceItem) {
        return context.getResources().getIdentifier(resourceItem , directory, context.getPackageName());
    }

//    static boolean hintEnable(EditText editText, TextInputLayout textInputLayout) {
//        if (editText.getText().toString().trim().isEmpty()) {
//            textInputLayout.setHintAnimationEnabled(true);
//            textInputLayout.setHintEnabled(false);
//        } else {
//            textInputLayout.setHintAnimationEnabled(true);
//            textInputLayout.setHintEnabled(true);
//        }
//        return true;
//    }

    static boolean validateEditText(EditText editText, TextInputLayout textInputLayout, String errorMessage) {
            if (editText.getText().toString().trim().isEmpty()) {
                textInputLayout.setError(errorMessage);
                textInputLayout.setHintEnabled(false);
                textInputLayout.setHintAnimationEnabled(true);
                return false;
            } else {
                textInputLayout.setHintEnabled(true);
                textInputLayout.setHintAnimationEnabled(true);
                textInputLayout.setErrorEnabled(false);
            }

            return true;
    }

//    static boolean validateInput(EditText editText) {
//        if (editText.getText().toString().trim().isEmpty()) {
//            return false;
//        }
//        return true;
//    }
//    static void requestFocus(View view, Context context) {
//        if (view.requestFocus()) {
//            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }
//
//    static void setSpinnerAdapter(Spinner spinner, Context context, String directory, String resourceItems) {
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, Utility.getResourceId(context, directory, resourceItems), android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//    }

    static void setSpinnerAdapterByArrayList(Spinner spinner, Context context, List<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


















































//    private void showTabs() {
//        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Item"));
//        tabLayout.addTab(tabLayout.newTab().setText("Type"));
//        tabLayout.addTab(tabLayout.newTab().setText("Consignment"));
//
//        tabLayout.getTabAt(0).setIcon(android.R.drawable.ic_input_add);
//        tabLayout.getTabAt(1).setIcon(android.R.drawable.ic_input_add);
//        tabLayout.getTabAt(2).setIcon(android.R.drawable.ic_input_add);
//
//
//        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
//        final TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//                if(tabLayout.getTabAt(0).isSelected()) {
//                    actionBar.setTitle("Add new Item");
//                } else if(tabLayout.getTabAt(1).isSelected()) {
//                    actionBar.setTitle("Add new Type");
//                } else if(tabLayout.getTabAt(2).isSelected()) {
//                    actionBar.setTitle("Add new Consignment");
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    public static String getValueFromBundle(Context context, String key) {
////        Intent in = context.getIntent();
////
////        Bundle bundle = intent.getExtras();
////        return bundle.getString("1");
//    }

//    void showInputDialog() {
////        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
////        android.app.FragmentManager fragmentManager = getFragmentManager();
////        customDialogFragment.setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
////        customDialogFragment.show(fragmentManager, "DialogFragment");
////
////        customDialogFragment.dismiss();
//    }
//
//    void fragment() {
//        // replace fragment
////        ItemFragment ItemFragment = new ItemFragment();
////        android.app.FragmentManager fragmentManager = getFragmentManager();
////        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////        fragmentTransaction.replace(R.id.fragment, ItemFragment);
//    }
}
