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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHandler extends SQLiteOpenHelper{
	
	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "ListaSpesa";
 
    // Table name
    private static final String TABLE_PRODUCTS = "products";
 
    // Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_SEL = "sel";
    private static final String KEY_PROD = "product";
 
    public dbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
 // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SEL + " BOOLEAN,"
                + KEY_PROD + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
 
        // Create tables again
        onCreate(db);
    }
	
 // Adding new product
    public void addProduct(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
 
        values.put(KEY_SEL, (int)1); // Selector
        values.put(KEY_PROD, (String) value); // Name
        
        // Inserting Row
        db.insert(TABLE_PRODUCTS, null, values);
        db.close(); 
    }
   
 // Updating single product
    public int updateProduct(int id,String value) {
    	int result;
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        
        values.put(KEY_SEL, (int)1); // Selector
        values.put(KEY_PROD, (String) value); // Name
        
        // updating row
        result = db.update(TABLE_PRODUCTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
        return result;
    }
    
 // Deleting single product
    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_ID + " = ?",new String[] {Integer.toString(id)});
        db.close();
    }
 
    public SQLiteDatabase Opendb(){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	return db;
    }
    
   public void Closedb(SQLiteDatabase db){
	   db.close();
   }
   
   public Cursor getProductCursor(SQLiteDatabase db){
	   Cursor cursor = db.rawQuery("SELECT  * FROM products ORDER BY product", null);
	   cursor.moveToFirst();
   	return cursor;
   }
   
   public Cursor getProductCursorbyID(SQLiteDatabase db, int id){
	   Cursor cursor = db.rawQuery("SELECT  * FROM products WHERE _id = " + Integer.toString(id), null);
	   cursor.moveToFirst();
   	return cursor;
   }
   
   public void toogleSelect(int id){
	    int s;
	    SQLiteDatabase db = this.getWritableDatabase();
	   	Cursor cursor = db.rawQuery("SELECT  * FROM products WHERE _id = " + Integer.toString(id), null);
	   	cursor.moveToFirst();
	   	int i = cursor.getInt(1);
	   	if(i==0)
	   		s=1;
	   	else
	   		s=0;
	   	ContentValues args = new ContentValues();
	   	args.put(KEY_SEL, Integer.toString(s));
	   	
	   	db.update("products", args, "_id = " + Integer.toString(id),null);
	   	
	   	db.close();
	   	
   }
   
   public Cursor getListCursor(SQLiteDatabase db){
	   Cursor cursor = db.rawQuery("SELECT  * FROM products WHERE sel = 1 ORDER BY product", null);
	   cursor.moveToFirst();
   	return cursor;
   }
   
   public void deleteFromList(int id){
	    int s;
	    SQLiteDatabase db = this.getWritableDatabase();
	   	Cursor cursor = db.rawQuery("SELECT  * FROM products WHERE _id = " + Integer.toString(id), null);
	   	cursor.moveToFirst();
	   	s=0;
	   	ContentValues args = new ContentValues();
	   	args.put(KEY_SEL, Integer.toString(s));
	   	
	   	db.update("products", args, "_id = " + Integer.toString(id),null);
	   	
	   	db.close();
  }  
   
}
