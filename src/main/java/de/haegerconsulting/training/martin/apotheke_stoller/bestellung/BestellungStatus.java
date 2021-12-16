package de.haegerconsulting.training.martin.apotheke_stoller.bestellung;

public enum BestellungStatus {
    NOT_YET_PROCESSED,
    SENT,
    FULLY_PROCESSED,
    CANCELED;

    public static BestellungStatus changeStatus(int newStatus){
        if (newStatus == 0) {
            return NOT_YET_PROCESSED;
        }
        else if (newStatus == 1) {
            return SENT;
        }
        else if (newStatus == 2) {
            return FULLY_PROCESSED;
        }
        else if (newStatus == 3) {
            return CANCELED;
        }
        else throw new IllegalStateException("This status does not exist!");
    }
}
