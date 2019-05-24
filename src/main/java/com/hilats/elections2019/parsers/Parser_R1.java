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
public class Parser_R1 extends AbstractCSVParser
{

    Logger log = LoggerFactory.getLogger(Parser_R1.class);

    // capture groups C, id and AA
    public static String REGEX = "R1([^X])(\\d{5})\\.(.{2})";

    static DateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public Parser_R1(String regex) {
        super(regex);
    }

    public Parser_R1() {
        super(REGEX);
    }

    @Override
    public void process(Path path, FileStorage storage) {
        MatchResult result = getMatchResult(path);

        FileStorage.POLLING_LEVEL C_value = FileStorage.POLLING_LEVEL.valueOf(result.group(1));
        String id_value = result.group(2);
        FileStorage.ELECTION_TYPE AA_value = FileStorage.ELECTION_TYPE.valueOf(result.group(3));

        try {
            Results content = parseContent(path);

            // this is a R1* file --> final results
            content.setFinal(true);

            storage.mergeResults(AA_value, C_value, id_value, content);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse results from file "+path, e);
        }


    }
    
    @Override
    protected void parseRowValues(List<String> row, Results results) {
        switch (row.get(0)) {
            case "G":
                // Global record
                parseGeneralRow(row, results);
                break;
            case "L":
                // List record
                parseListRow(row, results);
                break;
            case "C":
                // Candidate record
                parseCandidateRow(row, results);
                break;
            case "S":
                // Polling (Scrutin)

                // TODO
                // See R format spec, §2.5.2

                break;
        }
    }

    void parseGeneralRow(List<String> row, Results results) {
        results.setVersion(Integer.parseInt(row.get(1)));
        results.setStatus(row.get(2));
        results.setProcessedBooths(Integer.parseInt(row.get(3)));
        try {
            if (!row.get(5).isEmpty())
                results.setCreationDate(DATETIME_FORMAT.parse(row.get(5) + " " + row.get(6)));
        } catch (ParseException e) {
            log.warn("Failed to parse creation date", e);
        }
        results.setTotalBooths(Integer.parseInt(row.get(7)));
        results.setProcessedCantons(Integer.parseInt(row.get(8)));
        results.setTotalCantons(Integer.parseInt(row.get(9)));
        results.setId(row.get(10));
    }

    void parseListRow(List<String> row, Results results) {
        ListResults lr = new ListResults();

        if (row.get(1).length() > 0) lr.setListNb(Integer.parseInt(row.get(1)));
        lr.setStatus(row.get(2));
        lr.setBallotNb_cat1(Integer.parseInt(row.get(3)));
        lr.setBallotNb_cat2(Integer.parseInt(row.get(4)));
        lr.setBallotNb_cat3(Integer.parseInt(row.get(5)));
        lr.setBallotNb_cat4(Integer.parseInt(row.get(6)));
        if (row.size()>7 && !row.get(7).isEmpty()) lr.setEligibilityNumber(Integer.parseInt(row.get(7)));
        if (row.size()>8 && !row.get(8).isEmpty()) lr.setObtainedSeats(Integer.parseInt(row.get(8)));
        lr.setId(row.get(9));

        // TODO ignore idx 10 for now - can be subId or echevins
        //if (row.size()>10) lr.setGroupSubId(row.get(10));

        if (row.size()>11 && !row.get(11).isEmpty()) lr.setInsId(row.get(11));

        results.addListResults(lr);
    }

    void parseCandidateRow(List<String> row, Results results) {
        CandidateResult cr = new CandidateResult();

        cr.setListNb(Integer.parseInt(row.get(1)));
        cr.setCandidateType(row.get(2));
        cr.setOrder(Integer.parseInt(row.get(3)));
        cr.setPersonalVotes(Integer.parseInt(row.get(4)));
        if (!row.get(5).isEmpty()) {
            try {
                cr.setDeceasedDate(DATE_FORMAT.parse(row.get(5)));
            } catch (ParseException e) {
                log.warn("Failed to parse deceased date", e);
            }
        }
        if (!row.get(6).isEmpty()) cr.setTotalVotesAfterDevolution(Integer.parseInt(row.get(6)));
        if (!row.get(7).isEmpty()) cr.setDevolutionRemainder(Integer.parseInt(row.get(7)));
        if (!row.get(8).isEmpty()) cr.setElectedHolderOrder(Integer.parseInt(row.get(8)));
        if (!row.get(9).isEmpty()) cr.setElectedSubstituteOrder(Integer.parseInt(row.get(9)));

        cr.setId(row.get(10));

        if (row.size()>11 && !row.get(11).isEmpty()) cr.setEchevinOrder(Integer.parseInt(row.get(11)));

        results.addCandidateResults(cr);
    }
}
