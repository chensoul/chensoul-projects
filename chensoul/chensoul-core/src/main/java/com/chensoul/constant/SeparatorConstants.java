package com.chensoul.constant;

import static com.chensoul.constant.PathConstants.SLASH;
import static com.chensoul.constant.SymbolConstants.EXCLAMATION;

import java.io.File;

public interface SeparatorConstants {

    /**
     * Archive Entry Separator : "!/"
     */
    String ARCHIVE_ENTRY_SEPARATOR = EXCLAMATION + SLASH;

    /**
     * File Separator : {@link File#separator}
     */
    String FILE_SEPARATOR = File.separator;

    /**
     * Path Separator : {@link File#pathSeparator}
     */
    String PATH_SEPARATOR = File.pathSeparator;

    /**
     * Line Separator : {@link System#lineSeparator()}
     */
    String LINE_SEPARATOR = System.lineSeparator();

}
