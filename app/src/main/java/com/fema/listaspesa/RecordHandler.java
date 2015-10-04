package com.fema.listaspesa;

public class RecordHandler {

	//private variables
    int _id;
    int _sel;
    String _product;
	
 // Empty constructor
    public RecordHandler(){
 
    }
	
 // constructor
    public RecordHandler(int id, int sel, String product){
        this._id = id;
        this._sel = sel;
        this._product = product;
    }
 
    // constructor
    public RecordHandler(int sel, String product){
    	this._sel = sel;
        this._product = product;
    }
    
 // getting ID
    public int getID(){
        return this._id;
    }
 
    // setting id
    public void setID(int id){
        this._id = id;
    }
    
 // getting selected
    public int getSel(){
        return this._sel;
    }
 
    // setting selected
    public void setSel(int sel){
        this._sel = sel;
    }
 
    // getting product
    public String getProduct(){
        return this._product;
    }
 
    // setting product
    public void setProduct(String product){
        this._product = product;
    }
    
    
}
