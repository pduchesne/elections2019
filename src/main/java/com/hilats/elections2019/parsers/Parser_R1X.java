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
 * Parse files of type R1X, i.e. municipality extension of final results
 *
 * @author pduchesne
 * Created by pduchesne on 2019-05-24.
 */
public class Parser_R1X extends Parser_R1
{

    Logger log = LoggerFactory.getLogger(Parser_R1X.class);

    // capture groups id and AA
    public static String REGEX = "R1X(\\d{5})\\.(.{2})";

    public Parser_R1X() {
        super(REGEX);
    }

    @Override
    public void process(Path path, FileStorage storage) {
        MatchResult result = getMatchResult(path);

        // this parser is hardcoded for R1X files
        FileStorage.POLLING_LEVEL C_value = FileStorage.POLLING_LEVEL.X;

        String id_value = result.group(1);
        FileStorage.ELECTION_TYPE AA_value = FileStorage.ELECTION_TYPE.valueOf(result.group(2));

        try {
            Results content = parseContent(path);
            storage.mergeResults(AA_value, C_value, id_value, content);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse results from file "+path, e);
        }


    }

    void parseGeneralRow(List<String> row, Results results) {
        try {
            if (!row.get(1).isEmpty())
                results.setCreationDate(DATETIME_FORMAT.parse(row.get(1) + " " + row.get(2)));
        } catch (ParseException e) {
            log.warn("Failed to parse creation date", e);
        }
    }

    void parseListRow(List<String> row, Results results) {
        ListResults lr = new ListResults();

        lr.setInsId(row.get(1));
        lr.setListNb(Integer.parseInt(row.get(2)));
        lr.setListName(row.get(3));

        // TODO
        //lr.setMaximumHolderNb(Integer.parseInt(row.get(4)));
        //lr.setMaximumSubstituteNb(Integer.parseInt(row.get(5)));
        
        lr.setBallotNb_cat1(Integer.parseInt(row.get(6)));
        lr.setBallotNb_cat2(Integer.parseInt(row.get(7)));
        lr.setBallotNb_cat3(Integer.parseInt(row.get(8)));
        lr.setBallotNb_cat4(Integer.parseInt(row.get(9)));
        if (!row.get(10).isEmpty()) lr.setEligibilityNumber(Integer.parseInt(row.get(10)));
        lr.setId(row.get(11));


        results.addListResults(lr);
    }

    void parseCandidateRow(List<String> row, Results results) {
        CandidateResult cr = new CandidateResult();

        cr.setInsId(row.get(1));
        cr.setListNb(Integer.parseInt(row.get(2)));
        cr.setCandidateType(row.get(3));
        cr.setOrder(Integer.parseInt(row.get(4)));
        cr.setCandidateName(row.get(5));
        
        cr.setPersonalVotes(Integer.parseInt(row.get(6)));
        cr.setId(row.get(7));

        results.addCandidateResults(cr);
    }
}
