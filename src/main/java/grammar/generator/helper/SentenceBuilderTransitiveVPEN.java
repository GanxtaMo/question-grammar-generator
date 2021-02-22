package grammar.generator.helper;

import eu.monnetproject.lemon.model.PropertyValue;
import grammar.generator.helper.datasets.sentencetemplates.SentenceTemplateRepository;
import grammar.generator.helper.parser.SentenceTemplateParser;
import grammar.generator.helper.parser.SentenceToken;
import grammar.structure.component.FrameType;
import grammar.structure.component.Language;
import grammar.structure.component.SentenceType;
import lexicon.LexicalEntryUtil;
import lombok.*;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.List;
import java.util.Map;
public class SentenceBuilderTransitiveVPEN extends SentenceBuilderImpl {

    public SentenceBuilderTransitiveVPEN(
            Language language,
            FrameType frameType,
            SentenceTemplateRepository sentenceTemplateRepository,
            SentenceTemplateParser sentenceTemplateParser) {
        super(language, frameType, sentenceTemplateRepository, sentenceTemplateParser);
    }


    public String getSentence(String subject, String verb, String object) {
        String sentence;
        sentence = sentenceSubjOfPropActive(subject, verb, object);
        return sentence;
    }

    public String getBooleanSentence(String qWord, String subject, String verb, String preposition, String object) {
        String sentence;
        System.out.println(getSentenceTemplateRepository().findOneByEntryTypeAndLanguageAndArguments(SentenceType.VP, getLanguage(), "directObject", "test"));
        if (getSentenceTemplateRepository().getAll().contains("BooleanQuestionWord")) {
            System.out.println("Question Word Template existing");

        }
        sentence = booleanSentence(qWord, subject, verb, preposition, object);
        return sentence;
    }

    // Who writes $x?
    private String sentenceSubjOfPropActive(String subject, String verb, String object) {
        buildSentence(getSentenceTemplateRepository().getAll());
        return String.format("%s %s %s?", subject, verb, object);
    }

    //Does $y play for $x
    private String booleanSentence(String qWord, String subject, String verb, String preposition, String object) {
        return String.format("%s %s %s %s %s?", qWord, subject, verb, preposition, object);
    }

    @Override
    protected List<String> interpretSentenceToken(List<SentenceToken> sentenceTokens, String bindingVar, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        return null;
    }

    @Override
    protected String buildSentence(List<String> sentenceTokens) {
        return super.buildSentence(sentenceTokens);
    }

    @Override
    protected Map<PropertyValue, String> interpretSentenceTokenNP(List<SentenceToken> parsedSentenceTemplate, String bindingVar, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        return null;
    }
}
