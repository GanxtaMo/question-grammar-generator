package grammar.generator.helper;

public class SentenceTemplateTest {

    private final String qword;
    private final String subject;
    private final String verb;
    private final String object;

    public SentenceTemplateTest(String qword,String subject, String verb, String object) {
        this.qword = qword;
        this.subject=subject;
        this.verb = verb;
        this.object = object;
    }

    public String getSentence() {
        String sentence;
        sentence = sentenceTest();
        return sentence;
    }

    // Does $x write $y?
    public String sentenceTest() {
        return String.format("%s %s %s %s?", qword,subject, verb, object);
    }
}
