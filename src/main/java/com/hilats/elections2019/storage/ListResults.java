package com.hilats.elections2019.storage;

/**
 * @author pduchesne
 * Created by pduchesne on 2019-05-24.
 */
public class ListResults {
    int listNb;
    String status;

    int ballotNb_cat1;
    int ballotNb_cat2;
    int ballotNb_cat3;
    int ballotNb_cat4;
    int eligibilityNumber;
    int obtainedSeats;
    String id;
    String groupSubId;
    int obtainedEchevins; // for the lack of a proper translation
    String insId;
    private String listName;


    public void merge(ListResults lr, boolean isNewerResult) {

        if (!this.id.equals(lr.getId()))
            throw new IllegalArgumentException(String.format(
                    "Mismatching id when merging candidate results %s <> %s",
                    this.id, lr.getId()));

        if (this.listNb != lr.getListNb())
            throw new IllegalArgumentException(String.format(
                    "Mismatching listNb when merging candidate results %n <> %n",
                    this.listNb, lr.getListNb()));


        if (isNewerResult) {
            this.ballotNb_cat1 = lr.getBallotNb_cat1();
            this.ballotNb_cat2 = lr.getBallotNb_cat2();
            this.ballotNb_cat3 = lr.getBallotNb_cat3();
            this.ballotNb_cat4 = lr.getBallotNb_cat4();
            this.eligibilityNumber = lr.getEligibilityNumber();
            this.obtainedSeats = lr.getObtainedSeats();
            this.obtainedEchevins = lr.getObtainedEchevins();
        }

        if (this.insId == null)
            this.insId = lr.getInsId();

        if (this.listName == null)
            this.listName = lr.getListName();
    }


    public int getListNb() {
        return listNb;
    }

    public void setListNb(int listNb) {
        this.listNb = listNb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBallotNb_cat1() {
        return ballotNb_cat1;
    }

    public void setBallotNb_cat1(int ballotNb_cat1) {
        this.ballotNb_cat1 = ballotNb_cat1;
    }

    public int getBallotNb_cat2() {
        return ballotNb_cat2;
    }

    public void setBallotNb_cat2(int ballotNb_cat2) {
        this.ballotNb_cat2 = ballotNb_cat2;
    }

    public int getBallotNb_cat3() {
        return ballotNb_cat3;
    }

    public void setBallotNb_cat3(int ballotNb_cat3) {
        this.ballotNb_cat3 = ballotNb_cat3;
    }

    public int getBallotNb_cat4() {
        return ballotNb_cat4;
    }

    public void setBallotNb_cat4(int ballotNb_cat4) {
        this.ballotNb_cat4 = ballotNb_cat4;
    }

    public int getEligibilityNumber() {
        return eligibilityNumber;
    }

    public void setEligibilityNumber(int eligibilityNumber) {
        this.eligibilityNumber = eligibilityNumber;
    }

    public int getObtainedSeats() {
        return obtainedSeats;
    }

    public void setObtainedSeats(int obtainedSeats) {
        this.obtainedSeats = obtainedSeats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupSubId() {
        return groupSubId;
    }

    public void setGroupSubId(String groupSubId) {
        this.groupSubId = groupSubId;
    }

    public int getObtainedEchevins() {
        return obtainedEchevins;
    }

    public void setObtainedEchevins(int obtainedEchevins) {
        this.obtainedEchevins = obtainedEchevins;
    }

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListName() {
        return listName;
    }
}
