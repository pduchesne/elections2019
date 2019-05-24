package com.hilats.elections2019.storage;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pduchesne
 * Created by pduchesne on 2019-05-24.
 */
public class Results {
    Logger log = LoggerFactory.getLogger(Results.class);

    boolean isFinal;
    int version;

    /* P, C, <empty string> */
    String status;

    int processedBooths;
    Date creationDate;
    int totalBooths;
    int processedCantons;
    int totalCantons;
    String id;

    @JsonProperty
    Map<String, ListResults> listResults = new HashMap();

    @JsonProperty
    Map<String, CandidateResult> candidateResults = new HashMap();

    public Results() { }

    /**
     * Merge new Results instance with this instance
     * @param r
     */
    public void merge(Results r) {
        if (r.getId() == null || ! r.getId().equals(this.getId())) {
            //throw new IllegalArgumentException(String.format("Cannot merge results; mismatching ids %s <> %s", r.getId(), this.getId()));
            // TODO looks like this happens - why?
            log.warn(String.format("Trying to merge results with mismatching ids %s <> %s", r.getId(), this.getId()));
            return;
        }

        boolean isNewerResult =
                (!"C".equals(this.getStatus()) || "C".equals(r.getStatus())) &&   // is this result is incomplete or new result is complete, consider new values
                r.getCreationDate().getTime() > this.getCreationDate().getTime();

        if (isNewerResult) // merge only if new result is newer
        {
            // this looks like an update
            this.setProcessedCantons(r.getProcessedCantons());
            this.setProcessedBooths(r.getProcessedBooths());
        }

        for (CandidateResult cr: r.getCandidateResults().values()) {
            CandidateResult currentCr = this.getCandidateResults().get(cr.getId());
            if (currentCr == null) {
                this.getCandidateResults().put(cr.getId(), cr);
            } else {
                currentCr.merge(cr,isNewerResult);
            }
        }

        for (ListResults lr: r.getListResults().values()) {
            ListResults currentLr = this.getListResults().get(lr.getId());
            if (currentLr == null) {
                this.getListResults().put(lr.getId(), lr);
            } else {
                currentLr.merge(lr,isNewerResult);
            }
        }
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProcessedBooths() {
        return processedBooths;
    }

    public void setProcessedBooths(int processedBooths) {
        this.processedBooths = processedBooths;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getTotalBooths() {
        return totalBooths;
    }

    public void setTotalBooths(int totalBooths) {
        this.totalBooths = totalBooths;
    }

    public int getTotalCantons() {
        return totalCantons;
    }

    public void setTotalCantons(int totalCantons) {
        this.totalCantons = totalCantons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProcessedCantons() {
        return processedCantons;
    }

    public void setProcessedCantons(int processedCantons) {
        this.processedCantons = processedCantons;
    }

    public void addListResults(ListResults lr) {
        listResults.put(lr.getId(), lr);
    }

    public void addCandidateResults(CandidateResult cr) {
        candidateResults.put(cr.getId(), cr);
    }

    public Map<String, ListResults> getListResults() {
        return listResults;
    }

    public Map<String, CandidateResult> getCandidateResults() {
        return candidateResults;
    }
}
