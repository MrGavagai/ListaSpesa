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
