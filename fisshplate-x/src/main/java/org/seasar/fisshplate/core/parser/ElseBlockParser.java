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

package org.seasar.fisshplate.core.parser;

import java.util.regex.Pattern;

import org.seasar.fisshplate.core.element.AbstractBlock;
import org.seasar.fisshplate.core.element.ElseBlock;

/**
 * else を解析するクラスです。
 * @author rokugen
 */
public class ElseBlockParser extends AbstractElseParser {
    private static final Pattern patElse = Pattern.compile("^\\s*#else\\s*$");


    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.parser.AbstractElseParser#createElement(java.lang.String)
     */
    protected AbstractBlock createElement(String condition) {
        return new ElseBlock();
    }

    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.parser.AbstractElseParser#getPattern()
     */
    protected Pattern getPattern() {
        return patElse;
    }

}