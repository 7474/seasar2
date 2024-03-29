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

package org.seasar.fisshplate.core.element;

import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.IteratorUtil;
import org.seasar.fisshplate.util.OgnlUtil;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.RowWrapper;



/**
 * 横へ展開するイテレーションを処理するブロック要素クラスです
 * @author rokugen
 */
public class HorizontalIteratorBlock extends AbstractBlock{
    private String varName;
    private String iteratorName;
    private String indexName;
    private int cols;
    private int startCellIndex;
    private RowWrapper row;
    /**
     * 要素を保持する変数名とイテレータ自身の名前とループのインデックス名とタグの書かれたセルを受け取ります。
     * @param varName イテレータ内の要素を保持する変数名
     * @param iteratorName イテレータ名
     * @param indexName ループのインデックス名
     * @param cols ループする列数
     * @param cell タグの書かれたセル
     */
    public HorizontalIteratorBlock(String varName, String iteratorName, String indexName, int cols, CellWrapper cell){
        this.varName = varName;
        this.iteratorName = iteratorName;
        this.cols = cols;
        this.startCellIndex = cell.getCellIndex();
        this.row = cell.getRow();
        if(indexName == null || "".equals(indexName.trim())){
            this.indexName = FPConsts.DEFAULT_ITERATOR_INDEX_NAME;
        }else{
            this.indexName = indexName;
        }

    }

    public void merge(FPContext context) throws FPMergeException {
        Map<String, Object> data = context.getData();
        Object o = OgnlUtil.getValue(iteratorName, data);
        Iterator<?> ite = IteratorUtil.getIterator(o,iteratorName,row);
        mergeIteratively(context, ite, data);
    }


    private void mergeIteratively(FPContext context, Iterator<?> ite,Map<String, Object> data) throws FPMergeException{
        int index = 0;
        int startRowNum = context.getCurrentRowNum();
        int startCell = startCellIndex;
        int maxCellNum = getMaxCellNum();

        mergeNoIterationBlock(context);

        while(ite.hasNext()){
            Object var = ite.next();
            data.put(varName, var);
            data.put(indexName, Integer.valueOf(index));
            index ++;
            context.moveCurrentRowTo(startRowNum);
            mergeBlock(context,startCell,maxCellNum);
            startCell += maxCellNum;
        }
    }

    protected void mergeBlock(FPContext context, int startCell, int maxCellNum) throws FPMergeException {

        for (int i = 0; i < childList.size(); i++) {
            TemplateElement elem = (TemplateElement) childList.get(i);
            if(elem instanceof FPRow){
                context.moveCurrentCellTo((short) startCell);
                mergeRow(context, (FPRow)elem, maxCellNum);

            }else{
                elem.merge(context);
            }
        }
    }

    private int getMaxCellNum() {
        if (cols < 0) {
            return getMaxCellElementListSize() - startCellIndex;
        }
        return cols;
    }

    private int getMaxCellElementListSize(){
        int max = 0;
        for (int i = 0; i < childList.size(); i++) {
            TemplateElement elem = (TemplateElement) childList.get(i);
            if(elem instanceof FPRow){
                int listSize = ((FPRow)elem).getCellElementList().size();
                max = max > listSize ? max : listSize;
            }
        }
        return max;
    }

    private void mergeRow(FPContext context, FPRow row, int maxCellNum) throws FPMergeException {
        Row outRow = context.getCurrentRow();
        outRow.setHeight(row.getRowHeight());
        Map<String, Object> data = context.getData();
        data.put(FPConsts.ROW_NUMBER_NAME, Integer.valueOf(context.getCurrentRowNum() + 1));
        int maxCellIndex = startCellIndex + maxCellNum - 1;
        for (int i = 0; i < row.getCellElementList().size(); i++) {
            if(i < startCellIndex){
                continue;
            }else if(i > maxCellIndex){
                break;
            }
            adjustColumnWidth(context,(short) i);
            TemplateElement elem = (TemplateElement) row.getCellElementList().get(i);
            elem.merge(context);
        }
        context.nextRow();
    }

    private void adjustColumnWidth(FPContext context, int column){
        int cellWidth = this.row.getSheet().getSheet().getColumnWidth(column);
        context.getOutSheet().setColumnWidth(context.getCurrentCellNum(), cellWidth);

    }

    private void mergeNoIterationBlock(FPContext context) throws FPMergeException {
        for (int i = 0; i < childList.size(); i++) {
            TemplateElement elem = (TemplateElement) childList.get(i);
            if(elem instanceof FPRow){
                mergeNoIterationRow(context, (FPRow)elem);
            }else{
                elem.merge(context);
            }
        }

    }
    private void mergeNoIterationRow(FPContext context, FPRow row) throws FPMergeException {
        Row outRow = context.createCurrentRow();
        outRow.setHeight(row.getRowHeight());
        Map<String, Object> data = context.getData();
        data.put(FPConsts.ROW_NUMBER_NAME, Integer.valueOf(context.getCurrentRowNum() + 1));
        for (int i = 0; i < row.getCellElementList().size(); i++) {
            if(i >= startCellIndex){
                break;
            }
            TemplateElement elem = (TemplateElement) row.getCellElementList().get(i);
            elem.merge(context);
        }
        context.nextRow();
    }


}
