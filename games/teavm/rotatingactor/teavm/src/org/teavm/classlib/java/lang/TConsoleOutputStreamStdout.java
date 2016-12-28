/*
 *  Copyright 2013 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.classlib.java.lang;

import org.teavm.classlib.java.io.TIOException;
import org.teavm.classlib.java.io.TOutputStream;
import org.teavm.interop.DelegateTo;
import org.teavm.platform.Platform;

/**
 *
 * @author Alexey Andreev
 */
class TConsoleOutputStreamStdout extends TOutputStream {
    @Override
    @DelegateTo("writeLowLevel")
    public void write(int b) throws TIOException {
        Platform.getConsole().output(b);
    }

    private void writeLowLevel(int b) {
        TConsoleOutputStreamStderr.writeImpl(b);
    }
}
