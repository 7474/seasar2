/**
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 */

package org.seasar.fisshplate.wrapper;

import org.apache.poi.ss.usermodel.Cell;
import org.seasar.fisshplate.util.FPPoiUtil;

/**
 * Cellのラッパークラスです。
 * @author rokugen
 */
public class CellWrapper {
    private Cell poiCell;
    private RowWrapper row;

    public CellWrapper(Cell cell, RowWrapper row){
        this.row = row;
        this.poiCell = cell;

    }

    public Cell getCell(){
        return poiCell;
    }

    public RowWrapper getRow(){
        return row;
    }

    public boolean isNullCell() {
        return poiCell == null;
    }

    public int getCellIndex(){
        if(isNullCell()){
            return -1;
        }
        return poiCell.getColumnIndex();
    }

    public String getStringValue(){
        return FPPoiUtil.getStringValue(poiCell);
    }

    public Object getObjectValue() {
        return FPPoiUtil.getCellValueAsObject(poiCell);
    }

}
