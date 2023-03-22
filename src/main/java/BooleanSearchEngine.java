import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> words;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        List<File> pdfList = List.of(Objects.requireNonNull(pdfsDir.listFiles()));
        words = new HashMap<>();
        for (File pdf : pdfList) {
            var doc = new PdfDocument(new PdfReader(pdf));
            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i + 1));
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word.toLowerCase(), 0) + 1);
                }
                int count;
                for (var word : freqs.keySet()) {
                    if (freqs.containsKey(word.toLowerCase())) {
                        count = freqs.get(word.toLowerCase());
                        this.words.computeIfAbsent(word.toLowerCase(), w -> new ArrayList<>()).add(new PageEntry(pdf.getName(), i + 1, count));
                    }
                }
                freqs.clear();
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> result = new ArrayList<>();
        if (words.containsKey(word.toLowerCase())) {
            result.addAll(words.get(word.toLowerCase()));
        }
        Collections.sort(result);
        return result;
    }
}