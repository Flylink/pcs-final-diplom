public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public String getPdfName() {
        return pdfName;
    }

    @Override
    public int compareTo(PageEntry o) {
        if (count < o.count) {
            return 1;
        } else if (count > o.count) {
            return -1;
        } else {
            if (page < o.page) {
                return 1;
            } else if (page > o.page) {
                return -1;
            } else {
                return getPdfName().compareTo(o.getPdfName());
            }
        }
    }
}