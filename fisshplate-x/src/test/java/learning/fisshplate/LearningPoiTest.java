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

package learning.fisshplate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.seasar.fisshplate.consts.FPDocumentType;
import org.seasar.fisshplate.util.FPPoiUtil;

/**
 * @author a-conv
 */
public class LearningPoiTest extends TestCase {

    public void test() throws Exception {
        ClassLoader loader = null;
        loader = Thread.currentThread().getContextClassLoader();
        Assert.assertNotNull(loader.getResourceAsStream("FPTemplateTest.xls"));
    }

    private Workbook setupInputWorkbook(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        POIFSFileSystem poifs = new POIFSFileSystem(fis);
        fis.close();
        return FPPoiUtil.createWorkbook(poifs);
    }

    /**
     * シート初期化処理テスト
     *
     * @throws Exception
     */
    public void testInithialize() throws Exception {
        String filePath = "src/test/resources/LearningPOITest.xls";
        Workbook input = setupInputWorkbook(filePath);
        Sheet inputSheet = input.getSheetAt(0);

        for (int rowNo = 0; rowNo <= inputSheet.getLastRowNum(); rowNo++) {
            Row row = inputSheet.getRow(rowNo);
            if (row == null) {
                continue;
            }
            for (int columnNo = 0; columnNo <= row.getLastCellNum(); columnNo++) {
                Cell cell = row.getCell(columnNo);
                if (cell == null) {
                    continue;
                }
                RichTextString richText = FPPoiUtil.createRichTextString(FPDocumentType.HSSF, null);
                cell.setCellValue(richText);
                CellStyle style = input.createCellStyle();
                style.setFillPattern(CellStyle.NO_FILL);
                cell.setCellStyle(style);
            }
        }

        FileOutputStream fos = new FileOutputStream("target/outLearningTest.xls");
        input.write(fos);
        fos.close();
    }

    public void testCreateRowTest()throws Exception{
        InputStream is = getClass().getResourceAsStream("/MapBuilderTest_template.xls");
        Workbook wb = FPPoiUtil.createWorkbook(is);
        Sheet ws = wb.getSheetAt(0);
        for(int i=0; i <= ws.getLastRowNum();i++){
            Row hssfRow = ws.getRow(i);
            if(hssfRow != null){
                ws.removeRow(hssfRow);
            }
        }

        FileOutputStream os = new FileOutputStream("target/createRowTest.xls");
        wb.write(os);
        os.close();
        is.close();

    }
}
