public enum DomainModel {
    FOUR(4), EIGHT(8), SIXTEEN(16);

    private int result = 4;
    DomainModel(int i) {
        result = i;
    }

    public int getCount() {
        return result;
    }
}
