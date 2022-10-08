package pl.rejsykoalicja.charter.enums;

public enum Equipment {
    SPINNAKER(500),
    BEDDING(100);
    int cost;

    Equipment(int cost) {
        this.cost = cost;
    }
}
