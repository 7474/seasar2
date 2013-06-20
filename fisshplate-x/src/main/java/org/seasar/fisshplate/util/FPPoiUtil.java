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

package org.seasar.fisshplate.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.seasar.fisshplate.consts.FPDocumentType;

/**
 * POIの操作の便利メソッドを集めたユーティリティクラスです。
 * @author rokugen
 *
 */
public class FPPoiUtil {
	/**
	 * 非公開コンストラクタしか持たないオブジェクトの生成に使用します。
	 * ヘルパーが持つワークブックに依存するオブジェクトを生成しないように注意が必要となります。
	 */
    private static XSSFCreationHelper xssfCreationHelper = new XSSFWorkbook().getCreationHelper();

    private FPPoiUtil(){}

    /**
     * セルの書式設定に基いてセルの値を戻します。
     * @param poiCell
     * @return セルの値
     */
    public static Object getCellValueAsObject(Cell poiCell) {
        if(poiCell == null){
            return null;
        }
        int cellType = poiCell.getCellType();
        Object ret = null;

        switch(cellType){
        case Cell.CELL_TYPE_NUMERIC:
            ret = getValueFromNumericCell(poiCell);
            break;
        case Cell.CELL_TYPE_STRING:
            ret = poiCell.getRichStringCellValue().getString();
            break;
        case Cell.CELL_TYPE_BOOLEAN:
            ret = Boolean.valueOf(poiCell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_FORMULA:
            ret = poiCell.getCellFormula();
            break;
        case Cell.CELL_TYPE_ERROR:
            ret = new Byte(poiCell.getErrorCellValue());
            break;
        case Cell.CELL_TYPE_BLANK:
            break;
        default:
            return null;
        }

        return ret;
    }

    private static Object getValueFromNumericCell(Cell cell){
        String str = cell.toString();
        if(str.matches("\\d+-.+-\\d+")){
            return cell.getDateCellValue();
        }else{
            return Double.valueOf(cell.getNumericCellValue());
        }
    }

    /**
     *文字列を含むセルの値を文字列として戻します。
     *セルの書式が文字列でない場合はnullを戻します。
     * @param poiCell
     * @return セルの値
     */
    public static String getStringValue(Cell poiCell){
        if(! isStringCell(poiCell)){
            return null;
        }
        RichTextString richVal =  poiCell.getRichStringCellValue();
        if(richVal == null){
            return null;
        }
        return richVal.getString();
    }

    private static boolean isStringCell(Cell poiCell){
        if(poiCell == null){
            return false;
        }
        int type = poiCell.getCellType();
        if(type != Cell.CELL_TYPE_BLANK &&
                type != Cell.CELL_TYPE_STRING){
            return false;
        }
        return true;
    }

    /**
     * Workbookを生成します。
     *
     * @param type
     * @return Workbook
     */
    public static Workbook createWorkbook(FPDocumentType type) {
    	Workbook wb = null;
        switch (type) {
        case XSSF:
        	wb = new XSSFWorkbook();
            break;
        case HSSF:
        default:
        	wb = new HSSFWorkbook();
            break;
        }
        return wb;
    }

    /**
     * Workbookを生成します。
     *
     * @param is
     * @return Workbook
     * @throws IOException
     */
    public static Workbook createWorkbook(InputStream is) throws IOException{
        Workbook wb;
        try {
            wb = WorkbookFactory.create(is);
            return wb;
        } catch (InvalidFormatException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Workbookを生成します。
     *
     * @param pfs
     * @return Workbook
     * @throws IOException
     */
    public static Workbook createWorkbook(POIFSFileSystem pfs) throws IOException{
        Workbook wb;
        wb = WorkbookFactory.create(pfs);
        return wb;
    }

    /**
     * ドキュメントの種類を判定します。
     *
     * @param sheet
     * @return ドキュメントの種類
     */
    public static FPDocumentType getDocumentType(Sheet sheet) {
        if (sheet instanceof HSSFSheet) {
            return FPDocumentType.HSSF;
        } else if (sheet instanceof XSSFSheet) {
            return FPDocumentType.XSSF;
        } else if (sheet == null) {
            throw new NullPointerException("sheet is null.");
        } else {
            throw new IllegalStateException("sheet is invalid type. " + sheet.getClass());
        }
    }

    /**
     * RichTextStringを生成します。
     *
     * @param type
     * @param text
     * @return RichTextString
     */
    public static RichTextString createRichTextString(FPDocumentType type, String text) {
        RichTextString rt = null;
        switch (type) {
        case XSSF:
            rt = new XSSFRichTextString(text);
            break;
        case HSSF:
        default:
            rt = new HSSFRichTextString(text);
            break;
        }
        return rt;
    }

    /**
     * ClientAnchorを生成します。
     *
     * @param type
     * @return ClientAnchor
     */
    public static ClientAnchor createClientAnchor(FPDocumentType type) {
        ClientAnchor ca = null;
        switch (type) {
        case XSSF:
            ca = new XSSFClientAnchor();
            break;
        case HSSF:
        default:
            ca = new HSSFClientAnchor();
            break;
        }
        return ca;
    }

    /**
     * Hyperlinkを生成します。
     *
     * @param type
     * @param linkType
     * @return Hyperlink
     */
    public static Hyperlink createHyperlink(FPDocumentType type, int linkType) {
        Hyperlink hl;
        switch (type) {
        case XSSF:
            hl = xssfCreationHelper.createHyperlink(linkType);
            break;
        case HSSF:
        default:
            hl = new HSSFHyperlink(linkType);
            break;
        }
        return hl;
    }
}
