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

import java.util.List;

import junit.framework.TestCase;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.seasar.fisshplate.consts.FPDocumentType;
import org.seasar.fisshplate.core.element.El;
import org.seasar.fisshplate.core.element.GenericCell;
import org.seasar.fisshplate.core.element.NullCell;
import org.seasar.fisshplate.core.element.Picture;
import org.seasar.fisshplate.core.element.Root;
import org.seasar.fisshplate.core.element.FPRow;
import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.core.parser.handler.CellParserHandler;
import org.seasar.fisshplate.util.FPPoiUtil;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * @author rokugen
 */
public class RowTest extends TestCase {

    public RowTest(String name){
        super (name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testコンストラクタ(){
        Root root = new Root();
        Workbook templateWb =FPPoiUtil.createWorkbook(FPDocumentType.HSSF);
        Sheet templateSheet = templateWb.createSheet();
        Row templateRow = templateSheet.createRow(0);
        Cell cell = templateRow.createCell(0);
        cell.setCellValue(FPPoiUtil.createRichTextString(FPDocumentType.HSSF, "リテラル"));
        cell = templateRow.createCell(1);
        cell.setCellValue(10D);
        //cellNum 2 は設定しない。
        cell = templateRow.createCell(3);
        cell.setCellValue(FPPoiUtil.createRichTextString(FPDocumentType.HSSF, "${data}"));
        cell = templateRow.createCell(4);
        cell.setCellValue(FPPoiUtil.createRichTextString(FPDocumentType.HSSF, "#picture(/hoge/fuga.png cell=1 row=1)"));
        cell = templateRow.createCell(5);
        cell.setCellValue(FPPoiUtil.createRichTextString(FPDocumentType.HSSF, "#picture(${data.path} cell=1 row=1)"));
        cell = templateRow.createCell(6);
        cell.setCellValue(FPPoiUtil.createRichTextString(FPDocumentType.HSSF, "#suspend TEST is ${hoge}"));
        cell = templateRow.createCell(7);
        cell.setCellFormula("TEXT(VALUE(\"20040101\"),\"yyyy/mm/dd\")");
        cell = templateRow.createCell(8);
        cell.setCellFormula("TEXT(VALUE(\"${hoge}\"),\"yyyy/mm/dd\")");
        cell = templateRow.createCell(9);
        cell.setCellValue(FPPoiUtil.createRichTextString(FPDocumentType.HSSF, "#link-url  link = http://www.gyoizo.com text = ほげー"));
        cell = templateRow.createCell(10);
        cell.setCellValue(FPPoiUtil.createRichTextString(FPDocumentType.HSSF, "#link-url  link = ${data.hoge} text = ほげー"));


        WorkbookWrapper workbook = new WorkbookWrapper(templateWb);

        FPRow row = new FPRow(workbook.getSheetAt(0).getRow(0), root, new CellParserHandler());
        List<TemplateElement> elementList = row.getCellElementList();

        TemplateElement elem = elementList.get(0);
        assertTrue(elem.getClass() == GenericCell.class );
        elem = elementList.get(1);
        assertTrue(elem.getClass() == GenericCell.class);
        elem = elementList.get(2);
        assertTrue(elem.getClass() == NullCell.class);
        elem = elementList.get(3);
        assertTrue(elem.getClass() == El.class);
        assertTrue(((El)elem).targetElement.getClass() == GenericCell.class);
        elem = elementList.get(4);
        assertTrue(elem.getClass() == Picture.class);
        elem = elementList.get(5);
        assertTrue(elem.getClass() == El.class);
        assertTrue(((El)elem).targetElement.getClass() == Picture.class);
        elem = elementList.get(6);
        assertTrue(elem.getClass() == Suspend.class);
        assertEquals("TEST is ${hoge}", ((Suspend)elem).getEl().targetElement.getCellValue());
        elem = elementList.get(7);
        assertTrue(elem.getClass() == GenericCell.class);
        elem = elementList.get(8);
        assertTrue(elem.getClass() == El.class);
        elem = elementList.get(9);
        assertTrue(elem.getClass() == Link.class);
        elem = elementList.get(10);
        assertTrue(((El)elem).targetElement.getClass() == Link.class);

    }

}
