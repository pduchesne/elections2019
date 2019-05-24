package com.hilats.elections2019.storage;

import java.util.Date;

/**
 * @author pduchesne
 * Created by pduchesne on 2019-05-24.
 */
public class CandidateResult {
    int listNb;
    String candidateType;
    int order;
    int personalVotes;
    Date deceasedDate;
    int totalVotesAfterDevolution;
    int devolutionRemainder;
    int electedHolderOrder;
    int electedSubstituteOrder;
    String id;
    int echevinOrder;
    private String insId;
    private String candidateName;

    public void merge(CandidateResult cr, boolean isNewerResult) {
        if ( !this.id.equals(cr.getId()))
            throw new IllegalArgumentException(String.format(
                    "Mismatching id when merging candidate results %s <> %s",
                    this.id, cr.getId()));


        if (this.listNb != cr.getListNb())
            throw new IllegalArgumentException(String.format(
                    "Mismatching listNb when merging candidate results %n <> %n",
                    this.listNb, cr.getListNb()));

        if (this.order != cr.getOrder())
            throw new IllegalArgumentException(String.format(
                    "Mismatching order when merging candidate results %n <> %n",
                    this.order, cr.getOrder()));

        if (!this.candidateType.equals(cr.getCandidateType()))
            throw new IllegalArgumentException(String.format(
                    "Mismatching type when merging candidate results %n <> %n",
                    this.candidateType, cr.getCandidateType()));

        if (isNewerResult) {
            this.personalVotes = cr.getPersonalVotes();
            this.totalVotesAfterDevolution = cr.getTotalVotesAfterDevolution();
            this.devolutionRemainder = cr.getDevolutionRemainder();
            this.electedHolderOrder = cr.getElectedHolderOrder();
            this.electedSubstituteOrder = cr.getElectedSubstituteOrder();
            this.echevinOrder = cr.getEchevinOrder();
        }

        if (this.deceasedDate == null)
            this.deceasedDate = cr.getDeceasedDate();

        if (this.insId == null)
            this.insId = cr.getInsId();

        if (this.candidateName == null)
            this.candidateName = cr.getCandidateName();
    }

    public int getListNb() {
        return listNb;
    }

    public void setListNb(int listNb) {
        this.listNb = listNb;
    }

    public String getCandidateType() {
        return candidateType;
    }

    public void setCandidateType(String candidateType) {
        this.candidateType = candidateType;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getPersonalVotes() {
        return personalVotes;
    }

    public void setPersonalVotes(int personalVotes) {
        this.personalVotes = personalVotes;
    }

    public Date getDeceasedDate() {
        return deceasedDate;
    }

    public void setDeceasedDate(Date deceasedDate) {
        this.deceasedDate = deceasedDate;
    }

    public int getTotalVotesAfterDevolution() {
        return totalVotesAfterDevolution;
    }

    public void setTotalVotesAfterDevolution(int totalVotesAfterDevolution) {
        this.totalVotesAfterDevolution = totalVotesAfterDevolution;
    }

    public int getDevolutionRemainder() {
        return devolutionRemainder;
    }

    public void setDevolutionRemainder(int devolutionRemainder) {
        this.devolutionRemainder = devolutionRemainder;
    }

    public int getElectedHolderOrder() {
        return electedHolderOrder;
    }

    public void setElectedHolderOrder(int electedHolderOrder) {
        this.electedHolderOrder = electedHolderOrder;
    }

    public int getElectedSubstituteOrder() {
        return electedSubstituteOrder;
    }

    public void setElectedSubstituteOrder(int electedSubstituteOrder) {
        this.electedSubstituteOrder = electedSubstituteOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEchevinOrder() {
        return echevinOrder;
    }

    public void setEchevinOrder(int echevinOrder) {
        this.echevinOrder = echevinOrder;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public String getInsId() {
        return insId;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateName() {
        return candidateName;
    }
}
