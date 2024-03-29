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

import junit.framework.TestCase;

import org.apache.poi.ss.usermodel.Workbook;
import org.seasar.fisshplate.consts.FPDocumentType;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.FPPoiUtil;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * @author rokugen
 */
public class WhileBlockTest extends TestCase {

    public WhileBlockTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test例外テスト(){
        Workbook hssfWb =FPPoiUtil.createWorkbook(FPDocumentType.HSSF);
        hssfWb.createSheet().createRow(0).createCell( 0);
        WorkbookWrapper wb = new WorkbookWrapper(hssfWb);
        WhileBlock block = new WhileBlock(wb.getSheetAt(0).getRow(0), "hogehoge");
        try {
            block.merge(new FPContext(null,null));
            fail();
        } catch (FPMergeException e) {
            System.out.println(e);
            assertTrue(true);
        }

    }

}
