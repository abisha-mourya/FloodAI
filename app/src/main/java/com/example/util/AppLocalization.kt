package com.example.util

object AppLocalization {

    val supportedLanguages = listOf(
        LanguageOption("en", "English", "English"),
        LanguageOption("ta", "Tamil", "தமிழ்"),
        LanguageOption("te", "Telugu", "తెలుగు"),
        LanguageOption("kn", "Kannada", "ಕನ್ನಡ"),
        LanguageOption("ml", "Malayalam", "മലയാളം"),
        LanguageOption("hi", "Hindi", "हिन्दी")
    )

    data class LanguageOption(val code: String, val englishName: String, val nativeName: String)

    private val translations = mapOf(
        // Tabs
        "tab_sos" to mapOf(
            "en" to "SOS Alert",
            "ta" to "அவசர எச்சரிக்கை",
            "te" to "అత్యవసర హెచ్చరిక",
            "kn" to "ತುರ್ತು ಎಚ್ಚರಿಕೆ",
            "ml" to "അടിയന്തര സന്ദേശം",
            "hi" to "आपातकालीन चेतावनी"
        ),
        "tab_map" to mapOf(
            "en" to "Offline Map",
            "ta" to "ஆஃப்லைன் வரைபடம்",
            "te" to "ఆఫ్‌లైన్ మ్యాప్",
            "kn" to "ಆಫ್‌ಲೈನ್ ನಕ್ಷೆ",
            "ml" to "ഓഫ്‌ലൈൻ ഭൂപടം",
            "hi" to "ऑफ़लाइन मानचित्र"
        ),
        "tab_triage" to mapOf(
            "en" to "Protocols",
            "ta" to "நெறிமுறைகள்",
            "te" to "ప్రోటోకాల్స్",
            "kn" to "ನಿಯಮಾವಳಿಗಳು",
            "ml" to "മാർഗ്ഗരേഖകൾ",
            "hi" to "बचाव नियम"
        ),
        "tab_supplies" to mapOf(
            "en" to "Supplies",
            "ta" to "பொருட்கள்",
            "te" to "సామాగ్రి",
            "kn" to "ಸಾಮಗ್ರಿಗಳು",
            "ml" to "സാമഗ്രികൾ",
            "hi" to "राहत सामग्री"
        ),
        "tab_status" to mapOf(
            "en" to "Status",
            "ta" to "நிலை பகிர்வு",
            "te" to "స్థితి సమాచారం",
            "kn" to "ಸ್ಥಿತಿ ಹಂಚಿಕೆ",
            "ml" to "സ്ഥിതിവിവരം",
            "hi" to "स्थिति रिपोर्ट"
        ),

        // Header
        "header_title" to mapOf(
            "en" to "FLOODAID DISASTER MESH",
            "ta" to "ஃபிளட் எய்ட் அவசர வலைப்பின்னல்",
            "te" to "వరద సహాయక అత్యవసర వ్యవస్థ",
            "kn" to "ಪ್ರವಾಹ ನೆರವು ತುರ್ತು ಜಾಲ",
            "ml" to "ഫ്ലഡ് എയ്ഡ് ദുരന്ത രക്ഷാ ശൃംഖല",
            "hi" to "बाढ़ राहत आपातकालीन ग्रिड"
        ),
        "header_gps_active" to mapOf(
            "en" to "GPS Active",
            "ta" to "ஜிபிஎஸ் இயங்குகிறது",
            "te" to "జీపీఎస్ యాక్టివ్",
            "kn" to "ಜಿಪಿಎಸ್ ಸಕ್ರಿಯ",
            "ml" to "ജിപിഎസ് സജീവം",
            "hi" to "जीपीएस सक्रिय"
        ),
        "header_offline" to mapOf(
            "en" to "100% Offline Ready",
            "ta" to "100% ஆஃப்லைன் தயார்",
            "te" to "100% ఆఫ్‌లైన్ సిద్ధం",
            "kn" to "100% ಆಫ್‌ಲೈನ್ ಸಿದ್ಧ",
            "ml" to "100% ഓഫ്‌ലൈൻ സജ്ജം",
            "hi" to "100% ऑफ़लाइन तैयार"
        ),

        // SOS Screen
        "sos_beacon_title" to mapOf(
            "en" to "EMERGENCY RESCUE BEACON",
            "ta" to "அவசர மீட்பு சமிக்ஞை விளக்கு",
            "te" to "అత్యవసర రక్షణ బీకాన్",
            "kn" to "ತುರ್ತು ರಕ್ಷಣಾ ಬೀಕನ್",
            "ml" to "അടിയന്തര രക്ഷാ സിഗ്നൽ",
            "hi" to "आपातकालीन बचाव बीकन"
        ),
        "sos_strobe" to mapOf(
            "en" to "STROBE LIGHT",
            "ta" to "மின்னல் ஒளி",
            "te" to "స్ట్రోబ్ లైట్",
            "kn" to "ಮಿಂಚು ಬೆಳಕು",
            "ml" to "സ്ട്രോബ് ലൈറ്റ്",
            "hi" to "तेज चमकती लाइट"
        ),
        "sos_siren" to mapOf(
            "en" to "SIREN ALARM",
            "ta" to "கூச்சல் எச்சரிக்கை ஒலி",
            "te" to "సైరన్ అలారం",
            "kn" to "ಸೈರನ್ ಅಲಾರಾಂ",
            "ml" to "സൈറൺ അലാറം",
            "hi" to "खतरे का सायरन"
        ),
        "sos_fullscreen" to mapOf(
            "en" to "FULL SCREEN SOS",
            "ta" to "முழுத்திரை SOS",
            "te" to "పూర్తి స్క్రీన్ SOS",
            "kn" to "ಪೂರ್ಣ ಪರದೆ SOS",
            "ml" to "പൂർണ്ണ സ്‌ക്രീൻ SOS",
            "hi" to "फुल स्क्रीन एसओएस"
        ),
        "sos_water_level" to mapOf(
            "en" to "WATER LEVEL AT CURRENT LOCATION",
            "ta" to "தற்போதைய இடத்தில் நீர்மட்டம்",
            "te" to "ప్రస్తుత ప్రాంతంలో నీటి మట్టం",
            "kn" to "ಪ್ರಸ್ತುತ ಸ್ಥಳದಲ್ಲಿ ನೀರಿನ ಮಟ್ಟ",
            "ml" to "നിലവിലെ സ്ഥലത്തെ ജലനിരപ്പ്",
            "hi" to "वर्तमान स्थान पर पानी का स्तर"
        ),
        "sos_people_count" to mapOf(
            "en" to "PEOPLE REQUIRING EVACUATION",
            "ta" to "மீட்கப்பட வேண்டிய நபர்கள்",
            "te" to "రక్షించాల్సిన వ్యక్తుల సంఖ్య",
            "kn" to "ರಕ್ಷಿಸಬೇಕಾದ ಜನರ ಸಂಖ್ಯೆ",
            "ml" to "രക്ഷപ്പെടുത്തേണ്ട ആളുകളുടെ എണ്ണം",
            "hi" to "बचाव की आवश्यकता वाले लोग"
        ),
        "sos_medical_notes" to mapOf(
            "en" to "URGENT MEDICAL OR RELIEF NEEDS",
            "ta" to "அவசர மருத்துவ தேவை குறிப்புகள்",
            "te" to "అత్యవసర వైద్య లేదా సహాయ అవసరాలు",
            "kn" to "ತುರ್ತು ವೈದ್ಯಕೀಯ ನೆರವಿನ ವಿವರ",
            "ml" to "അടിയന്തര ചികിത്സാ ആവശ്യങ്ങൾ",
            "hi" to "आवश्यक दवाइयां या विशेष सहायता"
        ),
        "sos_dispatch_sms" to mapOf(
            "en" to "DISPATCH RESCUE SMS",
            "ta" to "மீட்பு எஸ்எம்எஸ் அனுப்பவும்",
            "te" to "రక్షణ ఎస్ఎమ్ఎస్ పంపండి",
            "kn" to "ರಕ್ಷಣಾ SMS ಕಳುಹಿಸಿ",
            "ml" to "രക്ഷാ എസ്എംഎസ് അയക്കുക",
            "hi" to "बचाव एसएमएस भेजें"
        ),
        "sos_copy" to mapOf(
            "en" to "COPY",
            "ta" to "நகலெடு",
            "te" to "కాపీ",
            "kn" to "ನಕಲಿಸು",
            "ml" to "പകർപ്പ്",
            "hi" to "कॉपी करें"
        ),
        "sos_hotlines_title" to mapOf(
            "en" to "DIRECT EMERGENCY HOTLINES (INDIA)",
            "ta" to "நேரடி அவசர உதவி எண்கள் (இந்தியா)",
            "te" to "ప్రత్యక్ష అత్యవసర నంబర్లు (భారతదేశం)",
            "kn" to "ನೇರ ತುರ್ತು ಸಹಾಯವಾಣಿಗಳು (ಭಾರತ)",
            "ml" to "അടിയന്തര സഹായ നമ്പറുകൾ (ഇന്ത്യ)",
            "hi" to "सीधे आपातकालीन हेल्पलाइन नंबर (भारत)"
        ),

        // Map Screen
        "map_download_pack" to mapOf(
            "en" to "Download 5km Pack",
            "ta" to "5 கி.மீ வரைபடம் பதிவிறக்கு",
            "te" to "5 కి.మీ మ్యాప్ డౌన్‌లోడ్",
            "kn" to "5 ಕಿ.ಮೀ ನಕ್ಷೆ ಡೌನ್‌ಲೋಡ್",
            "ml" to "5 കി.മീ മാപ്പ് ഡൗൺലോഡ്",
            "hi" to "5 किमी मैप पैक डाउनलोड करें"
        ),
        "map_custom_pin" to mapOf(
            "en" to "Add Custom Pin",
            "ta" to "புதிய இடத்தை சேர்க்கவும்",
            "te" to "కొత్త పిన్ జోడించండి",
            "kn" to "ಹೊಸ ಸ್ಥಳ ಸೇರಿಸಿ",
            "ml" to "പുതിയ സ്ഥലം അടയാളപ്പെടുത്തുക",
            "hi" to "कस्टम पिन जोड़ें"
        ),
        "map_safe_points_title" to mapOf(
            "en" to "SAVED SAFE ZONES & PINS",
            "ta" to "சேமிக்கப்பட்ட பாதுகாப்பான இடங்கள்",
            "te" to "భద్రమైన ప్రాంతాలు & పిన్లు",
            "kn" to "ಉಳಿಸಲಾದ ಸುರಕ್ಷಿತ ತಾಣಗಳು",
            "ml" to "സുരക്ഷിത കേന്ദ്രങ്ങൾ",
            "hi" to "सुरक्षित आश्रय स्थल और पिन"
        ),
        "map_no_pins" to mapOf(
            "en" to "No custom refuge pins yet. Tap anywhere on the map or click 'Add Custom Pin' to save safe spots.",
            "ta" to "பாதுகாப்பான இடங்கள் எதுவும் இல்லை. வரைபடத்தில் தொட்டு பாதுகாப்பான இடத்தை குறிக்கவும்.",
            "te" to "ఇంకా పిన్లు లేవు. మ్యాప్‌పై ట్యాప్ చేసి భద్రమైన ప్రాంతాన్ని సేవ్ చేయండి.",
            "kn" to "ಇನ್ನೂ ಯಾವುದೇ ತಾಣಗಳಿಲ್ಲ. ನಕ್ಷೆಯ ಮೇಲೆ ಒತ್ತಿ ಸುರಕ್ಷಿತ ಸ್ಥಳವನ್ನು ಗುರುತಿಸಿ.",
            "ml" to "കേന്ദ്രങ്ങൾ അടയാളപ്പെടുത്തിയിട്ടില്ല. ഭൂപടത്തിൽ തൊട്ട് പുതിയ സുരക്ഷിത സ്ഥലം ചേർക്കുക.",
            "hi" to "कोई पिन नहीं है। सुरक्षित स्थान चिह्नित करने के लिए मानचित्र पर टैप करें।"
        ),
        "map_navigate" to mapOf(
            "en" to "NAVIGATE",
            "ta" to "வழிசெலுத்து",
            "te" to "నావిగేట్",
            "kn" to "ಮಾರ್ಗದರ್ಶನ",
            "ml" to "വഴി കാണിക്കുക",
            "hi" to "रास्ता दिखाएं"
        ),
        "map_stop_nav" to mapOf(
            "en" to "STOP NAVIGATION",
            "ta" to "வழிகாட்டுதலை நிறுத்து",
            "te" to "నావిగేషన్ ఆపండి",
            "kn" to "ಮಾರ್ಗ ನಿಲ್ಲಿಸಿ",
            "ml" to "നിർത്തുക",
            "hi" to "नेविगेशन रोकें"
        ),

        // Triage Protocols
        "triage_title" to mapOf(
            "en" to "DISASTER SURVIVAL PROTOCOLS",
            "ta" to "பேரிடர் உயிர் பாதுகாப்பு நெறிமுறைகள்",
            "te" to "విపత్తు జీవన రక్షణ ప్రోటోకాల్స్",
            "kn" to "ವಿಪತ್ತು ರಕ್ಷಣಾ ನಿಯಮಾವಳಿಗಳು",
            "ml" to "ദുരന്ത അതിജീവന മാർഗ്ഗരേഖകൾ",
            "hi" to "आपदा जीवन रक्षा नियम"
        ),
        "triage_search_placeholder" to mapOf(
            "en" to "Search emergency hazard or protocol...",
            "ta" to "ஆபத்து அல்லது வழிகாட்டலை தேடவும்...",
            "te" to "ప్రమాదం లేదా సూచనలను వెతకండి...",
            "kn" to "ಅಪಾಯ ಅಥವಾ ನಿಯಮಗಳನ್ನು ಹುಡುಕಿ...",
            "ml" to "രക്ഷാ മാർഗ്ഗങ്ങൾ തിരയുക...",
            "hi" to "खतरे या प्राथमिक उपचार की खोज करें..."
        ),
        "triage_dos" to mapOf(
            "en" to "ACTION PROTOCOL (DO'S)",
            "ta" to "செய்ய வேண்டியவை (DO'S)",
            "te" to "చేయవలసినవి (DO'S)",
            "kn" to "ಮಾಡಬೇಕಾದ ಕ್ರಮಗಳು (DO'S)",
            "ml" to "ചെയ്യേണ്ട കാര്യങ്ങൾ (DO'S)",
            "hi" to "क्या करें (DO'S)"
        ),
        "triage_donts" to mapOf(
            "en" to "LETHAL MISTAKES (DO NOTS)",
            "ta" to "செய்யக்கூடாதவை (DO NOTS)",
            "te" to "చేయకూడని ప్రమాదాలు (DO NOTS)",
            "kn" to "ಮಾಡಬಾರದ ತಪ್ಪುಗಳು (DO NOTS)",
            "ml" to "ചെയ്യരുതാത്ത കാര്യങ്ങൾ (DO NOTS)",
            "hi" to "क्या न करें (DO NOTS)"
        ),

        // Supplies Log
        "supplies_title" to mapOf(
            "en" to "72-HOUR DISASTER SURVIVAL KIT",
            "ta" to "72 மணி நேர அவசர உயிர் காக்கும் பெட்டி",
            "te" to "72 గంటల అత్యవసర సామాగ్రి కిట్",
            "kn" to "72 ಗಂಟೆಗಳ ತುರ್ತು ಜೀವ ರಕ್ಷಣಾ ಕಿಟ್",
            "ml" to "72 മണിക്കൂർ അതിജീവന കിറ്റ്",
            "hi" to "72 घंटे की आपातकालीन किट"
        ),
        "supplies_packed" to mapOf(
            "en" to "Items Packed",
            "ta" to "சேகரிக்கப்பட்ட பொருட்கள்",
            "te" to "సిద్ధంగా ఉన్న వస్తువులు",
            "kn" to "ಸಿದ್ಧವಿರುವ ಸಾಮಗ್ರಿಗಳು",
            "ml" to "തയ്യാറായ സാധനങ്ങൾ",
            "hi" to "पैक की गई सामग्री"
        ),
        "supplies_add_item" to mapOf(
            "en" to "Add Custom Item",
            "ta" to "புதிய பொருள் சேர்க்க",
            "te" to "కొత్త వస్తువు జోడించు",
            "kn" to "ಹೊಸ ವಸ್ತು ಸೇರಿಸಿ",
            "ml" to "പുതിയ സാധനം ചേർക്കുക",
            "hi" to "नई वस्तु जोड़ें"
        ),

        // Status Sender
        "status_title" to mapOf(
            "en" to "PEOPLE STATUS SENDER",
            "ta" to "நபர்கள் நிலை பகிர்வு",
            "te" to "వ్యక్తుల స్థితి సమాచార వ్యవస్థ",
            "kn" to "ವ್ಯಕ್ತಿಗಳ ಸ್ಥಿತಿ ರವಾನೆ",
            "ml" to "വ്യക്തിഗത സ്ഥിതി വിവര വിതരണം",
            "hi" to "लोगों की स्थिति रिपोर्ट"
        ),
        "status_how_many" to mapOf(
            "en" to "HOW MANY PEOPLE WITH YOU?",
            "ta" to "உங்களுடன் எத்தனை நபர்கள் உள்ளனர்?",
            "te" to "మీతో ఎంతమంది వ్యక్తులు ఉన్నారు?",
            "kn" to "ನಿಮ್ಮೊಂದಿಗೆ ಎಷ್ಟು ಜನರಿದ್ದಾರೆ?",
            "ml" to "നിങ്ങളോടൊപ്പം എത്ര ആളുകളുണ്ട്?",
            "hi" to "आपके साथ कितने लोग हैं?"
        ),
        "status_fine" to mapOf(
            "en" to "Fine / Safe",
            "ta" to "பாதுகாப்பாக உள்ளோம்",
            "te" to "సురక్షితంగా ఉన్నాము",
            "kn" to "ಸುರಕ್ಷಿತವಾಗಿದ್ದೇವೆ",
            "ml" to "സുരക്ഷിതമാണ്",
            "hi" to "सुरक्षित / ठीक हैं"
        ),
        "status_injured" to mapOf(
            "en" to "Injured",
            "ta" to "காயமடைந்துள்ளார்",
            "te" to "గాయపడ్డారు",
            "kn" to "ಗಾಯಗೊಂಡಿದ್ದಾರೆ",
            "ml" to "പരിക്കേറ്റു",
            "hi" to "घायल हैं"
        ),
        "status_critical" to mapOf(
            "en" to "Critical / Emergency",
            "ta" to "அபாயகரமான நிலை",
            "te" to "తీవ్రమైన ప్రమాదం",
            "kn" to "ಗಂಭೀರ ಸ್ಥಿತಿ",
            "ml" to "ഗുരുതരാവസ്ഥ",
            "hi" to "गंभीर स्थिति"
        ),
        "status_trapped" to mapOf(
            "en" to "Trapped in Water",
            "ta" to "நீரில் சிக்கியுள்ளனர்",
            "te" to "నీటిలో చిక్కుకున్నారు",
            "kn" to "ನೀರಿನಲ್ಲಿ ಸಿಲುಕಿದ್ದಾರೆ",
            "ml" to "വെള്ളത്തിൽ കുടുങ്ങി",
            "hi" to "पानी में फंसे हैं"
        ),
        "status_meds" to mapOf(
            "en" to "Needs Medicine",
            "ta" to "மருந்து தேவை",
            "te" to "మందులు అవసరం",
            "kn" to "ಔಷಧಿ ಬೇಕಾಗಿದೆ",
            "ml" to "മരുന്ന് ആവശ്യം",
            "hi" to "दवाई की सख्त जरूरत"
        ),
        "status_send_btn" to mapOf(
            "en" to "SEND SMS (8384034810)",
            "ta" to "எஸ்எம்எஸ் அனுப்பவும் (8384034810)",
            "te" to "ఎస్ఎమ్ఎస్ పంపండి (8384034810)",
            "kn" to "SMS ಕಳುಹಿಸಿ (8384034810)",
            "ml" to "എസ്എംഎസ് അയക്കുക (8384034810)",
            "hi" to "एसएमएस भेजें (8384034810)"
        )
    )

    fun getString(key: String, langCode: String): String {
        val entry = translations[key] ?: return key
        return entry[langCode] ?: entry["en"] ?: key
    }
}
