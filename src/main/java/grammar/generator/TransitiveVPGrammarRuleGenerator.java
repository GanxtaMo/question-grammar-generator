package grammar.generator;

import eu.monnetproject.lemon.model.*;
import grammar.generator.helper.BindingConstants;
import grammar.generator.helper.SentenceBuilderTransitiveVPEN;
import grammar.generator.helper.SentenceTemplateTest;
import grammar.generator.helper.SubjectType;
import grammar.generator.helper.parser.SentenceToken;
import grammar.generator.helper.sentencetemplates.AnnotatedVerb;
import grammar.sparql.SelectVariable;
import grammar.structure.component.*;
import lexicon.LexicalEntryUtil;
import lexicon.LexiconSearch;
import net.lexinfo.LexInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.exceptions.QueGGMissingFactoryClassException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static grammar.generator.helper.BindingConstants.BINDING_TOKEN_TEMPLATE;
import static lexicon.LexicalEntryUtil.*;

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
            List<String> booleanSentenceTemplates =
                    getSentenceTemplateRepository().
                            findOneByEntryTypeAndLanguageAndArguments(
                                    SentenceType.VP,
                                    getLanguage(),
                                    "booleanSentence");
            //if booleansentencetemplate and past tense verb then use Was question word
            if (annotatedVerb.getTense().toString().contains("past") && lexicalEntryUtil.getLexicalSense().getReference().toString().contains("boolean")) {
                qWord = lexicalEntryUtil.getSubjectBySubjectType(SubjectType.BOOLEAN_INTERROGATIVE_BE, getLanguage(), null);
                String testSentence = sentenceBuilder.getBooleanSentence(
                        qWord,
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
                generatedSentences.add(testSentence);
            }
            if (new LexInfo().getPropertyValue("infinitive").equals(annotatedVerb.getVerbFormMood())) {
                //todo: find out how to get preposition from annotatedverb
                //todo: write tokenparser for boolean sentencetemplates(Boolean_Interrogative_Do,Boolean_Interrogative_Be)
                //todo: change returntype to boolean

                // skip infinitive forms
                if (!booleanSentenceTemplates.isEmpty() && lexicalEntryUtil.getLexicalSense().getReference().toString().contains("boolean")) {
                    //   List<LexicalEntry> entries = new ArrayList<>(lexicalEntryUtil.getLexicon().getEntrys());
                    // for (LexicalEntry e : entries) {
                    //  System.out.println(e);
                    //System.out.println( e.getCanonicalForm());
                    // if (annotatedVerb.getLexInfo().getPropertyValue(e.getProperty(annotatedVerb.getLexInfo().getProperty("partOfSpeech")))
                    //e.getTypes().forEach(System.out::println);
                    //}
                    //Frame frame = lexicalEntryUtil.getFrameByGrammarType();;
                    //SyntacticRoleMarker synRoleMarker = frame.getSynArg(annotatedVerb.getLexInfo().getSynArg("prepositionalObject")).iterator().next().getMarker();
                    //System.out.println(synRoleMarker);
                    //System.out.println(lexicalEntryUtil.getFrameByGrammarType().getSynArg(annotatedVerb.getLexInfo().getSynArg("prepositionalAdjunct")).iterator().next().getMarker());
                    // System.out.println(annotatedVerb.getWrittenRepValue());
                    //Frame frame = lexicalEntryUtil.getFrameByGrammarType();
                    //LexiconSearch lexiconSearch = new LexiconSearch(getLexicon());
                    //System.out.println(lexiconSearch.getReferencedResource(annotatedVerb.getLexInfo().getProperty("partOfSpeech").getURI()));
                    // System.out.println(annotatedVerb.getLexInfo().getProperty("partOfSpeech").getURI().getFragment());
                    // SyntacticRoleMarker synRoleMarker = frame.getSynArg()
                    //System.out.println(annotatedVerb.getLexInfo() .getPropertyValue("preposition"));
                    URI doRef = URI.create(LexiconSearch.LEXICON_BASE_URI + "component_do");
                    URI beRef = URI.create(LexiconSearch.LEXICON_BASE_URI + "component_be");
                    LexicalEntry doEntry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(doRef);
                    LexicalEntry beEntry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(beRef);
                    //String qword = doEntry.getOtherForms().stream().findFirst().get().getWrittenRep().value;
                    //String qword2 = new ArrayList<LexicalForm>(beEntry.getOtherForms()).get(3).getWrittenRep().value;
                    qWord = lexicalEntryUtil.getSubjectBySubjectType(SubjectType.BOOLEAN_INTERROGATIVE_DO, getLanguage(), null);
                    String testSentence = sentenceBuilder.getBooleanSentence(
                            qWord,
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
                    generatedSentences.add(testSentence);

                    // LexicalEntryUtil lexicalEntryUtil1 = new LexicalEntryUtil(getLexicon(),
                    //       doEntry,FrameType.VP,new ArrayList<LexicalSense>(doEntry.getSenses()).get(0));
                    //  System.out.println(new ArrayList<LexicalEntry>(getLexicon().getEntrys()).get(0).getSenses().toArray()[0]);
                    // sentenceBuilder.getBooleanSentence(qword,lexicalEntryUtil.ge)
                    //System.out.println(lexicalEntryUtil.getReferenceUri());
                    //booleanSentenceTemplates.forEach(System.out::println);
                    //URI detRef = ((SentenceToken) new LexiconSearch(getLexicon()).getReferencedResource("reference:component_do")).getLocalReference();
                    //LexicalEntry entry = new LexiconSearch(lexicalEntryUtil.getLexicon()).getReferencedResource(detRef);
                    //String determiner = entry.getCanonicalForm().getWrittenRep().value;
                    //System.out.println(determiner);
                    // System.out.println(new LexiconSearch(getLexicon()).getReferencedResource("reference:component_do"));
                }
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
        grammarEntry.setReturnType(DomainOrRangeType.AIRLINE);
        generateFragmentEntry(grammarEntry, lexicalEntryUtil);
        generatedSentences.sort(String::compareToIgnoreCase);
        return generatedSentences;
    }
}
