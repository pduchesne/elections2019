package com.hilats.elections2019.parsers;

import com.hilats.elections2019.storage.FileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * Parse files of type R1, i.e. final results, but not R1X (extension)
 *
 * @author pduchesne
 * Created by pduchesne on 2019-05-24.
 */
public class Parser_EML extends AbstractParser
{

    Logger log = LoggerFactory.getLogger(Parser_EML.class);

    // capture groups C, id and AA
    public static String REGEX = "\\w+\\.EML";


    public Parser_EML(String regex) {
        super(regex);
    }

    public Parser_EML() {
        super(REGEX);
    }

    @Override
    public void process(Path path, FileStorage storage) {
        // TODO process EML files
    }
}
