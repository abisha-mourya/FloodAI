package com.example.util

import com.example.data.model.TriageGuideItem
import com.example.data.model.UrgencyLevel

object OfflineTriageEngine {

    val allGuides = listOf(
        TriageGuideItem(
            id = "attic_trap",
            category = "Water Trapped",
            title = "Water Rising Inside Home / Attic Trap",
            urgencyLevel = UrgencyLevel.LIFE_THREATENING,
            iconName = "roof",
            summary = "NEVER climb into an attic without an axe or escape tool. Countless flood victims drown trapped under roofs.",
            doSteps = listOf(
                "Turn off main electrical circuit breaker ONLY if you can reach it safely before water touches you.",
                "Move to the highest floor with life jackets, blankets, phone, flashlight, whistle, and drinking water.",
                "If forced into the attic, take an AXE, CROWBAR, or heavy hammer to break through the roof.",
                "Get onto the ROOF so rescue boats and helicopters can see and extract you.",
                "Wave bright cloth (yellow/orange), blow a whistle 3 times, or use phone strobe at night."
            ),
            doNotSteps = listOf(
                "DO NOT hide in a closed attic without an exterior roof exit.",
                "DO NOT touch wet electrical outlets or appliances.",
                "DO NOT attempt to swim through swift floodwater outside — currents are deceptively strong."
            ),
            multiLangTranslations = mapOf(
                "ta" to "கூரை தளம் பாதுகாப்பு: கோடாரி அல்லது தப்பிக்கும் கருவி இல்லாமல் மேல் கூரைக்கு செல்லாதீர்கள்! நீர் அதிகரித்தால் கூரை மேல் ஏறி மீட்பு குழுவுக்கு சைகை செய்யுங்கள்.",
                "te" to "ఇంట్లో వరద నీరు పెరిగినప్పుడు: గొడ్డలి లేదా పైకప్పును పగలగొట్టే సాధనం లేకుండా అటకపైకి వెళ్లవద్దు. రక్షకుల కోసం పైకప్పు పైకి ఎక్కి సంకేతం ఇవ్వండి.",
                "kn" to "ಮನೆಯಲ್ಲಿ ಪ್ರವಾಹ ನೀರು ಹೆಚ್ಚಾದಾಗ: ಕೊಡಲಿ ಅಥವಾ ಉಪಕರಣವಿಲ್ಲದೆ ಅಟ್ಟಕ್ಕೆ ಏರಬೇಡಿ. ರಕ್ಷಣಾ ದಳಕ್ಕೆ ಕಾಣಿಸಲು ಸೂರಿನ ಮೇಲೆ ಏರಿ ಸಹಾಯ ಕೋರಿ.",
                "ml" to "വീടിനുള്ളിൽ വെള്ളം കയറുമ്പോൾ: കോടാലിയോ മേൽക്കൂര പൊളിക്കാനുള്ള ഉപകരണമോ ഇല്ലാതെ തട്ടിൻപുറത്തേക്ക് കയറരുത്. രക്ഷാപ്രവർത്തകർ കാണാൻ മേൽക്കൂരയിൽ കയറുക.",
                "hi" to "बिना कुल्हाड़ी के अटारी में कभी न जाएं। छत पर जाएं ताकि बचाव दल आपको देख सके। यदि संभव हो तो मुख्य बिजली बंद करें।"
            )
        ),
        TriageGuideItem(
            id = "water_purification",
            category = "Sanitation & Water",
            title = "Making Contaminated Floodwater Safe to Drink",
            urgencyLevel = UrgencyLevel.CRITICAL,
            iconName = "water",
            summary = "Floodwater contains raw sewage, dead animals, oil, and hazardous bacteria. Never drink untreated floodwater.",
            doSteps = listOf(
                "Method 1 (Best): Strain cloudy water through clean cloth/filter, then bring to a ROLLING BOIL for 1 full minute.",
                "Method 2 (Chlorine/Bleach): Add 6 to 8 drops of regular unscented household bleach per 1 gallon (approx 2 drops per liter) of clear water.",
                "Mix thoroughly and let stand for 30 MINUTES before drinking. Water should have a faint chlorine smell.",
                "Method 3 (Solar SODIS): Fill clear plastic bottles and leave in direct intense sunlight for 6 continuous hours.",
                "Keep purified water in sanitized, covered containers."
            ),
            doNotSteps = listOf(
                "DO NOT use scented, color-safe, or detergent-mixed bleach.",
                "DO NOT rely solely on simple cloth straining without boiling or chlorination.",
                "DO NOT use raw floodwater to wash utensils, brush teeth, or clean wounds."
            ),
            multiLangTranslations = mapOf(
                "ta" to "குடிநீர் சுத்திகரிப்பு: அழுக்கு நீரை துணியால் வடிகட்டி, 1 நிமிடம் கொதிக்க வைக்கவும். அல்லது 1 லிட்டர் தண்ணீருக்கு 2 சொட்டு மணம் இல்லாத பிளீச் சேர்த்து 30 நிமிடம் ஊற வைக்கவும்.",
                "te" to "నీటి శుద్ధీకరణ: నీటిని శుభ్రమైన గుడ్డతో వడకట్టి 1 నిమిషం పాటు బాగా మరిగించండి. లేదా లీటరుకు 2 చుక్కల క్లోరిన్ వేసి 30 నిమిషాలు ఉంచండి.",
                "kn" to "ಕುಡಿಯುವ ನೀರು ಶುದ್ಧೀಕರಣ: ನೀರನ್ನು ಬಟ್ಟೆಯಿಂದ ಸೋಸಿ ಕನಿಷ್ಠ 1 ನಿಮಿಷ ಚೆನ್ನಾಗಿ ಕುದಿಸಿ. ಅಥವಾ ಪ್ರತಿ ಲೀಟರ್ ನೀರಿಗೆ 2 ಹನಿ ಕ್ಲೋರಿನ್ ಹಾಕಿ 30 ನಿಮಿಷ ಬಿಡಿ.",
                "ml" to "വെള്ളം ശുദ്ധീകരിക്കൽ: തുണി ഉപയോഗിച്ച് അരിച്ചെടുത്ത ശേഷം 1 മിനിറ്റ് തിളപ്പിക്കുക. അല്ലെങ്കിൽ 1 ലിറ്റർ വെള്ളത്തിൽ 2 തുള്ളി ക്ലോറിൻ ഒഴിച്ച് 30 മിനിറ്റ് വെക്കുക.",
                "hi" to "पानी को 1 मिनट तक उबालें। या बिना खुशबू वाला ब्लीच: 1 गैलन पानी में 8 बूंदें डालें और 30 मिनट तक प्रतीक्षा करें।"
            )
        ),
        TriageGuideItem(
            id = "snake_bite",
            category = "Medical & Hazards",
            title = "Snake & Wildlife Bites in Floodwaters",
            urgencyLevel = UrgencyLevel.LIFE_THREATENING,
            iconName = "snake",
            summary = "Floods displace venomous snakes (Cobras, Kraits, Russell's Vipers) into homes, trees, and floating debris.",
            doSteps = listOf(
                "Move away from the snake immediately; stay calm and still to slow venom circulation.",
                "Keep the bitten limb IMMOBILE and AT OR SLIGHTLY BELOW heart level.",
                "Remove all rings, bangles, watches, and tight clothing before swelling begins.",
                "Gently wash the bite area with clean water if available without scrubbing.",
                "Signal for immediate emergency medical evacuation to reach Anti-Snake Venom (ASV)."
            ),
            doNotSteps = listOf(
                "DO NOT apply a tight tourniquet that cuts off blood supply.",
                "DO NOT cut the wound with a blade or attempt to suck out the venom.",
                "DO NOT apply ice, herbal pastes, or submerge the bite in dirty water.",
                "DO NOT give the victim tea, coffee, alcohol, or painkiller aspirin."
            ),
            multiLangTranslations = mapOf(
                "ta" to "பாம்புக்கடி முதலுதவி: கடித்த உறுப்பை அசைக்காமல் இதய மட்டத்திற்கு கீழே வைக்கவும். மோதிரம், வளையல்களை அகற்றவும். வெட்டவோ, உறிஞ்சவோ, கயிறு கட்டவோ வேண்டாம்!",
                "te" to "పాము కాటు చికిత్స: కాటు వేసిన భాగాన్ని కదల్చకుండా గుండె కంటే కింద ఉంచండి. ఉంగరాలు తొలగించండి. గాటు పెట్టడం, రక్తం పీల్చడం చేయవద్దు.",
                "kn" to "ಹಾವು ಕಡಿತ ಪ್ರಥಮ ಚಿಕಿತ್ಸೆ: ಕಚ್ಚಿದ ಅಂಗವನ್ನು ಅಲುಗಾಡಿಸಬೇಡಿ, ಹೃದಯಕ್ಕಿಂತ ಕೆಳಮಟ್ಟದಲ್ಲಿಡಿ. ಉಂಗುರಗಳನ್ನು ತೆಗೆಯಿರಿ. ಬ್ಲೇಡ್‌ನಿಂದ ಕೊಯ್ಯಬೇಡಿ ಅಥವಾ ಹಗ್ಗ ಕಟ್ಟಬೇಡಿ.",
                "ml" to "പാമ്പ് കടിയേറ്റാൽ: കടിയേറ്റ ഭാഗം അനക്കാതെ ഹൃദയത്തിന് താഴെയായി വെക്കുക. മോതിരങ്ങളും വളകളും മാറ്റുക. മുറിവുണ്ടാക്കാനോ വിഷം വലിച്ചെടുക്കാനോ ശ്രമിക്കരുത്.",
                "hi" to "सांप का काटना: काटे गए अंग को स्थिर रखें और दिल से नीचे रखें। अंगूठियां हटा दें। चीरा न लगाएं, जहर न चूसें और कसकर पट्टी न बांधें।"
            )
        ),
        TriageGuideItem(
            id = "downed_powerlines",
            category = "Electrical Safety",
            title = "Downed Powerlines & Electrocution in Water",
            urgencyLevel = UrgencyLevel.LIFE_THREATENING,
            iconName = "bolt",
            summary = "Floodwater conducts lethal electrical currents over dozens of meters. Submerged transformers can electrify whole streets.",
            doSteps = listOf(
                "Assume ALL downed or submerged electrical wires are live and deadly.",
                "Stay at least 15 meters (50 feet) away from any downed wire or water touching a wire.",
                "If electricity tingles your feet while wading, STOP! Keep feet touching together, hop on one foot or shuffle feet without lifting them apart.",
                "If a wire falls on your vehicle: STAY INSIDE until power authorities confirm the line is dead.",
                "If vehicle catches fire: JUMP clear with both feet together without touching vehicle and ground at the same time."
            ),
            doNotSteps = listOf(
                "DO NOT step into water near fallen poles or electric meters.",
                "DO NOT try to lift wires with wooden sticks or poles (wet wood conducts!).",
                "DO NOT stand in water while flipping circuit breakers inside home."
            ),
            multiLangTranslations = mapOf(
                "ta" to "மின்கம்பி ஆபத்து: தண்ணீரில் விழுந்த மின்கம்பிகளை தொடாதீர்கள். 15 மீட்டர் தூரம் விலகி இருங்கள். கால்களில் மின் அதிர்ச்சி உணர்ந்தால் கால்களை சேர்த்து வைத்து தத்தி நகரவும்.",
                "te" to "విద్యుత్ తీగల ప్రమాదం: నీటిలో పడిన కరెంట్ తీగలకు 15 మీటర్ల దూరంలో ఉండండి. షాక్ తగిలినట్లు అనిపిస్తే కాళ్లను కలిపి ఉంచి గెంతుతూ వెళ్ళండి.",
                "kn" to "ವಿದ್ಯುತ್ ತಂತಿ ಅಪಾಯ: ನೀರಿನಲ್ಲಿ ಬಿದ್ದಿರುವ ವಿದ್ಯುತ್ ತಂತಿಗಳಿಂದ 15 ಮೀಟರ್ ದೂರವಿರಿ. ಜುಮ್ಮೆನಿಸಿದರೆ ಕಾಲುಗಳನ್ನು ಜೋಡಿಸಿ ನೆಗೆಯುತ್ತಾ ದೂರ ಸರಿಯಿರಿ.",
                "ml" to "വൈദ്യുത ലൈൻ അപകടം: വെള്ളത്തിൽ കിടക്കുന്ന വൈദ്യുത കമ്പികളിൽ നിന്ന് 15 മീറ്റർ അകലം പാലിക്കുക. ഷോക്ക് അടിക്കുന്നതായി തോന്നിയാൽ കാൽ ഒന്നിച്ച് വെച്ച് ചാടി മാറുക.",
                "hi" to "पानी में गिरे बिजली के तार: जानलेवा खतरा। कम से कम 15 मीटर दूर रहें। अगर कार पर तार गिरे तो कार के अंदर ही रहें।"
            )
        ),
        TriageGuideItem(
            id = "cpr_drowning",
            category = "Medical & Hazards",
            title = "CPR & Drowning Rescue Protocol",
            urgencyLevel = UrgencyLevel.LIFE_THREATENING,
            iconName = "medical",
            summary = "For unconscious, non-breathing victims pulled from floodwater. Immediate CPR restores oxygen to the brain.",
            doSteps = listOf(
                "Check responsiveness: Tap shoulders firmly and shout 'Are you okay?'. Check for normal breathing for no more than 10 seconds.",
                "Lay victim flat on their back on a firm, dry surface if possible.",
                "Deliver 5 initial rescue breaths: Tilt head back, pinch nose, blow firmly into mouth until chest rises.",
                "Start Chest Compressions: Place heel of hand on center of chest. Push hard and fast at 100-120 beats per minute, 2 inches deep.",
                "Cycle: 30 compressions followed by 2 rescue breaths until spontaneous breathing returns or medics arrive.",
                "If breathing resumes, roll victim onto their side in Recovery Position to prevent choking on fluids."
            ),
            doNotSteps = listOf(
                "DO NOT waste time trying to drain water from lungs with the Heimlich maneuver.",
                "DO NOT delay CPR to look for a pulse if victim is limp and not breathing normally.",
                "DO NOT leave an unconscious victim unattended on their back."
            ),
            multiLangTranslations = mapOf(
                "ta" to "சிபிஆர் முதலுதவி: மூச்சு விடாத நபருக்கு மார்பின் நடுவில் 30 முறை வேகமாக அழுத்தவும், 2 முறை வாயோடு வாய் வைத்து மூச்சுக் காற்று ஊதவும். தொடர்ந்து செய்யவும்.",
                "te" to "సీపీఆర్ ప్రథమ చికిత్స: శ్వాస ఆడని వ్యక్తి ఛాతీ మధ్యలో నిమిషానికి 100-120 సార్లు వేగంగా నొక్కండి. 30 సార్లు ఛాతీ నొక్కి 2 సార్లు నోటి ద్వారా గాలిని ఊదండి.",
                "kn" to "ಸಿಪಿಆರ್ ಪ್ರಥಮ ಚಿಕಿತ್ಸೆ: ಉಸಿರಾಡದ ವ್ಯಕ್ತಿಯ ಎದೆಯ ಮಧ್ಯಭಾಗವನ್ನು ನಿಮಿಷಕ್ಕೆ 100-120 ಬಾರಿ ವೇಗವಾಗಿ ಒತ್ತಿ. 30 ಬಾರಿ ಒತ್ತಿದ ನಂತರ 2 ಬಾರಿ ಕೃತಕ ಉಸಿರು ನೀಡಿ.",
                "ml" to "സിപിആർ പ്രഥമശുശ്രൂഷ: ശ്വാസമില്ലാത്ത വ്യക്തിക്ക് നെഞ്ചിന്റെ മധ്യഭാഗത്ത് 30 തവണ വേഗത്തിൽ അമർത്തുക, തുടർന്ന് 2 തവണ വായിലേക്ക് ശ്വാസം നൽകുക.",
                "hi" to "सीपीआर: यदि व्यक्ति सांस नहीं ले रहा है, तो छाती के बीच में 30 बार जोर से दबाएं और 2 बार मुंह से सांस दें। बचाव दल आने तक जारी रखें।"
            )
        ),
        TriageGuideItem(
            id = "severe_bleeding",
            category = "Medical & Hazards",
            title = "Severe Bleeding & Flood Debris Lacerations",
            urgencyLevel = UrgencyLevel.CRITICAL,
            iconName = "medical",
            summary = "Hidden metal sheets, glass, and rebar in murky floodwaters cause deep arterial cuts that can bleed out rapidly.",
            doSteps = listOf(
                "Apply DIRECT, FIRM PRESSURE immediately on the wound with a clean cloth, towel, or gloved hand.",
                "Maintain continuous hard pressure for at least 10 minutes without lifting to peek.",
                "Elevate the injured limb above heart level while maintaining firm pressure.",
                "Apply a tight pressure dressing with a roll of gauze or cloth wrapped firmly around the wound.",
                "If bleeding is spurting or uncontrollable on limb: Apply a commercial tourniquet or improvised band 2-3 inches above wound (tighten until bleeding stops)."
            ),
            doNotSteps = listOf(
                "DO NOT wash heavy bleeding wounds under contaminated raw floodwater.",
                "DO NOT remove deeply embedded objects (glass/rebar) — stabilize around the object to prevent catastrophic bleeding.",
                "DO NOT release direct pressure prematurely."
            ),
            multiLangTranslations = mapOf(
                "ta" to "கடுமையான இரத்தப்போக்கு: சுத்தமான துணியை வைத்து காயத்தின் மீது 10 நிமிடம் உறுதியாக அழுத்தவும். உறுப்பை உயர்த்தி வைக்கவும். இரத்தம் நிற்கவில்லை என்றால் மேலே இறுக்கி கட்டவும்.",
                "te" to "తీవ్రమైన రక్తస్రావం: శుభ్రమైన గుడ్డతో గాయంపై 10 నిమిషాలు గట్టిగా అదిమి ఉంచండి. అవయవాన్ని పైకి ఎత్తండి. రక్తం ఆగకపోతే గాయం పైన గట్టిగా కట్టండి.",
                "kn" to "ರಕ್ತಸ್ರಾವ ನಿಯಂತ್ರಣ: ಸ್ವಚ್ಛ ಬಟ್ಟೆಯಿಂದ ಗಾಯದ ಮೇಲೆ 10 ನಿಮಿಷಗಳ ಕಾಲ ಗಟ್ಟಿಯಾಗಿ ಒತ್ತಿ ಹಿಡಿಯಿರಿ. ಅಂಗವನ್ನು ಮೇಲೆತ್ತಿ. ರಕ್ತ ನಿಲ್ಲದಿದ್ದರೆ ಗಾಯದ ಮೇಲೆ ಕಟ್ಟಿ.",
                "ml" to "രക്തസ്രാവം തടയാൻ: വൃത്തിയുള്ള തുണി ഉപയോഗിച്ച് മുറിവിൽ 10 മിനിറ്റ് തുടർച്ചയായി അമർത്തിപ്പിടിക്കുക. മുറിവ് ഹൃദയത്തിന് മുകളിലായി ഉയർത്തി വെക്കുക.",
                "hi" to "गंभीर रक्तस्राव: घाव पर साफ कपड़े से कम से कम 10 मिनट तक लगातार जोर से दबाए रखें। अंग को ऊपर उठाएं।"
            )
        ),
        TriageGuideItem(
            id = "waterborne_cholera",
            category = "Sanitation & Water",
            title = "Waterborne Epidemics (Cholera, Typhoid, Leptospirosis)",
            urgencyLevel = UrgencyLevel.CRITICAL,
            iconName = "water",
            summary = "Floodwaters mixed with sewage carry deadly Vibrio cholerae, Salmonella, and Leptospira (rat urine fever).",
            doSteps = listOf(
                "Prepare Homemade ORS (Oral Rehydration Salts): 1 Liter boiled water + 6 level teaspoons Sugar + 1/2 level teaspoon Salt. Drink continuously.",
                "Wash hands thoroughly with soap or alcohol sanitizer before eating any food.",
                "Eat only thoroughly boiled or packaged foods; discard all unsealed foods touched by floodwater.",
                "Protect cuts with waterproof dressings to prevent rat-urine Leptospirosis infection through skin.",
                "If high fever, intense jaundice (yellow eyes), or rice-water diarrhea begins, seek immediate doxycycline/antibiotics at emergency clinics."
            ),
            doNotSteps = listOf(
                "DO NOT wade bare-footed in stagnant floodwater (leptospirosis penetrates skin).",
                "DO NOT stop fluid intake if diarrhea occurs — dehydration kills within 24 hours.",
                "DO NOT consume cut fruits or salads prepared with floodwater."
            ),
            multiLangTranslations = mapOf(
                "ta" to "காலரா மற்றும் எலிக் காய்ச்சல் தடுப்பு: 1 லிட்டர் கொதித்த நீரில் 6 தேக்கரண்டி சர்க்கரை, அரை தேக்கரண்டி உப்பு சேர்த்து குடிக்கவும். தேங்கிய நீரில் வெறுங்காலுடன் நடக்காதீர்கள்.",
                "te" to "కలరా మరియు ఎలుక జ్వరం నివారణ: 1 లీటరు మరిగించిన నీటిలో 6 చెంచాల చక్కెర, అర చెంచా ఉప్పు కలిపి ఓఆర్ఎస్ ద్రావణం తాగండి. నిల్వ ఉన్న నీటిలో నడవకండి.",
                "kn" to "ಕಾಲರಾ ಮತ್ತು ಸಾಂಕ್ರಾಮಿಕ ರೋಗ ತಡೆ: 1 ಲೀಟರ್ ಕುದಿಸಿದ ನೀರಿಗೆ 6 ಚಮಚ ಸಕ್ಕರೆ ಮತ್ತು ಅರ್ಧ ಚಮಚ ಉಪ್ಪು ಬೆರೆಸಿ ಕುಡಿಯಿರಿ. ಕಲುಷಿತ ನೀರಿನಲ್ಲಿ ಬರಿಗಾಲಿನಲ್ಲಿ ನಡೆಯಬೇಡಿ.",
                "ml" to "എലിപ്പനിയും കോളറയും തടയാൻ: 1 ലിറ്റർ തിളപ്പിച്ച വെള്ളത്തിൽ 6 സ്പൂൺ പഞ്ചസാരയും അര സ്പൂൺ ഉപ്പും ചേർത്ത് ഒആർഎസ് ഉണ്ടാക്കി കുടിക്കുക. കെട്ടിക്കിടക്കുന്ന വെള്ളത്തിൽ ഇറങ്ങരുത്.",
                "hi" to "हैजा और जलजनित रोग: ओआरएस घोल (1 लीटर पानी + 6 चम्मच चीनी + आधा चम्मच नमक) पिएं। गंदे पानी में नंगे पैर न चलें।"
            )
        ),
        TriageGuideItem(
            id = "helicopter_signaling",
            category = "Rescue Signals",
            title = "Rooftop & Signal Helicopter Rescue Protocol",
            urgencyLevel = UrgencyLevel.LIFE_THREATENING,
            iconName = "roof",
            summary = "How to guide search-and-rescue helicopters and boats directly to your exact coordinates.",
            doSteps = listOf(
                "Display Ground-to-Air Symbols: Lay brightly colored tarps or paint a large 'V' (Require Assistance) or 'X' (Require Medical) on the roof.",
                "Signal with Light: At night, use continuous flashlight strobe or 3 short flashes. During day, reflect sunlight with a mirror or smartphone screen.",
                "Whistle Signaling: Standard international distress signal is 3 SHARP BLASTS, pause for 1 minute, repeat.",
                "When helicopter hovers: Secure all loose sheets, plastic, and tarps so rotor wash does not blow them into aircraft engines.",
                "Wait for the winchman/cable to touch the ground BEFORE you touch it (discharges static electricity safely)."
            ),
            doNotSteps = listOf(
                "DO NOT shine laser pointers or high-power searchlights into the cockpit pilots' eyes.",
                "DO NOT tie the rescue hoist cable to your roof, boat, or railing (aircraft will capsize or crash).",
                "DO NOT panic or rush the hoist basket — listen strictly to crew chief instructions."
            ),
            multiLangTranslations = mapOf(
                "ta" to "ஹெலிகாப்டர் மீட்பு சைகை: கூரையில் மஞ்சள்/ஆரஞ்சு துணியை விரிக்கவும். கண்ணாடியால் சூரிய ஒளியை பிரதிபலிக்கவும். விசிலை 3 முறை ஊதவும். மீட்புக் கயிறு தரையில் பட்ட பிறகே தொடவும்.",
                "te" to "హెలికాప్టర్ రక్షణ సంకేతాలు: పైకప్పుపై ప్రకాశవంతమైన రంగు వస్త్రాన్ని పరచండి. అద్దంతో సూర్యకాంతిని పరావర్తనం చెందించండి. 3 సార్లు విజిల్ వేయండి. కేబుల్ నేలను తాకిన తర్వాతే పట్టుకోండి.",
                "kn" to "ಹೆಲಿಕಾಪ್ಟರ್ ರಕ್ಷಣಾ ಸಂಕೇತ: ಸೂರಿನ ಮೇಲೆ ಪ್ರಕಾಶಮಾನವಾದ ಬಟ್ಟೆ ಹಾಸಿ. ಕನ್ನಡಿಯಿಂದ ಸೂರ್ಯನ ಬೆಳಕು ಪ್ರತಿಫಲಿಸಿ. 3 ಬಾರಿ ಸೀಟಿ ಊದಿ. ರಕ್ಷಣಾ ಹಗ್ಗ ನೆಲ ತಾಗಿದ ನಂತರವೇ ಮುಟ್ಟಿ.",
                "ml" to "ഹെലികോപ്റ്റർ രക്ഷാപ്രവർത്തനം: മേൽക്കൂരയിൽ തിളക്കമുള്ള തുണി വിരിക്കുക. കണ്ണാടി ഉപയോഗിച്ച് വെളിച്ചം അടിക്കുക. 3 തവണ വിസിൽ ഊതുക. റോപ്പ് നിലത്ത് തട്ടിയ ശേഷം മാത്രം തൊടുക.",
                "hi" to "हेलीकॉप्टर संकेत: छत पर चमकीला कपड़ा बिछाएं या आईने से धूप चमकाएं। सीटी को 3 बार बजाएं। रस्सी के जमीन छूने के बाद ही उसे पकड़ें।"
            )
        ),
        TriageGuideItem(
            id = "emergency_childbirth",
            category = "Medical & Hazards",
            title = "Emergency Childbirth in Flood Isolation",
            urgencyLevel = UrgencyLevel.LIFE_THREATENING,
            iconName = "medical",
            summary = "When an expectant mother goes into labor while cut off from hospitals by floodwaters.",
            doSteps = listOf(
                "Prepare a warm, dry, clean space with clean plastic sheets and freshly washed towels.",
                "Boil water to sanitize hands, scissors, and clean string or shoelaces (to tie umbilical cord).",
                "Encourage mother to rest between contractions; deliver baby gently as head crowns without pulling.",
                "Immediately dry the newborn thoroughly, wrap in dry warm cloth, and place skin-to-skin on mother's chest to prevent hypothermia.",
                "Wait for cord to stop pulsating (3-5 min), then tie string tightly in 2 places (3 inches and 5 inches from baby's navel). Cut between ties ONLY with sterilized scissors."
            ),
            doNotSteps = listOf(
                "DO NOT pull on the baby or umbilical cord during delivery.",
                "DO NOT wash off the protective white coating (vernix) from the newborn's skin.",
                "DO NOT pull on the cord to deliver placenta — allow it to come out naturally."
            ),
            multiLangTranslations = mapOf(
                "ta" to "அவசர பிரசவ உதவி: தாயை கதகதப்பான உலர் இடத்தில் படுக்க வைக்கவும். குழந்தை பிறந்தவுடன் உடனடியாக உலர்த்தி தாயின் மார்பில் வைக்கவும். தொப்புள் கொடியை இழுக்க வேண்டாம்.",
                "te" to "అత్యవసర ప్రసవ సహాయం: పొడిగా, వెచ్చగా ఉన్న ప్రదేశాన్ని సిద్ధం చేయండి. శిశువు జన్మించిన వెంటనే తడి తుడిచి తల్లి రొమ్ముపై ఉంచండి. బొడ్డు తాడును లాగవద్దు.",
                "kn" to "ತುರ್ತು ಹೆರಿಗೆ ನೆರವು: ಬೆಚ್ಚಗಿನ, ಸ್ವಚ್ಛವಾದ ಸ್ಥಳವನ್ನು ಸಿದ್ಧಪಡಿಸಿ. ಮಗು ಜನಿಸಿದ ತಕ್ಷಣ ಒರೆಸಿ ತಾಯಿಯ ಎದೆಗೆ ತಾಗಿಸಿ ಬೆಚ್ಚಗಿಡಿ. ಹೊಕ್ಕಳು ಬಳ್ಳಿಯನ್ನು ಎಳೆಯಬೇಡಿ.",
                "ml" to "അടിയന്തര പ്രസവ സഹായം: വൃത്തിയുള്ളതും ചൂടുള്ളതുമായ സ്ഥലം ഒരുക്കുക. കുട്ടി ജനിച്ചയുടൻ തുണി കൊണ്ട് തുടച്ച് അമ്മയുടെ നെഞ്ചോട് ചേർത്ത് കിടത്തുക.",
                "hi" to "आपातकालीन प्रसव: गर्म और सूखी जगह तैयार करें। बच्चे के जन्म के तुरंत बाद उसे पोंछकर मां के सीने पर रखें। गर्भनाल को न खींचें।"
            )
        ),
        TriageGuideItem(
            id = "gas_chemical_hazards",
            category = "Power & Gas",
            title = "Gas Leaks & Chemical Flood Runoff",
            urgencyLevel = UrgencyLevel.LIFE_THREATENING,
            iconName = "hazard",
            summary = "Floods dislodge underground gas pipelines, propane cylinders, and industrial chemical drums.",
            doSteps = listOf(
                "If you smell rotten eggs or hear hissing gas: EVACUATE IMMEDIATELY to upwind higher ground.",
                "Turn off the main gas valve at the meter ONLY if accessible without wading through deep water.",
                "Leave all light switches, appliances, and flashlights unchanged (flipping switches generates ignition sparks).",
                "Keep away from iridescent oil slicks or chemical-smelling discolored runoff.",
                "Warn neighbors by shouting rather than ringing electric doorbells."
            ),
            doNotSteps = listOf(
                "DO NOT strike matches, lighters, or use candles for lighting.",
                "DO NOT start motorboat engines inside enclosed flooded garages or near gas leaks.",
                "DO NOT enter homes until natural gas smell has completely dissipated."
            ),
            multiLangTranslations = mapOf(
                "ta" to "எரிவாயு கசிவு மற்றும் ரசாயன ஆபத்து: அழுகிய முட்டை வாசனை வந்தால் உடனே காற்று வீசும் திசைக்கு வெளியேறவும். தீக்குச்சி அல்லது சுவிட்சுகளை போடாதீர்கள்.",
                "te" to "గ్యాస్ లీకేజీ ప్రమాదం: కుళ్ళిన గుడ్డు వాసన వస్తే వెంటనే అక్కడి నుండి బయటకు వెళ్లండి. అగ్గిపుల్లలు లేదా విద్యుత్ స్విచ్‌లను ఆన్ చేయవద్దు.",
                "kn" to "ಅನಿಲ ಸೋರಿಕೆ ಎಚ್ಚರಿಕೆ: ಕೊಳೆತ ಮೊಟ್ಟೆಯ ವಾಸನೆ ಬಂದರೆ ತಕ್ಷಣ ಗಾಳಿಯ ದಿಕ್ಕಿಗೆ ದೂರ ಸರಿಯಿರಿ. ಯಾವುದೇ ಬೆಂಕಿ ಅಥವಾ ಸ್ವಿಚ್‌ಗಳನ್ನು ಆನ್ ಮಾಡಬೇಡಿ.",
                "ml" to "ഗ്യാസ് ചോർച്ച അപകടം: ചീഞ്ഞ മുട്ടയുടെ മണം വന്നാൽ ഉടൻ സുരക്ഷിതമായ ഉയർന്ന സ്ഥലത്തേക്ക് മാറുക. തീപ്പെട്ടി കത്തിക്കുകയോ സ്വിച്ചുകൾ ഇടുകയോ ചെയ്യരുത്.",
                "hi" to "गैस रिसाव का खतरा: सड़े अंडे जैसी गंध आने पर तुरंत सुरक्षित स्थान पर जाएं। माचिस या बिजली का स्विच न चलाएं।"
            )
        ),
        TriageGuideItem(
            id = "hypothermia_cold_water",
            category = "Medical & Hazards",
            title = "Hypothermia & Cold Water Immersion",
            urgencyLevel = UrgencyLevel.CRITICAL,
            iconName = "medical",
            summary = "Prolonged immersion in floodwaters saps body heat 25x faster than cold air, leading to fatal cardiac arrest.",
            doSteps = listOf(
                "Remove all wet clothing immediately; dry skin with clean towels or dry cloth.",
                "Insulate body: Layer dry clothes, wrap in plastic trash bags or emergency space blankets to trap radiant body heat.",
                "Warm the body CORE first (chest, neck, groin) with warm water bottles wrapped in cloth, not extremities.",
                "Give warm, sweet drinks (sugared tea or warm ORS) ONLY if patient is conscious and swallowing easily.",
                "Keep patient lying flat and still; avoid sudden violent movements which can trigger arrhythmia."
            ),
            doNotSteps = listOf(
                "DO NOT give alcoholic drinks or caffeine (causes rapid heat loss).",
                "DO NOT rub or massage cold extremities (pushes cold, acid-laden blood to the heart).",
                "DO NOT place victim directly in hot water or next to radiant fire (causes thermal shock)."
            ),
            multiLangTranslations = mapOf(
                "ta" to "குளிர் தாக்கம் (ஹைபோதெர்மியா): ஈரமான ஆடைகளை உடனே கழற்றி உலர்த்தவும். உலர் போர்வைகளால் போர்த்தி மார்பை கதகதப்பாக வைக்கவும். மதுபானம் கொடுக்காதீர்கள்.",
                "te" to "శరీర ఉష్ణోగ్రత పడిపోవడం: తడి బట్టలను వెంటనే తొలగించి పొడి బట్టలు వేయండి. ఛాతీ భాగాన్ని వెచ్చగా ఉంచండి. కాళ్లు చేతులు రుద్దవద్దు.",
                "kn" to "ತೀವ್ರ ಶೀತ ಬಾಧೆ: ಒದ್ದೆಯಾದ ಬಟ್ಟೆಗಳನ್ನು ತಕ್ಷಣ ತೆಗೆದು ಒಣ ಬಟ್ಟೆ ಹೊದಿಸಿ. ಎದೆಯ ಭಾಗವನ್ನು ಬೆಚ್ಚಗಿಡಿ. ಬಿಸಿ ನೀರಿನಲ್ಲಿ ನೇರವಾಗಿ ಇಡಬೇಡಿ.",
                "ml" to "ശരീരം തണുത്തുറയൽ: നനഞ്ഞ വസ്ത്രങ്ങൾ മാറ്റി ഉണങ്ങിയ വസ്ത്രങ്ങൾ ധരിപ്പിക്കുക. നെഞ്ചിലും കഴുത്തിലും ചൂട് നൽകുക. മദ്യം നൽകരുത്.",
                "hi" to "हाइपोथर्मिया (ठंड लगना): गीले कपड़े तुरंत उतारें और सूखे कंबल लपेटें। सीने को गर्म रखें। शराब या कैफीन न दें।"
            )
        ),
        TriageGuideItem(
            id = "car_submerged_escape",
            category = "Vehicle Hazards",
            title = "Car Sinking or Swept Away in Flash Flood",
            urgencyLevel = UrgencyLevel.LIFE_THREATENING,
            iconName = "car",
            summary = "Just 1 foot (30 cm) of water floats cars; 2 feet sweeps SUVs. Escape during the golden 30-60 seconds.",
            doSteps = listOf(
                "Stay Calm & Act Fast: You have 30 to 60 seconds before electrical systems short out.",
                "Unbuckle all seatbelts immediately (adults first, then quickly unbuckle children).",
                "Roll down power windows IMMEDIATELY while electrical circuits still function.",
                "If windows fail: Break side window using metal headrest prongs or emergency glass punch in the bottom corner.",
                "Climb out window onto the roof of the car and signal emergency rescuers."
            ),
            doNotSteps = listOf(
                "DO NOT waste precious seconds trying to push open car doors against intense outside water pressure.",
                "DO NOT attempt to retrieve luggage, bags, or laptop cases.",
                "DO NOT drive past road barricades or into flooded underpasses."
            ),
            multiLangTranslations = mapOf(
                "ta" to "காரில் வெள்ளம் மூழ்கும் போது: முதல் 30 வினாடிகளில் இருக்கை பெல்ட்டை கழற்றி, உடனடியாக ஜன்னல் கண்ணாடிகளை இறக்கவும். ஜன்னல் வழியாக கூரைக்கு தப்பவும்.",
                "te" to "కారు నీటిలో మునిగినప్పుడు: సీట్ బెల్ట్ తొలగించి వెంటనే కిటికీ అద్దాలను కిందకు దించండి. తలుపు తెరవడానికి ప్రయత్నించకండి. కిటికీ ద్వారా బయటకు రండి.",
                "kn" to "ಕಾರು ಮುಳುಗುವಾಗ: ಸೀಟ್ ಬೆಲ್ಟ್ ತೆಗೆದು ತಕ್ಷಣ ಕಿಟಕಿ ಗ್ಲಾಸ್ ಇಳಿಸಿ. ಕಿಟಕಿಯ ಮೂಲಕ ಹೊರಬಂದು ಕಾರಿನ ಮೇಲ್ಛಾವಣಿ ಏರಿ ಸಹಾಯ ಕೋರಿ.",
                "ml" to "കാർ വെള്ളത്തിൽ മുങ്ങുമ്പോൾ: ഉടൻ സീറ്റ് ബെൽറ്റ് അഴിക്കുക. വിൻഡോ ഗ്ലാസ് താഴ്ത്തി അതിലൂടെ പുറത്തിറങ്ങി കാറിന്റെ മുകളിൽ കയറുക.",
                "hi" to "डूबती कार से बचाव: पहले 30 सेकंड में सीट बेल्ट खोलें, तुरंत खिड़कियां नीचे करें और खिड़की से निकलकर कार की छत पर जाएं।"
            )
        ),
        TriageGuideItem(
            id = "building_collapse_rubble",
            category = "Structural Safety",
            title = "Building Collapse & Trapped Under Debris",
            urgencyLevel = UrgencyLevel.LIFE_THREATENING,
            iconName = "hazard",
            summary = "Flood erosion undermines building foundations causing sudden structural collapse.",
            doSteps = listOf(
                "Cover mouth and nose with a cloth, handkerchief, or clothing to prevent choking on cement dust.",
                "Protect vital organs: Curl into fetal position beside a sturdy void (bedframe, heavy desk, concrete column).",
                "Signal with sound: TAP ON PIPES OR WALLS IN RHYTHMS OF THREE (Tap-Tap-Tap) so rescue sonar can pinpoint you.",
                "Shout ONLY when you hear rescue crews directly overhead (saves oxygen and prevents swallowing lethal dust).",
                "Stay completely still to conserve energy and body water."
            ),
            doNotSteps = listOf(
                "DO NOT light matches or lighters (broken gas lines under rubble cause fatal explosions).",
                "DO NOT kick violently or struggle if pinned (triggers secondary debris collapse).",
                "DO NOT scream continuously — exhaust yourself only when rescuers are listening."
            ),
            multiLangTranslations = mapOf(
                "ta" to "கட்டிட இடிபாடுகளில் சிக்கினால்: மூக்கை துணியால் மூடவும். குழாய்கள் அல்லது சுவரில் மூன்று முறை தட்டி சத்தம் எழுப்பவும். தீக்குச்சி எரிக்காதீர்கள்.",
                "te" to "భవనం కూలిపోయినప్పుడు: ముక్కును గుడ్డతో కప్పుకోండి. పైపులపై మూడుసార్లు తట్టి రక్షకులకు శబ్దం వినిపించండి. అగ్గిపుల్ల వెలిగించవద్దు.",
                "kn" to "ಕಟ್ಟಡ ಕುಸಿದಾಗ: ಬಟ್ಟೆಯಿಂದ ಮೂಗು ಮುಚ್ಚಿಕೊಳ್ಳಿ. ಪೈಪ್ ಅಥವಾ ಗೋಡೆಯ ಮೇಲೆ ಮೂರು ಬಾರಿ ತಟ್ಟಿ ಶಬ್ದ ಮಾಡಿ. ಬೆಂಕಿ ಹಚ್ಚಬೇಡಿ.",
                "ml" to "കെട്ടിടം തകർന്നു വീണാൽ: മൂക്കും വായയും തുണി കൊണ്ട് മൂടുക. പൈപ്പുകളിൽ മൂന്ന് തവണ തട്ടി ശബ്ദമുണ്ടാക്കുക. തീപ്പെട്ടി കത്തിക്കരുത്.",
                "hi" to "इमारत मलबे में फंसने पर: मुंह पर कपड़ा रखें। पाइप या दीवार पर 3 बार ठक-ठक करके संकेत दें। माचिस न जलाएं।"
            )
        ),
        TriageGuideItem(
            id = "trench_foot_infections",
            category = "Sanitation & Water",
            title = "Trench Foot & Fungal Floodwater Necrosis",
            urgencyLevel = UrgencyLevel.IMPORTANT_PREP,
            iconName = "medical",
            summary = "Standing in cold, dirty floodwater causes tissue breakdown, severe bacterial ulcers, and nerve damage.",
            doSteps = listOf(
                "Wash feet daily with clean boiled water and antibacterial soap.",
                "Dry thoroughly between all toes; air dry feet elevated above ground whenever resting.",
                "Apply clean, dry socks. If dry socks are unavailable, wrap feet in dry cotton cloth inside plastic bags.",
                "Apply petroleum jelly (Vaseline) or coconut oil to create a moisture-repellent barrier before entering water.",
                "Elevate feet above heart level for 30 minutes twice daily to restore peripheral blood circulation."
            ),
            doNotSteps = listOf(
                "DO NOT sleep in wet socks or soaked shoes.",
                "DO NOT ignore white, wrinkled, numb, or painful tingling skin on feet.",
                "DO NOT scratch itchy blisters — causes rapid secondary bacterial cellulitis."
            ),
            multiLangTranslations = mapOf(
                "ta" to "பாத தொற்று பாதுகாப்பு: கால்களை சுத்தமான நீரால் கழுவி நன்கு உலர்த்தவும். தேங்காய் எண்ணெய் அல்லது வாஸ்லின் தடவவும். ஈரமான காலுறையுடன் தூங்காதீர்கள்.",
                "te" to "పాదాల ఇన్ఫెక్షన్ నివారణ: కాళ్లను శుభ్రమైన నీటితో కడిగి ఆరబెట్టండి. కొబ్బరి నూనె లేదా వాసెలిన్ రాయండి. తడి సాక్సులతో పడుకోవద్దు.",
                "kn" to "ಪಾದದ ಸೋಂಕು ತಡೆ: ಕಾಲುಗಳನ್ನು ಸ್ವಚ್ಛ ನೀರಿನಿಂದ ತೊಳೆದು ಒಣಗಿಸಿ. ತೆಂಗಿನೆಣ್ಣೆ ಅಥವಾ ವ್ಯಾಸಲಿನ್ ಹಚ್ಚಿ. ಒದ್ದೆ ಸಾಕ್ಸ್ ಧರಿಸಬೇಡಿ.",
                "ml" to "പാദ സംരക്ഷണം: കാലുകൾ വൃത്തിയുള്ള വെള്ളത്തിൽ കഴുകി തുടക്കുക. വെളിച്ചെണ്ണയോ വാസ്ലിനോ പുരട്ടുക. നനഞ്ഞ സോക്സുകൾ ഉപയോഗിക്കരുത്.",
                "hi" to "पैरों में संक्रमण से बचाव: पैरों को साफ पानी से धोकर सुखाएं। नारियल तेल या वैसलीन लगाएं। गीले मोजे न पहनें।"
            )
        ),
        TriageGuideItem(
            id = "fracture_splinting",
            category = "Medical & Hazards",
            title = "Broken Bones & Fracture Splinting with Debris",
            urgencyLevel = UrgencyLevel.CRITICAL,
            iconName = "medical",
            summary = "Slips, falling debris, and collapsing stairs cause severe fractures. Immobilization prevents severed blood vessels.",
            doSteps = listOf(
                "Check for circulation: Check if toes or fingers beyond the fracture are warm, pink, and have feeling.",
                "Immobilize the joint ABOVE and the joint BELOW the broken bone.",
                "Improvise a rigid splint using wooden planks, sturdy tree branches, rolled umbrellas, or folded cardboard.",
                "Pad the splint with clean cloth, towels, or shirts to prevent pressure sores.",
                "Secure with cloth strips, belts, or torn fabric tied firmly but NOT cutting off blood circulation."
            ),
            doNotSteps = listOf(
                "DO NOT attempt to push protruding broken bones back under the skin.",
                "DO NOT attempt to straighten a deformed joint or bone forcefully.",
                "DO NOT tie bindings directly over the fracture site."
            ),
            multiLangTranslations = mapOf(
                "ta" to "எலும்பு முறிவு முதலுதவி: உடைந்த எலும்பை நேராக்க முயற்சிக்காதீர்கள். மரப்பலகை அல்லது கிளையை வைத்து துணியால் அசைக்காமல் கட்டவும்.",
                "te" to "ఎముక విరిగినప్పుడు: విరిగిన భాగాన్ని సరిచేయడానికి లాగవద్దు. కర్ర లేదా చెక్క పలకను పెట్టి కదలకుండా గుడ్డతో కట్టండి.",
                "kn" to "ಮೂಳೆ ಮುರಿತ ಚಿಕಿತ್ಸೆ: ಮುರಿದ ಮೂಳೆಯನ್ನು ಬಲವಂತವಾಗಿ ಸರಿಪಡಿಸಬೇಡಿ. ಮರದ ಹಲಗೆ ಅಥವಾ ಕೋಲು ಇಟ್ಟು ಬಟ್ಟೆಯಿಂದ ಗಟ್ಟಿಯಾಗಿ ಕಟ್ಟಿ.",
                "ml" to "എല്ല് പൊട്ടിയാൽ: വളഞ്ഞ എല്ല് നേരെയാക്കാൻ ശ്രമിക്കരുത്. മരക്കഷണമോ വടിയോ വെച്ച് തുണി കൊണ്ട് അനങ്ങാതെ കെട്ടുക.",
                "hi" to "हड्डी टूटने पर: टूटी हड्डी को सीधा करने की कोशिश न करें। लकड़ी या पटरी रखकर कपड़े से स्थिर बांधें।"
            )
        ),
        TriageGuideItem(
            id = "generator_carbon_monoxide",
            category = "Power & Gas",
            title = "Generator Exhaust & Carbon Monoxide Poisoning",
            urgencyLevel = UrgencyLevel.LIFE_THREATENING,
            iconName = "hazard",
            summary = "Carbon monoxide is invisible, odorless, and kills in minutes. More flood casualties occur from generators than water.",
            doSteps = listOf(
                "Place generators OUTDOORS ONLY, at least 20 feet (6 meters) away from windows, doors, and vents.",
                "Point the exhaust muffler away from homes and neighboring buildings.",
                "If anyone experiences dizziness, headache, nausea, or sleepiness, evacuate them into outdoor fresh air IMMEDIATELY.",
                "Keep a battery-operated carbon monoxide alarm inside your shelter.",
                "Let generator cool for 15 minutes before adding petrol/diesel to avoid explosive vapor fire."
            ),
            doNotSteps = listOf(
                "NEVER run a generator inside a home, garage, basement, covered porch, or tent.",
                "DO NOT plug generator directly into a wall socket (backfeeding kills line workers).",
                "DO NOT refuel while engine is running or hot."
            ),
            multiLangTranslations = mapOf(
                "ta" to "ஜெனரேட்டர் பாதுகாப்பு: வீட்டின் உள்ளே அல்லது பால்கனியில் இயக்காதீர்கள்! ஜன்னல்களிலிருந்து 6 மீட்டர் தொலைவில் திறந்தவெளியில் வைக்கவும்.",
                "te" to "జనరేటర్ కార్బన్ మోనాక్సైడ్ ప్రమాదం: ఇంట్లో లేదా గ్యారేజీలో ఎప్పుడూ నడపకండి. కిటికీలకు 6 మీటర్ల దూరంలో ఆరుబయట ఉంచండి.",
                "kn" to "ಜನರೇಟರ್ ಅಪಾಯ ತಡೆ: ಮನೆಯೊಳಗೆ ಅಥವಾ ಬಾಲ್ಕನಿಯಲ್ಲಿ ಬಳಸಬೇಡಿ. ಕಿಟಕಿಗಳಿಂದ 6 ಮೀಟರ್ ದೂರದಲ್ಲಿ ಬಯಲಿನಲ್ಲಿಡಿ.",
                "ml" to "ജനറേറ്റർ സുരക്ഷ: വീടിനുള്ളിലോ ബാൽക്കണിയിലോ പ്രവർത്തിപ്പിക്കരുത്. ജനലുകളിൽ നിന്ന് 6 മീറ്റർ അകലെ തുറസ്സായ സ്ഥലത്ത് വെക്കുക.",
                "hi" to "जनरेटर सुरक्षा: घर या गैरेज के अंदर कभी न चलाएं। खिड़कियों से कम से कम 6 मीटर दूर बाहर रखें।"
            )
        )
    )

    fun searchGuides(query: String): List<TriageGuideItem> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return allGuides

        return allGuides.filter { item ->
            item.title.lowercase().contains(q) ||
                    item.summary.lowercase().contains(q) ||
                    item.category.lowercase().contains(q) ||
                    item.doSteps.any { it.lowercase().contains(q) } ||
                    item.doNotSteps.any { it.lowercase().contains(q) } ||
                    item.multiLangTranslations.values.any { it.lowercase().contains(q) }
        }
    }
}
