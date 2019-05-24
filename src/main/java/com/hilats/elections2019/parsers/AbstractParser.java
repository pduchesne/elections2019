package com.hilats.elections2019.parsers;

import com.hilats.elections2019.storage.FileStorage;
import com.hilats.elections2019.storage.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.util.stream.Collectors;

/**
 * @author pduchesne
 * Created by pduchesne on 2019-05-24.
 */
public abstract class AbstractParser {

    Logger log = LoggerFactory.getLogger(AbstractParser.class);

    public Pattern filenameRegex;

    public AbstractParser(String regex) {
        filenameRegex = Pattern.compile(regex);
    }

    public boolean accepts(Path path) {
        return path.toFile().isFile() && filenameRegex.matcher(path.getFileName().toString()).matches();
    }

    public MatchResult getMatchResult(Path path) {
        Matcher m = filenameRegex.matcher(path.getFileName().toString());
        m.matches();
        return m.toMatchResult();
    }

    public abstract void process(Path path, FileStorage storage);
}
