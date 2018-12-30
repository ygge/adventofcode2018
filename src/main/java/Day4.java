import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day4 {

    public static void main(String[] args) throws IOException {
        List<String> rows = Util.readStrings();
        Collections.sort(rows);

        List<Record> records = new ArrayList<>();
        for (String row : rows) {
            records.add(new Record(row));
        }

        Map<String, Map<Integer, Integer>> guardMinutes = new HashMap<>();
        Map<Integer, Integer> guardMap = null;
        Integer asleep = null;
        for (Record record : records) {
            if (record.recordType == RecordType.NEW_GUARD) {
                if (asleep != null) {
                    add(guardMap, asleep, 60);
                    asleep = null;
                }
                if (!guardMinutes.containsKey(record.guardId)) {
                    guardMinutes.put(record.guardId, new HashMap<>());
                }
                guardMap = guardMinutes.get(record.guardId);
            } else {
                if (record.recordType == RecordType.ASLEEP) {
                    if (asleep != null) {
                        System.out.println("Already asleep " + record.date + " " + record.time);
                    } else {
                        asleep = record.minute;
                    }
                } else {
                    if (asleep == null) {
                        System.out.println("Already awake " + record.date + " " + record.time);
                    } else {
                        add(guardMap, asleep, record.minute);
                        asleep = null;
                    }
                }
            }
        }
        if (asleep != null) {
            add(guardMap, asleep, 60);
        }

        // step 1
        int bestTotal = 0;
        String best = "";
        for (Map.Entry<String, Map<Integer, Integer>> entry : guardMinutes.entrySet()) {
            int total = entry.getValue().values().stream().reduce(0, (a, b) -> a+b);
            if (total > bestTotal) {
                best = entry.getKey();
                bestTotal = total;
            }
        }
        Map<Integer, Integer> guardM = guardMinutes.get(best);
        int bestM = 0;
        int bestMinute = -1;
        for (Map.Entry<Integer, Integer> entry : guardM.entrySet()) {
            if (entry.getValue() > bestM) {
                bestMinute = entry.getKey();
                bestM = entry.getValue();
            }
        }
        System.out.println(best + " " + bestMinute + " " + Integer.parseInt(best)*bestMinute);

        //step 2
        bestMinute = 0;
        int bestMinuteValue = 0;
        best = "";
        for (Map.Entry<String, Map<Integer, Integer>> entry : guardMinutes.entrySet()) {
            for (Map.Entry<Integer, Integer> guardEntry : entry.getValue().entrySet()) {
                if (guardEntry.getValue() > bestMinuteValue) {
                    bestMinuteValue = guardEntry.getValue();
                    bestMinute = guardEntry.getKey();
                    best = entry.getKey();
                }
            }
        }

        System.out.println(best + " " + bestMinute + " " + Integer.parseInt(best)*bestMinute);
    }

    private static void add(Map<Integer, Integer> guardMap, int start, int end) {
        for (int i = start; i < end; ++i) {
            Integer v = guardMap.get(i);
            guardMap.put(i, (v == null ? 0 : v) + 1);
        }
    }

    private static class Record {
        private final String date, time, guardId;
        private final RecordType recordType;
        private final int minute;

        private Record(String row) {
            int first = row.indexOf(' ');
            date = row.substring(1, first);
            int second = row.indexOf(' ', first+1);
            time = row.substring(first+1, second-1);
            minute = Integer.parseInt(time.substring(3, 5));
            int hash = row.indexOf('#');
            if (hash == -1) {
                guardId = null;
                if (row.substring(second+1).equals("falls asleep")) {
                    recordType = RecordType.ASLEEP;
                } else {
                    recordType = RecordType.WAKE;
                }
            } else {
                int space = row.indexOf(' ', hash);
                guardId = row.substring(hash+1, space);
                recordType = RecordType.NEW_GUARD;
            }
        }
    }

    private enum RecordType {
        NEW_GUARD,
        ASLEEP,
        WAKE
    }
}
