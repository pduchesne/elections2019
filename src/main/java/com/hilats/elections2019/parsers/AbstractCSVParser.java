package com.hilats.elections2019.parsers;

import com.hilats.elections2019.storage.Results;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pduchesne
 * Created by pduchesne on 2019-05-25.
 */
public abstract class AbstractCSVParser extends AbstractParser {


        /*
    Parent class for parsers for file matching filename  RBC<id>.AA where

    B is 'content type', one of :
        E	Evolution du dépouillement des cantons
        0	Résultats des listes (partiels)
        1	Détails des votes nominatifs et des élus (définitif)
        S	Sièges attribués au niveau du royaume
        H	Hit-parade des candidates
        G	Evolution du dépouillement

    C is 'polling level', one of
        M   Bureau principal de commune (not used ?)
        K   Bureau principal de canton
        D   Bureau principal de district
        A   Bureau principal d’arrondissement
        C   Bureau principal de circonscription
        G   Bureau principal de la circonscription germanophone
        O   Bureau central de province
        P   Bureau principal provincial
        L   Bureau principal de collège
        R   Royaume
        X   Extension au niveau communal

    id is 5-digits INS code

    AA is 'election type', one of
        CK  Chambre
        VL  Parlement flamand
        WL  Parlement régional wallon
        BR  Parlement de la Région de Bruxelles-Capitale
        DE  Parlement de la Communauté germanophone
        EU  Parlement européen
        PR  Conseil provincial
        CG  Conseil communal
        CS  Conseil CPAS
        DI  Conseil de district (Anvers)
     */

    // global pattern for reference; overridden in subclasses
    public static String GLOBAL_REGEX = "R(.)(.)(\\d{5})\\.(.{2})";

    public AbstractCSVParser(String regex) {
        super(regex);
    }

    public Results parseContent(Path path) throws IOException {

        Results results = new Results();

        List<List<String>> rows = parseFile(path);
        for (int i=0;i<rows.size();i++) {
            try {
                parseRowValues(rows.get(i), results);
            } catch (Exception e) {
                log.warn(String.format("Failed to parse row [%d] values in %s", i, path), e);
            }
        }

        return results;
    }

    protected abstract void parseRowValues(List<String> row, Results results);

    protected List<List<String>> parseFile(Path path) {
        try {
            return Files.lines(path).map(line -> parseRow(line)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse file " + path, e);
        }
    }

    protected List<String> parseRow(String line) {
        int idx = 0;
        List<String> result = new ArrayList<>();
        StringBuffer buf = new StringBuffer();
        boolean withinQuotes = false;
        while (idx < line.length()) {
            switch (line.charAt(idx)) {
                case '"':
                    if (withinQuotes) {
                        if (idx < line.length() - 1 && line.charAt(idx + 1) != ',')
                            throw new IllegalStateException("Leftover character after quoted string >> " + buf.toString());
                        withinQuotes = false;
                    } else {
                        if (buf.length() > 0)
                            throw new IllegalStateException("Quotes in the middle of a string >> " + buf.toString());
                        withinQuotes = true;
                    }
                    break;
                case ',':
                    result.add(buf.toString());
                    buf = new StringBuffer();
                    break;
                default:
                    buf.append(line.charAt(idx));
                    break;
            }

            idx++;
        }

        if (withinQuotes)
            throw new IllegalStateException("Line ended with unclosed quoted string");

        // add remaining buffer
        result.add(buf.toString());

        return result;
    }
}
