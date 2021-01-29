package grammar.generator;

import grammar.generator.helper.*;
import grammar.generator.helper.datasets.questionword.QuestionWordFactory;
import grammar.generator.helper.datasets.questionword.QuestionWordRepository;
import grammar.generator.helper.sentencetemplates.AnnotatedNoun;
import grammar.generator.helper.sentencetemplates.AnnotatedNounOrQuestionWord;
import grammar.generator.helper.sentencetemplates.AnnotatedVerb;
import grammar.structure.component.*;
import lexicon.LexicalEntryUtil;
import util.exceptions.QueGGMissingFactoryClassException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static grammar.generator.helper.BindingConstants.BINDING_TOKEN_TEMPLATE;


public class SentenceTemplateTestGrammarRuleGenerator extends GrammarRuleGeneratorRoot {
    public SentenceTemplateTestGrammarRuleGenerator(Language language) {
        super(FrameType.VP, language, BindingConstants.DEFAULT_BINDING_VARIABLE);
    }


    @Override
    public List<String> generateSentences(
            LexicalEntryUtil lexicalEntryUtil
    ) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();

        SubjectType subjectType = lexicalEntryUtil.getSubjectType(lexicalEntryUtil.getSelectVariable(), DomainOrRangeType.PERSON);
        String qWord = lexicalEntryUtil.getSubjectBySubjectType(SubjectType.TEST_SUBJECT_TYPE, Language.EN, null);

        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            System.out.println(annotatedVerb.getWrittenRepValue());
            SentenceTemplateTest sentenceBuilder = new SentenceTemplateTest(
                    qWord,
                    /*String.format(
                            BINDING_TOKEN_TEMPLATE,
                            getBindingVariable(),
                            DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                                    LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable())
                            )).name(),
                            SentenceType.NP
                    )*/"Neymar",
                    annotatedVerb.getWrittenRepValue(),
                    String.format(
                            BINDING_TOKEN_TEMPLATE,
                            getBindingVariable(),
                            DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                                    LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable())
                            )).name(),
                            SentenceType.NP
                    )

            );
            String sentence = sentenceBuilder.getSentence();
            generatedSentences.add(sentence);
        }

        return generatedSentences;

    }
/*
    @Override
    public GrammarEntry generateFragmentEntry(GrammarEntry grammarEntry, LexicalEntryUtil lexicalEntryUtil) throws QueGGMissingFactoryClassException {
        List<String> generatedSentences = new ArrayList<>();
        GrammarEntry fragmentEntry = copyGrammarEntry(grammarEntry);
        fragmentEntry.setType(SentenceType.VP);
        SubjectType subjectType = lexicalEntryUtil.getSubjectType(lexicalEntryUtil.getSelectVariable(), DomainOrRangeType.PERSON);
        String qWord = lexicalEntryUtil.getSubjectBySubjectType(SubjectType.TEST_SUBJECT_TYPE, Language.EN, null);

        List<AnnotatedVerb> annotatedVerbs = lexicalEntryUtil.parseLexicalEntryToAnnotatedVerbs();
        for (AnnotatedVerb annotatedVerb : annotatedVerbs) {
            SentenceTemplateTest sentenceBuilder = new SentenceTemplateTest(
                    qWord,
                    String.format(
                            BINDING_TOKEN_TEMPLATE,
                            getBindingVariable(),
                            DomainOrRangeType.getMatchingType(lexicalEntryUtil.getConditionUriBySelectVariable(
                                    LexicalEntryUtil.getOppositeSelectVariable(lexicalEntryUtil.getSelectVariable())
                            )).name(),
                            SentenceType.NP
                    ),
                    annotatedVerb.getWrittenRepValue(),
                    "Test"

            );
            String sentence = sentenceBuilder.getSentence();
            generatedSentences.add(sentence);
        }
        System.out.println(grammarEntry.getFrameType());
        grammarEntry.setSentences(generatedSentences);
        return grammarEntry;
    }

 */
}
