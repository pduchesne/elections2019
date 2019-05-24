package com.hilats.elections2019.parsers;

import com.hilats.elections2019.storage.CandidateResult;
import com.hilats.elections2019.storage.FileStorage;
import com.hilats.elections2019.storage.ListResults;
import com.hilats.elections2019.storage.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.MatchResult;

/**
 * Parse files of type R1, i.e. final results, but not R1X (extension)
 *
 * @author pduchesne
 * Created by pduchesne on 2019-05-24.
 */
public class Parser_R0 extends Parser_R1
{

    Logger log = LoggerFactory.getLogger(Parser_R0.class);

    // capture groups C, id and AA
    public static String REGEX = "R0([^X])(\\d{5})(?:_\\d{3})?\\.(.{2})";


    public Parser_R0(String regex) {
        super(regex);
    }

    public Parser_R0() {
        super(REGEX);
    }

}
