package com.example.data.content

import com.example.data.model.GrammarExample
import com.example.data.model.GrammarRule

object GrammarContentProvider {

    fun getGrammarRules(sourceLang: String = "bn"): List<GrammarRule> {
        val isBengali = sourceLang == "bn"

        return listOf(
            GrammarRule(
                id = "rule_noun",
                title = if (isBengali) "Noun (বিশেষ্য পদ)" else "Nouns & Types",
                category = "Parts of Speech",
                summary = if (isBengali) "কোনো ব্যক্তি, স্থান, বস্তু বা ধারণার নামকে Noun বলে।" else "A noun represents a person, place, thing, or idea.",
                formulaOrStructure = "Noun = Person | Place | Thing | Idea",
                explanation = if (isBengali) "Noun মূলত ৫ প্রকার: Proper Noun, Common Noun, Collective Noun, Material Noun, Abstract Noun। বাক্যে এটি Subject বা Object হিসেবে বসে।" else "Nouns can be proper, common, collective, material, or abstract. They act as subjects or objects in sentences.",
                examples = listOf(
                    GrammarExample("Rahim is reading an interesting book.", if (isBengali) "রহিম একটি আকর্ষণীয় বই পড়ছে।" else "Rahim is reading a book.", "Rahim, book"),
                    GrammarExample("Honesty is the best policy.", if (isBengali) "সততাই সর্বোৎকৃষ্ট পন্থা।" else "Honesty is the best policy.", "Honesty, policy")
                ),
                quizQuestion = if (isBengali) "নিচের বাক্যে কোনটি Abstract Noun? 'Knowledge is power.'" else "Identify the abstract noun: 'Knowledge is power.'",
                quizOptions = listOf("Knowledge", "is", "The", "None"),
                quizCorrectAnswer = "Knowledge",
                quizExplanation = if (isBengali) "'Knowledge' হলো একটি মানসিক ধারণা বা গুণ যা স্পর্শ করা যায় না, তাই এটি Abstract Noun।" else "Knowledge is an intangible concept, making it an abstract noun."
            ),
            GrammarRule(
                id = "rule_present_simple",
                title = if (isBengali) "Present Simple Tense (সাধারণ বর্তমান কাল)" else "Present Simple Tense",
                category = "Tenses",
                summary = if (isBengali) "চিরন্তন সত্য, অভ্যাসগত কাজ এবং সাধারণ বর্তমান ঘটনা বোঝাতে ব্যবহৃত হয়।" else "Used for universal truths, habits, and general facts.",
                formulaOrStructure = "Subject + Verb (Base Form) (+ s/es if 3rd Person Singular) + Object",
                explanation = if (isBengali) "Subject যদি He, She, It বা কোনো একক নাম হয় (3rd Person Singular), তাহলে মূল Verb-এর শেষে 's' বা 'es' যুক্ত করতে হবে।" else "Add -s/-es to the base verb when the subject is third-person singular (He, She, It, singular names).",
                examples = listOf(
                    GrammarExample("The sun rises in the east.", if (isBengali) "সূর্য পূর্ব দিকে ওঠে (চিরন্তন সত্য)।" else "The sun rises in the east.", "rises"),
                    GrammarExample("She drinks coffee every morning.", if (isBengali) "সে প্রতিদিন সকালে কফি পান করে (অভ্যাস)।" else "She drinks coffee every morning.", "drinks")
                ),
                quizQuestion = if (isBengali) "সঠিক রূপটি বাছাই করুন: 'He ___ to office by bus every day.'" else "Choose the correct form: 'He ___ to office by bus every day.'",
                quizOptions = listOf("goes", "go", "going", "is gone"),
                quizCorrectAnswer = "goes",
                quizExplanation = if (isBengali) "'He' হলো 3rd Person Singular, তাই 'go'-এর সাথে 'es' যোগ হয়ে 'goes' হবে।" else "'He' is third-person singular, taking the verb form 'goes'."
            ),
            GrammarRule(
                id = "rule_present_continuous",
                title = if (isBengali) "Present Continuous Tense (ঘটমান বর্তমান কাল)" else "Present Continuous Tense",
                category = "Tenses",
                summary = if (isBengali) "বর্তমানে কোনো কাজ চলছে বোঝাতে এটি ব্যবহৃত হয়।" else "Used for actions happening right now at the time of speaking.",
                formulaOrStructure = "Subject + am/is/are + Verb(ing) + Object",
                explanation = if (isBengali) "I-এর পর am; He/She/It-এর পর is; এবং You/We/They-এর পর are বসে। এরপর মূল Verb-এর সাথে -ing যোগ হয়।" else "Subject takes am/is/are according to person/number, followed by the present participle (verb+ing).",
                examples = listOf(
                    GrammarExample("I am learning English with NexVora.", if (isBengali) "আমি নেক্সভোরার সাথে ইংরেজি শিখছি।" else "I am learning English with NexVora.", "am learning"),
                    GrammarExample("They are playing football in the field.", if (isBengali) "তারা মাঠে ফুটবল খেলছে।" else "They are playing football.", "are playing")
                ),
                quizQuestion = if (isBengali) "সঠিক বাক্যটি চিহ্নিত করুন:" else "Which sentence is correctly structured?",
                quizOptions = listOf(
                    "We are studying for the exam right now.",
                    "We is studying for the exam.",
                    "We am studying for the exam.",
                    "We studying for the exam."
                ),
                quizCorrectAnswer = "We are studying for the exam right now.",
                quizExplanation = if (isBengali) "'We'-এর পর 'are' বসে এবং verb-এর সাথে ing যুক্ত হয়।" else "'We' requires auxiliary 'are' plus verb-ing."
            ),
            GrammarRule(
                id = "rule_past_simple",
                title = if (isBengali) "Past Simple Tense (সাধারণ অতীত কাল)" else "Past Simple Tense",
                category = "Tenses",
                summary = if (isBengali) "অতীতে কোনো নির্দিষ্ট সময়ে সম্পন্ন হওয়া কাজ বোঝাতে ব্যবহৃত হয়।" else "Describes actions completed at a specific time in the past.",
                formulaOrStructure = "Subject + Past Form of Verb (V2) + Object",
                explanation = if (isBengali) "অতীতের ঘটনা, যেমন yesterday, last night, in 2020 ইত্যাদি থাকলে Past Simple Tense হয়।" else "Use the V2 past tense form of regular (-ed) or irregular verbs for past events.",
                examples = listOf(
                    GrammarExample("She visited London last summer.", if (isBengali) "সে গত গ্রীষ্মে লন্ডন ভ্রমণ করেছিল।" else "She visited London.", "visited"),
                    GrammarExample("I wrote a letter yesterday.", if (isBengali) "আমি গতকাল একটি চিঠি লিখেছিলাম।" else "I wrote a letter.", "wrote")
                ),
                quizQuestion = if (isBengali) "'buy'-এর Past Form কোনটি?" else "What is the past form (V2) of 'buy'?",
                quizOptions = listOf("bought", "buyed", "buying", "buys"),
                quizCorrectAnswer = "bought",
                quizExplanation = if (isBengali) "'Buy' হলো irregular verb, যার Past form হলো 'bought'।" else "'Buy' is an irregular verb whose past tense is 'bought'."
            ),
            GrammarRule(
                id = "rule_prepositions",
                title = if (isBengali) "Prepositions (ইন, অন, অ্যাট এর ব্যবহার)" else "Prepositions of Time & Place",
                category = "Prepositions",
                summary = if (isBengali) "সময়ের ও স্থানের নির্ভুল বর্ণনায় In, On, At এর নিয়ম।" else "Rules for using In, On, and At for time and locations.",
                formulaOrStructure = "At (Exact time/point) | On (Days/Dates/Surfaces) | In (Months/Years/Enclosed)",
                explanation = if (isBengali) "• At: নির্দিষ্ট সময় (at 5 PM), উৎসব (at Eid/Christmas)\n• On: বার ও তারিখ (on Monday, on 15th August)\n• In: মাস, বছর, ঋতু (in 2026, in May, in Summer)" else "• At: Specific times, holidays\n• On: Days and dates\n• In: Months, years, seasons",
                examples = listOf(
                    GrammarExample("The class starts at 9:00 AM on Monday.", if (isBengali) "সোমবার সকাল ৯টায় ক্লাস শুরু হবে।" else "Starts at 9 AM on Monday.", "at 9:00 AM, on Monday"),
                    GrammarExample("We will travel in December.", if (isBengali) "আমরা ডিসেম্বর মাসে ভ্রমণ করব।" else "Travel in December.", "in December")
                ),
                quizQuestion = if (isBengali) "শূন্যস্থান পূরণ: 'I will meet you ___ Friday afternoon.'" else "Fill in: 'I will meet you ___ Friday afternoon.'",
                quizOptions = listOf("on", "in", "at", "by"),
                quizCorrectAnswer = "on",
                quizExplanation = if (isBengali) "সপ্তাহের যেকোনো দিন (Days) এর পূর্বে 'on' বসে।" else "Days of the week always take the preposition 'on'."
            ),
            GrammarRule(
                id = "rule_articles",
                title = if (isBengali) "Articles (A, An, The)" else "Definite & Indefinite Articles",
                category = "Articles",
                summary = if (isBengali) "অনির্দিষ্ট ও নির্দিষ্ট একবচন ও বহুবচনে Articles এর ব্যবহার।" else "Using A, An for non-specific and The for specific nouns.",
                formulaOrStructure = "A + Consonant Sound | An + Vowel Sound | The + Specific Noun",
                explanation = if (isBengali) "A ও An বসে অনির্দিষ্ট একবচন Noun-এর পূর্বে। Vowel সাউন্ড (a, e, i, o, u এর মতো উচ্চারণ) থাকলে 'An' বসে (যেমন: An honest man, কারণ h অনুচ্চারিত)। নির্দিষ্ট কিছুর ক্ষেত্রে 'The' বসে।" else "Use A/An for singular indefinite nouns. Use 'An' before vowel sounds. Use 'The' for specific nouns.",
                examples = listOf(
                    GrammarExample("He is an honest police officer.", if (isBengali) "তিনি একজন সৎ পুলিশ কর্মকর্তা।" else "An honest officer.", "an honest"),
                    GrammarExample("The Eiffel Tower is in Paris.", if (isBengali) "আইফেল টাওয়ার প্যারিসে অবস্থিত।" else "The Eiffel Tower.", "The Eiffel")
                ),
                quizQuestion = if (isBengali) "সঠিক Article কোনটি? 'She has ___ university degree.'" else "Choose the correct article: 'She has ___ university degree.'",
                quizOptions = listOf("a", "an", "the", "no article"),
                quizCorrectAnswer = "a",
                quizExplanation = if (isBengali) "'University' এর শুরুতে 'yu' ব্যঞ্জনধ্বনি উচ্চারিত হয়, তাই এর আগে 'a' বসে।" else "'University' begins with a consonant sound ('yu'), so it takes 'a'."
            )
        )
    }
}
