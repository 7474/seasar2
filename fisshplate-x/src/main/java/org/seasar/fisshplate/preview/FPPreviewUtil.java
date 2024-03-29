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

package org.seasar.fisshplate.preview;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.seasar.fisshplate.exception.FPException;
import org.seasar.fisshplate.template.FPTemplate;
import org.seasar.fisshplate.util.FPPoiUtil;

/**
 * テンプレートファイルと埋め込みデータファイルから出力ファイルを生成するユーティリティクラスです。
 * @author rokugen
 */
public class FPPreviewUtil {
    private FPPreviewUtil(){}

    /**
     * テンプレートファイルにデータファイル内のデータを埋め込んだ出力ファイルを戻します。
     *
     * @param template テンプレート用ファイル
     * @param data データ用ファイル
     * @return データを埋め込んだワークブック
     * @throws FPException
     */
    public static final Workbook getWorkbook(Workbook template, Workbook data) throws FPException{
        FPTemplate fptemp = new FPTemplate();
        MapBuilder mb = new MapBuilder();
        Map<String, Object> map = mb.buildMapFrom(data);
        return fptemp.process(template, map);
    }

    /**
     * テンプレートファイルのストリームと、データ用ファイルのストリームから出力ファイルを生成して戻します。
     *
     * @param template テンプレート用ストリーム
     * @param data データ用ストリーム
     * @return データを埋め込んだワークブック
     * @throws FPException
     * @throws IOException
     */
    public static final Workbook getWorkbook(InputStream template, InputStream data) throws FPException, IOException{
        Workbook tempWb = FPPoiUtil.createWorkbook(template);
        Workbook dataWb = FPPoiUtil.createWorkbook(data);
        return getWorkbook(tempWb, dataWb);
    }

}
