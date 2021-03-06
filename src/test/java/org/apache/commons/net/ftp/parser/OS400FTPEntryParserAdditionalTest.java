/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.net.ftp.parser;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileEntryParser;

import java.util.Calendar;

/**
 * @version $Id$
 */

public class OS400FTPEntryParserAdditionalTest extends CompositeFTPParseTestFramework
{
    private static final String[][] badsamples =
{
    {
        "QPGMR          135168 04/03/18 13:18:19 *FILE",
        "QPGMR          135168    03/24 13:18:19 *FILE",
        "QPGMR          135168 04/03/18 30:06:29 *FILE",
        "QPGMR                 04/03/18 13:18:19 *FILE      RPGUNITC1.FILE",
        "QPGMR          135168    03/24 13:18:19 *FILE      RPGUNITC1.FILE",
        "QPGMR          135168 04/03/18 30:06:29 *FILE      RPGUNITC1.FILE",
        "QPGMR                                   *MEM       ",
        "QPGMR          135168 04/03/18 13:18:19 *MEM       RPGUNITC1.FILE/RUCALLTST.MBR",
        "QPGMR          135168                   *MEM       RPGUNITC1.FILE/RUCALLTST.MBR",
        "QPGMR                 04/03/18 13:18:19 *MEM       RPGUNITC1.FILE/RUCALLTST.MBR",
        "QPGMR USR                               *MEM       RPGUNITC1.FILE/RUCALLTST.MBR"
            }
    };

    private static final String[][] goodsamples =
        {
    {
        "QPGMR                                   *MEM       RPGUNITC1.FILE/RUCALLTST.MBR",
        "QPGMR        16347136 29.06.13 15:45:09 *FILE      RPGUNIT.SAVF"
            }
    };

    /**
     * @see junit.framework.TestCase#TestCase(String)
     */
    public OS400FTPEntryParserAdditionalTest(String name)
    {
        super(name);
    }

    /**
     * @see FTPParseTestFramework#getBadListing()
     */
    @Override
    protected String[][] getBadListings()
    {
        return badsamples;
    }

    /**
     * @see FTPParseTestFramework#getGoodListing()
     */
    @Override
    protected String[][] getGoodListings()
    {
        return goodsamples;
    }

    /**
     * @see FTPParseTestFramework#getParser()
     */
    @Override
    protected FTPFileEntryParser getParser()
    {
        return new CompositeFileEntryParser(new FTPFileEntryParser[]
        {
            new OS400FTPEntryParser(),
            new UnixFTPEntryParser()
        });
    }

    /**
     * @see FTPParseTestFramework#testParseFieldsOnDirectory()
     */
    @Override
    public void testParseFieldsOnDirectory() throws Exception
    {
        FTPFile f = getParser().parseFTPEntry("PEP             36864 04/03/24 14:06:34 *DIR       dir1/");
        assertNotNull("Could not parse entry.",
                      f);
        assertTrue("Should have been a directory.",
                   f.isDirectory());
        assertEquals("PEP",
                     f.getUser());
        assertEquals("dir1",
                     f.getName());
        assertEquals(36864,
                     f.getSize());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MARCH);

        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 6);
        cal.set(Calendar.SECOND, 34);

        assertEquals(df.format(cal.getTime()),
                     df.format(f.getTimestamp().getTime()));
    }

    @Override
    protected void doAdditionalGoodTests(String test, FTPFile f)
    {
        if (test.startsWith("d"))
        {
            assertEquals("directory.type",
                FTPFile.DIRECTORY_TYPE, f.getType());
        }
    }

    /**
     * @see FTPParseTestFramework#testParseFieldsOnFile()
     */
    @Override
    public void testParseFieldsOnFile() throws Exception
    {
        FTPFile f = getParser().parseFTPEntry("PEP              5000000000 04/03/24 14:06:29 *STMF      build.xml");
        assertNotNull("Could not parse entry.",
                      f);
        assertTrue("Should have been a file.",
                   f.isFile());
        assertEquals("PEP",
                     f.getUser());
        assertEquals("build.xml",
                     f.getName());
        assertEquals(5000000000L,
                     f.getSize());

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 6);
        cal.set(Calendar.SECOND, 29);
        assertEquals(df.format(cal.getTime()),
                     df.format(f.getTimestamp().getTime()));
    }
}
