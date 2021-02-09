package grammar.generator;

import eu.monnetproject.lemon.model.PropertyValue;
import grammar.generator.helper.BindingConstants;
import grammar.generator.helper.SentenceBuilderTransitiveVPEN;
import grammar.generator.helper.SentenceTemplateTest;
import grammar.generator.helper.SubjectType;
import grammar.generator.helper.sentencetemplates.AnnotatedVerb;
import grammar.sparql.SelectVariable;
import grammar.structure.component.*;
import lexicon.LexicalEntryUtil;
import net.lexinfo.LexInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.ArrayList;
import java.util.List;

import static grammar.generator.helper.BindingConstants.BINDING_TOKEN_TEMPLATE;
import static lexicon.LexicalEntryUtil.getDeterminerTokenByNumber;
import static lexicon.LexicalEntryUtil.getOppositeSelectVariable;

public class TransitiveVPGrammarRuleGenerator extends GrammarRuleGeneratorRoot {
    private static final Logger LOG = LogManager.getLogger(TransitiveVPGrammarRuleGenerator.class);

    public TransitiveVPGrammarRuleGenerator(Language language) {
        super(FrameType.VP, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
    }

    @Override
    public GrammarEntry generateFragmentEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        return super.generateFragmentEntry(grammarEntry, lexicalEntryUtil);
    }

    @Override
    public List<String> generateSentences(LexicalEntryUtil lexicalEntryUtil) throws
            QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();

        SubjectType subjectType = lexicalEntryUtil.getSubjectType(lexicalEntryUtil.getSelectVariable(), DomainOrRangeType.PERSON);
        String qWord = lexicalEntryUtil.getSubjectBySubjectType(subjectType, getLanguage(), null);

        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            SentenceBuilderTransitiveVPEN sentenceBuilder = new SentenceBuilderTransitiveVPEN(
                    getLanguage(), getFrameType(), getSentenceTemplateRepository(), getSentenceTemplateParser());
            // skip infinitive forms
            if (new LexInfo().getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
                String qWord2 = lexicalEntryUtil.getSubjectBySubjectType(SubjectType.TEST_SUBJECT_TYPE, getLanguage(), null);
                String testSentence = sentenceBuilder.getBooleanSentence(
                        qWord2,
                        DomainOrRangeType.getMatchingType(
                                lexicalEntryUtil.getConditionUriBySelectVariable(
                                        lexicalEntryUtil.getSelectVariable())).name(),
                        annotatedVerb.getWrittenRepValue(), "for",
                        String.format(BINDING_TOKEN_TEMPLATE,
                                getBindingVariable(),
                                DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                                        LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable())
                                )).name(),
                                SentenceType.VP));
                // System.out.println(lexicalEntryUtil.getReferenceUri());
                // System.out.println(
                //         getSentenceTemplateRepository().findOneByEntryTypeAndLanguageAndArguments(
                //                SentenceType.SENTENCE, Language.EN, "subject", "directObject"));
                generatedSentences.add(testSentence);
                // System.out.println(testSentence);
                //todo: find out how to get preposition from annotatedverb
                //todo: write tokenparser for boolean sentencetemplates
            }
            // Make simple sentence (who develops $x?)
            String sentence = sentenceBuilder.getSentence(qWord,
                    annotatedVerb.getWrittenRepValue(),
                    String.format(
                            BINDING_TOKEN_TEMPLATE,
                            getBindingVariable(),
                            DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                                    LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable())
                            )).name(),
                            SentenceType.NP
                    ));
            generatedSentences.add(sentence);


            // Make sentence using the specified domain or range property (Which museum exhibits $x?)
            String conditionLabel = lexicalEntryUtil.getReturnVariableConditionLabel(lexicalEntryUtil.getSelectVariable());
            // Only generate "Which <condition-label>" if condition label is a DBPedia entity
            if (lexicalEntryUtil.hasInvalidDeterminerToken(lexicalEntryUtil.getSelectVariable())) {
                continue;
            }
            String determiner = lexicalEntryUtil.getSubjectBySubjectType(
                    SubjectType.INTERROGATIVE_DETERMINER,
                    getLanguage(),
                    null
            );
            String determinerToken = getDeterminerTokenByNumber(annotatedVerb.getNumber(), conditionLabel, determiner);
            sentence = sentenceBuilder.getSentence(determinerToken,
                    annotatedVerb.getWrittenRepValue(),
                    String.format(
                            BINDING_TOKEN_TEMPLATE,
                            getBindingVariable(),
                            DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                                    LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable())
                            )).name(),
                            SentenceType.NP
                    ));
            generatedSentences.add(sentence);
        }
        GrammarEntry grammarEntry = new GrammarEntry();
        grammarEntry.setSentences(generatedSentences);
        generateFragmentEntry(grammarEntry, lexicalEntryUtil);
        generatedSentences.sort(String::compareToIgnoreCase);
        return generatedSentences;
    }
}
