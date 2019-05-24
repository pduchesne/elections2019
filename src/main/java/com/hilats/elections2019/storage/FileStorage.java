package com.hilats.elections2019.storage;

import com.hilats.elections2019.parsers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author pduchesne
 * Created by pduchesne on 2019-05-24.
 */
public class FileStorage {

    Logger log = LoggerFactory.getLogger(FileStorage.class);

    List<AbstractParser> parsers = new ArrayList();

    public enum ELECTION_TYPE {
        CK, //  Chambre
        VL, //  Parlement flamand
        WL, //  Parlement régional wallon
        BR, //  Parlement de la Région de Bruxelles-Capitale
        DE, //  Parlement de la Communauté germanophone
        EU, //  Parlement européen
        PR, //  Conseil provincial
        CG, //  Conseil communal
        CS, //  Conseil CPAS
        DI, //  Conseil de district (Anvers)
    }

    public enum POLLING_LEVEL {
        M, //   Bureau principal de commune (not used ?)
        K, //   Bureau principal de canton
        D, //   Bureau principal de district
        A, //   Bureau principal d’arrondissement
        C, //   Bureau principal de circonscription
        G, //   Bureau principal de la circonscription germanophone
        O, //   Bureau central de province
        P, //   Bureau principal provincial
        L, //   Bureau principal de collège
        R, //   Royaume
        X, //   Extension au niveau communal
    }


    Path dataDir;

    Map<ELECTION_TYPE, Map<POLLING_LEVEL, Map<String, Results>>> resultCache = new HashMap();
    Map<String, ListResults> listResultsCache = new HashMap();
    Map<String, CandidateResult> candidateResultsCache = new HashMap();

    public FileStorage(Path dataDir) {
        this.dataDir = dataDir;

        registerParser(new Parser_R0());
        registerParser(new Parser_R1());
        registerParser(new Parser_R1X());
        registerParser(new Parser_EML());

        init();
    }

    private Map<String, Results> ensureExists(ELECTION_TYPE type, POLLING_LEVEL level) {
        Map<POLLING_LEVEL, Map<String, Results>> typeMap = resultCache.get(type);
        if (typeMap == null)
            resultCache.put(type, typeMap = new HashMap());

        Map<String, Results> levelMap = typeMap.get(level);
        if (levelMap == null)
            typeMap.put(level, levelMap = new HashMap());

        return levelMap;
    }

    public Set<String> getInsCodes(ELECTION_TYPE type, POLLING_LEVEL level) {

        return ensureExists(type, level).keySet();
    }

    public Results getResults(ELECTION_TYPE type, POLLING_LEVEL level, String insCode) {
        Map<String, Results> levelMap = ensureExists(type, level);
        return levelMap.get(insCode);
    }

    /*
    public void setResults(ELECTION_TYPE type, POLLING_LEVEL level, String insCode, Results results) {
        Map<String, Results> levelMap = ensureExists(type, level);
        levelMap.put(insCode, results);
    }
    */

    /**
     *
     * @param type
     * @param level
     * @param insCode
     * @param results
     */
    public void mergeResults(ELECTION_TYPE type, POLLING_LEVEL level, String insCode, Results results) {
        Map<String, Results> levelMap = ensureExists(type, level);
        Results r = levelMap.get(insCode);

        if (r != null) {
            r.merge(results);
        } else {
            levelMap.put(insCode, results);
        }

    }

    public void registerParser(AbstractParser parser) {
        // TODO ensure no duplicates
        parsers.add(parser);
    }


    /**
     * Initialize storage by reading all content from filesystem
     */
    public void init() {
        try (Stream<Path> paths = Files.walk(dataDir)) {
            paths.filter(Files::isRegularFile)
                    .forEach(this::parseFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to browser data path", e);
        }

        initDirectoryWatch(dataDir);
    }

    private void parseFile(Path file) {
        boolean processed = false;
        for (AbstractParser parser: parsers) {
            if (parser.accepts(file)) {
                parser.process(file, this);
                processed = true;
            }
        }

        if (!processed)
            log.warn("No parser found for "+file);
    }


    private void initDirectoryWatch(Path directoryToWatch) {

        Thread watchThread = new Thread() {
            @Override
            public void run() {
                try {
                    log.info("Watching for changes in "+directoryToWatch);

                    WatchService watcher = FileSystems.getDefault().newWatchService();
                    WatchKey key = directoryToWatch.register(watcher,
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_MODIFY);

                    while(true) {


                        try {
                            key = watcher.take();
                        } catch (InterruptedException x) {
                            return;
                        }

                        for (WatchEvent<?> event: key.pollEvents()) {
                            WatchEvent.Kind<?> kind = event.kind();

                            if (kind == StandardWatchEventKinds.OVERFLOW) {
                                continue;
                            }

                            WatchEvent<Path> ev = (WatchEvent<Path>)event;
                            Path filename = directoryToWatch.resolve(ev.context());

                            log.info("File changed, parsing "+filename);
                            parseFile(filename);

                        }

                        // Reset the key -- this step is critical if you want to
                        // receive further watch events.  If the key is no longer valid,
                        // the directory is inaccessible so exit the loop.
                        boolean valid = key.reset();
                        if (!valid) {
                            break;
                        }
                    }

                    System.out.println("File watcher exiting");

                } catch (IOException e) {
                    throw new RuntimeException("Failed to start directory watch", e);
                }
            }
        };

        watchThread.start();


    }

}
