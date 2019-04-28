import java.util.*;

public class Main {
    private static final int positiveEmotion = 1;
    private static final int negativeEmotion = 0;
    private static int allPositiveSentences = 0;
    private static int allNegativeSentences = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<ArrayList<Long>> learningLines;
        ArrayList<Integer> teachingTags;
        HashMap<Long, Word> wordOccurrences;
        ArrayList<ArrayList<Long>> testLines;
        ArrayList<Integer> emotionResults = new ArrayList<>();

        final int COUNTER_1 = 80000;
        final int COUNTER_2 = 20000;

        learningLines = readLines(sc, COUNTER_1);

        teachingTags = readLines2(sc, COUNTER_1);

        wordOccurrences = countOccurrences(learningLines, teachingTags);

        testLines = readLines(sc, COUNTER_2);

        for (int i = 0; i < testLines.size(); i++) {
            double resultNegative = (double)allNegativeSentences / 80000;
            double resultPositive = (double)allPositiveSentences / 80000;
            for (int j = 0; j < testLines.get(i).size(); j++) {
                if (testLines.get(i).get(j) != -100){
                    double negativeOccurrences;
                    double positiveOccurrences;
                    if(wordOccurrences.containsKey(testLines.get(i).get(j))) {
                        negativeOccurrences = (double)wordOccurrences.get(testLines.get(i).get(j)).negative;
                        positiveOccurrences = (double)wordOccurrences.get(testLines.get(i).get(j)).positive;
                    } else {
                        negativeOccurrences = 1;
                        positiveOccurrences = 1;
                    }
                    resultNegative *= (negativeOccurrences / allNegativeSentences);
                    resultPositive *= (positiveOccurrences / allPositiveSentences);
                }
            }
            if(resultNegative > resultPositive * 1.25) {
                emotionResults.add(negativeEmotion);
            } else {
                emotionResults.add(positiveEmotion);
            }
        }

        for (Integer emotion : emotionResults) {
            System.out.println(emotion);
        }
    }

    private static ArrayList<ArrayList<Long>> readLines(Scanner sc, int COUNTER) {
        int counter = 0;

        ArrayList<ArrayList<Long>> lines = new ArrayList<>();

        while(sc.hasNextLine()) {
            if (counter == COUNTER) {
                break;
            } else {
                counter++;
            }

            String nextLine = sc.nextLine();
            if(nextLine.isEmpty() ) {
                ArrayList<Long> emptyList = new ArrayList<>();
                emptyList.add((long)-100);
                lines.add(emptyList);
            } else {
                String line[] = nextLine.split("\t");

                if (line[0].equals("-1"))
                    break;

                ArrayList<Long> words = new ArrayList<>();

                for (String w : line) {
                    words.add(Long.parseLong(w));
                }
                lines.add(words);
            }
        }
        return lines;
    }

    private static ArrayList<Integer> readLines2(Scanner sc, int COUNTER) {
        int counter = 0;
        ArrayList<Integer> teachingTags = new ArrayList<>();

        while(sc.hasNextLine()) {
            if (counter == COUNTER) {
                break;
            } else {
                counter++;
            }

            String nextLine = sc.nextLine();

            if(nextLine.isEmpty()){
                teachingTags.add(-100);
            } else {
                String line = nextLine;
                if (line.equals("-1"))
                    break;

                if (Integer.parseInt((line)) == negativeEmotion) {
                    allNegativeSentences++;
                } else if (Integer.parseInt((line)) == positiveEmotion) {
                    allPositiveSentences++;
                }
                teachingTags.add(Integer.parseInt(line));
            }
        }
        return teachingTags;
    }

    private static void printLines(ArrayList<ArrayList<Integer>> lines) {
        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(i).size(); j++) {
                System.out.print(lines.get(i).get(j));
                System.out.print("\t");
            }
            System.out.print("\n");
        }
    }

    private static void printTeachingTags(ArrayList<Integer> teachingTags){
        for (Integer teachingTag : teachingTags) {
            System.out.println(teachingTag);
        }
    }

    private static HashMap<Long, Word> countOccurrences(ArrayList<ArrayList<Long>> lines, ArrayList<Integer> teachingTags){
        HashMap<Long, Word> wordOccurrences = new HashMap<>();
        for(int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).size(); j++) {
                if (wordOccurrences.containsKey(lines.get(i).get(j))){
                    if(teachingTags.get(i) == 0) {
                        wordOccurrences.get(lines.get(i).get(j)).negative++;
                    } else {
                        wordOccurrences.get(lines.get(i).get(j)).positive++;
                    }
                } else {
                    Word temp = new Word();
                    if (teachingTags.get(i) == 0) {
                        temp.negative++;
                    } else {
                        temp.positive++;
                    }
                    wordOccurrences.put(lines.get(i).get(j), temp);
                }
            }
        }
        return wordOccurrences;
    }
}
