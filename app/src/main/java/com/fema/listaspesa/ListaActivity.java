/*Copyright (C) <2015>  <Fernando Magnano>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.*/


package com.fema.listaspesa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListaActivity extends AppCompatActivity implements ActionBar.TabListener{

    private dbHandler db;
    private ListView lsv,psv;
    private int mState = 1;
    private AlertDialog ad;
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        // Set the Action Bar to use tabs for navigation
        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Add tabs to the Action Bar for display
        ab.addTab(ab.newTab().setText(R.string.tab_lista).setTabListener(this));
        ab.addTab(ab.newTab().setText(R.string.tab_prodotti).setTabListener(this));
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE); //ActionBar.DISPLAY_HOME_AS_UP |

        lsv =(ListView) findViewById(R.id.llist);
        psv =(ListView) findViewById(R.id.plist);
        db = new dbHandler(this);


        //lsv.setOnTouchListener(gestureListener);
        updateLView();

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        db = new dbHandler(this);
        if(mState == 0){
            setContentView(R.layout.activity_lista);
            lsv =(ListView) findViewById(R.id.llist);
            updateLView();
        }
        else{
            setContentView(R.layout.activity_products);
            psv =(ListView) findViewById(R.id.plist);
            updatePView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista, menu);

        if (mState == 0)
        {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nuovo:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle(R.string.title_activity_prodotti);
                alert.setMessage(R.string.add_new);
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                input.addTextChangedListener(new TextWatcher() {


                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(bt !=null){
                            if(count==0)
                                bt.setEnabled(false);
                            else
                                bt.setEnabled(true);
                        }
                    }

                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub
                    }


                });
                alert.setView(input);

                alert.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value  = input.getText().toString();
                        writedb(value);
                        updatePView();
                    }
                });

                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                ad = alert.create();
                ad.show();
                bt = ad.getButton(DialogInterface.BUTTON_POSITIVE);
                bt.setEnabled(false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Implemented from ActionBar.TabListener
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // This is called when a tab is selected.

        mState = tab.getPosition();
        invalidateOptionsMenu();

        if(tab.getPosition() == 0){
            setContentView(R.layout.activity_lista);
            lsv =(ListView) findViewById(R.id.llist);
            updateLView();
        }
        else{
            setContentView(R.layout.activity_products);
            psv =(ListView) findViewById(R.id.plist);
            updatePView();
        }

    }

    // Implemented from ActionBar.TabListener
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // This is called when a previously selected tab is unselected.
    }

    // Implemented from ActionBar.TabListener
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // This is called when a previously selected tab is selected again.
    }


    private void updateLView() {
        if(lsv != null && db != null){
            Parcelable state = lsv.onSaveInstanceState();
            SQLiteDatabase pdb = db.Opendb();
            Cursor cursor = db.getListCursor(pdb);

            //setListAdapter(new ListAdapter(this, cursor));
            lsv.setAdapter(new ListAdapter(this, cursor));

            lsv.onRestoreInstanceState(state);
            db.Closedb(pdb);
        }
    }

    private class ListAdapter extends CursorAdapter {

        private final LayoutInflater mInflater;
        private findID mfid;

        public ListAdapter(Context context, Cursor cursor) {
            super(context, cursor, true);
            mInflater = LayoutInflater.from(context);

        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            int i;

            TextView t = (TextView) view.findViewById(R.id.lname);
            if(t != null){
                t.setText(cursor.getString(cursor.getColumnIndex("product")));
            }
            ImageView v = (ImageView) view.findViewById(R.id.delete);
            if(v != null){
                i = cursor.getInt(0);
                findID fid = new findID(i);
                v.setTag(fid);
                v.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View vc) {
                        mfid = (findID) vc.getTag();
                        int id = mfid.id;
                        db.deleteFromList(id);

                        updateLView();
                    }
                });
            }
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final View view = mInflater.inflate(R.layout.list_lista, parent, false);
            return view;
        }

        private class findID {
            int id;

            private findID(int i){
                id = i;
            }
        }

    }

    private void writedb(String value){

        db.addProduct(value);
    }

    private void updatePView() {

        if(psv != null && db != null){
            Parcelable state = psv.onSaveInstanceState();
            SQLiteDatabase pdb = db.Opendb();
            Cursor cursor = db.getProductCursor(pdb);

            psv.setAdapter(new ProductAdapter(this, cursor));

            psv.onRestoreInstanceState(state);
            db.Closedb(pdb);
        }
    }

    private class ProductAdapter extends CursorAdapter {

        private final LayoutInflater mInflater;
        private findID mfid;

        public ProductAdapter(Context context, Cursor cursor) {
            super(context, cursor, true);
            mInflater = LayoutInflater.from(context);

        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            int i;

            TextView t = (TextView) view.findViewById(R.id.name);
            if(t != null){
                t.setText(cursor.getString(cursor.getColumnIndex("product")));
                t.setOnLongClickListener(new View.OnLongClickListener() {

                    public boolean onLongClick(View v) {

                        mfid = (findID)v.getTag();
                        final int id =mfid.id;
                        final String s = ((TextView)v).getText().toString();
                        AlertDialog.Builder alert = new AlertDialog.Builder(ListaActivity.this);
                        alert.setTitle(R.string.title_activity_prodotti);
                        alert.setMessage(getString(R.string.delete) + " " + s + "?");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                db.deleteProduct(id);
                                updatePView();
                            }
                        });

                        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });
                        alert.show();

                        return false;
                    }
                });

            }
            final ImageView v = (ImageView) view.findViewById(R.id.selector);
            if(v != null){
                i = cursor.getInt(0);
                findID fid = new findID(i);
                view.setTag(fid);
                t.setTag(fid);
                v.setTag(fid);
                i = cursor.getInt(1);
                if(i == 0)
                    v.setImageResource(android.R.drawable.btn_star_big_off);
                else
                    v.setImageResource(android.R.drawable.btn_star_big_on);
                v.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View vc) {
                        mfid = (findID)vc.getTag();
                        int id =mfid.id;
                        db.toogleSelect(id);
                        updatePView();
                    }
                });
            }
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final View view = mInflater.inflate(R.layout.list_products, parent, false);
            return view;
        }

        private class findID {
            int id;

            private findID(int i){
                id = i;
            }
        }

    }


}
